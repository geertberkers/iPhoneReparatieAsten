package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

public class ColorTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_test);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        int resourceColor = intent.getExtras().getInt("color");
        initControls(resourceColor);
    }

    private void initControls(int resourceColor) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lcdColor);
        linearLayout.setBackgroundResource(resourceColor);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
    }
}
