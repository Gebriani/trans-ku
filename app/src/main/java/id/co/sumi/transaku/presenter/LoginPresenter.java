package id.co.sumi.transaku.presenter;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.LoginBean;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 27/02/17.
 */

public class LoginPresenter implements Presenter<LoginMvpView> {
    public static String TAG = "LoginPresenter";

    private LoginMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(LoginMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if(subscription != null)
            subscription.unsubscribe();
    }

    public void login(String email, String pass){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().login(Const.LOGIN_TOKEN, email, pass, "password")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errrorMessage = e.getMessage();
                        if(e instanceof HttpException){
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(body.bytes()));
                                errrorMessage = jsonObject.getString("error_description");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                        view.showHome(false, errrorMessage);
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if(loginBean.isSuccess()){
                            Log.e(TAG, "onNext: " + loginBean.getProfile().getId());
                            PrefHelper.setString(Const.TOKEN, loginBean.getAccess_token());
                            PrefHelper.setString(Const.ID, loginBean.getProfile().getId());
                            PrefHelper.setString(Const.NAME, loginBean.getProfile().getName());
                            PrefHelper.setString(Const.EMAIL, loginBean.getProfile().getEmail());
                            view.showHome(true, loginBean);
                        } else {
                            view.showHome(false, "");

                        }

                    }

                });
    }

    public void resetPasswordByEmail(UserProfileModel userProfileModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().resetPassword(userProfileModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showHome(false, e.getMessage().toString());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if(baseResp.isSuccess()){
                            view.showHome(true, baseResp.getMsg());
                        } else {
                            view.showHome(false, baseResp.getMsg());
                        }
                    }
                });
    }
}

