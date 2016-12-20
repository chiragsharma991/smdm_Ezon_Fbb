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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;




public class SalesAnalysisAdapter extends BaseAdapter{

    private ArrayList<SalesAnalysisListDisplay> arrayList;
    SalesAnalysisViewPagerValue salesAnalysis;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList;
    SalesPagerAdapter pageradapter;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    int focusposition,selFirstPositionValue;
    ListView listView_SalesAnalysis;

    int offsetvalue = 0, count = 0, limit = 100;
    private LayoutInflater mInflater;

    Context context;
    String fromwhere;
    SalesAnalysisAdapter salesadapter;
    int level ;
    Gson gson;



    //private ValueFilter valueFilter;

    public SalesAnalysisAdapter(ArrayList<SalesAnalysisListDisplay> arrayList, Context context, String fromwhere, ListView listView_SalesAnalysis) {

        Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.context = context;
        this.fromwhere = fromwhere;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
        mInflater = LayoutInflater.from(context);
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

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Log.e("in ","getview");
        final Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.child_sales_listview, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.txtVal);
            viewHolder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
            viewHolder.innerrel = (RelativeLayout) convertView.findViewById(R.id.innerrellay);
            viewHolder.relValue = (RelativeLayout) convertView.findViewById(R.id.relValue);
            viewHolder.txtPlan = (TextView) convertView.findViewById(R.id.txtPlan);
            viewHolder.txtValue = (TextView) convertView.findViewById(R.id.txtValue);
            viewHolder.txtAchieve = (TextView) convertView.findViewById(R.id.txtAchieve);
            viewHolder.txtPvAValue =(TextView) convertView.findViewById(R.id.txtPvAValue);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (Holder) convertView.getTag();
        }

        final SalesAnalysisListDisplay productNameBean = (SalesAnalysisListDisplay) arrayList.get(position);


        // Log.e("--- ", " "+viewHolder.rel.getMeasuredWidth() + " "+ (200/100));

        if (fromwhere.equals("Department")) {

//            if(arrayList.get(position).getPlanDept().equals("All"))
//            {
//                viewHolder.txtPvAValue.setText(""+(int)analysisArrayList.get(position).getPvaAchieved());
//            }
    //        String planDept = productNameBean.getPlanDept().substring(0,1).toUpperCase()+productNameBean.getPlanDept().substring(1).toLowerCase();
            viewHolder.nameTv.setText(productNameBean.getPlanDept());
            viewHolder.txtPvAValue.setText(" " + Math.round( productNameBean.getPvaAchieved()) + "%");



        } else if (fromwhere.equals("Category")) {

      //      String planCat=productNameBean.getPlanCategory().substring(0,1).toUpperCase()+productNameBean.getPlanCategory().substring(1).toLowerCase();
            viewHolder.nameTv.setText(productNameBean.getPlanCategory());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");


        } else if (fromwhere.equals("Plan Class")) {

           // String planCls = productNameBean.getPlanClass().substring(0,1).toUpperCase()+productNameBean.getPlanClass().substring(1).toLowerCase();
            viewHolder.nameTv.setText(productNameBean.getPlanClass());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");
            //viewHolder.txtValue.setText(productNameBean.getPlanClass().toLowerCase());

        } else if (fromwhere.equals("Brand")) {

            //String brnd= productNameBean.getBrandName().substring(0,1).toUpperCase()+productNameBean.getBrandName().substring(1).toLowerCase();
            viewHolder.nameTv.setText(productNameBean.getBrandName());
            viewHolder.txtPvAValue.setText(""+Math.round(productNameBean.getPvaAchieved())+"%");

        } else if (fromwhere.equals("Brand Plan Class")) {

        //    String brndPlnCls= productNameBean.getBrandplanClass().substring(0,1).toUpperCase()+productNameBean.getBrandplanClass().substring(1).toLowerCase();
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

         //txtPvAClickedValue = viewHolder.nameTv.getText().toString();


       return convertView;
    }

    private class Holder {

        TextView nameTv;
        RelativeLayout rel;
        RelativeLayout innerrel, relValue;
        TextView txtPlan, txtValue,txtPvAValue;
        TextView txtAchieve;
    }

    public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }
}