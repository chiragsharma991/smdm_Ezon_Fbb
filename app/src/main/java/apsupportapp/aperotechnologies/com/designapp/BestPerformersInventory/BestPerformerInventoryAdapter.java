package apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory;

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


public class BestPerformerInventoryAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;
    private LayoutInflater mInflater;
    private Context context;
    private int Position;
    private ArrayList<StyleDetailsBean> optionList;
    private StyleDetailsBean styleDetailsBean;
    private Gson gson;
    private String TAG ;
    private int offset, limit;


    public BestPerformerInventoryAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context, String TAG) {

        this.arrayList = arrayList;
        this.context = context;
        this.TAG = TAG;
        mInflater = LayoutInflater.from(context);
        gson = new Gson();
        optionList = new ArrayList<StyleDetailsBean>();
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
            convertView = mInflater.inflate(R.layout.activity_bestperformer_inventory_child, null);
            holder.BestInvent_SOH = (TextView) convertView.findViewById(R.id.bestInvent_SOH);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.BestInvent_option = (TextView) convertView.findViewById(R.id.bestInvent_option);
            holder.BestInvent_sellThru = (TextView) convertView.findViewById(R.id.bestInvent_sellThru);
            holder.BestInvent_FWC = (TextView) convertView.findViewById(R.id.bestInvent_FWC);
            holder.BestInvent_RosU = (TextView) convertView.findViewById(R.id.bestInvent_RosU);
            holder.BestInvent_GIT = (TextView) convertView.findViewById(R.id.bestInvent_GIT);
            holder.BestInvent_Sale = (TextView) convertView.findViewById(R.id.bestInvent_Sale);
            holder.BestInvent_image_child = (ImageView) convertView.findViewById(R.id.bestInvent_image_child);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
        }

        holder.BestInvent_SOH.setText("" + Math.round(arrayList.get(position).getStkOnhandQty()));
        holder.BestInvent_option.setText(arrayList.get(position).getOption());
        holder.BestInvent_sellThru.setText("" + Math.round(arrayList.get(position).getSellThruUnits()));
        holder.BestInvent_FWC.setText("" + Math.round(arrayList.get(position).getFwdWeekCover()));
        holder.BestInvent_RosU.setText("" + Math.round(arrayList.get(position).getRos()));
        holder.BestInvent_GIT.setText("" + (int) arrayList.get(position).getStkGitQty());
        holder.BestInvent_Sale.setText("" + (int) arrayList.get(position).getSaleTotQty());




        if(TAG.equals("BestPerformer_Ez_Inventory")){

            if (arrayList.get(position).getProdImageUrl()!=null) {

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


                if(arrayList.get(position).getOption().equals("SAMSUNG G615F GXY J7 MAX 4GB/32GB-GOLD"))
                {
                    holder.ProgressPicaso.setVisibility(View.GONE);
                    Glide.with(this.context).
                            load(R.mipmap.samsung_j7_max_original).
                            into(holder.BestInvent_image_child);
                }
                else if(arrayList.get(position).getOption().equals("AMAZON FIRE TVSTICK WITH VOICEREMOTE BLK-OTHERS"))
                {
                    holder.ProgressPicaso.setVisibility(View.GONE);
                    Glide.with(this.context).
                            load(R.mipmap.amazon_fire_tv_stick).
                            into(holder.BestInvent_image_child);
                }
                else if(arrayList.get(position).getArticleDesc().equals("REDBANANA FIBREBRAIDED MINIUSBCABLE MXCL"))
                {
                    holder.ProgressPicaso.setVisibility(View.GONE);
                    Glide.with(this.context).
                            load(R.mipmap.red_banana_cable).
                            into(holder.BestInvent_image_child);
                }
                else
                {
                    holder.ProgressPicaso.setVisibility(View.GONE);
                    Glide.with(this.context).
                            load(R.mipmap.noimageavailable).
                            into(holder.BestInvent_image_child);

                }




        }
        }
        else{


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
                        load(R.mipmap.noimageavailable).
                        into(holder.BestInvent_image_child);


            }


            //Option Click event to get detail information
//            holder.BestInvent_option.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (Reusable_Functions.chkStatus(context)) {
//                        Reusable_Functions.hDialog();
//                        Reusable_Functions.sDialog(context, "Loading  data...");
//
//                        requestOptionDetailsAPI(arrayList.get(position).getOption());
//                    } else {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
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
        String url = " ";
        url = ConstsCore.web_url + "/v1/display/productdetailsNew/" + userId + "?articleOption=" + option.replaceAll(" ", "%20").replaceAll("&", "%26") +"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() < limit) {
                                Reusable_Functions.hDialog();
                                for (i = 0; i < response.length(); i++) {

                                    styleDetailsBean = gson.fromJson(response.get(i).toString(), StyleDetailsBean.class);
                                    optionList.add(styleDetailsBean);

                                }


                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom", "bestInventory");
                                intent.putExtra("articleCode", styleDetailsBean.getArticleCode());
                                intent.putExtra("articleOption", styleDetailsBean.getArticleOption());

                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                context.startActivity(intent);
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

        TextView BestInvent_SOH, BestInvent_option, BestInvent_sellThru, BestInvent_FWC,
                BestInvent_RosU, BestInvent_GIT, BestInvent_Sale;
        ImageView BestInvent_image_child;
        ProgressBar ProgressPicaso;


    }


}
