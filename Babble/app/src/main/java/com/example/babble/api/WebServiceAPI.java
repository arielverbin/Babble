package com.example.babble.api;

import com.example.babble.chats.MessageFromServer;
import com.example.babble.contacts.Contact;
import com.example.babble.contacts.ContactAfterAdd;
import com.example.babble.contacts.ContactFromServer;
import com.example.babble.registeration.LoginUser;
import com.example.babble.registeration.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

     @GET("Chats")
     Call<List<ContactFromServer>> getContacts(@Header("Content-Type") String contentType,
                                               @Header("Authorization") String token);
     @POST("Chats")
     Call<ContactAfterAdd> addContact(@Header("Content-Type") String contentType,
                                      @Header("Authorization") String token,
                                      @Body String username);
     @GET("Chats/{id}/Messages")
     Call<List<MessageFromServer>> getMessages(@Path("id") int id,
                                               @Header("Content-Type") String contentType,
                                               @Header("Authorization") String token);

     @POST("Chats/{id}/Messages")
     Call<Void> sendMessage(@Path("id") int id,
                        @Header("Content-Type") String contentType,
                        @Header("Authorization") String token);
 }
