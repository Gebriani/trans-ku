package id.co.sumi.transaku.view.customdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.presenter.InventoryMvpView;
import id.co.sumi.transaku.presenter.InventoryPresenter;

/**
 * Transaku
 * Author Gebriani
 * on 19, April 2017.
 */

public class EditPriceDialog extends DialogFragment implements View.OnClickListener, InventoryMvpView {

    public interface DialogPriceInterface{
        void dialogPriceFinish();
    }

    private static String INV_STRING = "inventory";
    private EditText et_harga_beli;
    private EditText et_harga_jual;
    private Button cancel_button;
    private Button submit_button;

    private InventoryModel tempInventory;
    private InventoryPresenter presenter;

    private DialogPriceInterface dialogInterface;

    public static EditPriceDialog newInstance(String inv, DialogPriceInterface dialogPriceInterface){
        EditPriceDialog dialog = new EditPriceDialog();
        Bundle bundle = new Bundle();
        bundle.putString(INV_STRING, inv);
        dialog.setArguments(bundle);
        dialog.dialogInterface = dialogPriceInterface;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Sumi_DialogBox);
        tempInventory = TransakuApplication.getInstance().getGson().fromJson(getArguments().getString(INV_STRING), InventoryModel.class);
        presenter = new InventoryPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_input_price, container, false);
        et_harga_beli = (EditText) view.findViewById(R.id.dialog_price_harga_beli);
        et_harga_jual = (EditText) view.findViewById(R.id.dialog_price_harga_jual);
        cancel_button = (Button) view.findViewById(R.id.dialog_price_cancel_button);
        submit_button = (Button) view.findViewById(R.id.dialog_price_submit_button);

        et_harga_jual.setText(String.valueOf(tempInventory.getPrice()));
        et_harga_beli.setText(String.valueOf(tempInventory.getBuyPrice()));

        cancel_button.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_price_submit_button:
                if(validateSellPrice() && validateBuyPrice()){
                    tempInventory.setPrice(Double.parseDouble(et_harga_jual.getText().toString()));
                    tempInventory.setBuyPrice(Double.parseDouble(et_harga_beli.getText().toString()));
                    presenter.editPriceInventory(tempInventory);
                }
                break;
            case R.id.dialog_price_cancel_button:
                dismiss();
                break;
        }
    }

    private boolean validateBuyPrice(){
        if(et_harga_beli.getText().toString().trim().isEmpty()){
            et_harga_beli.setError("Form ini tidak boleh kosong");
            return false;
        } else{
            return true;
        }
    }

    private boolean validateSellPrice(){
        if(et_harga_jual.getText().toString().trim().isEmpty()){
            et_harga_jual.setError("Form ini tidak boleh kosong");
            return false;
        } else{
            return true;
        }
    }

    @Override
    public void showActivity(boolean isSuccess, List<InventoryModel> inventory, String message) {

    }

    @Override
    public void searchResult(boolean isSuccess, List<InventoryModel> inventory, String message) {

    }

    @Override
    public void editPrice(boolean isSuccess, String message) {
        if(isSuccess){
            dialogInterface.dialogPriceFinish();
        }
        dismiss();
    }

    @Override
    public void deleteInventory(boolean isSuccess, String message) {

    }

}
