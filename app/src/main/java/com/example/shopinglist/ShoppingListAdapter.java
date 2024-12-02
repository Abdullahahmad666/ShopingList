package com.example.shopinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingItem, ShoppingListAdapter.ShoppingItemViewHolder> {

    // Constructor
    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position, @NonNull ShoppingItem model) {
        // Bind data to the ViewHolder
        holder.itemName.setText("Item: " + model.getItemName());
        holder.quantity.setText("Quantity: " + model.getQuantity());
        holder.price.setText("Price: $" + model.getPrice());

        // Handle delete button
        holder.deleteButton.setOnClickListener(view -> {
            // Remove item from Firestore
            FirebaseFirestore.getInstance().collection("ShoppingItems")
                    .document(getSnapshots().getSnapshot(position).getId())
                    .delete();
        });
    }

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingItemViewHolder(view);
    }

    // ViewHolder class
    static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
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
}

