package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.crashlytics.android.answers.LoginEvent;

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
import apsupportapp.aperotechnologies.com.designapp.model.ListBrand;
import apsupportapp.aperotechnologies.com.designapp.model.ListBrandClass;
import apsupportapp.aperotechnologies.com.designapp.model.ListCategory;
import apsupportapp.aperotechnologies.com.designapp.model.ListPlanClass;

import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity.level_filter;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity.subBrandPlanClass;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity.subBrandnm;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity.subCategory;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity.subPlanClass;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity.subdept;
import static com.crashlytics.android.Crashlytics.TAG;
import static com.crashlytics.android.Crashlytics.log;


public class SalesFilterExpandableList extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    private String TAG = "SalesFilterExpandableList";

    int offsetvalue = 0, limit = 100, count = 0;
    ArrayList categorylist, articleOptionList, planClassList, brandNameList, brandplanclassList;
    List tempcategorylist, tempplanclass, tempbrandname, tempbrandplanclass;
    ExpandableListView expandableListView;
    List<String> salesList;
    String txtClickedVal;
    List<ListCategory> categoryArray;
    List<ListPlanClass> planclassArray;
    List<ListBrand> brandArray;
    List<ListBrandClass> brandclassArray;

    private HashMap<String, List<String>> mListDataChild;
    HashMap<String, List<String>> dublicate_listDataChild;
    private List<String> mListDataGroup;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private SalesFilterActivity salesFilterActivity;
    private String groupText;
    private String childText;
    public static String planDepartmentName, planClassName, planCategoryName;
    //public static StringBuilder  text2, text3, text4, text5;
    List myList1 = new ArrayList();
    List myList2 = new ArrayList();
    List myList3 = new ArrayList();
    List myList4 = new ArrayList();
    List myList5 = new ArrayList();
    public static String text1 = "", text2 = "", text3 = "", text4 = "", text5 = "";

    SalesFilterExpandableList listAdapter;
    Boolean flag = false;
    int mGroupPosition = 0;
    int mChildPosition = 0;
    public static Boolean l1 = false, l2 = false, l3 = false, l4 = false, l5 = false;
    public static int level;


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
        categoryArray = new ArrayList();
        planclassArray = new ArrayList();
        brandArray = new ArrayList();
        brandclassArray = new ArrayList();
        flag = false;
        //text1 = new StringBuilder();
