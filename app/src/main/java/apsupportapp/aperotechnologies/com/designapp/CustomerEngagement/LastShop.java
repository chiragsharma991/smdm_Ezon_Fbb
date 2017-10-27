package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 22/06/17.
 */
public class LastShop extends android.support.v4.app.Fragment
{
    private ListView lv_productList;
    private ArrayList<CustomerDetail> customerDetailArrayList;
    private LastShopDataAdapter dataAdapter;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_offer_only,container,false);
        context = root.getContext();
        customerDetailArrayList = new ArrayList<CustomerDetail>();
        customerDetailArrayList = CustomerDetailActivity.customerDetailsarray;

        lv_productList = (ListView)root.findViewById(R.id.lv_productList);
        dataAdapter = new LastShopDataAdapter(context,customerDetailArrayList);
        lv_productList.setAdapter(dataAdapter);
        return  root;
    }
}
