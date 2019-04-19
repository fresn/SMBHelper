package net.yimingma.smbhelper.Order;


import net.yimingma.smbhelper.Customer.Customer;
import net.yimingma.smbhelper.Product.Product;
import net.yimingma.smbhelper.util.Generators;

import java.sql.Date;
import java.util.ArrayList;

public class Order {
    public ArrayList<Item> itemList = new ArrayList<>();
    public Customer customer;
    public String note;
    public boolean isDelivery;
    public String OrderID;
    public STATUS status;
    public Date createDate;
    public Date deuDate;
    public boolean isClosed;


    Order() {

    }

    public void setID() {
        this.OrderID=Generators.getID();
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void clearItems() {
        this.itemList = new ArrayList<>();
    }

    float getTotalPrice() {
        float price = 0;
        for (Item item : itemList) {
            price += item.product.getPrice() * item.count;
        }
        return price;
    }

    static class Item {
        public Product product;
        public int count;
        public float discount;
        public String note;

        public Item() {

        }

        public Item(Product product, int count, float discount, String note) {
            this.product = product;
            this.count = count;
            this.discount = discount;
            this.note = note;
        }
    }

    public enum STATUS {
        GOING,
        CLOSED,
        All
    }
}
