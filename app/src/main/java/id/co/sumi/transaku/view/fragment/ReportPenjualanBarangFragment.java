package id.co.sumi.transaku.view.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import id.co.sumi.transaku.view.adapters.ReportPenjualanBarangAdapter;

/**
 * Created by gebriani on 03/04/17.
 */

public class ReportPenjualanBarangFragment extends BaseFragment implements View.OnClickListener, ReportSellMvpView {

    private TextView tv_choose_start_date;
    private TextView tv_choose_end_date;
    private RecyclerView rv_penjualan_barang;
    private Button bt_search;

    private String flag_click, startDate, endDate;
    private SimpleDateFormat sdf;
    private Calendar currentTime;
    private DatePickerDialog datePicker;
    private ReportSellPresenter presenter;
    private ReportPenjualanBarangAdapter adapter;
    private ArrayList data = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_penjualan_barang, container, false);
        tv_choose_start_date = (TextView) view.findViewById(R.id.report_penjualan_barang_tv_start_date);
        tv_choose_end_date = (TextView) view.findViewById(R.id.report_penjualan_barang_tv_end_date);
        rv_penjualan_barang = (RecyclerView) view.findViewById(R.id.report_penjualan_barang_rv_pengeluaran);
        bt_search = (Button) view.findViewById(R.id.report_penjualan_barang_bt_search);
        rv_penjualan_barang.setLayoutManager(new LinearLayoutManager(rv_penjualan_barang.getContext()));
        tv_choose_end_date.setOnClickListener(this);
        tv_choose_start_date.setOnClickListener(this);
        bt_search.setOnClickListener(this);

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

        presenter = PresenterFactory.reportSellPresenter();
        presenter.attachView(this);

        adapter = new ReportPenjualanBarangAdapter(getContext());
        rv_penjualan_barang.setAdapter(adapter);

        return view;
    }

    //http://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap
    //http://stackoverflow.com/questions/22011200/creating-hashmap-from-a-json-string
    //http://www.vogella.com/tutorials/AndroidLocationAPI/article.html

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_penjualan_barang_tv_end_date:
                datePicker.show();
                flag_click = "end";
                break;
            case R.id.report_penjualan_barang_tv_start_date:
                datePicker.show();
                flag_click = "start";
                break;
            case R.id.report_penjualan_barang_bt_search:
                if (validateStartDate() && validateEndDate()) {
                    presenter.getReportPenjualanBarang(startDate, endDate);
                }
                break;
        }
    }

    private void getTimeFromTimePicker() {
        if (flag_click.equalsIgnoreCase("start")) {
            tv_choose_start_date.setText(sdf.format(currentTime.getTime()));
            startDate = sdf.format(currentTime.getTime());
        } else if (flag_click.equalsIgnoreCase("end")) {
            tv_choose_end_date.setText(sdf.format(currentTime.getTime()));
            endDate = sdf.format(currentTime.getTime());
            Log.d("TIME", sdf.format(currentTime.getTime()));
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
        if (tv_choose_end_date.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showReportSell(boolean isSuceess, List<SellModel> data, Map<String, String> totalPenjualan, String message) {

    }

    @Override
    public void showReportLabaRugi(boolean isSuceess, List<ReportModel> data, Map<String, String> additionalInfo, String message) {

    }


    @Override
    public void showReportPenjualanBarang(boolean isSuccess, Map<String, String> data, String message) {
        if (isSuccess) {
            if (data.size() > 0) {
                this.data.clear();
                this.data.addAll(data.entrySet());
                adapter.setData(this.data);
                adapter.notifyDataSetChanged();
                rv_penjualan_barang.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "Maaf data tidak ditemukan untuk tanggal tersebut", Toast.LENGTH_LONG).show();
                rv_penjualan_barang.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            rv_penjualan_barang.setVisibility(View.GONE);
        }
    }

}
