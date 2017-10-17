package id.co.sumi.transaku.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.SupplierModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SearchSupplierMvpView;
import id.co.sumi.transaku.presenter.SearchSupplierPresenter;
import id.co.sumi.transaku.view.adapters.SupplierSearchAdapter;

/**
 * Created by gebriani on 15/03/17.
 */

public class FindSupplierFragment extends BaseFragment implements View.OnClickListener, SearchSupplierMvpView {

    private EditText et_keyword;
    private Spinner spin_search_by;
    private Spinner spin_order_by;
    private Button bt_search;
    private RecyclerView rv_result;
    private ProgressBar progressBar;
    private SearchSupplierPresenter presenter;
    private SupplierSearchAdapter adapter;
    private List<SupplierModel> supplierModelList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_supplier, container, false);
        et_keyword = (EditText) view.findViewById(R.id.frg_findSup_et_keyword);
        spin_search_by = (Spinner) view.findViewById(R.id.frg_findSup_spin_search_by);
        spin_order_by = (Spinner) view.findViewById(R.id.frg_findSup_spin_order_by);
        bt_search = (Button) view.findViewById(R.id.frg_findSup_bt_search);
        rv_result = (RecyclerView) view.findViewById(R.id.ac_find_supp_recycleview);
        progressBar = (ProgressBar) view.findViewById(R.id.ac_find_supp_progressbar);
        rv_result.setLayoutManager(new LinearLayoutManager(rv_result.getContext()));
        bt_search.setOnClickListener(this);

        adapter = new SupplierSearchAdapter(getActivity(), supplierModelList);

        rv_result.setAdapter(adapter);
        presenter = PresenterFactory.searchSupplierPresenter();
        presenter.attachView(this);
        presenter.getAllActiveSupplier();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frg_findSup_bt_search:
                rv_result.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if(spin_search_by.getSelectedItemPosition() == 0){
                    presenter.searchSupplierByInventoryName(et_keyword.getText().toString());
                } else if(spin_search_by.getSelectedItemPosition() == 1){
                    presenter.searchSupplierBySupplierName(et_keyword.getText().toString());
                }
                break;
        }
    }

    @Override
    public void showResponse(boolean isSuceess, String message) {
        progressBar.setVisibility(View.GONE);
        rv_result.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Maaf Data Yang Anda Cari Belum Tersedia", Toast.LENGTH_LONG).show();
    }


    @Override
    public void showResponse(boolean isSuceess, List<SupplierModel> supplierModelList) {
        progressBar.setVisibility(View.GONE);
        if(supplierModelList.size() != 0){
            supplierModelList.clear();
        }
        supplierModelList.addAll(supplierModelList);
        adapter.notifyDataSetChanged();
        hideSoftKeyboard();
    }

    @Override
    public void showSearchResult(boolean isSuccess, List<SupplierModel> supplierModel) {
        progressBar.setVisibility(View.GONE);
        if(supplierModelList.size() != 0){
            supplierModelList.clear();
        }
        supplierModelList.addAll(supplierModel);
        adapter.notifyDataSetChanged();
        rv_result.setVisibility(View.VISIBLE);
        hideSoftKeyboard();
    }
}
