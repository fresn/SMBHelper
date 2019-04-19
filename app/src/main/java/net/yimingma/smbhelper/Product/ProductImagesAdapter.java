package net.yimingma.smbhelper.Product;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.yimingma.smbhelper.R;


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
    public ProductImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        return new ProductImageHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.holder_product_image, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageHolder productImageHolder, int i) {

        Log.d(TAG, "onBindViewHolder: " + i);

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
            mImageView = itemView.findViewById(R.id.product_image);
        }


    }
}
