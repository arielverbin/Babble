package com.example.babble.API;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.registeration.RequestCallBack;
import com.example.babble.registeration.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private final WebServiceAPI webServiceAPI;

    public UserAPI() {
        // Create Gson instance with lenient mode
        Gson gson = new GsonBuilder().setLenient().create();

        // Create Retrofit instance with base URL and Gson converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Create WebServiceAPI instance for making API calls
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void login(User loginUser, Context context, RequestCallBack callback) {
        Call<String> call = webServiceAPI.login(loginUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    // get user details and start the contacts activity.
                    MyApplication.setToken(response.body());
                    getUserDetails(context, callback, loginUser.getUsername(), response.body());

                } else {
                    if(response.code() == 404) {
                        callback.onFailure("Incorrect password or username.");
                    } else {
                        callback.onFailure("Error logging in (code: " + response.code() + ")");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callback.onFailure("Error logging in: server connectivity error.");
            }
        });
    }

    public void getUserDetails(Context context, RequestCallBack callback, String username, String token) {
        Call<User> call = webServiceAPI.getUserDetails(username, "application/json", "Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()) {
                    User userDetails = response.body();
                    if(userDetails != null) {
                        MyApplication.setUser(userDetails);
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Error fetching your details from server.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callback.onFailure("Error fetching your details from server: server connectivity error.");
            }
        });
    }

    public void post(User user, Context context, RequestCallBack callback) {
        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // auto login.
                    User loginUser = new User(user.getUsername(), user.getPassword());
                    login(loginUser, context, callback);
                } else {
                    if(response.code() == 409) {
                        callback.onFailure("Username is already taken.");
                    } else {
                        callback.onFailure("Error creating user.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callback.onFailure("Error signing up: server connectivity error");
            }
        });
    }

}
