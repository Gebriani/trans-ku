package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.presenter.InventoryMvpView;
import id.co.sumi.transaku.presenter.InventoryPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.view.adapters.InventoryListAdapter;

/**
 * Created by alodokter on 01/03/17.
 */

public class InventoryActivity extends ParentActivity implements View.OnClickListener, InventoryMvpView {

    private Toolbar inv_toolbar;
    private TextView inv_toolbar_title;
    private RecyclerView inv_recyleview;
    private EditText et_search_inv;
    private Button bt_search;

    private InventoryPresenter presenter;
    private InventoryListAdapter adapter;

    private void findView(){
        inv_toolbar = (Toolbar) findViewById(R.id.toolbar);
        inv_toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        inv_recyleview = (RecyclerView) findViewById(R.id.inventory_recycleView);
        inv_recyleview.setLayoutManager(new LinearLayoutManager(inv_recyleview.getContext()));
        et_search_inv = (EditText) findViewById(R.id.inventory_search_inventory);
        bt_search = (Button) findViewById(R.id.inventory_search_button);
        findViewById(R.id.inventory_reset_button).setOnClickListener(this);

        bt_search.setOnClickListener(this);
        setSupportActionBar(inv_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        inv_toolbar_title.setText("INVENTORY");

        inv_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hideSoftKeyboard();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        presenter = PresenterFactory.inventoryPresenter();
        presenter.attachView(this);
        findView();
        adapter = new InventoryListAdapter(this, new InventoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InventoryModel item) {

            }
        });
        inv_recyleview.setAdapter(adapter);
        presenter.getInventoryByCustomerID();
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, InventoryActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inventory_search_button:
                if(!et_search_inv.getText().toString().trim().isEmpty()){
                    presenter.searchInventoryByName(et_search_inv.getText().toString());
                }
                break;
            case R.id.inventory_reset_button:
                et_search_inv.setText("");
                presenter.getInventoryByCustomerID();
                break;
        }
    }

    @Override
    public void showActivity(boolean isSuccess, List<InventoryModel> inventory, String messag) {
        if(isSuccess){
            adapter.setInventoryList(inventory);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void searchResult(boolean isSuccess, List<InventoryModel> inventory, String message) {
        if(isSuccess){
            if(inventory.size() != 0){
                adapter.setInventoryList(inventory);
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "Maaf Data yang Anda Cari Tidak Ada", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void editPrice(boolean isSuccess, String message) {

    }

    @Override
    public void deleteInventory(boolean isSuccess, String message) {

    }
}
