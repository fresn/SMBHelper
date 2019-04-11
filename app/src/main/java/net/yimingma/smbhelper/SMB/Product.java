package net.yimingma.smbhelper.SMB;

import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

public class Product {
    private String title, description;
    private String[] images;
    private float tax, cost, price;
    private int inventory;
    private Variant[] variants;

    public Product(String title, String description, String[] images, float tax, float cost, float price, int inventory) throws Exception {
        this.title = title;
        this.description = description;
        this.images = images;
        setTax(tax);
        setCost(cost);
        setPrice(price);
        this.inventory = inventory;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri[] getImages() {
        ArrayList<Uri> uris = new ArrayList<Uri>();

        for (String image : images) {
            uris.add(Uri.parse(image));
        }

        return (Uri[]) uris.toArray();
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

    public void setImages(String[] images) {
        this.images = images;
    }

    public void addImage(Uri image) {
        ArrayList<String> mImages = new ArrayList<String>(Arrays.asList(this.images));
        mImages.add(image.toString());
        this.images = (String[]) mImages.toArray();
    }

    public void removeImage(int imageIndex) throws Exception {
        ArrayList<String> mImages = new ArrayList<String>(Arrays.asList(this.images));
        if (imageIndex > mImages.size()) {
            throw new Exception("Wrong index");
        }
        mImages.remove(imageIndex);
        this.images=(String[]) mImages.toArray();

    }

    public void setTax(float tax) throws Exception {
        if (tax < 0.4 && tax > 0) {
            this.tax = tax;
        } else {
            throw new Exception("Please confirm your tax rate.");
        }

    }

    public void setCost(float cost) throws Exception {
        if (cost > this.price) {
            throw new Exception("cost can not greater then price");
        }
        this.cost = cost;
    }

    public void setPrice(float price) throws Exception {
        if (price < this.cost) {
            throw new Exception("price can not less then cost");
        }
        this.price = price;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public float getTotal() {
        return price * (1 + tax);
    }

    public int getInventory() {
        return inventory;
    }


}
