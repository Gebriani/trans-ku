package id.co.sumi.transaku.presenter;


import java.util.List;

import id.co.sumi.transaku.model.CityModel;

public interface RegisterMvpView {
    void getCities(boolean isSuccess, List<CityModel> cityModelList);
    void showResult(boolean isSuccess, String message);
    void showVerification(boolean isSuccess, String message);
    void showResult(boolean isSuccess, Long userID);

}
