package id.co.sumi.transaku.view.customdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryCategoryModel;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.QtyUnitModel;
import id.co.sumi.transaku.presenter.AddInventoryMvpView;
import id.co.sumi.transaku.presenter.AddInventoryPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;

/**
 * Created on 01/03/17.
 */

public class AddNewInventoryDialog extends DialogFragment implements View.OnClickListener, AddInventoryMvpView {

    public interface InventoryDialogInterface {
        void finishDialog();
    }

    private final static String TAG = AddNewInventoryDialog.class.getSimpleName();

    private final static String INVENTORY_STRING = "inventory_string";
    private final static String PAGE_FROM = "page_from";

    private final static String FLAG = "flag";
    private final static String RESTOCK_FLAG = "restock";
    private final static String PAGE_FROM_FLAG = "jual";


    private EditText et_name;
    private Spinner spin_category;
    private EditText et_description;
    private EditText etSellPrice;
    private EditText etBuyPrice;
    private EditText et_quantity;
    private EditText et_weight;
    private Spinner spinCondition;
    private Button btCancel;
    private Button btSubmit;
    private Spinner spin_qty_unit;
    private Switch swt_isForSell;
    private TextInputLayout til_buy_price;

    private AddInventoryPresenter presenter;
    private InventoryModel inventoryModel;
    private InventoryModel tempInvent;

    private String flag, pageFrom  = "";
    private CustomSpinnerInventoryCategories customSpinnerInventoryCategories;
    private CustomSpinnerQtyUnits customSpinnerQtyUnits;
    private int isForSellValue = 1;
    private double tempWeigth = 0;
    private PopulateCombo populateCombo;

    private InventoryDialogInterface dialogInterface;

    public static AddNewInventoryDialog newInstance(String inventoryStr, String page, InventoryDialogInterface inventoryDialogInterface) {
        AddNewInventoryDialog dialog = new AddNewInventoryDialog();
        Bundle bundle = new Bundle();
        bundle.putString(INVENTORY_STRING, inventoryStr);
        bundle.putString(FLAG, RESTOCK_FLAG);
        bundle.putString(PAGE_FROM, page);
        dialog.setArguments(bundle);
        dialog.dialogInterface = inventoryDialogInterface;
//        dialog.setCancelable(false);
        return dialog;
    }

