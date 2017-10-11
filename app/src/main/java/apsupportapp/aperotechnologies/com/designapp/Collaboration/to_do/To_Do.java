package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.StockPullFragment;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.TransferRequestFragment;
import apsupportapp.aperotechnologies.com.designapp.R;

public class To_Do extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toobar;
    private ViewPager viewPager;
    private TabLayout tab;
    RelativeLayout ToDo_imageBtnBack;
    String userId, bearertoken, storeDescription, geoLeveLDesc, isMultiStore, value, storeCode,store_Code;
    SharedPreferences sharedPreferences;
    private TextView txtStoreCode,txtStoreName;
    public static String deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        deviceId = sharedPreferences.getString("device_id","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
        Log.e( "onCreate: ",""+deviceId );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_to_do);
        ToDo_imageBtnBack = (RelativeLayout)findViewById(R.id.toDo_imageBtnBack);
        txtStoreCode= (TextView)findViewById(R.id.txtStoreCode);
        txtStoreName = (TextView)findViewById(R.id.txtStoreName);
        if(getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("from") == null) {
                if (getIntent().getExtras().getString("storeCode") != null) {
                    storeCode = getIntent().getExtras().getString("storeCode");
                    store_Code = storeCode.substring(0, 4);

                    //  Log.i(TAG, "storeCode: "+storeCode );
                }
            }
            else
            {
                store_Code = getIntent().getExtras().getString("from");
            }
        }
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
//        txtStoreCode.setText(storeDescription.trim().substring(0, 4));
//        txtStoreName.setText(storeDescription.substring(5));
        ToDo_imageBtnBack.setOnClickListener(this);
        setSupportActionBar(toolbar);
        checkCollapsing();
   }


    private void checkCollapsing()
    {
        if(Build.VERSION.SDK_INT>=21)
        {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

           // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

           // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        viewPager=(ViewPager)findViewById(R.id.to_do_viewpager);
        setupViewPager(viewPager);
        tab = (TabLayout) findViewById(R.id.tabs_toDo);
        tab.setupWithViewPager(viewPager);
    }

    public static void StartIntent(Context c) {
        c.startActivity(new Intent(c, To_Do.class));
    }

    private void setupViewPager(ViewPager viewPager) {
        ToDoViewPagerAdapter adapter = new ToDoViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StockPullFragment(store_Code), "Pull from Stores");
        adapter.addFragment(new TransferRequestFragment(store_Code), "Requested by Stores");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.toDo_imageBtnBack :
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


