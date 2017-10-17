package id.co.sumi.transaku.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.sumi.transaku.R;

/**
 * Created by gebriani on 7/4/17.
 */

public class POSInventoryViewHolder extends RecyclerView.ViewHolder {
    protected ImageView inv_pic;
    protected TextView inv_name;
    protected TextView inv_price;
    protected Button inv_add_button;
    protected LinearLayout root_sum_button;
    protected TextView inv_sum;
    protected ImageButton bt_minus;
    protected ImageButton bt_plus;
    protected TextView tvInputManual;

    protected POSInventoryViewHolder(View itemView) {
        super(itemView);
        inv_pic = (ImageView) itemView.findViewById(R.id.adapter_pi_invPic);
        inv_name = (TextView) itemView.findViewById(R.id.adapter_pi_invName);
        inv_price = (TextView) itemView.findViewById(R.id.adapter_pi_invPrice);
        inv_add_button = (Button) itemView.findViewById(R.id.adapter_pi_add_button);
        root_sum_button = (LinearLayout) itemView.findViewById(R.id.adapter_pi_root_sum_button);
        bt_minus = (ImageButton) itemView.findViewById(R.id.adapter_pi_minus_button);
        bt_plus = (ImageButton) itemView.findViewById(R.id.adapter_pi_plus_button);
        inv_sum = (TextView) itemView.findViewById(R.id.adapter_pi_quantity);
        tvInputManual = (TextView) itemView.findViewById(R.id.adapter_pi_input_sum_manual);
    }

}

