package id.co.sumi.transaku.presenter;

import java.util.List;

import id.co.sumi.transaku.model.InventoryModel;

/**
 * Created by gebriani on 16/03/17.
 */

public interface SupplierInventoryMvpView  {
    void showResponse(boolean isSuceess, List<InventoryModel> resp, String message);
}
