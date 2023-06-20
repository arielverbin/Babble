package com.example.babble.api;

import com.example.babble.registeration.LoginUser;
import com.example.babble.registeration.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
     @GET("Users")
     Call<List<User>> getUsers();

     @POST("Tokens")
     Call<String> login(@Body LoginUser loginUser);

     @POST("Users")
     Call<Void> createUser(@Body User user);

     @DELETE("Users/{id}")
     Call<Void> deleteUser(@Path("id") int id);
 }
