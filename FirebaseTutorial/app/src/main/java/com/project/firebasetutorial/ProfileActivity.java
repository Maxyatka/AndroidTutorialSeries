package com.project.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    // Database reference
    private DatabaseReference mDatabaseReference;
    // FirebaseAuth instance to log/sign in users or get info
    private FirebaseAuth mAuth;
    // Reference to Cloud Storage
    private StorageReference mStorageReference;
    // Instance of current user
    FirebaseUser user;

    // Views
    ImageView profileImageView;

    EditText userNameEditText;
    EditText statusEditText;

    // Code for picking image
    public static final int PICK_IMAGE = 252;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get current user object
        user = mAuth.getCurrentUser();
        // Get database reference that will point to the DB root
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Set reference to the Storage > folder 'profile_images'
        mStorageReference = FirebaseStorage.getInstance().getReference("profile_images");

        // Get all the views
        profileImageView = findViewById(R.id.profileImage);

        userNameEditText = findViewById(R.id.editTextSettingsUserName);
        statusEditText = findViewById(R.id.editTextSettingsStatus);

        Button saveButton = findViewById(R.id.btnSettingsUpdate);

        // Display all necessary data
        readAndDisplayData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update data for current user
                updateData();
                // Finish the activity since we are done here
                finish();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to choose from file system
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                // Create intent to choose from gallery
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // Create chooser intent to select between two options
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                // Start activity chooser to get result from it
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }

    private void readAndDisplayData() {
        // Get current user's email
        String userEmail = user.getEmail();
        // Create a query to get current user's data
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
                        // Get Username, Status, and Profile Image
                        // from snapshot object by referencing child field
                        String userName = (String) snapshotData.child("userName").getValue();
                        String status = (String) snapshotData.child("status").getValue();
                        String profileImage = (String) snapshotData.child("profileImage").getValue();
                        // Setting values to EditTexts
                        userNameEditText.setText(userName);
                        statusEditText.setText(status);
                        // Set profile image
                        Glide.with(getApplicationContext())
                                .load(profileImage)
                                .into(profileImageView);
                    }
                } else {
                    // Here is the case when our query was unsuccessful
                    // and we could not find any results
                    // Set appropriate values in EditTexts
                    userNameEditText.setText("No matching entry!");
                    statusEditText.setText("No matching entry!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // This is the case when some error occurred while querying DB
                // We will set appropriate values in EditTexts
                userNameEditText.setText("Error");
                statusEditText.setText("Error");
            }
        });
    }

    private void updateData() {
        // Get reference to current user object in DB by UID
        DatabaseReference currentUserReference =
                mDatabaseReference.child("users").child(user.getUid());
        // Get values from EditTexts
        String newUserName = userNameEditText.getText().toString();
        String newStatus = statusEditText.getText().toString();
        // Set value to each 'field'
        currentUserReference.child("userName").setValue(newUserName);
        currentUserReference.child("status").setValue(newStatus);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the activity that brings the result is the one we've requested
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Upload image to Firebase Storage
                // Extract Image resource from what
                // the user has selected
                Uri imageUri = data.getData();
                // Set image to the ImageView
                profileImageView.setImageURI(imageUri);
                // Create a file name consisting of user ID + file extension
                String fileName = user.getUid() + ".jpg";
                // Create s reference (spot) for selected image
                final StorageReference fileReference = mStorageReference.child(fileName);
                // Put the file to the reference
                fileReference
                        .putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // In case of success we will display a toast message with text:
                                Toast.makeText(
                                        ProfileActivity.this,
                                        "Image Updated Successfully!",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    // When task is successful we will upload the link to the file
                                    // into our database, we take download url and put it in DB:
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Download uri for an image
                                            String downloadUri = uri.toString();
                                            // Set value in DB
                                            mDatabaseReference
                                                    .child("users")
                                                    .child(user.getUid())
                                                    .child("profileImage")
                                                    .setValue(downloadUri);
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Could Not Select the Image", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You Have Not Selected an Image", Toast.LENGTH_LONG).show();
        }
    }
}