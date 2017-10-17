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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.ProvinceModel;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.presenter.EditSellerProfileMvpView;
import id.co.sumi.transaku.presenter.EditSellerProfilePresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SellerProfileMvpView;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.CropImage;
import id.co.sumi.transaku.utils.InternalContentProvider;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerCity;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerProvince;

/**
 * Created by gebriani on 02/05/17.
 */

public class EditSellerProfileActivity extends ParentActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener,EditSellerProfileMvpView{

    private static String SELLER_PROFILE = "seller_profile";
    private EditText etUsername;
    private EditText etEmail;
    private EditText etAddress;
    private Spinner spCity;
    private EditText etTelp;
    private Spinner spGender;
    private Spinner spProvinsi;
    private EditText etStoreName;
    private EditText etStoreAddress;
    private EditText etStoreTelp;
    private EditText etKTPNo;
    private Button btCancel;
    private Button btSubmit;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private LinearLayout rootInputKtpPic;
    private TextView tvUploadKtpic;
    private UserProfileModel userProfileModel;
    private List<CityModel> cityModelList = new ArrayList<>();
    private CustomSpinnerCity cityAdapter;
    private CustomSpinnerProvince provinceAdapter;
    private EditSellerProfilePresenter presenter;
    private File tempFile;
    private String ktpBase64;

