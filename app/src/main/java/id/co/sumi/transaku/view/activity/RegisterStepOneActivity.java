package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.UserProfileModel;

public class RegisterStepOneActivity extends ParentActivity implements View.OnClickListener {

    private Spinner spin_custType;
    private EditText et_fullname;
    private EditText et_email;
    private EditText et_password;
    private EditText et_retryPassword;
    private Spinner spin_gender;
    private Button bt_cancel;
    private Button bt_next;
    private Toolbar toolbar;
    private TextView toolbar_title;

    private UserProfileModel profileModel;

    private String gender_str;


    private void findViews() {
        spin_custType = (Spinner) findViewById(R.id.regiter_step_one_spin_cust_type);
        et_fullname = (EditText) findViewById(R.id.regiter_step_one_et_fullname);
        et_email = (EditText) findViewById(R.id.regiter_step_one_et_email);
        et_password = (EditText) findViewById(R.id.regiter_step_one_et_password);
        et_retryPassword = (EditText) findViewById(R.id.regiter_step_one_et_retry_password);
        spin_gender = (Spinner) findViewById(R.id.regiter_step_one_spin_gender);
        bt_cancel = (Button) findViewById(R.id.regiter_step_one_bt_batal);
        bt_next = (Button) findViewById(R.id.regiter_step_one_bt_next);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("REGISTER");

        bt_cancel.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);
        findViews();

        profileModel = new UserProfileModel();
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, RegisterStepOneActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        if (v == bt_cancel) {
            onBackPressed();
        } else if (v == bt_next) {
            if (checkInputUser()) {
//                if (spin_custType.getSelectedItem().toString().equalsIgnoreCase("Penjual")) {
//                    profileModel.setIsSeller(1);
//                } else {
//                    profileModel.setIsSeller(0);
//                }
                if (spin_gender.getSelectedItem().toString().equalsIgnoreCase("Pria")) {
                    gender_str = "L";
                } else {
                    gender_str = "P";

                }
                profileModel.setIsSeller(1);
                profileModel.setGender(gender_str);
                profileModel.setName(et_fullname.getText().toString());
                profileModel.setEmail(et_email.getText().toString());
                profileModel.setPassword(et_password.getText().toString());
                String profileUs = TransakuApplication.getInstance().getGson().toJson(profileModel);
                RegisterStepTwoActivity.showActivity(this, profileUs);
                finish();
            }
        }
    }

    private boolean checkInputUser() {
        if (spin_custType.getSelectedItemPosition() == 0) {
            return false;
        }
        if (et_fullname.getText().toString().trim().isEmpty()) {
            et_fullname.setError("Tidak Boleh Kosong");
            return false;
        }
        if (et_email.getText().toString().trim().isEmpty()) {
            et_email.setError("Tidak Boleh Kosong");
            return false;
        }
        if (et_password.getText().toString().trim().isEmpty()) {
            et_password.setError("Tidak Boleh Kosong");
            return false;
        }
        if (et_retryPassword.getText().toString().trim().isEmpty()) {
            et_retryPassword.setError("Tidak Boleh Kosong");
            return false;
        }
        if (!et_retryPassword.getText().toString().equalsIgnoreCase(et_password.getText().toString())) {
            et_retryPassword.setError("Password Tidak Sama");
            return false;
        }
        if (spin_gender.getSelectedItemPosition() == 0) {
            return false;
        }
        return true;
    }
}
