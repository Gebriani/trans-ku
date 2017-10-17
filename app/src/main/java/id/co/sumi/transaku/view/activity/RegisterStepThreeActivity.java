package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.BusinessTypeModel;
import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.presenter.RegisterMvpView;
import id.co.sumi.transaku.presenter.RegisterPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerBusinessType;


public class RegisterStepThreeActivity extends ParentActivity implements View.OnClickListener, RegisterMvpView {

    private static String PROFILE_USER_STRING = "profile_user_string";

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Spinner spin_business_name;
    private EditText et_store_name;
    private Button bt_register;
    private TextView tvUploadProfile;
    private TextView tvUploadKTP;
    private RegisterPresenter presenter;
    private UserProfileModel userProfileModel;
    private CustomSpinnerBusinessType bt_adapter;
    private String profile_path, ktp_path = "";
    private File tempFileProfile, tempFileKTP;
    private int flagPhoto;

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("REGISTER");

        spin_business_name = (Spinner) findViewById(R.id.regiter_step_three_spin_business_name);
        et_store_name = (EditText) findViewById(R.id.regiter_step_three_et_store_name);
        bt_register = (Button) findViewById(R.id.regiter_step_three_bt_next);
        tvUploadProfile = (TextView) findViewById(R.id.regiter_step_three_upload_profile);
        tvUploadKTP = (TextView) findViewById(R.id.regiter_step_three_upload_ktp);

        bt_register.setOnClickListener(this);
        tvUploadProfile.setOnClickListener(this);
        tvUploadKTP.setOnClickListener(this);

        PopulateCombo populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);

        bt_adapter = new CustomSpinnerBusinessType(this, populateCombo.getBusinessTypes());
        spin_business_name.setAdapter(bt_adapter);

        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            tempFileProfile =  new File(Environment.getExternalStorageDirectory(), Const.TEMP_PHOTO_FILE_NAME);
            tempFileKTP = new File(Environment.getExternalStorageDirectory(), Const.TEMP_KTP_FILE_NAME);
        } else {
            tempFileProfile = new File(getFilesDir(), Const.TEMP_PHOTO_FILE_NAME);
            tempFileKTP = new File(getFilesDir(), Const.TEMP_KTP_FILE_NAME);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_three);
        findView();
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        userProfileModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(PROFILE_USER_STRING), UserProfileModel.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regiter_step_three_bt_next:
                if (checkInputUser()) {
                    userProfileModel.setBusinessName(et_store_name.getText().toString());
                    userProfileModel.setBusinessType((BusinessTypeModel) spin_business_name.getSelectedItem());
                    userProfileModel.setPictureBase64(profile_path);
                    userProfileModel.setIdCardBase64(ktp_path);
                    presenter.sendUserRegister(userProfileModel);
                }
                break;
            case R.id.regiter_step_three_upload_profile:
                break;
            case R.id.regiter_step_three_upload_ktp:
                break;
        }
    }

    private boolean checkInputUser() {
        if(et_store_name.getText().toString().trim().isEmpty()){
            et_store_name.setError("Tidak Boleh Kosong");
            return false;
        }

        if(profile_path == null && profile_path.equalsIgnoreCase("")){
            return false;
        }

        if(ktp_path == null && ktp_path.equalsIgnoreCase("")){
            return false;
        }
        return true;
    }

    public static void showActivity(Context context, String profileUser) {
        Intent intent = new Intent(context, RegisterStepThreeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PROFILE_USER_STRING, profileUser);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void getCities(boolean isSuccess, List<CityModel> cityModelList) {

    }


    @Override
    public void showResult(boolean isSuccess, String message) {
    }

    @Override
    public void showVerification(boolean isSuccess, String message) {

    }

    @Override
    public void showResult(boolean isSuccess, Long userID) {
        RegisterVerificationActivity.showActivity(this, userID);
        finish();
    }




}
