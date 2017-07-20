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


public class EzoneFilterAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    private String TAG = "SalesFilterExpandableList";

    int offsetvalue = 0, limit = 100, count = 0;
    ArrayList categorylist, articleOptionList, planClassList, brandNameList, brandplanclassList;
    ExpandableListView expandableListView;
    List<String> salesList;
    String txtClickedVal;

    private HashMap<String, List<String>> mListDataChild;
    HashMap<String, List<String>> dublicate_listDataChild1, dublicate_listDataChild2;
    private List<String> mListDataGroup;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private SalesFilterActivity salesFilterActivity;
    private String groupText;
    private String childText;

    List myList1 = new ArrayList();
    List myList2 = new ArrayList();
    List myList3 = new ArrayList();
    List myList4 = new ArrayList();
    List myList5 = new ArrayList();
    public static String text1 = "", text2 = "", text3 = "", text4 = "", text5 = "";

    EzoneFilterAdapter listAdapter;
    Boolean flag = false;
    int mGroupPosition = 0;
    int mChildPosition = 0;
    public static Boolean l1 = false, l2 = false, l3 = false, l4 = false, l5 = false;


    public EzoneFilterAdapter(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, EzoneFilterAdapter listAdapter) {

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
        this.dublicate_listDataChild1 = new HashMap<String, List<String>>();
        this.dublicate_listDataChild1.putAll(mListDataChild);
        this.dublicate_listDataChild2 = new HashMap<String, List<String>>();
        this.dublicate_listDataChild2.putAll(mListDataChild);

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
        }
        else
        {
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
                                               if (!cb.isChecked()) {
                                                   salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(true);
                                               } else if (cb.isChecked()) {
                                                   salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(false);
                                               }
                                           }
                                       }
        );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void filterData(String s) {
        String charText = s.toLowerCase(Locale.getDefault());
        mListDataChild.clear();
        if (charText.length() == 0) {
            mListDataChild.putAll(dublicate_listDataChild1);
            mListDataChild.putAll(dublicate_listDataChild2);
            //Collapse Group
            EzoneSalesFilter.explv_ez_locatn.collapseGroup(0);
            EzoneSalesFilter.explv_ez_locatn.collapseGroup(1);
            EzoneSalesFilter.explv_ez_prod.collapseGroup(0);
            EzoneSalesFilter.explv_ez_prod.collapseGroup(1);
            EzoneSalesFilter.explv_ez_prod.collapseGroup(2);
            EzoneSalesFilter.explv_ez_prod.collapseGroup(3);
            EzoneSalesFilter.explv_ez_prod.collapseGroup(4);
        }
        else
        {
            if(EzoneSalesFilter.lin_ez_locatn.getVisibility()==View.VISIBLE) {
                for (int j = 0; j < 2; j++) {
                    List<String> arrayList = new ArrayList<String>();

                    for (int k = 0; k < dublicate_listDataChild1.get(mListDataGroup.get(j)).size(); k++)

                    {
                        if (dublicate_listDataChild1.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                            arrayList.add(dublicate_listDataChild1.get(mListDataGroup.get(j)).get(k));
                            Log.e("array list size  :",""+arrayList.size());
                        }
                    }

                    mListDataChild.put(mListDataGroup.get(j), arrayList);
                }
                EzoneSalesFilter.lin_ez_locatn.setVisibility(View.VISIBLE);
                EzoneSalesFilter.explv_ez_locatn.expandGroup(0);
                EzoneSalesFilter.explv_ez_locatn.expandGroup(1);

            }
            if(EzoneSalesFilter.lin_ez_prod.getVisibility()==View.VISIBLE)
            {
                for (int j = 0; j < 5; j++) {
                    List<String> arrayList1 = new ArrayList<String>();

                    for (int k = 0; k < dublicate_listDataChild2.get(mListDataGroup.get(j)).size(); k++)

                    {
                        if (dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                            arrayList1.add(dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k));
                            Log.e("array list size 1 :",""+arrayList1.size());

                        }
                    }

                    mListDataChild.put(mListDataGroup.get(j), arrayList1);
                }
                EzoneSalesFilter.lin_ez_prod.setVisibility(View.VISIBLE);
                EzoneSalesFilter.explv_ez_prod.expandGroup(0);
                EzoneSalesFilter.explv_ez_prod.expandGroup(1);
                EzoneSalesFilter.explv_ez_prod.expandGroup(2);
                EzoneSalesFilter.explv_ez_prod.expandGroup(3);
                EzoneSalesFilter.explv_ez_prod.expandGroup(4);
            }

            notifyDataSetChanged();
        }
    }

    public final class GroupViewHolder
    {
        TextView mGroupText;
        ImageView mImage;
    }
    
    public final class ChildViewHolder
    {
        TextView mChildText;
        CheckBox mCheckBox;
    }
}


