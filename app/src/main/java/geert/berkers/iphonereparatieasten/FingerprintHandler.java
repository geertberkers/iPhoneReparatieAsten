package geert.berkers.iphonereparatieasten;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import geert.berkers.iphonereparatieasten.activitytest.FingerprintTestActivity;

/**
 * Created by Geert.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private FingerprintTestActivity appContext;

    public FingerprintHandler(FingerprintTestActivity context) {
        appContext = context;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Toast.makeText(appContext, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(appContext, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        appContext.setAuthenticationFailed();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        appContext.setAuthenticationSucceeded();
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(appContext, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }
}