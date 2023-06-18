package com.example.babble.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babble.R;

import java.util.List;

public class ContactsAdapter extends BaseAdapter {

    private final List<Contact> contacts;

    private static class ViewHolder {
        TextView nameTextView;
        TextView lastMessageTextView;
        TextView timeChattedTextView;
        ImageView profileImageView;
    }

    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contacts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.contactName);
            viewHolder.lastMessageTextView = convertView.findViewById(R.id.lastMessage);
            viewHolder.timeChattedTextView = convertView.findViewById(R.id.timeChatted);
            viewHolder.profileImageView = convertView.findViewById(R.id.profileImage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = contacts.get(position);
        String name = contact.getDisplayName();
        viewHolder.nameTextView.setText(name);
        viewHolder.lastMessageTextView.setText(contact.getLastMessage());
        viewHolder.timeChattedTextView.setText(contact.getTimeChatted());
        //viewHolder.profileImageView.setImageBitmap(contact.getProfilePicture());

        return convertView;
    }

}
