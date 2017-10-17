package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.RegisterMvpView;
import id.co.sumi.transaku.presenter.RegisterPresenter;

/**
 * Created by gebriani on 14/03/17.
 */

public class RegisterVerificationActivity extends ParentActivity implements View.OnClickListener, RegisterMvpView {
    private static String USER_ID = "userid";
    private EditText et_verification_code;
    private Button bt_submit;
    private Long user_id;
    private ImageView iv_show_password;
    private boolean isShowPass = false;

    private RegisterPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_verification);
        et_verification_code = (EditText) findViewById(R.id.regis_verification_et_inputOTP);
        bt_submit = (Button) findViewById(R.id.regis_verification_bt_submit);
        iv_show_password = (ImageView) findViewById(R.id.regis_verification_show_password);

        iv_show_password.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        user_id = getIntent().getLongExtra(USER_ID, 0);
        presenter = PresenterFactory.registerPresenter();
        presenter.attachView(this);
    }

    public static void showActivity(Context context, Long userid) {
        Intent intent = new Intent(context, RegisterVerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(USER_ID, userid);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regis_verification_bt_submit:
                if(!et_verification_code.getText().toString().isEmpty()){
                    UserProfileModel profileModel = new UserProfileModel(user_id, et_verification_code.getText().toString());
                    presenter.sendRegisterVerification(profileModel);
                } else {
                    Toast.makeText(this, "Mohon untuk mengisi kode OTP anda", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.regis_verification_show_password:
                if(isShowPass == false){
                    isShowPass = true;
                    iv_show_password.setImageResource(R.mipmap.ic_eye_off);
                    et_verification_code.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (isShowPass == true){
                    isShowPass = false;
                    iv_show_password.setImageResource(R.mipmap.ic_eye_open);
                    et_verification_code.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;

        }
    }

    @Override
    public void getCities(boolean isSuccess, List<CityModel> cityModelList) {

    }

    @Override
    public void showResult(boolean isSuccess, String message) {

    }

    @Override
    public void showVerification(boolean isSuccess, String message) {
        if(isSuccess){
            Toast.makeText(this, "Verifikasi berhasil, silahkan login untuk masuk ke akun anda...", Toast.LENGTH_LONG).show();
            LoginActivity.showActivity(this);
            finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showResult(boolean isSuccess, Long userID) {

    }
}
