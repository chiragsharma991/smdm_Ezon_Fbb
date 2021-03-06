package apsupportapp.aperotechnologies.com.designapp.Ezone;


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

import static apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesFilter.explv_ez_locatn;
import static apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesFilter.explv_ez_prod;
import static apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesFilter.rel_ez_process_filter;
import static apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesFilter.str_checkFrom;


public class EzoneFilterProductAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    EzoneSalesFilter ezoneSalesFilter;

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

    List departmentList = new ArrayList();
    List categryList = new ArrayList();
    List classList = new ArrayList();
    List brandList = new ArrayList();
    List brandclsList = new ArrayList();
    public static String department_text = "", categry_text = "", class_text = "", brand_text = "", brandcls_text = "";
    public static int ez_level;
    EzoneFilterProductAdapter listAdapter;
    int mGroupPosition = 0;
    int mChildPosition = 0;
    public static boolean department_flg = false, cat_flg = false, class_flg = false, brand_flg = false, brandcls_flg = false;


    public EzoneFilterProductAdapter(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild, ExpandableListView expandableListView, EzoneFilterProductAdapter listAdapter) {

        mContext = context;
        this.mListDataGroup = listDataGroup;
        this.mListDataChild = listDataChild;
        this.expandableListView = expandableListView;
        this.listAdapter = listAdapter;
        salesList = new ArrayList<>();
        this.dublicate_listDataChild2 = new HashMap<String, List<String>>();
        this.dublicate_listDataChild2.putAll(mListDataChild);
        ezoneSalesFilter = new EzoneSalesFilter();
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
                                               ez_level = groupPosition + 2;
                                               if (!cb.isChecked()) {

                                                   if (str_checkFrom.equals("ezoneSales")  || str_checkFrom.equals("ezonepvaAnalysis")) {
                                                       salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                       cb.setChecked(true);
                                                       rel_ez_process_filter.setVisibility(View.VISIBLE);
                                                       if (groupPosition == 3)
                                                       {
                                                          rel_ez_process_filter.setVisibility(View.GONE);
                                                       }

                                                   } else {
                                                       salesList.add(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                       cb.setChecked(true);
                                                       rel_ez_process_filter.setVisibility(View.VISIBLE);
                                                       if (groupPosition == 4)
                                                       {
                                                          rel_ez_process_filter.setVisibility(View.GONE);
                                                       }
                                                   }
                                                   BuildUP(ez_level);
                                               } else if (cb.isChecked())
                                               {
                                                   salesList.remove(mListDataGroup.get(groupPosition) + "." + txtClickedVal);
                                                   cb.setChecked(false);
                                                   removeBuildUP(ez_level);
                                               }
                                           }
                                       }
        );
        return convertView;
    }

    private void removeBuildUP(int prod_level) {
        int filterLevel;
        filterLevel = prod_level;

        if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("ezonepvaAnalysis"))
        {
            removeDataforSales(filterLevel);
        }
        else
        {
            removeDataforInventory(filterLevel);
        }

    }

    private void removeDataforSales(int filterLevel) {
        if (filterLevel == 2) {
            departmentList.remove(txtClickedVal.trim());
            String[] array = (String[]) departmentList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            department_text = str1;

            if (departmentList.size() == 0) {
                salesList.clear();
                mListDataChild.putAll(dublicate_listDataChild2);
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_ez_prod.collapseGroup(k);
                }
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_ez_prod.expandGroup(k);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, department_text);
            }
        }
        if (filterLevel == 3) {
            categryList.remove(categryList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) categryList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            categry_text = str1;
            if (categryList.size() == 0) {
                if (departmentList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 2;
                    requestProductHierarchyAPI(filterLevel, department_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, categry_text);
            }

        }
        if (filterLevel == 4) {
            classList.remove(classList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) classList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            class_text = str1;
            if (classList.size() == 0) {
                if (categryList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 3;
                    requestProductHierarchyAPI(filterLevel, categry_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, class_text);
            }
        }
        if (filterLevel == 5) {
            brandList.remove(brandList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) brandList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            brand_text = str1;
        }
    }

    private void removeDataforInventory(int filterLevel)
    {
        if (filterLevel == 2) {
            departmentList.remove(txtClickedVal.trim());
            String[] array = (String[]) departmentList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            department_text = str1;

            if (departmentList.size() == 0) {
                salesList.clear();
                mListDataChild.putAll(dublicate_listDataChild2);
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_ez_prod.collapseGroup(k);
                }
                for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                    explv_ez_prod.expandGroup(k);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, department_text);
            }
        }
        if (filterLevel == 3) {
            categryList.remove(categryList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) categryList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            categry_text = str1;
            if (categryList.size() == 0) {
                if (departmentList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 2;
                    requestProductHierarchyAPI(filterLevel, department_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, categry_text);
            }

        }
        if (filterLevel == 4) {
            classList.remove(classList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) classList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            class_text = str1;
            if (classList.size() == 0) {
                if (categryList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 3;
                    requestProductHierarchyAPI(filterLevel, categry_text);
                }
            } else {
                requestProductHierarchyAPI(filterLevel, class_text);
            }
        }
        if (filterLevel == 5) {
            brandList.remove(brandList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) brandList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            brand_text = str1;
            if (brandList.size() == 0) {
                if (classList.size() == 0) {
                    mListDataChild.putAll(dublicate_listDataChild2);
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.collapseGroup(k);
                    }
                    for (int k = filterLevel - 1; k < mListDataGroup.size(); k++) {
                        explv_ez_prod.expandGroup(k);
                    }
                } else {
                    filterLevel = 4;
                    requestProductHierarchyAPI(filterLevel, class_text);
                }

            } else {
                requestProductHierarchyAPI(filterLevel, brand_text);
            }
        }
        if (filterLevel == 6) {
            brandclsList.remove(brandclsList.indexOf(txtClickedVal.trim()));
            String[] array = (String[]) brandclsList.toArray(new String[0]);
            String str1 = Arrays.toString(array);
            str1 = str1.replace("[", "");
            str1 = str1.replace("]", "");
            str1 = str1.replace(", ", ",");
            brandcls_text = str1;
        }
    }


    private void BuildUP(int prod_level) {
        int filterLevel;
        filterLevel = prod_level;
        if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("ezonepvaAnalysis"))
        {
            buildUpdataSales(filterLevel);
        } else
        {
            buildUpdataInventory(filterLevel);
        }
    }


    private void buildUpdataSales(int filterLevel) {
        if (filterLevel == 2) {
            if (!department_flg) {
                departmentList.add(txtClickedVal.trim());
                department_flg = true;
                cat_flg = false;
                class_flg = false;
                brand_flg = false;
                String[] array = (String[]) departmentList.toArray(new String[0]);
                String str_department = Arrays.toString(array);
                str_department = str_department.replace("[", "");
                str_department = str_department.replace("]", "");
                str_department = str_department.replace(", ", ",");
                department_text = str_department;
                requestProductHierarchyAPI(filterLevel, department_text);
            } else {
                departmentList.add(txtClickedVal.trim());
                String[] array = (String[]) departmentList.toArray(new String[0]);
                String str_department = Arrays.toString(array);
                str_department = str_department.replace("[", "");
                str_department = str_department.replace("]", "");
                str_department = str_department.replace(", ", ",");
                department_text = str_department;
                requestProductHierarchyAPI(filterLevel, department_text);
            }
        }
        if (filterLevel == 3) {
            if (!cat_flg) {
                categryList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = true;
                class_flg = false;
                brand_flg = false;
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, categry_text);
            } else {
                categryList.add(txtClickedVal.trim());
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, categry_text);
            }
        }
        if (filterLevel == 4) {
            if (!class_flg) {
                classList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = false;
                class_flg = true;
                brand_flg = false;
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                class_text = str_class;
                requestProductHierarchyAPI(filterLevel, class_text);
            } else {
                classList.add(txtClickedVal.trim());
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                class_text = str_class;
                requestProductHierarchyAPI(filterLevel, class_text);
            }
        }
        if (filterLevel == 5) {
            if (!brand_flg) {
                brandList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = false;
                class_flg = false;
                brand_flg = true;
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                brand_text = str_brand;
            } else {
                brandList.add(txtClickedVal.trim());
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                brand_text = str_brand;
            }
        }
    }

    private void buildUpdataInventory(int filterLevel) {
        if (filterLevel == 2) {
            if (!department_flg) {
                departmentList.add(txtClickedVal.trim());
                department_flg = true;
                cat_flg = false;
                class_flg = false;
                brand_flg = false;
                brandcls_flg = false;
                String[] array = (String[]) departmentList.toArray(new String[0]);
                String str_department = Arrays.toString(array);
                str_department = str_department.replace("[", "");
                str_department = str_department.replace("]", "");
                str_department = str_department.replace(", ", ",");
                department_text = str_department;
                requestProductHierarchyAPI(filterLevel, department_text);
            } else {
                departmentList.add(txtClickedVal.trim());
                String[] array = (String[]) departmentList.toArray(new String[0]);
                String str_department = Arrays.toString(array);
                str_department = str_department.replace("[", "");
                str_department = str_department.replace("]", "");
                str_department = str_department.replace(", ", ",");
                department_text = str_department;
                requestProductHierarchyAPI(filterLevel, department_text);
            }
        }
        if (filterLevel == 3) {
            if (!cat_flg) {
                categryList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = true;
                class_flg = false;
                brand_flg = false;
                brandcls_flg = false;
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, categry_text);
            } else {
                categryList.add(txtClickedVal.trim());
                String[] array = (String[]) categryList.toArray(new String[0]);
                String str_cate = Arrays.toString(array);
                str_cate = str_cate.replace("[", "");
                str_cate = str_cate.replace("]", "");
                str_cate = str_cate.replace(", ", ",");
                categry_text = str_cate;
                requestProductHierarchyAPI(filterLevel, categry_text);
            }
        }
        if (filterLevel == 4) {
            if (!class_flg) {
                classList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = false;
                class_flg = true;
                brand_flg = false;
                brandcls_flg = false;
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                class_text = str_class;
                requestProductHierarchyAPI(filterLevel, class_text);
            } else {
                classList.add(txtClickedVal.trim());
                String[] array = (String[]) classList.toArray(new String[0]);
                String str_class = Arrays.toString(array);
                str_class = str_class.replace("[", "");
                str_class = str_class.replace("]", "");
                str_class = str_class.replace(", ", ",");
                class_text = str_class;
                requestProductHierarchyAPI(filterLevel, class_text);
            }
        }
        if (filterLevel == 5) {
            if (!brand_flg) {
                brandList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = false;
                class_flg = false;
                brand_flg = true;
                brandcls_flg = false;
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                brand_text = str_brand;
                requestProductHierarchyAPI(filterLevel, brand_text);
            } else {
                brandList.add(txtClickedVal.trim());
                String[] array = (String[]) brandList.toArray(new String[0]);
                String str_brand = Arrays.toString(array);
                str_brand = str_brand.replace("[", "");
                str_brand = str_brand.replace("]", "");
                str_brand = str_brand.replace(", ", ",");
                brand_text = str_brand;
                requestProductHierarchyAPI(filterLevel, brand_text);
            }
        }
        if (filterLevel == 6) {
            if (!brandcls_flg) {
                brandclsList.add(txtClickedVal.trim());
                department_flg = false;
                cat_flg = false;
                class_flg = false;
                brand_flg = false;
                brandcls_flg = true;
                String[] array = (String[]) brandclsList.toArray(new String[0]);
                String str_brndcls = Arrays.toString(array);
                str_brndcls = str_brndcls.replace("[", "");
                str_brndcls = str_brndcls.replace("]", "");
                str_brndcls = str_brndcls.replace(", ", ",");
                brandcls_text = str_brndcls;
            } else {
                brandclsList.add(txtClickedVal.trim());
                String[] array = (String[]) brandclsList.toArray(new String[0]);
                String str_brandcls = Arrays.toString(array);
                str_brandcls = str_brandcls.replace("[", "");
                str_brandcls = str_brandcls.replace("]", "");
                str_brandcls = str_brandcls.replace(", ", ",");
                brandcls_text = str_brandcls;
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

            if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("ezonepvaAnalysis")) {
                explv_ez_prod.collapseGroup(0);
                explv_ez_prod.collapseGroup(1);
                explv_ez_prod.collapseGroup(2);
                explv_ez_prod.collapseGroup(3);
                explv_ez_locatn.collapseGroup(0);
                explv_ez_locatn.collapseGroup(1);

            } else {
                explv_ez_prod.collapseGroup(0);
                explv_ez_prod.collapseGroup(1);
                explv_ez_prod.collapseGroup(2);
                explv_ez_prod.collapseGroup(3);
                explv_ez_prod.collapseGroup(4);
                explv_ez_locatn.collapseGroup(0);
                explv_ez_locatn.collapseGroup(1);
            }

        } else {

            if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("ezonepvaAnalysis")) {

                for (int j = 0; j < 4; j++) {
                    List<String> arrayList1 = new ArrayList<String>();

                    for (int k = 0; k < dublicate_listDataChild2.get(mListDataGroup.get(j)).size(); k++) {
                        if (dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                            arrayList1.add(dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k));

                        }
                    }
                    mListDataChild.put(mListDataGroup.get(j), arrayList1);
                }
                explv_ez_prod.expandGroup(0);
                explv_ez_prod.expandGroup(1);
                explv_ez_prod.expandGroup(2);
                explv_ez_prod.expandGroup(3);
                notifyDataSetChanged();
            } else {

                for (int j = 0; j < 5; j++) {
                    List<String> arrayList1 = new ArrayList<String>();

                    for (int k = 0; k < dublicate_listDataChild2.get(mListDataGroup.get(j)).size(); k++) {
                        if (dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k).toLowerCase(Locale.getDefault()).contains(charText)) {
                            arrayList1.add(dublicate_listDataChild2.get(mListDataGroup.get(j)).get(k));

                        }
                    }
                    mListDataChild.put(mListDataGroup.get(j), arrayList1);
                }
                explv_ez_prod.expandGroup(0);
                explv_ez_prod.expandGroup(1);
                explv_ez_prod.expandGroup(2);
                explv_ez_prod.expandGroup(3);
                explv_ez_prod.expandGroup(4);
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
        if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("ezonepvaAnalysis")) {
            if (prod_level == 2) {
                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&department=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 3) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&category=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 4) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&class=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;
            }
        } else {
            if (prod_level == 2)
            {
                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&department=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 3) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&category=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;

            } else if (prod_level == 4) {

                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&class=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;
            } else if (prod_level == 5) {
                search_url = ConstsCore.web_url + "/v1/display/globalsearchNew/" + userId + "?level=" + prod_level + "&brand=" + str.replaceAll("&", "%26").replace(" ", "%20")+ "&geoLevel2Code=" + geoLevel2Code+ "&lobId="+ lobId;
            }
        }
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, search_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mContext, "no data found", Toast.LENGTH_LONG).show();
                                rel_ez_process_filter.setVisibility(View.GONE);

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
                                        if (str_checkFrom.equals("ezoneSales") || str_checkFrom.equals("ezonepvaAnalysis")) {
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
                                    }
                                    for (int k = prod_level - 1; k < mListDataGroup.size(); k++) {
                                        explv_ez_prod.expandGroup(k);
                                    }
                                }
                                notifyDataSetChanged();
                                rel_ez_process_filter.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            Toast.makeText(mContext, "data failed..." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        rel_ez_process_filter.setVisibility(View.GONE);
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

