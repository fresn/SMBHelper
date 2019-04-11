package net.yimingma.smbhelper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.yimingma.smbhelper.SMB.Product;


public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ProductImageHolder> {

    private Uri[] images;
    private String TAG = "ProductImagesAdapter";

    public ProductImagesAdapter(Uri[] images) {
        Log.d(TAG, "ProductImagesAdapter: ");
        this.images = images;
    }

    public void updateDate(Uri[] images){
        this.images=images;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductImagesAdapter.ProductImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        return new ProductImageHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.product_image_holder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageHolder productImageHolder, int i) {

        Log.d(TAG, "onBindViewHolder: " + String.valueOf(i));

        Picasso.get().load(images[i]).into(productImageHolder.mImageView);


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+ images.length);
        return images.length ;
    }


    public static class ProductImageHolder extends RecyclerView.ViewHolder {

        private final static String TAG = "ProductImageHolder";
        public ImageView mImageView;

        public ProductImageHolder(@NonNull View itemView) {

            super(itemView);
            Log.d(TAG, "ProductImageHolder: ");
            mImageView = (ImageView) itemView.findViewById(R.id.product_image);
        }


    }
}
