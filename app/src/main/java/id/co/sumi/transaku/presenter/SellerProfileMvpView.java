package id.co.sumi.transaku.presenter;

import java.util.List;

import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;

/**
 * Created by gebriani on 20/04/17.
 */

public interface SellerProfileMvpView {
    void showActivity(boolean isSuccess, UserProfileModel userProfileModel, String message);
    void showChangePassword(boolean isSuccess, String message);
    void refreshActivity(boolean isSuccess, String message);

}
