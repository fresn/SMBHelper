package net.yimingma.smbhelper.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreCollectionPT {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isRoot = false;
    private CollectionReference mCollectionReference;

    public FireStoreCollectionPT(String path) {
        this.mCollectionReference = db.collection(path);
    }

    public FireStoreCollectionPT(boolean isRoot) {
        this.isRoot = isRoot;
    }
}
