package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.InventoryModel;

/**
 * Created by alodokter on 03/03/17.
 */

public class InventoryListResp extends BaseResp {
    private List<InventoryModel> data;

    public InventoryListResp(List<InventoryModel> data) {
        this.data = data;
    }

    public List<InventoryModel> getData() {
        return data;
    }

    public void setData(List<InventoryModel> data) {
        this.data = data;
    }
}
