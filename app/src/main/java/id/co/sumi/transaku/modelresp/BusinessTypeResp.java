package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.BusinessTypeModel;


public class BusinessTypeResp extends BaseResp {
    private List<BusinessTypeModel> data;

    public BusinessTypeResp(List<BusinessTypeModel> data) {
        this.data = data;
    }

    public List<BusinessTypeModel> getData() {
        return data;
    }

    public void setData(List<BusinessTypeModel> data) {
        this.data = data;
    }
}
