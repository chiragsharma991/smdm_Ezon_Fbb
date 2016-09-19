package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by dbhat on 15-03-2016.
 */
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
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

import apsupportapp.aperotechnologies.com.designapp.R;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint("UseSparseArrays")
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    ArrayList<FilterArray> arrfilter;
    int offsetvalue = 0, limit = 100, count = 0;
    List<String> productList;
    ArrayList productnamelist;
    String ProductName;
    ExpandableListView expandableListView;

    /*
     * Here we have a Hashmap containing a String key
     * (can be Integer or other type but I was testing
     * with contacts so I used contact name as the key)
    */
    private HashMap<String, List<String>> mListDataChild;

    // ArrayList that is what each key in the above
    // hashmap points to
    private ArrayList<String> mListDataGroup;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;
    private HashMap<Integer, boolean[]> mProductChildCheckStates;

    // Our getChildView & getGroupView use the viewholder patter
    // Here are the viewholders defined, the inner classes are
    // at the bottom
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    /*
          *  For the purpose of this document, I'm only using a single
     *	textview in the group (parent) and child, but you're limited only
     *	by your XML view for each group item :)
    */
    private String groupText;
    private String childText;

    /*  Here's the constructor we'll use to pass in our calling
     *  activity's context, group items, and child items
    */
    public ExpandableListAdapter(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView){

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;
        productList = new ArrayList<String>();
        productnamelist = new ArrayList();
        arrfilter = new ArrayList<FilterArray>();
        this. expandableListView = expandableListView;

        // Initialize our hashmap containing our check states here
        mChildCheckStates = new HashMap<Integer, boolean[]>();
        mProductChildCheckStates = new HashMap<Integer, boolean[]>();
    }

    public int getNumberOfCheckedItemsInGroup(int mGroupPosition)
    {
        boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
        int count = 0;
        if(getChecked != null) {
            for (int j = 0; j < getChecked.length; ++j) {
                if (getChecked[j] == true) count++;
            }
        }
        return  count;
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    /*
     * This defaults to "public object getGroup" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
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

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListGroupItems".
        //  Here is where I call the getter to get that text
        groupText = getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pfilter_list_group, null);

            // Initialize the GroupViewHolder defined at the bottom of this document
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
        }
        else {
            return 0;
        }
    }

    /*
     * This defaults to "public object getChild" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public String getChild(int groupPosition, int childPosition) {
        try
        {
            return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListChildItems".
        //  Here is where I call the getter to get that text
        childText = getChild(mGroupPosition, mChildPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pfilter_list_item, null);

            childViewHolder = new ChildViewHolder();

            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.txtsubdeptname);

            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.itemCheckBox);

            convertView.setTag(R.layout.pfilter_list_item, childViewHolder);

        } else {

            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.pfilter_list_item);
        }

        childViewHolder.mChildText.setText(childText);


		/*
		 * You have to set the onCheckChangedListener to null
		 * before restoring check states because each call to
		 * "setChecked" is accompanied by a call to the
		 * onCheckChangedListener
		*/
        //childViewHolder.mCheckBox.setOnCheckedChangeListener(null);
        convertView.setOnClickListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {


			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
			 * the value of the parent view (group) of this child (aka, the key),
			 * then retrive the boolean array getChecked[]
			*/
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]



            if(mChildPosition >= getChildrenCount(mGroupPosition))
            {
            }
            else
            {
                try
                {
                    if(mGroupPosition == 1)
                    {
                        childViewHolder.mCheckBox.setChecked(false);
                    }
                    else {
                        childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);
                    }

                }
                catch (Exception e)
                {

                }

            }



        } else {
            Log.e("does not contains grpPos "," "+mGroupPosition);
			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
			 * contain the value of the parent view (group) of this child (aka, the key),
			 * (aka, the key), then initialize getChecked[] as a new boolean array
			 *  and set it's size to the total number of children associated with
			 *  the parent group
			*/
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
            mChildCheckStates.put(mGroupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.mCheckBox.setChecked(false);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rel = (RelativeLayout)v;
                CheckBox cb = (CheckBox) rel.getChildAt(1);
                TextView txtView = (TextView) rel.getChildAt(0);
                String txtSubdept = txtView.getText().toString();//getChild(0,mChildPosition);
                ////Log.e("txtSubDept"," "+txtSubdept);
//
                if(mGroupPosition == 1)
                {
                    Log.e("mChildCheckStates for Group Product"," "+mChildCheckStates.get(1).length);
                    cb.setChecked(true);
                    Prod_FilterActivity.pfilter_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                    Intent intent = new Intent(mContext, KeyProductActivity.class);
                    intent.putExtra("filterproductname", txtSubdept);
                    mContext.startActivity(intent);
                    Prod_FilterActivity.filterActivity.finish();
                    return;
                }


                if (cb.isChecked() == false) {

                    ////Log.e("checkbox is not selected", "");

                    if (Reusable_Functions.chkStatus(mContext))
                    {

                        //Log.e("mChildCheckStates"," "+mChildCheckStates.get(0).length);
                        Prod_FilterActivity.pfilter_list.collapseGroup(1);
                        Reusable_Functions.sDialog(mContext, "Loading  data...");
                        offsetvalue = 0;
                        count = 0;
                        limit = 100;
                        productList = new ArrayList<String>();

                        //Log.e("text value "," "+txtSubdept);
                        requestFilterProductAPI(offsetvalue, limit, txtSubdept);
                        boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                        getChecked[mChildPosition] = true;
                        mChildCheckStates.put(mGroupPosition, getChecked);
                        cb.setChecked(true);

                    } else {
                        // Reusable_Functions.hDialog();
                        Toast.makeText(mContext, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }


                } else {
                    ////Log.e("checkbox is  selected", "");
                    ////Log.e("selected text value "," "+txtSubdept);

                    for (int i = 0; i < arrfilter.size(); i++) {
                        if (arrfilter.get(i).getSubdept().equals(txtSubdept)) {
                            Prod_FilterActivity.pfilter_list.collapseGroup(1);
                            productnamelist.removeAll(arrfilter.get(i).getprodarray());
                            arrfilter.remove(i);
                            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                            getChecked[mChildPosition] = false;
                            mChildCheckStates.put(mGroupPosition, getChecked);
                            cb.setChecked(false);
                            Prod_FilterActivity.pfilter_list.expandGroup(1);

                            return;
                        }
                    }



                }
            }
        });

