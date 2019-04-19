package net.yimingma.smbhelper.Product;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;

public class ProductManageActivity extends AppCompatActivity {


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

    SMBHelperBackgroundService.MyBind myBind;
    ImageView image_view_add_icon;
    RecyclerView recycler_view_product_manage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        bindService(new Intent(this, SMBHelperBackgroundService.class), serviceConnection, BIND_AUTO_CREATE);
        image_view_add_icon = findViewById(R.id.image_view_add_icon);
        recycler_view_product_manage = findViewById(R.id.recycler_view_product_manage);

        image_view_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductManageActivity.this, NewProductActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        recycler_view_product_manage.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        if(myBind!=null){
            myBind.requireProducts()
                    .addOnSuccessListener(
                            new SMBHelperBackgroundService.OnTypeSuccessListener<Product[]>() {
                                @Override
                                public void onSuccess(Product[] data) {
                                    recycler_view_product_manage.setAdapter(new ProductsAdapter(data));
                                }
                            }
                    ).addOnFailureListener(
                    new SMBHelperBackgroundService.OnFailureListener() {
                        @Override
                        public void onFailure() {
                            Toast.makeText(ProductManageActivity.this,"fail to get products",Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
