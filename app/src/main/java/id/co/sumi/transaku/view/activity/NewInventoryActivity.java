package id.co.sumi.transaku.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryCategoryModel;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.QtyUnitModel;
import id.co.sumi.transaku.presenter.AddInventoryMvpView;
import id.co.sumi.transaku.presenter.AddInventoryPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.CropImage;
import id.co.sumi.transaku.utils.InternalContentProvider;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerInventoryCategories;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerQtyUnits;

/**
 * Created by gebriani on 18/05/17.
 */

public class NewInventoryActivity extends ParentActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AddInventoryMvpView {

    private EditText etName;
    private Spinner spinCategory;
    private EditText etDescription;
    private Switch switcForSell;
    private EditText etBuyPrice;
    private EditText etSellPrice;
    private Spinner spinQuantityUnit;
    private EditText etQuantity;
    private EditText etWeight;
    private Spinner spinCondition;
    private Button btCancel;
    private Button btSubmit;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView tvUploadPic;
    private File tempFile;
    private String tempPICPATH = "";
    private double tempWeigth, tempSellPrice, tempBuyPrice = 0;

    private CustomSpinnerInventoryCategories customSpinnerInventoryCategories;
    private CustomSpinnerQtyUnits customSpinnerQtyUnits;
    private int isForSellValue = 0;
    private InventoryModel inventoryModel;
    private AddInventoryPresenter presenter;


    public NewInventoryActivity() {
    }


    private void findViews() {
        etName = (EditText) findViewById(R.id.new_inv_et_name);
        spinCategory = (Spinner) findViewById(R.id.new_inv_spin_category);
        etDescription = (EditText) findViewById(R.id.new_inv_et_description);
        switcForSell = (Switch) findViewById(R.id.new_inv_isForSell);
        etBuyPrice = (EditText) findViewById(R.id.new_inv_et_buy_price);
        etSellPrice = (EditText) findViewById(R.id.new_inv_et_sell_price);
        spinQuantityUnit = (Spinner) findViewById(R.id.new_inv_sp_qty_unit);
        etQuantity = (EditText) findViewById(R.id.new_inv_et_jumlah);
        etWeight = (EditText) findViewById(R.id.new_inv_et_berat);
        spinCondition = (Spinner) findViewById(R.id.new_inv_sp_kondisi);
        btCancel = (Button) findViewById(R.id.new_inv_bt_cancel);
        btSubmit = (Button) findViewById(R.id.new_inv_bt_simpan);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvUploadPic = (TextView) findViewById(R.id.new_inv_tv_uploadPic);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("Barang Baru");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btCancel.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        switcForSell.setOnCheckedChangeListener(this);
        tvUploadPic.setOnClickListener(this);

        PopulateCombo populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);

