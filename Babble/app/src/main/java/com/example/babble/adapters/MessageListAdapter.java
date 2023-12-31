package com.example.babble.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babble.R;
import com.example.babble.entities.Message;

import java.util.LinkedList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private final List<Message> messageList;
    private final LayoutInflater inflater;

    public MessageListAdapter(Context context) {
        messageList = new LinkedList<>();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = inflater.inflate(R.layout.recieved_message_layout, parent, false);
        } else if (viewType == 1) {
            itemView = inflater.inflate(R.layout.sent_message_layout, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.water_mark_layout, parent, false);
        }
        return new MessageViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.isWatermark()) {
            return 2;
        }
        return message.isSent() ? 1 : 0;
    }

    public void addMessage(Message message) {
        if (getItemCount() == 1) {
            messageList.get(0).setMsg("No previous messages.");
            messageList.add(message);
        } else {
            Message lastMessage = messageList.get(messageList.size() - 1);
            if(!lastMessage.getDaySent().equals(message.getDaySent())) {
                messageList.add(new Message(message.getDaySent(),
                        message.getChatId(), "", "", true).convertToWatermark());
                messageList.add(new Message(message.getMsg() + "ahahahha", message.getChatId(), message.getCreated(), message.getTimeSentExtended(), message.isSent()));
            }
        }
    }


    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTextView;
        private final TextView timestampTextView;
        private final TextView waterMarkTextView;

        MessageViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == 2) {
                waterMarkTextView = itemView.findViewById(R.id.waterMark);
                messageTextView = null;
                timestampTextView = null;
            } else if (viewType == 1) {
                waterMarkTextView = null;
                messageTextView = itemView.findViewById(R.id.messageTextView);
                timestampTextView = itemView.findViewById(R.id.text_gchat_timestamp_me);
            } else {
                waterMarkTextView = null;
                messageTextView = itemView.findViewById(R.id.messageTextView);
                timestampTextView = itemView.findViewById(R.id.text_gchat_timestamp_other);
            }
        }

        void bind(Message message) {
            if (waterMarkTextView != null) {
                waterMarkTextView.setText(message.getMsg());
                return;
            }
            messageTextView.setText(message.getMsg());
            timestampTextView.setText(message.getCreated());

        }
    }

    public void insertLastMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void setMessages(List<Message> messages) {
        messageList.clear();
        messageList.addAll(messages);
        notifyDataSetChanged();
    }
}
