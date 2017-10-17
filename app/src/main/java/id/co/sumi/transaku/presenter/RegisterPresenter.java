package id.co.sumi.transaku.presenter;


import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.modelresp.CitiesResp;
import id.co.sumi.transaku.modelresp.RegisterResp;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterPresenter implements Presenter<RegisterMvpView> {
    private RegisterMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(RegisterMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getCitiesByProvincesID(String id){
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

    public void sendUserRegister(UserProfileModel profileModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().userRegister(profileModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RegisterResp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showResult(false, e.getMessage());
                    }

                    @Override
                    public void onNext(RegisterResp baseResp) {
                        if(baseResp.isSuccess()){
                            view.showResult(true, baseResp.getData());
                        } else{
                            view.showResult(false, baseResp.getMsg());
                        }
                    }
                });
    }

    public void sendRegisterVerification(UserProfileModel userProfileModel){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi().registrasiVerification(userProfileModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showVerification(false,e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp resp) {
                        if(resp.isSuccess()){
                            view.showVerification(true,resp.getMsg());
                        } else {
                            view.showVerification(false,resp.getMsg());

                        }
                    }
                });
    }

}
