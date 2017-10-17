package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 03/03/17.
 */

public class AddInventoryPresenter implements Presenter<AddInventoryMvpView> {
    private AddInventoryMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(AddInventoryMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

//    public void getCategory() {
//        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getInventoryCategoryList()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<InventoryCategoryResp>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.showDialogWithCatData(false,e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(InventoryCategoryResp resp) {
//                        if (resp.isSuccess()) {
//                            view.showDialogWithCatData(true,resp.getData());
//                        } else {
//                            view.showDialogWithCatData(false,resp.getMsg());
//                        }
//                    }
//                });
//    }

    public void sendInventory(InventoryModel inventoryModel) {
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().addNewInventory("Bearer " + PrefHelper.getString(Const.TOKEN),inventoryModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showDialogAfterAddInventory(false,e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp resp) {
                        if (resp.isSuccess()) {
                            view.dismissDialog();
                        } else {
                            view.showDialogAfterAddInventory(false,resp.getMsg());
                        }
                    }
                });
    }

    public void restockInventory(InventoryModel inventoryBean) {
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().restockInventory("Bearer " + PrefHelper.getString(Const.TOKEN),inventoryBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        view.showDialogAfterRestockInventory(false,e.getMessage());
                    }
                    @Override
                    public void onNext(BaseResp resp) {
                        if (resp.isSuccess()) {
//                            view.showDialogAfterRestockInventory(true, resp.getMsg());
                            view.dismissDialog();
                        } else {
                            view.showDialogAfterRestockInventory(false,resp.getMsg());
                        }
                    }
                });
    }




}
