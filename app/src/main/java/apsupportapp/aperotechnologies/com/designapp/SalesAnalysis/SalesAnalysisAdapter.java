package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

/**
 * Created by hasai on 12/09/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;

import static java.security.AccessController.getContext;


public class SalesAnalysisAdapter extends BaseAdapter{

    private ArrayList<SalesAnalysisListDisplay> arrayList;
    SalesAnalysisViewPagerValue salesAnalysis;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList;
    SalesPagerAdapter pageradapter;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    int focusposition,selFirstPositionValue;
    ListView listView_SalesAnalysis;
    int currentIndex;
    int offsetvalue = 0, count = 0, limit = 100;
    private LayoutInflater mInflater;

    Context context;
    String fromwhere;
    SalesAnalysisAdapter salesadapter;
    int level ;
    Gson gson;

    public SalesAnalysisAdapter(Context context, ArrayList<SalesAnalysisListDisplay> arrayList)
    {
        this.context = context;
        this.arrayList= arrayList;


    }

   //private ValueFilter valueFilter;

    public SalesAnalysisAdapter(ArrayList<SalesAnalysisListDisplay> arrayList, Context context, int currentIndex, String fromwhere, ListView listView_SalesAnalysis) {

        Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.context = context;
        this.fromwhere = fromwhere;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
        mInflater = LayoutInflater.from(context);
        this.currentIndex = currentIndex;
        level = 1;
        focusposition = 0;
        selFirstPositionValue = 0;
        analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        gson = new Gson();

        //getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {


        return arrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }
    public ArrayList<SalesAnalysisListDisplay> getData() {
        return arrayList;
    }
    public void swapItems(ArrayList<SalesAnalysisListDisplay> items) {
        this.arrayList = items;
        notifyDataSetChanged();
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.child_sales_listview, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.txtVal);
            viewHolder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
            viewHolder.innerrel = (RelativeLayout) convertView.findViewById(R.id.innerrellay);
           // viewHolder.relValue = (RelativeLayout) convertView.findViewById(R.id.relValue);
            viewHolder.txtPlan = (TextView) convertView.findViewById(R.id.txtPlan);
           // viewHolder.txtValue = (TextView) convertView.findViewById(R.id.txtValue);
            viewHolder.txtAchieve = (TextView) convertView.findViewById(R.id.txtAchieve);
            viewHolder.txtPvAValue =(TextView) convertView.findViewById(R.id.txtPvAValue);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (Holder) convertView.getTag();
        }

        final SalesAnalysisListDisplay productNameBean = (SalesAnalysisListDisplay) arrayList.get(position);


        if (fromwhere.equals("Department")) {


            viewHolder.nameTv.setText(productNameBean.getPlanDept());
            viewHolder.txtPvAValue.setText(" " + Math.round( productNameBean.getPvaAchieved()) + "%");



        } else if (fromwhere.equals("Subdept")) {

            viewHolder.nameTv.setText(productNameBean.getPlanCategory());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");


        } else if (fromwhere.equals("Class")) {


            viewHolder.nameTv.setText(productNameBean.getPlanClass());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");


        } else if (fromwhere.equals("Subclass")) {

            viewHolder.nameTv.setText(productNameBean.getBrandName());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");

        } else if (fromwhere.equals("MC")) {

            viewHolder.nameTv.setText(productNameBean.getBrandplanClass());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");

        }

        double singlePercVal = 0.5;//50/100;// width divide by 100 perc

        int planVal = 100; // planned value from API
        double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API

        double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
        double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

//        int planvalueinpx = convertSpToPixels(calplanVal, context); //converting value from sp to px
//        int achievevalueinpx = convertSpToPixels(calachieveVal, context); //converting value from sp to px

        float density = context.getResources().getDisplayMetrics().density;

        int finalCalplanVal = (int) (density * calplanVal); //converting value from px to dp
        //Log.e("", "==finalCalplanVal= " + finalCalplanVal);
        int finalCalachieveVal = (int) (density * calachieveVal); //converting value from px to dp
        // Log.e("", "==finalCalachieveVal= " + finalCalachieveVal);


        viewHolder.txtPlan.setWidth(finalCalachieveVal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3,24);
        params.setMargins(finalCalplanVal, 0, 0, 0);
        viewHolder.txtAchieve.setLayoutParams(params);


        if (achieveVal < 70)
        {
            viewHolder.txtPlan.setBackgroundColor(Color.RED);
        }
        else if(achieveVal > 90)
        {
            viewHolder.txtPlan.setBackgroundColor(Color.GREEN);//yellow
        }
        else
        {
            viewHolder.txtPlan.setBackgroundColor(Color.parseColor("#ff7e00"));
        }

       return convertView;
    }

    private class Holder {

        TextView nameTv;
        RelativeLayout rel;
        RelativeLayout innerrel, relValue;
        TextView txtPlan, txtValue,txtPvAValue;
        TextView txtAchieve;
    }

    public void updateData(ArrayList<SalesAnalysisListDisplay> users) {

        arrayList.addAll(users);

        if(salesadapter == null) {
            salesadapter = new SalesAnalysisAdapter(arrayList,context,currentIndex, fromwhere, listView_SalesAnalysis);
            listView_SalesAnalysis.setAdapter(salesadapter);

//            if (listView_SalesAnalysis.getFirstVisiblePosition() > currentIndex || listView_SalesAnalysis.getLastVisiblePosition() < currentIndex)
//                listView_SalesAnalysis.setSelection(currentIndex);
        }
        salesadapter.notifyDataSetChanged();
        salesadapter.notifyDataSetChanged();

    }

    public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }
}