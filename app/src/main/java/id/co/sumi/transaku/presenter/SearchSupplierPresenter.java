package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.modelresp.SupplierResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Transaku
 * Author Gebriani
 * on 15, March 2017.
 */

public class SearchSupplierPresenter implements Presenter<SearchSupplierMvpView>{

    private SearchSupplierMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(SearchSupplierMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void searchSupplierByInventoryName(String name){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().searchSupplierByNameInventory("Bearer " + PrefHelper.getString(Const.TOKEN), name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SupplierResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, e.getMessage());
                    }

                    @Override
                    public void onNext(SupplierResp response) {
                        if(response.isSuccess()){
                            view.showSearchResult(true, response.getData());
                        } else {
                            view.showResponse(false, response.getMsg());
                        }
                    }
                });
    }

    public void searchSupplier_ByInventoryName_OrderByDistance(String name, String latitude, String longitude){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().searchSupplier_ByNameInventory_OrderByDistance("Bearer " + PrefHelper.getString(Const.TOKEN), name, latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SupplierResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, e.getMessage());
                    }

                    @Override
                    public void onNext(SupplierResp response) {
                        if(response.isSuccess()){
                            view.showSearchResult(true, response.getData());
                        } else {
                            view.showResponse(false, response.getMsg());
                        }
                    }
                });
    }

    public void searchSupplierBySupplierName(String name){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().searchSupplierByNameSupplier("Bearer " + PrefHelper.getString(Const.TOKEN), name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SupplierResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, e.getMessage());
                    }

                    @Override
                    public void onNext(SupplierResp supplierResp) {
                        if(supplierResp.isSuccess()){
                            view.showSearchResult(true, supplierResp.getData());
                        } else {
                            view.showResponse(false, supplierResp.getMsg());
                        }
                    }
                });
    }

    public void searchSupplier_BySupplierName_OrderByDistance(String name, String latitude, String longitude){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().searchSupplier_ByNameSupplier_OrderByDistance("Bearer " + PrefHelper.getString(Const.TOKEN), name, latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SupplierResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, e.getMessage());
                    }

                    @Override
                    public void onNext(SupplierResp response) {
                        if(response.isSuccess()){
                            view.showSearchResult(true, response.getData());
                        } else {
                            view.showResponse(false, response.getMsg());
                        }
                    }
                });
    }

    public void getAllActiveSupplier(){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getAllActiveSupplier("Bearer " + PrefHelper.getString(Const.TOKEN))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SupplierResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResponse(false, e.getMessage());
                    }

                    @Override
                    public void onNext(SupplierResp supplierResp) {
                        if(supplierResp.isSuccess()){
                            view.showResponse(true, supplierResp.getData());
                        } else {
                            view.showResponse(false, supplierResp.getMsg());
                        }
                    }
                });
    }
}
