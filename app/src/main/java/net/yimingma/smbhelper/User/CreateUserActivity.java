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

public class CreateUserActivity extends AppCompatActivity {

    EditText editTextEmail,editTextPassword,editTextConfirm;
    Button buttonSubmit;
    SMBHelperBackgroundService.MyBind myBind;

    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBind=(SMBHelperBackgroundService.MyBind)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBind=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Intent serviceIntent=new Intent(this,SMBHelperBackgroundService.class);
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);

        editTextConfirm=(EditText)  findViewById(R.id.create_user_confirm);
        editTextEmail=(EditText)  findViewById(R.id.create_user_email);
        editTextPassword=(EditText)  findViewById(R.id.create_user_password);
        buttonSubmit=(Button)findViewById(R.id.create_user_submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBind.signUp(editTextEmail.getText().toString(),editTextPassword.getText().toString())
                .addOnSuccessListener(new SMBHelperBackgroundService.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }
                })
                .addOnFailureListener(new SMBHelperBackgroundService.OnFailureListener() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Faile to create user", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
