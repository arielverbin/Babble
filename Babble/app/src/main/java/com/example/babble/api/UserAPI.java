package com.example.babble.api;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.chats.UserDataToSet;
import com.example.babble.chats.UserDetailsFromServer;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.registeration.LoginUser;
import com.example.babble.registeration.PostCallback;
import com.example.babble.registeration.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI() {
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

    public void getUserDetails(Context context, PostCallback callback, int id) {
        Call<UserDetailsFromServer> call = webServiceAPI.getUserDetails(id,
                "application/json",
                "Bearer " + MyApplication.token);
        call.enqueue(new Callback<UserDetailsFromServer>() {
            @Override
            public void onResponse(Call<UserDetailsFromServer> call, Response<UserDetailsFromServer> response) {
                if (response.code() == 200) {
                    UserDetailsFromServer userDetailsFromServer = response.body();
                }
            }
            @Override
            public void onFailure(Call<UserDetailsFromServer> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    public void setUserDetails(Context context, PostCallback callback, int id,
                               String username, String newPic, String newDisplayName) {
        UserDataToSet data = new UserDataToSet(newPic, newDisplayName);
        Call<Void> call = webServiceAPI.setUserDetails(id,
                "application/json",
                "Bearer " + MyApplication.token,
                data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }



    public void register(User user, Context context, PostCallback callback) {


        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                        Intent i = new Intent(context, ContactsActivity.class);
                        context.startActivity(i);
                } else if (response.code() == 409) {

                    callback.onPostFail();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
