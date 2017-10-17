package id.co.sumi.transaku.utils;

/**
 * Created by MuhammadAbrar on 01/26/2016.
 */
public class RxBusObject {

    private String key;
    private Object object;

    public RxBusObject(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    public String getKey() {
        return key;
    }

    public Object getObject() {
        return object;
    }

}
