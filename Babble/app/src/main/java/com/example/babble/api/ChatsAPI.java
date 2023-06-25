package com.example.babble.API;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.entities.Message;
import com.example.babble.entities.MessageDao;
import com.example.babble.serverObjects.ServerMessage;
import com.example.babble.services.WebServiceAPI;
import com.example.babble.utilities.RequestCallBack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatsAPI {


    private final WebServiceAPI webServiceAPI;

    private final String token;

    private final MessageDao messageDao;


    public ChatsAPI(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        messageDao = db.messageDao();

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

    public void getMessages(Context context, String chatId, String username,
                            RequestCallBack callback) {
        Call<List<ServerMessage>> call = webServiceAPI.getMessages(chatId,
                "application/json",
                "Bearer " + this.token);

        call.enqueue(new Callback<List<ServerMessage>>() {
            @Override
            public void onResponse(@NonNull Call<List<ServerMessage>> call,
                                   @NonNull Response<List<ServerMessage>> response) {
                if (response.code() == 200) {

                    List<ServerMessage> serverListMessages = response.body();
                    List<Message> chatMessages = new ArrayList<>();

                    if (serverListMessages != null) {
                        for (ServerMessage message : serverListMessages) {
                            chatMessages.add(0, message.convertToMessage(username, chatId));
                        }
                        // success! update dao and notify caller with "success"
                        messageDao.clearChat(chatId); // no need to clear messages from other chats.
                        messageDao.insertAll(chatMessages);
                        callback.onSuccess();

                    }
                } else if(response.code() == 404) {
                    callback.onFailure("Chat was deleted.");
                } else {
                    callback.onFailure("Error fetching messages from server (code: "
                            + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServerMessage>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callback.onFailure(
                        "Error fetching messages from server: server connectivity error.");
            }
        });
    }

    public void sendMessage(Context context, String chatId, Message message,
                            RequestCallBack callback) {
        Call<Void> call = webServiceAPI.sendMessage(chatId,
                "application/json",
                "Bearer " + this.token, message);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.code() == 200) {
                    messageDao.insert(message);
                    callback.onSuccess();
                } else if (response.code() == 404) {
                    callback.onFailure("This chat was deleted.");
                } else {
                    callback.onFailure("Could not send message (code: "
                            + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                callback.onFailure("Could not send message: server connectivity error");
            }
        });
    }
}

