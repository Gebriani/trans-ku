package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.view.MvpView;

/**
 * Created on 03/03/17.
 */

public interface AddInventoryMvpView extends MvpView {

    void showProgressIndicator();

    void showDialogAfterAddInventory(boolean isSuccess, String message);

    void dismissDialog();

    void showDialogAfterRestockInventory(boolean isSuccess, String message);
}
