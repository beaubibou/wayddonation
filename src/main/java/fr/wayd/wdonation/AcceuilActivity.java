package fr.wayd.wdonation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


import fr.wayd.bean.Association;
import fr.wayd.bean.User;

public class AcceuilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private Spinner spinnerlistassociation;
    private Association assocationSelected = null;
    List<Association> associationList = new ArrayList<>();
    private TextView detail;
    private TextView nbrcliks;
    private TextView titre;
    private boolean anonyme;
    ArrayAdapter<Association> associationAdapter;
    private MenuItem menuadmin;
    private Menu menudrawer;
    private ImageView photoassociation;
    ProgressDialog mProgressDialog;
    private String android_id ;
    private String TAG="AcceuilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_acceuil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressDialog = ProgressDialog.show(this, "Patientez ...", "Chargement...", true);

        Intent intent = getIntent();
        anonyme = intent.getBooleanExtra("anonyme", false);
        spinnerlistassociation = findViewById(R.id.spinnerlistassociation);
        nbrcliks = findViewById(R.id.nbrclicks);
        detail = findViewById(R.id.detail);
        titre = findViewById(R.id.titre);
        photoassociation = findViewById(R.id.photoassociation);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        associationAdapter = new ArrayAdapter<Association>(getBaseContext(),R.layout.item_spinner, associationList);
        spinnerlistassociation.setAdapter(associationAdapter);
        Button don = findViewById(R.id.don);

        don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appel = new Intent(AcceuilActivity.this,
                        PubliciteActivity.class);
                appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                appel.putExtra("association", assocationSelected);
                appel.putExtra("anonyme", anonyme);
                startActivity(appel);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menudrawer = navigationView.getMenu();
        menuadmin = menudrawer.findItem(R.id.admin);
        menuadmin.setVisible(false);


        //       BusMessages.addBusMessageListener(this);

        if (mAuth.getCurrentUser() != null && anonyme == false)
            addUserEventListenerValue();

        if (mAuth.getCurrentUser() == null && anonyme)
            addAnonymeEventListenerValue();


        spinnerlistassociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assocationSelected = (Association) (spinnerlistassociation.getSelectedItem());
                updateUIassociation(assocationSelected);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        android_id= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

      addChildAssociationListener();
      ischildExist(android_id);

     // addChildClicksListener(android_id);


    }

    private void ischildExist(final String android_id) {
        FirebaseDatabase.getInstance().getReference().child("clicks").child(android_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Object valeur= dataSnapshot.getValue(Object.class);

               if (valeur==null)
                   FirebaseDatabase.getInstance().getReference().child("clicks").child(android_id).child(Long.toString(System.currentTimeMillis())).setValue("time");

                addChildClicksListener(android_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addChildClicksListener(String android_id) {
        FirebaseDatabase.getInstance().getReference().child("clicks").child(android_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

              String s1=dataSnapshot.getKey();
              System.out.println("noued "+s1);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

                // met à jour spinner
                updateAssociationList(association);
                associationAdapter.notifyDataSetChanged();

                // Met a jour ecran si l'association modifié est celle de mon choix
                if (association.getId().equals(assocationSelected.getId()))
                    updateUIassociation(association);
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

    private void removeAssociationList(Association association) {

        //Met à jour si changement
        for (int f = 0; f < associationList.size(); f++) {
            if (association.getId().equals(associationList.get(f).getId())) {
                associationList.remove(f);
                return;
            }
        }

    }

    private void updateUIassociation(Association association) {
        System.out.println("riiiiii" + photoassociation);
        nbrcliks.setText("" + Integer.toString(association.getNbrclik()));
        detail.setText(association.getDetail());
        titre.setText(association.getTitre());

        if (association.getUrlphoto()!=null &&!association.getUrlphoto().isEmpty())
        Picasso.get().load(association.getUrlphoto()).into(photoassociation);
        else
            photoassociation.setImageBitmap(null);
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


    private void addUserEventListenerValue() {
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getProfil() == User.ADMIN_WAYD) menuadmin.setVisible(true);
                udpateNavHedear(user);
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addAnonymeEventListenerValue() {
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers.child("users").child(getString(R.string.anonymeid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                udpateNavHedear(user);
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);  // "Hide" your current Activity
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acceuil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admin) {
            Intent appel = new Intent(AcceuilActivity.this,
                    ListAssociationActivity.class);
            appel.putExtra("id", "1527884712381");
            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);

        } else if (id == R.id.nav_share) {
            share();

        } else if (id == R.id.nav_deconnexion) {
            mAuth.signOut();
            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // @Override
    //public void onChangeUser (User user){

    //  udpateNavHedear(user);
    // }

    private void udpateNavHedear(User user) {

        View hView = navigationView.getHeaderView(0);
        ImageView photo = (ImageView) hView.findViewById(R.id.photo);
        TextView pseudo = (TextView) hView.findViewById(R.id.pseudo);
        TextView email = (TextView) hView.findViewById(R.id.email);
        TextView nbrdon = (TextView) hView.findViewById(R.id.nbrdon);
        pseudo.setText(user.getNom());
        email.setText(user.getMail());
        nbrdon.setText("Vos dons " + user.getNbrclik());

    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "http://play.google.com/store/apps/details?id=fr.wayd.wdonation";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wayd lien");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}
