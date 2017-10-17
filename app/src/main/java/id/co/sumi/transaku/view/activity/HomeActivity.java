package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.HomeMenu;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.adapters.HomeMenuAdapter;

/**
 * Created  on 27/02/17.
 */

public class HomeActivity extends ParentActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button bt_logout;
    private RecyclerView rv_home_menu;
    private HomeMenuAdapter homeMenuAdapter;
    private List<HomeMenu> menuList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        bt_logout = (Button) findViewById(R.id.home_bt_logout);
        rv_home_menu = (RecyclerView) findViewById(R.id.home_rv_mainmenu);
        rv_home_menu.setLayoutManager(new GridLayoutManager(rv_home_menu.getContext(), 2));
        homeMenuAdapter = new HomeMenuAdapter(HomeActivity.this, setMenuList());
        rv_home_menu.setAdapter(homeMenuAdapter);
        bt_logout.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("TRANSAKU");
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }


    private List<HomeMenu> setMenuList(){
        menuList.add(new HomeMenu("POS", R.mipmap.ic_pos,"pos"));
        menuList.add(new HomeMenu("Inventory", R.mipmap.ic_inventory,"inventory"));
        menuList.add(new HomeMenu("Laporan", R.mipmap.newspaper,"report"));
        menuList.add(new HomeMenu("Daftar\nBarang Beli", R.mipmap.ic_shopping_cart,"buy_goods"));
        menuList.add(new HomeMenu("Daftar\nBarang Jual", R.mipmap.wallet,"sell_goods"));
        menuList.add(new HomeMenu("Cari Supplier", R.mipmap.ic_find_supplier,"find_supplier"));
//        menuList.add(new HomeMenu("Data Utama", R.mipmap.folder,"main_data"));
        menuList.add(new HomeMenu("Profile", R.mipmap.folder,"main_data"));

        return menuList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_bt_logout:
               showLogOutDialogConfirmation();
                break;
        }
    }

    private void showLogOutDialogConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setMessage("Apakah anda yakin akan keluar dari Transaku ?")
                .setCancelable(false)
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        PrefHelper.clearAllPreferences();
                        PrefHelper.clearPreference(Const.TOKEN);
                        PrefHelper.clearPreference(Const.ID);
                        PrefHelper.clearPreference(Const.EMAIL);
                        PrefHelper.clearPreference(Const.NAME);
                        dialog.dismiss();
                        LoginActivity.showActivity(HomeActivity.this);
                        finish();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
