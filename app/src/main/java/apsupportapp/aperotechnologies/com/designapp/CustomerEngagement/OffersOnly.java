package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

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
