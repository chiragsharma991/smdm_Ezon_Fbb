package apsupportapp.aperotechnologies.com.designapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
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

public class StyleActivity extends AppCompatActivity {
    Button  btnSearch,btnBarcode,imageBtnBack;
    Spinner collection,style;
    ArrayAdapter<String> adapter1,adapter2;
    List<String> collectionList,StyleList;
    ArrayList<String> arrayList;
    String userId;
    RequestQueue queue;
    Context context;
    StyleDetailsBean styleDetailsBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_style);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        context=this;


        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

//        Bundle bundle=getIntent().getExtras();
//        userId=bundle.getString("userId");
//        Log.d("userId","  "+userId);

        collectionList = new ArrayList<String>();
        arrayList=new ArrayList<String>();
//        Bundle b = getIntent().getExtras();
//
//        if(b!=null){
//             arrayList = (ArrayList<String>)b.getStringArrayList("arrayList");
//            Log.e("list  :",""+arrayList.get(0));
//           // Collections.copy(collectionList,arrayList);
//        }
//        Log.e("list  :",""+arrayList.get(0));

        requestCollectionAPI();


        btnSearch =(Button)findViewById(R.id.btnSearch);
        btnBarcode=(Button)findViewById(R.id.btnBarcode);
        imageBtnBack =(Button)findViewById(R.id.imageBtnBack);
        collection=(Spinner)findViewById(R.id.spinnerCollection);


        //collectionList.add(arrayList.get(0));
        collectionList.add("Collection 2");
        collectionList.add("Collection 3");
        collectionList.add("Collection 4");
        collectionList.add("Collection 5");
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, collectionList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collection.setAdapter(adapter1);

        style=(Spinner)findViewById(R.id.spinnerStyleType);
        StyleList = new ArrayList<String>();
        StyleList.add("Style 1");
        StyleList.add("Style 2");
        StyleList.add("Style 3");
        StyleList.add("Style 4");
        StyleList.add("Style 5");
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StyleList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        style.setAdapter(adapter2);
        style=(Spinner)findViewById(R.id.spinnerStyleType);




        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Reusable_Functions.sDialog(context,"fetching data");
                requestStyleDetailsAPI();


            }
        });
        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAMobileModel()){
                    Intent intent = new Intent(StyleActivity.this,ScannerActivity.class);
                    startActivity(intent);
                }
                else if(!isAMobileModel())
                {
                    Toast.makeText(StyleActivity.this,"Scanner is not inbuilt", Toast.LENGTH_LONG).show();

                    Intent intent1= new Intent(StyleActivity.this,ScannerActivity1.class);
                    startActivity(intent1);
                }


            }
        });
        imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StyleActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
    private boolean isAMobileModel()  {

        getDeviceInfo();

        return Build.MODEL.contains("G0550");
    }


    public String getDeviceInfo() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    private void requestStyleDetailsAPI()
    {
        String url="https://ra.manthan.com/v1/display/productfabricdetails/9?articleCode=000000001000459024";
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i("Style details :   ", response.toString());
                        try
                        {

                            // JSONObject mainObject = new JSONObject(response);
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject styleDetails = response.getJSONObject(i);
                                String productName=styleDetails.getString("productName");
                                String collectionName=styleDetails.getString("collectionName");
                                String productFabricDesc=styleDetails.getString("productFabricDesc");
                                String productFitDesc=styleDetails.getString("productFitDesc");
                                String productFinishDesc=styleDetails.getString("productFinishDesc");
                                String seasonName=styleDetails.getString("seasonName");

                                String promoFlg=styleDetails.getString("promoFlg");
                                String keyProductFlg=styleDetails.getString("keyProductFlg");




                                String firstReceiptDate=styleDetails.getString("firstReceiptDate");
                                String lastReceiptDate=styleDetails.getString("lastReceiptDate");
                                int fwdWeekCover=styleDetails.getInt("fwdWeekCover");

                                int twSaleTotQty=styleDetails.getInt("twSaleTotQty");
                                int lwSaleTotQty=styleDetails.getInt("lwSaleTotQty");
                                int ytdSaleTotQty=styleDetails.getInt("ytdSaleTotQty");

                                int stkOnhandQty=styleDetails.getInt("stkOnhandQty");
                                int stkGitQty=styleDetails.getInt("stkGitQty");
                                int targetStock=styleDetails.getInt("targetStock");

                                int unitGrossPrice=styleDetails.getInt("unitGrossPrice");
                                double sellThruUnitsRcpt=styleDetails.getDouble("sellThruUnitsRcpt");
                                int ros=styleDetails.getInt("ros");

                                int usp=styleDetails.getInt("usp");

                                styleDetailsBean=new StyleDetailsBean();

                                styleDetailsBean.setProductName(productName);
                                styleDetailsBean.setCollectionName(collectionName);
                                styleDetailsBean.setProductFabricDesc(productFabricDesc);
                                styleDetailsBean.setProductFitDesc(productFitDesc);
                                styleDetailsBean.setProductFinishDesc(productFinishDesc);
                                styleDetailsBean.setSeasonName(seasonName);

                                styleDetailsBean.setFirstReceiptDate(firstReceiptDate);
                                styleDetailsBean.setLastReceiptDate(lastReceiptDate);
                                styleDetailsBean.setFwdWeekCover(fwdWeekCover);

                                styleDetailsBean.setTwSaleTotQty(twSaleTotQty);
                                styleDetailsBean.setLwSaleTotQty(lwSaleTotQty);
                                styleDetailsBean.setYtdSaleTotQty(ytdSaleTotQty);

                                styleDetailsBean.setUnitGrossPrice(unitGrossPrice);
                                styleDetailsBean.setSellThruUnitsRcpt(sellThruUnitsRcpt);
                                styleDetailsBean.setRos(ros);

                                styleDetailsBean.setUsp(usp);






                                Log.e("Style details:",productName+"   "+collectionName+"  "+productFabricDesc+"  "+productFitDesc+"  "+productFinishDesc+"  "+seasonName);
                                Log.e("row 1:",firstReceiptDate+"   "+lastReceiptDate+"  "+fwdWeekCover);
                                Log.e("row 2:",twSaleTotQty+"   "+lwSaleTotQty+"  "+ytdSaleTotQty);
                                Log.e("row 3:",stkOnhandQty+"   "+stkGitQty+"  "+targetStock);
                                Log.e("row4:",unitGrossPrice+"   "+sellThruUnitsRcpt+"  "+ros);
                                Log.e("benefit :",""+usp);

                            }
                            if(response !=null){
                                Reusable_Functions.hDialog();
                            }

                            Intent intent=new Intent(StyleActivity.this,SwitchingTabActivity.class);
                            intent.putExtra("styleDetailsBean",styleDetailsBean);
                            startActivity(intent);


                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);




    }


    private void requestCollectionAPI()
    {
        String url="https://ra.manthan.com/v1/display/collections/"+userId;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {
                            // JSONObject mainObject = new JSONObject(response);
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject collectionName = response.getJSONObject(i);
                                Log.e("collectionName  :",collectionName.getString("collectionName"));
                                arrayList.add(collectionName.getString("collectionName"));
                                Log.e("size  :",""+arrayList.size());

                            }
//                               Intent intent = new Intent(DashBoardActivity.this, StyleActivity.class);
//                               intent.putExtra("arrayList",arrayList);
//                                startActivity(intent);


                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);





    }


    private void requestArticleOptionsAPI()
    {
        String url="https://ra.manthan.com/v1/display/collectionarticles/"+userId+"?offset=1";
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {
                            // JSONObject mainObject = new JSONObject(response);
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject jsonResponse = response.getJSONObject(i);
                                String collectionNames=jsonResponse.getString("collectionNames");
                                String articleOptions=jsonResponse.getString("articleOptions");
                                Log.e("collectionName  :",collectionNames+"   "+articleOptions);


                            }
//                            Intent intent = new Intent(DashBoardActivity.this, StyleActivity.class);
//                            intent.putExtra("arrayList",arrayList);
//                            startActivity(intent);


                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                // String auth_code = "Basic " + Base64.encodeToString((uname+":"+password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
                // Log.i("Auth Code", auth_code);
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;


            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }





}
