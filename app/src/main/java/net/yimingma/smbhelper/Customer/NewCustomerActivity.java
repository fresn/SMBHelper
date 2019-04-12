package net.yimingma.smbhelper.Customer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;


public class NewCustomerActivity extends AppCompatActivity {

    EditText edit_text_new_customer_first_name,
            edit_text_new_customer_last_name,
            edit_text_new_customer_state,
            edit_text_new_customer_country,
            edit_text_new_customer_street;
    Button button_create_customer;

    ImageView image_view_new_customer_back_icon;

    SMBHelperBackgroundService.MyBind myBind;
    private String TAG = "NewCustomerActivity";


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBind = (SMBHelperBackgroundService.MyBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBind = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        bindService(new Intent(this, SMBHelperBackgroundService.class), serviceConnection, BIND_AUTO_CREATE);


        edit_text_new_customer_first_name = (EditText) findViewById(R.id.edit_text_new_customer_first_name);
        edit_text_new_customer_last_name = (EditText) findViewById(R.id.edit_text_new_customer_last_name);
        edit_text_new_customer_state = (EditText) findViewById(R.id.edit_text_new_customer_state);
        edit_text_new_customer_country = (EditText) findViewById(R.id.edit_text_new_customer_country);
        edit_text_new_customer_street = (EditText) findViewById(R.id.edit_text_new_customer_street);
        button_create_customer = (Button) findViewById(R.id.button_new_customer_submit);
        image_view_new_customer_back_icon = (ImageView) findViewById(R.id.image_view_new_customer_back_icon);


        image_view_new_customer_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        button_create_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "button_create_customer onClick: ");
                myBind
                        .newCustomer(
                                new Customer(
                                        edit_text_new_customer_first_name.getText().toString(),
                                        edit_text_new_customer_last_name.getText().toString(),
                                        edit_text_new_customer_state.getText().toString(),
                                        edit_text_new_customer_country.getText().toString(),
                                        edit_text_new_customer_street.getText().toString()
                                )
                        )
                        .addOnSuccessListener(new SMBHelperBackgroundService.OnSuccessListener() {
                            @Override
                            public void onSuccess() {

                            }
                        })
                        .addOnFailureListener(new SMBHelperBackgroundService.OnFailureListener() {
                            @Override
                            public void onFailure() {
                                Log.d(TAG, "onFailure: ");
                                Toast.makeText(NewCustomerActivity.this,"fail to create customer",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }
}
