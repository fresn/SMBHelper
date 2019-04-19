package net.yimingma.smbhelper.Order;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.yimingma.smbhelper.R;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemHolder> {

    private final String TAG="OrderItemsAdapter";
    ArrayList<Order> orders;

    public OrderItemsAdapter(ArrayList<Order> orders) {
        Log.d(TAG, "OrderItemsAdapter: orders size:"+orders.size());
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderItemHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.holder_order_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder orderItemHolder, int i) {
        orderItemHolder.text_view_order_manage_holder_customer_name.setText(orders.get(i).customer.getDisplayName());
//        orderItemHolder.text_view_order_manage_holder_due.setText();
        orderItemHolder.text_view_order_manage_holder_price_total.setText(String.valueOf(orders.get(i).getTotalPrice()));
        orderItemHolder.text_view_order_manage_holder_street.setText(orders.get(i).customer.street);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderItemHolder extends RecyclerView.ViewHolder {
        public TextView
                text_view_order_manage_holder_customer_name,
                text_view_order_manage_holder_price_total,
                text_view_order_manage_holder_street,
                text_view_order_manage_holder_due;

        public OrderItemHolder(@NonNull View itemView) {

            super(itemView);
            text_view_order_manage_holder_customer_name = itemView.findViewById(R.id.text_view_order_manage_holder_customer_name);
            text_view_order_manage_holder_price_total = itemView.findViewById(R.id.text_view_order_manage_holder_price_total);
            text_view_order_manage_holder_street = itemView.findViewById(R.id.text_view_order_manage_holder_street);
            text_view_order_manage_holder_due = itemView.findViewById(R.id.text_view_order_manage_holder_due);

        }
    }
}
