package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.OrderModel;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gebriani on 7/23/17.
 */

public class BookingPresenter implements Presenter<BookingMvpView> {

    private BookingMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(BookingMvpView view) {
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
                        view.responseBuy(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp response) {
                        if(response.isSuccess()){
                            view.responseBuy(true, response.getMsg());
                        } else {
                            view.responseBuy(false, response.getMsg());
                        }
                    }
                });

    }


    public void sendOrderPOS(final OrderModel orderModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().orderSupplier("Bearer " + PrefHelper.getString(Const.TOKEN), orderModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.responseOrder(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp response) {
                        if(response.isSuccess()){
                            view.responseOrder(true, response.getMsg());
                        } else {
                            view.responseOrder(false, response.getMsg());
                        }
                    }
                });

    }
}
