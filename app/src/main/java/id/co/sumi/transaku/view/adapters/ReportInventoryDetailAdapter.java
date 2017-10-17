package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.SellDetailModel;
import id.co.sumi.transaku.utils.CommonFunction;

/**
 * Transaku
 * Author Gebriani
 * on 09, April 2017.
 */

public class ReportInventoryDetailAdapter extends RecyclerView.Adapter<ReportInventoryDetailAdapter.ReportInventoryDetailViewHolder> {

    private List<SellDetailModel> sellDetailModelList = new ArrayList<>();
    private Context context;

    public ReportInventoryDetailAdapter(Context context, List<SellDetailModel> sellDetailModelList) {
        this.sellDetailModelList = sellDetailModelList;
        this.context = context;
    }

    @Override
    public ReportInventoryDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_report_inventory_detail, parent, false);
        return new ReportInventoryDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportInventoryDetailViewHolder holder, int position) {
        SellDetailModel item = sellDetailModelList.get(position);
        holder.tv_inv_name.setText(item.getInventory().getName().substring(0, 1).toUpperCase() + item.getInventory().getName().substring(1));
        holder.tv_inv_price.setText("Harga : "+ CommonFunction.convertCurrencyFormat(item.getPrice()));
        holder.tv_inv_quantity.setText("Jumlah : "+item.getQty() + " " + item.getInventory().getQtyUnit().getName());
        holder.tv_inv_subTotal.setText(CommonFunction.convertCurrencyFormat(item.getSubTotal()));
    }

    @Override
    public int getItemCount() {
        if (sellDetailModelList == null) {
            return 0;
        }
        return sellDetailModelList.size();
    }

    protected static class ReportInventoryDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_inv_name;
        private TextView tv_inv_quantity;
        private TextView tv_inv_price;
        private TextView tv_inv_subTotal;

        protected ReportInventoryDetailViewHolder(View view) {
            super(view);
            tv_inv_name = (TextView) view.findViewById(R.id.adapter_invDetail_name);
            tv_inv_quantity = (TextView) view.findViewById(R.id.adapter_invDetail_quantity);
            tv_inv_price = (TextView) view.findViewById(R.id.adapter_invDetail_price);
            tv_inv_subTotal = (TextView) view.findViewById(R.id.adapter_invDetail_sub_total);
        }
    }
}
