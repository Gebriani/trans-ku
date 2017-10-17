package id.co.sumi.transaku.presenter;

import android.util.Log;

import java.io.File;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.modelresp.SellerProfileResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gebriani on 20/04/17.
 */

public class SellerProfilePresenter implements Presenter<SellerProfileMvpView> {
    private SellerProfileMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(SellerProfileMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getSellerProfile(){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getSellerProfile("Bearer " + PrefHelper.getString(Const.TOKEN))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SellerProfileResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showActivity(false, null, e.getMessage());
                    }

                    @Override
                    public void onNext(SellerProfileResp sellerProfileResp) {
                        if(sellerProfileResp.isSuccess()){
                            view.showActivity(true, sellerProfileResp.getData(), sellerProfileResp.getMsg());
                        } else {
                            view.showActivity(false, null, sellerProfileResp.getMsg());
                        }
                    }
                });
    }

    public void sellerChangePassword(UserProfileModel userProfileModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().sellerChangePassword("Bearer " + PrefHelper.getString(Const.TOKEN), userProfileModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showChangePassword(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if(baseResp.isSuccess()){
                            view.showChangePassword(true, baseResp.getMsg());
                        } else {
                            view.showChangePassword(false, baseResp.getMsg());
                        }
                    }
                });
    }

    public void editProfileSeller(UserProfileModel userProfileModel) {
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().editSellerProfile("Bearer " + PrefHelper.getString(Const.TOKEN), userProfileModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.refreshActivity(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if (baseResp.isSuccess()) {
                            view.refreshActivity(true, baseResp.getMsg());
                        } else {
                            view.refreshActivity(false, baseResp.getMsg());
                        }
                    }
                });
    }
}