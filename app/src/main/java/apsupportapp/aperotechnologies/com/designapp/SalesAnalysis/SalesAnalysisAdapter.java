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

            viewHolder.nameTv.setText(productNameBean.getPlanDept());
            viewHolder.txtPvAValue.setText(" " + (int) productNameBean.getPvaAchieved() + "%");



        } else if (fromwhere.equals("Category")) {

            viewHolder.nameTv.setText(productNameBean.getPlanCategory());
            viewHolder.txtPvAValue.setText(""+(int)productNameBean.getPvaAchieved()+"%");


        } else if (fromwhere.equals("Plan Class")) {


            viewHolder.nameTv.setText(productNameBean.getPlanClass());
            viewHolder.txtPvAValue.setText(""+(int)productNameBean.getPvaAchieved()+"%");
            //viewHolder.txtValue.setText(productNameBean.getPlanClass().toLowerCase());

        } else if (fromwhere.equals("Brand")) {


            viewHolder.nameTv.setText(productNameBean.getBrandName());
            viewHolder.txtPvAValue.setText(""+(int)productNameBean.getPvaAchieved()+"%");

        } else if (fromwhere.equals("Brand Plan Class")) {


            viewHolder.nameTv.setText(productNameBean.getBrandplanClass());
            viewHolder.txtPvAValue.setText(""+(int)productNameBean.getPvaAchieved()+"%");

        }

        double singlePercVal = 0.5;//50/100;// width divide by 100 perc

        int planVal = 100; // planned value from API
        double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API

        double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
        double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

        int planvalueinpx = convertSpToPixels(calplanVal, context); //converting value from sp to px
        int achievevalueinpx = convertSpToPixels(calachieveVal, context); //converting value from sp to px

        float density = context.getResources().getDisplayMetrics().density;

        int finalCalplanVal = (int) (density * planvalueinpx); //converting value from px to dp
        //Log.e("", "==finalCalplanVal= " + finalCalplanVal);
        int finalCalachieveVal = (int) (density * achievevalueinpx); //converting value from px to dp
        // Log.e("", "==finalCalachieveVal= " + finalCalachieveVal);


        viewHolder.txtPlan.setWidth(finalCalachieveVal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3, RelativeLayout.LayoutParams.MATCH_PARENT);
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
//        viewHolder.nameTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Toast.makeText(context, "name click" + viewHolder.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
//                Log.e("segmented click", "" + SalesAnalysisActivity.selectedsegValue);
//                txtPvAClickedValue = viewHolder.nameTv.getText().toString();
//                Log.e("txtPvAClickedValue---",""+txtPvAClickedValue);
//                switch (SalesAnalysisActivity.txtheaderplanclass.getText().toString()) {
//
//                    case "Department" :
//                        SalesAnalysisActivity.txtheaderplanclass.setText("Category");
//                        SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.GONE);
//                        SalesAnalysisActivity.txthDeptName.setText(productNameBean.getPlanDept());
//                       fromwhere = "Category";
//                        SalesPagerAdapter.currentPage = 0;
//                        if (SalesAnalysisActivity.lldots != null) {
//                            SalesAnalysisActivity.lldots.removeAllViews();
//                        }
//                        level = 2;
//                        if (Reusable_Functions.chkStatus(context)) {
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            arrayList = new ArrayList<SalesAnalysisListDisplay>();
//
//                            Log.i("dept next","-----");
//                            requestSalesCategoryList(txtPvAClickedValue);
//                            planDept = txtPvAClickedValue;
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//
//                    case "Category" :
//
//                       SalesAnalysisActivity.txtheaderplanclass.setText("Plan Class");
//                       SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.GONE);
//
//                        Log.e("txtPvAClickedValue1---",""+txtPvAClickedValue);
//                        fromWhere = "Plan Class";
//                        SalesPagerAdapter.currentPage = 0;
//                        if (SalesAnalysisActivity.lldots != null) {
//                            SalesAnalysisActivity.lldots.removeAllViews();
//                        }
//                        level = 3;
//                        if (Reusable_Functions.chkStatus(context)) {
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            arrayList = new ArrayList<SalesAnalysisListDisplay>();
//                            Log.i("category next","-----");
//                            Log.i("come","----"+planDept);
//                            requestSalesPlanClassListAPI(planDept,txtPvAClickedValue);
//                            planCategory = txtPvAClickedValue;
//                            Log.e("planCategory--",""+planCategory);
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    case "Plan Class" :
//
//                        SalesAnalysisActivity.txtheaderplanclass.setText("Brand");
//                        SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.GONE);
//                        SalesAnalysisActivity.txthDeptName.setText(productNameBean.getPlanDept());
//                        SalesAnalysisActivity.txthDeptNameNext.setText(">");
//                        SalesAnalysisActivity.txthCategory.setText(productNameBean.getPlanCategory());
//                        SalesAnalysisActivity.txthCategoryNext.setText(">");
//                        SalesAnalysisActivity.txthPlanClass.setText(productNameBean.getPlanClass());
//                        Log.e("txtPvAClickedValue2---",""+txtPvAClickedValue);
//                        fromWhere = "Brand";
//                        SalesPagerAdapter.currentPage = 0;
//                        if (SalesAnalysisActivity.lldots != null) {
//                            SalesAnalysisActivity.lldots.removeAllViews();
//                        }
//                        level = 4;
//                        if (Reusable_Functions.chkStatus(context)) {
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            arrayList = new ArrayList<SalesAnalysisListDisplay>();
//                            Log.i("Plan Class next","-----");
//                            requestSalesBrandListAPI(planDept,planCategory,txtPvAClickedValue);
//                            planClass = txtPvAClickedValue;
//                            Log.e("planClass---",""+planClass);
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//
//                        break;
//                    case "Brand" :
//                        SalesAnalysisActivity.txtheaderplanclass.setText("Brand Plan Class");
//                        SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.GONE);
//                        SalesAnalysisActivity.txthDeptName.setText(productNameBean.getPlanDept());
//                        SalesAnalysisActivity.txthDeptNameNext.setText(">");
//                        SalesAnalysisActivity.txthCategory.setText(productNameBean.getPlanCategory());
//                        SalesAnalysisActivity.txthCategoryNext.setText(">");
//                        SalesAnalysisActivity.txthPlanClass.setText(productNameBean.getPlanClass());
//                        SalesAnalysisActivity.txthPlanClassNext.setText(">");
//                        SalesAnalysisActivity.txthBrand.setText(productNameBean.getBrandName());
//                        Log.e("txtPvAClickedValue3---",""+txtPvAClickedValue);
//                        fromWhere = "Brand Plan Class";
//                        SalesPagerAdapter.currentPage = 0;
//                        if (SalesAnalysisActivity.lldots != null) {
//                            SalesAnalysisActivity.lldots.removeAllViews();
//                        }
//                        level = 5;
//                        if (Reusable_Functions.chkStatus(context)) {
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            arrayList = new ArrayList<SalesAnalysisListDisplay>();
//                            Log.i("brand next","-----");
//                            requestSalesBrandPlanListAPI(planDept,planCategory,planClass,txtPvAClickedValue);
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//        });