    private void findViews() {
        etUsername = (EditText) findViewById(R.id.edit_seller_et_username);
        etEmail = (EditText) findViewById(R.id.edit_seller_et_email);
        etAddress = (EditText) findViewById(R.id.edit_seller_et_address);
        spCity = (Spinner) findViewById(R.id.edit_seller_sp_city);
        etTelp = (EditText) findViewById(R.id.edit_seller_et_telp);
        spGender = (Spinner) findViewById(R.id.edit_seller_sp_gender);
        etStoreName = (EditText) findViewById(R.id.edit_seller_et_store_name);
        etStoreAddress = (EditText) findViewById(R.id.edit_seller_et_store_address);
        etStoreTelp = (EditText) findViewById(R.id.edit_seller_et_store_telp);
        btCancel = (Button) findViewById(R.id.edit_seller_bt_cancel);
        btSubmit = (Button) findViewById(R.id.edit_seller_bt_submit);
        etKTPNo = (EditText) findViewById(R.id.edit_seller_et_ktp_number);
        spProvinsi = (Spinner) findViewById(R.id.edit_seller_sp_provincy);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        rootInputKtpPic = (LinearLayout) findViewById(R.id.edit_seller_root_ktp);
        tvUploadKtpic = (TextView) findViewById(R.id.edit_seller_upload_ktppic);

        btCancel.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        rootInputKtpPic.setOnClickListener(this);
        tvUploadKtpic.setOnClickListener(this);

        PopulateCombo populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);
        userProfileModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(SELLER_PROFILE), UserProfileModel.class);

        provinceAdapter = new CustomSpinnerProvince(this, populateCombo.getProvinces());
        spProvinsi.setAdapter(provinceAdapter);
        spProvinsi.setOnItemSelectedListener(this);

        cityAdapter = new CustomSpinnerCity(this, cityModelList);
        spCity.setAdapter(cityAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText("Ubah Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            tempFile = new File(Environment.getExternalStorageDirectory(), Const.TEMP_PHOTO_FILE_NAME);
        } else {
            tempFile = new File(getFilesDir(), Const.TEMP_PHOTO_FILE_NAME);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_profile);
        findViews();
        presenter = PresenterFactory.editSellerProfilePresenter();
        presenter.attachView(this);

        int gendersel = 0;
        if(userProfileModel.getGender().equalsIgnoreCase("L")){
            gendersel = 1;
        } else if(userProfileModel.getGender().equalsIgnoreCase("P")){
            gendersel = 2;
        }
        spGender.setSelection(gendersel);
        etAddress.setText(userProfileModel.getAddress());
        etEmail.setText(userProfileModel.getEmail());
        spProvinsi.setSelection(provinceAdapter.getPosition(userProfileModel.getProvince()));
        etStoreAddress.setText(userProfileModel.getStoreAddress());
        etStoreName.setText(userProfileModel.getBusinessName());
        etStoreTelp.setText(userProfileModel.getBusinessPhone());
        etUsername.setText(userProfileModel.getName());
        etKTPNo.setText(userProfileModel.getIdCardNumber());
        etTelp.setText(userProfileModel.getPhone());
        ktpBase64 = userProfileModel.getIdCardBase64();

        if(userProfileModel.getProvince() != null){
            presenter.getCitiesByUser(String.valueOf(userProfileModel.getProvince().getId()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_seller_bt_cancel:
                onBackPressed();
                break;
            case R.id.edit_seller_bt_submit:
                if(checkinput()){
                    if(spGender.getSelectedItemId() == 1){
                        userProfileModel.setGender("L");
                    } else if(spGender.getSelectedItemId() == 2){
                        userProfileModel.setGender("P");
                    }

                    userProfileModel.setName(etUsername.getText().toString());
                    userProfileModel.setAddress(etAddress.getText().toString());
                    userProfileModel.setEmail(etEmail.getText().toString());
                    userProfileModel.setProvince((ProvinceModel) spProvinsi.getSelectedItem());
                    userProfileModel.setCity((CityModel) spCity.getSelectedItem());
                    userProfileModel.setStoreAddress(etStoreAddress.getText().toString());
                    userProfileModel.setBusinessName(etStoreName.getText().toString());
                    userProfileModel.setBusinessPhone(etStoreTelp.getText().toString());
                    userProfileModel.setIdCardNumber(etKTPNo.getText().toString());
                    userProfileModel.setIdCardBase64(ktpBase64);
                    presenter.editSellerProfile(userProfileModel);
                }
                break;

            case R.id.edit_seller_root_ktp:
                checkPermission();
                break;
            case R.id.edit_seller_upload_ktppic:
                checkPermission();
                break;
        }
    }

    private boolean checkinput(){
        if(etUsername.getText().toString().trim().isEmpty()){
            etUsername.setError("Tidak Boleh Kosong");
            return false;
        } else if(etEmail.getText().toString().trim().isEmpty()){
            etEmail.setError("Tidak Boleh Kosong");
            return false;
        } else if(etAddress.getText().toString().trim().isEmpty()){
            etAddress.setError("Tidak Boleh Kosong");
            return false;
        } else if(etTelp.getText().toString().trim().isEmpty()){
            etTelp.setError("Tidak Boleh Kosong");
            return false;
        } else if(etStoreName.getText().toString().trim().isEmpty()){
            etStoreName.setError("Tidak Boleh Kosong");
            return false;
        } else if(etKTPNo.getText().toString().trim().isEmpty()){
            etKTPNo.setError("Tidak Boleh Kosong");
            return false;
        } else if(etStoreTelp.getText().toString().trim().isEmpty()){
            etStoreTelp.setError("Tidak Boleh Kosong");
            return false;
        } else if(spGender.getSelectedItemId() == 0){
            Toast.makeText(this, "Gender Harus Diisi", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public static void showActivity(Context context, String sellerProfile) {
        Intent intent = new Intent(context, EditSellerProfileActivity.class);
        intent.putExtra(SELLER_PROFILE, sellerProfile);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

    @Override
    public void getCities(boolean isSuccess, List<CityModel> cityModels) {
        if(isSuccess){
            this.cityModelList.clear();
            this.cityModelList.addAll(cityModels);
            cityAdapter.notifyDataSetChanged();
            spCity.setSelection(cityAdapter.getPosition(userProfileModel.getCity()));
        }
    }

    @Override
    public void showActivity(boolean isSuccess, String message) {
        if(isSuccess){
            SellerProfileActivity.showActivity(this);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.edit_seller_sp_provincy:
                String str_province_id = String.valueOf(((ProvinceModel) spProvinsi.getSelectedItem()).getId());
                presenter.getCitiesByUser(str_province_id);
                break;
            case R.id.regiter_step_two_spin_city:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void checkPermission(){
        if (Build.VERSION.SDK_INT > 22) {
            String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
            String cameraPermission = Manifest.permission.CAMERA;
            int hasWritePermission = ContextCompat.checkSelfPermission(EditSellerProfileActivity.this, writeExternalStorage);
            int hasReadPermission = ContextCompat.checkSelfPermission(EditSellerProfileActivity.this, readExternalStorage);
            int hasCameraPermission = ContextCompat.checkSelfPermission(EditSellerProfileActivity.this, cameraPermission);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditSellerProfileActivity.this);
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
        Intent intent = new Intent(EditSellerProfileActivity.this, CropImage.class);
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
                ktpBase64 = CommonFunction.ConvertToBase64(tempFile);
                tvUploadKtpic.setText(tempFile.getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
