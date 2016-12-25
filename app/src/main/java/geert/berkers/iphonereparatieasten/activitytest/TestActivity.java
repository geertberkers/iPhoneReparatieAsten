package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

public class TestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle("TestActivity");

    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

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
    }

    public void isNotWorking() {
        System.out.println("Camera is not working!");

        //TODO: Handle isNotWorking

    }

    public void isWorking() {
        System.out.println("Camera is working!");

        //TODO: Handle isWorking
    }

    private void setResult(){
        Intent intentMessage = new Intent();

        //TODO: Set what is working and what not!
        //intentMessage.putExtra("backCameraIsWorking", backCameraIsWorking);

        setResult(RESULT_OK, intentMessage);
        finish();
    }
}

