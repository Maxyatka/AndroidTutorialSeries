package com.project.roompersistencetutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<Contact> contacts = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.contactNameTV.setText(contact.getContactName());
        holder.contactDescTV.setText(contact.getContactDescription());
        holder.phoneNumTV.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public Contact getContactAt(int position) {
        return contacts.get(position);
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        private TextView contactNameTV;
        private TextView contactDescTV;
        private TextView phoneNumTV;

        public ContactHolder(View itemView) {
            super(itemView);

            contactNameTV = itemView.findViewById(R.id.contact_title_tv);
            contactDescTV = itemView.findViewById(R.id.contact_desc_tv);
            phoneNumTV = itemView.findViewById(R.id.contact_phone_num_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(contacts.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public void setOnItemClickedListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
