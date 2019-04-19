package net.yimingma.smbhelper.Order;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.yimingma.smbhelper.R;

import java.util.ArrayList;

public class NewOrderItemsAdapter extends RecyclerView.Adapter<NewOrderItemsAdapter.ItemsHolder> {
    private ArrayList<net.yimingma.smbhelper.Order.Order.Item> items;

    NewOrderItemsAdapter(ArrayList<Order.Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NewOrderItemsAdapter.ItemsHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.holder_order_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsHolder itemsHolder, int i) {
        itemsHolder.setItem(this.items.get(i));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    class ItemsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image_view_Holder_item_product_image;
        public TextView text_view_product_title,
                text_view_holderitem_product_price,
                text_view_holder_item_count;
        public ImageButton image_button_holder_count_more,
                image_button_holder_count_less;
        Order.Item item;



        public ItemsHolder(@NonNull View itemView) {
            super(itemView);
            image_view_Holder_item_product_image = itemView.findViewById(R.id.image_view_Holder_item_product_image);
            text_view_product_title = itemView.findViewById(R.id.text_view_product_title);
            text_view_holderitem_product_price = itemView.findViewById(R.id.text_view_holderitem_product_price);
            text_view_holder_item_count = itemView.findViewById(R.id.text_view_holder_item_count);
            image_button_holder_count_more = itemView.findViewById(R.id.image_button_holder_count_more);
            image_button_holder_count_less = itemView.findViewById(R.id.image_button_holder_count_less);

            image_button_holder_count_less.setOnClickListener(this);
            image_button_holder_count_more.setOnClickListener(this);


        }

        void setItem(Order.Item item) {
            this.item = item;
            Picasso.get().load(item.product.getmIngUrl().get(0)).into(image_view_Holder_item_product_image);
            text_view_product_title.setText(item.product.getTitle());
            text_view_holderitem_product_price.setText(String.valueOf(getPrice()));
            text_view_holder_item_count.setText(String.valueOf(this.item.count));

        }

         float getPrice() {
            return (item.product.getPrice()*item.count)*(1+item.product.getTax());
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_button_holder_count_more:
                    this.item.count += 1;
                    text_view_holderitem_product_price.setText(String.valueOf(getPrice()));
                    text_view_holder_item_count.setText(String.valueOf(this.item.count));
                    break;
                case R.id.image_button_holder_count_less:
                    if (item.count >= 1) {
                        item.count -= 1;
                    }
                    text_view_holderitem_product_price.setText(String.valueOf(getPrice()));
                    text_view_holder_item_count.setText(String.valueOf(this.item.count));
                    break;
            }
        }
    }
}
