package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.ProvinceModel;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.presenter.RegisterMvpView;
import id.co.sumi.transaku.presenter.RegisterPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerCity;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerProvince;


public class RegisterStepTwoActivity extends ParentActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener,RegisterMvpView {

    private static String PROFILE_USER_STRING = "profile_user_string";
    private Toolbar toolbar;
    private TextView toolbar_title;
    private EditText et_no_ktp;
    private EditText et_address;
    private Spinner spin_province;
    private Spinner spin_city;
    private EditText et_portal_code;
    private EditText et_phone;
    private Button bt_batal;
    private Button bt_lanjut;
    private UserProfileModel userProfileModel;
    private RegisterPresenter presenter;
    private List<CityModel> cityModelList = new ArrayList<>();
    private CustomSpinnerProvince provinceAdapter;
    private CustomSpinnerCity cityAdapter;

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("REGISTER");

        et_no_ktp = (EditText) findViewById(R.id.regiter_step_two_et_no_ktp);
        et_address = (EditText) findViewById(R.id.regiter_step_two_et_address);
        spin_province = (Spinner) findViewById(R.id.regiter_step_two_spin_provinsi);
        spin_city = (Spinner) findViewById(R.id.regiter_step_two_spin_city);
        et_portal_code = (EditText) findViewById(R.id.regiter_step_two_et_portal_code);
        et_phone = (EditText) findViewById(R.id.regiter_step_two_et_phone);
        bt_batal = (Button) findViewById(R.id.regiter_step_two_bt_batal);
        bt_lanjut = (Button) findViewById(R.id.regiter_step_two_bt_next);

        bt_batal.setOnClickListener(this);
        bt_lanjut.setOnClickListener(this);

        PopulateCombo populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);

        provinceAdapter = new CustomSpinnerProvince(this, populateCombo.getProvinces());
        spin_province.setAdapter(provinceAdapter);

        cityModelList.add(0, new CityModel(-1, "- Pilih Kota -"));
        cityAdapter = new CustomSpinnerCity(this, cityModelList);
        spin_city.setAdapter(cityAdapter);

        spin_province.setOnItemSelectedListener(this);
        spin_city.setOnItemSelectedListener(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_two);
        findView();
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        userProfileModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(PROFILE_USER_STRING), UserProfileModel.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regiter_step_two_bt_batal:
                finish();
                break;
            case R.id.regiter_step_two_bt_next:
                if (checkInputUser()) {
                    userProfileModel.setIdCardNumber(et_no_ktp.getText().toString());
                    userProfileModel.setAddress(et_address.getText().toString());
                    userProfileModel.setProvince((ProvinceModel) spin_province.getSelectedItem());
                    userProfileModel.setCity((CityModel) spin_city.getSelectedItem());
                    userProfileModel.setPostalCode(Integer.parseInt(et_portal_code.getText().toString()));
                    userProfileModel.setPhone(et_phone.getText().toString());
                    String profileUs = TransakuApplication.getInstance().getGson().toJson(userProfileModel);
                    RegisterStepThreeActivity.showActivity(this, profileUs);
                    finish();
                }
                break;
        }
    }

    private boolean checkInputUser() {
        if (et_no_ktp.getText().toString().trim().toString().isEmpty()) {
            et_no_ktp.setError("Tidak Boleh Kosong");
            return false;
        }
        if (et_address.getText().toString().trim().toString().isEmpty()) {
            et_address.setError("Tidak Boleh Kosong");
            return false;
        }
        if (et_portal_code.getText().toString().trim().toString().isEmpty()) {
            et_portal_code.setError("Tidak Boleh Kosong");
            return false;
        }
        if (et_phone.getText().toString().trim().toString().isEmpty()) {
            et_phone.setError("Tidak Boleh Kosong");
            return false;
        }
        return true;
    }

    public static void showActivity(Context context, String profileUser) {
        Intent intent = new Intent(context, RegisterStepTwoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PROFILE_USER_STRING, profileUser);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void getCities(boolean isSuccess, List<CityModel> cityModelList) {
        cityAdapter = new CustomSpinnerCity(this, cityModelList);
        spin_city.setAdapter(cityAdapter);
    }

    @Override
    public void showResult(boolean isSuccess, String message) {

    }

    @Override
    public void showVerification(boolean isSuccess, String message) {

    }

    @Override
    public void showResult(boolean isSuccess, Long userID) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.regiter_step_two_spin_provinsi:
                String str_province_id = String.valueOf(((ProvinceModel) spin_province.getSelectedItem()).getId());
                presenter.getCitiesByProvincesID(str_province_id);
                break;
            case R.id.regiter_step_two_spin_city:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
