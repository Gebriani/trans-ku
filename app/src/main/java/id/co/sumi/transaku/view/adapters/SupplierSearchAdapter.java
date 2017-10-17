package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.SupplierModel;
import id.co.sumi.transaku.view.activity.SupplierProfileActivity;

/**
 * Transaku
 * Author Gebriani
 * on 15, March 2017.
 */

public class SupplierSearchAdapter extends RecyclerView.Adapter<SupplierSearchAdapter.SupplierSearchHolder>{
    private Context context;
    private List<SupplierModel> supplierModelList = new ArrayList<>();

    public SupplierSearchAdapter(Context context) {
        this.context = context;
    }

    public SupplierSearchAdapter(Context context, List<SupplierModel> supplierModelList) {
        this.context = context;
        this.supplierModelList = supplierModelList;
    }

    public void setSupplierModelList(List<SupplierModel> supplierModelList) {
        this.supplierModelList = supplierModelList;
    }

    @Override
    public SupplierSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_supplier,parent,false);
        return new SupplierSearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SupplierSearchHolder holder, int position) {
        final SupplierModel supplierModel = supplierModelList.get(position);
        holder.tv_supp_name.setText(supplierModel.getName());
        holder.tv_supp_description.setText(supplierModel.getDescription());
        holder.tv_supp_distance.setText(String.format("%.1f", supplierModel.getDistance())+" km");
        if(supplierModel.getPicturePath() != null && !supplierModel.getPicturePath().equalsIgnoreCase("")){
            Glide.with(context)
                    .load(supplierModel.getPicturePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.iv_supp_profilePic);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_logo_placeholder)
                    .into(holder.iv_supp_profilePic);
        }
        holder.bt_supp_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supmodel = TransakuApplication.getInstance().getGson().toJson(supplierModel);
                SupplierProfileActivity.showActivity(context, supmodel);
            }
        });
        holder.cv_supp_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supmodel = TransakuApplication.getInstance().getGson().toJson(supplierModel);
                SupplierProfileActivity.showActivity(context, supmodel);
            }
        });
    }

    @Override
    public int getItemCount() {
       return supplierModelList != null ? supplierModelList.size() : 0;
    }

    protected static class SupplierSearchHolder extends RecyclerView.ViewHolder{
        private ImageView iv_supp_profilePic;
        private TextView tv_supp_name;
        private TextView tv_supp_description;
        private Button bt_supp_order;
        private CardView cv_supp_root;
        private TextView tv_supp_distance;


        public SupplierSearchHolder(View itemView) {
            super(itemView);
            cv_supp_root = (CardView) itemView.findViewById(R.id.adapter_searchSup_root);
            iv_supp_profilePic = (ImageView) itemView.findViewById(R.id.adapter_searchSup_iv_supImageProfile);
            tv_supp_name = (TextView) itemView.findViewById(R.id.adapter_searchSup_tv_suppName);
            tv_supp_description = (TextView) itemView.findViewById(R.id.adapter_searchSup_tv_suppDesc);
            bt_supp_order = (Button) itemView.findViewById(R.id.adapter_searchSup_bt_order);
            tv_supp_distance = (TextView) itemView.findViewById(R.id.adapter_searchSup_tv_distance);

        }
    }
}
