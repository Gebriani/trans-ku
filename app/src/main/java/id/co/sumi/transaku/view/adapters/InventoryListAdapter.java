package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.utils.CommonFunction;

/**
 * Created on 02/03/17.
 */

public class InventoryListAdapter  extends RecyclerView.Adapter<InventoryListAdapter.InventoryListHolder>{

    public interface OnItemClickListener{
        void onItemClick(InventoryModel item);
    }

    private List<InventoryModel> inventoryList = new ArrayList<>();
    private Context context;
    private final OnItemClickListener listener;

    public InventoryListAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setInventoryList(List<InventoryModel> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @Override
    public InventoryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_inventory_list,parent,false);
        return new InventoryListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InventoryListHolder holder, int position) {
        holder.bind(inventoryList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        if(inventoryList == null){
            return 0;
        }
        return inventoryList.size();
    }

    protected static class InventoryListHolder extends RecyclerView.ViewHolder{
        private LinearLayout root_data;
        private TextView name_goods;
        private TextView store_stock;
        private TextView price_goods;
        private TextView buy_price;

        protected InventoryListHolder(View itemView) {
            super(itemView);
            root_data = (LinearLayout) itemView.findViewById(R.id.adapter_invenList_root_data);
            name_goods = (TextView) itemView.findViewById(R.id.adapter_invenList_nameGoods);
            store_stock = (TextView) itemView.findViewById(R.id.adapter_invenList_storeStock);
            price_goods = (TextView) itemView.findViewById(R.id.adapter_invenList_priceGoods);
            buy_price = (TextView) itemView.findViewById(R.id.adapter_invenList_buyPriceGoods);
        }

        protected void bind(final InventoryModel inventory, final OnItemClickListener listener, int position){
            if(position % 2 == 0){
                root_data.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                root_data.setBackgroundColor(Color.parseColor("#BDBDBD"));
            }
            name_goods.setText(inventory.getName().substring(0,1).toUpperCase() + inventory.getName().substring(1));
            if(inventory.getQtyUnit() != null && !inventory.getQtyUnit().getName().trim().isEmpty()){
                store_stock.setText(inventory.getQty() + " " + inventory.getQtyUnit().getName().toLowerCase());
            } else {
                store_stock.setText(String.valueOf(inventory.getQty()));
            }
            price_goods.setText(CommonFunction.convertCurrencyFormat(inventory.getPrice()));
            buy_price.setText(CommonFunction.convertCurrencyFormat(inventory.getBuyPrice()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(inventory);
                }
            });
        }

    }
}