package com.project.roompersistencetutorial;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    // This is how we make it singleton
    private static ContactDatabase instance;

    public abstract ContactDao contactDao();

    // And here also
    public static synchronized ContactDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contact_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(initCallback)
                    .build();
        }
        return instance;
    }

    // Create a callback to populate database with sample entries
    // when it is created (requires async task)
    private static RoomDatabase.Callback initCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // Async task to create sample entries in DB
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactDao contactDao;

        private PopulateDbAsyncTask(ContactDatabase db) {
            contactDao = db.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.insert(
                    new Contact(
                            "Contact #1",
                            "First Contact",
                            "+1(111)111-11-11")
            );
            contactDao.insert(
                    new Contact(
                            "Contact #2",
                            "Second Contact",
                            "+2(222)222-22-22")
            );
            contactDao.insert(
                    new Contact(
                            "Contact #3",
                            "Third Contact",
                            "+3(333)333-33-33")
            );
            return null;
        }
    }
}
