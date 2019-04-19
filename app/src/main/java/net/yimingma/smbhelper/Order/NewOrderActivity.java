package net.yimingma.smbhelper.Order;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.type.Date;
import com.google.type.TimeOfDay;

import net.yimingma.smbhelper.Customer.Customer;
import net.yimingma.smbhelper.Product.Product;
import net.yimingma.smbhelper.R;
import net.yimingma.smbhelper.service.SMBHelperBackgroundService;
import net.yimingma.smbhelper.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class NewOrderActivity extends AppCompatActivity
        implements View.OnFocusChangeListener,
        RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,
        DialogInterface.OnClickListener {

    Order order = new Order();
    Date dueDate = Date.getDefaultInstance();
    TimeOfDay dueTime = TimeOfDay.getDefaultInstance();

    final String TAG = "NewOrderActivity";
    final int SERVICE_TAG = 999;


    EditText edit_text_new_order_due_date, edit_text_new_order_due_time;
    Switch switch_new_order_delivery;
    TextView text_view_new_order_delivery_method,
            text_view_new_order_customer_name,
            text_view_new_order_customer_street,
            text_view_new_order_customer_address,
            text_view_new_order_save;
    EditText edit_text_new_order_note_content;
    ImageView image_view_new_order_items_add_tag,
            image_view_new_order_customer_add_tag;
    LinearLayout linear_layout_new_order_customer;
    RecyclerView recycler_view_new_order_items;
    String[] customersID;
    Customer[] customers;
    String[] productsID;
    Product[] products;

    ArrayList<Order.Item> items = new ArrayList<>();


    SMBHelperBackgroundService.MyBind myBind;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBind = (SMBHelperBackgroundService.MyBind) service;
            myBind.requireCustomers().addOnSuccessListener(
                    new SMBHelperBackgroundService.OnTypeSuccessListener<Customer[]>() {
                        @Override
                        public void onSuccess(Customer[] data) {
                            List<String> localCustomersID = new ArrayList<String>();
                            for (Customer customer : data) {
                                localCustomersID.add(customer.getDisplayName());
                            }
                            customersID = localCustomersID.toArray(new String[localCustomersID.size()]);
                            if (customersID != null && productsID != null) {
                                enableAddTags();
                            }

                            customers = data;
                        }
                    }
            );
            myBind.requireProducts().addOnSuccessListener(
                    new SMBHelperBackgroundService.OnTypeSuccessListener<Product[]>() {
                        @Override
                        public void onSuccess(Product[] data) {
                            List<String> LocalproductsID = new ArrayList<String>();
                            for (Product product : data) {
                                LocalproductsID.add(product.getTitle());
                            }
                            productsID = LocalproductsID.toArray(new String[LocalproductsID.size()]);
                            if (customersID != null && productsID != null) {
                                enableAddTags();
                            }

                            products = data;
                        }
                    }
            );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBind = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        this.order.setID();

        bindService(new Intent(this, SMBHelperBackgroundService.class), serviceConnection, BIND_AUTO_CREATE);

        edit_text_new_order_due_date = findViewById(R.id.edit_text_new_order_due_date);
        edit_text_new_order_due_time = findViewById(R.id.edit_text_new_order_due_time);
        switch_new_order_delivery = findViewById(R.id.switch_new_order_delivery);
        text_view_new_order_delivery_method = findViewById(R.id.text_view_new_order_delivery_method);
        image_view_new_order_items_add_tag = findViewById(R.id.image_view_new_order_items_add_tag);
        image_view_new_order_customer_add_tag = findViewById(R.id.image_view_new_order_customer_add_tag);
        linear_layout_new_order_customer = findViewById(R.id.linear_layout_new_order_customer);
        text_view_new_order_customer_name = findViewById(R.id.text_view_new_order_customer_name);
        text_view_new_order_customer_street = findViewById(R.id.text_view_new_order_customer_street);
        text_view_new_order_customer_address = findViewById(R.id.text_view_new_order_customer_address);
        recycler_view_new_order_items = findViewById(R.id.recycler_view_new_order_items);
        text_view_new_order_save = findViewById(R.id.text_view_new_order_save);
        edit_text_new_order_note_content=findViewById(R.id.edit_text_new_order_note_content);

        image_view_new_order_customer_add_tag.setOnClickListener(this);
        image_view_new_order_items_add_tag.setOnClickListener(this);
        switch_new_order_delivery.setChecked(false);
        edit_text_new_order_due_date.setOnFocusChangeListener(this);
        edit_text_new_order_due_time.setOnFocusChangeListener(this);
        switch_new_order_delivery.setOnCheckedChangeListener(this);
        text_view_new_order_save.setOnClickListener(this);
        linear_layout_new_order_customer.setVisibility(View.INVISIBLE);

        recycler_view_new_order_items.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void enableAddTags() {
        image_view_new_order_items_add_tag.setAlpha(1f);
        image_view_new_order_customer_add_tag.setAlpha(1f);
    }

    private void disableAddTags() {
        image_view_new_order_items_add_tag.setAlpha(0.3f);
        image_view_new_order_customer_add_tag.setAlpha(0.3f);
    }

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == edit_text_new_order_due_date) {
            if (hasFocus) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(this).setFirstDayOfWeek(Calendar.SATURDAY);
                cdp.show(getSupportFragmentManager(), "123");
                edit_text_new_order_due_date.clearFocus();
            }
        }

        if (v == edit_text_new_order_due_time) {
            if (hasFocus) {
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(this)
                        .setThemeDark();
                rtpd.show(getSupportFragmentManager(), "");
                edit_text_new_order_due_time.clearFocus();
            }
        }

    }

    /**
     * @param dialog    The view associated with this listener.
     * @param hourOfDay The hour that was set.
     * @param minute    The minute that was set.
     */
    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        edit_text_new_order_due_time.setText(String.format("%s:%s", hourOfDay, minute));
        dueTime = dueTime.toBuilder().setHours(hourOfDay).setMinutes(minute).build();
        Log.d(TAG, "onTimeSet: ");
    }

    /**
     * @param dialog      The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility with {@link java.util.Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        edit_text_new_order_due_date.setText(Utils.months[monthOfYear] + " " + dayOfMonth + "," + year);
        dueDate = dueDate.toBuilder().setYear(year).setMonth(monthOfYear).setDay(dayOfMonth).build();
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == switch_new_order_delivery) {
            Log.d(TAG, "onCheckedChanged: ");
            if (isChecked) {
                text_view_new_order_delivery_method.setText(R.string.tag_delivery);
            } else {
                text_view_new_order_delivery_method.setText(R.string.tag_pick_up);
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        final HashSet<Product> pr = new HashSet<>();
        if (customersID != null && productsID != null) {
            switch (v.getId()) {
                case R.id.image_view_new_order_customer_add_tag:
                    AlertDialog.Builder customerBuilder = new AlertDialog.Builder(this);
                    customerBuilder.setItems(customersID, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which != -1) {
                                Customer customer = customers[which];
                                text_view_new_order_customer_name.setText(customer.getDisplayName());
                                text_view_new_order_customer_street.setText(customer.street);
                                text_view_new_order_customer_address.setText(customer.state + " " + customer.country + " ");
                                linear_layout_new_order_customer.setVisibility(View.VISIBLE);
                                order.customer = customer;
                            }

                        }
                    })
                            .create()
                            .show();
                    break;
                case R.id.image_view_new_order_items_add_tag:
                    final AlertDialog.Builder itemBuilder = new AlertDialog.Builder(this);
                    itemBuilder
                            .setMultiChoiceItems(productsID, null, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if (isChecked) {
                                        pr.add(products[which]);
                                    } else {
                                        pr.remove(products[which]);
                                    }

                                }
                            })
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    order.clearItems();
                                    for (Product mproduct : pr) {
                                        order.addItem(new Order.Item(mproduct, 1, 0f, ""));
                                    }
                                    recycler_view_new_order_items.setAdapter(new NewOrderItemsAdapter(order.itemList));
                                    onStart();
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "onClick: ");
                                }
                            })
                            .create()
                            .show();
                    break;
            }
            if (v.getId() == R.id.text_view_new_order_save) {
                order.isDelivery=switch_new_order_delivery.isChecked();
                order.createDate= new java.sql.Date(System.currentTimeMillis());
                java.sql.Date localDate=new java.sql.Date(dueDate.getYear(),dueDate.getMonth(),dueDate.getDay());
                localDate.setHours(dueTime.getHours());
                localDate.setTime(dueTime.getMinutes());
                order.deuDate= localDate;
                order.note=edit_text_new_order_note_content.getText().toString();
                order.status= Order.STATUS.GOING;
                myBind.newOrder(this.order).addOnSuccessListener(new SMBHelperBackgroundService.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }
                });
            }
        }
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog the dialog that received the click
     * @param which  the button that was clicked (ex.
     *               {@link DialogInterface#BUTTON_POSITIVE}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