//        listView_SalesAnalysis.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (arrayList.size() != 0) {
//
//                    if (view.getFirstVisiblePosition() <= arrayList.size() - 1) {
//
//                        focusposition = view.getFirstVisiblePosition();
//
//                        listView_SalesAnalysis.setSelection(view.getFirstVisiblePosition());
//                        //Log.e("firstVisibleItem", " " + view.getFirstVisiblePosition() + " " + arrayList.get(view.getFirstVisiblePosition()).getPlanDept());
//
//                        if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Department"))
//                        {
//                            SalesAnalysisActivity.saleFirstVisibleItem = arrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getPlanDept().toString();
//
//                        }
//                        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Category"))
//                        {
//
//                            SalesAnalysisActivity.saleFirstVisibleItem = arrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getPlanCategory().toString();
//                        }
//                        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Plan Class"))
//                        {
//                            SalesAnalysisActivity.saleFirstVisibleItem = arrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getPlanClass().toString();
//                        }
//                        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Brand"))
//                        {
//                            SalesAnalysisActivity.saleFirstVisibleItem = arrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getBrandName().toString();
//                        }
//                        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Brand Plan Class"))
//                        {
//                            SalesAnalysisActivity.saleFirstVisibleItem = arrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getBrandplanClass().toString();
//                        }
//                        //Log.e("analysisArrayList", " " + analysisArrayList.size());
//
//                        if(focusposition != selFirstPositionValue) {
//                            if (Reusable_Functions.chkStatus(context)) {
//                                Reusable_Functions.hDialog();
//                                Reusable_Functions.sDialog(context, "Loading data...");
//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
//                                requestSalesPagerOnScrollAPI();
//
//                            } else {
//                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        selFirstPositionValue = focusposition;
//
//                    } else {
//                        focusposition = arrayList.size() - 1;
//                        listView_SalesAnalysis.setSelection(arrayList.size() - 1);
//                        pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, SalesAnalysisActivity.vwpagersales, SalesAnalysisActivity.lldots, salesadapter, listView_SalesAnalysis);
//                        SalesAnalysisActivity.vwpagersales.setAdapter(pageradapter);
//                        selFirstPositionValue = focusposition;
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });


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




