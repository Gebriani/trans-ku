package id.co.sumi.transaku.view.fragment;

import id.co.sumi.transaku.model.UserProfileModel;

/**
 * Created by alodokter on 12/05/17.
 */

public interface RegisterInterface {
    void closeFragment();
    void nextFragment(int step, UserProfileModel userProfileModel);
    void finishStepRegister(UserProfileModel userProfileModel);
}
