package com.example.foodonate.account.userDevelopment;

import android.util.Log;

import com.example.foodonate.URL;
import com.example.foodonate.util.SharedPreference;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class UserBLL {
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String password;
    private String profilePicture;

    private Response<UserResponse> response;

    public UserBLL() {
    }

    public UserBLL(String firstName, String lastName, String phoneNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
    }

    public UserBLL(String firstName, String lastName, String phoneNo, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public boolean checkGetUser() {
        UserAPI api = URL.getInstance().create(UserAPI.class);
        String token = SharedPreference.getToken();
        if (token.isEmpty()) {
            Log.i("UserBLL", "TOKEN IS EMPTY ...");
            return false;
        }
        Call<UserResponse> call = api.getUser(token);
        try {
             response = call.execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUpdateProfile() throws IOException {
        UserAPI api = URL.getInstance().create(UserAPI.class);
        String token = SharedPreference.getToken();
        if (token.isEmpty()) {
            Log.i("UserBLL", "TOKEN IS EMPTY ...");
            return false;
        }
        Call<Void> call = api.putUser(token, firstName, lastName, phoneNo, password, profilePicture);
        Response<Void> response =  call.execute();
        if (response.isSuccessful()) return true;
        return false;
    }

    public UserResponse returnUser() {
        return response.body();
    }

}
