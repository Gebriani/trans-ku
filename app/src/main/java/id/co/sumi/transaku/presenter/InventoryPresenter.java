package id.co.sumi.transaku.presenter;


import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.modelresp.InventoryListResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 02/03/17.
 */

public class InventoryPresenter implements Presenter<InventoryMvpView> {

    private InventoryMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(InventoryMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getInventoryByCustomerID(){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getInventoryByCustomerID("Bearer " + PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InventoryListResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showActivity(false,null, e.getMessage());
                    }

                    @Override
                    public void onNext(InventoryListResp inventoryResp) {
                        if(inventoryResp.isSuccess()){
                            view.showActivity(true, inventoryResp.getData(), inventoryResp.getMsg());
                        } else {
                            view.showActivity(false, null, inventoryResp.getMsg());
                        }
                    }
                });
    }

    public void searchInventoryByName(String keyword){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().searchInventoryByName("Bearer " + PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID),keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InventoryListResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.searchResult(false, null,e.getMessage());
                    }

                    @Override
                    public void onNext(InventoryListResp inventoryResp) {
                        if(inventoryResp.isSuccess()){
                            view.searchResult(true, inventoryResp.getData(), inventoryResp.getMsg());
                        } else {
                            view.searchResult(false, null,inventoryResp.getMsg());
                        }
                    }
                });
    }

    public void getInventoryForBarangJualBySellerID(){
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
                            view.showActivity(true, inventoryResp.getData(), inventoryResp.getMsg());
                        } else {
                            view.showActivity(false, null, inventoryResp.getMsg());
                        }
                    }
                });
    }

    public void editPriceInventory(InventoryModel inventoryModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().editPriceInventory("Bearer " + PrefHelper.getString(Const.TOKEN), inventoryModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.editPrice(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if(baseResp.isSuccess()){
                            view.editPrice(true, baseResp.getMsg());
                        } else {
                            view.editPrice(false, baseResp.getMsg());
                        }
                    }
                });
    }

    public void deleteInventory(InventoryModel inventoryModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().deleteInventory("Bearer " + PrefHelper.getString(Const.TOKEN), inventoryModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.deleteInventory(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if(baseResp.isSuccess()){
                            view.deleteInventory(true, baseResp.getMsg());
                        } else {
                            view.deleteInventory(false, baseResp.getMsg());
                        }
                    }
                });
    }
}
