package id.co.sumi.transaku.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.BusinessTypeModel;
import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.CropImage;
import id.co.sumi.transaku.utils.InternalContentProvider;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.activity.NewInventoryActivity;
import id.co.sumi.transaku.view.customdialog.CustomSpinnerBusinessType;

/**
 * Created by alodokter on 12/05/17.
 */

public class RegisterThreeFragment extends Fragment implements View.OnClickListener {

    private static String USERPROFILE = "user_profile";
    private Spinner spinBusinessName;
    private EditText etStoreName;
    private Button btNext;
    private TextView tvUploadProfile;
    private TextView tvUploadKTP;
    private RegisterInterface registerInterface;
    private UserProfileModel userProfileModel;
    private CustomSpinnerBusinessType businessTypeAdapter;
    private String profile_path, ktp_path = "";
    private File tempFileProfile, tempFileKTP;
    private int flagPhoto;


    public static RegisterThreeFragment newInstance(String userProfile){
        RegisterThreeFragment registerThreeFragment = new RegisterThreeFragment();
        Bundle args = new Bundle();
        args.putString(USERPROFILE, userProfile);
        registerThreeFragment.setArguments(args);
        return registerThreeFragment;
    }

    private void findView(View view) {
        spinBusinessName = (Spinner) view.findViewById(R.id.reg_three_spin_business_name);
        etStoreName = (EditText) view.findViewById(R.id.reg_three_et_store_name);
        btNext = (Button) view.findViewById(R.id.reg_three_bt_next);
        tvUploadProfile = (TextView) view.findViewById(R.id.reg_three_upload_profile);
        tvUploadKTP = (TextView) view.findViewById(R.id.reg_three_upload_ktp);

        btNext.setOnClickListener(this);
        tvUploadProfile.setOnClickListener(this);
        tvUploadKTP.setOnClickListener(this);

        PopulateCombo populateCombo = TransakuApplication.getInstance().getGson().fromJson(PrefHelper.getString(Const.POPULATE_COMBO_DATA), PopulateCombo.class);
        businessTypeAdapter = new CustomSpinnerBusinessType(getContext(), populateCombo.getBusinessTypes());
        spinBusinessName.setAdapter(businessTypeAdapter);
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            tempFileProfile =  new File(Environment.getExternalStorageDirectory(), Const.TEMP_PHOTO_FILE_NAME);
            tempFileKTP = new File(Environment.getExternalStorageDirectory(), Const.TEMP_KTP_FILE_NAME);
        } else {
            tempFileProfile = new File(getActivity().getFilesDir(), Const.TEMP_PHOTO_FILE_NAME);
            tempFileKTP = new File(getActivity().getFilesDir(), Const.TEMP_KTP_FILE_NAME);
        }
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
        View view = inflater.inflate(R.layout.fragment_register_three, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_three_bt_next:
                if(checkInputUser()){
                    userProfileModel.setBusinessName(etStoreName.getText().toString());
                    userProfileModel.setBusinessType((BusinessTypeModel) spinBusinessName.getSelectedItem());
                    userProfileModel.setIdCardBase64(ktp_path);
                    userProfileModel.setPictureBase64(profile_path);
                    registerInterface.finishStepRegister(userProfileModel);
                } else {
                    etStoreName.setError("Tidak Boleh Kosong");
                }
                break;
            case R.id.reg_three_upload_profile:
                flagPhoto = 0;
                checkPermission();
                break;
            case R.id.reg_three_upload_ktp:
                flagPhoto = 1;
                checkPermission();
                break;
        }
    }

    private boolean checkInputUser() {
        if(etStoreName.getText().toString().trim().isEmpty()){
            etStoreName.setError("Tidak Boleh Kosong");
            return false;
        }

//        if(profile_path == null && profile_path.equalsIgnoreCase("")){
//            return false;
//        }
//
//        if(ktp_path == null && ktp_path.equalsIgnoreCase("")){
//            return false;
//        }
        return true;
    }


    //TAKE PHOTO
    private void checkPermission(){
        if (Build.VERSION.SDK_INT > 22) {
            String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
            String cameraPermission = Manifest.permission.CAMERA;
            int hasWritePermission = ContextCompat.checkSelfPermission(getActivity(), writeExternalStorage);
            int hasReadPermission = ContextCompat.checkSelfPermission(getActivity(), readExternalStorage);
            int hasCameraPermission = ContextCompat.checkSelfPermission(getActivity(), cameraPermission);
            List<String> permissions = new ArrayList<String>();
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(writeExternalStorage);
                permissions.add(readExternalStorage);
                permissions.add(cameraPermission);
            }
            if (!permissions.isEmpty()) {
                String[] params = permissions.toArray(new String[permissions.size()]);
                requestPermissions(params, Const.PERMISSION_REQUEST_CODE_FOR_EXTERNAL_STORAGE);
            } else {
                takePicture();
            }
        } else {
            takePicture();
        }
    }

    private void takePicture() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder
                .setTitle(R.string.image_prompt)
                .setItems(R.array.image_arrays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                AvatarFromGallery();
                                dialog.dismiss();
                                break;
                            case 1:
                                AvatarFromCamera();
                                dialog.dismiss();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void AvatarFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri cameraFileName = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                switch (flagPhoto){
                    case 0:
                        cameraFileName = Uri.fromFile(tempFileProfile);
                        break;
                    case 1:
                        cameraFileName = Uri.fromFile(tempFileKTP);
                        break;
                }
            } else {
                cameraFileName = InternalContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraFileName);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, Const.CHOICE_AVATAR_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    //take image from gallery
    private void AvatarFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Const.CHOICE_AVATAR_FROM_GALLERY);
    }

    private void startCropImage() {
        Intent intent = new Intent(getActivity(), CropImage.class);
        switch (flagPhoto){
            case 0:
                intent.putExtra(CropImage.IMAGE_PATH, tempFileProfile.getPath());
                intent.putExtra(CropImage.SCALE, true);
                intent.putExtra(CropImage.ASPECT_X, 3);
                intent.putExtra(CropImage.ASPECT_Y, 3);
                intent.putExtra(CropImage.OUTPUT_X, 300);
                intent.putExtra(CropImage.OUTPUT_Y, 300);
                startActivityForResult(intent, Const.CHOICE_AVATAR_FROM_CAMERA_CROP);
                break;
            case 1:
                intent.putExtra(CropImage.IMAGE_PATH, tempFileKTP.getPath());
                intent.putExtra(CropImage.SCALE, true);
                intent.putExtra(CropImage.ASPECT_X, 3);
                intent.putExtra(CropImage.ASPECT_Y, 3);
                intent.putExtra(CropImage.OUTPUT_X, 300);
                intent.putExtra(CropImage.OUTPUT_Y, 300);
                startActivityForResult(intent, Const.CHOICE_AVATAR_FROM_CAMERA_CROP);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Const.PERMISSION_REQUEST_CODE_FOR_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.CHOICE_AVATAR_FROM_GALLERY && flagPhoto == 0) {
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(tempFileProfile);
                    CommonFunction.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    if (null != inputStream) {
                        inputStream.close();
                    }
                    startCropImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else  if (requestCode == Const.CHOICE_AVATAR_FROM_GALLERY && flagPhoto == 1) {
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(tempFileKTP);
                    CommonFunction.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    if (null != inputStream) {
                        inputStream.close();
                    }
                    startCropImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Const.CHOICE_AVATAR_FROM_CAMERA) {
                startCropImage();
            } else if (requestCode == Const.CHOICE_AVATAR_FROM_CAMERA_CROP) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }

                if(flagPhoto == 0){
                    profile_path = CommonFunction.ConvertToBase64(tempFileProfile);
                    tvUploadProfile.setText(tempFileProfile.getPath());
                } else {
                    ktp_path = CommonFunction.ConvertToBase64(tempFileKTP);
                    tvUploadKTP.setText(tempFileKTP.getPath());
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
