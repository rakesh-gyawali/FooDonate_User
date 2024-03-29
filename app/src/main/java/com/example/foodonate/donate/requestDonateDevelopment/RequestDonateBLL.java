package com.example.foodonate.donate.requestDonateDevelopment;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodonate.MainActivity;
import com.example.foodonate.URL;

import retrofit2.Call;
import retrofit2.Response;

public class RequestDonateBLL {
    private String token;
    private String requestedDate;
    private String address;
    private String lats;
    private String longs;
    private String charity;
    private String quantity;
    private String expiryDate;
    private String foodTypes;

    public RequestDonateBLL(String token, String requestedDate, String address, String lats, String longs, String charity, String quantity, String expiryDate, String foodTypes) {
        this.token = token;
        this.requestedDate = requestedDate;
        this.address = address;
        this.lats = lats;
        this.longs = longs;
        this.charity = charity;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.foodTypes = foodTypes;
    }

    public boolean checkPostDonation() {
        RequestDonateAPI requestDonateAPI = URL.getInstance().create(RequestDonateAPI.class);
        Call<Void> call = requestDonateAPI.postRequestDonate(token, requestedDate, address, lats, longs, charity, quantity, expiryDate, foodTypes);

        try {
            Response<Void> response = call.execute();
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