        customSpinnerQtyUnits = new CustomSpinnerQtyUnits(this, populateCombo.getQtyUnits());
        spinQuantityUnit.setAdapter(customSpinnerQtyUnits);
        customSpinnerInventoryCategories = new CustomSpinnerInventoryCategories(this, populateCombo.getInventoryCategories());
        spinCategory.setAdapter(customSpinnerInventoryCategories);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inventory);
        findViews();
        presenter = PresenterFactory.addInventoryPresenter();
        presenter.attachView(this);

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            tempFile = new File(Environment.getExternalStorageDirectory(), Const.TEMP_PHOTO_FILE_NAME);
        } else {
            tempFile = new File(getFilesDir(), Const.TEMP_PHOTO_FILE_NAME);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_inv_bt_cancel:
                onBackPressed();
                break;
            case R.id.new_inv_tv_uploadPic:
                checkPermission();
                break;
            case R.id.new_inv_bt_simpan:
                if(validateInput()){
                    if(isForSellValue == 1){
                        tempSellPrice = Double.valueOf(etSellPrice.getText().toString());
                    }
                    inventoryModel = new InventoryModel(new InventoryCategoryModel(((InventoryCategoryModel) spinCategory.getSelectedItem()).getDescription(),
                            ((InventoryCategoryModel) spinCategory.getSelectedItem()).getId(), ((InventoryCategoryModel) spinCategory.getSelectedItem()).getName())
                            , etName.getText().toString()
                            , Long.valueOf(PrefHelper.getString(Const.ID))
                            , etDescription.getText().toString()
                            , spinCondition.getSelectedItemPosition() == 0 ? "Baru" : "Bekas"
                            , isForSellValue
                            , etName.getText().toString()
                            , etDescription.getText().toString()
                            , tempSellPrice
                            , Double.valueOf(etQuantity.getText().toString())
                            , tempWeigth
                            , new QtyUnitModel(((QtyUnitModel) spinQuantityUnit.getSelectedItem()).getId()
                            , ((QtyUnitModel) spinQuantityUnit.getSelectedItem()).getName()
                            , ((QtyUnitModel) spinQuantityUnit.getSelectedItem()).getDescription())
                            , Double.valueOf(etBuyPrice.getText().toString()));
                    inventoryModel.setPictureBase64(tempPICPATH);
                    presenter.restockInventory(inventoryModel);
                }
                break;
        }
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, NewInventoryActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

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
    
    private boolean validateInput(){
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError(getResources().getString(R.string.no_empty_label));
            return false;
        }
        if (isForSellValue == 1 && etSellPrice.getText().toString().trim().isEmpty()) {
            etSellPrice.setError(getResources().getString(R.string.no_empty_label));
            return false;
        }

        if (etQuantity.getText().toString().trim().isEmpty()) {
            etQuantity.setError(getResources().getString(R.string.no_empty_label));
            return false;
        }

        if(etBuyPrice.getText().toString().trim().isEmpty()){
            etBuyPrice.setError(getResources().getString(R.string.no_empty_label));
            return false;
        }

        return true;
        
    }


    //TAKE PHOTO
    private void checkPermission(){
        if (Build.VERSION.SDK_INT > 22) {
            String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
            String cameraPermission = Manifest.permission.CAMERA;
            int hasWritePermission = ContextCompat.checkSelfPermission(NewInventoryActivity.this, writeExternalStorage);
            int hasReadPermission = ContextCompat.checkSelfPermission(NewInventoryActivity.this, readExternalStorage);
            int hasCameraPermission = ContextCompat.checkSelfPermission(NewInventoryActivity.this, cameraPermission);
            List<String> permissions = new ArrayList<String>();
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(writeExternalStorage);
                permissions.add(readExternalStorage);
                permissions.add(cameraPermission);
            }
            if (!permissions.isEmpty()) {
                String[] params = permissions.toArray(new String[permissions.size()]);
                requestPermissions(params, Const.PERMISSION_REQUEST_CODE_FOR_EXTERNAL_STORAGE);
            } else {
                takePicture();
            }
        } else {
            takePicture();
        }
    }


    private void takePicture() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewInventoryActivity.this);
        dialogBuilder
                .setTitle(R.string.image_prompt)
                .setItems(R.array.image_arrays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                AvatarFromGallery();
                                dialog.dismiss();
                                break;
                            case 1:
                                AvatarFromCamera();
                                dialog.dismiss();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    private void AvatarFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri cameraFileName = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                cameraFileName = Uri.fromFile(tempFile);
            } else {
                cameraFileName = InternalContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraFileName);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, Const.CHOICE_AVATAR_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    //take image from gallery
    private void AvatarFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Const.CHOICE_AVATAR_FROM_GALLERY);
    }

    private void startCropImage() {
        Intent intent = new Intent(NewInventoryActivity.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, tempFile.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 3);
        intent.putExtra(CropImage.OUTPUT_X, 300);
        intent.putExtra(CropImage.OUTPUT_Y, 300);

        startActivityForResult(intent, Const.CHOICE_AVATAR_FROM_CAMERA_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Const.PERMISSION_REQUEST_CODE_FOR_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.CHOICE_AVATAR_FROM_GALLERY) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                    CommonFunction.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    if (null != inputStream) {
                        inputStream.close();
                    }
                    startCropImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Const.CHOICE_AVATAR_FROM_CAMERA) {
                startCropImage();
            } else if (requestCode == Const.CHOICE_AVATAR_FROM_CAMERA_CROP) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }

                tempPICPATH = CommonFunction.ConvertToBase64(tempFile);
                tvUploadPic.setText(tempFile.getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void showDialogAfterAddInventory(boolean isSuccess, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void dismissDialog() {
        Toast.makeText(this, "Inventory berhasil di input", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showDialogAfterRestockInventory(boolean isSuccess, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
