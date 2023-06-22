package com.example.babble.API;

import com.example.babble.UserDataToSettings;
import com.example.babble.chats.Message;
import com.example.babble.chats.ServerMessage;

import com.example.babble.contacts.Contact;
import com.example.babble.contacts.ServerContact;
import com.example.babble.registeration.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("Users")
    Call<List<User>> getUsers();

    @POST("Tokens")
    Call<String> login(@Body User loginUser);

    @POST("Users")
    Call<Void> createUser(@Body User user);

    @DELETE("Users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @GET("Users/{username}")
    Call<User> getUserDetails(@Path("username") String username,
                                               @Header("Content-Type") String contentType,
                                               @Header("Authorization") String token);
    @PUT("Users/{username}")
    Call<Void> setUserDetails(@Path("username") String username,
                              @Header("Content-Type") String contentType,
                              @Header("Authorization") String token,
                              @Body UserDataToSettings data);

    @GET("Chats")
    Call<List<ServerContact>> getContacts(@Header("Content-Type") String contentType,
                                          @Header("Authorization") String token);
    @POST("Chats")
    Call<ServerContact> addContact(@Header("Content-Type") String contentType,
                                     @Header("Authorization") String token,
                                     @Body Contact contact);
    @GET("Chats/{id}/Messages")
    Call<List<ServerMessage>> getMessages(@Path("id") String id,
                                          @Header("Content-Type") String contentType,
                                          @Header("Authorization") String token);

    @POST("Chats/{id}/Messages")
    Call<Void> sendMessage(@Path("id") String id,
                           @Header("Content-Type") String contentType,
                           @Header("Authorization") String token,
                           @Body Message message);

    @DELETE("Chats/{id}")
    Call<Void> deleteContact(@Path("id") String id,
                             @Header("Content-Type") String contentType,
                             @Header("Authorization") String token);

}
