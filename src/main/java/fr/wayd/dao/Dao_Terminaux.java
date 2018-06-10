package fr.wayd.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.wayd.bean.Click;
import fr.wayd.bean.Commun;

public class Dao_Terminaux {

    public static void addClickTerminal(final String idTerminal) {
        final String nomStr = Commun.getDateNowStr();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("terminaux").child(idTerminal).child(nomStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Click click = dataSnapshot.getValue(Click.class);
                System.out.println(click);
                if (click == null) {
                    Click firstClick = new Click(1);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("terminaux").child(idTerminal).child(nomStr).setValue(firstClick);
                } else {
                    click.ajouteUn();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("terminaux").child(idTerminal).child(nomStr).setValue(click);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
