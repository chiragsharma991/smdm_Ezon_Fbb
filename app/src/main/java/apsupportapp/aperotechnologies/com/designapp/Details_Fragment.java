package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Details_Fragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    StyleDetailsBean styleDetailsBean;
    TextView txtProductName,txtCollcetion,txtFabric,txtFit,txtFinish,txtSeason,txtfirstReceiteDate,txtlastReceiteDate,
            txtFwdWeekCover,txtTwSalesUnit,txtLwSalesUnit,txtYtdSalesUnit,txtSOH,txtGIT,txtBaseStock,txtPrice,txtsalesThruUnit,
            txtROS,txtBenefit;



    public Details_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        styleDetailsBean = (StyleDetailsBean)i.getSerializableExtra("styleDetailsBean");
        Log.e("styleDetailsBean",styleDetailsBean.getStkOnhandQty()+"   "+styleDetailsBean.getStkGitQty());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view=inflater.inflate(R.layout.details_fragment, container, false);
        txtProductName=(TextView)view.findViewById(R.id.txtProductName);
        txtCollcetion=(TextView)view.findViewById(R.id.txtCollcetion);
        txtFabric=(TextView)view.findViewById(R.id.txtFabric);
        txtFit=(TextView)view.findViewById(R.id.txtFit);
        txtFinish=(TextView)view.findViewById(R.id.txtFinish);
        txtSeason=(TextView)view.findViewById(R.id.txtSeason);


        txtfirstReceiteDate=(TextView)view.findViewById(R.id.txtfirstReceiteDate);
        txtlastReceiteDate=(TextView)view.findViewById(R.id.txtlastReceiteDate);
        txtFwdWeekCover=(TextView)view.findViewById(R.id.txtFwdWeekCover);

        txtTwSalesUnit=(TextView)view.findViewById(R.id.txtTwSalesUnit);
        txtLwSalesUnit=(TextView)view.findViewById(R.id.txtLwSalesUnit);
        txtYtdSalesUnit=(TextView)view.findViewById(R.id.txtYtdSalesUnit);

        txtSOH=(TextView)view.findViewById(R.id.txtSOH);
        txtGIT=(TextView)view.findViewById(R.id.txtGIT);
        txtBaseStock=(TextView)view.findViewById(R.id.txtBaseStock);

        txtPrice=(TextView)view.findViewById(R.id.txtPrice);
        txtsalesThruUnit=(TextView)view.findViewById(R.id.txtsalesThruUnit);
        txtROS=(TextView)view.findViewById(R.id.txtROS);

        txtBenefit=(TextView)view.findViewById(R.id.txtBenefit);

        txtProductName.setText(styleDetailsBean.getProductName());
        txtCollcetion.setText(styleDetailsBean.getCollectionName());
        txtFabric.setText(styleDetailsBean.getProductFabricDesc());
        txtFit.setText(styleDetailsBean.getProductFitDesc());
        txtFinish.setText(styleDetailsBean.getProductFinishDesc());
        txtSeason.setText(styleDetailsBean.getSeasonName());

        txtfirstReceiteDate.setText(styleDetailsBean.getFirstReceiptDate());
        txtlastReceiteDate.setText(styleDetailsBean.getLastReceiptDate());
        txtFwdWeekCover.setText(""+styleDetailsBean.getFwdWeekCover());


        txtTwSalesUnit.setText(""+styleDetailsBean.getTwSaleTotQty());
        txtLwSalesUnit.setText(""+styleDetailsBean.getLwSaleTotQty());
        txtYtdSalesUnit.setText(""+styleDetailsBean.getYtdSaleTotQty());

       // if (styleDetailsBean.getStkOnhandQty().equals(null) ||styleDetailsBean.getStkOnhandQty()==null)

        txtSOH.setText(""+styleDetailsBean.getStkOnhandQty());
        txtGIT.setText(""+styleDetailsBean.getStkGitQty());
        txtBaseStock.setText(""+styleDetailsBean.getTargetStock());

        txtPrice.setText(""+styleDetailsBean.getUnitGrossPrice());
        txtsalesThruUnit.setText(""+styleDetailsBean.getSellThruUnitsRcpt());
        txtROS.setText(""+styleDetailsBean.getRos());

        txtBenefit.setText(""+styleDetailsBean.getUsp());
        return  view;
    }


}
