package com.project.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainPageActivity extends AppCompatActivity {

    // FirebaseAuth instance to log/sign in users or get info
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Declare Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get current user object
        final FirebaseUser user = mAuth.getCurrentUser();
        // Get database reference that will point to the DB root
        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Button readFromDbButton = findViewById(R.id.db_read_button);
        readFromDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user email from user object
                String userEmail = user.getEmail();
                // Create a query for Firebase Realtime DB based on email
                Query queryUser =
                        mDatabaseReference.child("users").orderByChild("email").equalTo(userEmail);

                // Execute query and get the result back
                queryUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Check if snapshot that is returned to us exists
                        if (snapshot.exists()) {
                            // Snapshot is a collection of objects returned to us,
                            // you can assume that it's sort of a cursor in some DBMSs
                            // We have to iterate through it and get our single result
                            for (DataSnapshot snapshotData : snapshot.getChildren()) {
                                // Here is our data, we will display a Toast with data
                                // Notice that snapshotData is our desired object from the DB
                                Toast.makeText(
                                        MainPageActivity.this,
                                        "Your Email is: " + snapshotData.child("email")
                                                .getValue() + "\n\n" +
                                                "Your ID is: " + snapshotData.child("id")
                                                .getValue(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } else {
                            // Here is the case when our query was unsuccessful
                            // and we could not find any results
                            // Display a Toast message indicating it
                            Toast.makeText(MainPageActivity.this,
                                    "Could not find desired entry",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // This is the case when some error occurred while querying DB
                        // We will display an error message here
                        Toast.makeText(
                                MainPageActivity.this,
                                error.toException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        });

        Button openProfileButton = findViewById(R.id.profileButton);
        openProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPageActivity.this, ProfileActivity.class));
            }
        });

        Button signOutButton = findViewById(R.id.logout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we sign out the user
                FirebaseAuth.getInstance().signOut();
                // Start log in activity
                startActivity(new Intent(MainPageActivity.this, MainActivity.class));
                // We finish current activity, since we don't want user to return here
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}