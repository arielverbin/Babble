package com.example.babble.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MutableLiveData;

import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityRegisterBinding;
import com.example.babble.registeration.LoginActivity;
import com.example.babble.registeration.LoginUser;
import com.example.babble.registeration.PostCallback;
import com.example.babble.registeration.RegisterActivity;
import com.example.babble.registeration.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI() {

//        retrofit = new Retrofit.Builder()
//                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        webServiceAPI = retrofit.create(WebServiceAPI.class);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void login(LoginUser loginUser, Context context, PostCallback callback) {
        Call<String> call = webServiceAPI.login(loginUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200){
                    String token = response.body();
                    MyApplication.token = token;

                    Intent i = new Intent(context, ContactsActivity.class);
                    context.startActivity(i);
                }
                else if (response.code() == 404){
                    callback.onPostFail();
                    return;
                }
//                List<User> users = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
            }
        });
    }

    public void post(User user, Context context, PostCallback callback) {


        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
//                    Toast.makeText(MyApplication.context, "succeeded",
//                            Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, ContactsActivity.class);
                        context.startActivity(i);
                } else if (response.code() == 409) {
//                    Toast.makeText(MyApplication.context, "failed",
//                            Toast.LENGTH_SHORT).show();
                    callback.onPostFail();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("11");
            }
        });
    }
}
