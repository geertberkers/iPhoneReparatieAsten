package geert.berkers.iphonereparatieasten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.iphonereparatieasten.enums.DeviceType;
import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.adapter.ModelAdapter;
import geert.berkers.iphonereparatieasten.listeners.ClickListener;
import geert.berkers.iphonereparatieasten.listeners.RecyclerTouchListener;
import geert.berkers.iphonereparatieasten.model.Model;

/**
 * Created by Geert.
 */

public class ModelsActivity extends AppCompatActivity {

    private List<Model> showModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        showModels = new ArrayList<>();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        List<Model> models = intent.getParcelableArrayListExtra("models");
        DeviceType deviceType = (DeviceType) intent.getSerializableExtra("deviceType");

        if (title != null) {
            setTitle(title + " Repareren");
        }

        if (models != null && models.size() > 0) {
            for (Model model : models) {
                if (model.getDeviceType() == deviceType) {
                    showModels.add(model);

                    initRecyclerView();
                    // Debug info
                }
            }
        } else if (showModels != null) {
            initRecyclerView();
        } else {
            finish();
        }
    }

    private void initRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new ModelAdapter(showModels);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new ClickListener() {
                    @Override
                    public void onClick(int position) {
                        Model model = showModels.get(position);
                        Intent priceActivity = new Intent(ModelsActivity.this, PriceActivity.class);
                        priceActivity.putExtra("model", model);
                        startActivity(priceActivity);

                    }
                })
        );
    }
}
