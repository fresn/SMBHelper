package net.yimingma.smbhelper.Fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.yimingma.smbhelper.Order.NewOrderActivity;
import net.yimingma.smbhelper.Order.Order;
import net.yimingma.smbhelper.Order.OrderItemsAdapter;
import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements View.OnClickListener {
    Button button_order_manage_add_new;
    RecyclerView
            recycler_view_order_manage_open,
            recycle_view_order_manage_closed;
    SMBHelperBackgroundService.MyBind myBind;

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


    private String TAG = "OrderFragment";

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getContext().bindService(new Intent(new Intent(getContext(), SMBHelperBackgroundService.class)), serviceConnection, Context.BIND_AUTO_CREATE);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_order_manage_add_new = view.findViewById(R.id.button_order_manage_add_new);
        button_order_manage_add_new.setOnClickListener(this);
        recycler_view_order_manage_open = view.findViewById(R.id.recycler_view_order_manage_open);
        recycle_view_order_manage_closed = view.findViewById(R.id.recycle_view_order_manage_closed);


        recycle_view_order_manage_closed.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_order_manage_open.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        if (myBind != null) {
            myBind.requireOrders(Order.STATUS.GOING)
                    .addOnSuccessListener(new SMBHelperBackgroundService.OnTypeSuccessListener<Order[]>() {
                        @Override
                        public void onSuccess(Order[] data) {
                            OrderItemsAdapter orderItemsAdapter=new OrderItemsAdapter(new ArrayList<Order>(Arrays.asList(data)));
                            recycler_view_order_manage_open.setAdapter(orderItemsAdapter);
                        }
                    });
            myBind.requireOrders(Order.STATUS.CLOSED)
                    .addOnSuccessListener(new SMBHelperBackgroundService.OnTypeSuccessListener<Order[]>() {
                        @Override
                        public void onSuccess(Order[] data) {
                            OrderItemsAdapter orderItemsAdapter=new OrderItemsAdapter(new ArrayList<Order>(Arrays.asList(data)));
                            recycle_view_order_manage_closed.setAdapter(orderItemsAdapter);
                        }
                    });
        }

        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        getContext().unbindService(serviceConnection);
        super.onDestroy();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_order_manage_add_new:
                Intent intentNewOrder = new Intent(getContext(), NewOrderActivity.class);
                startActivityForResult(intentNewOrder, 911);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
