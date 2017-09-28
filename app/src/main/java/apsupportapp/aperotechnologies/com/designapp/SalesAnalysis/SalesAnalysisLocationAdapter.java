package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter.explv_an_prod;


/**
 * Created by pamrutkar on 08/06/17.
 */
public class SalesAnalysisLocationAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    SalesAnalysisFilter salesAnalysisFilter;

    int offsetvalue = 0, limit = 100, count = 0;
    ExpandableListView expandableListView;
    List<String> salesList;
    String txtClickedVal;

    private HashMap<String, List<String>> mListDataChild;
    HashMap<String, List<String>> dublicate_listDataChild, dublicate_listDataChild2;
    private List<String> mListDataGroup;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private String groupText;
    private String childText;
    int location_level;
    List region_list, store_List;
    SalesAnalysisLocationAdapter listAdapter;
    int mGroupPosition = 0;
    int mChildPosition = 0;
    boolean region_flg = false, store_flg = false;
    public static String an_store_str = "";


    public SalesAnalysisLocationAdapter(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, SalesAnalysisLocationAdapter listAdapter) {

        mContext = context;
        this.mListDataGroup = listDataGroup;
        this.mListDataChild = listDataChild;
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        salesList = new ArrayList<>();
        this.dublicate_listDataChild = new HashMap<String, List<String>>();
        this.dublicate_listDataChild.putAll(mListDataChild);
        salesAnalysisFilter = new SalesAnalysisFilter();
        region_list = new ArrayList();
        store_List = new ArrayList();
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

        }
        else
        {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (mListDataChild.get(mListDataGroup.get(groupPosition)) != null)
        {
            return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
        }
        else
        {
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        mGroupPosition = groupPosition;
        mChildPosition = childPosition;

        childText = getChild(mGroupPosition, mChildPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sfilter_list_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.txtdeptname);
            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.itemCheckBox);

            //set tag for level
            childViewHolder.mCheckBox.setTag(groupPosition);
            convertView.setTag(R.layout.sfilter_list_item, childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.sfilter_list_item);
        }

        childViewHolder.mChildText.setText(childText);

        if (salesList.contains(mListDataGroup.get(groupPosition) + "." + childText)) {
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
                                               txtClickedVal = txtView.getText().toString();
                                               location_level = 3;
                                               if (!cb.isChecked()) {
                                                   salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(true);
                                                  // salesAnalysisFilter.rel_an_process_filter.setVisibility(View.G);

                                                   BuildUP(groupPosition);

                                               } else if (cb.isChecked())
                                               {
                                                   salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(false);
                                                   removeBuildUP(groupPosition);
                                               }
                                           }
                                       }
        );
        return convertView;
    }

    private void removeBuildUP(int level) {

//        if (level == 0)
//        {
//            Log.e("welcome,","------------"+region_list.size());
//            region_list.remove(txtClickedVal.trim());
//            String[] array = (String[]) region_list.toArray(new String[0]);
//            String str1 = Arrays.toString(array);
//            str1 = str1.replace("[", "");
//            str1 = str1.replace("]", "");
//            str1 = str1.replace(", ", ",");
//            an_region_str = str1;
//            Log.e("remove build up :", "" + region_list.size());
//        }
        if (level == 0)
        {
            Log.e("remove in store---","");
            store_List.remove(txtClickedVal.substring(0,4).trim());
            String[] array = (String[]) store_List.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_store_str = str1;
            Log.e("remove build up store:", "" + store_List.size());
        }

    }

    private void BuildUP(int level) {
//        if (level == 0)
//        {
//            if (!region_flg)
//            {
//                region_list.add(txtClickedVal.trim());
//                region_flg = true;
//                store_flg = false;
//
//                String[] array = (String[]) region_list.toArray(new String[0]);
//                String str_dept = Arrays.toString(array);
//                str_dept = str_dept.replace("[", "");
//                str_dept = str_dept.replace("]", "");
//                str_dept = str_dept.replace(", ", ",");
//                an_region_str = str_dept;
//                level = 3;
//                Log.e("Build up region in if:", "" + level + an_region_str);
//                requestLocationHierarchy(level, an_region_str);
//            }
//            else
//            {
//                region_list.add(txtClickedVal.trim());
//                String[] array = (String[]) region_list.toArray(new String[0]);
//                String str_dept = Arrays.toString(array);
//                str_dept = str_dept.replace("[", "");
//                str_dept = str_dept.replace("]", "");
//                str_dept = str_dept.replace(", ", ",");
//                an_region_str = str_dept;
//                level = 3;
//                Log.e("Build up region in else:", "" + level + an_region_str);
//                requestLocationHierarchy(level, an_region_str);
//            }
//        }
        if (level == 0) {
            if (!store_flg) {
                Log.e("come ", "----------");
                store_List.add(txtClickedVal.substring(0,4).trim());
                region_flg = false;
                store_flg = true;
                String[] array = (String[]) store_List.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                an_store_str = str_brndcls;
                Log.e("Build up store in if:", "" + an_store_str+store_List.size());

            } else
            {
                store_List.add(txtClickedVal.substring(0,4).trim());
                String[] array = (String[]) store_List.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                an_store_str = str_brndcls;
                Log.e("Build up store in else:", "" + an_store_str);

            }
        }
    }

    private void requestLocationHierarchy(int level ,String txtClickedVal) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        String geoLevel2Code = sharedPreferences.getString("concept","");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String loc_search_url = " ";
