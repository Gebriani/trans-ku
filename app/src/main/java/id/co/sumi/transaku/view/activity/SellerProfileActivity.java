package id.co.sumi.transaku.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.UserProfileModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SellerProfileMvpView;
import id.co.sumi.transaku.presenter.SellerProfilePresenter;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.CropCircleTransformation;
import id.co.sumi.transaku.utils.CropImage;
import id.co.sumi.transaku.utils.InternalContentProvider;

/**
 * Created by gebriani on 20/04/17.
 */

public class SellerProfileActivity extends ParentActivity implements View.OnClickListener, SellerProfileMvpView {

    private ImageView ivProfilePic;
    private Button btEdit;
    private TextView tvSellerName;
    private TextView tvSellerEmail;
    private TextView tvChangePassword;
    private TextView tvSellerAddress;
    private TextView tvSellerCity;
    private TextView tvSellerPhoneNumber;
    private TextView tvSellerGender;
    private TextView tvStoreName;
    private TextView tvStoreCategory;
    private TextView tvStoreAddress;
    private TextView tvStorePhoneNumber;
    private ImageView ibChangePicture;
    private SellerProfilePresenter presenter;
    private ProgressDialog progressDialog;
    private ImageView ivSellerKtpPic;
    private LinearLayout llRootData;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private UserProfileModel profileModel;
    private File tempFile;

