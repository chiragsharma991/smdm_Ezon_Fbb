package apsupportapp.aperotechnologies.com.designapp.ProductInformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.NumberFormat;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleDetailsBean;
import apsupportapp.aperotechnologies.com.designapp.R;


public class Details_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    StyleDetailsBean styleDetailsBean;
    String articleOption;
    TextView txtProductName, txtCollcetion, txtFabric, txtFit, txtFinish, txtSeason, txtfirstReceiteDate, txtlastReceiteDate,
            txtFwdWeekCover, txtTwSalesUnit, txtLwSalesUnit, txtYtdSalesUnit, txtSOH, txtGIT, txtBaseStock, txtPrice, txtsalesThruUnit,
            txtROS, txtBenefit, txtArticleOption,txtStoreDesc, txtStoreCode;
    ImageView imgPromo, imgKeyProduct, imgProfile;
    ProgressBar progressBar;
    public Details_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        styleDetailsBean = (StyleDetailsBean) i.getSerializableExtra("styleDetailsBean");
        Bundle bundle = getActivity().getIntent().getExtras();
        articleOption = bundle.getString("articleOption");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        txtStoreCode =(TextView)view.findViewById(R.id.txtStoreCode);
        txtStoreDesc =(TextView)view.findViewById(R.id.txtStoreName);
        txtArticleOption = (TextView) view.findViewById(R.id.txtArticle);
        txtArticleOption.setText(articleOption);
        txtProductName = (TextView) view.findViewById(R.id.txtProductName);
        txtCollcetion = (TextView) view.findViewById(R.id.txtCollcetion);
        txtFabric = (TextView) view.findViewById(R.id.txtFabric);
        txtFit = (TextView) view.findViewById(R.id.txtFit);
        txtFinish = (TextView) view.findViewById(R.id.txtFinish);
        txtSeason = (TextView) view.findViewById(R.id.txtSeason);

        txtfirstReceiteDate = (TextView) view.findViewById(R.id.txtfirstReceiteDate);
        txtlastReceiteDate = (TextView) view.findViewById(R.id.txtlastReceiteDate);
        txtFwdWeekCover = (TextView) view.findViewById(R.id.txtFwdWeekCover);
        progressBar = (ProgressBar) view.findViewById(R.id.detail_progressPicaso);
        progressBar.setVisibility(View.VISIBLE);
        txtTwSalesUnit = (TextView) view.findViewById(R.id.txtTwSalesUnit);
        txtLwSalesUnit = (TextView) view.findViewById(R.id.txtLwSalesUnit);
        txtYtdSalesUnit = (TextView) view.findViewById(R.id.txtYtdSalesUnit);

        txtSOH = (TextView) view.findViewById(R.id.txtSOH);
        txtGIT = (TextView) view.findViewById(R.id.txtGIT);
        txtBaseStock = (TextView) view.findViewById(R.id.txtBaseStock);

        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtsalesThruUnit = (TextView) view.findViewById(R.id.txtsalesThruUnit);
        txtROS = (TextView) view.findViewById(R.id.txtROS);

        txtBenefit = (TextView) view.findViewById(R.id.txtBenefit);
        imgPromo = (ImageView) view.findViewById(R.id.imgPromo);
        imgKeyProduct = (ImageView) view.findViewById(R.id.imgKeyProduct);
        imgProfile = (ImageView) view.findViewById(R.id.imgProfile);

        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
        if(!styleDetailsBean.getProductImageURL().equals(""))
        {
            Glide.
                    with(getActivity())
                    .load(styleDetailsBean.getProductImageURL())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgProfile);

        }else {
            progressBar.setVisibility(View.GONE);

            Glide.with(getActivity()).
                    load(R.mipmap.placeholder).
                    into(imgProfile);
        }

        if (styleDetailsBean.getPromoFlg().equals("N") || styleDetailsBean.getPromoFlg().equals("")) {
            imgPromo.setImageResource(R.mipmap.option_detail_indicator_red);

        } else if(styleDetailsBean.getPromoFlg().equals("Y")) {
            imgPromo.setImageResource(R.mipmap.option_detail_indicator_green);
        }

        if (styleDetailsBean.getKeyProductFlg().equals("N") || styleDetailsBean.getKeyProductFlg().equals("")) {
            imgKeyProduct.setImageResource(R.mipmap.option_detail_indicator_red);

        } else if (styleDetailsBean.getKeyProductFlg().equals("Y")) {
            imgKeyProduct.setImageResource(R.mipmap.option_detail_indicator_green);

        }
        txtStoreCode.setText(styleDetailsBean.getStoreCode());
        txtStoreDesc.setText(styleDetailsBean.getStoreDesc());
        txtProductName.setText(styleDetailsBean.getProductName());
        txtCollcetion.setText(styleDetailsBean.getCollectionName());
        txtFabric.setText(styleDetailsBean.getProductFabricDesc());
        txtFit.setText(styleDetailsBean.getProductFitDesc());
        txtFinish.setText(styleDetailsBean.getProductFinishDesc());
        txtSeason.setText(styleDetailsBean.getSeasonName());

        if (styleDetailsBean.getFirstReceiptDate().equals("")) {
            txtfirstReceiteDate.setText("NA");
        } else {
            txtfirstReceiteDate.setText(styleDetailsBean.getFirstReceiptDate());
        }

        if (styleDetailsBean.getLastReceiptDate().equals("")) {
            txtlastReceiteDate.setText("NA");
        } else {
            txtlastReceiteDate.setText(styleDetailsBean.getLastReceiptDate());
        }
        txtFwdWeekCover.setText("" + String.format("%.1f", styleDetailsBean.getFwdWeekCover()));
        txtTwSalesUnit.setText("" + styleDetailsBean.getTwSaleTotQty());
        txtLwSalesUnit.setText("" + styleDetailsBean.getLwSaleTotQty());
        txtYtdSalesUnit.setText("" + styleDetailsBean.getYtdSaleTotQty());

        txtSOH.setText("" + styleDetailsBean.getStkOnhandQty());
        txtGIT.setText("" + styleDetailsBean.getStkGitQty());
        txtBaseStock.setText("" + styleDetailsBean.getTargetStock());

        txtPrice.setText("₹" +format.format(Math.round(styleDetailsBean.getUnitGrossPrice())));
        txtsalesThruUnit.setText("" + String.format("%.1f", styleDetailsBean.getSellThruUnitsRcpt()) + "%");
        txtROS.setText("" + String.format("%.1f", styleDetailsBean.getRos()));

        return view;
    }
}
