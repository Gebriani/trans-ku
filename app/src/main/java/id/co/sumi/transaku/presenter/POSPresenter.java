package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.modelresp.InventoryListResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 06/03/17.
 */

public class POSPresenter implements Presenter<POSMvpView> {
    private POSMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(POSMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getInventoryForPOSBySellerID(){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getPOSFORSellByCustomerID("Bearer " + PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InventoryListResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showActivity(false, null, e.getMessage());
                    }

                    @Override
                    public void onNext(InventoryListResp inventoryResp) {
                        if(inventoryResp.isSuccess()){
                            view.showActivity(true, inventoryResp.getData(), "");
                        } else {
                            view.showActivity(false, null, inventoryResp.getMsg());
                        }
                    }
                });
    }

    public void searchInventoryForSell(String keyword){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().searchInventoryForSellByName("Bearer " + PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID), keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InventoryListResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.searchResult(false, null, e.getMessage());
                    }

                    @Override
                    public void onNext(InventoryListResp inventoryResp) {
                        if(inventoryResp.isSuccess()){
                            view.searchResult(true, inventoryResp.getData(), "");
                        } else {
                            view.searchResult(false, null, inventoryResp.getMsg());
                        }
                    }
                });
    }


}
