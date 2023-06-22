package com.example.babble.api;
import android.content.Context;
import android.widget.Toast;
import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.contacts.Contact;
import com.example.babble.contacts.ContactAfterAdd;
import com.example.babble.contacts.ContactFromServer;
import com.example.babble.registeration.PostCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public ContactsAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getContacts(Context context, PostCallback callback) {
        Call<List<ContactFromServer>> call = webServiceAPI.getContacts("application/json",
                "Bearer " + MyApplication.token);
        call.enqueue(new Callback<List<ContactFromServer>>() {
            @Override
            public void onResponse(Call<List<ContactFromServer>> call,
                                   Response<List<ContactFromServer>> response) {
                if (response.code() == 200){
                    List<ContactFromServer> serverContacts = response.body();
                    //the list for Ariel's methods
                    List<Contact> contacts = new ArrayList<>();
                    for (ContactFromServer serverContact : serverContacts) {
                        Contact contact = new Contact(serverContact);
                        contacts.add(contact);
                    }
                }
                //callback.onSuccess(contacts);


            }

            @Override
            public void onFailure(Call<List<ContactFromServer>> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addContact(Context context, PostCallback callback, String username) {
        Call<ContactAfterAdd> call = webServiceAPI.addContact("application/json",
                "Bearer " + MyApplication.token, username);
        call.enqueue(new Callback<ContactAfterAdd>() {
            @Override
            public void onResponse(Call<ContactAfterAdd> call,
                                   Response<ContactAfterAdd> response) {
                if (response.code() == 200){
                    ContactAfterAdd contactAfterAdd = response.body();

                    // Get the current time in the desired format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    String currentTime = dateFormat.format(new Date());

                    //contact for Ariel's methods
                    Contact contact = new Contact(contactAfterAdd.getUser().getUsername(),
                            contactAfterAdd.getUser().getDisplayName(),
                            contactAfterAdd.getUser().getProfilePic(),
                            "This conversation is new.", currentTime);

                }
                //callback.onSuccess(contact);


            }

            @Override
            public void onFailure(Call<ContactAfterAdd> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteContact(Context context, PostCallback callback, int id){
        Call<Void> call = webServiceAPI.deleteContact(id,
                "application/json",
                "Bearer " + MyApplication.token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
                if (response.code() == 200) {
                    //callback.onSuccess();
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
