package id.co.sumi.transaku.presenter;

/**
 * Created by gebriani on 7/23/17.
 */

public interface BookingMvpView {
    void responseOrder(boolean isSuceess, String message);
    void responseBuy(boolean isSuceess, String message);
}
