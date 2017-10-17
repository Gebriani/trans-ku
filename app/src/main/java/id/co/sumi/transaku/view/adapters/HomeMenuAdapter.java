package id.co.sumi.transaku.view.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.HomeMenu;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.view.activity.BarangBeliActivity;
import id.co.sumi.transaku.view.activity.BarangJualActivity;
import id.co.sumi.transaku.view.activity.FindSupplierActivity;
import id.co.sumi.transaku.view.activity.InventoryActivity;
import id.co.sumi.transaku.view.activity.POSActivity;
import id.co.sumi.transaku.view.activity.ReportBaseActivity;
import id.co.sumi.transaku.view.activity.SellerProfileActivity;


// gebriani
public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.HomeMenuHolder>{

    private Context context;
    private List<HomeMenu> menuList = new ArrayList<>();

    public HomeMenuAdapter(Context context, List<HomeMenu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public HomeMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_menu_home, parent, false);
        return new HomeMenuHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeMenuHolder holder, int position) {
        final HomeMenu menu = menuList.get(position);

        holder.title.setText(menu.getTitle());
        holder.icon.setImageResource(menu.getIcon());

        holder.root_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (menu.getType()){
                    case "pos":
                        POSActivity.showActivity(context, Const.page_flag_top);
                        break;
                    case "inventory":
                        InventoryActivity.showActivity(context);
                        break;
                    case "report":
                        ReportBaseActivity.showActivity(context);
//                        LabaRugiReportActivity.showActivity(context);
                        break;
                    case "buy_goods":
                        BarangBeliActivity.showActivity(context);
                        break;
                    case "sell_goods":
                        BarangJualActivity.showActivity(context);
                        break;
                    case "find_supplier":
                        FindSupplierActivity.showActivity(context);
                        break;
                    case "main_data":
                        SellerProfileActivity.showActivity(context);
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    protected static class HomeMenuHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected ImageView icon;
        protected CardView root_card;

        protected HomeMenuHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_menu_title);
            icon = (ImageView) itemView.findViewById(R.id.item_menu_icon);
            root_card = (CardView) itemView.findViewById(R.id.item_menu_card);
        }
    }
}
