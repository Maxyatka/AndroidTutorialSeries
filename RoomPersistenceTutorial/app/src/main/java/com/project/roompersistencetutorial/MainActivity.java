package com.project.roompersistencetutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    public static final int ADD_CONTACT_REQUEST = 252;
    public static final int EDIT_CONTACT_REQUEST = 525;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditAddContactActivity.class);
                startActivityForResult(intent, ADD_CONTACT_REQUEST);
            }
        });

        contactViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication())).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                contactViewModel.deleteContact(adapter.getContactAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Contact Removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickedListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                Intent intent = new Intent(MainActivity.this, EditAddContactActivity.class);
                intent.putExtra(EditAddContactActivity.CONTACT_ID, contact.getId());
                intent.putExtra(EditAddContactActivity.CONTACT_NAME, contact.getContactName());
                intent.putExtra(EditAddContactActivity.CONTACT_DESC, contact.getContactDescription());
                intent.putExtra(EditAddContactActivity.PHONE_NUMBER, contact.getPhoneNumber());
                startActivityForResult(intent, EDIT_CONTACT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CONTACT_REQUEST:
                    String name = data.getStringExtra(EditAddContactActivity.CONTACT_NAME);
                    String description = data.getStringExtra(EditAddContactActivity.CONTACT_DESC);
                    String number = data.getStringExtra(EditAddContactActivity.PHONE_NUMBER);

                    // Create contact from returned values
                    Contact newContact = new Contact(name, description, number);
                    // Insert new contact
                    contactViewModel.insertContact(newContact);
                    Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
                    break;

                case EDIT_CONTACT_REQUEST:
                    int id = data.getIntExtra(EditAddContactActivity.CONTACT_ID, -1);

                    if (id == -1) {
                        Toast.makeText(this, "Cannot Update Contact", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String contactName = data.getStringExtra(EditAddContactActivity.CONTACT_NAME);
                    String contactDescription = data.getStringExtra(EditAddContactActivity.CONTACT_DESC);
                    String phoneNumber = data.getStringExtra(EditAddContactActivity.PHONE_NUMBER);

                    Contact editedContact = new Contact(contactName, contactDescription, phoneNumber);
                    editedContact.setId(id);

                    contactViewModel.updateContact(editedContact);

                    Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(this, "Contact Not Saved", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}