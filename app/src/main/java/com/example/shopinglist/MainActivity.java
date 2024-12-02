package com.example.shopinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private RecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter adapter;
    FloatingActionButton fabadd;
    Button btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoppingListRecyclerView = findViewById(R.id.recyclerView);
        fabadd = findViewById(R.id.fabAddItem);
        btnlogout = findViewById(R.id.btnLogout);
        fabadd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddNewl.class);
            startActivity(i);
            finish();
        });
        btnlogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            // Redirect to Login Activity after logout
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });


        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        adapter = new ShoppingListAdapter();
        shoppingListRecyclerView.setAdapter(adapter);

        // Fetch data when activity starts
        adapter.fetchItemsFromFirestore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch data again when the activity resumes to ensure updated data
        adapter.fetchItemsFromFirestore();
    }
}