//        if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("pvaAnalysis"))
//        {
//            //with geoLevel2Code param
            loc_search_url = ConstsCore.web_url + "/v1/display/storehierarchyEZNew/" + userId + "?level=" + location_level + "&region=" + txtClickedVal.replaceAll("&", "%26").replace(" ", "%20")+"&geoLevel2Code="+geoLevel2Code;

//        }
//        else
//        {
//            //without geoLevel2Code param
//            loc_search_url = ConstsCore.web_url + "/v1/display/storehierarchyEZ/" + userId + "?level=" + location_level + "&region=" + txtClickedVal.replaceAll("&", "%26").replace(" ", "%20");
//
//        }

        Log.e("search url:", "" + loc_search_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, loc_search_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                                salesAnalysisFilter.rel_an_process_filter.setVisibility(View.GONE);

                            } else {
                                for (int i = mGroupPosition + 1 ; i < mListDataChild.size(); i++) {
                                    Log.e("i :",""+i);
                                    if (mGroupPosition +1 == i) {
                                        for (int j = i; j < mListDataChild.size(); j++) {
                                            mListDataChild.remove(i);
                                        }
                                    }
                                }//end of first for

                                for (int i = 0; i < response.length(); i++) {
                                    List<String> drillDownList = new ArrayList<String>();
                                    JSONObject obj = response.getJSONObject(i);

                                        String store = obj.getString("descEz");
                                        drillDownList.add(store);
                                        Log.e("drilldown list:", "" + drillDownList.size());

                                    Set<String> setValue = new HashSet<>();
                                    setValue.addAll(drillDownList);
                                    drillDownList.clear();
                                    drillDownList.addAll(setValue);
                                    Collections.sort(drillDownList);
                                    //expand group
                                     notifyDataSetChanged();
                                    mListDataChild.put(mListDataGroup.get(1), drillDownList);
                                    salesAnalysisFilter.explv_an_locatn.expandGroup(1);
                                    salesAnalysisFilter.rel_an_process_filter.setVisibility(View .GONE);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            salesAnalysisFilter.rel_an_process_filter.setVisibility(View.GONE);
                            Toast.makeText(mContext, "data failed..." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        salesAnalysisFilter.rel_an_process_filter.setVisibility(View.GONE);
                        Toast.makeText(mContext, "server not found...", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void filterData(String s) {
        String charText = s.toLowerCase(Locale.getDefault());
        mListDataChild.clear();
        if (charText.length() == 0) {
            mListDataChild.putAll(dublicate_listDataChild);
            //Collapse Group
            explv_an_prod.collapseGroup(0);
            explv_an_prod.collapseGroup(1);
            explv_an_prod.collapseGroup(2);
            explv_an_prod.collapseGroup(3);
            explv_an_prod.collapseGroup(4);
            salesAnalysisFilter.explv_an_locatn.collapseGroup(0);

        } else {

            for (int j = 0; j < 1; j++) {
                List<String> arrayList = new ArrayList<String>();

                for (int k = 0; k < dublicate_listDataChild.get(mListDataGroup.get(j)).size(); k++)
                {
                    if (dublicate_listDataChild.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayList.add(dublicate_listDataChild.get(mListDataGroup.get(j)).get(k));
                        Log.e("array list size  :", "" + arrayList.size());
                    }
                }
                mListDataChild.put(mListDataGroup.get(j), arrayList);
            }
            salesAnalysisFilter.explv_an_locatn.expandGroup(0);
            notifyDataSetChanged();
        }




    }

    public final class GroupViewHolder {
        TextView mGroupText;
        ImageView mImage;
    }

    public final class ChildViewHolder {
        TextView mChildText;
        CheckBox mCheckBox;
    }


}
