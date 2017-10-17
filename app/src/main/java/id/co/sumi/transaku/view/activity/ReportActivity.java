package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.ReportModel;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.ReportSellMvpView;
import id.co.sumi.transaku.presenter.ReportSellPresenter;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.view.adapters.ReportSellAdapter;

public class ReportActivity extends ParentActivity implements View.OnClickListener, ReportSellMvpView {

    private TextView tv_start_date;
    private TextView tv_end_date;
    private RecyclerView rv_result;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button bt_search;
    private LinearLayout root_penjualan;
    private TextView tv_total_penjualan;

    private Calendar currentTime;
    private DatePickerDialog datePicker;
    private String flag_click, startDate, endDate;

    private ReportSellPresenter presenter;
    private ReportSellAdapter adapter;
    private List<SellModel> sellModelList = new ArrayList<>();
    private SimpleDateFormat sdf;
    private ProgressDialog progressDialog;

    private void findView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tv_start_date = (TextView) findViewById(R.id.report_tv_start_date);
        tv_end_date = (TextView) findViewById(R.id.report_tv_end_date);
        rv_result = (RecyclerView) findViewById(R.id.report_rv_result);
        bt_search = (Button) findViewById(R.id.report_bt_search);
        root_penjualan = (LinearLayout) findViewById(R.id.report_root_total);
        tv_total_penjualan = (TextView) findViewById(R.id.report_tv_total_penjualan);

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
        bt_search.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        currentTime = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                currentTime.set(Calendar.YEAR, y);
                currentTime.set(Calendar.MONTH, m);
                currentTime.set(Calendar.DAY_OF_MONTH, d);
                if(flag_click.equalsIgnoreCase("start")){
                    currentTime.set(Calendar.HOUR_OF_DAY, 0);
                    currentTime.set(Calendar.MINUTE, 0);
                } else {
                    currentTime.set(Calendar.HOUR_OF_DAY, 23);
                    currentTime.set(Calendar.MINUTE, 59);
                }
                getTimeFromTimePicker();
            }
        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));

        rv_result.setLayoutManager(new LinearLayoutManager(rv_result.getContext()));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu");

    }

    private void setTimeForFirstTime(){
        Calendar tempTime = Calendar.getInstance();
        tempTime.set(Calendar.HOUR_OF_DAY, 0);
        tempTime.set(Calendar.MINUTE, 0);
        String firstDate = sdf.format(tempTime.getTime());

        tempTime.set(Calendar.HOUR_OF_DAY, 23);
        tempTime.set(Calendar.MINUTE, 59);
        String lastDate = sdf.format(tempTime.getTime());
        presenter.getReportSellByDate(firstDate, lastDate);
    }

    private void getTimeFromTimePicker(){
        if(flag_click.equalsIgnoreCase("start")){
            tv_start_date.setText(sdf.format(currentTime.getTime()));
            startDate = sdf.format(currentTime.getTime());
        } else if(flag_click.equalsIgnoreCase("end")){
            tv_end_date.setText(sdf.format(currentTime.getTime()));
            endDate = sdf.format(currentTime.getTime());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        findView();

        progressDialog.show();
        presenter = PresenterFactory.reportSellPresenter();
        presenter.attachView(this);

        adapter = new ReportSellAdapter(this, sellModelList);
        rv_result.setAdapter(adapter);

        setTimeForFirstTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_bt_search:
                if(validateStartDate() && validateEndDate()){
                    progressDialog.show();
                    presenter.getReportSellByDate(startDate, endDate);
                }
                break;
            case R.id.report_tv_start_date:
                datePicker.show();
                flag_click = "start";
                break;
            case R.id.report_tv_end_date:
                datePicker.show();
                flag_click = "end";
                break;
        }
    }

    private boolean validateStartDate(){
        if(tv_start_date.getText().toString().isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEndDate(){
        if(tv_end_date.getText().toString().isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, ReportActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void showReportSell(boolean isSuceess, List<SellModel> data, Map<String,String> additionalInfo, String message) {
        progressDialog.dismiss();
        if(isSuceess){
            sellModelList.clear();
            sellModelList.addAll(data);
            adapter.notifyDataSetChanged();
            if(data.size() == 0){
                root_penjualan.setVisibility(View.GONE);
                rv_result.setVisibility(View.GONE);
                Toast.makeText(this, "Maaf Tidak Ada Laporan Pada Tanggal Tersebut", Toast.LENGTH_SHORT).show();
            } else {
                root_penjualan.setVisibility(View.VISIBLE);
                rv_result.setVisibility(View.VISIBLE);
                ArrayList ditems = new ArrayList();
                ditems.addAll(additionalInfo.entrySet());
                Map.Entry<String, String> item0 = (Map.Entry<String, String>) ditems.get(0);
                tv_total_penjualan.setText(CommonFunction.convertCurrencyFormat(Double.parseDouble(item0.getValue())));
            }
        }
    }


    @Override
    public void showReportLabaRugi(boolean isSuceess, List<ReportModel> data, Map<String,String> additionalInfo, String message) {
    }

    @Override
    public void showReportPenjualanBarang(boolean isSuccess, Map<String,String> data, String message) {
    }
}
