package id.co.sumi.transaku.modelresp;

import java.util.List;

import id.co.sumi.transaku.model.CityModel;


public class CitiesResp extends BaseResp {
    private List<CityModel> data;

    public CitiesResp(List<CityModel> data) {
        this.data = data;
    }

    public List<CityModel> getData() {
        return data;
    }

    public void setData(List<CityModel> data) {
        this.data = data;
    }
}
