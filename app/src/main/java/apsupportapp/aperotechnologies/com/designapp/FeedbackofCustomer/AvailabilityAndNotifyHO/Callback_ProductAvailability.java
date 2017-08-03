package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;

public class Callback_ProductAvailability extends AppCompatActivity implements HttpResponse {

    private TextView txt_ean_number, txt_store_number, txt_cust_name, txt_mobile_number, txt_brand_name, txt_product_name, txt_size, txt_color;
    private TextView txt_fit, txt_style, txt_callback, txt_remarks, txt_feedback_date, txt_email, txt_sms;
    private Context context;
    private SharedPreferences sharedPreferences;
    private TextView storedesc;
    private String userId,store,bearertoken,geoLeveLDesc;
    private RequestQueue queue;
    private String TAG="CallbackActivity";
    private String attribute14,attribute1,feedbackdate,view_params,feedbackKey;
    private ArrayList<mpm_model> callbacklist;
    private RelativeLayout backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_product_availability);
        getSupportActionBar().hide();
        context=this;
        initialiseUI();
    }

    private void initialiseUI() {

        storedesc = (TextView) findViewById(R.id.txtStoreCode);
        txt_ean_number = (TextView) findViewById(R.id.txt_ean_number);
        txt_store_number = (TextView) findViewById(R.id.txt_store_number);
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        txt_brand_name = (TextView) findViewById(R.id.txt_brand_name);
        txt_product_name = (TextView) findViewById(R.id.txt_product_name);
        txt_size = (TextView) findViewById(R.id.txt_size);
        txt_color = (TextView) findViewById(R.id.txt_color);
        txt_fit = (TextView) findViewById(R.id.txt_fit);
        txt_style = (TextView) findViewById(R.id.txt_style);
        txt_callback = (TextView) findViewById(R.id.txt_callback);
        txt_remarks = (TextView) findViewById(R.id.txt_remarks);
        txt_feedback_date = (TextView) findViewById(R.id.txt_feedback_date);
        txt_email = (TextView) findViewById(R.id.txt_email);
        backButton = (RelativeLayout) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txt_sms = (TextView) findViewById(R.id.txt_sms);
        MainMethod();
        Apicallback(0, false);

    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        //userId = userId.substring(0, userId.length() - 5);    // Hourly works only userid=username;
        store = sharedPreferences.getString("storeDescription", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        storedesc.setText(store);
        Intent intent=getIntent();
        view_params=intent.getStringExtra("view_params");
        attribute14=intent.getStringExtra("attribute14");
        feedbackKey=intent.getStringExtra("feedbackKey");
        attribute1=intent.getStringExtra("attribute1");
        feedbackdate=intent.getStringExtra("arcDate");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }

    private void Apicallback(int id, boolean loader) {
        if (Reusable_Functions.chkStatus(context)) {
            if (loader) {
                Reusable_Functions.sDialog(context, "Loading...");
            }
            mpm_model model = new mpm_model();
            requestcallback(model, id);            //this id is select for url.

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private synchronized void requestcallback(mpm_model model, int id) {

        String url = "";
        ApiRequest api_request;
        switch (id) {
            // case 0 and 1 will follow like first update list and after pie chart
            case 0://
                //https://smdm.manthan.com/v1/display/feedbackdisplaydetail/69-2669?feedbackKey=1&view=LD&attribute14=YES&attribute1=7506556384&feedbackdate=2017-07-01 10:06:55
                url = ConstsCore.web_url + "/v1/display/feedbackdisplaydetail/" + userId + "?feedbackKey="+feedbackKey + "&view=" + view_params +
                        "&recache=true"+"&attribute14="+attribute14+"&attribute1="+attribute1+"&feedbackdate="+feedbackdate.replaceAll(" ", "%20").replaceAll("&", "%26"); //Callback  Api
                api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 1);  // 1 is id for new api response
                break;
            default:
                break;


        }
    }



    @Override
    public void response(ArrayList<mpm_model> list, int id) {
        Log.e(TAG, "response: sucess"+id );
        switch (id) {
            // case 0 and 1 will follow like first api call and set view in case 0;
            case 0:
                callbacklist = new ArrayList<>();
                callbacklist.addAll(list);
                setlist(callbacklist);
               // processbar_view.setVisibility(View.GONE);
                break;
            default:
                break;

        }

    }

    private void setlist(ArrayList<mpm_model> callbacklist) {

        txt_ean_number.setText(callbacklist.get(0).getAttribute1());
        txt_store_number.setText(callbacklist.get(0).getAttribute2());
        txt_cust_name.setText(callbacklist.get(0).getAttribute3());
        txt_mobile_number.setText(callbacklist.get(0).getAttribute4());
        txt_brand_name.setText(callbacklist.get(0).getAttribute5());
        txt_product_name.setText(callbacklist.get(0).getAttribute6());
        txt_size.setText(callbacklist.get(0).getAttribute7());
        txt_color.setText(callbacklist.get(0).getAttribute8());
        txt_fit.setText(callbacklist.get(0).getAttribute9());
        txt_style.setText(callbacklist.get(0).getAttribute10());
        txt_callback.setText(callbacklist.get(0).getAttribute11());
        txt_remarks.setText(callbacklist.get(0).getAttribute12());
        txt_email.setText(callbacklist.get(0).getAttribute13());
        txt_sms.setText(callbacklist.get(0).getAttribute14());


    }

    @Override
    public void nodatafound() {
        Log.e(TAG, "response: null" );

        txt_ean_number.setText("");
        txt_store_number.setText("");
        txt_cust_name.setText("");
        txt_mobile_number.setText("");
        txt_brand_name.setText("");
        txt_product_name.setText("");
        txt_size.setText("");
        txt_color.setText("");
        txt_fit.setText("");
        txt_style.setText("");
        txt_callback.setText("");
        txt_remarks.setText("");
        txt_email.setText("");
        txt_sms.setText("");

    }

    public static void startScreen(Context context, String view_params, String attribute14, String feedbackKey, String attribute1, String arcDate)
    {
        context.startActivity(new Intent(context, Callback_ProductAvailability.class)
        .putExtra("view_params",view_params).putExtra("attribute14",attribute14).putExtra("feedbackKey",feedbackKey).putExtra("attribute1",attribute1).putExtra("arcDate",arcDate)
        );

    }
}
