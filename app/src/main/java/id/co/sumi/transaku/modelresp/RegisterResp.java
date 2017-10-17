package id.co.sumi.transaku.modelresp;


/**
 * Created by gebriani on 14/03/17.
 */

public class RegisterResp extends BaseResp {
    private Long data;

    public RegisterResp(Long data) {
        this.data = data;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }
}