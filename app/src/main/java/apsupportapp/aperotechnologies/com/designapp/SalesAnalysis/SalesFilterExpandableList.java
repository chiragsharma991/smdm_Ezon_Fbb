package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.ListBrand;
import apsupportapp.aperotechnologies.com.designapp.model.ListBrandClass;
import apsupportapp.aperotechnologies.com.designapp.model.ListCategory;
import apsupportapp.aperotechnologies.com.designapp.model.ListPlanClass;

import static com.crashlytics.android.Crashlytics.TAG;


public class SalesFilterExpandableList extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;

    int offsetvalue = 0, limit = 100, count = 0, level;
    ArrayList categorylist, articleOptionList, planClassList, brandNameList, brandplanclassList;

    ExpandableListView expandableListView;
    List<String> salesList;
    static  String txtClickedVal;
    //    List<ListCategory> categoryArray;
//    List<ListPlanClass> planclassArray;
//    List<ListBrand> brandArray;
//    List<ListBrandClass> brandclassArray;

    private HashMap<String, List<String>> mListDataChild, dublicate_listDataChild;
    private List<String> mListDataGroup;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private String groupText;
    private String childText;
    SalesFilterExpandableList listAdapter;
    Boolean flag = false;
    //  SFilter sFilter;

    public SalesFilterExpandableList(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, SalesFilterExpandableList listAdapter) {

        mContext = context;
        this.mListDataGroup = listDataGroup;
        this.mListDataChild = listDataChild;
        categorylist = new ArrayList();
        planClassList = new ArrayList();
        brandNameList = new ArrayList();
        brandplanclassList = new ArrayList();
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        articleOptionList = new ArrayList();
        salesList = new ArrayList<>();
        this.dublicate_listDataChild = new HashMap<String, List<String>>();
        this.dublicate_listDataChild.putAll(mListDataChild);
//        categoryArray = new ArrayList();
//        planclassArray = new ArrayList();
//        brandArray = new ArrayList();
//        brandclassArray = new ArrayList();
//        subCategory = new ArrayList<String>();
        flag = false;
    }

    @Override
    public int getGroupCount() {
        return mListDataChild.size();
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
            convertView = inflater.inflate(R.layout.sfilter_list_group, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);

            convertView.setTag(groupViewHolder);

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
            convertView = inflater.inflate(R.layout.sfilter_list_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.txtdeptname);
//            childViewHolder.mCheckBox = (CheckBox) convertView
//                    .findViewById(R.id.itemCheckBox);
            convertView.setTag(R.layout.sfilter_list_item, childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.sfilter_list_item);
        }

        childViewHolder.mChildText.setText(childText);

