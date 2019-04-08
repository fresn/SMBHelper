package net.yimingma.smbhelper;

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

import net.yimingma.smbhelper.SMB.Customer;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;


public class NewCustomerActivity extends AppCompatActivity {

    EditText firstName, lastName, state, country, street;
    Button buttonSubmit;
    SMBHelperBackgroundService.MyBind myBind;
    private String TAG="NewCustomerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);


        firstName = (EditText) findViewById(R.id.newCustomerFirstName);
        lastName = (EditText) findViewById(R.id.newCustomerLastName);
        state = (EditText) findViewById(R.id.newCustomerState);
        country = (EditText) findViewById(R.id.newCustomerCountry);
        street = (EditText) findViewById(R.id.newCustomerAddress);
        buttonSubmit = findViewById(R.id.newCustomerButtonSubmit);

        Intent serviceIntent = new Intent(this, SMBHelperBackgroundService.class);
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBind.newCustomer(
                        new Customer(
                                firstName.getText().toString(),
                                lastName.getText().toString(),
                                state.getText().toString(),
                                country.getText().toString(),
                                street.getText().toString()
                        )
                );
            }
        });
    }

    void setResult(boolean result) {
        setResult(result);
        Intent serviceIntent = new Intent(this, SMBHelperBackgroundService.class);
        unbindService(mConnection);
        finish();
    }


    ServiceConnection mConnection = new ServiceConnection() {
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
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }
}
