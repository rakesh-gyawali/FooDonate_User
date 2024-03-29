package com.example.foodonate.account;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodonate.R;
import com.example.foodonate.URL;
import com.example.foodonate.account.uploadImageDevelopment.UploadImageBLL;
import com.example.foodonate.account.userRegistrationDevelopment.UserRegistrationBLL;
import com.example.foodonate.util.SharedPreference;

import static android.app.Activity.RESULT_OK;

public class SelectProfilePictureFragment extends Fragment implements View.OnClickListener {
    private ImageView imgPP;
    private TextView tvSkip;
    private Button btnSelect;

    private Integer CHOOSE_FROM_GALLERY = 0;
    private Integer CHOOSE_FROM_CAMERA = 1;

    private String imagePath;
    private String file_name;

    private String firstname;
    private String lastname;
    private String phone;
    private String password;

    private boolean isImageUploaded;

    private AlertDialog.Builder builder;
    private UploadImageBLL uploadImageBLL;


    public SelectProfilePictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_profile_picture, container, false);
        imgPP = view.findViewById(R.id.imgPP);
        tvSkip = view.findViewById(R.id.tvSkip);
        btnSelect = view.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(this);
        tvSkip.setOnClickListener(this);

        isImageUploaded = false;
        return view;
    }


    @Override
    public void onClick(View v) {
        getSignUpInfoFromSharedPreference();
        switch (v.getId()) {
            case R.id.btnSelect:
                if (isImageUploaded)  {
                    registerCall();
                    return;
                }
                checkCameraPermission();
                popUpSelectFromCameraOrGallery();
                break;
            case R.id.tvSkip:
                break;

        }
    }

    private void registerCall() {
        URL.getStrictMode();
        UserRegistrationBLL bll = new UserRegistrationBLL(phone, password, firstname, lastname, file_name);
        if (bll.checkRegister()) {
            Fragment profileFragment = new ProfileFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SharedPreference.clearLoggedInStatus();
            Toast.makeText(getContext(), "registerCall done ...", Toast.LENGTH_SHORT).show();

            if (bll.checkLogin()) {
                transaction.replace(R.id.frameLayout, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Toast.makeText(getContext(), "login done ...", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), "login failed ...", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(getContext(), "registerCall fail ...", Toast.LENGTH_SHORT).show();
        }

    }

    private void getSignUpInfoFromSharedPreference() {
        SharedPreferences savedData = this.getActivity().getSharedPreferences("SIGN_UP", Context.MODE_PRIVATE);
        firstname =  savedData.getString("firstname", "");
        lastname =  savedData.getString("lastname", "");
        phone =  savedData.getString("phone", "");
        password =  savedData.getString("password", "");

        if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Error while getting value from shared preference ...", Toast.LENGTH_LONG).show();
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void popUpSelectFromCameraOrGallery() {
        final CharSequence[] options = { "Choose from Gallery","Cancel" };
        builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Add Profile Pic!")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("debug", String.valueOf(which));

                        if (which == CHOOSE_FROM_GALLERY) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, 0);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uploadImageBLL = new UploadImageBLL();
        if (resultCode == RESULT_OK) {
            if ( data == null ) {
                Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_LONG).show();
            }
        }
        Uri uri = data.getData();
        imagePath = uploadImageBLL.getRealPathFromUri(uri, getContext());
        uploadImageBLL.previewImage(imagePath, imgPP);

        if (uploadImageBLL.checkImageUpload(imagePath)) {
            file_name = uploadImageBLL.returnFilename();
            btnSelect.setText("Create Account");
            isImageUploaded = true;
            Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Upload Image Failed ... ", Toast.LENGTH_SHORT).show();
        }
    }
}