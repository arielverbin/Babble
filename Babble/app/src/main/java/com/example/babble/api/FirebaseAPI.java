package com.example.babble.API;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.entities.PreferenceDao;
import com.example.babble.services.WebServiceAPI;
import com.example.babble.utilities.RequestCallBack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseAPI {

    private final WebServiceAPI webServiceAPI;
    private final PreferenceDao preferenceDao;

    public FirebaseAPI(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        this.preferenceDao = db.preferenceDao();

        // Create Gson instance with lenient mode
        Gson gson = new GsonBuilder().setLenient().create();

        // Create Retrofit instance with base URL and Gson converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Create WebServiceAPI instance for making API calls
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public void setFirebaseToken(String firebaseToken, RequestCallBack callBack) {
        String JWT = "Bearer " + preferenceDao.get("token");
        Call<Void> call = webServiceAPI.setFirebaseToken("application/json", JWT, new TokenWrapper(firebaseToken));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // Handle successful response
                if (response.isSuccessful()) {
                    callBack.onSuccess();
                } else {
                    callBack.onFailure("error setting token (code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callBack.onFailure("Error setting token: server connectivity error.");
            }
        });
    }

    public void removeFirebaseToken(RequestCallBack callBack) {
        String JWT = "Bearer " + preferenceDao.get("token");
        Call<Void> call = webServiceAPI.removeFirebaseToken("application/json", JWT);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    callBack.onSuccess();
                } else {
                    callBack.onFailure("error removing token (code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callBack.onFailure("Error removing token: server connectivity error.");

            }
        });
    }

    public static class TokenWrapper{
        private String token;

        public TokenWrapper(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
