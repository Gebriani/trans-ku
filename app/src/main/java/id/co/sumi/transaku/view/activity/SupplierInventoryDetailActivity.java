package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.SupplierModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SupplierInventoryMvpView;
import id.co.sumi.transaku.presenter.SupplierInventoryPresenter;
import id.co.sumi.transaku.view.adapters.SupplierInventoryAdapter;

/**
 * Created by gebriani on 16/03/17.
 */

public class SupplierInventoryDetailActivity extends ParentActivity implements SupplierInventoryMvpView, View.OnClickListener {
    private static String SUPPLIER_DATA = "supplier_data";

    private ImageView iv_supp_pic;
    private TextView tv_supp_name;
    private TextView tv_supp_address;
    private TextView tv_supp_desription;
    private RecyclerView rv_supp_inventory;
    private Toolbar inv_toolbar;
    private TextView inv_toolbar_title;
    private SupplierModel supplierModel;
    private SupplierInventoryPresenter presenter;
    private SupplierInventoryAdapter adapter;
    private FloatingActionButton fab_order;
    private static TextView tv_sub_total;

    private void findViews() {
        inv_toolbar = (Toolbar) findViewById(R.id.toolbar);
        inv_toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        iv_supp_pic = (ImageView) findViewById(R.id.supp_inv_suppProfilePic);
        tv_supp_name = (TextView) findViewById(R.id.supp_inv_suppName);
        tv_supp_address = (TextView) findViewById(R.id.supp_inv_suppAddress);
        tv_supp_desription = (TextView) findViewById(R.id.supp_inv_suppDescription);
        fab_order = (FloatingActionButton) findViewById(R.id.supp_inv_fab_order);
        tv_sub_total = (TextView) findViewById(R.id.supp_inv_sum_pos);
        rv_supp_inventory = (RecyclerView) findViewById(R.id.supp_inv_inventory_result);
        rv_supp_inventory.setLayoutManager(new LinearLayoutManager(rv_supp_inventory.getContext()));
        fab_order.setOnClickListener(this);

        setSupportActionBar(inv_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        inv_toolbar_title.setText("SUPPLIER INVENTORY");

        inv_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        supplierModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(SUPPLIER_DATA), SupplierModel.class);
        tv_supp_name.setText(supplierModel.getName());
        tv_supp_desription.setText(supplierModel.getDescription());
        tv_supp_address.setText(supplierModel.getAddress());

        if(supplierModel.getPicturePath() == null){
            Glide.with(this)
                    .load("http://www.ga-asi.com/Websites/gaasi/images/supplier-portal-home-image.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .into(iv_supp_pic);
        } else {
            Glide.with(this)
                    .load(supplierModel.getPicturePath())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .into(iv_supp_pic);
        }


    }

    public static void showPopUp(int sum_qty){
        tv_sub_total.setText(String.valueOf(sum_qty));
        if(tv_sub_total.getVisibility() == View.GONE){
            tv_sub_total.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_inventory_detail);
        findViews();
        presenter = PresenterFactory.supplierInventoryPresenter();
        presenter.attachView(this);
        presenter.getAllInventory(supplierModel.getId());
        adapter = new SupplierInventoryAdapter(this);
        rv_supp_inventory.setAdapter(adapter);
    }

    public static void showActivity(Context context, String result) {
        Intent intent = new Intent(context, SupplierInventoryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SUPPLIER_DATA, result);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void showResponse(boolean isSuceess, List<InventoryModel> resp, String message) {
        if(isSuceess){
            adapter.setInventoryModels(resp);
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.supp_inv_fab_order:
                if(adapter.getTotalList().size() != 0){
                    finish();
                    String sendData = TransakuApplication.getInstance().getGson().toJson(adapter.getSellResult());
//                    POSResultActivity.showActivity(this, sendData, adapter.getTotalPrice());
                }
                break;
        }
    }
}
