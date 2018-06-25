package fr.wayd.wdonation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import fr.wayd.bean.Click;
import fr.wayd.bean.Commun;
import fr.wayd.bean.Configuration;
import fr.wayd.bean.User;
import fr.wayd.dao.Dao_Associations;
import fr.wayd.dao.Dao_Terminaux;
import fr.wayd.dao.Dao_Users;

public class AcceuilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, RewardedVideoAdListener {

    private int nbrMaxClicsAutorise = 4;
    DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private Spinner spinnerlistassociation;
    private Association assocationSelected = null;
    List<Association> associationList = new ArrayList<>();
    private TextView detail;
    private TextView nbrcliks;
    private TextView titre;
    //private boolean anonyme;
    ArrayAdapter<Association> associationAdapter;
    private MenuItem menuadmin, configurationapplication;
    private MenuItem menuparametre;
    private Menu menudrawer;
    private ImageView photoassociation;
    ProgressDialog mProgressDialog;
    private String android_id;
    private TextView ensavoirplus, totalclickjour;
    private String TAG = "AcceuilActivity";
    private int nbrtotalclicksjours = 0;
    private Button don;
    private int nbrTentative = 0;

    private boolean donok = false;
    private int versionStore;
    private int versionCode;
    private RewardedVideoAd mRewardedVideoAd;


