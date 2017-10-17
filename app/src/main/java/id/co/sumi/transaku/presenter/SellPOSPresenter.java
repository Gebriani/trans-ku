package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SellPOSPresenter implements Presenter<SellPOSMvpView> {

    private SellPOSMvpView view;
    private Subscription subscription;


    @Override
    public void attachView(SellPOSMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void sendSellPOS(final SellModel sellModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().sendSellPOS("Bearer " + PrefHelper.getString(Const.TOKEN), sellModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp response) {
                        if(response.isSuccess()){
                            view.showResponse(true, response.getMsg());
                        } else {
                            view.showResponse(false, response.getMsg());
                        }
                    }
                });

    }
}
