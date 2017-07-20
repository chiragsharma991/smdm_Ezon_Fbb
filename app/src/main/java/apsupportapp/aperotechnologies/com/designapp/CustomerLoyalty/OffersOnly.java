package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 22/06/17.
 */
public class OffersOnly extends Fragment
{
    private ListView lv_productList;
    private ArrayList<CustomerRecomdtn> customerDetailArrayList;
    private CustomDataAdapter dataAdapter;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_offer_only,container,false);
        context = root.getContext();

        customerDetailArrayList = CustomerDetailActivity.customerDetailArrayList;
        lv_productList = (ListView)root.findViewById(R.id.lv_productList);
        dataAdapter = new CustomDataAdapter(context,customerDetailArrayList);
        lv_productList.setAdapter(dataAdapter);

       return  root;
    }


}
