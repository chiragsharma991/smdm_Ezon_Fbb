package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment.ToBeReceiver;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment.ToBeSender;
import apsupportapp.aperotechnologies.com.designapp.R;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tab;
    private Context context;
    private TextView txtStoreName, txtStoreCode;
    RelativeLayout status_imageBtnBack;
    public static ProgressBar StatusProcess;
    private String store, isMultiStore, value, storeCode, store_Code;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_status);
        setSupportActionBar(toolbar);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
        if(getIntent().getExtras().getString("storeCode") != null )
        {
            storeCode = getIntent().getExtras().getString("storeCode");
            store_Code = storeCode.substring(0,4);

            Log.e( "storeCode : in status activity", " " +store_Code );
        }
        initialise();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        }


        viewPager = (ViewPager) findViewById(R.id.status_viewpager);
        setupViewPager(viewPager);
        tab = (TabLayout) findViewById(R.id.tabs_toDo);
        tab.setupWithViewPager(viewPager);
        tab.setSelectedTabIndicatorColor(Color.parseColor("#e8112d"));

    }

    private void initialise() {

        status_imageBtnBack = (RelativeLayout) findViewById(R.id.status_imageBtnBack);
        StatusProcess = (ProgressBar) findViewById(R.id.statusProcess);
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreName = (TextView) findViewById(R.id.txtStoreName);

        StatusProcess.setVisibility(View.GONE);
        if(isMultiStore.equals("Yes"))
        {
            txtStoreCode.setText("Concept : ");
            txtStoreName.setText(value);

        }
        else
        {
            txtStoreCode.setText("Store : ");
            txtStoreName.setText(value);
        }
     //   store = sharedPreferences.getString("storeDescription", "");
//        SelectedStoreCode = store.trim().substring(0, 4);
   //     Log.e("store"," "+store);
   //     storedescription.setText(store);
        status_imageBtnBack.setOnClickListener(this);
    }

    public static void StartIntent(Context c) {
        c.startActivity(new Intent(c, StatusActivity.class));
    }

    private void setupViewPager(ViewPager viewPager) {
        StatusViewPagerAdapter adapter = new StatusViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ToBeSender(store_Code), "Transfer Status");
        adapter.addFragment(new ToBeReceiver(store_Code), "Receive Status");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_imageBtnBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
