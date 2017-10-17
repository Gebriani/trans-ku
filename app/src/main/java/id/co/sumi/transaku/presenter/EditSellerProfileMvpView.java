package id.co.sumi.transaku.presenter;

import java.util.List;

import id.co.sumi.transaku.model.CityModel;

/**
 * Created by alodokter on 05/05/17.
 */

public interface EditSellerProfileMvpView {

    void getCities(boolean isSuccess, List<CityModel> cityModels);
    void showActivity(boolean isSuccess, String message);
}
