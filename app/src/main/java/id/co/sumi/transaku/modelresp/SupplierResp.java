package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.SupplierModel;

/**
 * Transaku
 * Author Gebriani
 * on 15, March 2017.
 */

public class SupplierResp extends BaseResp {
    private List<SupplierModel> data;

    public SupplierResp(List<SupplierModel> data) {
        this.data = data;
    }

    public List<SupplierModel> getData() {
        return data;
    }

    public void setData(List<SupplierModel> data) {
        this.data = data;
    }
}