package com.example.babble.API;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.serverObjects.UserDataToSettings;
import com.example.babble.entities.Preference;
import com.example.babble.entities.PreferenceDao;
import com.example.babble.services.WebServiceAPI;
import com.example.babble.utilities.RequestCallBack;
import com.example.babble.serverObjects.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private final WebServiceAPI webServiceAPI;

    private final PreferenceDao preferenceDao;

    public UserAPI(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        preferenceDao = db.preferenceDao();

        // Create Gson instance with lenient mode
        Gson gson = new GsonBuilder().setLenient().create();

        // Create Retrofit instance with base URL and Gson converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preferenceDao.get("serverUrl"))
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
                    String token = response.body();
                    preferenceDao.set(new Preference("token", token));

                    getUserDetails(context, callback, loginUser.getUsername(), response.body());

                } else {
                    if(response.code() == 404) {
                        callback.onFailure("Incorrect password or username.");
                    } else if(response.code() == 409){
                        callback.onFailure("Another device is currently logged in to this account. Please log out from that device and try again.");
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
                        preferenceDao.set(new Preference("username", userDetails.getUsername()));
                        preferenceDao.set(new Preference("profilePic", userDetails.getProfilePic()));
                        preferenceDao.set(new Preference("displayName", userDetails.getDisplayName()));
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

    public void setUserDetails(Context context, UserDataToSettings data, RequestCallBack callBack) {
        String token = "Bearer " + preferenceDao.get("token");
        Call<Void> call = webServiceAPI.setUserDetails(preferenceDao.get("username"),
                "application/json", token, data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    callBack.onSuccess();
                } else {
                    callBack.onFailure("Error saving data (Code " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callBack.onFailure("Error updating your data: server connectivity error.");
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
