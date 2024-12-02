package com.example.shopinglist;
public class ShoppingItem
{
    private String uniqueKey; // The Firebase unique key
    private String itemName;
    private int price;
    private int quantity;

    public ShoppingItem() {}

    // Constructor, getters, and setters
    public ShoppingItem(String uniqueKey, String itemName, int price, int quantity) {
        this.uniqueKey = uniqueKey;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
