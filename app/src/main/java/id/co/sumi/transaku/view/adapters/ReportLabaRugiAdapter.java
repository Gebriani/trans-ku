package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.ReportModel;
import id.co.sumi.transaku.utils.CommonFunction;

/**
 * Transaku
 * Author Gebriani
 * on 02, April 2017.
 */

public class ReportLabaRugiAdapter extends RecyclerView.Adapter<ReportLabaRugiAdapter.ReportLabaRugiViewHolder>{
    private Context context;
    private List<ReportModel> reportModelList = new ArrayList<>();
    private final int TYPE_HEADER = 0, TYPE_REPORT = 1;
    private String type_report;

    public ReportLabaRugiAdapter(Context context, String type_report) {
        this.context = context;
        this.type_report = type_report;
    }

    public void setReportModelList(List<ReportModel> reportModelList) {
        this.reportModelList = reportModelList;
    }

    @Override
    public ReportLabaRugiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View report_type = LayoutInflater.from(context).inflate(R.layout.adapter_report_laba_rugi, parent, false);
        return new ReportLabaRugiViewHolder(report_type);
    }

    @Override
    public void onBindViewHolder(ReportLabaRugiViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case TYPE_HEADER:
                holder.transaksi_root.setBackgroundColor(Color.parseColor("#B51212"));
                holder.tv_transaksi_title.setText(Html.fromHtml("<font color='#FFFFFF'>Transaksi</font>"));
                holder.tv_transaksi_date.setText(Html.fromHtml("<font color='#FFFFFF'>Tanggal</font>"));
                holder.tv_transaksi_total.setText(Html.fromHtml("<font color='#FFFFFF'>Nominal</font>"));

                break;
            case TYPE_REPORT:
                ReportModel reportModel = reportModelList.get(position);
                holder.transaksi_root.setBackgroundColor(Color.parseColor("#FFFFFF"));

                holder.tv_transaksi_title.setText(Html.fromHtml("<font color='#36454f'>"+reportModel.getNote()+"</font>"));
                holder.tv_transaksi_date.setText(Html.fromHtml("<font color='#36454f'>"+reportModel.getTrxDate()+"</font>"));
                if(type_report.equalsIgnoreCase("C")){
                    holder.tv_transaksi_total.setText(Html.fromHtml("<font color='#36454f'>"+ CommonFunction.convertCurrencyFormat(reportModel.getCredit())+"</font>"));
                } else {
                    holder.tv_transaksi_total.setText(Html.fromHtml("<font color='#36454f'>"+ CommonFunction.convertCurrencyFormat(reportModel.getDebit())+"</font>"));

                }
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        } else {
            return TYPE_REPORT;
        }
    }

    @Override
    public int getItemCount() {
        if(reportModelList.size() == 0){
            return 0;
        } else {
            return reportModelList.size();
        }
    }

    protected static class ReportLabaRugiViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_transaksi_title;
        private TextView tv_transaksi_date;
        private TextView tv_transaksi_total;
        private LinearLayout transaksi_root;

        public ReportLabaRugiViewHolder(View itemView) {
            super(itemView);
            tv_transaksi_title = (TextView) itemView.findViewById(R.id.adapter_reLabaRugi_transaksi_title);
            tv_transaksi_date = (TextView) itemView.findViewById(R.id.adapter_reLabaRugi_transaks_date);
            tv_transaksi_total = (TextView) itemView.findViewById(R.id.adapter_reLabaRugi_transaksi_total);
            transaksi_root = (LinearLayout) itemView.findViewById(R.id.adapter_reLabaRugi_root);
        }
    }
}
