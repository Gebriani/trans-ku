package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.presenter.InventoryMvpView;
import id.co.sumi.transaku.presenter.InventoryPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.view.adapters.InventoryBarangAdapter;
import id.co.sumi.transaku.view.customdialog.AddNewInventoryDialog;
import id.co.sumi.transaku.view.customdialog.EditPriceDialog;
import id.co.sumi.transaku.view.customdialog.RestockInventoryDialog;

/**
 * Created  on 03/03/17.
 */

public class BarangBeliActivity extends ParentActivity implements
        InventoryMvpView, InventoryBarangAdapter.inventoryBarangInterface,
        EditPriceDialog.DialogPriceInterface, RestockInventoryDialog.DialogRestockInterface
        ,SearchView.OnQueryTextListener{

    private Toolbar toolbarBarangBeli;
    private TextView toolbarTitle;
    private RecyclerView rvBarangBeli;
    private InventoryPresenter presenter;
    private ProgressDialog progressDialog;
    private InventoryBarangAdapter adapter;
    private List<InventoryModel> inventoryModelList = new ArrayList<>();
    private SearchView searchView;
    private MenuItem searchMenuItem;

    private void findView() {
        toolbarBarangBeli = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        rvBarangBeli = (RecyclerView) findViewById(R.id.barangBeli_recycleView);
        rvBarangBeli.setLayoutManager(new LinearLayoutManager(rvBarangBeli.getContext()));

        setSupportActionBar(toolbarBarangBeli);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText("BARANG BELI");

        toolbarBarangBeli.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu");

        adapter = new InventoryBarangAdapter(this, inventoryModelList, 0, this);
        rvBarangBeli.setAdapter(adapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_beli);
        presenter = PresenterFactory.inventoryPresenter();
        presenter.attachView(this);
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getInventoryByCustomerID();
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.item_menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_add:
//                AddNewInventoryDialog addNewInventoryDialog = AddNewInventoryDialog.newInstance("beli", this);
//                addNewInventoryDialog.show(getSupportFragmentManager(), "");
                NewInventoryActivity.showActivity(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, BarangBeliActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void showActivity(boolean isSuccess, List<InventoryModel> inventory, String message) {
        progressDialog.dismiss();
        if(isSuccess){
            if(inventoryModelList.size() != 0){
                inventoryModelList.clear();
            }
            inventoryModelList.addAll(inventory);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void searchResult(boolean isSuccess, List<InventoryModel> inventory, String messag) {
        progressDialog.dismiss();
        if(searchView.isShown()){
            searchMenuItem.collapseActionView();
            searchView.setQuery("",false);
        }

        if(isSuccess){
            if(inventoryModelList.size() != 0){
                inventoryModelList.clear();
            }

            inventoryModelList.addAll(inventory);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void editPrice(boolean isSuccess, String message) {

    }

    @Override
    public void deleteInventory(boolean isSuccess, String message) {
        presenter.getInventoryByCustomerID();
        Toast.makeText(BarangBeliActivity.this, message, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void finishDialog() {
//        presenter.getInventoryByCustomerID();
//    }

    @Override
    public void showDialogEdit(InventoryModel inventoryModel) {
        String inv_str = TransakuApplication.getInstance().getGson().toJson(inventoryModel);
        EditPriceDialog dialog = EditPriceDialog.newInstance(inv_str, this);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void showDialogDelete(final InventoryModel inventoryModel) {
        new AlertDialog.Builder(BarangBeliActivity.this)
                .setTitle("Hapus Barang")
                .setMessage("Apakah Anda Yakin Menghapus Barang Ini ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.deleteInventory(inventoryModel);
                        progressDialog.show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showDialogRestock(InventoryModel inventoryModel) {
        String inv_str = TransakuApplication.getInstance().getGson().toJson(inventoryModel);
        RestockInventoryDialog dialog = RestockInventoryDialog.newInstance(inv_str, this);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void dialogPriceFinish() {
        presenter.getInventoryByCustomerID();
        progressDialog.show();
    }

    @Override
    public void dialogRestockFinish() {
        presenter.getInventoryByCustomerID();
        progressDialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.searchInventoryByName(query);
        progressDialog.show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
