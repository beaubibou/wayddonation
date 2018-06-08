package fr.wayd.wdonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fr.wayd.bean.Association;

public class ListAssociationActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Association> associationList = new ArrayList<>();
    private AssociationAdapter associationAdapter;
    private FloatingActionButton ajouteassociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_association);

        associationAdapter = new AssociationAdapter(this, associationList);
        ListView listViewAssociation = (ListView) findViewById(R.id.listassocitations);
        ajouteassociation = findViewById(R.id.ajouteassociation);
        ajouteassociation.setOnClickListener(this);
        listViewAssociation.setAdapter(associationAdapter);
        listViewAssociation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Association association = (Association) view.getTag();
                Intent appel = new Intent(ListAssociationActivity.this, AddAssociationActivity.class);
                appel.putExtra("id", association.getId());
                startActivity(appel);
                //   getActivity().finish();

            }
        });
        // intListView();
        addChildAssociationListener();
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        retourAccueil();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        retourAccueil();
    }

    private void retourAccueil() {
        finish();

    }


    private void addChildAssociationListener() {
        FirebaseDatabase.getInstance().getReference().child("associations").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Association association = dataSnapshot.getValue(Association.class);
                updateAssociationList(association);
                associationAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Association association = dataSnapshot.getValue(Association.class);
                updateAssociationList(association);
                associationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Association association = dataSnapshot.getValue(Association.class);
                removeAssociationList(association);
                associationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateAssociationList(Association association) {

        //Met à jour si changement
        for (int f = 0; f < associationList.size(); f++) {
            if (association.getId().equals(associationList.get(f).getId())) {
                associationList.get(f).updateAssociation(association);
                return;


            }
        }
        // si non ajoute
        associationList.add(association);
    }

    private void removeAssociationList(Association association) {

        //Met à jour si changement
        for (int f = 0; f < associationList.size(); f++) {
            if (association.getId().equals(associationList.get(f).getId())) {
                associationList.remove(f);
                return;


            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ajouteassociation:
                Intent appel = new Intent(ListAssociationActivity.this, AddAssociationActivity.class);
                startActivity(appel);
                break;

        }
    }
}
