package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import apsupportapp.aperotechnologies.com.designapp.model.ListBrand;
import apsupportapp.aperotechnologies.com.designapp.model.ListBrandClass;
import apsupportapp.aperotechnologies.com.designapp.model.ListCategory;
import apsupportapp.aperotechnologies.com.designapp.model.ListPlanClass;
import apsupportapp.aperotechnologies.com.designapp.model.ListProdLevel6;


public class InventoryFilterExpandableList extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;

    int offsetvalue = 0, limit = 100, count = 0, level = 1;
    ArrayList categorylist,planClassList,brandNameList,brandplanclassList,prodlevel6list;

    ExpandableListView expandableListView;
    List<String> salesList;
    List<ListCategory> categoryArray;
    List<ListPlanClass> planclassArray;
    List<ListBrand> brandArray;
    List<ListBrandClass> brandclassArray;
    List<ListProdLevel6> mcArray;
    List tempcategorylist,tempplanclass,tempbrandname,tempbrandplanclass,tempprodlevel6;
    private HashMap<String, List<String>> mListDataChild;
    private ArrayList<String> mListDataGroup;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private String groupText;
    private String childText;
    static String planDepartmentName,planCategoryName,planClassName,brand_Name;
    InventoryFilterExpandableList listAdapter;
    Boolean flag = false;

    public InventoryFilterExpandableList(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, InventoryFilterExpandableList listAdapter) {

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;
        categorylist = new ArrayList();
        planClassList = new ArrayList();
        brandNameList = new ArrayList();
        brandplanclassList = new ArrayList();
        prodlevel6list = new ArrayList();
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        salesList = new ArrayList<>();
        categoryArray = new ArrayList();
        planclassArray = new ArrayList();
        brandArray = new ArrayList();
        brandclassArray = new ArrayList();
        mcArray = new ArrayList();
        flag = false;
        level = 1;
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        groupText = getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.inv_filter_list_group, null);

            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);
//            groupViewHolder.mImage = (ImageView) convertView.findViewById(R.id.groupImage);
//            int imageId = InventoryFilterActivity.groupImages.get(groupPosition);
//            groupViewHolder.mImage.setImageResource(imageId);
            convertView.setTag(groupViewHolder);
            planDepartmentName = " ";

        } else {

            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (mListDataChild.get(mListDataGroup.get(groupPosition)) != null) {
            return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
        } else {
            return 0;
        }
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        try {
            return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        childText = getChild(mGroupPosition, mChildPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.inv_filter_list_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.txtdeptname);
            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.itemCheckBox);
            convertView.setTag(R.layout.inv_filter_list_item, childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.inv_filter_list_item);
        }

        childViewHolder.mChildText.setText(childText);

        if (salesList.contains(mListDataGroup.get(groupPosition)+"."+childText)) {
            childViewHolder.mCheckBox.setChecked(true);
        } else {
            childViewHolder.mCheckBox.setChecked(false);
        }

        convertView.setOnClickListener(null);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rel = (RelativeLayout) v;
                CheckBox cb = (CheckBox) rel.getChildAt(1);
                TextView txtView = (TextView) rel.getChildAt(0);
                String txtClickedVal = txtView.getText().toString();
