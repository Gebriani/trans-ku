package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.modelresp.BaseResp;

/**
 * Transaku
 * on 12, March 2017.
 */

public interface GeneralMvpView {
    void showToken(boolean isSuccess, String message);
    void showPopulateCombo(boolean isSuccess, PopulateCombo populateCombo, String message);
}