package id.co.sumi.transaku.view;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
