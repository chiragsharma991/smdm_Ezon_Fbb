package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;


public class AboutUsActivity extends AppCompatActivity {

    RelativeLayout btnBack;
    TextView txtversioname,txtversioncode;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.about_us);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        txtversioname = (TextView) findViewById(R.id.versioname);
        txtversioncode = (TextView) findViewById(R.id.versioncode);
        context = this;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = pInfo.versionName;
        int versionCode = pInfo.versionCode;
        txtversioname.setText("VersionName "+version);
        txtversioncode.setText("VersionCode "+versionCode);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, SnapDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AboutUsActivity.this, SnapDashboardActivity.class);
        startActivity(intent);
        finish();

    }
}