    private String idApplication = "ca-app-pub-3940256099942544/5224354917";
    private String idBlockReward = "ca-app-pub-3940256099942544/5224354917";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this,
                idApplication);
        setContentView(R.layout.activity_acceuil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressDialog = ProgressDialog.show(this, "Patientez ...", "Chargement...", true);

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //    mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
//        mRewardedVideoAd.setRewardedVideoAdListener(this);

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCode = pInfo.versionCode;

        readConfiguration();

        spinnerlistassociation = findViewById(R.id.spinnerlistassociation);
        nbrcliks = findViewById(R.id.nbrclicks);
        detail = findViewById(R.id.detail);
        titre = findViewById(R.id.titre);
        photoassociation = findViewById(R.id.photoassociation);
        ensavoirplus = findViewById(R.id.ensavoirplus);
        totalclickjour = findViewById(R.id.totalclickjour);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        associationAdapter = new ArrayAdapter<Association>(getBaseContext(), R.layout.item_spinner, associationList);
        spinnerlistassociation.setAdapter(associationAdapter);
        don = findViewById(R.id.don);

        don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Intent appel = new Intent(AcceuilActivity.this,
                //          PubliciteActivity.class);
                //appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //appel.putExtra("association", assocationSelected);
                //     appel.putExtra("anonyme", anonyme);
                //startActivity(appel);
                if (mRewardedVideoAd != null && mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });

        ensavoirplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ouvrePageFaceBook();
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ouvrePageFaceBook();
            }
        });
        titre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ouvrePageFaceBook();
            }
        });
        photoassociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ouvrePageFaceBook();
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
        menuparametre = menudrawer.findItem(R.id.parametre);
        configurationapplication = menudrawer.findItem(R.id.configurationapplication);


        //       BusMessages.addBusMessageListener(this);

        // if (mAuth.getCurrentUser() != null && anonyme == false)
        addUserEventListenerValue();


        spinnerlistassociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assocationSelected = (Association) (spinnerlistassociation.getSelectedItem());
                nbrTentative = 0;
                donok = false;
                updateUIassociation(assocationSelected);
                idApplication = assocationSelected.getIdapplication();
                idBlockReward = assocationSelected.getIdcampagne();
                loadRewardedVideoAd();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        addConfigurationValueListener();
        addChildAssociationListener();
        initNoeudTerminaux(android_id);


    }

    private void ouvrePageFaceBook() {
        if (assocationSelected.getLienFacebook() == null || assocationSelected.getLienFacebook().isEmpty())
            return;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(assocationSelected.getLienFacebook()));
        startActivity(i);
    }

    private void addConfigurationValueListener() {
        FirebaseDatabase.getInstance().getReference().child("configuration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configuration configuration = dataSnapshot.getValue(Configuration.class);
                nbrMaxClicsAutorise = configuration.getNbrclicksjourmax();
                updateUInbrCLicksTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadRewardedVideoAd() {
        don.setText(getString(R.string.donencours));
        if (mRewardedVideoAd != null)
            mRewardedVideoAd.destroy(this);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(idBlockReward,
                new AdRequest.Builder().build());
    }

    private void initNoeudTerminaux(final String android_id) {
        FirebaseDatabase.getInstance().getReference().child("terminaux").child(android_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object valeur = dataSnapshot.getValue(Object.class);
                mProgressDialog.dismiss();
                if (valeur == null)
                    testCGU(android_id);
                else
                    addChildTerminauxListener(android_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addChildTerminauxListener(String android_id) {
        FirebaseDatabase.getInstance().getReference().child("terminaux").child(android_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String nowStr = Commun.getDateNowStr();

                if (nowStr.equals(dataSnapshot.getKey())) {
                    Click click = dataSnapshot.getValue(Click.class);
                    if (click != null) nbrtotalclicksjours = click.getNbr();

                }
                updateUInbrCLicksTotal();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String nowStr = Commun.getDateNowStr();
                if (nowStr.equals(dataSnapshot.getKey())) {
                    Click click = dataSnapshot.getValue(Click.class);
                    if (click != null) nbrtotalclicksjours = click.getNbr();
                }
                updateUInbrCLicksTotal();
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

    private void updateUInbrCLicksTotal() {

        int reste = nbrMaxClicsAutorise - nbrtotalclicksjours;
        String texte;
        if (reste > 1)
            texte = " dons restants pour la journée";
        else
            texte = " don restant pour la journée";

        if (nbrtotalclicksjours < nbrMaxClicsAutorise) {
            totalclickjour.setText(Integer.toString(reste) + texte);
        //    don.setVisibility(View.VISIBLE);
        }
        else {
            totalclickjour.setText("Vous avez utilisé tous vos droits à dons.");
            don.setVisibility(View.GONE);
        }
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
        nbrcliks.setText("" + Integer.toString(association.getNbrclik()));
        detail.setText(association.getDetail());
        titre.setText(association.getTitre());

        if (association.getLienFacebook() != null && !association.getLienFacebook().isEmpty())
            ensavoirplus.setVisibility(View.VISIBLE);

        if (association.getUrlphoto() != null && !association.getUrlphoto().isEmpty())
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
                udpateNavHedear(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void testCGU(final String android_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AcceuilActivity.this);
        builder.setCancelable(false);
        String text = "Conditions générales d'utilisation " + "/n" + getString(R.string.cgu) + "/n" + "Politique de confidentialité" + "/n" + getString(R.string.politiqueconfidentialite);
        builder.setTitle("Condition générale d'utilisation - Politique de confidentialité ");
        builder.setMessage(text);
        builder.setPositiveButton("Accepter", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("terminaux").child(android_id).child(Commun.getDateNowStr()).setValue(new Click(0));
                addChildTerminauxListener(android_id);
            }
        });

        builder.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                mAuth.signOut();
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


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

        //getMenuInflater().inflate(R.menu.acceuil, menu);
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

        } else if (id == R.id.configurationapplication) {
            Intent appel = new Intent(AcceuilActivity.this,
                    ConfigurationActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);

        } else if (id == R.id.leconcept) {
            Intent appel = new Intent(AcceuilActivity.this,
                    ConceptActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);

        } else if (id == R.id.unprobleme) {
            Intent appel = new Intent(AcceuilActivity.this,
                    ProblemeActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);

        } else if (id == R.id.apropos) {
            Intent appel = new Intent(AcceuilActivity.this,
                    AproposActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);
        } else if (id == R.id.parametre) {
            Intent appel = new Intent(AcceuilActivity.this,
                    ParametreActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void udpateNavHedear(User user) {

        if (user == null)
            return;

        if (user.getProfil() == User.ADMIN_WAYD) {
            menuadmin.setVisible(true);
            configurationapplication.setVisible(true);
        } else {
            menuadmin.setVisible(false);
            configurationapplication.setVisible(false);
        }

        View hView = navigationView.getHeaderView(0);
        ImageView photo = (ImageView) hView.findViewById(R.id.photo);
        TextView pseudo = (TextView) hView.findViewById(R.id.pseudo);
        TextView email = (TextView) hView.findViewById(R.id.email);
        TextView nbrdon = (TextView) hView.findViewById(R.id.nbrdon);
        pseudo.setText(user.getNom());
        email.setText(user.getMail());
        if (!mAuth.getCurrentUser().getUid().equals(getString(R.string.idanonyme)))
            menuparametre.setVisible(true);
        else
            menuparametre.setVisible(false);


        if (mAuth.getCurrentUser().getUid().equals(getString(R.string.idanonyme)))
            nbrdon.setText("Total des dons anonymes " + user.getNbrclik());
        else
            nbrdon.setText("Total de vos dons " + user.getNbrclik());
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = Commun.getMessagePartage(assocationSelected);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.titrepartage));
        // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Fais comme moi.....</p>" + "<br>" +
                "<a href='http://play.google.com/store/apps/details?id=fr.wayd.wdonation'>http://play.google.com/store/apps/details?id=fr.wayd.wdonation</a>"));

        startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    private void readConfiguration() {
        FirebaseDatabase.getInstance().getReference().child("configuration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configuration configuration = dataSnapshot.getValue(Configuration.class);
                versionStore = configuration.getVersionstore();

                if (!testVersion()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AcceuilActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Mise à jour ");
                    builder.setMessage("Votre version n'est pas à jour");
                    builder.setPositiveButton("Mettre à jour", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=fr.wayd.wdonation"));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAuth.signOut();
                            dialog.dismiss();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean testVersion() {


        if (versionCode < versionStore) {
            //     Toast.makeText(getBaseContext(), "Votre version n'est pas à jour", Toast.LENGTH_SHORT).show();
            return false;

        }


        return true;

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        don.setText(getString(R.string.donok));
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (!donok) {
            messageDonPasPrisEnCompte();

        }
        nbrTentative = 0;
        donok = false;
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Dao_Users.addClick(mAuth.getCurrentUser().getUid());
        Dao_Associations.addClickTransaction(assocationSelected.getId());
        Dao_Terminaux.addClickTerminal(android_id);
        donok = true;
        Toast toast = Toast.makeText(this, "Votre don est  pris en compte. Vous pouvez quitter la vidéo.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        showAlertDialog();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //   messageDonPasPrisEnCompte();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        nbrTentative++;

        if (nbrTentative > 20) {
            don.setText(getString(R.string.donechec));
            nbrTentative = 0;
        } else {
            don.setText(getString(R.string.donencours) + nbrTentative);
            loadRewardedVideoAd();
        }

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private void messageDonPasPrisEnCompte() {
        Toast toast = Toast.makeText(this, "Votre don n'a pas été pris en compte", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AcceuilActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Merci pour votre don");
        builder.setMessage("Voulez-vous partager votre bonne action ?");
        builder.setPositiveButton("Partager", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                share();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Non merci", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
