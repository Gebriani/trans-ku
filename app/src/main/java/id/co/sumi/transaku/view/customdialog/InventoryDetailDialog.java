package id.co.sumi.transaku.view.customdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.SellDetailModel;
import id.co.sumi.transaku.view.adapters.ReportInventoryDetailAdapter;

/**
 * Transaku
 * Author Gebriani
 * on 09, April 2017.
 */

public class InventoryDetailDialog extends DialogFragment {

    private final static String INVENTORY_DETAIL_ARRAY = "inventory_details";

    private Button bt_closed;
    private RecyclerView rv_result;
    private ReportInventoryDetailAdapter adapter;
    private List<SellDetailModel> sellDetailModelList = new ArrayList<>();

    public static InventoryDetailDialog newInstance(String inv_str){
        InventoryDetailDialog dialog = new InventoryDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putString(INVENTORY_DETAIL_ARRAY, inv_str);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Sumi_DialogBox);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_inventory_detail, container, false);
        bt_closed = (Button) view.findViewById(R.id.dialog_invDetail_ok_button);
        rv_result = (RecyclerView) view.findViewById(R.id.dialog_invDetail_rv_result);
        rv_result.setLayoutManager(new LinearLayoutManager(rv_result.getContext()));
        SellDetailModel[] sellDetailModelsArray = TransakuApplication.getInstance().getGson().fromJson(getArguments().getString(INVENTORY_DETAIL_ARRAY), SellDetailModel[].class);
        sellDetailModelList = Arrays.asList(sellDetailModelsArray);
        adapter = new ReportInventoryDetailAdapter(getActivity(), sellDetailModelList);
        rv_result.setAdapter(adapter);
        bt_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