    public static AddNewInventoryDialog newInstance(String page, InventoryDialogInterface inventoryDialogInterface) {
        AddNewInventoryDialog dialog = new AddNewInventoryDialog();
        Bundle bundle = new Bundle();
        bundle.putString(FLAG, "addNew");
        bundle.putString(PAGE_FROM, page);
        dialog.setArguments(bundle);
        dialog.dialogInterface = inventoryDialogInterface;
//        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AddInventoryPresenter();
        presenter.attachView(this);
        flag = getArguments().getString(FLAG);
        pageFrom = getArguments().getString(PAGE_FROM);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Sumi_DialogBox);
        if (flag.equalsIgnoreCase(RESTOCK_FLAG)) {
            tempInvent = TransakuApplication.getInstance().getGson().fromJson(getArguments().getString(INVENTORY_STRING), InventoryModel.class);
        }
        populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_inventory, container, false);
        et_name = (EditText) view.findViewById(R.id.dialog_add_et_nama);
        spin_category = (Spinner) view.findViewById(R.id.dialog_add_sp_kategori);
        spin_qty_unit = (Spinner) view.findViewById(R.id.dialog_add_sp_qty_unit);
        et_description = (EditText) view.findViewById(R.id.dialog_add_et_deskripsi);
        swt_isForSell = (Switch) view.findViewById(R.id.dialog_add_isForSell);
        etSellPrice = (EditText) view.findViewById(R.id.dialog_add_et_sell_price);
        et_quantity = (EditText) view.findViewById(R.id.dialog_add_et_jumlah);
        et_weight = (EditText) view.findViewById(R.id.dialog_add_et_berat);
        spinCondition = (Spinner) view.findViewById(R.id.dialog_add_sp_kondisi);
        btCancel = (Button) view.findViewById(R.id.dialog_add_bt_cancel);
        btSubmit = (Button) view.findViewById(R.id.dialog_add_bt_simpan);
        etBuyPrice = (EditText) view.findViewById(R.id.dialog_add_et_buy_price) ;
        til_buy_price = (TextInputLayout) view.findViewById(R.id.dialog_add_textinput_buyprice);

        btCancel.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        swt_isForSell.setChecked(true);

        if(pageFrom.equalsIgnoreCase(PAGE_FROM_FLAG)){
            swt_isForSell.setChecked(true);
            swt_isForSell.setVisibility(View.GONE);
            isForSellValue = 1;
            til_buy_price.setVisibility(View.GONE);
        } else {
            swt_isForSell.setVisibility(View.VISIBLE);
            til_buy_price.setVisibility(View.VISIBLE);
        }

        //TODO give validation later
        customSpinnerQtyUnits = new CustomSpinnerQtyUnits(getActivity(), populateCombo.getQtyUnits());
        spin_qty_unit.setAdapter(customSpinnerQtyUnits);
        customSpinnerInventoryCategories = new CustomSpinnerInventoryCategories(getActivity(), populateCombo.getInventoryCategories());
        spin_category.setAdapter(customSpinnerInventoryCategories);

        swt_isForSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isForSellValue = 1;
                    etSellPrice.setVisibility(View.VISIBLE);
                } else {
                    isForSellValue = 0;
                    etSellPrice.setVisibility(View.GONE);
                }
            }
        });

        if (flag.equalsIgnoreCase(RESTOCK_FLAG)) {
            et_name.setText(tempInvent.getName());
            et_description.setText(tempInvent.getDescription());
            etSellPrice.setText(String.valueOf(tempInvent.getPrice()));
            et_weight.setText(String.valueOf(tempInvent.getWeight()));
            et_quantity.setText(String.valueOf(tempInvent.getQty()));
            etBuyPrice.setText(String.valueOf(tempInvent.getBuyPrice()));
            if (tempInvent.getIsItemToSell() == 1) {
                swt_isForSell.setChecked(true);
                isForSellValue = 1;
            } else {
                swt_isForSell.setChecked(false);
                isForSellValue = 0;
            }
            spin_category.setSelection(customSpinnerInventoryCategories.getPosition(tempInvent.getCategory()));
            spin_qty_unit.setSelection(customSpinnerQtyUnits.getPosition(tempInvent.getQtyUnit()));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_add_bt_cancel:
                dismiss();
                break;
            case R.id.dialog_add_bt_simpan:
                if(pageFrom.equalsIgnoreCase(PAGE_FROM_FLAG)){
                    if (validateName() && validatePrice() && validateQuantity()) {
                        if (flag.equalsIgnoreCase(RESTOCK_FLAG)) {
                            sendRestockBarangForBarangJual();
                        } else {
                            sendNewStockBarangForBarangJual();
                        }

                    }
                } else {
                    if (validateName() && validatePrice() && validateQuantity() && validateBuyPrice()) {
                        if (flag.equalsIgnoreCase(RESTOCK_FLAG)) {
                            sendRestockBarangtoBarangBeli();
                        } else {
                            sendNewStockBarangtoBarangBeli();
                        }
                    }
                }

                break;
        }
    }


    private void sendRestockBarangtoBarangBeli() {
        if(swt_isForSell.isChecked()){
            isForSellValue = 1;
        } else {
            isForSellValue = 0;
        }

//        if(et_weight.getText().toString().trim().isEmpty()){
//            tempWeigth = 0;
//        } else {
//            tempWeigth = Double.valueOf(et_weight.getText().toString());
//        }

        tempWeigth = 0;

        inventoryModel = new InventoryModel(new InventoryCategoryModel(((InventoryCategoryModel) spin_category.getSelectedItem()).getDescription()
                , ((InventoryCategoryModel) spin_category.getSelectedItem()).getId()
                , ((InventoryCategoryModel) spin_category.getSelectedItem()).getName())
                , tempInvent.getCode()
                , Long.valueOf(PrefHelper.getString(Const.ID))
                , Long.valueOf(tempInvent.getId())
                , et_description.getText().toString()
                , spinCondition.getSelectedItemPosition() == 0 ? "Baru" : "Bekas"
                , isForSellValue
                , et_name.getText().toString()
                , et_description.getText().toString()
                , Double.valueOf(etSellPrice.getText().toString())
                , Double.valueOf(et_quantity.getText().toString())
                , tempWeigth
                , new QtyUnitModel(((QtyUnitModel) spin_qty_unit.getSelectedItem()).getId()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getName()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getDescription())
                , Double.valueOf(etBuyPrice.getText().toString()));
        presenter.restockInventory(inventoryModel);
    }

    private void sendNewStockBarangtoBarangBeli() {
        if(swt_isForSell.isChecked()){
            isForSellValue = 1;
        } else {
            isForSellValue = 0;
        }

//        if(et_weight.getText().toString().trim().isEmpty()){
//            tempWeigth = 0;
//        } else {
//            tempWeigth = Double.valueOf(et_weight.getText().toString());
//        }

        inventoryModel = new InventoryModel(new InventoryCategoryModel(((InventoryCategoryModel) spin_category.getSelectedItem()).getDescription(),
                ((InventoryCategoryModel) spin_category.getSelectedItem()).getId(), ((InventoryCategoryModel) spin_category.getSelectedItem()).getName())
                , et_name.getText().toString()
                , Long.valueOf(PrefHelper.getString(Const.ID))
                , et_description.getText().toString()
                , spinCondition.getSelectedItemPosition() == 0 ? "Baru" : "Bekas"
                , isForSellValue
                , et_name.getText().toString()
                , et_description.getText().toString()
                , Double.valueOf(etSellPrice.getText().toString())
                , Double.valueOf(et_quantity.getText().toString())
                , tempWeigth
                , new QtyUnitModel(((QtyUnitModel) spin_qty_unit.getSelectedItem()).getId()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getName()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getDescription())
                , Double.valueOf(etBuyPrice.getText().toString()));
        presenter.restockInventory(inventoryModel);
    }

    private void sendRestockBarangForBarangJual() {
        if(swt_isForSell.isChecked()){
            isForSellValue = 1;
        } else {
            isForSellValue = 0;
        }

//        if(et_weight.getText().toString().trim().isEmpty()){
//            tempWeigth = 0;
//        } else {
//            tempWeigth = Double.valueOf(et_weight.getText().toString());
//        }

        inventoryModel = new InventoryModel(new InventoryCategoryModel(((InventoryCategoryModel) spin_category.getSelectedItem()).getDescription()
                , ((InventoryCategoryModel) spin_category.getSelectedItem()).getId()
                , ((InventoryCategoryModel) spin_category.getSelectedItem()).getName())
                , tempInvent.getCode()
                , Long.valueOf(PrefHelper.getString(Const.ID))
                , Long.valueOf(tempInvent.getId())
                , et_description.getText().toString()
                , spinCondition.getSelectedItemPosition() == 0 ? "Baru" : "Bekas"
                , isForSellValue
                , et_name.getText().toString()
                , et_description.getText().toString()
                , Double.valueOf(etSellPrice.getText().toString())
                , Double.valueOf(et_quantity.getText().toString())
                , tempWeigth
                , new QtyUnitModel(((QtyUnitModel) spin_qty_unit.getSelectedItem()).getId()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getName()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getDescription()));
        presenter.restockInventory(inventoryModel);
    }

    private void sendNewStockBarangForBarangJual() {
        if(swt_isForSell.isChecked()){
            isForSellValue = 1;
        } else {
            isForSellValue = 0;
        }

//        if(et_weight.getText().toString().trim().isEmpty()){
//            tempWeigth = 0;
//        } else {
//            tempWeigth = Double.valueOf(et_weight.getText().toString());
//        }


        inventoryModel = new InventoryModel(new InventoryCategoryModel(((InventoryCategoryModel) spin_category.getSelectedItem()).getDescription(),
                ((InventoryCategoryModel) spin_category.getSelectedItem()).getId(), ((InventoryCategoryModel) spin_category.getSelectedItem()).getName())
                , et_name.getText().toString()
                , Long.valueOf(PrefHelper.getString(Const.ID))
                , et_description.getText().toString()
                , spinCondition.getSelectedItemPosition() == 0 ? "Baru" : "Bekas"
                , isForSellValue
                , et_name.getText().toString()
                , et_description.getText().toString()
                , Double.valueOf(etSellPrice.getText().toString())
                , Double.valueOf(et_quantity.getText().toString())
                , tempWeigth
                , new QtyUnitModel(((QtyUnitModel) spin_qty_unit.getSelectedItem()).getId()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getName()
                , ((QtyUnitModel) spin_qty_unit.getSelectedItem()).getDescription()));
        presenter.sendInventory(inventoryModel);
    }

    private boolean validateName() {
        if (et_name.getText().toString().trim().isEmpty()) {
            et_name.setError(getResources().getString(R.string.no_empty_label));
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePrice() {
        if (etSellPrice.getText().toString().trim().isEmpty()) {
            etSellPrice.setError(getResources().getString(R.string.no_empty_label));
            return false;
        } else {
            return true;
        }
    }

    private boolean validateQuantity() {
        if (et_quantity.getText().toString().trim().isEmpty()) {
            et_quantity.setError(getResources().getString(R.string.no_empty_label));
            return false;
        } else {
            return true;
        }
    }

    private boolean validateWeight() {
        if (et_weight.getText().toString().trim().isEmpty()) {
            et_weight.setError(getResources().getString(R.string.no_empty_label));
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBuyPrice(){
        if(etBuyPrice.getText().toString().trim().isEmpty()){
            etBuyPrice.setError(getResources().getString(R.string.no_empty_label));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void showDialogAfterAddInventory(boolean isSuccess, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismissDialog() {
        dismiss();
        dialogInterface.finishDialog();
    }

    @Override
    public void showDialogAfterRestockInventory(boolean isSuccess, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
