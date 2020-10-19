package com.project.firebasetutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // FirebaseAuth instance to log/sign in users
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.signIn_email_editText);
        final EditText passwordTEditText = findViewById(R.id.signIn_password_editText);

        Button signInButton = findViewById(R.id.signIn_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve String values
                String email = emailEditText.getText().toString();
                String password = passwordTEditText.getText().toString();

                // Check for empty strings
                if (!email.equals("") && !password.equals("")) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Successful sign in
                                    if (task.isSuccessful()) {
//                                        Toast.makeText(MainActivity.this, "Login Successful",
//                                                Toast.LENGTH_SHORT).show();
                                        // Start next activity and finish current
                                        startActivity(new Intent(MainActivity.this,
                                                MainPageActivity.class));
                                        finish();
                                        // Unsuccessful sign in
                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(MainActivity.this,
                                                "Login Failed " + message,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email or Password cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button signUpButton = findViewById(R.id.signUp_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
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
}