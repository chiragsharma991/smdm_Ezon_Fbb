package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import apsupportapp.aperotechnologies.com.designapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 23/08/16.
 */
public class SearchActivity1  extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    Button btnSubDept, btnMc, btnProductName, btnArticle;
    RelativeLayout btnBack;
    EditText editSearch;
    ImageButton btnSearch;
    MySingleton m_config;
    String searchData;
    String activeId;
    ListView listView, listView1, listView2;
    RequestQueue queue;
    RadioButton radioButton;
    //ArrayList<ProductLevelBean> productsubdeptList,productNameList,productArticleList;
    ArrayList<String> productsubdeptList, productNameList, productArticleList;
    // SubDeptAdapter adapter;
    ValueAdapter adapter;
    //    ProductAdapter productAdapter;
//    ArticleAdapter articleAdapter;
    ProductLevelBean productLevelBean;
    Context context;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    TextWatcher mSearchTw;
    int offsetvalue = 0, limit = 100, offsetvalue1 = 0, limit1 = 100, offsetvalue2 = 0, limit2 = 100;
    int count = 0, count1 = 0, count2 = 0;

    public static String searchSubDept = "",searchProductName = "", searchArticleOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_search1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        m_config = MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


        context = this;
        searchSubDept = "";
        searchProductName = "";
        searchArticleOption = "";
        radioButton=(RadioButton)findViewById(R.id.button31);
        radioButton.toggle();
        SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
        segmented3.setOnCheckedChangeListener(this);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            requestSubDeptAPI(offsetvalue, limit);
        } else {
            // Reusable_Functions.hDialog();
            Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        activeId = "SubDept";
        context = this;
        productsubdeptList = new ArrayList<>();
        //productMCList= new ArrayList<ProductLevelBean>();
        productNameList = new ArrayList<>();
        productArticleList = new ArrayList<>();
        // btnSubDept = (Button) findViewById(R.id.btnSubDept);
        listView = (ListView) findViewById(R.id.list);
        //  btnSubDept.setOnClickListener(this);
        //   btnProductName = (Button) findViewById(R.id.btnProductName);
        // btnProductName.setOnClickListener(this);
        // btnArticle = (Button) findViewById(R.id.btnArticle);
        // btnArticle.setOnClickListener(this);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(this);
        btnSearch=(ImageButton) findViewById(R.id.btnSeatchList);
        btnSearch.setOnClickListener(this);
        editSearch = (EditText) findViewById(R.id.editSearch);


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageBtnBack:
//                Intent intent = new Intent(SearchActivity1.this, DashBoardActivity.class);
//                startActivity(intent);
                Intent i = new Intent(SearchActivity1.this, KeyProductActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnSeatchList:
                // editSearch.setText("");
                searchData = editSearch.getText().toString();
//                Matcher m = Pattern.compile(Pattern.quote(searchData, Pattern.CASE_INSENSITIVE).matcher("");
                Log.e("list", searchData);
                editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                adapter.getFilter().filter(searchData);
                break;
            default:
                break;


        }
    }
    /*-------------------------API Declaration ----------------------------*/


    public void requestSubDeptAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/hourlyfilterproducts/"+userId+"?offset=" + offsetvalue1 + "&limit=" + limit1;

        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("SubDept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SearchActivity1.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String prodLevel3Code = productName1.getString("prodLevel3Code");
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
//                                   productLevelBean =new ProductLevelBean();
//                                   productLevelBean.setProductLevel3Code(prodLevel3Code);
//                                   productLevelBean.setProductLevel3Desc(prodLevel3Desc);
                                    productsubdeptList.add(prodLevel3Desc);
                                    // Log.e("ArrayList", "" + productsubdeptList.size());
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSubDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String prodLevel3Code = productName1.getString("prodLevel3Code");
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
                                    // productLevelBean =new ProductLevelBean();
//                                    productLevelBean.setProductLevel3Code(prodLevel3Code);
//                                    productLevelBean.setProductLevel3Desc(prodLevel3Desc);
                                    productsubdeptList.add(prodLevel3Desc);
                                    Log.e("ArrayList", "" + productsubdeptList.size());
                                }

                                Collections.sort(productsubdeptList);


                                adapter = new ValueAdapter(productsubdeptList, getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setTextFilterEnabled(true);
                                Reusable_Functions.hDialog();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        String productSubDeptItem = (String) parent.getItemAtPosition(position);

                                        searchSubDept = productSubDeptItem.replaceAll(" ","%20").replaceAll("&","%26");

                                        Intent intent = new Intent(SearchActivity1.this,KeyProductActivity.class);
                                        //intent.putExtra("productSubDeptItem",productSubDeptItem);
                                        Log.e("productSubDeptItem",productSubDeptItem+" ==== "+searchSubDept);
                                        startActivity(intent);
                                        finish();

                                    }
                                });

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
                params.put("Authorization", "Bearer "+bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestProductListAPI(int offsetvalue2, final int limit2) {

        String url = ConstsCore.web_url + "/v1/display/hourlyfilterproducts/"+userId+"?view=PNF&offset=" + offsetvalue1 + "&limit=" + limit1;

        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("ProduName Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count1 == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SearchActivity1.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit1) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String productName = productName1.getString("productName");

//                                    productLevelBean = new ProductLevelBean();
//                                    productLevelBean.setProductName(productName);

                                    productNameList.add(productName);
                                    Log.e("ArrayList-----", "" + productNameList.size());
                                }
                                offsetvalue1 = (limit1 * count1) + limit1;
                                count1++;
                                requestProductListAPI(offsetvalue1, limit1);

                            } else if (response.length() < limit1) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String productName = productName1.getString("productName");
//
//                                    productLevelBean = new ProductLevelBean();
//                                    productLevelBean.setProductName(productName);

                                    productNameList.add(productName);
                                    Log.e("ArrayList-----", "" + productNameList.size());
                                }


                                Collections.sort(productNameList);
                                adapter = new ValueAdapter(productNameList, getApplicationContext());
                                listView1.setAdapter(adapter);
                                listView1.setTextFilterEnabled(true);
                                Reusable_Functions.hDialog();

                                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        String productNameItem = (String) parent.getItemAtPosition(position);

                                        searchProductName = productNameItem.replaceAll(" ","%20").replaceAll("&","%26");

                                        Intent intent = new Intent(SearchActivity1.this,KeyProductActivity.class);
                                        //intent.putExtra("productSubDeptItem",productSubDeptItem);
                                        Log.e("productNameItem",productNameItem+" ==== "+searchProductName);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

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
                params.put("Authorization", "Bearer "+bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


//    private void requestArticleAPI(int offsetvalue3, int limit3) {
//        String url = "https://ra.manthan.com/v1/display/hourlyfilterproducts/"+userId+"?view=ANF&offset=" + offsetvalue2 + "&limit=" + limit2;
//
//        Log.i("URL   ", url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("Article Response", response.toString());
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count2 == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(SearchActivity1.this, "no data found", Toast.LENGTH_LONG).show();
//                            } else if (response.length() == limit2) {
//                                /// Reusable_Functions.hDialog();
//
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String articleCode = productName1.getString("articleCode");
//                                    String articleDesc = productName1.getString("articleDesc");
//
////                                    productLevelBean =new ProductLevelBean();
////                                    productLevelBean.setArticleCode(articleCode);
////                                    productLevelBean.setArticleDesc(articleDesc);
//                                    productArticleList.add(articleDesc);
//                                    Log.e("ArrayList1--------", "" + productArticleList.size());
//                                }
//                                offsetvalue2 = (limit2 * count2) + limit2;
//                                count2++;
//                                requestArticleAPI(offsetvalue2, limit2);
//                            } else if (response.length() < limit2) {
//
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String articleCode = productName1.getString("articleCode");
//                                    String articleDesc = productName1.getString("articleDesc");
//
////                                    productLevelBean = new ProductLevelBean();
////                                    productLevelBean.setArticleCode(articleCode);
////                                    productLevelBean.setArticleDesc(articleDesc);
//                                    productArticleList.add(articleDesc);
//                                    Log.e("ArrayList1--------", "" + productArticleList.size());
//                                }
//
//                                Collections.sort(productArticleList);
//                                adapter = new ValueAdapter(productArticleList, getApplicationContext());
//                                listView2.setAdapter(adapter);
//                                listView2.setTextFilterEnabled(true);
//                                Reusable_Functions.hDialog();
//                                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        String productArticleNameItem= (String) parent.getItemAtPosition(position);
//                                        Log.e("productArticleNameItem",productArticleNameItem);
//
//                                        searchArticleOption = productArticleNameItem.replaceAll(" ","%20").replaceAll("&","%26");
//
//                                        Intent intent = new Intent(SearchActivity1.this,KeyProductActivity.class);
//                                        //intent.putExtra("productSubDeptItem",productSubDeptItem);
//                                        Log.e("productNameItem",productArticleNameItem+" ==== "+searchArticleOption);
//                                        startActivity(intent);
//                                    }
//                                });
//
//
//                            }
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
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.button31:
              //  Toast.makeText(context, "One", Toast.LENGTH_SHORT).show();
                if (activeId == "SubDept")
                    break;

                    activeId = "SubDept";
                    productsubdeptList.clear();
//                    btnSubDept.setBackgroundResource(R.drawable.key_product_search_sub_dept_selected_button);
//                    btnArticle.setBackgroundResource(R.mipmap.key_product_search_article_unselected_button);
//                    btnProductName.setBackgroundResource(R.mipmap.key_product_search_product_name_unselected_button);
                    listView = (ListView) findViewById(R.id.list);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        offsetvalue = 0;
                        count = 0;
                        requestSubDeptAPI(offsetvalue, limit);
                    } else {
                        // Reusable_Functions.hDialog();
                        Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }


                break;
            case R.id.button32:
              //  Toast.makeText(context, "Two", Toast.LENGTH_SHORT).show();
                if (activeId == "ProductName")
                    break;

                    activeId = "ProductName";
                    productNameList.clear();
                    // editSearch.setText("");
