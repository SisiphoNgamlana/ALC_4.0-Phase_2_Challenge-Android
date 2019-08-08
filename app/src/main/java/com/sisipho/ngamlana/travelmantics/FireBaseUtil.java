package com.sisipho.ngamlana.travelmantics;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FireBaseUtil {
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    private static FireBaseUtil fireBaseUtil;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener fireBaseAuthStateListener;
    public static ArrayList<TravelDeal> travelDealList;
    private static final int RC_SIGN_IN = 123;
    private static MainActivity caller;
    public static boolean isAdmin;
    public static FirebaseStorage firebaseStorage;
    public static StorageReference storageReference;

    private FireBaseUtil() {

    }

    public static void openFireBaseReference(String reference, final MainActivity callerActivity) {
        if (fireBaseUtil == null) {
            fireBaseUtil = new FireBaseUtil();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            fireBaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        FireBaseUtil.signIn();
                    } else {
                        String userID = firebaseAuth.getUid();
                        checkAdmin(userID);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome Back", Toast.LENGTH_LONG).show();
                }
            };
        }
        travelDealList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(reference);
        connectStorage();
    }

    private static void checkAdmin(String uid) {
        FireBaseUtil.isAdmin = false;
        DatabaseReference ref = firebaseDatabase.getReference().child("administrators").
                child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FireBaseUtil.isAdmin = true;
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    public static void attachListener() {
        firebaseAuth.addAuthStateListener(fireBaseAuthStateListener);
    }

    public static void detachListener() {
        firebaseAuth.removeAuthStateListener(fireBaseAuthStateListener);
    }

    private static void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void connectStorage() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("deals_pictures");
    }
}
