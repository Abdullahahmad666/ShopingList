package com.example.shopinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

import kotlinx.coroutines.MainCoroutineDispatcher;

public class AddNewl extends AppCompatActivity {

    private EditText itemNameInput, quantityInput, priceInput;
    private Button addItemButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingItems");

        itemNameInput = findViewById(R.id.itemNameInput);
        quantityInput = findViewById(R.id.quantityInput);
        priceInput = findViewById(R.id.priceInput);
        addItemButton = findViewById(R.id.addItemButton);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewItem();
                ProgressDialog progressDialog = new ProgressDialog(AddNewl.this);
                progressDialog.show();
                Intent i = new Intent(AddNewl.this, MainActivity.class);
                startActivity(i);
                finish();
                progressDialog.dismiss();
            }
        });
    }

    private void addNewItem() {
        String itemName = itemNameInput.getText().toString();
        String quantity = quantityInput.getText().toString();
        String price = priceInput.getText().toString();

        if (itemName.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create unique ID for item
        String itemId = databaseReference.push().getKey();

        // Create item data
        HashMap<String, Object> itemData = new HashMap<>();
        itemData.put("itemName", itemName);
        itemData.put("quantity", Integer.parseInt(quantity));
        itemData.put("price", Double.parseDouble(price));

        // Save item to Realtime Database
        databaseReference.child(itemId).setValue(itemData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

