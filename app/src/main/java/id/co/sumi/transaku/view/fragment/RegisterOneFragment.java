package id.co.sumi.transaku.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.UserProfileModel;

/**
 * Created by alodokter on 12/05/17.
 */

public class RegisterOneFragment extends Fragment implements View.OnClickListener {

    private static String USERPROFILE = "user_profile";
    private Spinner spinCustType;
    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRetryPassword;
    private Spinner spinGender;
    private Button btCancel;
    private Button btNext;
    private String genderStr;
    private RegisterInterface registerInterface;
    private UserProfileModel userProfileModel;

    public static RegisterOneFragment newInstance(String userProfile){
        RegisterOneFragment registerOneFragment = new RegisterOneFragment();
        Bundle args = new Bundle();
        args.putString(USERPROFILE, userProfile);
        registerOneFragment.setArguments(args);
        return registerOneFragment;
    }

    private void findView(View view) {
        spinCustType = (Spinner) view.findViewById(R.id.reg_one_spin_cust_type);
        etFullName = (EditText) view.findViewById(R.id.reg_one_et_fullname);
        etEmail = (EditText) view.findViewById(R.id.reg_one_et_email);
        etPassword = (EditText) view.findViewById(R.id.reg_one_et_password);
        etRetryPassword = (EditText) view.findViewById(R.id.reg_one_et_retry_password);
        spinGender = (Spinner) view.findViewById(R.id.reg_one_spin_gender);
        btCancel = (Button) view.findViewById(R.id.reg_one_bt_batal);
        btNext = (Button) view.findViewById(R.id.reg_one_bt_next);
        btCancel.setOnClickListener(this);
        btNext.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registerInterface = (RegisterInterface) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileModel = TransakuApplication.getInstance().getGson().fromJson(getArguments().getString(USERPROFILE), UserProfileModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_one, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_one_bt_batal:
                registerInterface.closeFragment();
                break;
            case R.id.reg_one_bt_next:
                if(checkInputUser()){
                    genderStr = spinGender.getSelectedItem().toString().equalsIgnoreCase("Pria") ? "L" : "P";
                    userProfileModel.setGender(genderStr);
                    userProfileModel.setName(etFullName.getText().toString());
                    userProfileModel.setEmail(etEmail.getText().toString());
                    userProfileModel.setPassword(etPassword.getText().toString());
                    registerInterface.nextFragment(1,userProfileModel);
                }
                break;
        }
    }

    private boolean checkInputUser() {
        if (spinCustType.getSelectedItemPosition() == 0) {
            return false;
        }
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Tidak Boleh Kosong");
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Tidak Boleh Kosong");
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Tidak Boleh Kosong");
            return false;
        }
        if (etRetryPassword.getText().toString().trim().isEmpty()) {
            etRetryPassword.setError("Tidak Boleh Kosong");
            return false;
        }
        if (!etRetryPassword.getText().toString().equalsIgnoreCase(etPassword.getText().toString())) {
            etRetryPassword.setError("Password Tidak Sama");
            return false;
        }
        if (spinGender.getSelectedItemPosition() == 0) {
            return false;
        }
        return true;
    }
}
