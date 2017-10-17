package id.co.sumi.transaku.view.fragment;

import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by gebriani on 15/03/17.
 */

public class BaseFragment extends Fragment {

    protected void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
