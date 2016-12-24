package geert.berkers.iphonereparatieasten.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.iphonereparatieasten.DeviceType;
import geert.berkers.iphonereparatieasten.adapter.BrandAdapter;
import geert.berkers.iphonereparatieasten.clicklistener.ClickListener;
import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.clicklistener.RecyclerTouchListener;
import geert.berkers.iphonereparatieasten.model.Brand;
import geert.berkers.iphonereparatieasten.model.Model;
import geert.berkers.iphonereparatieasten.model.Reparatie;

public class MainActivity extends AppCompatActivity {

    private final static int CALL_PERMISSION = 123;
    private final static int PICK_DEVICE_TYPE = 999;

    private final static String PHONE_NUMBER = "+31646830525";

    private List<Brand> brandList;
    private Brand selectedBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brandList = getBrandsList();

        initControls();
    }

    private void initControls() {
        createAndSetToolbar();
        createFloatingActionButton();
        initBrandRecyclerView();
    }

    private void initBrandRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new BrandAdapter(brandList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new ClickListener() {
                    @Override
                    public void onClick(int position) {
                        selectedBrand = brandList.get(position);
                        System.out.println(selectedBrand.getBrand());

                        if (selectedBrand.getDeviceTypes().size() > 1) {
                            Intent pickDeviceType = new Intent(MainActivity.this, DeviceTypeListActivity.class);
                            pickDeviceType.putExtra("brand", selectedBrand);
                            startActivityForResult(pickDeviceType, PICK_DEVICE_TYPE);
                        } else {
                            showModels(selectedBrand, DeviceType.MOBILE_PHONE, "Mobiele Telefoon");
                        }
                    }
                })
        );
    }

    private void showModels(Brand brand, DeviceType mobilePhone, String title) {
        Intent modelIntent = new Intent(MainActivity.this, ModelsActivity.class);
        modelIntent.putExtra("title", title);
        modelIntent.putExtra("deviceType", mobilePhone);
        modelIntent.putParcelableArrayListExtra("models", (ArrayList<? extends Parcelable>) brand.getModels());
        startActivity(modelIntent);
    }

    private void createAndSetToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
    }

    private void createFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCall();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            createAboutUsDialog().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case CALL_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    createCallAlertDialog().show();
                }
                break;
            default:
                break;
        }
    }

    private void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + PHONE_NUMBER)));
        }
    }

    private AlertDialog.Builder createCallAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Bellen mislukt");
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_call);
        alertDialogBuilder.setMessage("Geen permissies toegestaan om te bellen.");
        alertDialogBuilder.setPositiveButton("Geef rechten", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCall();
            }
        });
        alertDialogBuilder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return alertDialogBuilder;
    }

    private AlertDialog.Builder createAboutUsDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Over ons");
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setMessage("iPhone Reparatie Asten\nBurgemeester Wijnenstraat 17\n5721 AG ASTEN");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        return alertDialogBuilder;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_DEVICE_TYPE) {
            if (resultCode == RESULT_OK && data != null) {
                String result = data.getStringExtra("deviceType");
                DeviceType deviceType;

                switch (result) {
                    case "iPhone":
                        deviceType = DeviceType.IPHONE;
                        break;
                    case "iPad":
                        deviceType = DeviceType.IPAD;
                        break;
                    case "iPod":
                        deviceType = DeviceType.IPOD;
                        break;
                    case "Mobiele Telefoon":
                        deviceType = DeviceType.MOBILE_PHONE;
                        break;
                    case "Tablet":
                        deviceType = DeviceType.TABLET;
                        break;
                    default:
                        deviceType = DeviceType.MOBILE_PHONE;
                }

                showModels(selectedBrand, deviceType, result);
            }
        }
    }

    //TODO: Add other brands (And all missing from current)
    private List<Brand> getBrandsList() {
        List<Brand> allBrands = new ArrayList<>();

        allBrands.add(createAppleBrand());
        allBrands.add(createBlackBerryBrand());
        //allBrands.add(createHTCBrand());
        //allBrands.add(createHuaweiBrand());
        //allBrands.add(createBlackViewBrand());
        //allBrands.add(createLGBrand());
        //allBrands.add(createNokiaBrand());
        allBrands.add(createOnePlusBrand());
        allBrands.add(createSamsungBrand());
        //allBrands.add(createSonyBrand());

        return allBrands;
    }

    private Brand createAppleBrand() {
        Brand apple = new Brand("Apple", R.drawable.apple_logo);
        apple.setDeviceType("iPhone");
        apple.setDeviceType("iPad");
        apple.setDeviceType("iPod");

        Model iPhone7 = new Model("iPhone 7", R.drawable.iphone_7, DeviceType.IPHONE);
        Model iPhone6SPlus = new Model("iPhone 6S Plus", R.drawable.iphone_6s_plus, DeviceType.IPHONE);
        Model iPhone6S = new Model("iPhone 6S", R.drawable.iphone_6s, DeviceType.IPHONE);
        Model iPhone6Plus = new Model("iPhone 6 Plus", R.drawable.iphone_6_plus, DeviceType.IPHONE);
        Model iPhone6 = new Model("iPhone 6", R.drawable.iphone_6, DeviceType.IPHONE);
        Model iPhone5C = new Model("iPhone 5C", R.drawable.iphone_5c, DeviceType.IPHONE);
        Model iPhone5SE = new Model("iPhone 5SE", R.drawable.iphone_5se, DeviceType.IPHONE);
        Model iPhone5S = new Model("iPhone 5S", R.drawable.iphone_5s, DeviceType.IPHONE);
        Model iPhone5G = new Model("iPhone 5G", R.drawable.iphone_5g, DeviceType.IPHONE);
        Model iPhone4S = new Model("iPhone 4S", R.drawable.iphone_4s, DeviceType.IPHONE);
        Model iPhone4G = new Model("iPhone 4G", R.drawable.iphone_4g, DeviceType.IPHONE);

        iPhone7.addReparatieList(createReparaties());
        iPhone6SPlus.addReparatieList(createReparaties());
        iPhone6S.addReparatieList(createReparaties());
        iPhone6Plus.addReparatieList(createReparaties());
        iPhone6.addReparatieList(createReparaties());
        iPhone5C.addReparatieList(createReparaties());
        iPhone5SE.addReparatieList(createReparaties());
        iPhone5S.addReparatieList(createReparaties());
        iPhone5G.addReparatieList(createReparaties());
        iPhone4S.addReparatieList(createReparaties());
        iPhone4G.addReparatieList(createReparaties());

        apple.addModel(iPhone7);
        apple.addModel(iPhone6SPlus);
        apple.addModel(iPhone6S);
        apple.addModel(iPhone6Plus);
        apple.addModel(iPhone6);
        apple.addModel(iPhone5C);
        apple.addModel(iPhone5SE);
        apple.addModel(iPhone5S);
        apple.addModel(iPhone5G);
        apple.addModel(iPhone4S);
        apple.addModel(iPhone4G);

        Model iPadAir2 = new Model("iPad Air 2", R.drawable.ipad_air_2, DeviceType.IPAD);
        Model iPadAir = new Model("iPad Air", R.drawable.ipad_air, DeviceType.IPAD);
        Model iPadMini3 = new Model("iPad Mini 3", R.drawable.ipad_mini_3, DeviceType.IPAD);
        Model iPadMini2 = new Model("iPad Mini 2", R.drawable.ipad_mini_2, DeviceType.IPAD);
        Model iPadMini = new Model("iPad Mini", R.drawable.ipad_mini, DeviceType.IPAD);
        Model iPad4 = new Model("iPad 4", R.drawable.ipad_4, DeviceType.IPAD);
        Model iPad3 = new Model("iPad 3", R.drawable.ipad_3, DeviceType.IPAD);
        Model iPad2 = new Model("iPad 2", R.drawable.ipad_2, DeviceType.IPAD);


        iPadAir2.addReparatieList(createReparaties());
        iPadAir.addReparatieList(createReparaties());
        iPadMini3.addReparatieList(createReparaties());
        iPadMini2.addReparatieList(createReparaties());
        iPadMini.addReparatieList(createReparaties());
        iPad4.addReparatieList(createReparaties());
        iPad3.addReparatieList(createReparaties());
        iPad2.addReparatieList(createReparaties());

        apple.addModel(iPadAir2);
        apple.addModel(iPadAir);
        apple.addModel(iPadMini3);
        apple.addModel(iPadMini2);
        apple.addModel(iPadMini);
        apple.addModel(iPad4);
        apple.addModel(iPad3);
        apple.addModel(iPad2);

        Model iPod5 = new Model("iPod 5", R.drawable.ipod_5, DeviceType.IPOD);
        Model iPod4 = new Model("iPod 4", R.drawable.ipod_4, DeviceType.IPOD);

        iPod5.addReparatieList(createReparaties());
        iPod4.addReparatieList(createReparaties());

        apple.addModel(iPod5);
        apple.addModel(iPod4);

        return apple;
    }

    private Brand createBlackBerryBrand() {
        Brand blackBerry = new Brand("BlackBerry", R.drawable.blackberry_logo);
        blackBerry.setDeviceType("Mobiele Telefoon");

        Model blackBerryZ10 = new Model("BlackBerry Z10", R.drawable.blackberry_z10, DeviceType.MOBILE_PHONE);
        blackBerryZ10.addReparatieList(createReparaties());
        blackBerry.addModel(blackBerryZ10);

        return blackBerry;
    }

    @SuppressWarnings("unused")
    private Brand createHTCBrand() {
        Brand htc = new Brand("HTC", R.drawable.htc_logo);
        htc.setDeviceType("Mobiele Telefoon");
        return htc;
    }

    @SuppressWarnings("unused")
    private Brand createHuaweiBrand() {
        Brand huawei = new Brand("Huawei", R.drawable.huawei_logo);
        huawei.setDeviceType("Mobiele Telefoon");
        return huawei;
    }

    @SuppressWarnings("unused")
    private Brand createBlackViewBrand() {
        Brand blackview = new Brand("Blackview", R.drawable.blackview_logo);
        blackview.setDeviceType("Mobiele Telefoon");
        return blackview;
    }

    @SuppressWarnings("unused")
    private Brand createLGBrand() {
        Brand lg = new Brand("LG", R.drawable.lg_logo);
        lg.setDeviceType("Mobiele Telefoon");
        lg.setDeviceType("Tablet");
        return lg;
    }

    @SuppressWarnings("unused")
    private Brand createNokiaBrand() {
        Brand nokia = new Brand("Nokia", R.drawable.nokia_logo);
        nokia.setDeviceType("Mobiele Telefoon");
        return nokia;
    }

    private Brand createOnePlusBrand() {
        Brand oneplus = new Brand("OnePlus", R.drawable.oneplus_logo);
        oneplus.setDeviceType("Mobiele Telefoon");

        Model onePlusOne = new Model("OnePlus One", R.drawable.oneplus_one, DeviceType.MOBILE_PHONE);
        onePlusOne.addReparatieList(createReparaties());
        oneplus.addModel(onePlusOne);

        Model onePlus2 = new Model("OnePlus 2", R.drawable.oneplus_2, DeviceType.MOBILE_PHONE);
        onePlus2.addReparatieList(createReparaties());
        oneplus.addModel(onePlus2);

        Model onePlusX = new Model("OnePlus X", R.drawable.oneplus_x, DeviceType.MOBILE_PHONE);
        onePlusX.addReparatieList(createReparaties());
        oneplus.addModel(onePlusX);

        return oneplus;
    }

    private Brand createSamsungBrand() {
        Brand samsung = new Brand("Samsung", R.drawable.samsung_logo);
        samsung.setDeviceType("Mobiele Telefoon");
        samsung.setDeviceType("Tablet");

        // Samsung Telefoons
        Model samsungS7Edge = new Model("Samsung Galaxy S7 Edge", R.drawable.samsung_s7_edge, DeviceType.MOBILE_PHONE);
        Model samsungS7 = new Model("Samsung Galaxy S7", R.drawable.samsung_s7, DeviceType.MOBILE_PHONE);
        Model samsungS6EdgePlus = new Model("Samsung Galaxy S6 Edge Plus", R.drawable.samsung_s6_egde_plus, DeviceType.MOBILE_PHONE);
        Model samsung6Edge = new Model("Samsung Galaxy S6 Edge", R.drawable.samsung_s6_edge, DeviceType.MOBILE_PHONE);
        Model samsungS6 = new Model("Samsung Galaxy S6", R.drawable.samsung_s6, DeviceType.MOBILE_PHONE);
        Model samsungS5Neo = new Model("Samsung Galaxy S5 Neo", R.drawable.samsung_s5_neo, DeviceType.MOBILE_PHONE);
        Model samsungS5G900F = new Model("Samsung Galaxy S5 G900F", R.drawable.samsung_s5_g900f, DeviceType.MOBILE_PHONE);
        Model samsungS5Mini = new Model("Samsung Galaxy S5 Mini", R.drawable.samsung_s5_mini, DeviceType.MOBILE_PHONE);
        Model samsungS5Active = new Model("Samsung Galaxy S5 Active", R.drawable.samsung_s5_active, DeviceType.MOBILE_PHONE);
        Model samsungS4 = new Model("Samsung Galaxy S4", R.drawable.samsung_s4, DeviceType.MOBILE_PHONE);
        Model samsungS4Mini = new Model("Samsung Galaxy S4 Mini", R.drawable.samsung_s4_mini, DeviceType.MOBILE_PHONE);
        Model samsungS3 = new Model("Samsung Galaxy S3", R.drawable.samsung_s3, DeviceType.MOBILE_PHONE);
        Model samsungS3Neo = new Model("Samsung Galaxy S3 Neo", R.drawable.samsung_s3_neo, DeviceType.MOBILE_PHONE);
        Model samsungS3Mini = new Model("Samsung Galaxy S3 Mini", R.drawable.samsung_s3_mini, DeviceType.MOBILE_PHONE);
        Model samsungS2 = new Model("Samsung Galaxy S2", R.drawable.samsung_s2, DeviceType.MOBILE_PHONE);
        Model samsungS2Plus = new Model("Samsung Galaxy S2 Plus", R.drawable.samsung_s2_plus, DeviceType.MOBILE_PHONE);

        samsungS7Edge.addReparatieList(createReparaties());
        samsungS7.addReparatieList(createReparaties());
        samsungS6EdgePlus.addReparatieList(createReparaties());
        samsung6Edge.addReparatieList(createReparaties());
        samsungS6.addReparatieList(createReparaties());
        samsungS5Neo.addReparatieList(createReparaties());
        samsungS5G900F.addReparatieList(createReparaties());
        samsungS5Mini.addReparatieList(createReparaties());
        samsungS5Active.addReparatieList(createReparaties());
        samsungS4.addReparatieList(createReparaties());
        samsungS4Mini.addReparatieList(createReparaties());
        samsungS3.addReparatieList(createReparaties());
        samsungS3Neo.addReparatieList(createReparaties());
        samsungS3Mini.addReparatieList(createReparaties());
        samsungS2.addReparatieList(createReparaties());
        samsungS2Plus.addReparatieList(createReparaties());

        samsung.addModel(samsungS7Edge);
        samsung.addModel(samsungS7);
        samsung.addModel(samsungS6EdgePlus);
        samsung.addModel(samsung6Edge);
        samsung.addModel(samsungS6);
        samsung.addModel(samsungS5Neo);
        samsung.addModel(samsungS5G900F);
        samsung.addModel(samsungS5Mini);
        samsung.addModel(samsungS5Active);
        samsung.addModel(samsungS4);
        samsung.addModel(samsungS4Mini);
        samsung.addModel(samsungS3);
        samsung.addModel(samsungS3Neo);
        samsung.addModel(samsungS3Mini);
        samsung.addModel(samsungS2);
        samsung.addModel(samsungS2Plus);

        // Samsung Tablets
        Model samsungTabS2 = new Model("Samsung Tab S2 T810", R.drawable.samsung_tab_s2, DeviceType.TABLET);
        Model samsungTab4T530 = new Model("Samsung Tab 4 T530", R.drawable.samsung_tab_4_t530, DeviceType.TABLET);
        Model samsungTab4T23x = new Model("Samsung Tab 4 T230/T231", R.drawable.samsung_tab_4_t23x, DeviceType.TABLET);
        Model samsungTab3 = new Model("Samsung Tab 3 P5200/P5210", R.drawable.samsung_tab_3, DeviceType.TABLET);
        Model samsungTab2_p51xx = new Model("Samsung Tab 2 P5100/P5110", R.drawable.samsung_tab_2_p51xx, DeviceType.TABLET);
        Model samsungTab2_p3110 = new Model("Samsung Tab 2 P3110", R.drawable.samsung_tab_2_p3110, DeviceType.TABLET);
        Model samsungTab = new Model("Samsung Tab P7300/P7310", R.drawable.samsung_tab_p73xx, DeviceType.TABLET);

        samsungTabS2.addReparatieList(createReparaties());
        samsungTab4T530.addReparatieList(createReparaties());
        samsungTab4T23x.addReparatieList(createReparaties());
        samsungTab3.addReparatieList(createReparaties());
        samsungTab2_p51xx.addReparatieList(createReparaties());
        samsungTab2_p3110.addReparatieList(createReparaties());
        samsungTab.addReparatieList(createReparaties());

        samsung.addModel(samsungTabS2);
        samsung.addModel(samsungTab4T530);
        samsung.addModel(samsungTab4T23x);
        samsung.addModel(samsungTab3);
        samsung.addModel(samsungTab2_p51xx);
        samsung.addModel(samsungTab2_p3110);
        samsung.addModel(samsungTab);

        return samsung;
    }

    @SuppressWarnings("unused")
    private Brand createSonyBrand() {
        Brand sony = new Brand("Sony", R.drawable.sony_logo);
        sony.setDeviceType("Mobiele Telefoon");
        return sony;
    }

    private List<Reparatie> createReparaties() {
        ArrayList<Reparatie> reparaties = new ArrayList<>();

        reparaties.add(new Reparatie("Scherm", 150.00));
        reparaties.add(new Reparatie("Batterij", 50.00));
        reparaties.add(new Reparatie("Waterschadebehandeling", 35.00));
        reparaties.add(new Reparatie("Onderzoekskosten", 15.00));
        reparaties.add(new Reparatie("Software", 25.00));

        return reparaties;
    }

    private List<Brand> getBrandsFromInternet() {
        List<Brand> brands = new ArrayList<>();


        return brands;
    }
}
