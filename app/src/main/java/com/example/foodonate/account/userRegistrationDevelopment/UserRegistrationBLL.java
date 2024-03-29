package com.example.foodonate.account.userRegistrationDevelopment;

import retrofit2.Call;
import retrofit2.Response;

import com.example.foodonate.URL;
import com.example.foodonate.util.SharedPreference;

import java.io.IOException;

public class UserRegistrationBLL {
    private String phoneNo;
    private String password;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private boolean isSuccess;

    public UserRegistrationBLL(String phoneNo, String password, String firstName, String lastName, String profilePicture) {
        this.phoneNo = phoneNo;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }

    public UserRegistrationBLL(String phoneNo, String password) {
        this.password = password;
        this.phoneNo = phoneNo;
    }

    public boolean checkLogin() {
        isSuccess = false;
        UserRegistrationAPI userAPI = URL.getInstance().create(UserRegistrationAPI.class);
        Call<LoginResponse> call =userAPI.checkLogin(phoneNo, password);
        try {
            Response<LoginResponse> response = call.execute();
            if (response.isSuccessful()) {
                URL.token = response.body().getToken();
                firstName = response.body().getFirstName();
                lastName = response.body().getLastName();
                phoneNo = response.body().getPhoneNo();
                SharedPreference.storeLoggedInStatus();
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean checkRegister() {
        isSuccess = false;
        UserRegistrationAPI userAPI = URL.getInstance().create(UserRegistrationAPI.class);
        Call<RegistrationResponse> call = userAPI.checkRegister(phoneNo, password, firstName, lastName, profilePicture);
        try {
            Response<RegistrationResponse> response = call.execute();
            if (response.isSuccessful()) {
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }





}
