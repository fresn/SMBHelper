package net.yimingma.smbhelper.Product;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.yimingma.smbhelper.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    Product[] products;

    public ProductsAdapter(Product[] products) {
        this.products = products;
    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProductHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.holder_product_item, viewGroup, false));
    }



    @Override
    public void onBindViewHolder(@NonNull ProductHolder productHolder, int i) {
        if (!products[i].getmIngUrl().isEmpty()) {
            Picasso.get().load(products[i].getmIngUrl().get(0))
                    .into(productHolder.image_view_product_manage_title_image);
        }
        productHolder
                .text_view_product_manage_product_count
                .setText(String
                        .valueOf(products[i].getInventory())
                );
        productHolder.text_view_product_manage_title
                .setText(
                        products[i].getTitle()
                );

    }


    @Override
    public int getItemCount() {
        if(products==null){
            return 0;
        }
        return this.products.length;
    }

    static class ProductHolder extends RecyclerView.ViewHolder {

        ImageView image_view_product_manage_title_image;
        TextView text_view_product_manage_title,
                text_view_product_manage_product_count;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            image_view_product_manage_title_image = itemView.findViewById(R.id.image_view_product_manage_title_image);
            text_view_product_manage_title = itemView.findViewById(R.id.text_product_manage_title);
            text_view_product_manage_product_count = itemView.findViewById(R.id.text_view_product_manage_product_count);

        }
    }
}
