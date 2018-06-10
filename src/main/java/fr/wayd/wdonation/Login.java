package fr.wayd.wdonation;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.wayd.bean.Click;
import fr.wayd.bean.Commun;
import fr.wayd.bean.Configuration;
import fr.wayd.dao.Dao_Users;

public class Login extends AppCompatActivity implements  View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN=9001;
    private FirebaseAuth mAuth;
    private String TAG="MainActivity";
    private int versionStore;
    private int versionCode;
 //   public static  String idAnonyme="1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //String version = pInfo.versionName;
        versionCode = pInfo.versionCode;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        SignInButton   mSignInButton = findViewById(R.id.google_sign_in_button);
        Button anonyme=  findViewById(R.id.anonyme);
        anonyme.setOnClickListener(this);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setOnClickListener(this);
        readConfiguration();


    }

    private void readConfiguration() {
        FirebaseDatabase.getInstance().getReference().child("configuration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configuration configuration = dataSnapshot.getValue(Configuration.class);
                versionStore = configuration.getVersionstore();

              if ( testVersion()){
                  FirebaseUser currentUser = mAuth.getCurrentUser();
                  updateUI(currentUser);


              }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean testVersion() {


        if (versionCode<versionStore){
           Toast.makeText(getBaseContext(), "Votre version n'est pas à jour", Toast.LENGTH_SHORT).show();
           return false;

        }


        return true;

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.google_sign_in_button){
         if (testVersion())
            signIn();
            }

        if (id == R.id.anonyme){
          if (testVersion())
            signInAnonyme();
        }


    }

    private void signInAnonyme() {
        Dao_Users.AddUser( getString(R.string.anonymeid),"Anonyme","Anonyme");
        Intent  appel = new Intent(Login.this,
                AcceuilActivity.class);
        appel.putExtra("anonyme",true);
        appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(appel);

    }


    private void updateUI(FirebaseUser currentUser) {

        if (currentUser!=null){
          Dao_Users.AddUser(currentUser.getUid(),currentUser.getDisplayName(),currentUser.getEmail());
           Intent  appel = new Intent(Login.this,
                    AcceuilActivity.class);
          appel.putExtra("anonyme",false);
          appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
          startActivity(appel);
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
       // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                        //    Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                 //       hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
              //  updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }


    // [END onactivityresult]
}
