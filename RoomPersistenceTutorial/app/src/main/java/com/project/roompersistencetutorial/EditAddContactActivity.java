package com.project.roompersistencetutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditAddContactActivity extends AppCompatActivity {
    public static final String CONTACT_NAME =
            "com.project.roomperssitencetutorial.CONTACT_NAME";
    public static final String CONTACT_DESC =
            "com.project.roomperssitencetutorial.CONTACT_DESC";
    public static final String PHONE_NUMBER =
            "com.project.roomperssitencetutorial.PHONE_NUMBER";
    public static final String CONTACT_ID =
            "com.project.roomperssitencetutorial.CONTACT_ID";

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText phoneNumEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.name_et);
        descriptionEditText = findViewById(R.id.description_et);
        phoneNumEditText = findViewById(R.id.number_et);

        Intent intent = getIntent();
        if (intent.hasExtra(CONTACT_ID)) {
            setTitle("Edit Contact");
            nameEditText.setText(intent.getStringExtra(CONTACT_NAME));
            descriptionEditText.setText(intent.getStringExtra(CONTACT_DESC));
            phoneNumEditText.setText(intent.getStringExtra(PHONE_NUMBER));
        } else {
            setTitle("Add Contact");
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
    }

    public void saveContact() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String phoneNum = phoneNumEditText.getText().toString();

        if (name.trim().isEmpty() || description.trim().isEmpty() || phoneNum.trim().isEmpty()) {
            Toast.makeText(this, "Please Insert Data In All Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(CONTACT_NAME, name);
        data.putExtra(CONTACT_DESC, description);
        data.putExtra(PHONE_NUMBER, phoneNum);

        // Check for valid ID
        int id = getIntent().getIntExtra(CONTACT_ID, -1);
        if (id != -1) {
            data.putExtra(CONTACT_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}