package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.InspectionHistoryZonalRatings;


/**
 * Created by hasai on 10/10/17.
 */

public class ExternalAudReviewActivity  extends AppCompatActivity {

    private Context context;
    TabLayout tab_fg_competitor_store, tab_fg_competitor_storeupdates;
    CheckBox chckbox_All, chckbox_Pending, chckbox_Approved;
    String update_of_yes_week_mon = "Yesterday";
    String rev_selection = "All";
    List<InspectionHistoryZonalRatings> arr_zonalratings;
    ListView list_externalauditorreview;
    ExternalAuditReviewAdapter adp_externalauditorreview;
    String store_type;
    private RelativeLayout imageBtnBack1;
    private String tab = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_externalaudrev);
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
        chckbox_Pending = (CheckBox) findViewById(R.id.chckbox_Pending);
        chckbox_Approved = (CheckBox) findViewById(R.id.chckbox_Approved);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);

        list_externalauditorreview = (ListView) findViewById(R.id.list_externalauditorreview);
        arr_zonalratings = new ArrayList<>();
        adp_externalauditorreview = new ExternalAuditReviewAdapter(arr_zonalratings, context, list_externalauditorreview, tab);
        list_externalauditorreview.setAdapter(adp_externalauditorreview);
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
                chckbox_Pending.setChecked(false);
                chckbox_Approved.setChecked(false);
                rev_selection = "All";

                setListAdpter("");
                setLastWeek("FBB 006", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });

        chckbox_Pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chckbox_Pending.setChecked(true);
                chckbox_All.setChecked(false);
                chckbox_Approved.setChecked(false);
                rev_selection = "Pending";

                setListAdpter("");
                setLastWeek("FBB 007", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });

        chckbox_Approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chckbox_Approved.setChecked(true);
                chckbox_Pending.setChecked(false);
                chckbox_All.setChecked(false);
                rev_selection = "Approved";

                setListAdpter("");
                setLastWeek("FBB 008", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

            }
        });


        tab_fg_competitor_store.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int check_pos = tab.getPosition();
                if(check_pos == 0)
                {
                    store_type = "FgStore";
                    setListAdpter("FgStore");
                    setLastWeek("FBB 001", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");
                }
                else
                {
                    store_type = "CompetitorStore";
                    setListAdpter("CompetitorStore");
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
                    setListAdpter("");
                    setLastWeek("FBB 003", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

                }
                else if(check_pos == 1)
                {
                    update_of_yes_week_mon = "Last Week";
                    setListAdpter("");
                    setLastWeek("FBB 004", "FBB ABC Mall","Kandiwali Mumbai 400088","Thursday, August 31, 2017");

                }
                else if(check_pos == 2)
                {
                    update_of_yes_week_mon = "Last Month";
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
        adp_externalauditorreview = new ExternalAuditReviewAdapter(arr_zonalratings, context, list_externalauditorreview, tab);
        list_externalauditorreview.setAdapter(adp_externalauditorreview);
    }

    public  void setLastWeek(String txt1, String txt2, String txt3, String txt4)
    {
        InspectionHistoryZonalRatings inspectionHistoryZonalRatings = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings.setZone_name(txt1);
        inspectionHistoryZonalRatings.setAvg_rating(txt2);
        inspectionHistoryZonalRatings.setAudit_done(txt3);
        inspectionHistoryZonalRatings.setCount_fg_audit(txt4);


        arr_zonalratings.add(inspectionHistoryZonalRatings);

        adp_externalauditorreview.notifyDataSetChanged();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
