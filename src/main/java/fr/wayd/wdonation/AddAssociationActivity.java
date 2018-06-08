package fr.wayd.wdonation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.wayd.bean.Association;
import fr.wayd.bean.User;
import fr.wayd.dao.Dao_Associations;

public class AddAssociationActivity extends AppCompatActivity implements View.OnClickListener {

    private String idAssociaton = null;
    private Button ajoute;
    private Button supprime;
    private EditText nom;
    private EditText email;
    private EditText titre;
    private EditText detail;
    private EditText lienfacebook;
    private EditText lienurlsite,idapplication,idcampagne;
    private EditText photourl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_association);
        ajoute = findViewById(R.id.ajoute);
        ajoute.setOnClickListener(this);
        supprime = findViewById(R.id.supprime);
        supprime.setOnClickListener(this);
        Intent intent = getIntent();
        idAssociaton = intent.getStringExtra("id");
        System.out.println("id" + idAssociaton);
        nom = findViewById(R.id.nom);
        email = findViewById(R.id.email);
        detail = findViewById(R.id.detail);
        titre = findViewById(R.id.titre);
        photourl= findViewById(R.id.photourl);
        idapplication = findViewById(R.id.idapplication);
        idcampagne = findViewById(R.id.idcampagne);
        lienfacebook = findViewById(R.id.lienfacebook);
        lienurlsite = findViewById(R.id.liensurlite);

        if (idAssociaton == null) {
            modeAjout();
        } else {
            modeModification();
        }
    }

    private void modeModification() {

        ajoute.setText("Modifie");
        supprime.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference()
                .child("associations").child(idAssociaton).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Association association = dataSnapshot.getValue(Association.class);
                System.out.println(association);
                updateUIassociation(association);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateUIassociation(Association association) {

        nom.setText(association.getNom());
        email.setText(association.getMail());
        detail.setText(association.getDetail());
        lienfacebook.setText(association.getLienFacebook());
        lienurlsite.setText(association.getLienSitePerso());
        idapplication.setText(association.getIdapplication());
        idcampagne.setText(association.getIdcampagne());
        titre.setText(association.getTitre());
        photourl.setText(association.getUrlphoto());

    }

    private void modeAjout() {
        ajoute.setText("Ajoute");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ajoute:
                ajouteAssociation();
                finish();
                break;

            case R.id.supprime:
                System.out.println("remvoeeeeeeee");
                supprimeAssociation();
               finish();
                break;
        }
    }

    private void supprimeAssociation() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
         mDatabase.child("associations").child(idAssociaton).removeValue();
    }


    private void ajouteAssociation() {

        // Si l'id association est null on génére un id
        if (idAssociaton == null) {
            idAssociaton = Long.toString(System.currentTimeMillis());

            AddAssociation(idAssociaton, nom.getText().toString(), email.getText().toString(), detail.getText().toString(),
                    lienfacebook.getText().toString(), lienurlsite.getText().toString(),idapplication.getText().toString(),
                    idcampagne.getText().toString(),titre.getText().toString(),photourl.getText().toString());
        }
        else{

            updateAssociation(idAssociaton, nom.getText().toString(), email.getText().toString(), detail.getText().toString(),
                    lienfacebook.getText().toString(), lienurlsite.getText().toString(),idapplication.getText().toString(),
                    idcampagne.getText().toString(),titre.getText().toString(),photourl.getText().toString());

        }
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }
    public  void AddAssociation(String id, String nom, String mail, String detail, String lienFacebook, String lienSitePerso,String idapplication,String idcampagne,String titre,String photourl) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Association association = new Association(id,nom, mail, detail, lienFacebook, lienSitePerso,idapplication,idcampagne,titre,photourl);
        mDatabase.child("associations").child(id).setValue(association);

    }

    public  void updateAssociation(String id, String nom, String mail, String detail, String lienFacebook, String lienSitePerso,String idapplication,String idcampagne,String titre,String urlphoto) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("associations").child(id).child("nom").setValue(nom);
        mDatabase.child("associations").child(id).child("mail").setValue(mail);
        mDatabase.child("associations").child(id).child("detail").setValue(detail);
        mDatabase.child("associations").child(id).child("lienFacebook").setValue(lienFacebook);
        mDatabase.child("associations").child(id).child("lienSitePerso").setValue(lienSitePerso);
        mDatabase.child("associations").child(id).child("idapplication").setValue(idapplication);
        mDatabase.child("associations").child(id).child("idcampagne").setValue(idcampagne);
        mDatabase.child("associations").child(id).child("titre").setValue(titre);
        mDatabase.child("associations").child(id).child("urlphoto").setValue(urlphoto);
    }

}
