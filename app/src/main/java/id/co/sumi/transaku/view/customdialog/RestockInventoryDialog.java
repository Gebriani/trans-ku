package id.co.sumi.transaku.view.customdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.QtyUnitModel;
import id.co.sumi.transaku.presenter.AddInventoryMvpView;
import id.co.sumi.transaku.presenter.AddInventoryPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;

/**
 * Created by alodokter on 26/04/17.
 */

public class RestockInventoryDialog extends DialogFragment implements View.OnClickListener, AddInventoryMvpView {

    public interface DialogRestockInterface{
        void dialogRestockFinish();
    }

    private static String INV_STRING = "inventory";

    private TextView tvInventoryName;
    private TextView tvInventoryCategory;
    private TextView tvQuantityUnit;
    private TextView tvChangeQUnit;
    private EditText etHargaJual;
    private EditText etHargaBeli;
    private EditText etQuantity;
    private Spinner spQuantityUnit;
    private Button btCancel;
    private Button btSubmit;
    private InventoryModel inventoryItem;
    private AddInventoryPresenter presenter;
    private PopulateCombo populateComboItem;
    private CustomSpinnerQtyUnits customSpinnerQtyUnits;
    private boolean isQuantityChange = false;

    private DialogRestockInterface dialogRestockInterface;

    public static RestockInventoryDialog newInstance(String inventory, DialogRestockInterface dialogRestockInterface) {
        RestockInventoryDialog dialog = new RestockInventoryDialog();
        Bundle bundle = new Bundle();
        bundle.putString(INV_STRING, inventory);
        dialog.setArguments(bundle);
        dialog.dialogRestockInterface = dialogRestockInterface;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Sumi_DialogBox);
        inventoryItem = TransakuApplication.getInstance().getGson().fromJson(getArguments().getString(INV_STRING), InventoryModel.class);
        presenter = new AddInventoryPresenter();
        presenter.attachView(this);
        populateComboItem = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);
        customSpinnerQtyUnits = new CustomSpinnerQtyUnits(getActivity(), populateComboItem.getQtyUnits());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_restock_inventory, container, false);
        tvInventoryName = (TextView) view.findViewById(R.id.dialog_restock_name);
        tvInventoryCategory = (TextView) view.findViewById(R.id.dialog_restock_category_name);
        etQuantity = (EditText) view.findViewById(R.id.dialog_restock_quantity);
        etHargaBeli = (EditText) view.findViewById(R.id.dialog_restock_buy_price);
        etHargaJual = (EditText) view.findViewById(R.id.dialog_restock_sell_price);
        tvQuantityUnit = (TextView) view.findViewById(R.id.dialog_restock_qty_unit_name);
        tvChangeQUnit = (TextView) view.findViewById(R.id.dialog_restock_change_unit_name);
        spQuantityUnit = (Spinner) view.findViewById(R.id.dialog_restock_quantity_unit_spinner);
        btCancel = (Button) view.findViewById(R.id.dialog_restock_cancel_button);
        btSubmit = (Button) view.findViewById(R.id.dialog_restock_submit_button);
        spQuantityUnit.setAdapter(customSpinnerQtyUnits);
        isQuantityChange = false;

        btCancel.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        tvChangeQUnit.setOnClickListener(this);

        tvInventoryName.setText("Nama : "+inventoryItem.getName());
        tvInventoryCategory.setText("Kategory : " +inventoryItem.getCategory().getName());
        etHargaBeli.setText(String.valueOf(inventoryItem.getBuyPrice()));
        etHargaJual.setText(String.valueOf(inventoryItem.getPrice()));
        tvQuantityUnit.setText(inventoryItem.getQtyUnit().getName());

        spQuantityUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isQuantityChange= true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_restock_cancel_button:
                dismiss();
                break;
            case R.id.dialog_restock_change_unit_name:
                spQuantityUnit.setVisibility(View.VISIBLE);
                break;
            case R.id.dialog_restock_submit_button:
                //TODO add code submit here
                if(validateQuantity() && validateSellPrice() && validateBuyPrice()){
                    if(isQuantityChange){
                        inventoryItem.setQtyUnit((QtyUnitModel) spQuantityUnit.getSelectedItem());
                    }
                    inventoryItem.setBuyPrice(Double.parseDouble(etHargaBeli.getText().toString().trim()));
                    inventoryItem.setPrice(Double.parseDouble(etHargaJual.getText().toString().trim()));
                    inventoryItem.setQty(Double.parseDouble(etQuantity.getText().toString().trim()));
                    presenter.restockInventory(inventoryItem);
                }
                break;
        }
    }

    private boolean validateQuantity(){
        if(etQuantity.getText().toString().trim().isEmpty()){
            etQuantity.setError("Tidak boleh kosong");
            return false;
        } else{
            return true;
        }
    }

    private boolean validateSellPrice(){
        if(etHargaJual.getText().toString().trim().isEmpty()){
            etHargaJual.setError("Tidak boleh kosong");
            return false;
        } else{
            return true;
        }
    }

    private boolean validateBuyPrice(){
        if(etHargaBeli.getText().toString().trim().isEmpty()){
            etHargaBeli.setError("Tidak boleh kosong");
            return false;
        } else{
            return true;
        }
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void showDialogAfterAddInventory(boolean isSuccess, String message) {

    }

    @Override
    public void dismissDialog() {
        Toast.makeText(getActivity(), "Barang berhasil di tambahkan", Toast.LENGTH_LONG).show();
        dialogRestockInterface.dialogRestockFinish();
        dismiss();
    }

    @Override
    public void showDialogAfterRestockInventory(boolean isSuccess, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
