package apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis;


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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.ListCategory;
import apsupportapp.aperotechnologies.com.designapp.model.ListPlanClass;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint("UseSparseArrays")
public class ExpandableList extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    ArrayList<FilterArray> arrfilter;
    int offsetvalue = 0, limit = 100, count = 0;
    ArrayList productnamelist, articleOptionList;
    String ProductName, ArticleOption;
    ExpandableListView expandableListView;
    List<String> salesList;

    List<ListCategory> categoryArray;
    List<ListPlanClass> planclassArray;
    List<ListCategory> brandArray;
    List<ListCategory> brandclassArray;
    List tempcategorylist;
    List tempplanclass;


    private HashMap<String, List<String>> mListDataChild;

    private ArrayList<String> mListDataGroup;


    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    private String groupText;
    private String childText;
    ExpandableList listAdapter;

    public ExpandableList(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, ExpandableList listAdapter) {

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;
        productnamelist = new ArrayList();
        arrfilter = new ArrayList<FilterArray>();
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        articleOptionList = new ArrayList();
        salesList= new ArrayList<>();
        categoryArray = new ArrayList();
        planclassArray = new ArrayList();

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
            convertView = inflater.inflate(R.layout.sfilter_list_group, null);

            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);
            groupViewHolder.mImage = (ImageView) convertView.findViewById(R.id.groupImage);
            int imageId = SalesFilterActivity.groupImages.get(groupPosition);
            groupViewHolder.mImage.setImageResource(imageId);
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
    public String getChild(int groupPosition, int childPosition)
    {
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

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sfilter_list_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.txtdeptname);
            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.itemCheckBox);
            convertView.setTag(R.layout.sfilter_list_item, childViewHolder);
            //childViewHolder.mCheckBox.setTag(childPosition);

        }
        else
        {
            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.sfilter_list_item);
        }

        childViewHolder.mChildText.setText(childText);
