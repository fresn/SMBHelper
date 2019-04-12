package net.yimingma.smbhelper.Customer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;

public class CustomerManageActivity extends AppCompatActivity {


    ImageView imageView_customer_add_icon;
    SMBHelperBackgroundService.MyBind myBind;
    RecyclerView recycler_view_customer_manage;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBind = (SMBHelperBackgroundService.MyBind) service;
            onStart();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBind = null;
        }
    };

    private final int NEW_CUSTOMER = 121;
    private String TAG = "CustomerManageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage);

        bindService(new Intent(this, SMBHelperBackgroundService.class), serviceConnection, BIND_AUTO_CREATE);


        imageView_customer_add_icon = (ImageView) findViewById(R.id.imageView_customer_add_icon);
        recycler_view_customer_manage= (RecyclerView) findViewById(R.id.recycler_view_customer_manage);

        imageView_customer_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCustomerIntent = new Intent(CustomerManageActivity.this, NewCustomerActivity.class);
                startActivityForResult(newCustomerIntent, NEW_CUSTOMER);
            }
        });
        recycler_view_customer_manage.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        if (myBind != null) {

            myBind.requireCustomers().addOnSuccessListener(new SMBHelperBackgroundService.OnTypeSuccessListener<Customer[]>() {
                @Override
                public void onSuccess(Customer[] data) {
                    for (Customer customer : data) {
                        Log.d(TAG, "onSuccess: " + customer.getDisplayName());
                    }
                    recycler_view_customer_manage.setAdapter(new CustomersAdapter(data));
                }
            });

        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        unbindService(serviceConnection);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
