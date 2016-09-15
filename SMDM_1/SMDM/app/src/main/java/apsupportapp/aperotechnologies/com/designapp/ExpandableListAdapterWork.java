package  apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
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

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.FilterArray;
import apsupportapp.aperotechnologies.com.designapp.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.Prod_FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


/**
 * Created by pamrutkar on 07/09/16.
 */
public class ExpandableListAdapterWork extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    ExpandableListView pfList;
    ExpandableListAdapter listAdapter;
    // ChildViewHolder childViewHolder;
    private HashMap<Integer, boolean[]> mChildCheckStates;
    ArrayList<FilterArray> arrfilter;
    int offsetvalue = 0, limit = 100, count = 0;
    List<String> productList;
    ArrayList productnamelist;
    int gposition, cposition;
    String ProductName;
    ArrayAdapter<String> adapter;


    public ExpandableListAdapterWork(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        //listAdapter= new ExpandableListAdapter(_context,_listDataHeader,_listDataChild);
        mChildCheckStates = new HashMap<Integer, boolean[]>();
        productList = new ArrayList<String>();
        productnamelist = new ArrayList();
        arrfilter = new ArrayList<FilterArray>();

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        gposition = groupPosition;
        cposition = childPosition;

        Log.e("get ChildView","");

        View participentView = convertView;
        final ViewHolder view;

        if (participentView == null) {
            view = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(_context.LAYOUT_INFLATER_SERVICE);
            participentView = inflater.inflate(R.layout.pfilter_list_item, null);
            view.mchildText = (TextView) participentView
                    .findViewById(R.id.txtsubdeptname);
            view.pitem_checkBox = (CheckBox) participentView
                    .findViewById(R.id.itemCheckBox);

            participentView.setTag(view);
        } else {
            view = (ViewHolder) participentView.getTag();
        }
        view.mchildText.setText(childText);
        //view.pitem_checkBox.setChecked(false);
        view.pitem_checkBox.setTag(childPosition);
        if (groupPosition == 1) {
            view.pitem_checkBox.setVisibility(View.GONE);

        }

        participentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("v", " " + v);

                Log.e("cb", " " + view.pitem_checkBox + " " + view.pitem_checkBox.isChecked() + " " + view.pitem_checkBox.isSelected() + " " + (view.pitem_checkBox.isChecked() == false));
                if (view.pitem_checkBox.isChecked() == false) {

                    Log.e("checkbox is not selected", "");
                    switch (groupPosition) {
                        case 0:

                            if (Reusable_Functions.chkStatus(_context)) {
//                                if(Prod_FilterActivity.pfilter_list.isGroupExpanded(1)== false) {

                                view.pitem_checkBox.setChecked(true);
                                Prod_FilterActivity.pfilter_list.collapseGroup(1);



                                Reusable_Functions.sDialog(_context, "Loading  data...");
                                offsetvalue = 0;
                                count = 0;
                                limit = 100;
                                productList = new ArrayList<String>();
                                requestFilterProductAPI(offsetvalue, limit, view.mchildText.getText().toString());

                            } else {
                                // Reusable_Functions.hDialog();

                                Toast.makeText(_context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 1:
                            Prod_FilterActivity.pfilter_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                            Intent intent = new Intent(_context, KeyProductActivity.class);
                            intent.putExtra("filterproductname", view.mchildText.getText().toString());
                            Log.e("------ 1", " " + view.mchildText.getText().toString());
                            _context.startActivity(intent);

                            break;
                    }
                } else {
                    Log.e("checkbox is  selected", "");

                    for (int i = 0; i < arrfilter.size(); i++) {
                        if (arrfilter.get(i).getSubdept().equals(view.mchildText.getText().toString())) {
                            //Log.e("---",""+productnamelist.retainAll(arrfilter.get(i).getprodarray()));
                            productnamelist.removeAll(arrfilter.get(i).getprodarray());
                            arrfilter.remove(i);
                            view.pitem_checkBox.setChecked(false);


                            return;
                        }

                    }
                }
            }
        });

        return participentView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {

        if (this._listDataChild.get(this._listDataHeader.get(groupPosition)) != null) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.pfilter_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void requestFilterProductAPI(int offsetvalue1, int limit1, final String subdeptName) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this._context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(_context.getCacheDir(), 1024 * 1024); // 1MB cap
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
                                Toast.makeText(_context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);

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
                                    Log.e("Product Name:", ProductName);

                                    productList.add(ProductName);
                                    productnamelist.add(ProductName);
                                }
                                Collections.sort(productnamelist);
                                //view.pitem_checkBox.setVisibility(View.GONE);
                                _listDataChild.put(_listDataHeader.get(1), productnamelist);
                                Prod_FilterActivity.pfilter_list.expandGroup(1);
//                                Prod_FilterActivity.pfilter_list.setAdapter(listAdapter);
//                                listAdapter.notifyDataSetChanged();

                                Reusable_Functions.hDialog();

                                FilterArray filterArray = new FilterArray();
                                filterArray.setSubdept(subdeptName);
                                filterArray.setprodArray((ArrayList) productList);
                                arrfilter.add(filterArray);
                                Prod_FilterActivity.pfilter_list.expandGroup(1);

                                Log.e("--- ", " " + arrfilter.size());
//                                Prod_FilterActivity.pf_productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        Intent intent = new Intent(_context, KeyProductActivity.class);
//                                        intent.putExtra("filterproductname", productnamelist.get(position).toString());
//                                        Log.e("------ 1", " " + productnamelist.get(position));
//                                        _context.startActivity(intent);
//
//                                    }
//                                });


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
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
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

//    public  void requestSubDeptAPI(int offsetvalue1, int limit1) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this._context);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(_context.getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
//
//        String url = ConstsCore.web_url + "/v1/display/hourlyfilterproducts/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1;
//
//        Log.i("URL   ", url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("SubDept Response", response.toString());
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(_context, "no data found", Toast.LENGTH_LONG).show();
//                            } else if (response.length() == limit) {
//
//                                Reusable_Functions.hDialog();
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject productName1 = response.getJSONObject(i);
//
//                                    String prodLevel3Code = productName1.getString("prodLevel3Code");
//                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
////
//                                    Prod_FilterActivity.subdept.add(prodLevel3Desc);
//
//
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestSubDeptAPI(offsetvalue, limit);
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String prodLevel3Code = productName1.getString("prodLevel3Code");
//                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
//                                    Prod_FilterActivity.subdept.add(prodLevel3Desc);
//                                    // Log.e("ArrayList", "" + productsubdeptList.size());
//                                }
//
//                                Collections.sort( Prod_FilterActivity.subdept);
//                                _listDataChild.put(_listDataHeader.get(0),  Prod_FilterActivity.subdept);
//                                Reusable_Functions.hDialog();
//
//
//                            }
//
//
//                        } catch (Exception e) {
//                            Log.e("Exception e", e.toString() + "");
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
//                params.put("Authorization","Bearer "+bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//

    public void onBackPressed() {
        Intent i = new Intent(_context, KeyProductActivity.class);
        _context.startActivity(i);

    }

    class ViewHolder {
        TextView mchildText;
        CheckBox pitem_checkBox;
    }
}



