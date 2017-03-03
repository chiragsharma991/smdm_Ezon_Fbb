package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment.ToBeReceived;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment.ToBeTransfer;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.StockPullFragment;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.TransferRequestFragment;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.R;

public class StatusActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_status);
        setSupportActionBar(toolbar);

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



        viewPager=(ViewPager)findViewById(R.id.status_viewpager);
        setupViewPager(viewPager);
        tab = (TabLayout) findViewById(R.id.tabs_toDo);
        tab.setupWithViewPager(viewPager);
    }

    public static void StartIntent(Context c) {
        c.startActivity(new Intent(c, StatusActivity.class));
    }

    private void setupViewPager(ViewPager viewPager)
    {
        StatusViewPagerAdapter adapter = new StatusViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ToBeTransfer(), "TO BE TRANSFER");
        adapter.addFragment(new ToBeReceived(), "TO BE RECEIVED");
        viewPager.setAdapter(adapter);
    }
}
