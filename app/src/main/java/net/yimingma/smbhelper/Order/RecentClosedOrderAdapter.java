package net.yimingma.smbhelper.Order;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.yimingma.smbhelper.R;

public class RecentClosedOrderAdapter extends RecyclerView.Adapter<RecentClosedOrderAdapter.RecentClosedOrderHolder> {

    @NonNull
    @Override
    public RecentClosedOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecentClosedOrderHolder(
                LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.holder_order_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentClosedOrderHolder recentClosedOrderHolder, int i) {
        //TODO


    }

    @Override
    public int getItemCount() {
        return 0;
    }



    static class RecentClosedOrderHolder extends RecyclerView.ViewHolder{

        public RecentClosedOrderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
