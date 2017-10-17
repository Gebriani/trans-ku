package id.co.sumi.transaku.presenter;

/**
 * Created on 02/03/17.
 */

public interface Presenter<V> {
    void attachView(V view);
    void detachView();
}
