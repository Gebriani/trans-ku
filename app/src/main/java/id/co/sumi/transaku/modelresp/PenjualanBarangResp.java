package id.co.sumi.transaku.modelresp;

import java.util.Map;

/**
 * Created by gebriani on 04/04/17.
 */

public class PenjualanBarangResp extends BaseResp {

    private Map<String, String> data;

    public PenjualanBarangResp(Map<String, String> data) {
        this.data = data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }
}
