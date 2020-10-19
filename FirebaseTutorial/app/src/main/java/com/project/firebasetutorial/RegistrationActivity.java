package com.project.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    // FirebaseAuth instance to log/sign in users
    private FirebaseAuth mAuth;

    // Link to default profile image
    private static final String defaultProfileImage =
            "https://firebasestorage.googleapis.com/v0/b/fir-tutorial-b5efa.appspot.com/o/Default-Profile.jpg?alt=media&token=e81e2d3b-da2e-4800-a24c-48f01b190720";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.registration_email_editText);
        final EditText passwordEditText = findViewById(R.id.registration_password_editTex);
        final EditText confirmPasswordEditText = findViewById(R.id.registration_confirm_password_editTex);

        Button registerButton = findViewById(R.id.registration_createNewAccount_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If passwords match we retrieve strings and continue process
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (password.equals(confirmPassword)) {
                    // Check for all strings to be not empty
                    if (!email.equals("") && !password.equals("")) {
                        // Create user with email and password
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Create a DB entry for a newly registered user
                                            addUserToDB(email);
                                            // Start next activity and finish current
                                            startActivity(new Intent(
                                                    RegistrationActivity.this,
                                                    MainPageActivity.class));
                                            // We finish this activity, since we don't want users
                                            // to return to this activity after successful
                                            // registration process
                                            finish();
                                        } else {
                                            Toast.makeText(RegistrationActivity.this,
                                                    "Sign Up Failed: " +
                                                            Objects.requireNonNull(task
                                                                    .getException()
                                                                    .getMessage()), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Email and Password Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Passwords Do Not Match!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        }
    }

    private void addUserToDB(String email) {
        // Get database reference to the 'users' node
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Get ID from Firebase Auth
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // If there is no Firebase Auth, generate new UUID
        final String id = userID != null ? userID : UUID.randomUUID().toString();
        // Create a new User object
        User newUser = new User(id, email, email, "default status", defaultProfileImage);
        // Create a user entry in DB
        mDatabase.child(id).setValue(newUser);
    }
}