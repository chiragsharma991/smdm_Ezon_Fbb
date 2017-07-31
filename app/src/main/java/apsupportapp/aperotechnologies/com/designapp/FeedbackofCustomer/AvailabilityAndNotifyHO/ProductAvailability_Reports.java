package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 24/07/17.
 */

public class ProductAvailability_Reports extends Fragment implements TabLayout.OnTabSelectedListener {
    private Context context;
    private ReportInterface mCallback;
    private View v;
    private RecyclerView listview;
    private TextView storedesc;
    private PieChart pieChart;
    private TabLayout Tabview;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String userId,store,bearertoken,geoLeveLDesc;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
           // mCallback = (ReportInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        View view =inflater.inflate(R.layout.fragment_productavailability_reports, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         v = getView();
         initialiseUI();
    }

    private void initialiseUI(){
        listview = (RecyclerView)v.findViewById(R.id.listView);
        storedesc = (TextView)v.findViewById(R.id.txtStoreCode);
        pieChart = (PieChart)v.findViewById(R.id.cf_pieChart);
        Tabview = (TabLayout)v.findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));
        Tabview.setOnTabSelectedListener(this);
        MainMethod();


    }
    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        store = sharedPreferences.getString("storeDescription", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        storedesc.setText(store);


        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }













    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    public interface ReportInterface {

        void onTrigger(int position);

    }
}
