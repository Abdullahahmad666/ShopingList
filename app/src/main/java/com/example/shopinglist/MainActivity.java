package com.example.shopinglist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoppingListRecyclerView = findViewById(R.id.recyclerView);
        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        adapter = new ShoppingListAdapter();
        shoppingListRecyclerView.setAdapter(adapter);
    }
}
