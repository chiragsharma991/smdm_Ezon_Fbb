package apsupportapp.aperotechnologies.com.designapp.SkewedSize;

/**
 * Created by csuthar on 29/11/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleDetailsBean;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.SwitchingTabActivity;
import apsupportapp.aperotechnologies.com.designapp.model.SkewedSizeListDisplay;


/**
 * Created by csuthar on 18/11/16.
 */


public class SkewedSizeAdapter extends BaseAdapter {

    private final Resources resources;
    private ArrayList<SkewedSizeListDisplay> arrayList;

    //private List mStringFilterList;
    //git tese 10/1/2017


    private LayoutInflater mInflater;
    Context context;
    private int Position;
    String TAG="SkewedSizesActivity";
    private TextView mType;
    private List<String> product;
    private Holder holder;
    private List<String> SOH;
    private List<String> items;
    private List<String> setFlag;
    private List<String> setFwc;
    StyleDetailsBean styleDetailsBean;
    ArrayList<StyleDetailsBean> OptionDetailsList;
    Gson gson;
    int offset,limit;

    public SkewedSizeAdapter(ArrayList<SkewedSizeListDisplay> arrayList, Context context, Resources resources) {

        // Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.resources=resources;
        OptionDetailsList = new ArrayList<StyleDetailsBean>();
        gson = new Gson();
        offset = 0;
        limit = 5;
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

        Position = position;

        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.skewedsize_child, null);
            holder.skewed_SOHU = (TextView) convertView.findViewById(R.id.skewed_SOHU);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.skewed_fwc = (TextView) convertView.findViewById(R.id.skewed_fwc);
            // holder.Skewed_ProdAttribute = (TextView) convertView.findViewById(R.id.skewed_ProdAttribute);
            // holder.Skewed_SOH = (TextView) convertView.findViewById(R.id.skewed_SOH);
            holder.skewed_option = (TextView) convertView.findViewById(R.id.skewed_option);
            holder.skewed_image_child = (ImageView) convertView.findViewById(R.id.skewed_image_child);
            holder.toggle_skewed_fav = (ToggleButton) convertView.findViewById(R.id.toggle_skewed_fav);
            holder.SOH = (LinearLayout) convertView.findViewById(R.id.sohDetails);
            holder.ProductAttribute = (LinearLayout) convertView.findViewById(R.id.productInfo);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);


        }
        holder.skewed_option.setText(arrayList.get(position).getOption());

        //Option Click event to get detail information
        holder.skewed_option.setOnClickListener(new View.OnClickListener() {
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
        holder.skewed_SOHU.setText(arrayList.get(position).getStkOnhandQtyTotal());
        int totalSOH=calculation(arrayList.get(position).getStkOnhandQty());
        product=new ArrayList<String>();
        product.clear();
        product = Arrays.asList(arrayList.get(position).getProdAttribute4().split("\\s*,\\s*"));
        setFlag = Arrays.asList(arrayList.get(position).getSkewedFlag().split("\\s*,\\s*"));
        double fwc = Double.parseDouble(arrayList.get(position).getFwdWeekCoverTotal());
        holder.skewed_fwc.setText(String.format("%.1f",fwc));
       /* setFwc = Arrays.asList(arrayList.get(position).getFwdWeekCover().split("\\s*,\\s*"));
        // ArrayList<String>setNewFwc=new ArrayList<String>();
        StringBuilder stringBuild=new StringBuilder();
        for (int i = 0; i <setFwc.size() ; i++) {
            Double x= Double.parseDouble(setFwc.get(i));
            //setNewFwc.add(String.format("%.1f",x));
            stringBuild.append(String.format("%.1f",x));
            stringBuild.append(",");

        }
        stringBuild.deleteCharAt(stringBuild.length()-1);
        holder.skewed_fwc.setText(stringBuild);*/

        createText();
        createSOH();

        // holder.Skewed_ProdAttribute.setText(arrayList.get(position).getProdAttribute4());
        // holder.Skewed_SOH.setText((arrayList.get(position).getStkOnhandQty()));
        Log.e(TAG, "getView: "+calculation(arrayList.get(position).getStkOnhandQty()));


        if(!arrayList.get(position).getProdImageURL().equals("")) {

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
                    .into(holder.skewed_image_child);

        }else {

            holder.ProgressPicaso.setVisibility(View.GONE);

            Glide.with(this.context).
                    load(R.mipmap.placeholder).
                    into(holder.skewed_image_child);


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

        String url ;

        url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?articleOption=" + option.replaceAll(" ", "%20").replaceAll("&", "%26")+"&offset="+offset+"&limit="+limit ;

        Log.e(TAG, "requestStyleDetailsAPI  " + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, " requestStyleDetailsAPI :   " + response.toString());
                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_LONG).show();
                            } else if(response.length() < limit){
                                Reusable_Functions.hDialog();
                                for ( i = 0; i < response.length(); i++) {

                                    styleDetailsBean = gson.fromJson(response.get(i).toString(), StyleDetailsBean.class);
                                    OptionDetailsList.add(styleDetailsBean);

                                }

                                Log.e(TAG, "intent calling: ");
                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom","SkewedActivity");
                                intent.putExtra("articleCode",styleDetailsBean.getArticleCode());
                                intent.putExtra("articleOption",styleDetailsBean.getArticleOption());
                                Log.e("Article Option :",""+styleDetailsBean.getArticleOption());
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                context.startActivity(intent);
                                SkewedSizesActivity.SkewedSizes.finish();
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



    private void createSOH()
    {
        holder.SOH.removeAllViewsInLayout();

        for (int i = 0; i <items.size() ; i++) {

            mType = new TextView(context);

            if ((resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                //Log.e(TAG, "Normal sized screen:" );
                mType.setLayoutParams(new LinearLayout.LayoutParams(120,50));
                mType.setTextSize(12);
            }
            else if ((resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
               // Log.e(TAG, "Small sized screen :" );
                mType.setLayoutParams(new LinearLayout.LayoutParams(70,30));
                mType.setTextSize(12);
            }
            else {
              //  Log.e(TAG, "Screen Size is neither large, normal or small :" );
                mType.setLayoutParams(new LinearLayout.LayoutParams(70,30));
                mType.setTextSize(12);
            }
            if(setFlag.get(i).equals("Y")){
                mType.setBackgroundResource(R.drawable.cell_shape_mark);
                mType.setTextColor(Color.parseColor("#ffffff"));
            }else
            {
                mType.setBackgroundResource(R.drawable.cell_shape);
                mType.setTextColor(Color.parseColor("#757575"));
            }
            mType.setGravity(Gravity.CENTER);
            mType.setText(""+items.get(i));
            holder.SOH.addView(mType);

        }
    }

    private void createText()
    {
        holder.ProductAttribute.removeAllViewsInLayout();

        for (int i = 0; i <product.size() ; i++) {

            mType = new TextView(context);
            if ((resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
               // Log.e(TAG, "Normal sized screen:" );
                mType.setLayoutParams(new LinearLayout.LayoutParams(120,50));
                mType.setTextSize(12);
                //mType.setLayoutParams(new LinearLayout.LayoutParams(90,45));
                //mType.setTextSize(7);

            }
            else if ((resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
              //  Log.e(TAG, "Small sized screen :" );
                mType.setLayoutParams(new LinearLayout.LayoutParams(70,30));
                mType.setTextSize(12);

            }
            else {
               // Log.e(TAG, "Screen Size is neither large, normal or small :" );
                mType.setLayoutParams(new LinearLayout.LayoutParams(70,30));
                mType.setTextSize(12);
            }
            //  mType.setPadding(5, 3, 0, 3);
            if(setFlag.get(i).equals("Y")){
                mType.setBackgroundResource(R.drawable.cell_shape_mark);
                mType.setTextColor(Color.parseColor("#ffffff"));
            }else
            {
                mType.setBackgroundResource(R.drawable.cell_shape);
                mType.setTextColor(Color.parseColor("#404040"));
            }
            mType.setGravity(Gravity.CENTER);
            mType.setText(""+product.get(i));
            mType.setTypeface(null, Typeface.BOLD);

            holder.ProductAttribute.addView(mType);
        }
    }

    private int calculation(String value) {
        items = Arrays.asList(value.split("\\s*,\\s*"));
        int stkOnhandQty []=new int[items.size()];
        for (int i = 0; i <items.size(); i++) {
            stkOnhandQty[i]=Integer.parseInt(items.get(i));
        }
        return sumof(stkOnhandQty);
    }

    private int sumof(int[] stkOnhandQty )
    {
        int sum=0;
        for(int y : stkOnhandQty)
        {
            sum += y;
        }
        return sum;
        // Log.e(TAG, "sumof: "+sum );
    }

    private class Holder {

        TextView skewed_SOHU, skewed_fwc, skewed_option, Skewed_ProdAttribute, Skewed_SOH;
        ImageView skewed_image_child;
        ToggleButton toggle_skewed_fav;
        LinearLayout ProductAttribute ,SOH;
        public ProgressBar ProgressPicaso;
    }
}
