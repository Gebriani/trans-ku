package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.ProvinceModel;

public class ProvincesResp extends BaseResp {
    private List<ProvinceModel> data;

    public ProvincesResp(List<ProvinceModel> data) {
        this.data = data;
    }

    public List<ProvinceModel> getData() {
        return data;
    }

    public void setData(List<ProvinceModel> data) {
        this.data = data;
    }
}