//                    btnProductName.setBackgroundResource(R.drawable.key_product_search_product_name_selected_button);
//                    //btnMc.setBackgroundResource(R.mipmap.key_product_search_mc_unselected_button);
//                    btnSubDept.setBackgroundResource(R.mipmap.key_product_search_sub_dept_unselected_button);
//                    btnArticle.setBackgroundResource(R.mipmap.key_product_search_article_unselected_button);
                    listView1 = (ListView) findViewById(R.id.list);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        offsetvalue1 = 0;
                        count1 = 0;
                        requestProductListAPI(offsetvalue1, limit1);
                    } else {
                        // Reusable_Functions.hDialog();
                        Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }



                break;
                default:
//            case R.id.button33:
//               // Toast.makeText(context, "Three", Toast.LENGTH_SHORT).show();
//                if (activeId == "Article")
//                    break;
//
//                    activeId = "Article";
//                    productArticleList.clear();
//                    // editSearch.setText("");
////                    btnArticle.setBackgroundResource(R.drawable.key_product_search_article_selected_button);
////                    btnProductName.setBackgroundResource(R.mipmap.key_product_search_product_name_unselected_button);
////                    // btnMc.setBackgroundResource(R.mipmap.key_product_search_mc_unselected_button);
////                    btnSubDept.setBackgroundResource(R.mipmap.key_product_search_sub_dept_unselected_button);
//                    listView2 = (ListView) findViewById(R.id.list);
//                    if (Reusable_Functions.chkStatus(context)) {
//                        Reusable_Functions.sDialog(context, "Loading  data...");
//                        offsetvalue2 = 0;
//                        limit2 = 100;
//                        count2 = 0;
//                        productArticleList.clear();
//                        requestArticleAPI(offsetvalue2, limit2);
//                    } else {
//                        // Reusable_Functions.hDialog();
//                        Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                    }
//
//                break;
////            case R.id.imageBtnBack:
////                Intent intent = new Intent(SearchActivity1.this, DashBoardActivity.class);
////                startActivity(intent);
////                break;
////            case R.id.btnSeatchList:
////                // editSearch.setText("");
////                searchData = editSearch.getText().toString();
//////                Matcher m = Pattern.compile(Pattern.quote(searchData, Pattern.CASE_INSENSITIVE).matcher("");
////                Log.e("list", searchData);
////                editSearch.clearFocus();
////                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
////                in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
////                adapter.getFilter().filter(searchData);
////                break;
//            default:
                // Nothing to do
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SearchActivity1.this, KeyProductActivity.class);
        startActivity(i);
        finish();
        //super.onBackPressed();
    }

}