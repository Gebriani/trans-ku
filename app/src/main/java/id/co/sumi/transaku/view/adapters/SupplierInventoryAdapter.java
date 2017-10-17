package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.SellDetailModel;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.model.UserModel;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.activity.SupplierInventoryDetailActivity;

/**
 * Transaku
 * Author Gebriani
 * on 17, March 2017.
 */

public class SupplierInventoryAdapter extends RecyclerView.Adapter<SupplierInventoryAdapter.SupplierInventoryViewHolder> {

    private List<InventoryModel> inventoryModels = new ArrayList<>();
    private Context context;
    private double st_total_price;
    private int sum_qty_pos;
    private List<InventoryModel> listResult = new ArrayList<>();
    private List<SellDetailModel> sellDetailModelList = new ArrayList<>();
    private SellModel sellResultList = null;

    public SupplierInventoryAdapter(Context context) {
        this.context = context;
    }

    public void setInventoryModels(List<InventoryModel> inventoryModels) {
        this.inventoryModels = inventoryModels;
    }

    @Override
    public SupplierInventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_supplier_inventory_user_order, parent, false);
        return new SupplierInventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SupplierInventoryViewHolder holder, final int position) {
        final InventoryModel item = inventoryModels.get(position);
        holder.order_name.setText(item.getName());
        holder.order_stock.setText(String.valueOf(item.getQty()));
        holder.order_price.setText(CommonFunction.convertCurrencyFormat(item.getPrice()));

        if(item.getTemp_total() == 0){
            holder.order_sum_total.setText("0");
            holder.order_checkbox.setChecked(false);
        } else {
            holder.order_sum_total.setText(String.valueOf(item.getTemp_total()));
            holder.order_checkbox.setChecked(true);
        }
        holder.order_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showDialogAdd(item, holder.order_sum_total, position);
                } else {
                    for(InventoryModel result: listResult){
                        if(result.getId() == item.getId()){

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (inventoryModels.size() == 0) {
            return 0;
        } else {
            return inventoryModels.size();
        }
    }

    protected static class SupplierInventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView order_name;
        private TextView order_stock;
        private TextView order_price;
        private TextView order_sum_total;
        private CheckBox order_checkbox;

        public SupplierInventoryViewHolder(View itemView) {
            super(itemView);
            order_name = (TextView) itemView.findViewById(R.id.adapter_suppInv_order_name);
            order_stock = (TextView) itemView.findViewById(R.id.adapter_suppInv_order_stock);
            order_price = (TextView) itemView.findViewById(R.id.adapter_suppInv_order_price);
            order_sum_total = (TextView) itemView.findViewById(R.id.adapter_suppInv_order_sum_order);
            order_checkbox = (CheckBox) itemView.findViewById(R.id.adapter_suppInv_checkbox);
        }
    }

    public List<InventoryModel> getTotalList() {
        return listResult;
    }

    public SellModel getSellResult(){
        UserModel userModel = new UserModel(Long.parseLong(PrefHelper.getString(Const.ID)));
        sellResultList = new SellModel(sellDetailModelList,userModel,st_total_price);
        return sellResultList;
    }

    public double getTotalPrice() {
        return st_total_price;
    }

    private void showDialogAdd(final InventoryModel inventoryModel, final TextView tv_sum_total, final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_pos_inventory, null);
        dialogBuilder.setView(view);
        final EditText add_quantity = (EditText) view.findViewById(R.id.dialog_add_pos_quantity);

        dialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!add_quantity.getText().toString().trim().isEmpty() && add_quantity.getText().toString() != "0"){
                            double qty = Double.parseDouble(add_quantity.getText().toString());
                            sum_qty_pos += qty;
                            double subTotal =  inventoryModel.getPrice() * qty;
                            st_total_price += subTotal;
                            tv_sum_total.setText(String.valueOf(sum_qty_pos));

                            listResult.add(inventoryModel);

                            SellDetailModel sellDetailModel = new SellDetailModel(inventoryModel, qty, inventoryModel.getPrice(), subTotal);
                            sellDetailModelList.add(sellDetailModel);

                            inventoryModel.setTemp_total((int) qty);
                            notifyItemChanged(position);
                            SupplierInventoryDetailActivity.showPopUp(sum_qty_pos);
                        } else {
                            Toast.makeText(context, "Mohon isi jumlah barang", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