//        if (salesList.contains(mListDataGroup.get(groupPosition) + "." + childText)) {
//            childViewHolder.mCheckBox.setChecked(true);
//        } else {
//            childViewHolder.mCheckBox.setChecked(false);
//        }


        convertView.setOnClickListener(null);


        convertView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {



                                               RelativeLayout rel = (RelativeLayout) v;
                                               //CheckBox cb = (CheckBox) rel.getChildAt(1);
                                               TextView txtView = (TextView) rel.getChildAt(0);
                                               txtClickedVal= txtView.getText().toString();
                                               v.setBackgroundColor(R.color.bel_lightgrey_text);

//                                               if (cb.isChecked() == false) {
//
//                                                  salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
//                                                   Log.e(TAG,"salesList"+salesList);
//                                                   cb.setChecked(true);
//                                               } else {
//                                                   salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
//                                                   cb.setChecked(false);
//                                               }
//                if (cb.isChecked() == false) {
//                    ////Log.e("checkbox is not selected", "");
//                    if (Reusable_Functions.chkStatus(mContext)) {
//
//                        Reusable_Functions.sDialog(mContext, "Loading  data...");
//                        offsetvalue = 0;
//                        count = 0;
//                        limit = 100;
//                        if (groupPosition == 0) {
//                            tempcategorylist = new ArrayList();
//                            SalesFilterActivity.pfilter_list.collapseGroup(1);
//                            planDepartmentName = txtClickedVal;
//                            Log.i("click dept value",""+planDepartmentName);
//                            String groupname = mListDataGroup.get(0);
//                            requestCategoryAPI(offsetvalue, limit, planDepartmentName, groupname, cb);
//
//                        }
//                        else if (groupPosition == 1)
//                        {
//
//                            tempplanclass = new ArrayList();
//                            SalesFilterActivity.pfilter_list.collapseGroup(2);
//                            planCategoryName = txtClickedVal;
//                            Log.i("click category value---",""+planCategoryName);
//                            String groupname = mListDataGroup.get(1);
//
//                            requestPlanClassAPI(offsetvalue, limit, planDepartmentName, txtClickedVal, groupname, cb);
//
//
//                        }
//                        else if (groupPosition == 2)
//                        {
//
//                            tempbrandname = new ArrayList();
//                            SalesFilterActivity.pfilter_list.collapseGroup(3);
//                            planClassName = txtClickedVal;
//                            Log.i("click brand name value --- ",""+planClassName);
//                            String groupname = mListDataGroup.get(2);
//                            requestBrandNameAPI(offsetvalue, limit,planDepartmentName, planCategoryName, txtClickedVal, groupname, cb);
//
//                        }
//                        else if(groupPosition == 3)
//                        {
//                            tempbrandplanclass = new ArrayList();
//                            SalesFilterActivity.pfilter_list.collapseGroup(4);
//                            String groupname = mListDataGroup.get(3);
//                            requestBrandClassNameAPI(offsetvalue, limit,planDepartmentName,planCategoryName,planClassName, txtClickedVal, groupname, cb);
//                        }
//
////                        if(flag == true)
////                        {
////                            salesList.add(mListDataGroup.get(groupPosition)+"."+txtClickedVal);
////                            cb.setChecked(true);
////                        }
//
//
//                        } else {
//                            Toast.makeText(mContext, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                        }
//                    }
//            else {
//                        Log.e("groupPosition ", " " + groupPosition);
//
//                        if (groupPosition == 0) {
//                            //Log.e("here ", " " + planclassArray.size());
//                            // category array
//                            for (int i = 0; i < categoryArray.size(); i++) {
//                                if (categoryArray.get(i).getSubdept().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(1);
//                                    categorylist.removeAll(categoryArray.get(i).getCategory());
//                                    Log.e("salesList"," ---111--- "+salesList);
//                                    salesList.remove(mListDataGroup.get(0)+"."+categoryArray.get(i).getSubdept());
//                                    categoryArray.remove(i);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(1);
//
//                                }
//                            }
//
//                            for (int k = 0; k < planclassArray.size(); k++) {
//                                Log.e("sub ", " " + planclassArray.get(k).getSubdept() + " " + planclassArray.get(k).getSubdept().equals(txtClickedVal));
//                                if (planclassArray.get(k).getSubdept().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(2);
//                                    Log.e("plan list", " === " + planclassArray.get(k).getPlanclass() + " " + planclassArray.get(k).getCategory());
//                                    planClassList.removeAll(planclassArray.get(k).getPlanclass());
//                                    salesList.remove(mListDataGroup.get(1)+"."+planclassArray.get(k).getCategory());
//                                    planclassArray.remove(k);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(2);
//                                }
//                            }
//                            for (int j = 0; j < brandArray.size(); j++) {
//                                   if (brandArray.get(j).getSubdept().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(3);
//                                    brandNameList.removeAll(brandArray.get(j).getBrand());
//                                    salesList.remove(mListDataGroup.get(2)+"."+brandArray.get(j).getPlanclass());
//                                    brandArray.remove(j);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(3);
//                                }
//                            }
//                            for (int m = 0;  m < brandclassArray.size(); m++) {
//
//                                if (brandclassArray.get(m).getSubdept().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(4);
//                                    brandplanclassList.removeAll(brandclassArray.get(m).getBrandClass());
//                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(m).getBrand());
//                                    brandclassArray.remove(m);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(4);
//                                }
//                            }
//                            Log.e("salesList"," ---222--- "+salesList);
//
//                        } else if (groupPosition == 1)
//                        {
//                            for (int j = 0; j < planclassArray.size(); j++)
//                            {
//                                if (planclassArray.get(j).getCategory().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(2);
//                                    planClassList.removeAll(planclassArray.get(j).getPlanclass());
//                                    salesList.remove(mListDataGroup.get(1)+"."+planclassArray.get(j).getCategory());
//                                    planclassArray.remove(j);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(2);
//                                }
//                            }
//                            for (int k = 0; k < brandArray.size(); k++)
//                            {
//                                if (brandArray.get(k).getCategory().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(3);
//                                    brandNameList.removeAll(brandArray.get(k).getBrand());
//                                    salesList.remove(mListDataGroup.get(2)+"."+brandArray.get(k).getPlanclass());
//                                    brandArray.remove(k);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(3);
//                                }
//                            }
//                            for (int l = 0; l < brandclassArray.size(); l++)
//                            {
//                                if (brandclassArray.get(l).getCategory().equals(planCategoryName)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(4);
//                                    brandplanclassList.removeAll(brandclassArray.get(l).getBrandClass());
//                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(l).getBrand());
//                                    brandclassArray.remove(l);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(4);
//                                }
//                            }
//                        }else if(groupPosition == 2)
//                        {
//                            for (int i = 0; i < brandArray.size(); i++)
//                            {
//                                if (brandArray.get(i).getPlanclass().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(3);
//                                    brandNameList.removeAll(brandArray.get(i).getBrand());
//                                    salesList.remove(mListDataGroup.get(2)+"."+brandArray.get(i).getPlanclass());
//                                    brandArray.remove(i);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(3);
//                                }
//                            }
//                            for (int j = 0; j < brandclassArray.size(); j++)
//                            {
//                                if (brandclassArray.get(j).getPlanclass().equals(txtClickedVal)) {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(4);
//                                    brandplanclassList.removeAll(brandclassArray.get(j).getBrandClass());
//                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(j).getBrand());
//                                    brandclassArray.remove(j);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(4);
//                                }
//                            }
//
//                        } else if(groupPosition == 3)
//                        {
//                            for(int i = 0;i < brandclassArray.size(); i++)
//                            {
//                                if(brandclassArray.get(i).getBrand().equals(txtClickedVal))
//                                {
//                                    SalesFilterActivity.pfilter_list.collapseGroup(4);
//                                    brandplanclassList.removeAll(brandclassArray.get(i).getBrandClass());
//                                    salesList.remove(mListDataGroup.get(3)+"."+brandclassArray.get(i).getBrand());
//                                    brandclassArray.remove(i);
//                                    cb.setChecked(false);
//                                    SalesFilterActivity.pfilter_list.expandGroup(4);
//                                }
//                            }
//                        }
//
//
                                           }
                                           // }
                                       }
        );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void filterData(String query) {


        String charText = query.toLowerCase(Locale.getDefault());
        mListDataChild.clear();
        if (charText.length() == 0) {
            Log.e("add in list: ", "" + dublicate_listDataChild.size());
            mListDataChild.putAll(dublicate_listDataChild);
            //Collapse Group
            SalesFilterActivity.pfilter_list.collapseGroup(0);
            SalesFilterActivity.pfilter_list.collapseGroup(1);
            SalesFilterActivity.pfilter_list.collapseGroup(2);
            SalesFilterActivity.pfilter_list.collapseGroup(3);
            SalesFilterActivity.pfilter_list.collapseGroup(4);

//            //Expand Group
//            SalesFilterActivity.pfilter_list.expandGroup(0);
//            SalesFilterActivity.pfilter_list.expandGroup(1);
//            SalesFilterActivity.pfilter_list.expandGroup(2);
//            SalesFilterActivity.pfilter_list.expandGroup(3);
//            SalesFilterActivity.pfilter_list.expandGroup(4);
        } else {
            // for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                List<String> arrayList = new ArrayList<String>();

                for (int k = 0; k < dublicate_listDataChild.get(mListDataGroup.get(j)).size(); k++) {
                    if (dublicate_listDataChild.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayList.add(dublicate_listDataChild.get(mListDataGroup.get(j)).get(k));

                    }
                }
                mListDataChild.put(mListDataGroup.get(j), arrayList);
                SalesFilterActivity.pfilter_list.expandGroup(0);
                SalesFilterActivity.pfilter_list.expandGroup(1);
                SalesFilterActivity.pfilter_list.expandGroup(2);
                SalesFilterActivity.pfilter_list.expandGroup(3);
                SalesFilterActivity.pfilter_list.expandGroup(4);

            }

            notifyDataSetChanged();

        }

        Log.e("After notifying filterData size : ", "" + mListDataChild.get(mListDataGroup.get(0)).size());

    }

    public final class GroupViewHolder {

        TextView mGroupText;
        ImageView mImage;
    }

    public final class ChildViewHolder {
        TextView mChildText;
        CheckBox mCheckBox;
    }

