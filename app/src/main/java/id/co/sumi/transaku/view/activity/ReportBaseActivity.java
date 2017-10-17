package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.view.adapters.ViewPagerAdapter;
import id.co.sumi.transaku.view.customdialog.InventoryDetailDialog;
import id.co.sumi.transaku.view.fragment.ReportLabaRugiFragment;
import id.co.sumi.transaku.view.fragment.ReportPenjualanBarangFragment;
import id.co.sumi.transaku.view.fragment.ReportPenjualanFragment;

/**
 * Created by gebriani on 03/04/17.
 */

public class ReportBaseActivity extends ParentActivity {

    private TabLayout base_tabLayout;
    private ViewPager base_viewpager;
    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        base_tabLayout = (TabLayout) findViewById(R.id.report_base_tablayout);
        base_viewpager = (ViewPager) findViewById(R.id.report_base_viewpager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("LAPORAN");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupViewPager(base_viewpager);
        base_tabLayout.setupWithViewPager(base_viewpager);
        base_viewpager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new ReportPenjualanFragment(), "Transaksi");
        adapter.addFragment(new ReportLabaRugiFragment(), "Laba-Rugi");
        adapter.addFragment(new ReportPenjualanBarangFragment(), "Penjualan Barang");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
    }

    public static void showActivity(Context context){
        Intent intent = new Intent(context, ReportBaseActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public void showInventoryDialog(int position, String sellDetail){
        InventoryDetailDialog inventoryDetailDialog = InventoryDetailDialog.newInstance(sellDetail);
        inventoryDetailDialog.show(getSupportFragmentManager(), "");
    }
}
