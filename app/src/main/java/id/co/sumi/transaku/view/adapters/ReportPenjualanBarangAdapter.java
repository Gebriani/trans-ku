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
import java.util.Map;

import id.co.sumi.transaku.R;

/**
 * Transaku
 * Author Gebriani
 * on 05, April 2017.
 */

public class ReportPenjualanBarangAdapter extends RecyclerView.Adapter<ReportPenjualanBarangAdapter.ReportPenjualanBarangViewHolder> {
    private ArrayList data = new ArrayList();
    private Context context;
    private final int TYPE_HEADER = 0, TYPE_REPORT = 1;

    public ReportPenjualanBarangAdapter(Context context) {
        this.context = context;
    }

//    public void setData(Map<String, String> data) {
//        this.data.addAll(data.entrySet());
//    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    @Override
    public ReportPenjualanBarangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View report_type = LayoutInflater.from(context).inflate(R.layout.adapter_report_penjualan_barang, parent, false);
        return new ReportPenjualanBarangViewHolder(report_type);
    }

    @Override
    public void onBindViewHolder(ReportPenjualanBarangViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case TYPE_HEADER:
                holder.ln_root.setBackgroundColor(Color.parseColor("#B51212"));
                holder.tv_title.setText(Html.fromHtml("<font color='#FFFFFF'>Nama Barang</font>"));
                holder.tv_sum.setText(Html.fromHtml("<font color='#FFFFFF'>Jumlah</font>"));
                break;
            case TYPE_REPORT:
                Map.Entry<String, String> item = (Map.Entry<String, String>) data.get(position - 1);
                holder.ln_root.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.tv_title.setText(Html.fromHtml("<font color='#36454f'>"+item.getKey()+"</font>"));
                holder.tv_sum.setText(Html.fromHtml("<font color='#36454f'>"+item.getValue()+"</font>"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(data.size() == 0){
            return 0;
        } else {
            return data.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        } else {
            return TYPE_REPORT;
        }
    }


    private boolean isPositionHeader (int position) {
        return position == 0;
    }


    protected static class ReportPenjualanBarangViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private TextView tv_sum;
        private LinearLayout ln_root;

        public ReportPenjualanBarangViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.adapter_rep_jualbarang_title);
            tv_sum = (TextView) itemView.findViewById(R.id.adapter_rep_jualbarang_sum);
            ln_root = (LinearLayout) itemView.findViewById(R.id.adapter_rep_jualbarang_root);
        }
    }
}