//
                if(groupPosition == 5)
                {
                    cb.setChecked(true);
                    return;
                }

                if (cb.isChecked() == false) {
                    ////Log.e("checkbox is not selected", "");
                    if (Reusable_Functions.chkStatus(mContext)) {

                        Reusable_Functions.sDialog(mContext, "Loading  data...");
                        offsetvalue = 0;
                        count = 0;
                        limit = 100;
                        
                        if (groupPosition == 0) {
                            level = 2;
                            tempcategorylist = new ArrayList();
                            InventoryFilterActivity.pfilter_list.collapseGroup(1);
                            planDepartmentName = txtClickedVal;
                            Log.i("click dept value",""+planDepartmentName);
                            String groupname = mListDataGroup.get(0);
                            requestInventoryCategoryAPI(offsetvalue, limit, planDepartmentName, groupname, cb);

                        }
                        else if (groupPosition == 1)
                        {
                            level = 3;
                            tempplanclass = new ArrayList();
                            InventoryFilterActivity.pfilter_list.collapseGroup(2);
                            planCategoryName = txtClickedVal;
                            Log.i("click category value---",""+planCategoryName);
                            String groupname = mListDataGroup.get(1);

                            requestInventoryPlanClassAPI(offsetvalue, limit, planDepartmentName, txtClickedVal, groupname, cb);


                        }
                        else if (groupPosition == 2)
                        {
                            level = 4;
                            tempbrandname = new ArrayList();
                            InventoryFilterActivity.pfilter_list.collapseGroup(3);
                            planClassName = txtClickedVal;
                            Log.i("click brand name value --- ",""+planClassName);
                            String groupname = mListDataGroup.get(2);
                            requestInventoryBrandNameAPI(offsetvalue, limit,planDepartmentName, planCategoryName, txtClickedVal, groupname, cb);

                        }
                        else if(groupPosition == 3)
                        {
                            level = 5;
                            tempbrandplanclass = new ArrayList();
                            brand_Name = txtClickedVal;
                            Log.i("click brand name value --- ",""+brand_Name);
                            InventoryFilterActivity.pfilter_list.collapseGroup(4);
                            String groupname = mListDataGroup.get(3);
                            requestInventoryBrandClassAPI(offsetvalue, limit,planDepartmentName,planCategoryName,planClassName, txtClickedVal, groupname, cb);

                        }

                        else if(groupPosition == 4)
                        {
                            level = 6;
                            tempprodlevel6 = new ArrayList();
                            InventoryFilterActivity.pfilter_list.collapseGroup(5);
                            Log.i("click brand name value --- ",""+txtClickedVal);
                            String groupname = mListDataGroup.get(4);
                            requestInventoryProdLevel6API(offsetvalue, limit,planDepartmentName,planCategoryName,planClassName,brand_Name, txtClickedVal, groupname, cb);
                        }
                        } else
                        {
                            Toast.makeText(mContext, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("groupPosition ", " " + groupPosition);

                        if (groupPosition == 0)  //Department Group
                        {
                            //Log.e("here ", " " + planclassArray.size());
                            // category array
                            for (int i = 0; i < categoryArray.size(); i++) {
                                if (categoryArray.get(i).getSubdept().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(1);
                                    categorylist.removeAll(categoryArray.get(i).getCategory());
                                    Log.e("salesList"," ---111--- "+salesList);
                                    salesList.remove(mListDataGroup.get(0)+"."+categoryArray.get(i).getSubdept());
                                    categoryArray.remove(i);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(1);

                                }
                            }

                            for (int k = 0; k < planclassArray.size(); k++) {
                                Log.e("sub ", " " + planclassArray.get(k).getSubdept() + " " + planclassArray.get(k).getSubdept().equals(txtClickedVal));
                                if (planclassArray.get(k).getSubdept().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(2);
                                    Log.e("plan list", " === " + planclassArray.get(k).getPlanclass() + " " + planclassArray.get(k).getCategory());
                                    planClassList.removeAll(planclassArray.get(k).getPlanclass());
                                    salesList.remove(mListDataGroup.get(1)+"."+planclassArray.get(k).getCategory());
                                    planclassArray.remove(k);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(2);
                                }
                            }
                            for (int j = 0; j < brandArray.size(); j++) {
                                   if (brandArray.get(j).getSubdept().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(3);
                                    brandNameList.removeAll(brandArray.get(j).getBrand());
                                    salesList.remove(mListDataGroup.get(2)+"."+brandArray.get(j).getPlanclass());
                                    brandArray.remove(j);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(3);
                                }
                            }
                            for (int m = 0;  m < brandclassArray.size(); m++) {

                                if (brandclassArray.get(m).getSubdept().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(4);
                                    brandplanclassList.removeAll(brandclassArray.get(m).getBrandClass());
                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(m).getBrand());
                                    brandclassArray.remove(m);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(4);
                                }
                            }
                            Log.e("salesList"," ---222--- "+salesList);

                        } else if (groupPosition == 1) //Category Group
                        {
                            for (int j = 0; j < planclassArray.size(); j++)
                            {
                                if (planclassArray.get(j).getCategory().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(2);
                                    planClassList.removeAll(planclassArray.get(j).getPlanclass());
                                    salesList.remove(mListDataGroup.get(1)+"."+planclassArray.get(j).getCategory());
                                    planclassArray.remove(j);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(2);
                                }
                            }
                            for (int k = 0; k < brandArray.size(); k++)
                            {
                                if (brandArray.get(k).getCategory().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(3);
                                    brandNameList.removeAll(brandArray.get(k).getBrand());
                                    salesList.remove(mListDataGroup.get(2)+"."+brandArray.get(k).getPlanclass());
                                    brandArray.remove(k);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(3);
                                }
                            }
                            for (int l = 0; l < brandclassArray.size(); l++)
                            {
                                if (brandclassArray.get(l).getCategory().equals(planCategoryName)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(4);
                                    brandplanclassList.removeAll(brandclassArray.get(l).getBrandClass());
                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(l).getBrand());
                                    brandclassArray.remove(l);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(4);
                                }
                            }
                            for (int m = 0; m < mcArray.size(); m++)
                            {
                                if (mcArray.get(m).getCategory().equals(planCategoryName)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(5);
                                    prodlevel6list.removeAll(mcArray.get(m).getPrdlevel6());
                                    salesList.remove(mListDataGroup.get(4)+"."+mcArray.get(m).getBrandClass());
                                    mcArray.remove(m);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(5);
                                }
                            }
                        }else if(groupPosition == 2)  // Plan Class Group
                        {
                            for (int i = 0; i < brandArray.size(); i++)
                            {
                                if (brandArray.get(i).getPlanclass().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(3);
                                    brandNameList.removeAll(brandArray.get(i).getBrand());
                                    salesList.remove(mListDataGroup.get(2)+"."+brandArray.get(i).getPlanclass());
                                    brandArray.remove(i);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(3);
                                }
                            }
                            for (int j = 0; j < brandclassArray.size(); j++)
                            {
                                if (brandclassArray.get(j).getPlanclass().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(4);
                                    brandplanclassList.removeAll(brandclassArray.get(j).getBrandClass());
                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(j).getBrand());
                                    brandclassArray.remove(j);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(4);
                                }
                            }
                            for (int k = 0; k < mcArray.size(); k++)
                            {
                                if (mcArray.get(k).getPlanclass().equals(txtClickedVal)) {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(5);
                                    prodlevel6list.removeAll(mcArray.get(k).getPrdlevel6());
                                    salesList.remove(mListDataGroup.get(4)+"."+mcArray.get(k).getBrandClass());
                                    mcArray.remove(k);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(5);
                                }
                            }

                        } else if(groupPosition == 3) //Brand
                        {
                            for(int i = 0;i < brandclassArray.size(); i++)
                            {
                                if(brandclassArray.get(i).getBrand().equals(txtClickedVal))
                                {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(4);
                                    brandplanclassList.removeAll(brandclassArray.get(i).getBrandClass());
                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(i).getBrand());
                                    brandclassArray.remove(i);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(4);
                                }
                            }
                            for(int j = 0;j < mcArray.size(); j++)
                            {
                                if(mcArray.get(j).getBrand().equals(txtClickedVal))
                                {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(5);
                                    prodlevel6list.removeAll(mcArray.get(j).getPrdlevel6());
                                    salesList.remove(mListDataGroup.get(4)+"."+mcArray.get(j).getBrandClass());
                                    mcArray.remove(j);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(5);
                                }
                            }
                        } else if(groupPosition == 4) //Brand Plan Class
                        {
                            for(int i = 0;i < mcArray.size(); i++)
                            {
                                if(mcArray.get(i).getBrandClass().equals(txtClickedVal))
                                {
                                    InventoryFilterActivity.pfilter_list.collapseGroup(5);
                                    prodlevel6list.removeAll(mcArray.get(i).getPrdlevel6());
                                    salesList.remove(mListDataGroup.get(4)+"."+mcArray.get(i).getBrandClass());
                                    mcArray.remove(i);
                                    cb.setChecked(false);
                                    InventoryFilterActivity.pfilter_list.expandGroup(5);
                                }
                            }
                        }
                     }
                }
            }

            );
            return convertView;
        }



    @Override
        public boolean isChildSelectable ( int groupPosition, int childPosition){
            return false;
        }

        @Override
        public boolean hasStableIds () {
            return false;
        }

        public final class GroupViewHolder {

            TextView mGroupText;
            ImageView mImage;
        }

        public final class ChildViewHolder {
            TextView mChildText;
            CheckBox mCheckBox;
        }

    
    
    // API declration used API 1.18
    public void requestInventoryCategoryAPI(int offsetvalue1, int limit1, final String deptName, final String groupname, final CheckBox cb) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String inv_category_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level="+level+"&dept=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.i("URL   ", inv_category_url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, inv_category_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inventory Category Filter Response", response.toString());
                        Log.e("Inventory category list","---"+response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no category data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String category = productName1.getString("planCategory");
                                    categorylist.add(category);
                                    tempcategorylist.add(category);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestInventoryCategoryAPI(offsetvalue, limit, deptName, groupname, cb);

                            } else {
                                if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String category = productName1.getString("planCategory");
                                        categorylist.add(category);
                                        tempcategorylist.add(category);

                                    }
                                    //Collections.sort(categorylist);
                                    mListDataChild.put(mListDataGroup.get(1), categorylist);
                                    ListCategory plancategory = new ListCategory();
                                    plancategory.setSubdept(deptName);
                                    plancategory.setCategory(tempcategorylist);
                                    categoryArray.add(plancategory);
                                    InventoryFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();
                                    Log.e("here ","----111----");
                                    
                                    salesList.add(groupname+"."+planDepartmentName);
                                    cb.setChecked(true);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    @SuppressLint("LongLogTag")

    // Plan class API
    public void requestInventoryPlanClassAPI(int offsetvalue1, int limit1, String plandeptName, final String category, final String groupname, final CheckBox cb) {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

         String inv_planclass_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level="+level +"&dept=" + planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("requestPlanClassAPI URL   ", inv_planclass_url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, inv_planclass_url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inventory Plan Class Filter Response", response.toString());
                        Log.e("Inventory plan class list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no plan class data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String planClass = productName1.getString("planClass");
                                        planClassList.add(planClass);
                                        tempplanclass.add(planClass);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryPlanClassAPI(offsetvalue, limit, planDepartmentName, category, groupname, cb);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String planClass = productName1.getString("planClass");
                                        planClassList.add(planClass);
                                        tempplanclass.add(planClass);

                                    }

                                    mListDataChild.put(mListDataGroup.get(2), planClassList);

                                    ListPlanClass planclass = new ListPlanClass();
                                    planclass.setSubdept(planDepartmentName);
                                    planclass.setCategory(category);
                                    planclass.setPlanclass(tempplanclass);
                                    planclassArray.add(planclass);
                                    InventoryFilterActivity.pfilter_list.expandGroup(2);
                                    InventoryFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();
                                    salesList.add(groupname+"."+category);
                                    cb.setChecked(true);
                                    Log.e("planClass size", " " + planclassArray.size());


                                }
                            }
                        } catch (Exception e) {
                            //Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    //Brand Name Api
    public void requestInventoryBrandNameAPI(int offsetvalue1, int limit1, String plandeptName, String category, final String planClass, final String groupname, final CheckBox cb) {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String inv_brand_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level="+ level +"&dept=" + planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("requestPlanClassAPI URL   ", inv_brand_url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, inv_brand_url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inventory Brand Filter Response", response.toString());
                        Log.e("Inventory brand list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no brand name data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brand_name = productName1.getString("brandName");
                                        brandNameList.add(brand_name);
                                        tempbrandname.add(brand_name);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryBrandNameAPI(offsetvalue, limit, planDepartmentName, planCategoryName,planClass, groupname, cb);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brand_name = productName1.getString("brandName");
                                        brandNameList.add(brand_name);
                                        tempbrandname.add(brand_name);
                                    }
                                    mListDataChild.put(mListDataGroup.get(3), brandNameList);

                                    ListBrand brandNm = new ListBrand();
                                    brandNm.setSubdept(planDepartmentName);
                                    brandNm.setCategory(planCategoryName);
                                    brandNm.setPlanclass(planClass);
                                    brandNm.setBrand(tempbrandname);
                                    brandArray.add(brandNm);

                                    InventoryFilterActivity.pfilter_list.expandGroup(3);
                                    InventoryFilterActivity.pfilter_list.expandGroup(2);
                                    InventoryFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();
                                    salesList.add(groupname+"."+planClass);
                                    cb.setChecked(true);
                                    Log.e("brand name size", " " + brandArray.size());

                             }
                            }
                        } catch (Exception e) {
                            //Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    //Brand Class Api
    public void requestInventoryBrandClassAPI(int offsetvalue1, int limit1, String plandeptName, String category, String planclass, final String brand, final String groupname, final CheckBox cb)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String inv_brandplanclass_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level="+level+"&dept="+ planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClassName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand="+brand.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("requestPlanClassAPI URL   ", inv_brandplanclass_url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, inv_brandplanclass_url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inv Brand Plan Class Filter Response", response.toString());
                        Log.e("Inv brand plan class list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no brand plan class data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brandPlanClass = productName1.getString("brandPlanClass");
                                        brandplanclassList.add(brandPlanClass);
                                        tempbrandplanclass.add(brandPlanClass);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryBrandClassAPI(offsetvalue, limit, planDepartmentName, planCategoryName,planClassName,brand, groupname, cb);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brandPlanClass = productName1.getString("brandPlanClass");
                                        brandplanclassList.add(brandPlanClass);
                                        tempbrandplanclass.add(brandPlanClass);

                                    }
                                    mListDataChild.put(mListDataGroup.get(4), brandplanclassList);

                                    ListBrandClass brandplancls = new ListBrandClass();
                                    brandplancls.setSubdept(planDepartmentName);
                                    brandplancls.setCategory(planCategoryName);
                                    brandplancls.setPlanclass(planClassName);
                                    brandplancls.setBrand(brand);
                                    brandplancls.setBrandClass(tempbrandplanclass);
                                    brandclassArray.add(brandplancls);

                                    InventoryFilterActivity.pfilter_list.expandGroup(4);
                                    InventoryFilterActivity.pfilter_list.expandGroup(3);
                                    InventoryFilterActivity.pfilter_list.expandGroup(2);
                                    InventoryFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();
                                    salesList.add(groupname+"."+brand);
                                    cb.setChecked(true);
                                    Log.e("brand plan class size", " " + brandclassArray.size());

                                }
                            }
                        } catch (Exception e) {
                            //Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    // Prod level 6 Api
    private void requestInventoryProdLevel6API(int offsetvalue1, int limit1, String planDept, String planCategory, String planClass, String brandnm, final String brandPlanClass, final String groupname, final CheckBox cb) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
      String url = ConstsCore.web_url +"/v1/display/salesanalysishierarchy/"+userId+"?level="+level+"&dept="+planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&class="+planClassName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand="+brand_Name.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brandclass="+brandPlanClass.replaceAll(" ", "%20").replaceAll("&", "%26")+"&offset=" + offsetvalue + "&limit=" + limit;

       // String inv_prodlevel6_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level="+level+"&dept="+ planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClassName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand= "+ brand_Name.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brandclass=" + brandPlanClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("requestPlanClassAPI URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inv Prod level 6 Filter Response", response.toString());
                        Log.e("Inv prod level 6 list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no brand plan class data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String mcCode = productName1.getString("prodLevel6Code");
                                        String mc = productName1.getString("prodLevel6Desc");
                                        prodlevel6list.add(mc);
                                        tempprodlevel6.add(mc);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryProdLevel6API(offsetvalue, limit, planDepartmentName, planCategoryName,planClassName,brand_Name,brandPlanClass, groupname, cb);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String mcCode = productName1.getString("prodLevel6Code");
                                        String mc = productName1.getString("prodLevel6Desc");
                                        prodlevel6list.add(mc);
                                        tempprodlevel6.add(mc);


                                    }
                                    mListDataChild.put(mListDataGroup.get(5), prodlevel6list);
                                    ListProdLevel6 mc = new ListProdLevel6();
                                    mc.setSubdept(planDepartmentName);
                                    mc.setCategory(planCategoryName);
                                    mc.setPlanclass(planClassName);
                                    mc.setBrand(brand_Name);
                                    mc.setBrandClass(brandPlanClass);
                                    mc.setPrdlevel6(tempprodlevel6);
                                    mcArray.add(mc);
                                    InventoryFilterActivity.pfilter_list.expandGroup(5);
                                    InventoryFilterActivity.pfilter_list.expandGroup(4);
                                    InventoryFilterActivity.pfilter_list.expandGroup(3);
                                    InventoryFilterActivity.pfilter_list.expandGroup(2);
                                    InventoryFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();
                                    salesList.add(groupname+"."+brandPlanClass);
                                    cb.setChecked(true);
                                    Log.e("array size", " " + mcArray.size());

                                }
                            }
                        } catch (Exception e) {
                            //Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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


