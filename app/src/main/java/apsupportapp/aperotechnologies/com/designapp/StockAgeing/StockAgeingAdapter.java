package apsupportapp.aperotechnologies.com.designapp.StockAgeing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleDetailsBean;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.SwitchingTabActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

/**
 * Created by pamrutkar on 05/12/16.
 */
public class StockAgeingAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;
    private LayoutInflater mInflater;
    Context context;
    int Position;
    int offset, limit;
    StyleDetailsBean styleDetailsBean;
    ArrayList<StyleDetailsBean> optionDetailsList;
    Gson gson;

    public StockAgeingAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        gson = new Gson();
        optionDetailsList = new ArrayList<StyleDetailsBean>();
        offset = 0;
        limit = 10;
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

        Position = position;
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.activity_stock_ageing_child, null);
            holder.stock_ageing = (TextView) convertView.findViewById(R.id.stock_ageing);
            holder.stock_SOH_U = (TextView) convertView.findViewById(R.id.stock_SOH_U);
            holder.stock_option = (TextView) convertView.findViewById(R.id.stock_option);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.imgLoader_stockAge);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.stock_image_child = (ImageView) convertView.findViewById(R.id.stock_image_child);
            holder.stock_fav = (RelativeLayout) convertView.findViewById(R.id.stock_fav);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
        }
        holder.stock_option.setText(arrayList.get(position).getOption());

        //Option Click event to get detail information
//        holder.stock_option.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Reusable_Functions.chkStatus(context)) {
//                    Reusable_Functions.hDialog();
//                    Reusable_Functions.sDialog(context, "Loading  data...");
//                    requestOptionDetailsAPI(arrayList.get(position).getOption());
//                } else {
//                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        holder.stock_ageing.setText(arrayList.get(position).getStockageBandDesc() + " Days");
        holder.stock_SOH_U.setText("" + Math.round(arrayList.get(position).getStkOnhandQty()));

        if (!arrayList.get(position).getProdImageURL().equals("")) {

            Glide.with(this.context)
                    .load(arrayList.get(position).getProdImageURL())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.ProgressPicaso.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.ProgressPicaso.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.stock_image_child);

        } else {
            holder.ProgressPicaso.setVisibility(View.GONE);
            Glide.with(this.context).
                    load(R.mipmap.noimageavailable).
                    into(holder.stock_image_child);

        }

        // ---------------------click listener -------------------------
        return convertView;
    }

    private void requestOptionDetailsAPI(String option) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        String geoLevel2Code = sharedPreferences.getString("concept", "");
        String lobId = sharedPreferences.getString("lobid", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url;
        url = ConstsCore.web_url + "/v1/display/productdetailsNew/" + userId + "?articleOption=" + option.replaceAll(" ", "%20").replaceAll("&", "%26") +"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() < limit) {
                                Reusable_Functions.hDialog();
                                for (i = 0; i < response.length(); i++) {
                                    styleDetailsBean = gson.fromJson(response.get(i).toString(), StyleDetailsBean.class);
                                    optionDetailsList.add(styleDetailsBean);
                                }
                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom", "stockAgeing");
                                intent.putExtra("articleCode", styleDetailsBean.getArticleCode());
                                intent.putExtra("articleOption", styleDetailsBean.getArticleOption());
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                context.startActivity(intent);
                                StockAgeingActivity.stockAgeing.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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
        TextView stock_ageing, stock_SOH_U, stock_option;
        ImageView stock_image_child;
        RelativeLayout stock_fav;
        public ProgressBar ProgressPicaso;
    }
}
