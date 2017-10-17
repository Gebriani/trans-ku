package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.utils.CommonFunction;

/**
 * Created by gebriani on 18/04/17.
 */

public class InventoryBarangAdapter extends RecyclerView.Adapter<InventoryBarangAdapter.InvBarViewHolder>{

    public interface inventoryBarangInterface{
        void showDialogEdit(InventoryModel inventoryModel);
        void showDialogDelete(InventoryModel inventoryModel);
        void showDialogRestock(InventoryModel inventoryModel);
    }

    private List<InventoryModel> inventoryList = new ArrayList<>();
    private Context context;
    private inventoryBarangInterface inventoryBarangInterface;
    private int statusPage;

    public InventoryBarangAdapter(Context context, List<InventoryModel> inventoryList, int status, inventoryBarangInterface inventoryBarangInterface) {
        this.inventoryList = inventoryList;
        this.context = context;
        this.inventoryBarangInterface = inventoryBarangInterface;
        this.statusPage = status;
    }

    @Override
    public InvBarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adater_item_inventory_barang, parent, false);
        return new InvBarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvBarViewHolder holder, int position) {
        final InventoryModel item = inventoryList.get(position);
        if(statusPage == 1){
            holder.cart_button.setVisibility(View.GONE);
        }
        holder.inv_title.setText(item.getName());
        holder.inv_stok.setText("Stok: " + item.getQty() + " " + item.getQtyUnit().getName().toLowerCase());
        holder.inv_harga_beli.setText(CommonFunction.convertCurrencyFormat(item.getBuyPrice()));
        holder.inv_harga_jual.setText(CommonFunction.convertCurrencyFormat(item.getPrice()));

        if(item.getPicturePath() != null && !item.getPicturePath().equalsIgnoreCase("")){
            Glide.with(context)
                    .load(item.getPicturePath())
                    .skipMemoryCache(true)
                    .into(holder.inv_image);
        }

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryBarangInterface.showDialogDelete(item);
            }
        });

        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryBarangInterface.showDialogEdit(item);
            }
        });

        holder.cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryBarangInterface.showDialogRestock(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (inventoryList == null) {
            return 0;
        }
        return inventoryList.size();
    }


    protected static class InvBarViewHolder extends RecyclerView.ViewHolder {
        private ImageView inv_image;
        private Button delete_button;
        private Button edit_button;
        private TextView inv_title;
        private TextView inv_stok;
        private TextView inv_harga_jual;
        private TextView inv_harga_beli;
        private Button cart_button;

        public InvBarViewHolder(View itemView) {
            super(itemView);
            inv_image = (ImageView) itemView.findViewById(R.id.adapter_item_invBar_inventory_image);
            delete_button = (Button) itemView.findViewById(R.id.adapter_item_invBar_delete_button);
            edit_button = (Button) itemView.findViewById(R.id.adapter_item_invBar_edit_button);
            cart_button = (Button) itemView.findViewById(R.id.adapter_item_invBar_cart_button);
            inv_title = (TextView) itemView.findViewById(R.id.adapter_item_invBar_inventory_title);
            inv_stok = (TextView) itemView.findViewById(R.id.adapter_item_invBar_inventory_stock);
            inv_harga_jual = (TextView) itemView.findViewById(R.id.adapter_item_invBar_inventory_harga_jual);
            inv_harga_beli = (TextView) itemView.findViewById(R.id.adapter_item_invBar_inventory_harga_beli);
        }
    }
}