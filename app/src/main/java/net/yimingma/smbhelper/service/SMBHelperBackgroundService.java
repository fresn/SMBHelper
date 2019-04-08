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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yimingma.smbhelper.SMB.Customer;

import java.util.HashSet;
import java.util.Set;


public class SMBHelperBackgroundService extends Service implements FirebaseAuth.AuthStateListener {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    //event listeners
    Set<OnUserStateChangeListeners> userChangeListeners = new HashSet<OnUserStateChangeListeners>();

    private String TAG = "SMBHelperBackgroundService";

    public SMBHelperBackgroundService() {

        Log.d(TAG, "SMBHelperBackgroundService: service created");

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseAuth.addAuthStateListener(this);

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

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d(TAG, "onAuthStateChanged: ");
        for (OnUserStateChangeListeners listener : userChangeListeners) {
            if (firebaseAuth.getCurrentUser() != null) {
                Log.d(TAG, "onAuthStateChanged: login");
                listener.onLogIn(firebaseAuth.getCurrentUser());
            } else {
                Log.d(TAG, "onAuthStateChanged: signOut");
                listener.onSignOut();
            }
        }
    }


    //Binder class
    public class MyBind extends Binder {

        public void newCustomer(Customer customer) {
            db.collection("Customers").add(customer)
                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                        }
                    });
        }

        public ListenerHolder login(String email, String password) {

            final ListenerHolder listenerHolder=new ListenerHolder();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                for (OnSuccessListener onSuccessListener:listenerHolder.onSuccessListeners){
                                    onSuccessListener.onSuccess();
                                }
                                for (OnCompleteListener onCompleteListener:listenerHolder.onCompleteListeners){
                                    onCompleteListener.onComplete(true);
                                }
                            }else {
                                for (OnFailureListener onFailureListener:listenerHolder.onFailureListeners){
                                    onFailureListener.onFailure();
                                }
                                for (OnCompleteListener onCompleteListener:listenerHolder.onCompleteListeners){
                                    onCompleteListener.onComplete(false);
                                }
                            }
                        }
                    }
            );
            return listenerHolder;
        }

        public ListenerHolder signUp(String email, String password) {
            final ListenerHolder listenerHolder=new ListenerHolder();
            Log.d(TAG, "signUp: ");
            firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: create user success");
                                for (OnSuccessListener onSuccessListener:listenerHolder.onSuccessListeners){
                                    onSuccessListener.onSuccess();
                                }
                                for (OnCompleteListener onCompleteListener:listenerHolder.onCompleteListeners){
                                    onCompleteListener.onComplete(true);
                                }
                            } else {
                                for (OnFailureListener onFailureListener:listenerHolder.onFailureListeners){
                                    onFailureListener.onFailure();
                                }
                                for (OnCompleteListener onCompleteListener:listenerHolder.onCompleteListeners){
                                    onCompleteListener.onComplete(false);
                                }
                                Log.d(TAG, "onComplete: create user failure");
                            }
                        }
                    });
            return listenerHolder;
        }

        public void logOut() {
            firebaseAuth.signOut();
        }

        public boolean isLogin() {
            return firebaseAuth.getCurrentUser() != null;
        }

        public void addOnUserStateChangeListeners(OnUserStateChangeListeners onUserStateChangeListeners) {
            userChangeListeners.add(onUserStateChangeListeners);
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
        public ListenerHolder addOnFailureListener(OnFailureListener onFailureListener){
            onFailureListeners.add(onFailureListener);
            return this;
        }
        public ListenerHolder addOnSuccessListener(OnSuccessListener onSuccessListener){
            onSuccessListeners.add(onSuccessListener);
            return this;
        }
    }

    public interface OnCompleteListener {
        public void onComplete(Boolean isSuccess);
    }

    public interface OnSuccessListener {
        public void onSuccess();
    }

    public interface OnFailureListener {
        public void onFailure();
    }


    public interface OnUserStateChangeListeners {
        public void onLogIn(FirebaseUser firebaseUser);

        public void onSignOut();
    }
}
