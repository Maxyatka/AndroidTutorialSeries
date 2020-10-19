package com.project.roompersistencetutorial;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String contactName;
    @ColumnInfo(name = "description")
    private String contactDescription;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    public Contact(String contactName, String contactDescription, String phoneNumber) {
        this.contactName = contactName;
        this.contactDescription = contactDescription;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }
}
