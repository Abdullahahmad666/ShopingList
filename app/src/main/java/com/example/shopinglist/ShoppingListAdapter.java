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
                shoppingItemList.clear(); // Clear the existing list

                // Loop through the dataSnapshot to get the items
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                    if (item != null) {
                        item.setUniqueKey(snapshot.getKey()); // Set the unique key from Firebase
                        shoppingItemList.add(item);
                    }
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
        // Get the item to delete from the list
        ShoppingItem itemToDelete = shoppingItemList.get(position);

        // Retrieve the unique key for the item
        String uniqueKey = itemToDelete.getUniqueKey(); // Ensure ShoppingItem has a getUniqueKey() method
        if (uniqueKey == null || uniqueKey.isEmpty()) {
            Log.e("DeleteItem", "Unique key is null or empty.");
            return;
        }

        Log.d("DeleteItem", "Deleting item with unique key: " + uniqueKey);

        // Delete the item from Firebase Realtime Database using the unique key
        databaseReference.child(uniqueKey)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Successfully deleted from Realtime Database

                    // Remove the item from the local list
                    shoppingItemList.remove(position);

                    // Notify the adapter about the changes in a consistent way
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, shoppingItemList.size());

                    Log.d("DeleteItem", "Item deleted successfully.");
                })
                .addOnFailureListener(e -> {
                    // Handle errors if deletion fails
                    Log.e("DeleteItem", "Error deleting item: ", e);
                });
    }

}
