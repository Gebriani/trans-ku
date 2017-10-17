package id.co.sumi.transaku.presenter;


import android.util.Log;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.modelresp.PopulateComboResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralPresenter implements Presenter<GeneralMvpView> {

    private GeneralMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(GeneralMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if(subscription != null)
            subscription.unsubscribe();
    }

    public void checkToken(){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().checkToken(Const.LOGIN_TOKEN, PrefHelper.getString(Const.TOKEN))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToken(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        Log.d("CHECK_TOKEN", baseResp.toString());
                        if(baseResp.isSuccess()){
                            view.showToken(true, baseResp.getMsg());
                        } else{
                            view.showToken(false, baseResp.getMsg());
                        }

                    }
                });
    }

    public void getPopulateComboData(){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getAllPopulateCombo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PopulateComboResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showPopulateCombo(false, null, e.getMessage());
                    }

                    @Override
                    public void onNext(PopulateComboResp populateComboResp) {
                        if(populateComboResp.isSuccess()){
                            view.showPopulateCombo(true, populateComboResp.getData(), populateComboResp.getMsg());
                        } else{
                            view.showPopulateCombo(false, null, populateComboResp.getMsg());
                        }

                    }
                });
    }

}