//        if(mGroupPosition == 1)
//        {
//            RelativeLayout rel = (RelativeLayout)convertView;
//            CheckBox cb = (CheckBox) rel.getChildAt(1);
//            cb.setVisibility(View.INVISIBLE);
//
//
//            ExpandableListView expList = (ExpandableListView)rel.getParent();
//            Group group = (Group) expList.getChildAt(1);
//
//            childViewHolder.mCheckBox.setVisibility(View.INVISIBLE);
//        }

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

    public final class GroupViewHolder {

        TextView mGroupText;
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

                                    productList.add(ProductName);
                                    productnamelist.add(ProductName);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFilterProductAPI(offsetvalue, limit, subdeptName);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    ProductName = productName1.getString("productName");
                                    //Log.e("Product Name:", ProductName);

                                    productList.add(ProductName);
                                    productnamelist.add(ProductName);
                                }
                                Collections.sort(productnamelist);
                                mListDataChild.put(mListDataGroup.get(1), productnamelist);


                                FilterArray filterArray = new FilterArray();
                                filterArray.setSubdept(subdeptName);
                                filterArray.setprodArray((ArrayList) productList);
                                arrfilter.add(filterArray);
                                Prod_FilterActivity.pfilter_list.expandGroup(1);
                                Reusable_Functions.hDialog();


                            }

                        } catch (Exception e)
                        {
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


