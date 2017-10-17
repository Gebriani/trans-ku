package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.SellDetailModel;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.view.activity.BookingConfirmationActivity;

/**
 * Created on 07/03/17.
 */

public class POSResultAdapter extends RecyclerView.Adapter<POSResultAdapter.POSResultViewHolder> {
    private Context context;

    private List<SellDetailModel> sellDetailModelList = new ArrayList<>();
    private double total_price;
    private List<SellDetailModel> resultSellDetail = new ArrayList<>();

    public POSResultAdapter(Context context, List<SellDetailModel> sellDetailModelsList, double total_price) {
        this.context = context;
        this.sellDetailModelList = sellDetailModelsList;
        this.total_price = total_price;
    }

    @Override
    public POSResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_pos_result, parent, false);
        return new POSResultViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return sellDetailModelList != null ? sellDetailModelList.size() : 0;
    }

    @Override
    public void onBindViewHolder(final POSResultViewHolder holder, final int position) {
        final SellDetailModel selldetail = sellDetailModelList.get(position);
        holder.result_name.setText(selldetail.getInventory().getName());
        holder.result_quantity.setText(String.valueOf((int) selldetail.getQty()));
        holder.result_total_price.setText("Harga : " + CommonFunction.convertCurrencyFormat(selldetail.getPrice()));

        holder.ib_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.result_quantity.setText(String.valueOf((int) selldetail.getQty() + 1));
                addTotal(selldetail);
            }
        });

        holder.ib_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selldetail.getQty() <= 0) {
                    selldetail.setQty(0);
                } else {
                    holder.result_quantity.setText(String.valueOf((int) selldetail.getQty() - 1));
                    deleteTotal(selldetail);
                }

            }
        });
    }


    private void deleteTotal(SellDetailModel sellDetailModel) {
        sellDetailModel.setQty(sellDetailModel.getQty() - 1);
        sellDetailModel.setSubTotal(sellDetailModel.getQty() * sellDetailModel.getPrice());
//        POSResultActivity.editTotalHargaBarang(total_price - sellDetailModel.getPrice());
        BookingConfirmationActivity.editTotalHargaBarang(total_price - sellDetailModel.getPrice());
        setTotal_price(total_price - sellDetailModel.getPrice());
        notifyDataSetChanged();
    }


    private void addTotal(SellDetailModel sellDetailModel) {
        sellDetailModel.setQty(sellDetailModel.getQty() + 1);
        sellDetailModel.setSubTotal(sellDetailModel.getQty() * sellDetailModel.getPrice());
//        POSResultActivity.editTotalHargaBarang(total_price + sellDetailModel.getPrice());
        BookingConfirmationActivity.editTotalHargaBarang(total_price + sellDetailModel.getPrice());
        setTotal_price(total_price + sellDetailModel.getPrice());
        notifyDataSetChanged();
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }


    public List<SellDetailModel> getSellDetailModelList() {
        return resultSellDetail;

    }

    public int getTotalResult() {
        for (SellDetailModel temp : sellDetailModelList) {
            if (temp.getQty() != 0) {
                resultSellDetail.add(temp);
            }
        }
        return resultSellDetail.size();
    }

    protected static class POSResultViewHolder extends RecyclerView.ViewHolder {

        private TextView result_name;
        private TextView result_quantity;
        private TextView result_total_price;
        private ImageButton ib_plus;
        private ImageButton ib_minus;

        public POSResultViewHolder(View itemView) {
            super(itemView);
            result_name = (TextView) itemView.findViewById(R.id.adapter_pos_result_name);
            result_quantity = (TextView) itemView.findViewById(R.id.adapter_pos_result_quantity);
            result_total_price = (TextView) itemView.findViewById(R.id.adapter_pos_result_total_harga);
            ib_minus = (ImageButton) itemView.findViewById(R.id.adapter_pos_result_minus_button);
            ib_plus = (ImageButton) itemView.findViewById(R.id.adapter_pos_result_plus_button);
        }
    }


//    private void showDeleteAll(final int position, final SellDetailModel sellDetailModel) {
//        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_delete_all_item_pos, null);
//        dialogBuilder.setView(view);
//        Button batal = (Button) view.findViewById(R.id.dialog_delete_item_pos_cancel);
//        Button deleteAll = (Button) view.findViewById(R.id.dialog_delete_item_pos_deleteAll);
//        Button deleteAmount = (Button) view.findViewById(R.id.dialog_delete_item_pos_deleteAmount);
//        final AlertDialog alertDialog = dialogBuilder.create();
//
//        batal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//
//        deleteAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                double totalPriceNow = total_price - sellDetailModelList.get(position).getSubTotal();
//                POSResultActivity.editTotalHargaBarang(totalPriceNow);
//                sellDetailModelList.remove(sellDetailModelList.get(position));
//                notifyDataSetChanged();
//                setTotal_price(totalPriceNow);
//
//            }
//        });
//
//        deleteAmount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                showDeleteAmount(sellDetailModel);
//            }
//        });
//
//        alertDialog.show();
//    }
//

//
//    private void showDeleteAmount(final SellDetailModel sellDetailModel) {
//        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_quantity_pos_result, null);
//        dialogBuilder.setView(view);
//        final EditText quantity = (EditText) view.findViewById(R.id.dialog_edit_pos_quantity);
//        Button deleteAmount = (Button) view.findViewById(R.id.dialog_edit_pos_deleteSomeAmount);
//        final AlertDialog alertDialog = dialogBuilder.create();
//
//        deleteAmount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!quantity.getText().toString().trim().isEmpty() && quantity.getText().toString() != "0") {
//                    alertDialog.dismiss();
//                    double qtyNow = sellDetailModel.getQty() - Double.parseDouble(quantity.getText().toString());
//                    double price_reduction = Double.parseDouble(quantity.getText().toString()) * sellDetailModel.getPrice();
//                    sellDetailModel.setQty(qtyNow);
//                    sellDetailModel.setSubTotal(qtyNow * sellDetailModel.getPrice());
//                    POSResultActivity.editTotalHargaBarang(total_price - price_reduction);
//                    setTotal_price(total_price - price_reduction);
//                    notifyDataSetChanged();
//                }
//            }
//        });
//        alertDialog.show();
//    }

}
