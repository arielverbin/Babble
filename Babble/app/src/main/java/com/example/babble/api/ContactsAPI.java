package com.example.babble.API;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.entities.Contact;
import com.example.babble.entities.ContactDao;
import com.example.babble.serverObjects.ServerContact;
import com.example.babble.services.WebServiceAPI;
import com.example.babble.utilities.RequestCallBack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAPI {
    WebServiceAPI webServiceAPI;

    String token;
    ContactDao dao;

    public ContactsAPI(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        dao = db.contactDao();

        this.token = db.preferenceDao().get("token");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(db.preferenceDao().get("serverUrl"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getContacts(Context context, RequestCallBack callback) {
        Call<List<ServerContact>> call = webServiceAPI.getContacts("application/json",
                "Bearer " + this.token);
        call.enqueue(new Callback<List<ServerContact>>() {
            @Override
            public void onResponse(@NonNull Call<List<ServerContact>> call,
                                   @NonNull Response<List<ServerContact>> response) {

                List<Contact> contacts = new LinkedList<>();
                if (response.code() == 200) {
                    List<ServerContact> serverContacts = response.body();
                    if (serverContacts != null) {
                        for (ServerContact serverContact : serverContacts) {
                            contacts.add(serverContact.convertToContact());
                        }
                        // success! insert contacts and notify caller with success.
                        //dao.clear();
                        dao.markAllAsDeleted();
                        dao.insertAll(contacts); //will un-mark "deleted" for still-existing chats.
                        callback.onSuccess();

            //rest of function is fail handling - notify caller with failure.

                    } else {
                        callback.onFailure("Could fetch contacts from server.");
                    }
                } else {
                    try {
                        callback.onFailure("Could not fetch contacts from server: " + response.errorBody().string());
                    } catch(Exception e) {
                        Toast.makeText(context, "Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        callback.onFailure("Could not fetch contacts from server: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServerContact>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callback.onFailure("Could fetch contacts from server: server connectivity error.");
            }
        });
    }

    public void addContact(Context context, String username, RequestCallBack callBack) {
        Call<ServerContact> call = webServiceAPI.addContact("application/json",
                "Bearer " + this.token, new Contact("", username, "", "", "", ""));
        call.enqueue(new Callback<ServerContact>() {
            @Override
            public void onResponse(@NonNull Call<ServerContact> call,
                                   @NonNull Response<ServerContact> response) {
                if (response.code() == 200) {
                    ServerContact contactAfterAdd = response.body();
                    if (contactAfterAdd != null) {

                        // success - add new contact and notify caller with success.
                        Contact newContact = contactAfterAdd.convertToContact();
                        newContact.setLastMessage("This conversation is new.");
                        newContact.setTimeChatted("");
                        dao.insert(newContact);
                        callBack.onSuccess();

            // rest of function is fail handling - notify caller with failure.

                    } else {
                        callBack.onFailure("Could not add contact.");
                    }
                } else {
                    try {
                        callBack.onFailure(response.errorBody().string());
                    } catch(Exception e) {
                        Toast.makeText(context, "Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        callBack.onFailure("Could not add contact.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerContact> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callBack.onFailure("Could not add contact: server connectivity error.");
            }
        });
    }

    public void deleteContact(Context context, String id, RequestCallBack callBack) {
        Call<Void> call = webServiceAPI.deleteContact(id,
                "application/json",
                "Bearer " + this.token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.code() == 200 || response.code() == 404) {
                    // success! delete contact and notify caller with success.
                    dao.deleteById(id);
                    callBack.onSuccess();

            // rest of function is fail handling - notify caller with failure.

                } else {
                    callBack.onFailure(response.message() + " (Error code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callBack.onFailure("Could not delete contact: server connectivity error.");
            }
        });

    }

}
