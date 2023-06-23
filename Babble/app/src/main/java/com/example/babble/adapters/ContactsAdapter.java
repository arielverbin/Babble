package com.example.babble.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babble.R;
import com.example.babble.entities.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAdapter {

    private final List<Contact> contacts;

    private static class ViewHolder {
        TextView nameTextView;
        TextView lastMessageTextView;
        TextView timeChattedTextView;
        ImageView profileImageView;
    }

    public ContactsAdapter() {
        this.contacts = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts.clear();
        if (contacts != null) {
            this.contacts.addAll(contacts);
        }
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        String base64ProfilePic = contact.getProfilePicture();
        // remove "data:image/jpg;base64"
        String pureBase64Encoded = base64ProfilePic.substring(base64ProfilePic.indexOf(",")  + 1);
        // decode to bitmap.
        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        viewHolder.profileImageView.setImageBitmap(decodedByte);

        return convertView;
    }

}
