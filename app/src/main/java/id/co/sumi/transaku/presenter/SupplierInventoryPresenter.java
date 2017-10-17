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
 * Created by gebriani on 16/03/17.
 */

public class SupplierInventoryPresenter implements Presenter<SupplierInventoryMvpView> {
    private SupplierInventoryMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(SupplierInventoryMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getAllInventory(long id){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getAllInventoryBySupplierID("Bearer " + PrefHelper.getString(Const.TOKEN), id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InventoryListResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, null, e.getMessage());

                    }

                    @Override
                    public void onNext(InventoryListResp inventoryListResp) {
                        if(inventoryListResp.isSuccess()){
                            view.showResponse(true, inventoryListResp.getData(), inventoryListResp.getMsg());
                        } else {
                            view.showResponse(false, null, inventoryListResp.getMsg());
                        }
                    }
                });
    }
}
