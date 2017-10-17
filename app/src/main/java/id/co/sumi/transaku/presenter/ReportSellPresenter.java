package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.modelresp.PenjualanBarangResp;
import id.co.sumi.transaku.modelresp.ReportListResp;
import id.co.sumi.transaku.modelresp.SellListResp;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReportSellPresenter implements Presenter<ReportSellMvpView> {
    private ReportSellMvpView view;
    private Subscription subscription;

    @Override
    public void attachView(ReportSellMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if(subscription != null) subscription.unsubscribe();
    }

    public void getReportSellByDate(String startDate, String endDate){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi()
                .getReportSellByDate("Bearer"+ PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID), startDate,endDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SellListResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showReportSell(false,null, null,e.getMessage());
                    }

                    @Override
                    public void onNext(SellListResp sellResp) {
                        if(sellResp.isSuccess()){
                            view.showReportSell(true,sellResp.getData(),sellResp.getAdditionalInfo(), sellResp.getMsg());
                        } else{
                            view.showReportSell(false, null, null, sellResp.getMsg());
                        }
                    }
                });
    }

    public void getReportLabaRugi(String startDate, String endDate){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi()
                .getReportLabaRugi("Bearer"+ PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID), startDate,endDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReportListResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showReportLabaRugi(false,null, null,e.getMessage());
                    }

                    @Override
                    public void onNext(ReportListResp reportListResp) {
                        if(reportListResp.isSuccess()){
                            view.showReportLabaRugi(true,reportListResp.getData(),reportListResp.getAdditionalInfo(),reportListResp.getMsg());
                        } else{
                            view.showReportLabaRugi(false, null, null, reportListResp.getMsg());
                        }
                    }
                });
    }

    public void getReportPenjualanBarang(String startDate, String endDate){
        subscription = TransakuApplication.getInstance().getRestAPI().getApi()
                .getReportPenjualanBarang("Bearer"+ PrefHelper.getString(Const.TOKEN), PrefHelper.getString(Const.ID), startDate,endDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PenjualanBarangResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showReportPenjualanBarang(false,null,e.getMessage());
                    }

                    @Override
                    public void onNext(PenjualanBarangResp penjualanBarangResp) {
                        if(penjualanBarangResp.isSuccess()){
                            view.showReportPenjualanBarang(true,penjualanBarangResp.getData(), penjualanBarangResp.getMsg());
                        } else {
                            view.showReportPenjualanBarang(false, null, penjualanBarangResp.getMsg());
                        }
                    }
                });
    }

}
