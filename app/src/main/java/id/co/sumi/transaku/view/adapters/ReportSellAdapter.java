package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.view.activity.ReportBaseActivity;


public class ReportSellAdapter extends RecyclerView.Adapter<ReportSellAdapter.ReportSellViewHolder> {
    private Context context;
    private List<SellModel> sellModelList = new ArrayList<>();

    public ReportSellAdapter(Context context, List<SellModel> sellModelList) {
        this.context = context;
        this.sellModelList = sellModelList;
    }

    @Override
    public ReportSellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_report_sell, parent, false);
        return new ReportSellViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportSellViewHolder holder, final int position) {
        final SellModel sellModel = sellModelList.get(position);

        holder.report_code.setText(sellModel.getCode());
        holder.report_date.setText(sellModel.getCreatedDate());
        holder.total_price.setText(CommonFunction.convertCurrencyFormat(sellModel.getTotalPrice()));
        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selldetail = TransakuApplication.getInstance().getGson().toJson(sellModel.getSellDetails());
                ReportBaseActivity reportBaseActivity = (ReportBaseActivity) context;
                reportBaseActivity.showInventoryDialog(position, selldetail);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sellModelList != null ? sellModelList.size() : 0;
    }

    protected static class ReportSellViewHolder extends RecyclerView.ViewHolder {
        private TextView report_code;
        private TextView report_date;
        private TextView total_price;
        private RelativeLayout root_layout;

        protected ReportSellViewHolder(View itemView) {
            super(itemView);
            report_code = (TextView) itemView.findViewById(R.id.adapter_report_code);
            report_date = (TextView) itemView.findViewById(R.id.adapter_report_date);
            total_price = (TextView) itemView.findViewById(R.id.adapter_report_totalPrice);
            root_layout = (RelativeLayout) itemView.findViewById(R.id.adapter_report_root_layout);
        }
    }
}
