package fr.wayd.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.wayd.bean.Click;
import fr.wayd.bean.User;

public class Dao_Users {
private static String TAG="Dao_Users";
    public static void AddUser(String id, String nom, String email) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).child("nom").setValue(nom);
        mDatabase.child("users").child(id).child("mail").setValue(email);

    }

    public static void addClick(final String uid) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
         mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println(user);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                user.addClick();
                mDatabase.child("users").child(uid).setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public static void addClickAnonyme(String idAnonyme) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(idAnonyme).runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if (user == null) {
                    return Transaction.success(mutableData);
                }

                user.addClick();

                // Set value and report transaction success
                mutableData.setValue(user);
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
