package fr.wayd.wdonation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.wayd.bean.Probleme;

public class ProblemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probleme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouteProbleme();
            }
        });

    }

    private void ajouteProbleme() {

        TextView probleme = findViewById(R.id.probleme);
        String problemeStr = probleme.getText().toString();
        problemeStr = problemeStr.trim();
        if (problemeStr.isEmpty()) {
            Toast.makeText(this, "Saisissez votre problème", Toast.LENGTH_SHORT).show();
            return;
        }
        String time = Long.toString(System.currentTimeMillis());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Probleme prb = new Probleme(uid, problemeStr, time);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = uid + "-" + time;
        mDatabase.child("problemes").child(id).setValue(prb);
        finish();
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

}
