package com.example.babble.api;
import android.content.Context;
import android.widget.Toast;
import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.chats.Message;
import com.example.babble.chats.MessageFromServer;
import com.example.babble.registeration.PostCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {


    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public ChatAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getMessages(Context context, PostCallback callback, int id,
                            String username, String content) {
        Call<List<MessageFromServer>> call = webServiceAPI.getMessages(id,
                "application/json",
                "Bearer " + MyApplication.token,
                content);
        call.enqueue(new Callback<List<MessageFromServer>>() {
            @Override
            public void onResponse(Call<List<MessageFromServer>> call,
                                   Response<List<MessageFromServer>> response) {
                if (response.code() == 200) {
                    //convert from the server message's type to Ariel's.
                    List<MessageFromServer> serverListMessages = response.body();
                    List<Message> chatMessages = new ArrayList<>();

                    for (MessageFromServer message : serverListMessages) {
                        Message chatMessage = new com.example.babble.chats.Message(
                                message.getContent(),
                                message.getId(),
                                message.isSent(username)
                        );
                        chatMessage.setId(message.getId());
                        chatMessages.add(chatMessage);
                    }
                }
                //callback.onSuccess(chatMessages);


            }

            @Override
            public void onFailure(Call<List<MessageFromServer>> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendMessage(Context context, PostCallback callback, int id) {
        Call<Void> call = webServiceAPI.sendMessage(id,
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