//    @Override
//    public Filter getFilter() {
//        if (sFilter == null) {
//            sFilter = new SalesFilterExpandableList.SFilter();
//        }
//        return sFilter;
//    }
//
//    public class SFilter extends Filter {
//
//        //Invoked in a worker thread to filter the data according to the constraint.
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            FilterResults results = new FilterResults();
//
//
//            mFilterList = SalesFilterActivity.subdept;
//            Log.e("Filter Array Size", "" + mFilterList.size());
//
//            if (constraint != null && constraint.length() > 0) {
//                ArrayList<String> filterList = new ArrayList<String>();
//                for (int i = 0; i < mFilterList.size(); i++) {
//                    if (mFilterList.get(i).toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        filterList.add(mFilterList.get(i));
//                        Log.e("List after filter", "" + filterList.size());
//                    }
//                }
//                results.count = filterList.size();
//                results.values = filterList;
//            } else {
//                results.count = mFilterList.size();
//                results.values = mFilterList;
//            }
//            return results;
//        }
//
//        //Invoked in the UI thread to publish the filtering results in the user interface.
//        @SuppressWarnings("unchecked")
//
//        @Override
//        protected void publishResults(CharSequence constraint,
//                                      FilterResults results) {
//            mFilterList = (ArrayList<String>) results.values;
//            SalesFilterActivity.listAdapter.notifyDataSetChanged();
//
//        }
//    }


