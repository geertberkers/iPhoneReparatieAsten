package geert.berkers.iphonereparatieasten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.List;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.adapter.ReparatieAdapter;
import geert.berkers.iphonereparatieasten.model.Model;
import geert.berkers.iphonereparatieasten.model.Reparatie;

/**
 * Created by Geert.
 */
public class PriceActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        Intent intent = getIntent();
        Model model = intent.getParcelableExtra("model");

        if (model != null) {
            setTitle(model.getModelName());

            ImageView modelImage = (ImageView) findViewById(R.id.modelImage);
            modelImage.setImageResource(model.getModelImage());
            modelImage.setContentDescription(model.getModelName());

            initRecyclerView(model.getReparaties());
        } else {
            finish();
        }
    }

    private void initRecyclerView(List<Reparatie> reparaties) {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new ReparatieAdapter(reparaties);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