//        text2 = new StringBuilder();
//        text3 = new StringBuilder();
//        text4 = new StringBuilder();
//        text5 = new StringBuilder();

        salesFilterActivity = new SalesFilterActivity();
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
            Log.e(TAG, "getChild: catch" + e.getMessage());
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
                                               Log.e(TAG, "onClick: " + txtClickedVal);
                                               level = groupPosition + 2;
                                               if (cb.isChecked() == false) {

                                                   Log.e(TAG, "level is : " + level);
                                                   salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(true);
                                                   salesFilterActivity.processbar.setVisibility(View.VISIBLE);
                                                   if (groupPosition == 4) {
                                                       salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                       cb.setChecked(true);
                                                       salesFilterActivity.processbar.setVisibility(View.GONE);
                                                   }
                                                   BuildUP(level);

                                               } else if(cb.isChecked() == true)
                                               {
                                                   salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   Log.e(TAG, "unchecked " + salesList);
                                                   cb.setChecked(false);
                                                 //  salesFilterActivity.processbar.setVisibility(View.VISIBLE);
//                                                   if(groupPosition == 4)
//                                                   {
//                                                       salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
//                                                       Log.e(TAG, "unchecked " + salesList);
//                                                       cb.setChecked(false);
//                                                      // salesFilterActivity.processbar.setVisibility(View.GONE);
//
//                                                   }
                                                   removeBuildUP(level);

                                               }

                                           }
                                       }
        );
        return convertView;
    }

    private void removeBuildUP(int level) {

        if (level == 2) {
            Log.e(TAG, "text1" + text1);

            myList1.remove(txtClickedVal.trim());

            String[] array = (String[]) myList1.toArray(new String[0]);
            Log.e("array", " " + array);

            String str1 = Arrays.toString(array);
            //replace starting "[" and ending "]" and ","
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ",",");

            // str1 = str1.replaceAll("\\s+", "");
            Log.e("str1--", " " + str1);
            text1 = str1;

            if (myList1.size() == 0) {
                Log.e("Duplicate Array", "" + dublicate_listDataChild.size());
                salesList.clear();
                mListDataChild.putAll(dublicate_listDataChild);
                for (int k = level - 1; k < mListDataGroup.size(); k++) {
                    SalesFilterActivity.pfilter_list.collapseGroup(k);
                }
                for (int k = level - 1; k < mListDataGroup.size(); k++) {
                    SalesFilterActivity.pfilter_list.expandGroup(k);
                }
            } else {
                requestCategoryAPI(level, text1);
            }


        }
        if (level == 3) {

            myList2.remove(myList2.indexOf(txtClickedVal.trim()));

            String[] array = (String[]) myList2.toArray(new String[0]);
            Log.e("array", " " + array);

            String str1 = Arrays.toString(array);
            //replace starting "[" and ending "]" and ","
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ",",");
            // str1 = str1.replaceAll("\\s+","");
            Log.e("str1--", " " + str1);
            text2 = str1;
            if (myList2.size() == 0) {
                if (myList1.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild);
                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                        SalesFilterActivity.pfilter_list.collapseGroup(k);
                    }
                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                        SalesFilterActivity.pfilter_list.expandGroup(k);
                    }
                } else {
                    Log.e("text 1 in text 2 loop in else ","" + text1);
                    level = 2;
                    requestCategoryAPI(level, text1);
                }
            } else {
                requestCategoryAPI(level, text2);
            }

        }
        if (level == 4) {


            myList3.remove(myList3.indexOf(txtClickedVal.trim()));

            String[] array = (String[]) myList3.toArray(new String[0]);
            Log.e("myList3 size is ", " " + myList3.size());

            String str1 = Arrays.toString(array);
            //replace starting "[" and ending "]" and ","
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ",",");
            // str1 = str1.replaceAll("\\s+", "");
            Log.e("str1--", " " + str1);
            text3 = str1;
            if (myList3.size() == 0) {
                if (myList2.size() == 0) {

                    mListDataChild.putAll(dublicate_listDataChild);
                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                        SalesFilterActivity.pfilter_list.collapseGroup(k);
                    }
                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                        SalesFilterActivity.pfilter_list.expandGroup(k);
                    }

                } else {
                    Log.e("text 2 in text 3 loop in else ","" + text2);
                    level = 3;
                    requestCategoryAPI(level, text2);
                }
            } else {
                requestCategoryAPI(level, text3);
            }

        }
        if (level == 5)
        {
            myList4.remove(myList4.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) myList4.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ",",");
            //str1 = str1.replaceAll("\\s+", "");
            Log.e("str1--", " " + str1);
            text4 = str1;
            if (myList4.size() == 0) {
                if (myList3.size() == 0) {
                    Log.e("text 3 in text 4 loop","" + text3);
                    mListDataChild.putAll(dublicate_listDataChild);
                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                        SalesFilterActivity.pfilter_list.collapseGroup(k);
                    }
                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                        SalesFilterActivity.pfilter_list.expandGroup(k);
                    }

                } else {
                    Log.e("text 3 in text 4 loop in else ","" + text3);
                    level = 4;
                    requestCategoryAPI(level, text3);
                }

            } else {
                requestCategoryAPI(level, text4);
            }
        }
        if (level == 6) {

            myList5.remove(myList5.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) myList5.toArray(new String[0]);
            Log.e("array", " " + array);

            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ",",");
            // str1 = str1.replaceAll("\\s+", "");
            Log.e("str1--", " " + str1);
            text5 = str1;

        }
    }

    private void BuildUP(int level) {

        if (level == 2) {
            if (l1 == false) {

                myList1.add(txtClickedVal.trim());
                Log.e("myList", " " + myList1.toString());
                //text1.append(txtClickedVal);
                l1 = true;
                l2 = false;
                l3 = false;
                l4 = false;
                l5 = false;

                //text1.append(",");
                String[] array = (String[]) myList1.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_dept = Arrays.toString(array);
                //replace starting "[" and ending "]" and ","
                str_dept = str_dept.replace("[", "");
                str_dept = str_dept.replace("]", "");
                str_dept = str_dept.replace(", ",",");

                // str_dept = str_dept.replaceAll("\\s+", "");
                Log.e("str1--", " " + str_dept);
                text1 = str_dept;
                requestCategoryAPI(level, text1);

            } else {

                myList1.add(txtClickedVal.trim());
                Log.e("myList", " " + myList1.toString());
                String[] array = (String[]) myList1.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_dept = Arrays.toString(array);
                //replace starting "[" and ending "]" and ","
                str_dept = str_dept.replace("[", "");
                str_dept = str_dept.replace("]", "");
                str_dept = str_dept.replace(", ",",");

                //  str_dept = str_dept.replaceAll("\\s+", "");
                Log.e("str1==", " " + str_dept);
                text1 = str_dept;
                requestCategoryAPI(level, text1);

            }
        }
        if (level == 3) {
            if (l2 == false) {
                myList2.add(txtClickedVal.trim());
                //text2.append(txtClickedVal);
                l1 = false;
                l2 = true;
                l3 = false;
                l4 = false;
                l5 = false;
                String[] array = (String[]) myList2.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ",",");

                // str_cate = str_cate.replaceAll("\\s+","");
                Log.e("str1--", " " + str_cate);
                text2 = str_cate;
                requestCategoryAPI(level, text2);

            } else {

                myList2.add(txtClickedVal.trim());
                Log.e("myList", " " + myList2.toString());
                String[] array = (String[]) myList2.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ",",");
                //  str_cate = str_cate.replaceAll("\\s+","");
                Log.e("str1==", " " + str_cate);
                text2 = str_cate;
                requestCategoryAPI(level, text2);
            }
        }
        if (level == 4) {
            if (l3 == false) {
                myList3.add(txtClickedVal.trim());

                l1 = false;
                l2 = false;
                l3 = true;
                l4 = false;
                l5 = false;
                String[] array = (String[]) myList3.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_class = Arrays.toString(array);
                //replace starting "[" and ending "]" and ","
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ",",");

                // str_class = str_class.replaceAll("\\s+","");
                Log.e("str1--", " " + str_class);
                text3 = str_class;
                requestCategoryAPI(level, text3);

            } else {

                myList3.add(txtClickedVal.trim());
                String[] array = (String[]) myList3.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ",",");
                //  str_class = str_class.replaceAll("\\s+","");
                Log.e("str1--", " " + str_class);
                text3 = str_class;
                requestCategoryAPI(level, text3);
            }
        }
        if (level == 5) {
            if (l4 == false) {

                myList4.add(txtClickedVal.trim());
                l1 = false;
                l2 = false;
                l3 = false;
                l4 = true;
                l5 = false;
                String[] array = (String[]) myList4.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
               // str_brand = str_brand.replace("^\\s+","");
                str_brand = str_brand.replace(", ",",");
                Log.e(TAG, "BuildUP: "+str_brand );
                Log.e("str1--", " " + str_brand);
                text4 = str_brand;
                requestCategoryAPI(level, text4);

            } else {

                myList4.add(txtClickedVal.trim());
                String[] array = (String[]) myList4.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_brand = Arrays.toString(array);
                //replace starting "[" and ending "]" and ","
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                //str_brand = str_brand.replace("^\\s+","");
                str_brand = str_brand.replace(", ",",");
                Log.e("str1--", " " + str_brand);
                text4 = str_brand;
                requestCategoryAPI(level, text4);

            }
        }
        if (level == 6) {
            if (l5 == false) {
                myList5.add(txtClickedVal.trim());
                l1 = false;
                l2 = false;
                l3 = false;
                l4 = false;
                l5 = true;
                String[] array = (String[]) myList5.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_brndcls = Arrays.toString(array);
                //replace starting "[" and ending "]" and ","
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ",",");

                // str_brndcls = str_brndcls.replaceAll("\\s+","");
                Log.e("str1--", " " + str_brndcls);
                text5 = str_brndcls;

            } else {

                myList5.add(txtClickedVal.trim());
                String[] array = (String[]) myList5.toArray(new String[0]);
                Log.e("array", " " + array);

                String str_brandcls = Arrays.toString(array);
                str_brandcls = str_brandcls.replace("[", "");
                str_brandcls = str_brandcls.replace("]", "");
                str_brandcls = str_brandcls.replace(", ",",");

                // str_brandcls = str_brandcls.replaceAll("\\s+","");
                Log.e("str1--", " " + str_brandcls);
                text5 = str_brandcls;

            }
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
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

                for (int k = 0; k < dublicate_listDataChild.get(mListDataGroup.get(j)).size(); k++)

                {
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


    public void requestCategoryAPI(int level1, final String dept) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String category_url = " ";
        if (level1 == 2) {


            category_url = ConstsCore.web_url + "/v1/display/globalsearch/" + userId + "?level=" + level1 + "&dept=" + dept.replaceAll("&", "%26").replace(" ","%20");

        } else if (level1 == 3) {

            category_url = ConstsCore.web_url + "/v1/display/globalsearch/" + userId + "?level=" + level1 + "&category=" + dept.replaceAll("&", "%26").replace(" ","%20");

        } else if (level1 == 4) {

            category_url = ConstsCore.web_url + "/v1/display/globalsearch/" + userId + "?level=" + level1 + "&class=" + dept.replaceAll("&", "%26").replace(" ","%20");

        } else if (level1 == 5) {

            category_url = ConstsCore.web_url + "/v1/display/globalsearch/" + userId + "?level=" + level1 + "&brand=" + dept.replaceAll("&", "%26").replace(" ","%20");

        }

        Log.e(TAG, "URL   " + category_url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, category_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Category Filter Response" + response.toString());
                        Log.i(TAG, "category list" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                                salesFilterActivity.processbar.setVisibility(View.GONE);

                            } else {
                                //Reusable_Functions.hDialog();

                                Log.e(TAG, "Before mListDataChild: size is " + mListDataChild.size());

                                for (int i = level - 1; i < mListDataChild.size(); i++) {
                                    if (level - 1 == i) {
                                        for (int j = i; j < mListDataChild.size(); j++) {
                                            mListDataChild.remove(i);

                                        }
                                    }
                                }
                                Log.e(TAG, "After mListDataChild: size is " + mListDataChild.size());

                                // level = clickgroupPosition+1  >>>>
                                Log.e(TAG, "onResponse:" + mGroupPosition);
                                for (int i = level - 1; i < response.length(); i++) {
                                    //  JSONObject productName1 = response.getJSONObject(i);
                                    List<String> drillDownList = new ArrayList<String>();

                                    JSONArray jsonArray = response.getJSONArray(i);
                                    Log.e(TAG, "JSONArray: " + i + "size is " + jsonArray.length());
                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        if (i == 1) {

                                            String listValueCategory = jsonArray.getJSONObject(j).getString("planCategory");
                                            drillDownList.add(listValueCategory);

                                        } else if (i == 2) {
                                            String listValuePlanClass = jsonArray.getJSONObject(j).getString("planClass");
                                            drillDownList.add(listValuePlanClass);

                                        } else if (i == 3) {
                                            String listValueBrand = jsonArray.getJSONObject(j).getString("brandName");
                                            drillDownList.add(listValueBrand);

                                        } else if (i == 4) {
                                            String listValueBrandClass = jsonArray.getJSONObject(j).getString("brandPlanClass");
                                            drillDownList.add(listValueBrandClass);

                                        }
                                    }

                                    //this is for remove dublicate values in arraylist.

                                    Set<String> setValue = new HashSet<>();
                                    setValue.addAll(drillDownList);
                                    drillDownList.clear();
                                    drillDownList.addAll(setValue);
                                    Collections.sort(drillDownList);
                                    //expand group
                                    mListDataChild.put(mListDataGroup.get(i), drillDownList);

                                    for (int k = level - 1; k < mListDataGroup.size(); k++) {
                                        SalesFilterActivity.pfilter_list.expandGroup(k);
                                    }


                                }

                                Log.e(TAG, "mListDataChild: length " + mListDataChild.size());
                                notifyDataSetChanged();

                                salesFilterActivity.processbar.setVisibility(View.GONE);


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            salesFilterActivity.processbar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "data failed...", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onResponse: Catch>>>> " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        salesFilterActivity.processbar.setVisibility(View.GONE);
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

    // Department List
    public void requestDeptAPI(int offsetvalue1, int limit1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  Log.i("Department Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String plandept = productName1.getString("planDept");
                                    subdept.add(plandept);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestDeptAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planDept = productName1.getString("planDept");
                                    subdept.add(planDept);

                                }
                                //Collections.sort(subdept);
                                //   listDataChild.put(listDataHeader.get(0), subdept);

                                // pfilter_list.expandGroup(0);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
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

    //Category List
    public void requestCategoryListAPI(int offsetvalue1, int limit1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.i("Category Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planCategory = productName1.getString("planCategory");

                                    subCategory.add(planCategory);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestCategoryListAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planCategory = productName1.getString("planCategory");
                                    subCategory.add(planCategory);

                                }
                                //Collections.sort(subdept);
                                //   listDataChild.put(listDataHeader.get(1), subCategory);
                                // pfilter_list.expandGroup(1);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
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

    //Plan Class List
    public void requestPlanClassAPI(int offsetvalue1, int limit1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.i("PlanClass Response", response.toString() + " Size" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planClass = productName1.getString("planClass");
                                    subPlanClass.add(planClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanClassAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planClass = productName1.getString("planClass");
                                    subPlanClass.add(planClass);

                                }
                                //Collections.sort(subdept);
                                //   listDataChild.put(listDataHeader.get(2), subPlanClass);
                                //  pfilter_list.expandGroup(2);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
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


    //Brand Name List
    public void requestBrandNameAPI(int offsetvalue1, int limit1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.i("Brand Name Response", response.toString() + "Size---" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandName = productName1.getString("brandName");
                                    subBrandnm.add(brandName);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandNameAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandName = productName1.getString("brandName");
                                    subBrandnm.add(brandName);
                                }
                                //Collections.sort(subdept);
                                //  listDataChild.put(listDataHeader.get(3), subBrandnm);
                                // pfilter_list.expandGroup(3);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
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


    //Brand Plan Class List
    public void requestBrandPlanClassAPI(int offsetvalue1, int limit1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  Log.i("Brand Class Response", response.toString() + "Size ---" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandClass = productName1.getString("brandPlanClass");
                                    subBrandPlanClass.add(brandClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandPlanClassAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandClass = productName1.getString("brandPlanClass");
                                    subBrandPlanClass.add(brandClass);
                                }
                                //Collections.sort(subdept);
                                //     listDataChild.put(listDataHeader.get(4), subBrandPlanClass);
                                //   pfilter_list.expandGroup(4);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
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