//    private void requestSalesPlanClassListAPI(final String deptName,final String category) {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue  + "&level=" + level + "&department="+planDept.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+category.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
//        Log.e("url", " " + salespva_planclass_listurl);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("Sales Plan Class List : ", " " + response);
//                        Log.i("Sales Plan Class List response length", "" + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    arrayList.add(salesAnalysisListDisplay);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestSalesPlanClassListAPI(deptName,category);
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    arrayList.add(salesAnalysisListDisplay);
//                                }
//                                for (int i = 0; i < 3; i++) {
//
//                                    ImageView imgdot = new ImageView(context);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
//                                    layoutParams.setMargins(3, 3, 3, 3);
//                                    imgdot.setLayoutParams(layoutParams);
//                                    imgdot.setImageResource(R.mipmap.dots_unselected);
//                                    SalesAnalysisActivity.lldots.addView(imgdot);
//                                    ImageView img = (ImageView) SalesAnalysisActivity.lldots.getChildAt(0);
//                                    img.setImageResource(R.mipmap.dots_selected);
//                                }
//
//                                salesadapter = new SalesAnalysisAdapter(arrayList, context, fromwhere, listView_SalesAnalysis);
//                               listView_SalesAnalysis.setAdapter(salesadapter);
//                                SalesAnalysisActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
//                                SalesAnalysisActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
//                                SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.VISIBLE);
//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                requestSalesViewPagerValueAPI();
//                            }
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no  plan class data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//    private void requestSalesBrandListAPI( String deptName, String category, final String planclass) {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue  + "&level=" + level + "&department="+planDept.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+planCategory.replaceAll(" ", "%20").replaceAll("&", "%26")+"&class="+planclass.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
//        Log.e("url", " " + salespva_brand_listurl);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("Sales brand List : ", " " + response);
//                        Log.i("Sales brand List response length", "" + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    arrayList.add(salesAnalysisListDisplay);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestSalesBrandListAPI(planDept,planCategory,planclass);
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    arrayList.add(salesAnalysisListDisplay);
//                                }
//
//
//                                for (int i = 0; i < 3; i++) {
//
//                                    ImageView imgdot = new ImageView(context);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
//                                    layoutParams.setMargins(3, 3, 3, 3);
//                                    imgdot.setLayoutParams(layoutParams);
//                                    imgdot.setImageResource(R.mipmap.dots_unselected);
//                                    SalesAnalysisActivity.lldots.addView(imgdot);
//                                    ImageView img = (ImageView) SalesAnalysisActivity.lldots.getChildAt(0);
//                                    img.setImageResource(R.mipmap.dots_selected);
//                                }
//
//                                salesadapter = new SalesAnalysisAdapter(arrayList, context, fromwhere, listView_SalesAnalysis);
//                                listView_SalesAnalysis.setAdapter(salesadapter);
//                                SalesAnalysisActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
//                                SalesAnalysisActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
//                                SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.VISIBLE);
//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                requestSalesViewPagerValueAPI();
//                            }
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//    private void requestSalesBrandPlanListAPI(String deptName, String category, String plan_class, final String brandnm) {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue  + "&level=" + level + "&department="+planDept.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+planCategory.replaceAll(" ", "%20").replaceAll("&", "%26")+ "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand=" +brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
//        Log.e("url", " " + salespva_brandplan_listurl);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("Sales Brand Plan Class List : ", " " + response);
//                        Log.i("Sales Brand Plan Class List response length", "" + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
//
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    arrayList.add(salesAnalysisListDisplay);
//
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestSalesBrandPlanListAPI(planDept,planCategory,planClass,brandnm);
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    arrayList.add(salesAnalysisListDisplay);
//                                }
//
//                                for (int i = 0; i < 3; i++) {
//
//                                    ImageView imgdot = new ImageView(context);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
//                                    layoutParams.setMargins(3, 3, 3, 3);
//                                    imgdot.setLayoutParams(layoutParams);
//                                    imgdot.setImageResource(R.mipmap.dots_unselected);
//                                    SalesAnalysisActivity.lldots.addView(imgdot);
//                                    ImageView img = (ImageView) SalesAnalysisActivity.lldots.getChildAt(0);
//                                    img.setImageResource(R.mipmap.dots_selected);
//                                }
//
//                                salesadapter = new SalesAnalysisAdapter(arrayList, context, fromwhere, listView_SalesAnalysis);
//                                listView_SalesAnalysis.setAdapter(salesadapter);
//                                SalesAnalysisActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
//                                SalesAnalysisActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
//                                SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.VISIBLE);
//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                requestSalesViewPagerValueAPI();
//                            }
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//    private void requestSalesViewPagerValueAPI() {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit;
//
//        Log.e("Url", "" + url);
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.e("Adapter View pager response",""+response);
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
//                                    analysisArrayList.add(salesAnalysis);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//
//                                requestSalesViewPagerValueAPI();
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
//                                    analysisArrayList.add(salesAnalysis);
//                                }
//                            }
//
////                            for (int i = 0; i < 3; i++) {
////                                ImageView imgdot = new ImageView(context);
////                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
////                                layoutParams.setMargins(3, 3, 3, 3);
////                                imgdot.setLayoutParams(layoutParams);
////                                imgdot.setImageResource(R.mipmap.dots_unselected);
////                                SalesAnalysisActivity.lldots.addView(imgdot);
////                                ImageView img = (ImageView) SalesAnalysisActivity.lldots.getChildAt(0);
////                                img.setImageResource(R.mipmap.dots_selected);
////                            }
//                            pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, SalesAnalysisActivity.vwpagersales, SalesAnalysisActivity.lldots, salesadapter, listView_SalesAnalysis);
//                            //Log.i("-------",""+analysisArrayList.size());
//                            SalesAnalysisActivity.vwpagersales.setAdapter(pageradapter);
//                            SalesAnalysisActivity.txtZonalSales.setText("" + salesAnalysis.getZonalSalesRank());
//                            SalesAnalysisActivity.txtNationalSales.setText("" + salesAnalysis.getNationalSalesRank());
//                            SalesAnalysisActivity.txtZonalYOY.setText("" + salesAnalysis.getZonalYOYGrowthRank());
//                            SalesAnalysisActivity.txtNationalYOY.setText("" + salesAnalysis.getNationalYOYGrowthRank());
//
//                            // Log.i("Sales Zonal Rank",""+salesAnalysis.getZonalSalesRank());
//
//                            //pageradapter.notifyDataSetChanged();
//                            SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.VISIBLE);
//                            Reusable_Functions.hDialog();
//
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//    }
//    private void requestSalesPagerOnScrollAPI() {
//
//        Log.e("Department onsroll api", "" + SalesAnalysisActivity.saleFirstVisibleItem);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String url = "";
//
//        if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Department"))
//        {
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue + "&department=" + SalesAnalysisActivity.saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Category"))
//        {
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue + "&category=" + SalesAnalysisActivity.saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Plan Class"))
//        {
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue + "&class=" + SalesAnalysisActivity.saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Brand"))
//        {
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue + "&brand=" + SalesAnalysisActivity.saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if(SalesAnalysisActivity.txtheaderplanclass.getText().toString().equals("Brand Plan Class"))
//        {
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + SalesAnalysisActivity.selectedsegValue + "&brandclass=" + SalesAnalysisActivity.saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        Log.e("Url", "" + url);
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("Sales Analysis by time : ", " " + response);
//                        Log.i("Sales View Pager response", "" + response.length());
//                        try {
//
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
//                                    analysisArrayList.add(salesAnalysis);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestSalesPagerOnScrollAPI();
//
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
//                                    analysisArrayList.add(salesAnalysis);
//                                }
//                                Log.e("analysisArrayList", " --- " + analysisArrayList.size());
//
////                                for (int i = 0; i < 3; i++) {
////                                    ImageView imgdot = new ImageView(context);
////                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
////                                    layoutParams.setMargins(3, 3, 3, 3);
////                                    imgdot.setLayoutParams(layoutParams);
////                                    imgdot.setImageResource(R.mipmap.dots_unselected);
////                                    SalesAnalysisActivity.lldots.addView(imgdot);
////                                    ImageView img = (ImageView) SalesAnalysisActivity.lldots.getChildAt(0);
////                                    img.setImageResource(R.mipmap.dots_selected);
////                                }
//                                pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, SalesAnalysisActivity.vwpagersales, SalesAnalysisActivity.lldots, salesadapter, listView_SalesAnalysis);
//                                //Log.i("-------",""+analysisArrayList.size());
//                                SalesAnalysisActivity.vwpagersales.setAdapter(pageradapter);
//                                SalesAnalysisActivity.txtZonalSales.setText("" + salesAnalysis.getZonalSalesRank());
//                                SalesAnalysisActivity.txtNationalSales.setText("" + salesAnalysis.getNationalSalesRank());
//                                SalesAnalysisActivity.txtZonalYOY.setText("" + salesAnalysis.getZonalYOYGrowthRank());
//                                SalesAnalysisActivity.txtNationalYOY.setText("" + salesAnalysis.getNationalYOYGrowthRank());
//
//                                SalesAnalysisActivity.llayoutSalesAnalysis.setVisibility(View.VISIBLE);
//                                Reusable_Functions.hDialog();
//                            }
//
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//    }

}