package com.example.shopinglist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private RecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter adapter;
    private FloatingActionButton fabAdd;
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        // Check if the user is authenticated
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            redirectToLogin();
        }

        // Handle adding a new item
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNewl.class);
            startActivity(intent);
        });

        // Handle logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            redirectToLogin();
        });

        // Set up RecyclerView
        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter();
        shoppingListRecyclerView.setAdapter(adapter);

        // Fetch data from the database
        adapter.fetchItemsFromRealtimeDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        if (adapter != null) {
            adapter.fetchItemsFromRealtimeDatabase();
        }
    }

    private void initViews() {
        shoppingListRecyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAddItem);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
