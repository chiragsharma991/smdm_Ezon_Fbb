package apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StyleDetailsBean;
import apsupportapp.aperotechnologies.com.designapp.SwitchingTabActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;


/**
 * Created by csuthar on 18/11/16.
 */


public class TopOptionAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;
    String TAG = "TopCut";
    ArrayList<StyleDetailsBean> optionList;
    StyleDetailsBean styleDetailsBean;
    Gson gson;
    int offset,limit;

    //private ValueFilter valueFilter;

    public TopOptionAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

        // Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        optionList = new ArrayList<StyleDetailsBean>();
        gson = new Gson();
        offset = 0;
        limit = 10;
        //getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {


        return arrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Log.e("in ","getview");

        Position=position;
        final Holder holder;
        if (convertView == null) {
            holder=new Holder();
            convertView = mInflater.inflate(R.layout.activity_topfullcut_child, null);
            holder.Top_SOHU = (TextView) convertView.findViewById(R.id.top_SOHU);
            holder.Top_bstStockU = (TextView) convertView.findViewById(R.id.top_bstStockU);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.Top_option = (TextView) convertView.findViewById(R.id.bst_option);
            holder.Top_RosU = (TextView) convertView.findViewById(R.id.top_RosU);
            holder.Top_image_child = (ImageView) convertView.findViewById(R.id.top_image_child);
            holder.Top_fav = (RelativeLayout) convertView.findViewById(R.id.top_fav);



            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);


        }
        holder.Top_option.setText(arrayList.get(position).getOption());
        //Option Click event to get detail information
        holder.Top_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //  Toast.makeText(context,"Option Click...",Toast.LENGTH_SHORT).show();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading  data...");
                    Log.e("select item", arrayList.get(position).getOption());
                    requestOptionDetailsAPI(arrayList.get(position).getOption());
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
            }
        });


        holder.Top_SOHU.setText(""+Math.round(arrayList.get(position).getStkOnhandQty()));
        holder.Top_bstStockU.setText(""+(int)arrayList.get(position).getTargetStock());
        holder.Top_RosU.setText(""+String.format("%.1f",arrayList.get(position).getRos()));

        if(!arrayList.get(position).getProdImageURL().equals("")) {
            Picasso.with(this.context).

                    load(arrayList.get(position).getProdImageURL()).
                    into(holder.Top_image_child, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.ProgressPicaso.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.ProgressPicaso.setVisibility(View.GONE);

                        }
                    });
        }else {
            holder.ProgressPicaso.setVisibility(View.GONE);

            Picasso.with(this.context).
                    load(R.mipmap.placeholder).
                    into(holder.Top_image_child);

        }


        // ---------------------click listener -------------------------
       return convertView;
    }

    private void requestOptionDetailsAPI(String option)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = " ";

        url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?articleOption=" + option.replaceAll(" ", "%20").replaceAll("&", "%26")+"&offset="+offset+"&limit="+limit ;

        Log.e(TAG, "requestStyleDetailsAPI  " + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, " requestStyleDetailsAPI :   " + response.toString());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_LONG).show();
                            } else if(response.length() < limit){
                                Reusable_Functions.hDialog();
                                for ( i = 0; i < response.length(); i++) {

                                    styleDetailsBean = gson.fromJson(response.get(i).toString(), StyleDetailsBean.class);
                                    optionList.add(styleDetailsBean);

                                }

                                Log.e(TAG, "intent calling: ");
                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom","topCut");
                                intent.putExtra("articleCode",styleDetailsBean.getArticleCode());
                                intent.putExtra("articleOption",styleDetailsBean.getArticleOption());
                                Log.e("Article Option :",""+styleDetailsBean.getArticleOption());
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                context.startActivity(intent);
                                TopFullCut.topFullcut.finish();
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
                        Log.e("", "" + error.networkResponse + "");
                        Toast.makeText(context, "Network connectivity fail", Toast.LENGTH_LONG).show();
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

    private class Holder {

        TextView Top_SOHU,Top_bstStockU,Top_RosU,Top_option;
        ImageView Top_image_child;
        RelativeLayout Top_fav;


        public ProgressBar ProgressPicaso;
    }




}
