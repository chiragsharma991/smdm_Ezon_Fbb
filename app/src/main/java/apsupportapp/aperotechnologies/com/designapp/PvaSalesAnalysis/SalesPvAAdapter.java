package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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


public class SalesPvAAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    String fromWhere;
    int level ;
    int offsetvalue = 0, count = 0, limit = 100;
    private ArrayList<SalesAnalysisListDisplay> salesAnalysisListDisplayArrayList;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    SalesPvAAdapter salesPvAAdapter;
    ListView listViewSalesPvA;
    Gson gson ;
    static String planDept,planCategory,planClass,planBrandName;

    public SalesPvAAdapter(ArrayList<SalesAnalysisListDisplay> salesAnalysisListDisplayArrayList, Context context, String fromWhere, ListView listViewSalesPvA) {
        this.context = context;
        this.salesAnalysisListDisplayArrayList = salesAnalysisListDisplayArrayList;
        this.fromWhere = fromWhere;
        this.listViewSalesPvA = listViewSalesPvA;
        level = 1;
        gson = new Gson();
    }

    @Override
    public int getCount() {

        return salesAnalysisListDisplayArrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return salesAnalysisListDisplayArrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }
    static class ViewHolderItem {
        TextView txtPlanClass, txtPlanSales, txtNetSales, txtPvASales, txtPlan, txtAchieve;
        RelativeLayout rel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.salespva_list_row, parent,
                    false);
            viewHolder = new ViewHolderItem();
            viewHolder.txtPlanClass = (TextView) convertView.findViewById(R.id.txtPlanClass);
            viewHolder.txtPlanSales = (TextView) convertView.findViewById(R.id.txtPlanSales);
            viewHolder.txtNetSales = (TextView) convertView.findViewById(R.id.txtNetSales);

            viewHolder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
            viewHolder.txtPlan = (TextView) convertView.findViewById(R.id.txtPlan);
            viewHolder.txtAchieve = (TextView) convertView.findViewById(R.id.txtAchieve);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.txtPlanClass, viewHolder.txtPlanClass);
            convertView.setTag(R.id.txtPlanSales, viewHolder.txtPlanSales);
            convertView.setTag(R.id.txtNetSales, viewHolder.txtNetSales);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        SalesAnalysisListDisplay productNameBean = (SalesAnalysisListDisplay) salesAnalysisListDisplayArrayList.get(position);