//        for (int i = 0; i < salesList.size(); i++)
//        {
//            Log.e("saleslist"," "+salesList.get(i).getValue() +" "+childText);
//
//            if (salesList.get(i).getValue().contains(childText))
//            {
//                childViewHolder.mCheckBox.setChecked(salesList.get(i).getCheck());
//            }
//            else
//            {
//                childViewHolder.mCheckBox.setChecked(false);
//            }
//
//        }

        if (salesList.contains(childText))
        {
            childViewHolder.mCheckBox.setChecked(true);
        }
        else
        {
            childViewHolder.mCheckBox.setChecked(false);
        }

        convertView.setOnClickListener(null);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rel = (RelativeLayout)v;
                CheckBox cb = (CheckBox) rel.getChildAt(1);
                TextView txtView = (TextView) rel.getChildAt(0);
                String txtClickedVal = txtView.getText().toString();


                if (cb.isChecked() == false)
                {
                    ////Log.e("checkbox is not selected", "");
                    if (Reusable_Functions.chkStatus(mContext))
                    {

                        Reusable_Functions.sDialog(mContext, "Loading  data...");
                        offsetvalue = 0;
                        count = 0;
                        limit = 100;
                        if(groupPosition == 0)
                        {
                            tempcategorylist = new ArrayList();
                            SalesFilterActivity.pfilter_list.collapseGroup(1);
                            requestFilterProductAPI(offsetvalue, limit, txtClickedVal);
                        }
                        else if(groupPosition == 1)
                        {
                            tempplanclass = new ArrayList();
                            SalesFilterActivity.pfilter_list.collapseGroup(2);
                            requestProductArticleAPI(offsetvalue, limit, txtClickedVal);
                        }

                        salesList.add(txtClickedVal);
                        cb.setChecked(true);


                    }
                    else
                    {
                        Toast.makeText(mContext, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }


                }
                else
                {

                    Log.e("groupPosition "," "+groupPosition);

                    if(groupPosition == 0)
                    {
                        Log.e("here ", " "+planclassArray.size());


//
                        for (int i = 0; i < categoryArray.size(); i++) {
                            if (categoryArray.get(i).getSubdept().equals(txtClickedVal)) {
                                SalesFilterActivity.pfilter_list.collapseGroup(1);
                                productnamelist.removeAll(categoryArray.get(i).getCategory());
                                salesList.remove(categoryArray.get(i).getSubdept());
                                categoryArray.remove(i);
                                cb.setChecked(false);
                                SalesFilterActivity.pfilter_list.expandGroup(1);


                            }
                        }

                        for (int k = 0; k < planclassArray.size(); k++)
                        {
                            Log.e("sub ", " "+planclassArray.get(k).getSubdept()+" "+planclassArray.get(k).getSubdept().equals(txtClickedVal));
                            if (planclassArray.get(k).getSubdept().equals(txtClickedVal))
                            {
                                SalesFilterActivity.pfilter_list.collapseGroup(2);
                                Log.e("plan list"," === "+planclassArray.get(k).getPlanclass() +" "+planclassArray.get(k).getCategory());
                                articleOptionList.removeAll(planclassArray.get(k).getPlanclass());
                                salesList.remove(planclassArray.get(k).getCategory());
                                planclassArray.remove(planclassArray.get(k).getSubdept());
                                cb.setChecked(false);
                                SalesFilterActivity.pfilter_list.expandGroup(2);


                            }

                        }
//

                        Log.e("planclassArray size "," "+planclassArray.size());
                    }

                    else if(groupPosition == 1)
                    {
                        for (int j = 0; j < planclassArray.size(); j++)
                        {
                            if (planclassArray.get(j).getCategory().equals(txtClickedVal)) {
                                SalesFilterActivity.pfilter_list.collapseGroup(2);
                                articleOptionList.removeAll(planclassArray.get(j).getPlanclass());
                                salesList.remove(planclassArray.get(j).getCategory());
                                planclassArray.remove(j);
                                cb.setChecked(false);
                                SalesFilterActivity.pfilter_list.expandGroup(2);
                            }

                        }


                    }



                }


            }
        });
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

    public void requestFilterProductAPI(int offsetvalue1, int limit1, final String subdeptName) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?view=productName&prodLevel3Desc=" + subdeptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset" + offsetvalue + "&limit" + limit;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sub Dept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    ProductName = productName1.getString("productName");
                                    //Log.e("Product Name:", ProductName);

                                    productnamelist.add(ProductName);
                                    tempcategorylist.add(ProductName);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFilterProductAPI(offsetvalue, limit, subdeptName);


                            } else {
                                if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);
                                        ProductName = productName1.getString("productName");
                                        //Log.e("Product Name:", ProductName);

                                        productnamelist.add(ProductName);
                                        tempcategorylist.add(ProductName);

                                    }
                                    Collections.sort(productnamelist);
                                    mListDataChild.put(mListDataGroup.get(1), productnamelist);
                                    ListCategory category = new ListCategory();
                                    category.setSubdept(subdeptName);
                                    category.setCategory(tempcategorylist);
                                    categoryArray.add(category);


                                    SalesFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();
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

    public void requestProductArticleAPI(int offsetvalue1, int limit1, final String prodName) {

        final String[] prodLevel3Desc = {""};

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?view=articleOption&productName=" + prodName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("requestProductArticleAPI URL   ", url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("articleOptionList"," "+articleOptionList.size());

                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no article data found", Toast.LENGTH_LONG).show();
                            } else {
                                if (response.length() == limit)
                                {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);
                                        //Log.e("Product Name:", ProductName);
                                        prodLevel3Desc[0] = productName1.getString("prodLevel3Desc");
                                        ArticleOption = productName1.getString("articleOption");
                                        tempplanclass.add(ArticleOption);
                                        articleOptionList.add(ArticleOption);
                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestProductArticleAPI(offsetvalue, limit, prodName);
                                }
                                else if (response.length() < limit)
                                {
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        JSONObject productName1 = response.getJSONObject(i);
                                        prodLevel3Desc[0] = productName1.getString("prodLevel3Desc");
                                        ArticleOption = productName1.getString("articleOption");
                                        tempplanclass.add(ArticleOption);
                                        articleOptionList.add(ArticleOption);
                                    }
                                    //Collections.sort(articleOptionList);
                                    mListDataChild.put(mListDataGroup.get(2), articleOptionList);

                                    ListPlanClass planclass = new ListPlanClass();
                                    planclass.setSubdept(prodLevel3Desc[0]);
                                    planclass.setCategory(prodName);
                                    planclass.setPlanclass(tempplanclass);
                                    planclassArray.add(planclass);

                                    SalesFilterActivity.pfilter_list.expandGroup(2);
                                    SalesFilterActivity.pfilter_list.expandGroup(1);
                                    Reusable_Functions.hDialog();


                                    Log.e("planClass size"," "+planclassArray.size());


                                    for(int i = 0; i < planclassArray.size(); i++)
                                    {
                                        Log.e("---getSubdept---"," "+planclassArray.get(i).getSubdept()+" ");
                                        Log.e("---getCategory---"," "+planclassArray.get(i).getCategory()+" ");
                                        Log.e("---getPlanClassList ---"," "+planclassArray.get(i).getPlanclass()+" ");
                                        Log.e("---getPlanClassListSize ---"," "+planclassArray.get(i).getPlanclass().size()+" ");
                                    }

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


