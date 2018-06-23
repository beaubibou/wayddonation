package fr.wayd.wdonation;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AproposActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int   versionCode = pInfo.versionCode;
        String name=pInfo.versionName;
        TextView version = findViewById(R.id.version);
        TextView cgu = findViewById(R.id.cgu);
        TextView regleconfidence = findViewById(R.id.regleconfidence);
        cgu.setOnClickListener(this);
        regleconfidence.setOnClickListener(this);
        String versionStr=name+"."+versionCode;

        version.setText(versionStr);

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
        int id = v.getId();

        if (id == R.id.cgu) {
            Intent appel = new Intent(AproposActivity.this,
                    CguActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);

            finish();
        }

        if (id == R.id.regleconfidence) {
            Intent appel = new Intent(AproposActivity.this,
                    RegleConfidenceActivity.class);

            appel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(appel);

            finish();
        }

    }
}
