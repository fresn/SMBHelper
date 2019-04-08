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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.User.CreateUserActivity;
import net.yimingma.smbhelper.User.LoginActivity;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardUserGuideFragment extends Fragment {

    private String TAG = "DashboardUserGuideFragment";

    Button buttonCreateUser,buttonLogin;
    SMBHelperBackgroundService.MyBind myBind;

    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBind=(SMBHelperBackgroundService.MyBind)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public DashboardUserGuideFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dashboard_user_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buttonCreateUser=(Button) view.findViewById(R.id.dashboard_guide_button_create_user);
        buttonLogin=(Button) view.findViewById(R.id.dashboard_guide_login);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {

        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createUserIntent=new Intent(getContext(), CreateUserActivity.class);
                startActivityForResult(createUserIntent,0);

                Log.d(TAG, "onClick: CreateUser" );
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(getContext(), LoginActivity.class);
                startActivityForResult(loginIntent,0);

                Log.d(TAG, "onClick: Login");
            }
        });


        super.onStart();
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        getContext().bindService(new Intent(getContext(),SMBHelperBackgroundService.class),serviceConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    public void onPause() {

        getContext().unbindService(serviceConnection);
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
