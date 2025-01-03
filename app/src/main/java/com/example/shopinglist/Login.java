package com.example.shopinglist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email, etpassword;
    Button btnLogin;
    TextView tvsignup, tvForgetPassword;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        init();

        // Set up click listeners
        tvsignup.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, SignUp.class);
            startActivity(i);
            finish();
        });

        if(user != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(view -> {
            String username = email.getText().toString().trim();
            String password = etpassword.getText().toString();
            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Email or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            ProgressDialog progressDialog = new ProgressDialog(Login.this);
            progressDialog.show();
            auth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener(authResult -> {
                        progressDialog.dismiss();
                        startActivity(new Intent(Login.this, MainActivity.class));  // Correct activity
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
        });

        tvForgetPassword.setOnClickListener(view -> {
            EditText etEmail = new EditText(view.getContext());
            AlertDialog.Builder forgetPasswordDialog = new AlertDialog.Builder(view.getContext())
                    .setTitle("Forget Password Email...")
                    .setView(etEmail)
                    .setPositiveButton("Send", (dialogInterface, i) -> {
                        String email = etEmail.getText().toString().trim();
                        if(TextUtils.isEmpty(email)) {
                            etEmail.setError("Give valid email address");
                        } else {
                            ProgressDialog progressDialog = new ProgressDialog(Login.this);
                            progressDialog.show();
                            auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(Login.this, "Check your inbox...", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    });
                        }
                    })
                    .setNegativeButton("Cancel", null);
            forgetPasswordDialog.show();
        });
    }

    public void init() {
        email = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        tvsignup = findViewById(R.id.signUpText);
        tvForgetPassword = findViewById(R.id.forgotPassword);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}
