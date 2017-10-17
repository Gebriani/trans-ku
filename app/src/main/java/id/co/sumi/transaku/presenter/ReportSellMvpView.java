package id.co.sumi.transaku.presenter;

import java.util.List;
import java.util.Map;

import id.co.sumi.transaku.model.ReportModel;
import id.co.sumi.transaku.model.SellModel;

public interface ReportSellMvpView {
    void showReportSell(boolean isSuceess, List<SellModel> data, Map<String, String> totalPenjualan, String message);
    void showReportLabaRugi(boolean isSuceess, List<ReportModel> data, Map<String, String> additionalInfo, String message);
    void showReportPenjualanBarang(boolean isSuccess, Map<String, String> data, String message);
}
