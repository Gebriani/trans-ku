package id.co.sumi.transaku.presenter;

import java.util.List;

import id.co.sumi.transaku.model.SupplierModel;

/**
 * Transaku
 * Author Gebriani
 * on 15, March 2017.
 */

public interface SearchSupplierMvpView  {
    void showResponse(boolean isSuceess, String message);
    void showResponse(boolean isSuceess, List<SupplierModel> supplierModel);

    void showSearchResult(boolean isSuccess, List<SupplierModel> supplierModel);
}
