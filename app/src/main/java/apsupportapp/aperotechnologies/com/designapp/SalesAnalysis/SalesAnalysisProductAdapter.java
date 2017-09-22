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

import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter.an_str_checkFrom;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter.explv_an_prod;
import static apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter.rel_an_process_filter;


public class SalesAnalysisProductAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    SalesAnalysisFilter salesAnalysisFilter;

    int offsetvalue = 0, limit = 100, count = 0;
    ExpandableListView expandableListView;
    List<String> salesList;
    String txtClickedVal;

    private HashMap<String, List<String>> mListDataChild;
    HashMap<String, List<String>> dublicate_listDataChild2;
    private List<String> mListDataGroup;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private String groupText;
    private String childText;

    List deptList = new ArrayList();
    List categryList = new ArrayList();
    List classList = new ArrayList();
    List brandList = new ArrayList();
    List brandclsList = new ArrayList();
    public static String an_dept_text = "", an_categry_text = "", an_class_text = "", an_brand_text = "", an_brandcls_text = "";
    public static int an_level;
    SalesAnalysisProductAdapter listAdapter;
    int mGroupPosition = 0;
    int mChildPosition = 0;
    public static boolean an_dept_flg = false, an_cat_flg = false, an_class_flg = false, an_brand_flg = false, an_brandcls_flg = false;


    public SalesAnalysisProductAdapter(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, SalesAnalysisProductAdapter listAdapter) {

        mContext = context;
        this.mListDataGroup = listDataGroup;
        this.mListDataChild = listDataChild;
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        salesList = new ArrayList<>();
        this.dublicate_listDataChild2 = new HashMap<String, List<String>>();
        this.dublicate_listDataChild2.putAll(mListDataChild);
        salesAnalysisFilter = new SalesAnalysisFilter();
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
                                               an_level = groupPosition + 2;
                                               if (!cb.isChecked()) {

                                                   if (an_str_checkFrom.equals("anoneSales")  || an_str_checkFrom.equals("pvaAnalysis")) {
                                                       salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                       cb.setChecked(true);
                                                       rel_an_process_filter.setVisibility(View.VISIBLE);
                                                       if (groupPosition == 3)
                                                       {
                                                          rel_an_process_filter.setVisibility(View.GONE);
                                                       }

                                                   } else {
                                                       salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                       cb.setChecked(true);
                                                       rel_an_process_filter.setVisibility(View.VISIBLE);
                                                       if (groupPosition == 4)
                                                       {
                                                          rel_an_process_filter.setVisibility(View.GONE);
                                                       }
                                                   }
                                                   Log.e("salesListchecked :", "" + salesList);
                                                   BuildUP(an_level);
                                               } else if (cb.isChecked())
                                               {
                                                   salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(false);
                                                   Log.e("salesListunchecked :", "" + salesList);
                                                   removeBuildUP(an_level);
                                               }
                                           }
                                       }
        );
        return convertView;
    }

    private void removeBuildUP(int prod_level) {
        int filterLevel;
        filterLevel = prod_level;

        if (an_str_checkFrom.equals("ezoneSales") || an_str_checkFrom.equals("pvaAnalysis"))
        {
            Log.e("removeBuildUP: ","Sales" );
            removeDataforSales(filterLevel);
        }
        else
        {
            Log.e("removeBuildUP: ","Inventory" );
            removeDataforInventory(filterLevel);
        }

    }

    private void removeDataforSales(int filterLevel) {
        if (filterLevel == 2) {
            deptList.remove(txtClickedVal.trim());
            String[] array = (String[]) deptList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_dept_text = str1;

            if (deptList.size() == 0) {
                salesList.clear();
                mListDataChild.putAll(dublicate_listDataChild2);
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_an_prod.collapseGroup(k);
                }
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_an_prod.expandGroup(k);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, an_dept_text);
            }
        }
        if (filterLevel == 3) {
            categryList.remove(categryList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) categryList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_categry_text = str1;
            if (categryList.size() == 0) {
                if (deptList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 2;
                    requestProductHierarchyAPI(filterLevel, an_dept_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, an_categry_text);
            }

        }
        if (filterLevel == 4) {
            classList.remove(classList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) classList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_class_text = str1;
            if (classList.size() == 0) {
                if (categryList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 3;
                    requestProductHierarchyAPI(filterLevel, an_categry_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, an_class_text);
            }
        }
        if (filterLevel == 5) {
            brandList.remove(brandList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) brandList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_brand_text = str1;
        }
    }

    private void removeDataforInventory(int filterLevel)
    {
        if (filterLevel == 2) {
            deptList.remove(txtClickedVal.trim());
            String[] array = (String[]) deptList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_dept_text = str1;

            if (deptList.size() == 0) {
                salesList.clear();
                mListDataChild.putAll(dublicate_listDataChild2);
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_an_prod.collapseGroup(k);
                }
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_an_prod.expandGroup(k);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, an_dept_text);
            }
        }
        if (filterLevel == 3) {
            categryList.remove(categryList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) categryList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_categry_text = str1;
            if (categryList.size() == 0) {
                if (deptList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 2;
                    requestProductHierarchyAPI(filterLevel, an_dept_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, an_categry_text);
            }

        }
        if (filterLevel == 4) {
            classList.remove(classList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) classList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_class_text = str1;
            if (classList.size() == 0) {
                if (categryList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 3;
                    requestProductHierarchyAPI(filterLevel, an_categry_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, an_class_text);
            }
        }
        if (filterLevel == 5) {
            brandList.remove(brandList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) brandList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_brand_text = str1;
            if (brandList.size() == 0) {
                if (classList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_an_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 4;
                    requestProductHierarchyAPI(filterLevel, an_class_text);
                }

            } else {
                requestProductHierarchyAPI(filterLevel, an_brand_text);
            }
        }
        if (filterLevel == 6) {
            brandclsList.remove(brandclsList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) brandclsList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            an_brandcls_text = str1;
        }
    }


    private void BuildUP(int prod_level) {
        int filterLevel;
        filterLevel = prod_level;
        if (an_str_checkFrom.equals("ezoneSales") || an_str_checkFrom.equals("pvaAnalysis"))
        {
            Log.e("BuildUP: ","Sales" );
            buildUpdataSales(filterLevel);
        } else
        {
            Log.e("BuildUP: ","Inventory" );
            buildUpdataInventory(filterLevel);
        }
    }


    private void buildUpdataSales(int filterLevel) {
        if (filterLevel == 2) {
            if (!an_dept_flg) {
                deptList.add(txtClickedVal.trim());
                an_dept_flg = true;
                an_cat_flg = false;
                an_class_flg = false;
                an_brand_flg = false;
                String[] array = (String[]) deptList.toArray(new String[0]);
                String str_dept = Arrays.toString(array);
                str_dept = str_dept.replace("[", "");
                str_dept = str_dept.replace("]", "");
                str_dept = str_dept.replace(", ", ",");
                an_dept_text = str_dept;
                requestProductHierarchyAPI(filterLevel, an_dept_text);
            } else {
                deptList.add(txtClickedVal.trim());
                String[] array = (String[]) deptList.toArray(new String[0]);
                String str_dept = Arrays.toString(array);
                str_dept = str_dept.replace("[", "");
                str_dept = str_dept.replace("]", "");
                str_dept = str_dept.replace(", ", ",");
                an_dept_text = str_dept;
                requestProductHierarchyAPI(filterLevel, an_dept_text);
            }
        }
        if (filterLevel == 3) {
            if (!an_cat_flg) {
                categryList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = true;
                an_class_flg = false;
                an_brand_flg = false;
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                an_categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, an_categry_text);
            } else {
                categryList.add(txtClickedVal.trim());
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                an_categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, an_categry_text);
            }
        }
        if (filterLevel == 4) {
            if (!an_class_flg) {
                classList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = false;
                an_class_flg = true;
                an_brand_flg = false;
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                an_class_text = str_class;
                requestProductHierarchyAPI(filterLevel, an_class_text);
            } else {
                classList.add(txtClickedVal.trim());
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                an_class_text = str_class;
                requestProductHierarchyAPI(filterLevel, an_class_text);
            }
        }
        if (filterLevel == 5) {
            if (!an_brand_flg) {
                brandList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = false;
                an_class_flg = false;
                an_brand_flg = true;
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                an_brand_text = str_brand;
            } else {
                brandList.add(txtClickedVal.trim());
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                an_brand_text = str_brand;
            }
        }
    }

    private void buildUpdataInventory(int filterLevel) {
        if (filterLevel == 2) {
            if (!an_dept_flg) {
                deptList.add(txtClickedVal.trim());
                an_dept_flg = true;
                an_cat_flg = false;
                an_class_flg = false;
                an_brand_flg = false;
                an_brandcls_flg = false;
                String[] array = (String[]) deptList.toArray(new String[0]);
                String str_dept = Arrays.toString(array);
                str_dept = str_dept.replace("[", "");
                str_dept = str_dept.replace("]", "");
                str_dept = str_dept.replace(", ", ",");
                an_dept_text = str_dept;
                requestProductHierarchyAPI(filterLevel, an_dept_text);
            } else {
                deptList.add(txtClickedVal.trim());
                String[] array = (String[]) deptList.toArray(new String[0]);
                String str_dept = Arrays.toString(array);
                str_dept = str_dept.replace("[", "");
                str_dept = str_dept.replace("]", "");
                str_dept = str_dept.replace(", ", ",");
                an_dept_text = str_dept;
                requestProductHierarchyAPI(filterLevel, an_dept_text);
            }
        }
        if (filterLevel == 3) {
            if (!an_cat_flg) {
                categryList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = true;
                an_class_flg = false;
                an_brand_flg = false;
                an_brandcls_flg = false;
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                an_categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, an_categry_text);
            } else {
                categryList.add(txtClickedVal.trim());
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                an_categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, an_categry_text);
            }
        }
        if (filterLevel == 4) {
            if (!an_class_flg) {
                classList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = false;
                an_class_flg = true;
                an_brand_flg = false;
                an_brandcls_flg = false;
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                an_class_text = str_class;
                requestProductHierarchyAPI(filterLevel, an_class_text);
            } else {
                classList.add(txtClickedVal.trim());
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                an_class_text = str_class;
                requestProductHierarchyAPI(filterLevel, an_class_text);
            }
        }
        if (filterLevel == 5) {
            if (!an_brand_flg) {
                brandList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = false;
                an_class_flg = false;
                an_brand_flg = true;
                an_brandcls_flg = false;
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                an_brand_text = str_brand;
                requestProductHierarchyAPI(filterLevel,an_brand_text);
            } else {
                brandList.add(txtClickedVal.trim());
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                an_brand_text = str_brand;
                requestProductHierarchyAPI(filterLevel, an_brand_text);
            }
        }
        if (filterLevel == 6) {
            if (!an_brandcls_flg) {
                brandclsList.add(txtClickedVal.trim());
                an_dept_flg = false;
                an_cat_flg = false;
                an_class_flg = false;
                an_brand_flg = false;
                an_brandcls_flg = true;
                String[] array = (String[]) brandclsList.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                an_brandcls_text = str_brndcls;
            } else {
                brandclsList.add(txtClickedVal.trim());
                String[] array = (String[]) brandclsList.toArray(new String[0]);
                String str_brandcls = Arrays.toString(array);
                str_brandcls = str_brandcls.replace("[", "");
                str_brandcls = str_brandcls.replace("]", "");
                str_brandcls = str_brandcls.replace(", ", ",");
                an_brandcls_text = str_brandcls;
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
            mListDataChild.putAll(dublicate_listDataChild2);
            //Collapse Group

            if (an_str_checkFrom.equals("ezoneSales") || an_str_checkFrom.equals("pvaAnalysis")) {
                explv_an_prod.collapseGroup(0);
                explv_an_prod.collapseGroup(1);
                explv_an_prod.collapseGroup(2);
                explv_an_prod.collapseGroup(3);
            } else {
                explv_an_prod.collapseGroup(0);
                explv_an_prod.collapseGroup(1);
                explv_an_prod.collapseGroup(2);
                explv_an_prod.collapseGroup(3);
                explv_an_prod.collapseGroup(4);
            }

        } else {

            if (an_str_checkFrom.equals("ezoneSales") || an_str_checkFrom.equals("pvaAnalysis")) {

                for (int j = 0; j < 4; j++) {
                    List<String> arrayList1 = new ArrayList<String>();

                    for (int k = 0; k < dublicate_listDataChild2.get(mListDataGroup.get(j)).size(); k++) {
                        if (dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                            arrayList1.add(dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k));
                            Log.e("array list size 1 :", "" + arrayList1.size());

                        }
                    }
                    mListDataChild.put(mListDataGroup.get(j), arrayList1);
                }
                explv_an_prod.expandGroup(0);
                explv_an_prod.expandGroup(1);
                explv_an_prod.expandGroup(2);
                explv_an_prod.expandGroup(3);
                notifyDataSetChanged();
            } else {

                for (int j = 0; j < 5; j++) {
                    List<String> arrayList1 = new ArrayList<String>();

                    for (int k = 0; k < dublicate_listDataChild2.get(mListDataGroup.get(j)).size(); k++) {
                        if (dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                            arrayList1.add(dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k));
                            Log.e("array list size 1 :", "" + arrayList1.size());

                        }
                    }
                    mListDataChild.put(mListDataGroup.get(j), arrayList1);
                }
                explv_an_prod.expandGroup(0);
                explv_an_prod.expandGroup(1);
                explv_an_prod.expandGroup(2);
                explv_an_prod.expandGroup(3);
                explv_an_prod.expandGroup(4);
                notifyDataSetChanged();
            }
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

    //---------------------------------API Declaration------------------------------//
    private void requestProductHierarchyAPI(final int prod_level, String str) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String userId = sharedPreferences.getString("userId", "");
        String geoLevel2Code = sharedPreferences.getString("concept","");
        String lobId = sharedPreferences.getString("lobid","");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        String search_url = " ";
        if (an_str_checkFrom.equals("ezoneSales") || an_str_checkFrom.equals("pvaAnalysis")) {
            if (prod_level == 2) {
                Log.e("str in global search :", "" + str);
                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&dept=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 3) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&category=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 4) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&class=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;
            }
        } else {
            if (prod_level == 2)
            {
                Log.e("str in global search :", "" + str);
                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&dept=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 3) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&category=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 4) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&class=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;
            } else if (prod_level == 5) {
                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&brand=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;
            }
        }
        Log.e("search url:", "" + search_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, search_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                                rel_an_process_filter.setVisibility(View.GONE);

                            } else {
                                for (int i = prod_level - 1; i < mListDataChild.size(); i++) {
                                    if (prod_level - 1 == i) {
                                        for (int j = i; j < mListDataChild.size(); j++) {
                                            mListDataChild.remove(i);
                                        }
                                    }
                                }

                                for (int i = prod_level - 1; i < response.length(); i++) {
                                    List<String> drillDownList = new ArrayList<String>();
                                    JSONArray jsonArray = response.getJSONArray(i);
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        if (an_str_checkFrom.equals("ezoneSales") || an_str_checkFrom.equals("pvaAnalysis")) {
                                            if (i == 1) {
                                                String listValueCategory = jsonArray.getJSONObject(j).getString("planCategory");
                                                drillDownList.add(listValueCategory);
                                            } else if (i == 2) {
                                                String listValuePlanClass = jsonArray.getJSONObject(j).getString("planClass");
                                                drillDownList.add(listValuePlanClass);
                                            } else if (i == 3) {
                                                String listValueBrand = jsonArray.getJSONObject(j).getString("brandName");
                                                drillDownList.add(listValueBrand);
                                            }
                                        }
                                        else
                                        {
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

                                    }
                                    //this is for remove dublicate values in arraylist.
                                    Set<String> setValue = new HashSet<>();
                                    setValue.addAll(drillDownList);
                                    drillDownList.clear();
                                    drillDownList.addAll(setValue);
                                    Collections.sort(drillDownList);
                                    //expand group
                                    try {
                                        mListDataChild.put(mListDataGroup.get(i), drillDownList);
                                    } catch (IndexOutOfBoundsException e) {
                                        Log.e("onResponse: ", "" + e.getMessage());
                                    }
                                    for (int k = prod_level - 1; k < mListDataGroup.size(); k++) {
                                        explv_an_prod.expandGroup(k);
                                    }
                                }
                                notifyDataSetChanged();
                                rel_an_process_filter.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            rel_an_process_filter.setVisibility(View.GONE);
                            Toast.makeText(mContext, "data failed..." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        rel_an_process_filter.setVisibility(View.GONE);
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

}

