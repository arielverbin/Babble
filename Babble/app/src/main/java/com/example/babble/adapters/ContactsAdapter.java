package com.example.babble.adapters;
import android.annotation.SuppressLint;
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

    private static final int VIEW_TYPE_CONTACT = 0;
    private static final int VIEW_TYPE_DELETED_CONTACT = 1;

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
    public int getItemViewType(int position) {
        Contact contact = contacts.get(position);
        return contact.getWasDeleted() ? VIEW_TYPE_DELETED_CONTACT : VIEW_TYPE_CONTACT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if (viewType == VIEW_TYPE_DELETED_CONTACT) {
                convertView = inflater.inflate(R.layout.deleted_contact_layout, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.contact_layout, parent, false);
            }

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
        if(viewType == VIEW_TYPE_DELETED_CONTACT) {
            viewHolder.lastMessageTextView.setText(R.string.this_chat_deleted);

        } else {
            viewHolder.lastMessageTextView.setText(contact.getLastMessage());
        }
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
