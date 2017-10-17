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
import id.co.sumi.transaku.view.fragment.FindSupplierFragment;
import id.co.sumi.transaku.view.fragment.RiwayatSupplierFragment;

/**
 * Created by gebriani on 03/04/17.
 */

public class SupplierBaseActivity extends ParentActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TextView toolbar_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_base);
        tabLayout = (TabLayout) findViewById(R.id.supplier_base_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.supplier_base_viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

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

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new FindSupplierFragment(), "Cari Supplier");
        adapter.addFragment(new RiwayatSupplierFragment(), "Riwayat");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, SupplierBaseActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

}
