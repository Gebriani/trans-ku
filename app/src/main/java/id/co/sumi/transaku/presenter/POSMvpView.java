package id.co.sumi.transaku.presenter;

import java.util.List;

import id.co.sumi.transaku.model.InventoryModel;

/**
 * Created on 06/03/17.
 */

public interface POSMvpView {
    void searchResult(boolean isSuccess, List<InventoryModel> inventoryModelList, String message);
    void showActivity(boolean isSuccess, List<InventoryModel> inventory, String message);
}
