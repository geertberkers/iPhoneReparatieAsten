package geert.berkers.iphonereparatieasten.activitytest;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import geert.berkers.iphonereparatieasten.FingerprintHandler;
import geert.berkers.iphonereparatieasten.Information;
import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
@SuppressWarnings("FieldCanBeLocal")
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintTestActivity extends AppCompatActivity {


    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean incorrectFingerTested;
    private boolean fingerprintIsWorking;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.fingerprint_scanner));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_fingerprint);
        txtQuestion.setText(R.string.info_question_test_fingerprint);

        ImageView imageView = new ImageView(this);
        //imageView.setPadding(100, 100, 100, 100);
        imageView.setImageResource(R.drawable.fingerprint_untested);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.removeAllViews();
        frame.addView(imageView);

        fabNotWorking = (FloatingActionButton) findViewById(R.id.fabNotWorking);
        fabNotWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotWorking();
            }
        });

        fabWorking = (FloatingActionButton) findViewById(R.id.fabWorking);
        fabWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWorking();
            }
        });

        initFingerprintManager();

        fabWorking.setVisibility(View.GONE);
        fingerprintIsWorking = false;
        incorrectFingerTested = false;
    }

    private void initFingerprintManager() {
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if (!keyguardManager.isKeyguardSecure()) {
            txtQuestion.setText(R.string.no_lockscreen);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            txtQuestion.setText(R.string.no_permission_fingerprint);
            return;
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            txtQuestion.setText(R.string.no_fingerprints);
            return;
        }

        generateKey();

        if (cipherInit()) {
            cryptoObject = new FingerprintManager.CryptoObject(cipher);
            FingerprintHandler helper = new FingerprintHandler(this);
            helper.startAuth(fingerprintManager, cryptoObject);
        }
    }

    private void generateKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new RuntimeException("Failed to get KeyGenerator instance", e);
            }

            try {
                keyStore.load(null);
                keyGenerator.init(new KeyGenParameterSpec.Builder(Information.KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(Information.KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private void isNotWorking() {
        fingerprintIsWorking = false;
        setResult();
    }

    private void isWorking() {
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("fingerprintIsWorking", fingerprintIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    public void setAuthenticationFailed() {
        incorrectFingerTested = true;
        txtQuestion.setText(R.string.info_first_question_test_fingerprint);
    }

    public void setAuthenticationSucceeded() {

        if (!incorrectFingerTested) {
            Toast.makeText(this, R.string.toast_working_finger, Toast.LENGTH_SHORT).show();
            initFingerprintManager();
        } else {
            fingerprintIsWorking = true;
            fabWorking.setVisibility(View.VISIBLE);
            fabNotWorking.setVisibility(View.GONE);
            txtQuestion.setText(R.string.result_test_fingerprint);
        }
    }
}