package com.project.roompersistencetutorial;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository contactRepository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        allContacts = contactRepository.getAllContacts();
    }

    public void insertContact(Contact contact) {
        contactRepository.insert(contact);
    }

    public void updateContact(Contact contact) {
        contactRepository.update(contact);
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}