//    public void requestCategoryAPI(int offsetvalue1, int limit1, final String deptName, final String groupname, final CheckBox cb) {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String category_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=PCA&dept=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
//
//        Log.i("URL   ", category_url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, category_url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.e("Category Filter Response", response.toString());
//                        Log.e("category list","---"+response.length());
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(mContext, "no category data found", Toast.LENGTH_LONG).show();
//                            } else if (response.length() == limit) {
//                                Reusable_Functions.hDialog();
//
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject productName1 = response.getJSONObject(i);
//
//                                    String category = productName1.getString("planCategory");
//                                    categorylist.add(category);
//                                    tempcategorylist.add(category);
//
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestCategoryAPI(offsetvalue, limit, deptName, groupname, cb);
//
//                            } else {
//                                if (response.length() < limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String category = productName1.getString("planCategory");
//                                        categorylist.add(category);
//                                        tempcategorylist.add(category);
//
//                                    }
//                                    //Collections.sort(categorylist);
//                                    mListDataChild.put(mListDataGroup.get(1), categorylist);
//                                    ListCategory plancategory = new ListCategory();
//                                    plancategory.setSubdept(deptName);
//                                    plancategory.setCategory(tempcategorylist);
//                                    categoryArray.add(plancategory);
//                                    SalesFilterActivity.pfilter_list.expandGroup(1);
//                                    Reusable_Functions.hDialog();
//                                    Log.e("here ","----111----");
//
//                                    salesList.add(groupname+"."+planDepartmentName);
//                                    cb.setChecked(true);
//                                }
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
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

//    @SuppressLint("LongLogTag")
//
//
//    public void requestPlanClassAPI(int offsetvalue1, int limit1, String plandeptName, final String category, final String groupname, final CheckBox cb) {
//
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//         String planclass_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level=PCL&dept=" + planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&category="+category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
//
//        Log.e("requestPlanClassAPI URL   ", planclass_url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, planclass_url,
//                new Response.Listener<JSONArray>() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.e("Plan Class Filter Response", response.toString());
//                        Log.e("plan class list", " " + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(mContext, "no plan class data found", Toast.LENGTH_LONG).show();
//
//                            } else {
//                                if (response.length() == limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String planClass = productName1.getString("planClass");
//                                        planClassList.add(planClass);
//                                        tempplanclass.add(planClass);
//
//                                    }
//                                    offsetvalue = (limit * count) + limit;
//                                    count++;
//                                    requestPlanClassAPI(offsetvalue, limit, planDepartmentName, category, groupname, cb);
//
//                                } else if (response.length() < limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String planClass = productName1.getString("planClass");
//                                        planClassList.add(planClass);
//                                        tempplanclass.add(planClass);
//
//                                    }
//
//                                    mListDataChild.put(mListDataGroup.get(2), planClassList);
//
//                                    ListPlanClass planclass = new ListPlanClass();
//                                    planclass.setSubdept(planDepartmentName);
//                                    planclass.setCategory(category);
//                                    planclass.setPlanclass(tempplanclass);
//                                    planclassArray.add(planclass);
//                                    SalesFilterActivity.pfilter_list.expandGroup(2);
//                                    SalesFilterActivity.pfilter_list.expandGroup(1);
//                                    Reusable_Functions.hDialog();
//                                    salesList.add(groupname+"."+category);
//                                    cb.setChecked(true);
//                                    Log.e("planClass size", " " + planclassArray.size());
//
//
//                                }
//                            }
//                        } catch (Exception e) {
//                            //Log.e("Exception e", e.toString() + "");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        error.printStackTrace();
//                    }
//                }
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
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }


