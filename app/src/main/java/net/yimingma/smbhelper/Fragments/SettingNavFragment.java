package net.yimingma.smbhelper.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.yimingma.smbhelper.Customer.CustomerManageActivity;
import net.yimingma.smbhelper.Product.ProductManageActivity;
import net.yimingma.smbhelper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingNavFragment extends Fragment {

    private Button mBtnCustomer,mBtnProduct;

    private String TAG="SettingNavFragment";

    public SettingNavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_nav, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //init btn
        mBtnCustomer=view.findViewById(R.id.button_setting_customer);
        mBtnProduct=view.findViewById(R.id.button_setting_product);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {

        mBtnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CustomerManageActivity.class);
                startActivityForResult(intent,0);
            }
        });

        mBtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ProductManageActivity.class);
                startActivityForResult(intent,0);
            }
        });

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
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
