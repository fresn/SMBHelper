package net.yimingma.smbhelper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import net.yimingma.smbhelper.Customer.Customer;
import net.yimingma.smbhelper.Order.Order;
import net.yimingma.smbhelper.Product.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SMBHelperBackgroundService extends Service {


    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    Set<UserStateChangeHandler> SMBUserStateChangeHandlers = new HashSet<>();
    Set<ProductListeners> productListeners = new HashSet<ProductListeners>();
    FirebaseUser me;
    CollectionReference productsRef, customersRef, ordersRef;
    DocumentReference userRoot;
    boolean isInit = false;

    //event listeners
    private String TAG = "SMBHelperBackgroundService";

    public SMBHelperBackgroundService() {

        Log.d(TAG, "SMBHelperBackgroundService: service created");
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //listen to user login logout
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && !isInit) {

                    Log.d(TAG, "onAuthStateChanged: logedIn");
                    init();

                    for (UserStateChangeHandler userStateChangeHandler : SMBUserStateChangeHandlers) {
                        userStateChangeHandler.onLogIn(me);
                    }
                } else {
                    for (UserStateChangeHandler userStateChangeHandler : SMBUserStateChangeHandlers) {
                        userStateChangeHandler.onSignOut();
                    }
                }
            }
        });


    }

    private void init() {
        Log.d(TAG, "init: user is " + firebaseAuth.getCurrentUser().getEmail());
        me = firebaseAuth.getCurrentUser();
        assert me != null : "user is null";
        assert me.getEmail() != null : "user email is null";
        userRoot = db.collection("Users").document(me.getEmail());
        productsRef = userRoot.collection("Products");
        customersRef = userRoot.collection("Customers");
        ordersRef = userRoot.collection("Orders");
        isInit = true;
    }

    private void logout() {
        Log.d(TAG, "logout: ");
        firebaseAuth.signOut();
        me = null;
        userRoot = null;
        productsRef = null;
        customersRef = null;
        ordersRef = null;
        isInit = false;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new MyBind();
    }


    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     *
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     *
     * <p class="caution">Note that the system calls this on your
     * service's main thread.  A service's main thread is the same
     * thread where UI operations take place for Activities running in the
     * same process.  You should always avoid stalling the main
     * thread's event loop.  When doing long-running operations,
     * network calls, or heavy disk I/O, you should kick off a new
     * thread, or use {@link AsyncTask}.</p>
     *
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Called when new clients have connected to the service, after it had
     * previously been notified that all had disconnected in its
     * {@link #onUnbind}.  This will only be called if the implementation
     * of {@link #onUnbind} was overridden to return true.
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     */
    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind: ");
        super.onRebind(intent);
    }


    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    /**
     * Called when all clients have disconnected from a particular interface
     * published by the service.  The default implementation does nothing and
     * returns false.
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return true if you would like to have the service's
     * {@link #onRebind} method later called when new clients bind to it.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }


    //Binder class
    public class MyBind extends Binder implements FirebaseAuth.AuthStateListener {

        List<UserStateChangeHandler> MyBindUserStateChangeHandler = new ArrayList<>();

        boolean ifEventInitialized = false;


        public MyBind() {
            Log.d(TAG, "MyBind: ");
            SMBHelperBackgroundService.this.SMBUserStateChangeHandlers.add(new UserStateChangeHandler() {
                @Override
                public void onLogIn(FirebaseUser firebaseUser) {
                    if (!ifEventInitialized) {
                        initEvent();
                    }
                    for (UserStateChangeHandler userStateChangeHandler : MyBindUserStateChangeHandler) {
                        Log.d(TAG, "onLogIn: MyBind");
                        userStateChangeHandler.onLogIn(SMBHelperBackgroundService.this.me);
                    }

                }

                @Override
                public void onSignOut() {
                    for (UserStateChangeHandler userStateChangeHandler : MyBindUserStateChangeHandler) {
                        userStateChangeHandler.onSignOut();
                        unInitEvent();
                    }
                }
            });

        }

        void initEvent() {
            Log.d(TAG, "initEvent: ");

            ifEventInitialized = true;

        }

        void unInitEvent() {

            ifEventInitialized = false;
        }


        public ListenerHolder newOrder(Order order) {
            final ListenerHolder listenerHolder = new ListenerHolder();
            ordersRef.document(order.OrderID).set(order)
                    .addOnSuccessListener(
                            new com.google.android.gms.tasks.OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    listenerHolder.success();
                                }
                            })
                    .addOnFailureListener(
                            new com.google.android.gms.tasks.OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listenerHolder.failure();
                                }
                            }
                    );
            return listenerHolder;
        }

        public TypeListenerHolder<Order[]> requireOrders(Order.STATUS status) {
            final TypeListenerHolder<Order[]> typeListenerHolder = new TypeListenerHolder<>();

            boolean isClosed;
            switch (status) {
                case GOING:
                    isClosed = false;
                    break;
                case CLOSED:
                    isClosed = true;
                    break;
                default:
                    isClosed = false;
                    break;
            }

            ordersRef

                    .whereEqualTo("isClosed", isClosed)
                    .get()
                    .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Order> orders = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                orders.add(documentSnapshot.toObject(Order.class));
                            }
                            typeListenerHolder.success(orders.toArray(new Order[queryDocumentSnapshots.getDocuments().size()]));
                        }
                    })
                    .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            typeListenerHolder.failure();
                        }
                    });


            return typeListenerHolder;
        }

        public TypeListenerHolder<Order[]> requireOrders(Date dateStart, Date dateEnd) {
            final TypeListenerHolder<Order[]> typeListenerHolder = new TypeListenerHolder<Order[]>();
            ordersRef.whereGreaterThan("createTime", dateStart).whereLessThan("createDate", dateEnd)
                    .get()
                    .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Order> orders = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                orders.add(documentSnapshot.toObject(Order.class));
                            }
                            typeListenerHolder.success(orders.toArray(new Order[orders.size()]));
                        }
                    })
                    .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            typeListenerHolder.failure();
                        }
                    });
            return typeListenerHolder;
        }

        public TypeListenerHolder<Product[]> requireProducts() {
            final TypeListenerHolder<Product[]> typeListenerHolder = new TypeListenerHolder<>();
            productsRef.get().addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            ArrayList<Product> products = new ArrayList<>();

                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                products.add(queryDocumentSnapshot.toObject(Product.class));
                            }

                            typeListenerHolder.success(products.toArray(new Product[task.getResult().size()]));
                        } else {
                            typeListenerHolder.success(null);
                        }

                    } else {
                        typeListenerHolder.failure();
                    }
                }
            });
            return typeListenerHolder;
        }

        public TypeListenerHolder<Customer[]> requireCustomers() {
            Log.d(TAG, "requireCustomers: ");
            final TypeListenerHolder<Customer[]> customerTypeListenerHolder = new TypeListenerHolder<Customer[]>();
            customersRef.orderBy("timestamp")
                    .get()

                    .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<QuerySnapshot>() {
                        ArrayList<Customer> customers = new ArrayList<>();

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d(TAG, "onSuccess: ");
                            if (!queryDocumentSnapshots.isEmpty()) {

                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    customers.add(documentSnapshot.toObject(Customer.class));
                                }
                                customerTypeListenerHolder.success(customers.toArray(new Customer[queryDocumentSnapshots.size()]));
                            } else {
                                Log.d(TAG, "onSuccess: empty result");
                            }
                        }
                    }).addOnCompleteListener(
                    new com.google.android.gms.tasks.OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d(TAG, "onComplete: " + task.isSuccessful());
                        }
                    }
            );
            return customerTypeListenerHolder;
        }

        public TypeListenerHolder<Customer> getCustomer(String id) {
            final TypeListenerHolder<Customer> customerTypeListenerHolder = new TypeListenerHolder<>();
            customersRef.document(id).get().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    customerTypeListenerHolder.success(documentSnapshot.toObject(Customer.class));
                }
            });
            return customerTypeListenerHolder;
        }

        public ListenerHolder newCustomer(Customer customer) {
            final ListenerHolder listenerHolder = new ListenerHolder();
            customersRef.add(customer).addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        listenerHolder.success();
                    } else {
                        listenerHolder.failure();
                    }
                }
            });
            return listenerHolder;
        }

        public ListenerHolder login(String email, String password) {

            final ListenerHolder listenerHolder = new ListenerHolder();
            firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        for (OnSuccessListener onSuccessListener : listenerHolder.onSuccessListeners) {
                                            onSuccessListener.onSuccess();
                                        }
                                        for (OnCompleteListener onCompleteListener : listenerHolder.onCompleteListeners) {
                                            onCompleteListener.onComplete(true);
                                        }
                                    } else {
                                        for (OnFailureListener onFailureListener : listenerHolder.onFailureListeners) {
                                            onFailureListener.onFailure();
                                        }
                                        for (OnCompleteListener onCompleteListener : listenerHolder.onCompleteListeners) {
                                            onCompleteListener.onComplete(false);
                                        }
                                    }
                                }
                            }
                    );
            return listenerHolder;
        }

        public ListenerHolder signUp(String email, String password) {
            final ListenerHolder listenerHolder = new ListenerHolder();
            Log.d(TAG, "signUp: ");
            firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: create user success");
                                listenerHolder.success();
                            } else {
                                listenerHolder.failure();
                                Log.d(TAG, "onComplete: create user failure");
                            }
                        }
                    });
            return listenerHolder;
        }

        public void logOut() {
            logout();
        }

        public boolean isLogin() {
            return firebaseAuth.getCurrentUser() != null;
        }

        public ListenerHolder newProduct(final Product product) {
            final ListenerHolder listenerHolder = new ListenerHolder();

            productsRef.document(product.getTitle()).set(product)
                    .addOnCompleteListener(
                            new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: success create " + product.getTitle());
                                        listenerHolder.success();
                                    } else {
                                        Log.d(TAG, "onComplete: fail to create " + product.getTitle());
                                        listenerHolder.failure();
                                    }
                                }
                            }
                    );


            return listenerHolder;
        }

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            Log.d(TAG, "onAuthStateChanged: ");
            for (UserStateChangeHandler listener : SMBHelperBackgroundService.this.SMBUserStateChangeHandlers) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d(TAG, "onAuthStateChanged: login");
                    listener.onLogIn(firebaseAuth.getCurrentUser());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signOut");
                    listener.onSignOut();
                }
            }
        }


        public void addOnUserStateChangeListeners(UserStateChangeHandler userStateChangeListener) {
            SMBHelperBackgroundService.this.SMBUserStateChangeHandlers.add(userStateChangeListener);
        }
    }

    public class TypeListenerHolder<T> {
        Set<OnTypeCompleteListener> onCompleteListeners = new HashSet<>();
        Set<OnFailureListener> onFailureListeners = new HashSet<>();
        Set<OnTypeSuccessListener> onSuccessListeners = new HashSet<>();

        public TypeListenerHolder<T> addOnCompleteListener(OnTypeCompleteListener<T> onCompleteListener) {
            onCompleteListeners.add(onCompleteListener);
            return this;
        }

        public TypeListenerHolder<T> addOnFailureListener(OnFailureListener onFailureListener) {
            onFailureListeners.add(onFailureListener);
            return this;
        }

        public TypeListenerHolder<T> addOnSuccessListener(OnTypeSuccessListener<T> onSuccessListener) {
            onSuccessListeners.add(onSuccessListener);
            return this;
        }

        void success(T data) {
            for (OnTypeSuccessListener<T> onSuccessListener : this.onSuccessListeners) {
                onSuccessListener.onSuccess(data);
            }
            for (OnTypeCompleteListener<T> onCompleteListener : this.onCompleteListeners) {
                onCompleteListener.onComplete(true, data);
            }
        }

        void failure() {
            for (OnFailureListener onFailureListener : this.onFailureListeners) {
                onFailureListener.onFailure();
            }
            for (OnTypeCompleteListener<T> onCompleteListener : this.onCompleteListeners) {
                onCompleteListener.onComplete(false, null);
            }
        }
    }

    public class ListenerHolder {
        Set<OnCompleteListener> onCompleteListeners = new HashSet<>();
        Set<OnFailureListener> onFailureListeners = new HashSet<>();
        Set<OnSuccessListener> onSuccessListeners = new HashSet<>();

        public ListenerHolder addOnCompleteListener(OnCompleteListener onCompleteListener) {
            onCompleteListeners.add(onCompleteListener);
            return this;
        }

        public ListenerHolder addOnFailureListener(OnFailureListener onFailureListener) {
            onFailureListeners.add(onFailureListener);
            return this;
        }

        public ListenerHolder addOnSuccessListener(OnSuccessListener onSuccessListener) {
            onSuccessListeners.add(onSuccessListener);
            return this;
        }

        void success() {
            for (OnSuccessListener onSuccessListener : this.onSuccessListeners) {
                onSuccessListener.onSuccess();
            }
            for (OnCompleteListener onCompleteListener : this.onCompleteListeners) {
                onCompleteListener.onComplete(true);
            }
        }

        void failure() {
            for (OnFailureListener onFailureListener : this.onFailureListeners) {
                onFailureListener.onFailure();
            }
            for (OnCompleteListener onCompleteListener : this.onCompleteListeners) {
                onCompleteListener.onComplete(false);
            }
        }
    }

    public interface OnCompleteListener<T> {
        void onComplete(Boolean isSuccess);
    }

    public interface OnTypeCompleteListener<T> {
        void onComplete(Boolean isSuccess, T data);
    }

    public interface OnTypeSuccessListener<T> {
        void onSuccess(T data);
    }

    public interface OnSuccessListener {
        void onSuccess();
    }

    public interface OnFailureListener {
        void onFailure();
    }


    public interface UserStateChangeHandler {
        void onLogIn(FirebaseUser firebaseUser);

        void onSignOut();
    }

    public interface ProductListeners {
        void onProductAdded(Product product);

        void onProductSold(Product product, Order order);

        void onProductSoldOut(Product product);
    }
}
