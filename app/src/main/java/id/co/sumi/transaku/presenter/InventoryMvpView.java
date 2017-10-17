package id.co.sumi.transaku.presenter;

import java.util.List;

import id.co.sumi.transaku.model.InventoryModel;

/**
 * Created on 02/03/17.
 */

public interface InventoryMvpView {
    void showActivity(boolean isSuccess, List<InventoryModel> inventory, String message);
    void searchResult(boolean isSuccess, List<InventoryModel> inventory, String message);
    void editPrice(boolean isSuccess, String message);
    void deleteInventory(boolean isSuccess, String message);
}
