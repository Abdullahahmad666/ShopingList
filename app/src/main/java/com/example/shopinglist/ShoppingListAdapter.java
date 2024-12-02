package com.example.shopinglist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingItemViewHolder> {
    private List<ShoppingItem> shoppingItemList;
    private DatabaseReference databaseReference;

    public ShoppingListAdapter() {
        this.shoppingItemList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingItems");
    }

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position) {
        ShoppingItem item = shoppingItemList.get(position);
        holder.itemName.setText("Item: " + item.getItemName());
        holder.quantity.setText("Quantity: " + item.getQuantity());
        holder.price.setText("Price: $" + item.getPrice());

        // Delete button
        holder.deleteButton.setOnClickListener(view -> {
            // Delete item from Realtime Database
            deleteItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }

    public class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, quantity, price;
        Button deleteButton;

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTextView);
            quantity = itemView.findViewById(R.id.quantityTextView);
            price = itemView.findViewById(R.id.priceTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Fetch data from Realtime Database
    public void fetchItemsFromRealtimeDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoppingItemList.clear();  // Clear the existing list

                // Loop through the dataSnapshot to get the items
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                    shoppingItemList.add(item);
                }

                // Notify the adapter about the data change
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Realtime DB", "Error fetching data: ", databaseError.toException());
            }
        });
    }

    // Method to delete item from Realtime Database
    private void deleteItem(int position) {
        ShoppingItem itemToDelete = shoppingItemList.get(position);
        databaseReference.child(itemToDelete.getItemName())  // Assuming itemName is unique and used as key
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    shoppingItemList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, shoppingItemList.size());
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                });
    }
}
