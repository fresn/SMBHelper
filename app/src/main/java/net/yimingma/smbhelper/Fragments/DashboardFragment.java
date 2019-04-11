package net.yimingma.smbhelper.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.yimingma.smbhelper.NewProductActivity;
import net.yimingma.smbhelper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    Button buttonLoadImage,buttonNewProduct;
    final int CHOOSE_IMAGES=119;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dashboard, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        buttonLoadImage=view.findViewById(R.id.load_image);
        buttonNewProduct=view.findViewById(R.id.new_product);

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadImage=new Intent();
                intentLoadImage.setType("image/*");
                intentLoadImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intentLoadImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentLoadImage,"Choose Images"),CHOOSE_IMAGES);
            }
        });
        buttonNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewProduct=new Intent(getContext(), NewProductActivity.class);
                startActivityForResult(intentNewProduct,0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
