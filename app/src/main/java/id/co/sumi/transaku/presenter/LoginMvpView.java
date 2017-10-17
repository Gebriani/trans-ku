package id.co.sumi.transaku.presenter;

import id.co.sumi.transaku.model.LoginBean;
import id.co.sumi.transaku.modelresp.BaseResp;

/**
 * Created on 27/02/17.
 */

public interface LoginMvpView {
    void showHome(boolean isSuccess, LoginBean loginBean);
    void showHome(boolean isSuccess, String message);
}