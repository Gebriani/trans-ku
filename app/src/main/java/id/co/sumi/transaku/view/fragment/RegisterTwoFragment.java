package id.co.sumi.transaku.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.CityModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.ProvinceModel;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.presenter.RegisterMvpView;
import id.co.sumi.transaku.presenter.RegisterPresenter;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerCity;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerProvince;

/**
 * Created by alodokter on 12/05/17.
 */

public class RegisterTwoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RegisterMvpView {

    private static String USERPROFILE = "user_profile";
    private EditText etNoKtp;
    private EditText etAddress;
    private Spinner spinProvinsi;
    private Spinner spinCity;
    private EditText etPortalCode;
    private EditText etPhone;
    private Button btCancel;
    private Button btNext;
    private RegisterInterface registerInterface;
    private UserProfileModel userProfileModel;
    private List<CityModel> cityModelList = new ArrayList<>();
    private CustomSpinnerProvince provinceAdapter;
    private CustomSpinnerCity cityAdapter;
    private RegisterPresenter presenter;


    public static RegisterTwoFragment newInstance(String userProfile){
        RegisterTwoFragment registerTwoFragment = new RegisterTwoFragment();
        Bundle args = new Bundle();
        args.putString(USERPROFILE, userProfile);
        registerTwoFragment.setArguments(args);
        return registerTwoFragment;
    }

    private void findViews(View view) {
        etNoKtp = (EditText) view.findViewById(R.id.reg_two_et_no_ktp);
        etAddress = (EditText) view.findViewById(R.id.reg_two_et_address);
        spinProvinsi = (Spinner) view.findViewById(R.id.reg_two_spin_provinsi);
        spinCity = (Spinner) view.findViewById(R.id.reg_two_spin_city);
        etPortalCode = (EditText) view.findViewById(R.id.reg_two_et_portal_code);
        etPhone = (EditText) view.findViewById(R.id.reg_two_et_phone);
        btCancel = (Button) view.findViewById(R.id.reg_two_bt_batal);
        btNext = (Button) view.findViewById(R.id.reg_two_bt_next);
        btCancel.setOnClickListener(this);
        btNext.setOnClickListener(this);

        PopulateCombo populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);

        if(populateCombo != null){
            provinceAdapter = new CustomSpinnerProvince(getContext(), populateCombo.getProvinces());
        }
        spinProvinsi.setAdapter(provinceAdapter);
        cityModelList.add(0, new CityModel(-1, "- Pilih Kota -"));
        cityAdapter = new CustomSpinnerCity(getContext(), cityModelList);
        spinCity.setAdapter(cityAdapter);
        spinProvinsi.setOnItemSelectedListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registerInterface = (RegisterInterface) context;
        presenter = new RegisterPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileModel = TransakuApplication.getInstance().getGson().fromJson(getArguments().getString(USERPROFILE), UserProfileModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_two, container, false);
        findViews(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_two_bt_batal:
                registerInterface.closeFragment();
                break;
            case R.id.reg_two_bt_next:
                if(checkInputUser()){
                    userProfileModel.setIdCardNumber(etNoKtp.getText().toString());
                    userProfileModel.setAddress(etAddress.getText().toString());
                    userProfileModel.setProvince((ProvinceModel) spinProvinsi.getSelectedItem());
                    userProfileModel.setCity((CityModel) spinCity.getSelectedItem());
                    userProfileModel.setIsSeller(1);
                    userProfileModel.setPostalCode(Integer.parseInt(etPortalCode.getText().toString()));
                    userProfileModel.setPhone(etPhone.getText().toString());
                    registerInterface.nextFragment(2, userProfileModel);
                }
                break;
        }
    }

    private boolean checkInputUser() {
        if (etNoKtp.getText().toString().trim().toString().isEmpty()) {
            etNoKtp.setError("Tidak Boleh Kosong");
            return false;
        }
        if (etAddress.getText().toString().trim().toString().isEmpty()) {
            etAddress.setError("Tidak Boleh Kosong");
            return false;
        }
        if (etPortalCode.getText().toString().trim().toString().isEmpty()) {
            etPortalCode.setError("Tidak Boleh Kosong");
            return false;
        }
        if (etPhone.getText().toString().trim().toString().isEmpty()) {
            etPhone.setError("Tidak Boleh Kosong");
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.reg_two_spin_provinsi){
            String provinci_id = String.valueOf(((ProvinceModel) spinProvinsi.getSelectedItem()).getId());
            presenter.getCitiesByProvincesID(provinci_id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void getCities(boolean isSuccess, List<CityModel> cityModelList) {
        this.cityModelList.addAll(cityModelList);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void showResult(boolean isSuccess, String message) {

    }

    @Override
    public void showVerification(boolean isSuccess, String message) {

    }

    @Override
    public void showResult(boolean isSuccess, Long userID) {

    }
}
