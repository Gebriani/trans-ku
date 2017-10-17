package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.LoginBean;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.presenter.LoginMvpView;
import id.co.sumi.transaku.presenter.LoginPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;

/**
 * Created by gebriani on 27/02/17.
 */

public class LoginActivity extends ParentActivity implements View.OnClickListener, LoginMvpView {
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private ImageView btFacebook;
    private ImageView btTwitter;
    private ImageView btGooglePlus;
    private TextView tvForgotPassword;
    private TextView tx_regisration;
    private ImageView ivShowPassword;
    private boolean isShowPass = false;

    private LoginPresenter presenter;

    private void findViews() {
        etUsername = (EditText) findViewById(R.id.login_username);
        etPassword = (EditText) findViewById(R.id.login_password);
        btLogin = (Button) findViewById(R.id.login_button_login);
        btFacebook = (ImageView) findViewById(R.id.login_facebook_login);
        btTwitter = (ImageView) findViewById(R.id.login_twitter_login);
        btGooglePlus = (ImageView) findViewById(R.id.login_googleplus_login);
        tvForgotPassword = (TextView) findViewById(R.id.login_forgot_password);
        tx_regisration = (TextView) findViewById(R.id.login_registration);
        ivShowPassword = (ImageView) findViewById(R.id.login_show_password);

        ivShowPassword.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btFacebook.setOnClickListener(this);
        btTwitter.setOnClickListener(this);
        btGooglePlus.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tx_regisration.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        presenter = PresenterFactory.loginPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button_login:
                if (validateEmail() && validatePassword()) {
                    //TODO call login API
                    presenter.login(etUsername.getText().toString(), etPassword.getText().toString());
                }
                break;
            case R.id.login_facebook_login:
                break;
            case R.id.login_twitter_login:
                break;
            case R.id.login_googleplus_login:
                break;
            case R.id.login_forgot_password:
                showResetPasswordDialog();
                break;
            case R.id.login_registration:
                RegisterActivity.showActivity(LoginActivity.this);
                break;
            case R.id.login_show_password:
                if (isShowPass == false) {
                    isShowPass = true;
                    ivShowPassword.setImageResource(R.mipmap.ic_eye_open);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (isShowPass == true) {
                    isShowPass = false;
                    ivShowPassword.setImageResource(R.mipmap.ic_eye_off);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }

    private boolean validateEmail() {
        if (etUsername.getText().toString().trim().isEmpty()) {
            etUsername.setError(getResources().getString(R.string.empty_username_message));
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError(getResources().getString(R.string.empty_password_message));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showHome(boolean isSuccess, LoginBean loginBean) {
        if (isSuccess) {
            Bundle bundle = new Bundle();
            bundle.putString("User_ID", loginBean.getProfile().getId());
            bundle.putString("User_Email", loginBean.getProfile().getEmail());
            bundle.putString("User_Name", loginBean.getProfile().getName());
            TransakuApplication.getInstance().getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
            finish();
            HomeActivity.showActivity(LoginActivity.this);
        }
    }

    @Override
    public void showHome(boolean isSuccess, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        //TODO need testing
        if(message.equalsIgnoreCase("User belum melakukan verifikasi") && PrefHelper.getString(Const.ID) != null){
            Log.d("USERID", PrefHelper.getString(Const.ID));
            RegisterVerificationActivity.showActivity(this, Long.parseLong(PrefHelper.getString(Const.ID)));
        }
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    private void showResetPasswordDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_with_edittext, null);
        dialogBuilder.setView(view);
        TextView tv_title = (TextView) view.findViewById(R.id.dialog_withet_title);
        final EditText add_email = (EditText) view.findViewById(R.id.dialog_withet_edittext);
        tv_title.setText("Silahkan Masukkan Email Anda");

        dialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!add_email.getText().toString().trim().isEmpty()) {
                    UserProfileModel userProfileModel = new UserProfileModel(add_email.getText().toString());
                    presenter.resetPasswordByEmail(userProfileModel);
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Mohon isi jumlah barang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

}
