package com.example.shopinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    EditText email, etpassword;
    Button btnSignup;
    TextView tvLogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        // Handle login redirection
        tvLogin.setOnClickListener(v -> {
            Intent i = new Intent(SignUp.this, Login.class);
            startActivity(i);
            finish();
        });

        // Handle sign-up logic
        btnSignup.setOnClickListener(view -> {
            String username = email.getText().toString().trim();
            String password = etpassword.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(SignUp.this, "Email or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            // Sign up with Firebase Auth
            auth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String userID = auth.getCurrentUser().getUid();

                            // Store user data in Firestore
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("email", username);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .document(userID)
                                    .set(data)
                                    .addOnCompleteListener(storeTask -> {
                                        if (storeTask.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUp.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignUp.this, "Firestore error: " + storeTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void init() {
        email = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
        btnSignup = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.backToLogin);
        auth = FirebaseAuth.getInstance();
    }
}
