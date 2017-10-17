package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import id.co.sumi.transaku.view.adapters.ReportLabaRugiAdapter;

/**
 * Transaku
 * Author Gebriani
 * on 02, April 2017.
 */

public class LabaRugiReportActivity extends ParentActivity implements ReportSellMvpView, View.OnClickListener {
    private TextView tv_choose_start_date;
    private TextView tv_choose_end_time;
    private Button bt_search;
    private RecyclerView rv_debit;
    private TextView tv_sum_pengeluaran;
    private RecyclerView rv_credit;
    private TextView tv_sum_pemasukan;
    private TextView tv_sum_laba_rugi;
    private TextView text_pemasukan;
    private TextView text_pengeluaran;

    private ReportSellPresenter presenter;
    private String flag_click, startDate, endDate;
    private SimpleDateFormat sdf;
    private Calendar currentTime;
    private DatePickerDialog datePicker;
    private List<ReportModel> creditList = new ArrayList<>();
    private List<ReportModel> debitList = new ArrayList<>();
    private ReportLabaRugiAdapter creditAdapter;
    private ReportLabaRugiAdapter debitAdapter;

    private void findViews() {
        tv_choose_start_date = (TextView) findViewById(R.id.labarugi_tv_start_date);
        tv_choose_end_time = (TextView) findViewById(R.id.labarugi_tv_end_date);
        bt_search = (Button) findViewById(R.id.labarugi_bt_search);
        rv_debit = (RecyclerView) findViewById(R.id.labarugi_rv_pengeluaran);
        tv_sum_pengeluaran = (TextView) findViewById(R.id.labarugi_tv_sum_pengeluaran);
        rv_credit = (RecyclerView) findViewById(R.id.labarugi_rv_pemasukan);
        tv_sum_pemasukan = (TextView) findViewById(R.id.labarugi_tv_sum_pemasukan);
        tv_sum_laba_rugi = (TextView) findViewById(R.id.labarugi_tv_sum_laba_rugi);
        text_pemasukan = (TextView) findViewById(R.id.labarugi_text_pemasukan);
        text_pengeluaran = (TextView) findViewById(R.id.labarugi_text_pengeluaran);
        tv_choose_start_date.setOnClickListener(this);
        tv_choose_end_time.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        rv_credit.setLayoutManager(new LinearLayoutManager(rv_credit.getContext()));
        rv_debit.setLayoutManager(new LinearLayoutManager(rv_debit.getContext()));

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        currentTime = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                currentTime.set(Calendar.YEAR, y);
                currentTime.set(Calendar.MONTH, m);
                currentTime.set(Calendar.DAY_OF_MONTH, d);
                if (flag_click.equalsIgnoreCase("start")) {
                    currentTime.set(Calendar.HOUR_OF_DAY, 0);
                    currentTime.set(Calendar.MINUTE, 0);
                } else {
                    currentTime.set(Calendar.HOUR_OF_DAY, 23);
                    currentTime.set(Calendar.MINUTE, 59);
                }
                getTimeFromTimePicker();
            }
        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));

        creditAdapter = new ReportLabaRugiAdapter(getApplicationContext(), "C");
        debitAdapter = new ReportLabaRugiAdapter(getApplicationContext(), "D");
        rv_credit.setAdapter(creditAdapter);
        rv_debit.setAdapter(debitAdapter);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_laba_rugi_report);
        presenter = PresenterFactory.reportSellPresenter();
        presenter.attachView(this);
        findViews();
    }

    @Override
    public void showReportSell(boolean isSuceess, List<SellModel> data, Map<String,String> totalPenjualan, String message) {

    }

    @Override
    public void showReportLabaRugi(boolean isSuceess, List<ReportModel> data, Map<String,String> additionalInfo, String message) {
        if(isSuceess){
            if(data.size() > 0){
                debitList.add(0, data.get(0));
                creditList.add(0, data.get(0));
                for(ReportModel reportModel : data){
                    if(reportModel.getTrxType().equalsIgnoreCase("D")){
                        debitList.add(reportModel);
                    } else if (reportModel.getTrxType().equalsIgnoreCase("C")){
                        creditList.add(reportModel);
                    }
                }
                creditAdapter.setReportModelList(creditList);
                debitAdapter.setReportModelList(debitList);
                creditAdapter.notifyDataSetChanged();
                debitAdapter.notifyDataSetChanged();

                ArrayList ditems = new ArrayList();
                ditems.addAll(additionalInfo.entrySet());
                Map.Entry<String, String> item0 = (Map.Entry<String, String>) ditems.get(0);
                Map.Entry<String, String> item1 = (Map.Entry<String, String>) ditems.get(1);
                Map.Entry<String, String> item2 = (Map.Entry<String, String>) ditems.get(2);

                tv_sum_pemasukan.setText(CommonFunction.convertCurrencyFormat(Double.parseDouble(item0.getValue())));
                tv_sum_pengeluaran.setText(CommonFunction.convertCurrencyFormat(Double.parseDouble(item1.getValue())));

                String text_title = "Laba / Rugi : ";
                tv_sum_laba_rugi.setText(Html.fromHtml("<font color='#36454f'><b>"+text_title+"</b></font>"+"<font color='#B51212'>"+ CommonFunction.convertCurrencyFormat(Double.parseDouble(item2.getValue()))+"</font"));

                text_pengeluaran.setVisibility(View.VISIBLE);
                text_pemasukan.setVisibility(View.VISIBLE);
                rv_debit.setVisibility(View.VISIBLE);
                rv_credit.setVisibility(View.VISIBLE);
                tv_sum_laba_rugi.setVisibility(View.VISIBLE);
                tv_sum_pemasukan.setVisibility(View.VISIBLE);
                tv_sum_pengeluaran.setVisibility(View.VISIBLE);
            } else {
                text_pengeluaran.setVisibility(View.GONE);
                text_pemasukan.setVisibility(View.GONE);
                rv_debit.setVisibility(View.GONE);
                rv_credit.setVisibility(View.GONE);
                tv_sum_laba_rugi.setVisibility(View.GONE);
                tv_sum_pemasukan.setVisibility(View.GONE);
                tv_sum_pengeluaran.setVisibility(View.GONE);
                Toast.makeText(this, "Data tidak ditemukan untuk tanggal tersebut", Toast.LENGTH_LONG).show();
            }
        } else {
            text_pengeluaran.setVisibility(View.GONE);
            text_pemasukan.setVisibility(View.GONE);
            rv_debit.setVisibility(View.GONE);
            rv_credit.setVisibility(View.GONE);
            tv_sum_laba_rugi.setVisibility(View.GONE);
            tv_sum_pemasukan.setVisibility(View.GONE);
            tv_sum_pengeluaran.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReportPenjualanBarang(boolean isSuccess, Map<String,String> data, String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.labarugi_tv_start_date:
                datePicker.show();
                flag_click = "start";
                break;
            case R.id.labarugi_tv_end_date:
                datePicker.show();
                flag_click = "end";
                break;
            case R.id.labarugi_bt_search:
                if (validateStartDate() && validateEndDate()) {
                    presenter.getReportLabaRugi(startDate, endDate);
                }
                break;

        }
    }

    private void getTimeFromTimePicker() {
        if (flag_click.equalsIgnoreCase("start")) {
            tv_choose_start_date.setText(sdf.format(currentTime.getTime()));
            startDate = sdf.format(currentTime.getTime());
        } else if (flag_click.equalsIgnoreCase("end")) {
            tv_choose_end_time.setText(sdf.format(currentTime.getTime()));
            endDate = sdf.format(currentTime.getTime());
        }
    }

    private boolean validateStartDate() {
        if (tv_choose_start_date.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEndDate() {
        if (tv_choose_end_time.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, LabaRugiReportActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

}
