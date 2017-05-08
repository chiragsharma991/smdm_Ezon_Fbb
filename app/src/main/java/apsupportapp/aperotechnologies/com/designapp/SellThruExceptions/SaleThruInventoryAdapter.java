package apsupportapp.aperotechnologies.com.designapp.SellThruExceptions;

/**
 * Created by csuthar on 05/12/16.
 */

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
 * Created by csuthar on 29/11/16.
 */


public class SaleThruInventoryAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;
    ArrayList<StyleDetailsBean> styleDetailsBeanArrayList;
    StyleDetailsBean styleDetailsBean;
    int offset, limit;
    Gson gson;
    String TAG = "Sell Thru Exceptions";
    private LayoutInflater mInflater;
    Context context;
    private int Position;


    public SaleThruInventoryAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

        this.arrayList = arrayList;
        styleDetailsBeanArrayList = new ArrayList<StyleDetailsBean>();
        gson = new Gson();
        offset = 0;
        limit = 10;
        this.context = context;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.activity_salethru_inventory_child, null);
            holder.BestInvent_SOH = (TextView) convertView.findViewById(R.id.bestInvent_SOH);
            holder.BestInvent_sellThru = (TextView) convertView.findViewById(R.id.bestInvent_sellThru);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.BestInvent_Sale = (TextView) convertView.findViewById(R.id.bestInvent_Sale);
            holder.bestInvent_zonalSell = (TextView) convertView.findViewById(R.id.bestInvent_zonalSell);
            holder.bestInvent_wks = (TextView) convertView.findViewById(R.id.bestInvent_wks);
            holder.BestInvent_option = (TextView) convertView.findViewById(R.id.bestInvent_option);
            holder.BestInvent_image_child = (ImageView) convertView.findViewById(R.id.bestInvent_image_child);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);


        }
        holder.BestInvent_SOH.setText("" + Math.round(arrayList.get(position).getStkOnhandQty()));
        holder.BestInvent_sellThru.setText(String.format("%.1f", arrayList.get(position).getSellthruUnits()));

        holder.bestInvent_zonalSell.setText(String.format("%.1f", arrayList.get(position).getSellthruUnitsZonal()));
        holder.BestInvent_Sale.setText("" + arrayList.get(position).getSaleTotQty());
        holder.BestInvent_option.setText(arrayList.get(position).getOption());
        //Option Click event to get detail information
        holder.BestInvent_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(context,"Option Click...",Toast.LENGTH_SHORT).show();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading  data...");
                    requestOptionDetailsAPI(arrayList.get(position).getOption());
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }

            }
        });

        SaleThruInventory.BestInvent_txtStoreCode.setText(arrayList.get(position).getStoreCode());
        SaleThruInventory.BestInvent_txtStoreName.setText(arrayList.get(position).getStoreDesc());

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
                    .into(holder.BestInvent_image_child);

        } else {
            holder.ProgressPicaso.setVisibility(View.GONE);

            Glide.with(this.context).
                    load(R.mipmap.placeholder).
                    into(holder.BestInvent_image_child);


        }

        // ---------------------click listener -------------------------

        return convertView;
    }

    private void requestOptionDetailsAPI(String option) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url;

        url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?articleOption=" + option.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offset + "&limit=" + limit;

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() < limit) {
                                Reusable_Functions.hDialog();
                                for (i = 0; i < response.length(); i++) {

                                    styleDetailsBean = gson.fromJson(response.get(i).toString(), StyleDetailsBean.class);
                                    styleDetailsBeanArrayList.add(styleDetailsBean);

                                }

                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom", "sell_thru_exception");
                                intent.putExtra("articleCode", styleDetailsBean.getArticleCode());
                                intent.putExtra("articleOption", styleDetailsBean.getArticleOption());
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                context.startActivity(intent);
                                SaleThruInventory.saleThru.finish();
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

        TextView BestInvent_SOH, BestInvent_sellThru,
                BestInvent_Sale, bestInvent_zonalSell, bestInvent_thru, bestInvent_wks, BestInvent_option;

        ImageView BestInvent_image_child;


        public ProgressBar ProgressPicaso;
    }


}
