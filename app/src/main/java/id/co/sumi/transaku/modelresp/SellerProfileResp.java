package id.co.sumi.transaku.modelresp;

import id.co.sumi.transaku.model.UserProfileModel;

/**
 * Created by gebriani on 20/04/17.
 */

public class SellerProfileResp extends BaseResp {
    private UserProfileModel data;

    public SellerProfileResp(UserProfileModel data) {
        this.data = data;
    }

    public UserProfileModel getData() {
        return data;
    }

    public void setData(UserProfileModel data) {
        this.data = data;
    }
}
