package apsupportapp.aperotechnologies.com.designapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pamrutkar on 23/08/16.
 */
public class KeyProductActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnBack,btnSearch;
     String productData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_key_product);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
         productData=getIntent().getStringExtra("productNameBean");
        //setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Product Name"));
        tabLayout.addTab(tabLayout.newTab().setText("Option"));
        tabLayout.addTab(tabLayout.newTab().setText("SKU"));
        //tabLayout.getChildAt(3).setVisibility(View.GONE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);

        final KeyProductAdapter adapter = new KeyProductAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnBack=(Button)findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(this);
        btnSearch=(Button)findViewById(R.id.imageBtnSearch);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageBtnBack:
                Intent intent = new Intent(KeyProductActivity.this,DashBoardActivity.class);
                startActivity(intent);
                break;
            case R.id.imageBtnSearch:
                Intent intent1=new Intent(KeyProductActivity.this,SearchActivity.class);
                startActivity(intent1);
        }
    }
}
