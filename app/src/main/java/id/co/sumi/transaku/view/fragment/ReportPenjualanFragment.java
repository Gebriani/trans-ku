package id.co.sumi.transaku.view.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by gebriani on 03/04/17.
 */

public class ReportPenjualanFragment extends BaseFragment implements View.OnClickListener, ReportSellMvpView {

    private TextView tv_start_date;
    private TextView tv_end_date;
    private RecyclerView rv_result;
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

    private void findViews(View view) {
        tv_start_date = (TextView) view.findViewById(R.id.fragment_report_tv_start_date);
        tv_end_date = (TextView) view.findViewById(R.id.fragment_report_tv_end_date);
        rv_result = (RecyclerView) view.findViewById(R.id.fragment_report_rv_result);
        bt_search = (Button) view.findViewById(R.id.fragment_report_bt_search);
        root_penjualan = (LinearLayout) view.findViewById(R.id.fragment_report_root_total);
        tv_total_penjualan = (TextView) view.findViewById(R.id.fragment_report_tv_total_penjualan);
        bt_search.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        currentTime = Calendar.getInstance();
        datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

        rv_result.setLayoutManager(new LinearLayoutManager(rv_result.getContext()));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Mohon Tunggu");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_penjualan, container, false);
        findViews(view);
        progressDialog.show();
        presenter = PresenterFactory.reportSellPresenter();
        presenter.attachView(this);
        adapter = new ReportSellAdapter(getContext(), sellModelList);
        rv_result.setAdapter(adapter);
        setTimeForFirstTime();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_report_bt_search:
                if (validateStartDate() && validateEndDate()) {
                    progressDialog.show();
                    presenter.getReportSellByDate(startDate, endDate);
                }
                break;
            case R.id.fragment_report_tv_start_date:
                datePicker.show();
                flag_click = "start";
                break;
            case R.id.fragment_report_tv_end_date:
                datePicker.show();
                flag_click = "end";
                break;
        }
    }

    @Override
    public void showReportSell(boolean isSuceess, List<SellModel> data, Map<String, String> additionalInfo, String message) {
        progressDialog.dismiss();
        if (isSuceess) {
            sellModelList.clear();
            sellModelList.addAll(data);
            adapter.notifyDataSetChanged();
            ArrayList ditems = new ArrayList();
            ditems.addAll(additionalInfo.entrySet());
            Map.Entry<String, String> item0 = (Map.Entry<String, String>) ditems.get(0);
            if (data.size() == 0) {
                root_penjualan.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Maaf Tidak Ada Laporan Pada Tanggal Tersebut", Toast.LENGTH_SHORT).show();
            } else {
                root_penjualan.setVisibility(View.VISIBLE);
                tv_total_penjualan.setText(CommonFunction.convertCurrencyFormat(Double.parseDouble(item0.getValue())));
            }
        }
    }

    @Override
    public void showReportLabaRugi(boolean isSuceess, List<ReportModel> data, Map<String, String> additionalInfo, String message) {

    }

    @Override
    public void showReportPenjualanBarang(boolean isSuccess, Map<String, String> data, String message) {

    }

    private void setTimeForFirstTime() {
        Calendar tempTime = Calendar.getInstance();
        tempTime.set(Calendar.HOUR_OF_DAY, 0);
        tempTime.set(Calendar.MINUTE, 0);
        String firstDate = sdf.format(tempTime.getTime());
        tempTime.set(Calendar.HOUR_OF_DAY, 23);
        tempTime.set(Calendar.MINUTE, 59);
        String lastDate = sdf.format(tempTime.getTime());
        presenter.getReportSellByDate(firstDate, lastDate);
    }

    private void getTimeFromTimePicker() {
        if (flag_click.equalsIgnoreCase("start")) {
            tv_start_date.setText(sdf.format(currentTime.getTime()));
            startDate = sdf.format(currentTime.getTime());
        } else if (flag_click.equalsIgnoreCase("end")) {
            tv_end_date.setText(sdf.format(currentTime.getTime()));
            endDate = sdf.format(currentTime.getTime());
        }
    }

    private boolean validateStartDate() {
        if (tv_start_date.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEndDate() {
        if (tv_end_date.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
