package fr.wayd.wdonation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;

import fr.wayd.bean.Association;
import fr.wayd.bean.Commun;
import fr.wayd.dao.Dao_Associations;
import fr.wayd.dao.Dao_Terminaux;
import fr.wayd.dao.Dao_Users;

public class PubliciteActivity extends AppCompatActivity implements RewardedVideoAdListener, View.OnClickListener {

    ProgressDialog mProgressDialog;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean donok = false;
    private FirebaseAuth mAuth;
    private String idApplication = "ca-app-pub-3940256099942544/5224354917";
    private String idBlockReward = "ca-app-pub-3940256099942544/5224354917";
    // private boolean anonyme;
    Button ouipartage;
    Button nonpartage;
    TextView text_mercidon;
    private String android_id;
    private Association associationselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publicite);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        MobileAds.initialize(this,
                idApplication);
        Intent intent = getIntent();
        associationselected = intent.getParcelableExtra("association");

        idApplication = associationselected.getIdapplication();
        idBlockReward = associationselected.getIdcampagne();
        //    anonyme=intent.getBooleanExtra("anonyme",false);
        text_mercidon = findViewById(R.id.text_mercidon);
        ouipartage = findViewById(R.id.ouipartage);
        nonpartage = findViewById(R.id.nonpartage);
        ouipartage.setOnClickListener(this);
        nonpartage.setOnClickListener(this);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = ProgressDialog.show(this, "Patientez ...", "Chargement du don...", true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                retourAccueil();
            }
        });
        mProgressDialog.setCancelable(true);
        loadRewardedVideoAd();

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mProgressDialog.dismiss();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (!donok)
            retourAccueil();


    }

    @Override
    public void onRewarded(RewardItem rewardItem) {


        Dao_Users.addClick(mAuth.getCurrentUser().getUid());


        Dao_Associations.addClickTransaction(associationselected.getId());
        Dao_Terminaux.addClickTerminal(android_id);

        donok = true;
        Toast.makeText(this, "Votre don est  pris en compte", Toast.LENGTH_SHORT).show();
        // text_mercidon.setVisibility(View.VISIBLE);
        //ouipartage.setVisibility(View.VISIBLE);
        //nonpartage.setVisibility(View.VISIBLE);
        showAlertDialog();
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PubliciteActivity.this);
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
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "Votre don n'est pas pris en compte", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "Echec de chargement", Toast.LENGTH_SHORT).show();
        retourAccueil();
    }

    @Override
    public void onRewardedVideoCompleted() {
        donok = true;

    }


    private void loadRewardedVideoAd() {

        mRewardedVideoAd.loadAd(idBlockReward,
                new AdRequest.Builder().build());
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void retourAccueil() {
        finish();
        mProgressDialog.dismiss();
        mRewardedVideoAd.destroy(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        retourAccueil();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ouipartage:
                share();
                break;
            case R.id.nonpartage:
                retourAccueil();
                break;
        }
    }

    private void share() {
        finish();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = Commun.getMessagePartage(associationselected);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,  getString(R.string.titrepartage));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

    }
}
