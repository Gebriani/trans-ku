package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.ReportModel;

/**
 * Transaku
 * Author Gebriani
 * on 02, April 2017.
 */

public class ReportListResp extends BaseResp{
    private List<ReportModel> data;

    public ReportListResp(List<ReportModel> data) {
        this.data = data;
    }

    public List<ReportModel> getData() {
        return data;
    }

    public void setData(List<ReportModel> data) {
        this.data = data;
    }
}
