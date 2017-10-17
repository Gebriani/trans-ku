package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.modelresp.CitiesResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alodokter on 05/05/17.
 */

public class EditSellerProfilePresenter implements Presenter<EditSellerProfileMvpView> {

    private Subscription subscription;
    private EditSellerProfileMvpView view;

    @Override
    public void attachView(EditSellerProfileMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getCitiesByUser(String id){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().getCitiesByProvID(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CitiesResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getCities(false, null);
                    }

                    @Override
                    public void onNext(CitiesResp citiesResp) {
                        if(citiesResp.isSuccess()){
                            view.getCities(true, citiesResp.getData());
                        } else{
                            view.getCities(false, null);
                        }
                    }
                });
    }

    public void editSellerProfile(UserProfileModel userProfileModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().editSellerProfile("Bearer " + PrefHelper.getString(Const.TOKEN), userProfileModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showActivity(false, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if(baseResp.isSuccess()){
                            view.showActivity(true, baseResp.getMsg());
                        } else{
                            view.showActivity(false, baseResp.getMsg());
                        }
                    }
                });
    }
}
