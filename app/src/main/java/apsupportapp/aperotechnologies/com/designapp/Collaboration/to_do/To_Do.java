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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.StockPullFragment;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.TransferRequestFragment;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class To_Do extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toobar;
    private ViewPager viewPager;
    private TabLayout tab;
    private RelativeLayout ToDo_imageBtnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        initialise();
       // getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_to_do);
        setSupportActionBar(toolbar);
        checkCollapsing();




    }

    private void initialise()
    {
        ToDo_imageBtnBack=(RelativeLayout)findViewById(R.id.toDo_imageBtnBack);
        ToDo_imageBtnBack.setOnClickListener(this);
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
        adapter.addFragment(new StockPullFragment(), "STOCK PULL");
        adapter.addFragment(new TransferRequestFragment(), "TRANSFER REQUEST");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View view)
    {
        finish();
    }
}


