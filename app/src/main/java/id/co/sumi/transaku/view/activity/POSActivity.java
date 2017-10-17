package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.SellDetailModel;
import id.co.sumi.transaku.presenter.POSMvpView;
import id.co.sumi.transaku.presenter.POSPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.view.adapters.POSInventoryAdapter;


public class POSActivity extends ParentActivity implements View.OnClickListener, POSMvpView, SearchView.OnQueryTextListener {
    private static String RESULT_TOTAL_HARGA = "result_total_harga";
    private static String RESULT_INVENTORY = "result_inventory";
    private static String RESULT_TOTAL_POS = "result_total_pos";
    private static String PAGE_FLAG = "page_flag";

    private static TextView tv_total_price;
    private Button bt_scan;
    private Button bt_bayar;
    private RecyclerView rv_inventory;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private POSInventoryAdapter adapter;
    private List<InventoryModel> inventoryModelList = new ArrayList<>();
    private POSPresenter presenter;
    private ProgressBar progressBar;
    private static TextView tv_sum_inventory;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        bt_scan = (Button) findViewById(R.id.pos_bt_scan);
        bt_bayar = (Button) findViewById(R.id.pos_bt_bayar);
        rv_inventory = (RecyclerView) findViewById(R.id.pos_rv_inventory);
        tv_total_price = (TextView) findViewById(R.id.pos_total_price);
        progressBar = (ProgressBar) findViewById(R.id.pos_progressBar);
        tv_sum_inventory = (TextView) findViewById(R.id.pos_total_inventory);
        rv_inventory.setLayoutManager(new LinearLayoutManager(rv_inventory.getContext()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("POS");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bt_scan.setOnClickListener(this);
        bt_bayar.setOnClickListener(this);
        adapter = new POSInventoryAdapter(this, inventoryModelList);
        rv_inventory.setAdapter(adapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        findView();
        presenter = PresenterFactory.posPresenter();
        presenter.attachView(this);
        progressBar.setVisibility(View.VISIBLE);
        presenter.getInventoryForPOSBySellerID();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        progressBar.setVisibility(View.VISIBLE);
//        if(getIntent().getStringExtra(PAGE_FLAG).equalsIgnoreCase(Const.page_flag_top)){
//            presenter.getInventoryForPOSBySellerID();
//        } else if(getIntent().getStringExtra(PAGE_FLAG).equalsIgnoreCase(Const.page_flag_back)){
//            InventoryModel[] inventoryModels = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(RESULT_INVENTORY), InventoryModel[].class);
//            inventoryModelList.clear();
//            inventoryModelList.addAll(Arrays.asList(inventoryModels));
//            adapter.notifyDataSetChanged();
//            tv_sum_inventory.setText(String.valueOf(getIntent().getIntExtra(RESULT_TOTAL_POS,0)));
//            tv_total_price.setText(CommonFunction.convertCurrencyFormat(getIntent().getDoubleExtra(RESULT_TOTAL_HARGA,0)));
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pos_bt_scan:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            case R.id.pos_bt_bayar:
//                if (adapter.inputResult() != 0) {
//                    String sendData = TransakuApplication.getInstance().getGson().toJson(adapter.getSellResult());
//                    POSResultActivity.showActivity(this, sendData,TransakuApplication.getInstance().getGson().toJson(inventoryModelList), adapter.getTotalPrice(), Integer.parseInt(tv_sum_inventory.getText().toString()));
//                }
                if (adapter.getResultTotal() != 0) {
                    Log.d("RESULT_LIST", TransakuApplication.getInstance().getGson().toJson(adapter.getListResult()) + " ");
                    Log.d("RESULT_LIST", adapter.getTotalPrice() + " ");
                    Log.d("RESULT_LIST", adapter.getTotalInventory() + " ");
                    String listResult = TransakuApplication.getInstance().getGson().toJson(adapter.getListResult());
                    String listAll = TransakuApplication.getInstance().getGson().toJson(adapter.getAllListInventory());
                    BookingConfirmationActivity.showActivity(this, 0, listResult, listAll, adapter.getTotalPrice(), adapter.getTotalInventory(), "");
                } else {
                    Toast.makeText(this, "Anda Belum Memilih Barang", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public static void showPopUp(Double totalprice, int sum_qty) {
        tv_total_price.setText(CommonFunction.convertCurrencyFormat(totalprice));
        tv_sum_inventory.setText(String.valueOf(sum_qty));
        if (tv_sum_inventory.getVisibility() == View.GONE) {
            tv_sum_inventory.setVisibility(View.VISIBLE);
        }

    }

    public static void showActivity(Context context, String page_flag) {
        Intent intent = new Intent(context, POSActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PAGE_FLAG, page_flag);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public static void showActivity(Context context, String page_flag, String result, double totalHarga, int totalPOS) {
        Intent intent = new Intent(context, POSActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PAGE_FLAG, page_flag);
        bundle.putString(RESULT_INVENTORY, result);
        bundle.putDouble(RESULT_TOTAL_HARGA, totalHarga);
        bundle.putInt(RESULT_TOTAL_POS, totalPOS);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            Log.d("SCAN_BARCODE", scanContent);
        } else {
            Toast.makeText(this, "Gagal Membaca Barcode", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressBar.setVisibility(View.VISIBLE);
        presenter.searchInventoryForSell(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void searchResult(boolean isSuccess, List<InventoryModel> inventoryModelList, String message) {
        progressBar.setVisibility(View.GONE);

        if(searchView.isShown()){
            searchMenuItem.collapseActionView();
            searchView.setQuery("",false);
        }

        if (isSuccess){
            inventoryModelList.clear();
            inventoryModelList.addAll(inventoryModelList);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Maaf, Barang yang anda cari tidak tersedia", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showActivity(boolean isSuccess, List<InventoryModel> inventory, String message) {
        progressBar.setVisibility(View.GONE);
        if(isSuccess){
            inventoryModelList.clear();
            inventoryModelList.addAll(inventory);
            adapter.notifyDataSetChanged();
        }
    }
}
