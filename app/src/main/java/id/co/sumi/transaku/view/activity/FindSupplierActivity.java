package id.co.sumi.transaku.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.SupplierModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SearchSupplierMvpView;
import id.co.sumi.transaku.presenter.SearchSupplierPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.view.adapters.SupplierSearchAdapter;

/**
 * Created by gebriani on 14/03/17.
 */

public class FindSupplierActivity extends ParentActivity implements View.OnClickListener,
        SearchSupplierMvpView, LocationListener{

    private Toolbar toolbar;
    private TextView toolbar_title;
    private EditText et_keyword;
    private Spinner spin_search_by;
    private Spinner spin_order_by;
    private Button bt_search;
    private RecyclerView rv_result;
    private ProgressBar progressBar;
    private SearchSupplierPresenter presenter;
    private SupplierSearchAdapter adapter;
    private String latitude_str, longitude_str = "";
    private List<SupplierModel> supplierModelList = new ArrayList<>();

    private LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private Location loc;

    //http://en.proft.me/2017/04/17/how-get-location-latitude-longitude-gps-android/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_supplier);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        et_keyword = (EditText) findViewById(R.id.frg_findSup_et_keyword);
        spin_search_by = (Spinner) findViewById(R.id.frg_findSup_spin_search_by);
        spin_order_by = (Spinner) findViewById(R.id.frg_findSup_spin_order_by);
        bt_search = (Button) findViewById(R.id.frg_findSup_bt_search);
        rv_result = (RecyclerView) findViewById(R.id.find_supp_recycleview);
        progressBar = (ProgressBar) findViewById(R.id.find_supp_progressbar);
        rv_result.setLayoutManager(new LinearLayoutManager(rv_result.getContext()));
        bt_search.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("SUPPLIER");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new SupplierSearchAdapter(this, supplierModelList);
        rv_result.setAdapter(adapter);
        presenter = PresenterFactory.searchSupplierPresenter();
        presenter.attachView(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
        progressBar.setVisibility(View.VISIBLE);
        presenter.getAllActiveSupplier();

    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, FindSupplierActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_findSup_bt_search:
//                rv_result.setVisibility(View.GONE);
                if(latitude_str != null && longitude_str != null){
                    progressBar.setVisibility(View.VISIBLE);
                    latitude_str = latitude_str.replace(".",",");
                    longitude_str = longitude_str.replace(".",",");
                    Log.d("LATITUDE", latitude_str);
                    Log.d("LONGITUDE", longitude_str);
                    Bundle bundle = new Bundle();
                    bundle.putString("latitude", latitude_str);
                    bundle.putString("longitude", longitude_str);
                    if (spin_search_by.getSelectedItemPosition() == 0 && spin_order_by.getSelectedItemPosition() == 0) {
                        presenter.searchSupplier_BySupplierName_OrderByDistance(et_keyword.getText().toString(), latitude_str, longitude_str);
                        bundle.putString("search_by", "by_supplier_name");
                    } else if (spin_search_by.getSelectedItemPosition() == 1 && spin_order_by.getSelectedItemPosition() == 0) {
                        presenter.searchSupplier_ByInventoryName_OrderByDistance(et_keyword.getText().toString(), latitude_str, longitude_str);
                        bundle.putString("search_by", "by_inventory_name");
                    }
                    TransakuApplication.getInstance().getFirebaseAnalytics().logEvent("search_supplier", bundle);
                } else {
                    Toast.makeText(this, "Maaf lokasi anda tidak dapat di akses", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void showResponse(boolean isSuceess, String message) {
        progressBar.setVisibility(View.GONE);
        rv_result.setVisibility(View.GONE);
        Toast.makeText(this, "Maaf Supplier Yang Anda Cari Belum Tersedia", Toast.LENGTH_LONG).show();
    }


    @Override
    public void showResponse(boolean isSuceess, List<SupplierModel> supplierModelList) {
        progressBar.setVisibility(View.GONE);
        if(this.supplierModelList.size() != 0){
            this.supplierModelList.clear();
        }

        this.supplierModelList.addAll(supplierModelList);
        adapter.notifyDataSetChanged();
        rv_result.setVisibility(View.VISIBLE);
        hideSoftKeyboard();
    }

    @Override
    public void showSearchResult(boolean isSuccess, List<SupplierModel> supplierModel) {
        progressBar.setVisibility(View.GONE);
        if(isSuccess && supplierModel.size() != 0){
            if(supplierModelList.size() != 0){
                supplierModelList.clear();
            }
            supplierModelList.addAll(supplierModel);
            adapter.notifyDataSetChanged();
            rv_result.setVisibility(View.VISIBLE);
            hideSoftKeyboard();
        } else {
            Toast.makeText(this, "Maaf Supplier Yang Anda Cari Belum Tersedia", Toast.LENGTH_LONG).show();
        }

    }

    private void checkPermission(){
        if(Build.VERSION.SDK_INT > 22){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission. ACCESS_FINE_LOCATION}, Const.PERMISSION_ACCESS_LOCATION_CODE);
            } else {
                currentLocation();
            }
        } else {
            currentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Const.PERMISSION_ACCESS_LOCATION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                currentLocation();
            } else {
                Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void currentLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if(locationManager != null){
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(loc != null){
                        latitude_str = String.valueOf(loc.getLatitude());
                        longitude_str = String.valueOf(loc.getLongitude());
                    } else {
                        latitude_str = "-6.174668";
                        longitude_str = "106.827126";
                    }
                } else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(loc != null){
                        latitude_str = String.valueOf(loc.getLatitude());
                        longitude_str = String.valueOf(loc.getLongitude());
                    } else {
                        latitude_str = "-6.174668";
                        longitude_str = "106.827126";
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
//            latitude_str = String.valueOf(loc.getLatitude());
//            longitude_str = String.valueOf(loc.getLongitude());
            Log.d("PERMISSION", "ENABLE lat "+ location.getLongitude() + " - "+location.getLatitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
