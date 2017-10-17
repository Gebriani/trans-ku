package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import id.co.sumi.transaku.view.activity.POSActivity;

/**
 * Created on 06/03/17.
 */

public class POSInventoryAdapter extends RecyclerView.Adapter<POSInventoryViewHolder> {

    private Context context;
    private List<InventoryModel> inventoryModelList = new ArrayList<>();
    private double st_total_price;
    private int sum_qty_pos;
    private List<InventoryModel> listResult = new ArrayList<>();
    private SellModel sellResultList = null;
    private List<SellDetailModel> sellDetailModelList = new ArrayList<>();
    private int status_page;

    public POSInventoryAdapter(Context context, List<InventoryModel> inventoryModelList) {
        this.context = context;
        this.inventoryModelList = inventoryModelList;
    }

    @Override
    public POSInventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_pos_inventory, parent, false);
        return new POSInventoryViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return inventoryModelList != null ? inventoryModelList.size() : 0;
    }

    @Override
    public void onBindViewHolder(final POSInventoryViewHolder holder, final int position) {
        final InventoryModel inventoryModel = inventoryModelList.get(position);
        holder.inv_name.setText(inventoryModel.getName().substring(0, 1).toUpperCase() + inventoryModel.getName().substring(1));
        holder.inv_price.setText(CommonFunction.convertCurrencyFormat(inventoryModel.getPrice()));
        if (inventoryModel.getTemp_total() == 0) {
            holder.inv_add_button.setVisibility(View.VISIBLE);
            holder.root_sum_button.setVisibility(View.GONE);
            holder.tvInputManual.setVisibility(View.GONE);
        } else {
            holder.inv_add_button.setVisibility(View.GONE);
            holder.root_sum_button.setVisibility(View.VISIBLE);
            holder.tvInputManual.setVisibility(View.VISIBLE);
        }

        if (inventoryModel.getPicturePath() != null) {
            Glide.with(context)
                    .load(inventoryModel.getPicturePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.inv_pic);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_logo_placeholder)
                    .into(holder.inv_pic);
        }

        holder.inv_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryModel.setTemp_total(inventoryModel.getTemp_total() + 1);
                holder.inv_sum.setText(String.valueOf(inventoryModel.getTemp_total()));
                holder.inv_add_button.setVisibility(View.GONE);
                holder.root_sum_button.setVisibility(View.VISIBLE);
                holder.tvInputManual.setVisibility(View.VISIBLE);
                addInventory(inventoryModel, 1);

            }
        });

        holder.bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryModel.setTemp_total(inventoryModel.getTemp_total() + 1);
                holder.inv_sum.setText(String.valueOf(inventoryModel.getTemp_total()));
                addInventory(inventoryModel, 1);
            }
        });

        holder.bt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inventoryModel.getTemp_total() <= 1) {
                    inventoryModel.setTemp_total(0);
                    holder.inv_add_button.setVisibility(View.VISIBLE);
                    holder.root_sum_button.setVisibility(View.GONE);
                } else {
                    inventoryModel.setTemp_total(inventoryModel.getTemp_total() - 1);
                    holder.inv_sum.setText(String.valueOf(inventoryModel.getTemp_total()));
                }
                deleteInventory(inventoryModel, 1);
            }
        });

        holder.tvInputManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd(inventoryModel, holder.inv_sum);
            }
        });

    }

    private void addInventory(InventoryModel inventoryModel, int sumAdd) {
        sum_qty_pos = sum_qty_pos + sumAdd;
        st_total_price = st_total_price + (inventoryModel.getPrice() * sumAdd);
        POSActivity.showPopUp(st_total_price, sum_qty_pos);
    }

    private void deleteInventory(InventoryModel inventoryModel, int sumDel) {
        sum_qty_pos = sum_qty_pos - sumDel;
        st_total_price = st_total_price - (inventoryModel.getPrice() * sumDel);
        POSActivity.showPopUp(st_total_price, sum_qty_pos);
    }

    public int getResultTotal() {
        listResult = new ArrayList<>();
        for (InventoryModel inventoryModel : inventoryModelList) {
            if (inventoryModel.getTemp_total() != 0) {
                listResult.add(inventoryModel);
            }
        }
        return listResult.size();
    }

    public double getTotalPrice() {
        return st_total_price;
    }

    public List<InventoryModel> getAllListInventory() {
        return inventoryModelList;
    }

    public List<InventoryModel> getListResult() {
        return listResult;
    }

    public int getTotalInventory(){
        return sum_qty_pos;
    }

    private void showDialogAdd(final InventoryModel invModel, final TextView tvSum) {
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
                        int qty = Integer.parseInt(add_quantity.getText().toString()) + invModel.getTemp_total();
                        invModel.setTemp_total(qty);
                        tvSum.setText(String.valueOf(invModel.getTemp_total()));
                        addInventory(invModel, Integer.parseInt(add_quantity.getText().toString()));

                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


}