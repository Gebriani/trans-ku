package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.SupplierModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SupplierInventoryMvpView;
import id.co.sumi.transaku.presenter.SupplierInventoryPresenter;
import id.co.sumi.transaku.view.adapters.SupplierPosAdapter;

/**
 * Created by gebriani on 16/03/17.
 */

public class SupplierProfileActivity extends ParentActivity implements SupplierInventoryMvpView, View.OnClickListener {

    private static String SUPPLIER_DATA = "supplier_data";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvSuppName;
    private TextView tvSuppAddress;
    private TextView tvSuppDescription;
    private TextView tvCity;
    private RecyclerView rvInventory;
    private ImageView ivSuppProfilePic;
    private SupplierModel supplierModel;
    private SupplierInventoryPresenter presenter;
    private static FrameLayout frameFloating;
    private SupplierPosAdapter adapter;
    private List<InventoryModel> inventoryModelList = new ArrayList<>();

    private void findView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.supp_profile_collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.supp_profile_toolbar);
        tvSuppName = (TextView) findViewById(R.id.supp_profile_suppName);
        tvSuppDescription = (TextView) findViewById(R.id.item_supp_profile_deskripsi);
        tvSuppAddress = (TextView) findViewById(R.id.item_supp_profile_address);
        tvCity = (TextView) findViewById(R.id.item_supp_profile_city);
        ivSuppProfilePic = (ImageView) findViewById(R.id.supp_profile_suppProfilePic);
        rvInventory = (RecyclerView) findViewById(R.id.supp_profile_rv_inventory);
        rvInventory.setLayoutManager(new LinearLayoutManager(rvInventory.getContext()));
        frameFloating = (FrameLayout) findViewById(R.id.supp_profile_frameFloatingButton);
        findViewById(R.id.supp_profile_fab_close).setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        supplierModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(SUPPLIER_DATA), SupplierModel.class);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(supplierModel.getName());
        tvSuppName.setText(supplierModel.getName());
        tvSuppAddress.setText(supplierModel.getAddress());
        tvSuppDescription.setText(supplierModel.getDescription());
        tvCity.setText(supplierModel.getCity().getName());
        if (supplierModel.getPicturePath() != null && !supplierModel.getPicturePath().equalsIgnoreCase("")) {
            Glide.with(this)
                    .load(supplierModel.getPicturePath())
                    .centerCrop()
//                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivSuppProfilePic);
        }

        Bundle bundle = new Bundle();
        bundle.putString("supplier_name", supplierModel.getName());
        bundle.putString("supplier_id", supplierModel.getId() + "");
        bundle.putString("supplier_email", supplierModel.getEmail());
        TransakuApplication.getInstance().getFirebaseAnalytics().logEvent("supplier_profile", bundle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);
        findView();
        adapter = new SupplierPosAdapter(this, inventoryModelList);
        rvInventory.setAdapter(adapter);
        presenter = PresenterFactory.supplierInventoryPresenter();
        presenter.attachView(this);
        presenter.getAllInventory(supplierModel.getId());
    }

    @Override
    public void showResponse(boolean isSuceess, List<InventoryModel> resp, String message) {
        if (isSuceess) {
            inventoryModelList.clear();
            inventoryModelList.addAll(resp);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Tidak dapat mengakses data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supp_profile_fab_close:
                if (adapter.getResultTotal() != 0) {
                    String listResult = TransakuApplication.getInstance().getGson().toJson(adapter.getListResult());
                    String listAll = TransakuApplication.getInstance().getGson().toJson(adapter.getAllListInventory());
                    String supplier = TransakuApplication.getInstance().getGson().toJson(supplierModel);
                    BookingConfirmationActivity.showActivity(this, 1, listResult, listAll, adapter.getTotalPrice(), adapter.getTotalInventory(), supplier);
                } else {
                    Toast.makeText(this, "Anda Belum Memilih Barang", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public static void showActivity(Context context, String result) {
        Intent intent = new Intent(context, SupplierProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SUPPLIER_DATA, result);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public static void activeBayar(int flag) {
        if (flag == 0 && frameFloating.getVisibility() == View.GONE) {
            frameFloating.setVisibility(View.VISIBLE);
        } else if (flag == 1 && frameFloating.getVisibility() == View.VISIBLE) {
            frameFloating.setVisibility(View.GONE);
        }
    }
}
