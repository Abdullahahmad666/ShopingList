package com.example.shopinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter adapter;
    FloatingActionButton fabadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoppingListRecyclerView = findViewById(R.id.recyclerView);
        fabadd = findViewById(R.id.fabAddItem);
        fabadd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddNewl.class);
            startActivity(i);
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
