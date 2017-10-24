package apsupportapp.aperotechnologies.com.designapp.ExternalServiceOppAudit;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.InspectionHistoryZonalRatings;

public class ExternalHistoryActivity extends AppCompatActivity {

    private Context context;
    private RelativeLayout imageBtnBack1;
    TabLayout tab_fg_competitor_store, tab_fg_competitor_storeupdates;
    CheckBox chckbox_All, chckbox_rejected, chckbox_Approved, chckbox_underreview;
    String update_of_yes_week_mon = "Yesterday";
    String rev_selection = "All";
    List<InspectionHistoryZonalRatings> arr_zonalratings;
    ListView list_externalauditorreview;
    ExternalHistoryAdapter externalHistoryAdapter;
    String store_type, tab = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_history);
        getSupportActionBar().hide();
        context = this;
        store_type = "FgStore";
        arr_zonalratings = new ArrayList<>();

        tab_fg_competitor_store = (TabLayout) findViewById(R.id.tab_fg_competitor_store);
        tab_fg_competitor_store.addTab(tab_fg_competitor_store.newTab().setText("FG Store"));
        tab_fg_competitor_store.addTab(tab_fg_competitor_store.newTab().setText("Competitor Store"));

        tab_fg_competitor_storeupdates = (TabLayout) findViewById(R.id.tab_fg_competitor_storeupdates);
        tab_fg_competitor_storeupdates.addTab(tab_fg_competitor_storeupdates.newTab().setText("Yesterday"));
        tab_fg_competitor_storeupdates.addTab(tab_fg_competitor_storeupdates.newTab().setText("Last Week"));
        tab_fg_competitor_storeupdates.addTab(tab_fg_competitor_storeupdates.newTab().setText("Last Month"));

        chckbox_All = (CheckBox) findViewById(R.id.chckbox_All);
        chckbox_rejected = (CheckBox) findViewById(R.id.chckbox_rejected);
        chckbox_Approved = (CheckBox) findViewById(R.id.chckbox_Approved);
        chckbox_underreview = (CheckBox) findViewById(R.id.chckbox_underreview);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);

        list_externalauditorreview = (ListView) findViewById(R.id.list_externalauditorreview);
        arr_zonalratings = new ArrayList<>();
        externalHistoryAdapter = new ExternalHistoryAdapter(arr_zonalratings, context, list_externalauditorreview, tab);
        list_externalauditorreview.setAdapter(externalHistoryAdapter);
        setLastWeek("FBB 001", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        chckbox_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chckbox_All.setChecked(true);
                chckbox_rejected.setChecked(false);
                chckbox_Approved.setChecked(false);
                chckbox_underreview.setChecked(false);
                rev_selection = "All";
                Log.e("chckbox_All click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                setListAdpter("");
                setLastWeek("FBB 006", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });

        chckbox_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chckbox_rejected.setChecked(true);
                chckbox_All.setChecked(false);
                chckbox_Approved.setChecked(false);
                chckbox_underreview.setChecked(false);
                rev_selection = "Rejected";
                Log.e("chckbox_Pending click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                setListAdpter("");
                setLastWeek("FBB 007", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });

        chckbox_Approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chckbox_Approved.setChecked(true);
                chckbox_rejected.setChecked(false);
                chckbox_All.setChecked(false);
                chckbox_underreview.setChecked(false);
                rev_selection = "Approved";
                Log.e("chckbox_Approved click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                setListAdpter("");
                setLastWeek("FBB 008", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });

        chckbox_underreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chckbox_underreview.setChecked(true);
                chckbox_rejected.setChecked(false);
                chckbox_All.setChecked(false);
                chckbox_Approved.setChecked(false);
                rev_selection = "Under Review";
                Log.e("chckbox_Approved click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                setListAdpter("");
                setLastWeek("FBB 009", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });


        tab_fg_competitor_store.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int check_pos = tab.getPosition();
                if(check_pos == 0)
                {
                    Log.e("here ","0");
                    store_type = "FgStore";
                    setListAdpter("FgStore");
                    Log.e("tab1 click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                    setLastWeek("FBB 001", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");
                }
                else
                {
                    Log.e("here ","1");
                    store_type = "CompetitorStore";
                    setListAdpter("CompetitorStore");
                    Log.e("tab2 click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                    setLastWeek("FBB 002", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab_fg_competitor_storeupdates.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int check_pos = tab.getPosition();
                if(check_pos == 0)
                {
                    update_of_yes_week_mon = "Yesterday";
                    Log.e("Yesterday click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                    setListAdpter("");
                    setLastWeek("FBB 003", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

                }
                else if(check_pos == 1)
                {
                    update_of_yes_week_mon = "Last Week";
                    Log.e("Last Week click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                    setListAdpter("");
                    setLastWeek("FBB 004", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

                }
                else if(check_pos == 2)
                {
                    update_of_yes_week_mon = "Last Month";
                    Log.e("Last Month click ", " "+rev_selection+" "+update_of_yes_week_mon+" "+store_type);
                    setListAdpter("");
                    setLastWeek("FBB 005", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void setListAdpter(String tab)
    {
        arr_zonalratings = new ArrayList<>();
        externalHistoryAdapter = new ExternalHistoryAdapter(arr_zonalratings, context, list_externalauditorreview, tab);
        list_externalauditorreview.setAdapter(externalHistoryAdapter);
    }

    public  void setLastWeek(String txt1, String txt2, String txt3, String txt4)
    {
        InspectionHistoryZonalRatings inspectionHistoryZonalRatings = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings.setZone_name(txt1);
        inspectionHistoryZonalRatings.setAvg_rating(txt2);
        inspectionHistoryZonalRatings.setAudit_done(txt3);
        inspectionHistoryZonalRatings.setCount_fg_audit(txt4);


        arr_zonalratings.add(inspectionHistoryZonalRatings);

        Log.e("--- "," "+arr_zonalratings.get(0).getZone_name());
        externalHistoryAdapter.notifyDataSetChanged();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
