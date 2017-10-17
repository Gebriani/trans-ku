package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.InventoryCategoryModel;

/**
 * Created on 03/03/17.
 */

public class InventoryCategoryResp extends BaseResp {
    private List<InventoryCategoryModel> data;

    public InventoryCategoryResp(List<InventoryCategoryModel> data) {
        this.data = data;
    }

    public List<InventoryCategoryModel> getData() {
        return data;
    }

    public void setData(List<InventoryCategoryModel> data) {
        this.data = data;
    }
}