//        viewHolder.rel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        Log.e("--- ", " " + viewHolder.rel.getMeasuredWidth() + " " + (200 / 100));
//
//        int singlePercVal = 200 / 100;//rel.getMeasuredWidth()/100;
//
//        int planVal = 100;//30
//        //int achieveVal = salesAnalysisListDisplayArrayList.get(position).getDayNetSales();
//
//        int calplanVal = planVal * singlePercVal;
//        //int calachieveVal = achieveVal * singlePercVal;
//
//        viewHolder.txtPlan.setWidth(calplanVal);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2, 18);
//        //params.setMargins(calachieveVal, 0, 0, 0);
//        viewHolder.txtAchieve.setLayoutParams(params);
//
////        if (planVal < achieveVal) {
////            viewHolder.txtPlan.setBackgroundColor(Color.RED);
////        } else {
////            viewHolder.txtPlan.setBackgroundColor(Color.GREEN);
////        }
        viewHolder.txtPlanClass.setTag(position);
        viewHolder.txtPlanSales.setTag(position);
        viewHolder.txtNetSales.setTag(position);

        if(fromWhere.equals("Department")) {

            viewHolder.txtPlanClass.setText(productNameBean.getPlanDept());
            viewHolder.txtPlanSales.setText(" " + productNameBean.getPlanSaleNetVal());
            viewHolder.txtNetSales.setText(" " + productNameBean.getSaleNetVal());


        }
        else if(fromWhere.equals("Category"))
        {

            viewHolder.txtPlanClass.setText(productNameBean.getPlanCategory());
            viewHolder.txtPlanSales.setText(" " + productNameBean.getPlanSaleNetVal());
            viewHolder.txtNetSales.setText(" " + productNameBean.getSaleNetVal());

//
//            viewHolder.txtValue.setText(productNameBean.getPlanCategory().toLowerCase());

        }
        else if(fromWhere.equals("Plan Class"))
        {

           viewHolder.txtPlanClass.setText(productNameBean.getPlanClass());
            viewHolder.txtPlanSales.setText(" " + productNameBean.getPlanSaleNetVal());
            viewHolder.txtNetSales.setText(" " + productNameBean.getSaleNetVal());

            //viewHolder.txtValue.setText(productNameBean.getPlanClass().toLowerCase());

        }

        else if(fromWhere.equals("Brand"))
        {

           viewHolder.txtPlanClass.setText(productNameBean.getBrandName());
            viewHolder.txtPlanSales.setText(" " + productNameBean.getPlanSaleNetVal());
            viewHolder.txtNetSales.setText(" " + productNameBean.getSaleNetVal());

            //viewHolder.txtValue.setText(productNameBean.getBrandName().toLowerCase());

        }

        else if(fromWhere.equals("Brand Plan Class"))
        {

            viewHolder.txtPlanClass.setText(productNameBean.getBrandplanClass());
            viewHolder.txtPlanSales.setText(" " + productNameBean.getPlanSaleNetVal());
            viewHolder.txtNetSales.setText(" " + productNameBean.getSaleNetVal());

            //viewHolder.txtValue.setText(productNameBean.getBrandplanClass().toLowerCase());
        }

        double singlePercVal = 1;//50/100;// width divide by 100 perc

        int planVal = 100; // planned value from API
        double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API

        double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
        double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

        //int planvalueinpx = convertSpToPixels(calplanVal, context); //converting value from sp to px
        //int achievevalueinpx = convertSpToPixels(calachieveVal, context); //converting value from sp to px

        float density = context.getResources().getDisplayMetrics().density;

        int finalCalplanVal = (int) (calplanVal); //converting value from px to dp
        int finalCalachieveVal = (int) (calachieveVal); //converting value from px to dp


        viewHolder.txtPlan.setWidth((int) calplanVal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2, 24);
        params.setMargins((int) calachieveVal, 0, 0, 0);
        viewHolder.txtAchieve.setLayoutParams(params);


        if (achieveVal == planVal || achieveVal > planVal)
        {
            viewHolder.txtPlan.setBackgroundColor(Color.GREEN);
        }
        else if(achieveVal >= 80 && achieveVal < planVal)
        {
            viewHolder.txtPlan.setBackgroundColor(Color.parseColor("#ffff00"));//yellow
        }
        else if(achieveVal < 80) {
            viewHolder.txtPlan.setBackgroundColor(Color.RED);

        }

        viewHolder.txtPlanClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "name click" + viewHolder.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                Log.e("segmented click", "" + SalesPvAActivity.salesPvA_SegmentClick);
                String txtPvAClickedValue = viewHolder.txtPlanClass.getText().toString();
                Log.e("txtPvAClickedValue---",""+txtPvAClickedValue);
                switch (SalesPvAActivity.txtheaderplanclass.getText().toString()) {

                    case "Department" :
                        SalesPvAActivity.txtheaderplanclass.setText("Category");
                        SalesPvAActivity.llayoutSalesPvA.setVisibility(View.GONE);

                        fromWhere = "Category";
                        level = 2;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            salesAnalysisListDisplayArrayList = new ArrayList<SalesAnalysisListDisplay>();

                            Log.i("dept next","-----");
                            requestSalesPvACategoryList(txtPvAClickedValue);
                            planDept = txtPvAClickedValue;

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Category" :

                        SalesPvAActivity.txtheaderplanclass.setText("Plan Class");
                        SalesPvAActivity.llayoutSalesPvA.setVisibility(View.GONE);
                        Log.e("txtPvAClickedValue1---",""+txtPvAClickedValue);
                        fromWhere = "Plan Class";
                        level = 3;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            salesAnalysisListDisplayArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            Log.i("category next","-----");
                            requestSalesPvAPlanClassListAPI(planDept,txtPvAClickedValue);
                            planCategory = txtPvAClickedValue;
                            Log.e("planCategory--",""+planCategory);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Plan Class" :

                        SalesPvAActivity.txtheaderplanclass.setText("Brand");
                        SalesPvAActivity.llayoutSalesPvA.setVisibility(View.GONE);
                        Log.e("txtPvAClickedValue2---",""+txtPvAClickedValue);
                        fromWhere = "Brand";
                        level = 4;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            salesAnalysisListDisplayArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            Log.i("Plan Class next","-----");
                            requestSalesPvABrandListAPI(planDept,planCategory,txtPvAClickedValue);
                            planClass = txtPvAClickedValue;
                            Log.e("planClass---",""+planClass);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "Brand" :
                        SalesPvAActivity.txtheaderplanclass.setText("Brand Plan Class");

                        SalesPvAActivity.llayoutSalesPvA.setVisibility(View.GONE);
                        Log.e("txtPvAClickedValue3---",""+txtPvAClickedValue);
                        fromWhere = "Brand Plan Class";
                        level = 5;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            salesAnalysisListDisplayArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            Log.i("brand next","-----");
                            requestSalesPvABrandPlanListAPI(planDept,planCategory,planClass,txtPvAClickedValue);

                           } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }

            }
       });
        return convertView;
    }

       public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }

    private void requestSalesPvACategoryList(final String deptName) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesPvAActivity.salesPvA_SegmentClick  + "&level=" + level + "&department="+deptName.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespvacategory_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales PvA Category List: ", " " + response);
                        Log.i("Sales PvA Category List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvACategoryList(deptName);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(SalesPvAActivity.salesPvAAdapter);
                                SalesPvAActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
                                SalesPvAActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
                                SalesPvAActivity.llayoutSalesPvA.setVisibility(View.VISIBLE);

                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void requestSalesPvAPlanClassListAPI(final String deptName,final String category) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesPvAActivity.salesPvA_SegmentClick  + "&level=" + level + "&department="+planDept.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+category.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_planclass_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva Plan Class List : ", " " + response);
                        Log.i("Sales Pva Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvAPlanClassListAPI(deptName,category);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(SalesPvAActivity.salesPvAAdapter);
                                SalesPvAActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
                                SalesPvAActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
                                SalesPvAActivity.llayoutSalesPvA.setVisibility(View.VISIBLE);

                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no  plan class data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestSalesPvABrandListAPI( String deptName, String category, final String planclass) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesPvAActivity.salesPvA_SegmentClick  + "&level=" + level + "&department="+planDept.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+planCategory.replaceAll(" ", "%20").replaceAll("&", "%26")+"&class="+planclass.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brand_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva brand List : ", " " + response);
                        Log.i("Sales Pva brand List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandListAPI(planDept,planCategory,planclass);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(SalesPvAActivity.salesPvAAdapter);
                                SalesPvAActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
                                SalesPvAActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
                                SalesPvAActivity.llayoutSalesPvA.setVisibility(View.VISIBLE);

                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestSalesPvABrandPlanListAPI(String deptName, String category, String plan_class, final String brandnm) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + SalesPvAActivity.salesPvA_SegmentClick  + "&level=" + level + "&department="+planDept.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+planCategory.replaceAll(" ", "%20").replaceAll("&", "%26")+ "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand=" +brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva Brand Plan Class List : ", " " + response);
                        Log.i("Sales Pva Brand Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandPlanListAPI(planDept,planCategory,planClass,brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisListDisplayArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(SalesPvAActivity.salesPvAAdapter);
                                SalesPvAActivity.txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
                                SalesPvAActivity.txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
                                SalesPvAActivity.llayoutSalesPvA.setVisibility(View.VISIBLE);

                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}



