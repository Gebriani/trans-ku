package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.SellModel;


public class SellListResp extends BaseResp{
    private List<SellModel> data;

    public SellListResp(List<SellModel> data) {
        this.data = data;
    }

    public List<SellModel> getData() {
        return data;
    }

    public void setData(List<SellModel> data) {
        this.data = data;
    }
}
