package net.yimingma.smbhelper.SMB;

import android.util.Log;

import net.yimingma.smbhelper.util.Generators;


public class Customer {
    private String TAG = "Customer";
    String CusID;
    String firstName, lastName,state,country, street,timestamp;

    String avatarUrl;

    /**
     *
     * @param firstName customer first name String
     * @param lastName customer last name String
     * @param state customer address state String
     * @param country customer address country String
     * @param street customer address street String
     */
    public Customer(String firstName, String lastName, String state ,String country, String street) {
        this.street =street;
        this.country=country;
        this.state=state;
        this.CusID= Generators.getCustomerID();
        this.firstName = firstName;
        Log.d(TAG, "Customer: ");
        this.lastName = lastName;
        timestamp=String.valueOf(System.currentTimeMillis());
    }

    public String getDisplayName() {

        return this.firstName + " " + this.lastName;

    }

}
