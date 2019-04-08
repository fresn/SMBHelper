package net.yimingma.smbhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CustomerManageActivity extends AppCompatActivity {

    Button buttonNewCustomer,buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage);

        buttonNewCustomer=findViewById(R.id.button_new_customer);
        buttonLogin=(Button) buttonLogin.findViewById(R.id.dashboard_guide_login);

    }

    @Override
    protected void onStart() {

        buttonNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewCustomer=new Intent(getApplicationContext(),NewCustomerActivity.class);
                startActivityForResult(intentNewCustomer,0);
            }
        });

        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
    }
}
