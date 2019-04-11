package net.yimingma.smbhelper.SMB;

import android.net.Uri;
import android.util.Log;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private final String TAG = "Product";
    private String title, description;
    List<String> mIngUrl = new ArrayList<>();
    private float  cost, price;
    private int inventory,tax;
    private Variant[] variants;

    public Product(String title, String description, ArrayList<Uri> mImages, int tax, float cost, float price, int inventory) throws Exception {
        Log.d(TAG, "Product: ");
        this.title = title;
        this.description = description;


        if (mImages.size() > 0) {
            for (int i = 0; i < mImages.size(); i++) {
                this.mIngUrl.add(mImages.get(i).toString());
            }
        }


        this.tax=tax;
        setCostPrice(cost, price);

        this.inventory = inventory;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public float getCost() {
        return cost;
    }

    public float getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public float getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax=tax;
    }

    public void setCostPrice(float cost, float price) throws Exception {
        if (cost > price) {
            throw new Exception("cost can not greater then price cost : " + cost + " price : " + this.price);
        }
        this.cost = cost;
        this.price = price;
    }


    public void setInventory(int inventory) {
        this.inventory = inventory;
    }


    public int getInventory() {
        return inventory;
    }


}
