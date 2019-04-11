package net.yimingma.smbhelper;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewProductActivity extends AppCompatActivity {

    RecyclerView productImagesRecyclerView;
    Uri[] images;
    ImageView addImageButton;
    String TAG = "NewProductActivity";
    ProductImagesAdapter productImagesAdapter;

    int CHOOSE_IMAGES = 119;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        Log.d(TAG, "onCreate: ");
        addImageButton = (ImageView) findViewById(R.id.add_image_button);


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
        images = new Uri[0];

        productImagesRecyclerView = (RecyclerView) findViewById(R.id.product_images_recycler_view);

        productImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productImagesAdapter=new ProductImagesAdapter(images);
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

        this.images = (Uri[]) localImages.toArray(new Uri[localImages.size()]);

        productImagesAdapter.notifyDataSetChanged();

        super.onActivityResult(requestCode, resultCode, data);
    }
}
