//package apsupportapp.aperotechnologies.com.designapp.Httpcall;
//
//import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Cache;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Network;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.JsonArrayRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventoryAdapter;
//import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
//import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
//import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
//
///**
// * Created by csuthar on 10/01/17.
// */
//
//public class ApiRequest  {
//
//
//    public ApiRequest(Context context)
//    {
//
//    }
//
//    public void setApi(final Context context) {
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//
//
//
//        String url;
//
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//
//                        try {
//
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//
//
//                            }
//
//
//                        } catch (Exception e) {
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
//
//
//                        error.printStackTrace();
//                    }
//
//
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        RequestQueue queue = new RequestQueue(cache, network);
//
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//
//    }
//}
//
//
//
//
