package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.RegisterMvpView;
import id.co.sumi.transaku.presenter.RegisterPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.fragment.RegisterInterface;
import id.co.sumi.transaku.view.fragment.RegisterOneFragment;
import id.co.sumi.transaku.view.fragment.RegisterThreeFragment;
import id.co.sumi.transaku.view.fragment.RegisterTwoFragment;

/**
 * Created by alodokter on 12/05/17.
 */

public class RegisterActivity extends ParentActivity implements RegisterInterface, RegisterMvpView{

    private FrameLayout mainFrameLayour;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private UserProfileModel profileModel;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        presenter = PresenterFactory.registerPresenter();
        presenter.attachView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mainFrameLayour = (FrameLayout) findViewById(R.id.reg_framelayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText("REGISTER");

        profileModel = new UserProfileModel();

        if(savedInstanceState == null){
            RegisterOneFragment registerOneFragment = RegisterOneFragment.newInstance(TransakuApplication.getInstance().getGson().toJson(profileModel));
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.reg_framelayout, registerOneFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
        finish();
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public void closeFragment() {
        finish();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void nextFragment(int step, UserProfileModel userProfileModel) {
        this.profileModel = userProfileModel;
        if(step == 1){
            RegisterTwoFragment registerTwoFragment = RegisterTwoFragment.newInstance(TransakuApplication.getInstance().getGson().toJson(userProfileModel));
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.reg_framelayout, registerTwoFragment).commit();
        } else if(step == 2){
            RegisterThreeFragment registerThreeFragment = RegisterThreeFragment.newInstance(TransakuApplication.getInstance().getGson().toJson(userProfileModel));
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.reg_framelayout, registerThreeFragment).commit();
        }
    }

    @Override
    public void finishStepRegister(UserProfileModel userProfileModel) {
        presenter.sendUserRegister(userProfileModel);
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
        PrefHelper.setString(Const.ID, String.valueOf(userID));
        RegisterVerificationActivity.showActivity(this, userID);
        finish();
    }
}
