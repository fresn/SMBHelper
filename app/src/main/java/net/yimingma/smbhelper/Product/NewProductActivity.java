package net.yimingma.smbhelper.Product;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;
import net.yimingma.smbhelper.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;


public class NewProductActivity extends AppCompatActivity {

    RecyclerView productImagesRecyclerView;
    Uri[] images;
    ImageView addImageButton;
    String TAG = "NewProductActivity";
    ProductImagesAdapter productImagesAdapter;
    EditText
            edit_text_new_product_inventory,
            edit_text_new_product_cost,
            edit_text_new_product_tax,
            edit_text_new_product_price,
            edit_text_new_product_title,
            edit_text_new_product_description;
    Button button_new_product_submit;
    SMBHelperBackgroundService.MyBind myBind;

    int CHOOSE_IMAGES = 119;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        Log.d(TAG, "onCreate: ");

        bindService(new Intent(this,SMBHelperBackgroundService.class),serviceConnection, BIND_AUTO_CREATE);


        images = new Uri[0];
        //find views
        button_new_product_submit = findViewById(R.id.button_new_product_submit);
        addImageButton = findViewById(R.id.add_image_button);
        edit_text_new_product_inventory = findViewById(R.id.edit_text_new_product_inventory);
        edit_text_new_product_cost = findViewById(R.id.edit_text_new_product_cost);
        edit_text_new_product_tax = findViewById(R.id.edit_text_new_product_tax);
        edit_text_new_product_price = findViewById(R.id.edit_text_new_product_price);
        edit_text_new_product_title = findViewById(R.id.edit_text_new_product_title);
        edit_text_new_product_description = findViewById(R.id.edit_text_new_product_description);
        //basic input check
        edit_text_new_product_price.addTextChangedListener(new TextWatcher() {
            String beforeChanged;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeChanged = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float localPrice;
                if (s.length() != 0) {
                    try {
                        localPrice = Float.parseFloat(s.toString());
                        if (localPrice < 0) {
                            Log.d(TAG, "onTextChanged: rollback");
                            Utils.rollbackEditTextChangAndShake(edit_text_new_product_price, beforeChanged);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onTextChanged: rollback");
                        Utils.rollbackEditTextChangAndShake(edit_text_new_product_price, beforeChanged);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_text_new_product_cost.addTextChangedListener(new TextWatcher() {
            String beforeChanged;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeChanged = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float localCost;
                if (s.length() != 0) {
                    try {
                        localCost = Float.parseFloat(s.toString());
                        if (localCost < 0) {
                            Utils.rollbackEditTextChangAndShake(edit_text_new_product_cost, beforeChanged);
                        }
                    } catch (Exception e) {
                        Utils.rollbackEditTextChangAndShake(edit_text_new_product_cost, beforeChanged);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_text_new_product_tax.addTextChangedListener(new TextWatcher() {
            String beforeChanged;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeChanged = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int localTax;
                if (s.length() != 0) {
                    try {
                        localTax = Integer.parseInt(s.toString());
                        if (localTax > 30 || localTax < 0) {
                            Utils.rollbackEditTextChangAndShake(edit_text_new_product_tax, beforeChanged);
                        }
                    } catch (Exception e) {
                        Utils.rollbackEditTextChangAndShake(edit_text_new_product_tax, beforeChanged);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_text_new_product_inventory.addTextChangedListener(new TextWatcher() {
            String beforeChanged;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeChanged = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int localInventory;
                if (s.length() != 0) {
                    try {
                        localInventory = Integer.parseInt(s.toString());
                        if (localInventory < 0) {
                            Utils.rollbackEditTextChangAndShake(edit_text_new_product_inventory, beforeChanged);
                        }
                    } catch (Exception e) {
                        Utils.rollbackEditTextChangAndShake(edit_text_new_product_inventory, beforeChanged);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        button_new_product_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myBind.newProduct(new Product(
                            edit_text_new_product_title.getText().toString(),
                            edit_text_new_product_description.getText().toString(),
                            new ArrayList<Uri>(Arrays.asList(images)),
                            Integer.parseInt(edit_text_new_product_tax.getText().toString()),
                            Float.parseFloat(edit_text_new_product_cost.getText().toString()),
                            Float.parseFloat(edit_text_new_product_price.getText().toString()),
                            Integer.parseInt(edit_text_new_product_inventory.getText().toString())
                    )).addOnSuccessListener(new SMBHelperBackgroundService.OnSuccessListener() {
                        @Override
                        public void onSuccess() {
                            unbindService(serviceConnection);
                            finish();
                        }
                    }).addOnFailureListener(new SMBHelperBackgroundService.OnFailureListener() {
                        @Override
                        public void onFailure() {
                            Toast.makeText(NewProductActivity.this,"Failure to create product",Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadImage = new Intent();
                intentLoadImage.setType("image/*");
                intentLoadImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intentLoadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentLoadImage, "Choose Images"), CHOOSE_IMAGES);
            }
        });


        productImagesRecyclerView = findViewById(R.id.product_images_recycler_view);
        productImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productImagesAdapter = new ProductImagesAdapter(images);
        productImagesRecyclerView.setAdapter(productImagesAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        ArrayList<Uri> localImages = new ArrayList<Uri>();
        Log.d(TAG, "onActivityResult: ");
        if (data != null && data.getClipData() != null) {
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                localImages.add(data.getClipData().getItemAt(i).getUri());
                Log.d(TAG, "onActivityResult: " + data.getClipData().getItemAt(i).getUri().toString());
            }
        } else if (data != null && data.getData() != null) {
            localImages.add(data.getData());
            Log.d(TAG, "onActivityResult: " + data.getData().toString());
        }

        this.images = localImages.toArray(new Uri[localImages.size()]);


        productImagesAdapter.updateDate(this.images);

        super.onActivityResult(requestCode, resultCode, data);
    }
}
