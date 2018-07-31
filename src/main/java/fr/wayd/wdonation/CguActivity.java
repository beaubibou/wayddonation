package fr.wayd.wdonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import fr.wayd.bean.Click;
import fr.wayd.bean.Commun;

public class CguActivity extends AppCompatActivity {
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_cgu);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean cguvalide = getIntent().getBooleanExtra("validecgu", false);
        android_id = getIntent().getStringExtra("android_id");
        Button b_cguvalide = findViewById(R.id.validecgu);

        Button b_cgurefuse = findViewById(R.id.refusecgu);

        b_cgurefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        b_cguvalide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("terminaux").child(android_id).child(Commun.getDateNowStr()).setValue(new Click(0));
                finish();
                Intent appel = new Intent(CguActivity.this,
                        AcceuilActivity.class);

                appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(appel);
            }
        });
        if (cguvalide) {

            b_cguvalide.setVisibility(View.VISIBLE);
            b_cgurefuse.setVisibility(View.VISIBLE);


        }


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
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();

}

}
