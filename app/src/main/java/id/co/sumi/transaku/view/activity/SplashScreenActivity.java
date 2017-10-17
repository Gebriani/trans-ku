package id.co.sumi.transaku.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.presenter.GeneralMvpView;
import id.co.sumi.transaku.presenter.GeneralPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.DateTimeStrategy;
import id.co.sumi.transaku.utils.PrefHelper;

/**
 * Created  on 10/03/17.
 */

public class SplashScreenActivity extends ParentActivity implements GeneralMvpView {

    private static final long SPLASH_TIMEOUT = 2000;
    private Button goButton;
    private boolean gone;
    private GeneralPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splashscreen);

        DateTimeStrategy.setLocale("th", "TH");
//        setLanguage(LanguageController.getInstance().getLanguage());
        goButton = (Button) findViewById(R.id.goButton);

        presenter = PresenterFactory.generalPresenter();
        presenter.attachView(this);
        presenter.getPopulateComboData();
    }


    @Override
    public void showToken(boolean isSuccess, String message) {
        if(isSuccess){
            finish();
            HomeActivity.showActivity(this);
        } else {
            finish();
            LoginActivity.showActivity(SplashScreenActivity.this);
        }
    }

    @Override
    public void showPopulateCombo(boolean isSuccess, PopulateCombo populateCombo, String message) {
        if(isSuccess){
            PrefHelper.setString(Const.POPULATE_COMBO_DATA, TransakuApplication.getInstance().getGson().toJson(populateCombo));
            if (PrefHelper.getString(Const.TOKEN) != null) {
                presenter.checkToken();
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
