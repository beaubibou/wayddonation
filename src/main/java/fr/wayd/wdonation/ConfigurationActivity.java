package fr.wayd.wdonation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.wayd.bean.Association;
import fr.wayd.bean.Configuration;

public class ConfigurationActivity extends AppCompatActivity {

    private TextView nbrclicsmaxparjour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nbrclicsmaxparjour = findViewById(R.id.nbrclicsmaxparjour);
        getValueConfiguration();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        updateConfiguration();
            }
        });
    }

    private void updateConfiguration() {
       int nbrmaxjour=Integer.valueOf(nbrclicsmaxparjour.getText().toString());
       FirebaseDatabase.getInstance().getReference().child("configuration").child("nbrclicksjourmax").setValue(nbrmaxjour);
    }


    public void getValueConfiguration() {
        FirebaseDatabase.getInstance().getReference()
                .child("configuration").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configuration configuration = dataSnapshot.getValue(Configuration.class);

                updateUIconfiguration(configuration);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateUIconfiguration(Configuration configuration) {
        nbrclicsmaxparjour.setText(Integer.toString(configuration.getNbrclicksjourmax()));
    }
}
