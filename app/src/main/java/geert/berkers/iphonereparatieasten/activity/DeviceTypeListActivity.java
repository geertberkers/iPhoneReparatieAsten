package geert.berkers.iphonereparatieasten.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.model.Brand;

/**
 * Created by Geert.
 */

public class DeviceTypeListActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Brand brand = getIntent().getParcelableExtra("brand");
        if (brand != null) {
            initToolbar(brand);

            List<String> listValues = new ArrayList<>();
            for (String deviceType : brand.getDeviceTypes()) {
                listValues.add(deviceType);
            }

            ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listValues);

            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                    String deviceType = (String) adapter.getItemAtPosition(position);

                    Intent intentMessage = new Intent();
                    intentMessage.putExtra("deviceType", deviceType);
                    setResult(RESULT_OK, intentMessage);
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    private void initToolbar(Brand brand) {
        setTitle(brand.getBrand() + " Reparaties");

        /* Niet aan te raden icon hier weer te geven
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED, null);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
