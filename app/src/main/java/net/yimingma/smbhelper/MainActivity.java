package net.yimingma.smbhelper;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import net.yimingma.smbhelper.Fragments.DashboardFragment;
import net.yimingma.smbhelper.Fragments.DashboardUserGuideFragment;
import net.yimingma.smbhelper.Fragments.OrderFragment;
import net.yimingma.smbhelper.Fragments.SettingNavFragment;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String TAG = "MainActivity";
    Point mPoint;
    FrameLayout mFrameLayout;
    int mToastYOffset;
    FirebaseFirestore db;
    FragmentManager mFragmentManager;
    BottomNavigationView bottomNavigationView;
    Toolbar mToolbar;
    SMBHelperBackgroundService.MyBind myServiceBind;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            myServiceBind = (SMBHelperBackgroundService.MyBind) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.loged_in, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                Log.d(TAG, "onOptionsItemSelected: logout");
                myServiceBind.logOut();
                onStart();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, SMBHelperBackgroundService.class);
        serviceIntent.putExtra("from", this.getClass().toString());
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mFrameLayout = (FrameLayout) findViewById(R.id.main_content_layout);

        //get scream size
        mPoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(mPoint);
        mToastYOffset = (int) (mPoint.y / 5);

        //init Fragment
        mFragmentManager = getSupportFragmentManager();

        //setupActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);


    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");

        //mFragmentManager.beginTransaction().add(R.id.main_content_layout,new DashboardUserGuideFragment(),"GuideFragment").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");


        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }


    void lunchSettingScream() {


        Fragment currentFragment = mFragmentManager.findFragmentByTag("Setting");
        mToolbar.setTitle("Setting");
        if (currentFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.main_content_layout, new SettingNavFragment(), "Setting").commit();
        }
    }

    void lunchProcessScream() {
        Fragment currentFragment = mFragmentManager.findFragmentByTag("Order");
        mToolbar.setTitle("Order");
        if (currentFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.main_content_layout, new OrderFragment(), "Process").commit();
        }
    }

    void lunchDashboardScream() {

        if (myServiceBind != null && myServiceBind.isLogin()) {

            Fragment currentFragment = mFragmentManager.findFragmentByTag("Dash");
            mToolbar.setTitle("Dashboard");
            if (currentFragment == null) {
                mFragmentManager.beginTransaction().replace(R.id.main_content_layout, new DashboardFragment(), "Dash").commit();
            }


        } else {

            Fragment currentFragment = mFragmentManager.findFragmentByTag("UserGuide");
            mToolbar.setTitle("Dashboard");
            if (currentFragment == null) {
                mFragmentManager.beginTransaction().replace(R.id.main_content_layout, new DashboardUserGuideFragment(), "UserGuide").commit();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");
        switch (item.getItemId()) {
            case R.id.navigation_setting:

                Log.d(TAG, "onNavigationItemSelected: setting");
                if (myServiceBind != null && myServiceBind.isLogin()) {
                    lunchSettingScream();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_LONG).show();
                    return false;
                }


            case R.id.navigation_dashboard:
                Log.d(TAG, "onNavigationItemSelected: dashboard");
                lunchDashboardScream();
                return true;


            case R.id.navigation_process:
                Log.d(TAG, "onNavigationItemSelected: process");
                if (myServiceBind != null && myServiceBind.isLogin()) {
                    lunchProcessScream();
                    return true;
                }else{
                    Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_LONG).show();
                    return false;
                }
        }
        return true;
    }
}
