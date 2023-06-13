package com.example.babble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactsAdapter extends BaseAdapter {

    List<Contact> contacts;

    private class ViewHolder {
        TextView name;
        TextView lastMes;
        TextView timeChatted;
        ImageView profilePic;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.contactName);
            viewHolder.lastMes = convertView.findViewById(R.id.lastMessage);
            viewHolder.timeChatted = convertView.findViewById(R.id.timeChatted);
            viewHolder.profilePic = convertView.findViewById(R.id.profileImage);

            convertView.setTag(viewHolder);
        }

        Contact c = contacts.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(c.getDisplayName());
        viewHolder.lastMes.setText(c.getLastMessage());
        viewHolder.timeChatted.setText(c.getTimeChatted());
        viewHolder.profilePic.setImageBitmap(c.getProfilePicture());

        return convertView;
    }

}