    private void findViews() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ac_sellerProfile_collapsing);
        toolbar = (Toolbar) findViewById(R.id.ac_sellerProfile_toolbar);
        ivProfilePic = (ImageView) findViewById(R.id.ac_sellerProfile_picProfile);
        btEdit = (Button) findViewById(R.id.ac_sellerProfile_ubah_button);
        tvSellerName = (TextView) findViewById(R.id.ac_sellerProfile_username);
        tvSellerEmail = (TextView) findViewById(R.id.ac_sellerProfile_email);
        tvChangePassword = (TextView) findViewById(R.id.ac_sellerProfile_change_password);
        tvSellerAddress = (TextView) findViewById(R.id.ac_sellerProfile_address);
        tvSellerCity = (TextView) findViewById(R.id.ac_sellerProfile_city);
        tvSellerPhoneNumber = (TextView) findViewById(R.id.ac_sellerProfile_phone_number);
        tvSellerGender = (TextView) findViewById(R.id.ac_sellerProfile_gender);
        tvStoreName = (TextView) findViewById(R.id.ac_sellerProfile_store_name);
        tvStoreCategory = (TextView) findViewById(R.id.ac_sellerProfile_store_category);
        tvStoreAddress = (TextView) findViewById(R.id.ac_sellerProfile_store_address);
        tvStorePhoneNumber = (TextView) findViewById(R.id.ac_sellerProfile_store_phone);
        ivSellerKtpPic = (ImageView) findViewById(R.id.ac_sellerProfile_seller_ktp_pic);
        llRootData = (LinearLayout) findViewById(R.id.ac_sellerProfile_root_data);
        ibChangePicture = (ImageView) findViewById(R.id.ac_sellerProfile_change_profilePic);

        btEdit.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        ibChangePicture.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle("Profile Saya");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        findViews();
        presenter = PresenterFactory.sellerProfilePresenter();
        presenter.attachView(this);

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            tempFile = new File(Environment.getExternalStorageDirectory(), Const.TEMP_PHOTO_FILE_NAME);
        } else {
            tempFile = new File(getFilesDir(), Const.TEMP_PHOTO_FILE_NAME);
        }

    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, SellerProfileActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();
        presenter.getSellerProfile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_sellerProfile_change_password:
                showDialogChangePassword(profileModel);
                break;
            case R.id.ac_sellerProfile_ubah_button:
                finish();
                String sellerprofile = TransakuApplication.getInstance().getGson().toJson(profileModel);
                EditSellerProfileActivity.showActivity(this, sellerprofile);
                break;
            case R.id.ac_sellerProfile_change_profilePic:
                checkPermission();
                break;
            case R.id.ac_sellerProfile_picProfile:
                checkPermission();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showActivity(boolean isSuccess, UserProfileModel userProfileModel, String messag) {
        progressDialog.dismiss();
        if(isSuccess){
            this.profileModel = new UserProfileModel();
            this.profileModel = userProfileModel;
            llRootData.setVisibility(View.VISIBLE);
            tvSellerName.setText(userProfileModel.getName());
            tvSellerEmail.setText(userProfileModel.getEmail());
            tvSellerAddress.setText(userProfileModel.getAddress());
            if (userProfileModel.getGender().equalsIgnoreCase("L")) {
                tvSellerGender.setText("Laki-laki");
            } else {
                tvSellerGender.setText("Perempuan");
            }
            tvSellerPhoneNumber.setText(userProfileModel.getPhone());
            tvSellerCity.setText(userProfileModel.getCity().getName());
            tvStoreName.setText(userProfileModel.getBusinessName());
            tvStoreCategory.setText(userProfileModel.getBusinessType().getName());
            tvStoreAddress.setText(userProfileModel.getStoreAddress());
            tvStorePhoneNumber.setText(userProfileModel.getBusinessPhone());

            if (userProfileModel.getPicturePath() != null && !userProfileModel.getPicturePath().equalsIgnoreCase("")) {
                Glide.with(this)
                        .load(userProfileModel.getPicturePath())
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivProfilePic);
            }

            if (userProfileModel.getIdCardPath() != null && !userProfileModel.getIdCardPath().equalsIgnoreCase("")) {
                Glide.with(this)
                        .load(userProfileModel.getIdCardPath())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivSellerKtpPic);
            }
        }

    }

    @Override
    public void showChangePassword(boolean isSuccess, String message) {
        progressDialog.dismiss();
        Toast.makeText(SellerProfileActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void refreshActivity(boolean isSuccess, String message) {
        if(isSuccess){
            progressDialog.show();
            presenter.getSellerProfile();
        } else {
            Toast.makeText(this, message + "", Toast.LENGTH_LONG).show();
        }
    }

    private void showDialogChangePassword(final UserProfileModel userProfile) {
        LayoutInflater layoutInflater = LayoutInflater.from(SellerProfileActivity.this);
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        final EditText old_password = (EditText) dialogView.findViewById(R.id.dialog_changePassword_old_password);
        final EditText new_password = (EditText) dialogView.findViewById(R.id.dialog_changePassword_new_password);
        Button cancel_button = (Button) dialogView.findViewById(R.id.dialog_changePassword_cancel_button);
        Button submit_button = (Button) dialogView.findViewById(R.id.dialog_changePassword_submit_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!old_password.getText().toString().trim().isEmpty() && !new_password.getText().toString().trim().isEmpty()) {
                    userProfile.setOldPassword(old_password.getText().toString());
                    userProfile.setPassword(new_password.getText().toString());
                    presenter.sellerChangePassword(userProfile);
                    alertDialog.dismiss();
                    progressDialog.show();
                } else {
                    Toast.makeText(SellerProfileActivity.this, "Mohon isi form terlampir", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialog.show();
    }


    private void checkPermission(){
        if (Build.VERSION.SDK_INT > 22) {
            String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
            String cameraPermission = Manifest.permission.CAMERA;
            int hasWritePermission = ContextCompat.checkSelfPermission(SellerProfileActivity.this, writeExternalStorage);
            int hasReadPermission = ContextCompat.checkSelfPermission(SellerProfileActivity.this, readExternalStorage);
            int hasCameraPermission = ContextCompat.checkSelfPermission(SellerProfileActivity.this, cameraPermission);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SellerProfileActivity.this);
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
                cameraFileName = Uri.fromFile(tempFile);
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
        Intent intent = new Intent(SellerProfileActivity.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, tempFile.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 3);
        intent.putExtra(CropImage.OUTPUT_X, 300);
        intent.putExtra(CropImage.OUTPUT_Y, 300);

        startActivityForResult(intent, Const.CHOICE_AVATAR_FROM_CAMERA_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
            if (requestCode == Const.CHOICE_AVATAR_FROM_GALLERY) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
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

                String result = CommonFunction.ConvertToBase64(tempFile);
                profileModel.setPictureBase64(result);
                presenter.editProfileSeller(profileModel);

//                try {
//                    InputStream inputStream = new FileInputStream(tempFile.getAbsolutePath());
//                    byte[] buffer = new byte[8192];
//                    int bytesRead;
//                    ByteArrayOutputStream output = new ByteArrayOutputStream();
//                    Base64OutputStream outputStream = new Base64OutputStream(output, Base64.DEFAULT);
//                    try {
//                        while((bytesRead = inputStream.read(buffer))!= -1){
//                            outputStream.write(buffer, 0, bytesRead);
//                        }
//                        outputStream.close();
//                        Log.d("TES_IMAGE", output.toString());
//                        userProfileModel.setPictureBase64(output.toString());
//                        presenter.editProfileSeller(userProfileModel);
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}


//https://github.com/JakeWharton/picasso2-okhttp3-downloader
//http://stackoverflow.com/questions/24273783/android-picasso-library-how-to-add-authentication-headers

//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request newRequest = chain.request().newBuilder()
//                                    .addHeader("X-TOKEN", "VAL")
//                                    .build();
//                            return chain.proceed(newRequest);
//                        }
//                    }).build();
//
//            Picasso picasso = new Picasso.Builder(this)
//                    .downloader(new OkHttpDownloader(client))
//                    .build();
