package com.example.shopinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingItemViewHolder> {
    private List<ShoppingItem> shoppingItemList;
    private FirebaseFirestore firestore;

    public ShoppingListAdapter() {
        this.shoppingItemList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
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
            // Delete item from Firestore
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

    // Refactor data fetching to a method
    public void fetchItemsFromFirestore() {
        firestore.collection("ShoppingItems")
                .orderBy("itemName")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        shoppingItemList.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            ShoppingItem item = snapshot.toObject(ShoppingItem.class);
                            shoppingItemList.add(item);
                        }
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                });
    }

    // Method to delete item
    private void deleteItem(int position) {
        ShoppingItem itemToDelete = shoppingItemList.get(position);
        firestore.collection("ShoppingItems")
                .document(itemToDelete.getItemName())  // Assuming itemName is unique
                .delete()
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



