package fr.wayd.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import fr.wayd.bean.Association;
import fr.wayd.bean.User;
import fr.wayd.wdonation.Login;

public class Dao_Associations {
    public static String TAG="Dao_Associations";




    public static void addClickTransaction(final String uid){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("associations").child(uid).runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Association association = mutableData.getValue(Association.class);
                if (association == null) {
                    return Transaction.success(mutableData);
                }

                association.addClick();

                // Set value and report transaction success
                mutableData.setValue(association);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });



    }










}
