package net.yimingma.smbhelper.User;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button submit;
    SMBHelperBackgroundService.MyBind serviceMyBind;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMyBind = (SMBHelperBackgroundService.MyBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMyBind = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent serviceIntent = new Intent(this, SMBHelperBackgroundService.class);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);


        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        submit = (Button) findViewById(R.id.login_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceMyBind.login(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new SMBHelperBackgroundService.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }
                }).addOnFailureListener(
                        new SMBHelperBackgroundService.OnFailureListener() {
                            @Override
                            public void onFailure() {
                                Toast.makeText(getApplicationContext(), "Email or Password wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        });
    }
}
