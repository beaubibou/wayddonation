package fr.wayd.wdonation;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.wayd.bean.Click;
import fr.wayd.bean.Commun;

public class ParametreActivity extends AppCompatActivity implements View.OnClickListener {
    private String uiduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button supprimercompte = findViewById(R.id.supprimecompte);
        uiduser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        supprimercompte.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supprimecompte:

                supprimeCompte();
                break;


        }
    }

    private void supprimeCompte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ParametreActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Suppression de votre compte? ");
        builder.setMessage("Vous aller supprimer votre compte. Cette action est irréversible. ");
        builder.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                effaceCompteFireBase();
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void effaceCompteFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("*************+" + uiduser);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                            mDatabase.child("users").child(uiduser).removeValue();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ParametreActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle("Information ");
                            builder.setMessage("Merci d'avoir utilisé wDonation. ");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAffinity();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        } else {

                            Toast.makeText(getBaseContext(), "Une erreur est survenue, déconnectez vous et reconnectez vous, et recommencer l'opération.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