//    public void requestBrandNameAPI(int offsetvalue1, int limit1, String plandeptName, String category, final String planClass, final String groupname, final CheckBox cb) {
//
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String brand_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level=BRN&dept=" + planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
//
//        Log.e("requestPlanClassAPI URL   ", brand_url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, brand_url,
//                new Response.Listener<JSONArray>() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.e("Brand Filter Response", response.toString());
//                        Log.e("brand list", " " + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(mContext, "no brand name data found", Toast.LENGTH_LONG).show();
//
//                            } else {
//                                if (response.length() == limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String brand_name = productName1.getString("brandName");
//                                        brandNameList.add(brand_name);
//                                        tempbrandname.add(brand_name);
//
//                                    }
//                                    offsetvalue = (limit * count) + limit;
//                                    count++;
//                                    requestBrandNameAPI(offsetvalue, limit, planDepartmentName, planCategoryName,planClass, groupname, cb);
//
//                                } else if (response.length() < limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String brand_name = productName1.getString("brandName");
//                                        brandNameList.add(brand_name);
//                                        tempbrandname.add(brand_name);
//                                    }
//                                    mListDataChild.put(mListDataGroup.get(3), brandNameList);
//
//                                    ListBrand brandNm = new ListBrand();
//                                    brandNm.setSubdept(planDepartmentName);
//                                    brandNm.setCategory(planCategoryName);
//                                    brandNm.setPlanclass(planClass);
//                                    brandNm.setBrand(tempbrandname);
//                                    brandArray.add(brandNm);
//
//                                    SalesFilterActivity.pfilter_list.expandGroup(3);
//                                    SalesFilterActivity.pfilter_list.expandGroup(2);
//                                    SalesFilterActivity.pfilter_list.expandGroup(1);
//                                    Reusable_Functions.hDialog();
//                                    salesList.add(groupname+"."+planClass);
//                                    cb.setChecked(true);
//                                    Log.e("brand name size", " " + brandArray.size());
//
//                             }
//                            }
//                        } catch (Exception e) {
//                            //Log.e("Exception e", e.toString() + "");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        error.printStackTrace();
//                    }
//                }
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
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }

//    public void requestBrandClassNameAPI(int offsetvalue1, int limit1, String plandeptName, String category, String planclass, final String brand, final String groupname, final CheckBox cb) {
//
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String brandplanclass_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level=BPC&dept="+ planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClassName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand="+brand.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
//        Log.e("requestPlanClassAPI URL   ", brandplanclass_url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, brandplanclass_url,
//                new Response.Listener<JSONArray>() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.e("Brand Plan Class Filter Response", response.toString());
//                        Log.e("brand plan class list", " " + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(mContext, "no brand plan class data found", Toast.LENGTH_LONG).show();
//
//                            } else {
//                                if (response.length() == limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String brandPlanClass = productName1.getString("brandPlanClass");
//                                        brandplanclassList.add(brandPlanClass);
//                                        tempbrandplanclass.add(brandPlanClass);
//
//                                    }
//                                    offsetvalue = (limit * count) + limit;
//                                    count++;
//                                    requestBrandClassNameAPI(offsetvalue, limit, planDepartmentName, planCategoryName,planClassName,brand, groupname, cb);
//
//                                } else if (response.length() < limit) {
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject productName1 = response.getJSONObject(i);
//
//                                        String brandPlanClass = productName1.getString("brandPlanClass");
//                                        brandplanclassList.add(brandPlanClass);
//                                        tempbrandplanclass.add(brandPlanClass);
//
//                                    }
//                                    mListDataChild.put(mListDataGroup.get(4), brandplanclassList);
//
//                                    ListBrandClass brandplancls = new ListBrandClass();
//                                    brandplancls.setSubdept(planDepartmentName);
//                                    brandplancls.setCategory(planCategoryName);
//                                    brandplancls.setPlanclass(planClassName);
//                                    brandplancls.setBrand(brand);
//                                    brandplancls.setBrandClass(tempbrandplanclass);
//                                    brandclassArray.add(brandplancls);
//
//                                    SalesFilterActivity.pfilter_list.expandGroup(4);
//                                    SalesFilterActivity.pfilter_list.expandGroup(3);
//                                    SalesFilterActivity.pfilter_list.expandGroup(2);
//                                    SalesFilterActivity.pfilter_list.expandGroup(1);
//                                    Reusable_Functions.hDialog();
//                                    salesList.add(groupname+"."+brand);
//                                    cb.setChecked(true);
//                                    Log.e("brand plan class size", " " + brandclassArray.size());
//
//                                }
//                            }
//                        } catch (Exception e) {
//                            //Log.e("Exception e", e.toString() + "");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        error.printStackTrace();
//                    }
//                }
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
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }


}


