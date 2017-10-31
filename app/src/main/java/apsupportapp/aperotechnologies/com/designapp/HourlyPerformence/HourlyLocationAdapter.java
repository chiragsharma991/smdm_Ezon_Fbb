package apsupportapp.aperotechnologies.com.designapp.HourlyPerformence;

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
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;

import static apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyStoreFilterActivity.explv_hr_locatn;
import static apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductFilterActivity.explv_key_locatn;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter.explv_an_prod;


/**
 * Created by pamrutkar on 08/06/17.
 */
public class HourlyLocationAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    HourlyStoreFilterActivity hourlyStoreFilterActivity;
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
    List store_List;
    HourlyLocationAdapter listAdapter;
    int mGroupPosition = 0;
    int mChildPosition = 0;
    boolean region_flg = false, store_flg = false;
    public static String hr_store_str = "";


    public HourlyLocationAdapter(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, HourlyLocationAdapter listAdapter) {

        mContext = context;
        this.mListDataGroup = listDataGroup;
        this.mListDataChild = listDataChild;
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        salesList = new ArrayList<>();
        this.dublicate_listDataChild = new HashMap<String, List<String>>();
        this.dublicate_listDataChild.putAll(mListDataChild);
        hourlyStoreFilterActivity = new HourlyStoreFilterActivity();
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

    /*private void removeBuildUP(int level) {

        if (level == 0)
        {
            Log.e("remove in store---","");
            store_List.remove(txtClickedVal.trim());
            String[] array = (String[]) store_List.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            hr_store_str = str1;
            Log.e("remove build up store:", "" + store_List.size());
        }

    }

    private void BuildUP(int level)
    {
        if (level == 0) {
            if (!store_flg) {
                Log.e("come ", "----------");
                store_List.add(txtClickedVal.trim());
                region_flg = false;
                store_flg = true;
                String[] array = (String[]) store_List.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                hr_store_str = str_brndcls;
                Log.e("Build up store in if:", "" + hr_store_str);

            } else
            {
                store_List.add(txtClickedVal.trim());
                String[] array = (String[]) store_List.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                hr_store_str = str_brndcls;
                Log.e("Build up store in else:", "" + hr_store_str);

            }
        }
    }*/

    private void removeBuildUP(int level) {


        if (level == 0)
        {
            store_List.remove(txtClickedVal.substring(0,4).trim());
            String[] array = (String[]) store_List.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            hr_store_str = str1;
        }

    }

    private void BuildUP(int level) {

        if (level == 0) {
            if (!store_flg) {
                store_List.add(txtClickedVal.substring(0,4).trim());
                region_flg = false;
                store_flg = true;
                String[] array = (String[]) store_List.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                hr_store_str = str_brndcls;

            } else
            {
                store_List.add(txtClickedVal.substring(0,4).trim());
                String[] array = (String[]) store_List.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                hr_store_str = str_brndcls;

            }
        }
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

            if(explv_hr_locatn != null)
            {
                explv_hr_locatn.collapseGroup(0);
            }

            if(explv_key_locatn != null)
            {
                explv_key_locatn.collapseGroup(0);
            }


//            explv_hr_locatn.collapseGroup(1);
//            explv_hr_locatn.collapseGroup(2);
//            explv_hr_locatn.collapseGroup(3);
//            explv_hr_locatn.collapseGroup(4);
//            explv_hr_locatn.collapseGroup(0);

        } else {

            for (int j = 0; j < 1; j++) {
                List<String> arrayList = new ArrayList<String>();

                for (int k = 0; k < dublicate_listDataChild.get(mListDataGroup.get(j)).size(); k++)
                {
                    if (dublicate_listDataChild.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayList.add(dublicate_listDataChild.get(mListDataGroup.get(j)).get(k));
                    }
                }
                mListDataChild.put(mListDataGroup.get(j), arrayList);
            }
            if(explv_hr_locatn != null)
            {
                explv_hr_locatn.expandGroup(0);
            }

            if(explv_key_locatn != null)
            {
                explv_key_locatn.expandGroup(0);
            }
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
