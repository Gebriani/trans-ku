package id.co.sumi.transaku.modelresp;

import id.co.sumi.transaku.model.PopulateCombo;

/**
 * Transaku
 * Author Gebriani
 * on 12, March 2017.
 */

public class PopulateComboResp extends BaseResp {

    private PopulateCombo data;

    public PopulateComboResp(PopulateCombo data) {
        this.data = data;
    }

    public PopulateCombo getData() {
        return data;
    }

    public void setData(PopulateCombo data) {
        this.data = data;
    }
}
