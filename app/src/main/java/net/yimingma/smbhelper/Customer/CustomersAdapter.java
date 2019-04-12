package net.yimingma.smbhelper.Customer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.yimingma.smbhelper.Product.ProductImagesAdapter;
import net.yimingma.smbhelper.R;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomerItemHolder> {
    Customer[] customers;

    CustomersAdapter(Customer[] customers) {
        this.customers = customers;
    }

    @NonNull
    @Override
    public CustomerItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new CustomerItemHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.customer_item_holder, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerItemHolder customerItemHolder, int i) {
        customerItemHolder.text_view_customer_manage_holder_customer_name.setText(customers[i].getDisplayName());

    }


    @Override
    public int getItemCount() {
        return this.customers.length;
    }

    public static class CustomerItemHolder extends RecyclerView.ViewHolder {

        ImageView image_view_customer_avatar;
        TextView text_view_customer_manage_holder_customer_name,
                text_view_holder_order_count;

        public CustomerItemHolder(@NonNull View itemView) {
            super(itemView);
            image_view_customer_avatar = itemView.findViewById(R.id.image_view_customer_manage_holder_avatar);
            text_view_customer_manage_holder_customer_name = itemView.findViewById(R.id.text_view_customer_manage_holder_customer_name);
            text_view_holder_order_count=itemView.findViewById(R.id.text_view_holder_order_count);
        }
    }
}
