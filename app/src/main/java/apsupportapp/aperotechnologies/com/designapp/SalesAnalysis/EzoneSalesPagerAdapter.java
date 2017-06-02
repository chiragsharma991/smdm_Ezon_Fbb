package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;


public class EzoneSalesPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    Context context;

    ArrayList<SalesAnalysisViewPagerValue> ez_sales_pager_array;
    int focusposition;
    SalesAnalysisViewPagerValue ez_sales_pager;
    ViewPager ez_vwpagersales;
    LinearLayout ez_linear_dots;
    EzoneSalesAdapter ez_salesadapter;
    RecyclerView recycle_ez_sales;
    LayoutInflater inflater;
    public static int ez_currentPage;
    EzoneSalesPagerAdapter ez_pager_adapter;


    // ViewPager 0
    ImageView txt_ez_NetSalesImage, txt_ez_PlanSalesImage, txt_ez_NetSalesUImage;
    TextView txt_ez_NetSalesVal, txt_ez_NetSales, txt_ez_NetSalesPerc, txt_ez_NetSalesName;
    TextView txt_ez_PlanSalesVal, txt_ez_PlanSales, txt_ez_PlanSalesPerc, txt_ez_PlanSalesName;
    TextView txt_ez_NetSalesUVal, txt_ez_NetSalesU, txt_ez_NetSalesUPerc, txt_ez_NetSalesUName;
    TextView txt_ez_SohUVal, txt_ez_SohU,txt_ez_MarginName,txt_ez_MarginPerc;

    //viewPager 1
    TextView txt_ez_StoreVal_PvASales1, txt_ez_ZonalSalesVal, txt_ez_NationalSalesVal;
    TextView txt_ez_PvASales, txt_ez_YoYSales, txt_ez_SellThru, txt_ez_MixSales, txt_ez_Rank;
    TextView txt_ez_PvAZonalRankVal, txt_ez_PvANationalRankVal, txt_ez_YoYZonalRankVal, txt_ez_YoYNationalRankVal;
    RelativeLayout ez_storesales, ez_zonalsales, ez_nationalsales, ez_sznchildRelLayout, ez_rankRelLayout, pmsyrtxtRelLayout;
    LinearLayout lin_ez_PvAZonalRank, lin_ez_PvANationalRank, lin_ez_YoYZonalRank, lin_ez_YoYNationalRank;

    // ViewPager 2
    TextView txt_ez_SOHVal2, txt_ez_SOH2;
    TextView txt_ez_GITVal, txt_ez_GIT;
    TextView txt_ez_ROSVal2, txt_ez_ROS2;
    TextView txt_ez_FwdWkCoverVal2, txt_ez_FwdWkCover2;

    ArrayList<SalesAnalysisListDisplay> ez_sales_detail_array;
    String ez_fromWhere;

    public EzoneSalesPagerAdapter(Context context, ArrayList<SalesAnalysisViewPagerValue> arrayList, int focusposition, ViewPager vwpagersales, LinearLayout lldots, EzoneSalesAdapter salesadapter, RecyclerView listView_SalesAnalysis, ArrayList<SalesAnalysisListDisplay> ez_sales_pagerClassArrayList, String fromWhere, EzoneSalesPagerAdapter pagerAdapter) {

        this.context = context;
        this.ez_sales_pager_array = arrayList;
        this.focusposition = focusposition;
        this.ez_vwpagersales = vwpagersales;
        this.ez_linear_dots = lldots;
        this.ez_sales_detail_array = ez_sales_pagerClassArrayList;
        this.ez_fromWhere = fromWhere;
        this.ez_pager_adapter = pagerAdapter;
        ez_salesadapter = salesadapter;
        this.recycle_ez_sales = listView_SalesAnalysis;
        if (arrayList.size() != 0) {
            ez_sales_pager = arrayList.get(0);
        }
        ez_currentPage = vwpagersales.getCurrentItem();
    }

    public EzoneSalesPagerAdapter() {

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        // Declare Variables
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = null;


        if (position == 0) {
            itemView = inflater.inflate(R.layout.activity_ez_viewpager1, container,
                    false);

            txt_ez_NetSalesImage = (ImageView) itemView.findViewById(R.id.txt_ez_NetSalesImage);
            txt_ez_PlanSalesImage = (ImageView) itemView.findViewById(R.id.txt_ez_PlanSalesImage);
            txt_ez_NetSalesUImage = (ImageView) itemView.findViewById(R.id.txt_ez_NetSalesUImage);

            txt_ez_NetSalesVal = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesVal);
            txt_ez_NetSales = (TextView) itemView.findViewById(R.id.txt_ez_NetSales);
            txt_ez_NetSalesPerc = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesPerc);
            txt_ez_PlanSalesVal = (TextView) itemView.findViewById(R.id.txt_ez_PlanSalesVal);
            txt_ez_PlanSales = (TextView) itemView.findViewById(R.id.txt_ez_PlanSales);
            txt_ez_PlanSalesPerc = (TextView) itemView.findViewById(R.id.txtPlanSalesPerc);
            txt_ez_NetSalesUVal = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesUVal);
            txt_ez_NetSalesU = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesU);
            txt_ez_NetSalesUPerc = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesUPerc);
            txt_ez_SohUVal = (TextView) itemView.findViewById(R.id.txt_ez_SoHUVal);
            txt_ez_SohU = (TextView) itemView.findViewById(R.id.txt_ez_SoHU);

            txt_ez_NetSalesPerc = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesPerc);
            txt_ez_PlanSalesPerc = (TextView) itemView.findViewById(R.id.txt_ez_PlanSalesPerc);
            txt_ez_NetSalesUPerc = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesUPerc);
            txt_ez_NetSalesName = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesName);
            txt_ez_PlanSalesName = (TextView) itemView.findViewById(R.id.txt_ez_PlanSalesName);
            txt_ez_NetSalesUName = (TextView) itemView.findViewById(R.id.txt_ez_NetSalesUName);
            txt_ez_MarginName = (TextView)itemView.findViewById(R.id.txt_ez_MarginName);
            txt_ez_MarginPerc =(TextView)itemView.findViewById(R.id.txt_ez_MarginPerc);

            txt_ez_NetSales.setText("Net Sales");
            txt_ez_PlanSales.setText("Plan Sales");
            txt_ez_NetSalesU.setText("Net Sales(U)");
            txt_ez_SohU.setText("Margin");
//            if (SalesAnalysisActivity1.ez_segment_val.equals("LD") || SalesAnalysisActivity1.ez_segment_val.equals("WTD")) {


                txt_ez_NetSalesName.setText("WOW Gr%");
                txt_ez_PlanSalesName.setText("PvA%");
                txt_ez_NetSalesUName.setText("WOW Gr%");
                txt_ez_MarginName.setText("Margin%");


//            } else if (SalesAnalysisActivity1.ez_segment_val.equals("MTD") || SalesAnalysisActivity1.ez_segment_val.equals("YTD")) {


//                txt_ez_NetSalesName.setText("YOY Gr%");
//                txt_ez_PlanSalesName.setText("PvA%");
//                txt_ez_NetSalesUName.setText("YOY Gr%");

//            }

        }
//      else if (position == 1) {
//            itemView = inflater.inflate(R.layout.activity_ez_viewpager2, container,
//                    false);
//            txt_ez_StoreVal_PvASales1 = (TextView) itemView.findViewById(R.id.txt_ez_StoreVal_PvASales1);
//            txt_ez_ZonalSalesVal = (TextView) itemView.findViewById(R.id.txt_ez_ZonalSalesVal);
//            txt_ez_NationalSalesVal = (TextView) itemView.findViewById(R.id.txt_ez_NationalSalesVal);
//            txt_ez_PvASales = (TextView) itemView.findViewById(R.id.txt_ez_PvA);
//            txt_ez_YoYSales = (TextView) itemView.findViewById(R.id.txt_ez_YoY);
//            txt_ez_SellThru = (TextView) itemView.findViewById(R.id.txt_ez_SellThru);
//            txt_ez_MixSales = (TextView) itemView.findViewById(R.id.txt_ez_Mixsales);
//            txt_ez_Rank = (TextView) itemView.findViewById(R.id.txt_ez_rank);
//            //Rank Layout
//            txt_ez_PvAZonalRankVal = (TextView) itemView.findViewById(R.id.txt_ez_PvAZonalRankVal);
//            txt_ez_PvANationalRankVal = (TextView) itemView.findViewById(R.id.txt_ez_PvANationalRankVal);
//            txt_ez_YoYZonalRankVal = (TextView) itemView.findViewById(R.id.txt_ez_YoYZonalRankVal);
//            txt_ez_YoYNationalRankVal = (TextView) itemView.findViewById(R.id.txt_ez_YoYNationalRankVal);
//            ez_storesales = (RelativeLayout) itemView.findViewById(R.id.rel_ez_storesales);
//            ez_zonalsales = (RelativeLayout) itemView.findViewById(R.id.rel_ez_zonalsales);
//            ez_nationalsales = (RelativeLayout) itemView.findViewById(R.id.rel_ez_nationalsales);
//            ez_sznchildRelLayout = (RelativeLayout) itemView.findViewById(R.id.sznchildRelLayout);
//            ez_sznchildRelLayout.setVisibility(View.VISIBLE);
//            ez_rankRelLayout = (RelativeLayout) itemView.findViewById(R.id.rankRelLayout);
//            ez_rankRelLayout.setVisibility(View.GONE);
//            pmsyrtxtRelLayout = (RelativeLayout) itemView.findViewById(R.id.pmsyrtxtRelLayout);
//            lin_ez_PvAZonalRank = (LinearLayout) itemView.findViewById(R.id.lin_ez_PvAZonalRank);
//            lin_ez_PvANationalRank = (LinearLayout) itemView.findViewById(R.id.lin_ez_PvANationalRank);
//            lin_ez_YoYZonalRank = (LinearLayout) itemView.findViewById(R.id.lin_ez_YoYZonalRank);
//            lin_ez_YoYNationalRank = (LinearLayout) itemView.findViewById(R.id.lin_ez_YoYNationalRank);
//
//        }
        else if (position == 1) {
            itemView = inflater.inflate(R.layout.activity_ez_viewpager3, container,
                    false);

            txt_ez_SOHVal2 = (TextView) itemView.findViewById(R.id.txt_ez_SOHUVal2);
            txt_ez_SOH2 = (TextView) itemView.findViewById(R.id.txt_ez_SOH2);
            txt_ez_GITVal = (TextView) itemView.findViewById(R.id.txt_ez_GITVal);
            txt_ez_GIT = (TextView) itemView.findViewById(R.id.txt_ez_GIT);
            txt_ez_ROSVal2 = (TextView) itemView.findViewById(R.id.txt_ez_ROSVal2);
            txt_ez_ROS2 = (TextView) itemView.findViewById(R.id.txt_ez_ROS2);
            txt_ez_FwdWkCoverVal2 = (TextView) itemView.findViewById(R.id.txt_ez_FwdWkCoverVal2);
            txt_ez_FwdWkCover2 = (TextView) itemView.findViewById(R.id.txt_ez_FwdWkCover2);

        }


        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));

        if (position == 0) {
            if (SalesAnalysisActivity1.ez_segment_val.equals("LD") || SalesAnalysisActivity1.ez_segment_val.equals("WTD")) {

                if (ez_sales_pager != null) {
                    txt_ez_NetSalesVal.setText("\u20B9\t" + format.format(Math.round(ez_sales_pager.getSaleNetVal())));
                    txt_ez_PlanSalesVal.setText("\u20B9\t" + format.format(Math.round(ez_sales_pager.getPlanSaleNetVal())));
                    txt_ez_NetSalesUVal.setText("" + format.format(Math.round(ez_sales_pager.getSaleTotQty())));
                    txt_ez_SohUVal.setText("" + format.format(Math.round(ez_sales_pager.getStkOnhandQty())));
                    txt_ez_NetSalesPerc.setText("" + Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) + "%");
                    txt_ez_PlanSalesPerc.setText("" + Math.round(ez_sales_pager.getPvaAchieved()) + "%");
                    txt_ez_NetSalesUPerc.setText("" + Math.round(ez_sales_pager.getYoyNetSalesUnitsGrowthPct()) + "%");
                    txt_ez_MarginPerc.setText("" + Math.round(ez_sales_pager.getMarginPct()) + "%");

                    // Color Condition for Wow Net Sale, Pva Achieved , Wow net sale Growth
                    if (ez_sales_pager.getWowNetSalesGrowthPct() <= 0) {
                        txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                        txt_ez_NetSalesVal.setTextColor(Color.parseColor("#fe0000"));
                    } else if (ez_sales_pager.getWowNetSalesGrowthPct() > 0) {
                        txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txt_ez_NetSalesVal.setTextColor(Color.parseColor("#70e503"));
                    }
                    if (ez_sales_pager.getPvaAchieved() < 70) {
                        txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                        txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#fe0000"));
                    } else if (ez_sales_pager.getPvaAchieved() > 90) {
                        txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#70e503"));
                    } else {
                        txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                        txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#ff7e00"));
                    }
                    if (ez_sales_pager.getWowNetSalesUnitsGrowthPct() <= 0) {
                        txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                        txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#fe0000"));
                    } else if (ez_sales_pager.getWowNetSalesUnitsGrowthPct() > 0) {
                        txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                        txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#70e503"));
                    }
                }


            } else if (SalesAnalysisActivity1.ez_segment_val.equals("MTD") || SalesAnalysisActivity1.ez_segment_val.equals("YTD")) {

                if (ez_sales_pager != null) {

                    txt_ez_NetSalesVal.setText("\u20B9\t" + format.format(Math.round(ez_sales_pager.getSaleNetVal())));
                    txt_ez_PlanSalesVal.setText("\u20B9\t" + format.format(Math.round(ez_sales_pager.getPlanSaleNetVal())));
                    txt_ez_NetSalesUVal.setText(" " + format.format(Math.round(ez_sales_pager.getSaleTotQty())));
                    txt_ez_SohUVal.setText(" " + format.format(Math.round(ez_sales_pager.getStkOnhandQty())));

                    txt_ez_NetSalesPerc.setText("" + Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) + "%");
                    txt_ez_PlanSalesPerc.setText("" + Math.round(ez_sales_pager.getPvaAchieved()) + "%");
                    txt_ez_NetSalesUPerc.setText("" + Math.round(ez_sales_pager.getYoyNetSalesUnitsGrowthPct()) + "%");
                    txt_ez_MarginPerc.setText("" + Math.round(ez_sales_pager.getMarginPct()) + "%");

                }
                if (ez_sales_pager.getWowNetSalesGrowthPct() <= 0) {
                    txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    txt_ez_NetSalesVal.setTextColor(Color.parseColor("#fe0000"));

                } else if (ez_sales_pager.getWowNetSalesGrowthPct() > 0) {
                    txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    txt_ez_NetSalesVal.setTextColor(Color.parseColor("#70e503"));
                }

                if (ez_sales_pager.getPvaAchieved() < 70) {
                    txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#fe0000"));

                } else if (ez_sales_pager.getPvaAchieved() > 90) {
                    txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#70e503"));

                } else {
                    txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                    txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#ff7e00"));
                }

                if (ez_sales_pager.getWowNetSalesUnitsGrowthPct() <= 0) {
                    txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                    txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#fe0000"));
                } else if (ez_sales_pager.getWowNetSalesUnitsGrowthPct() > 0) {
                    txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                    txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#70e503"));
                }

            }
            if (ez_sales_detail_array.size() != 0) {
                SalesAnalysisListDisplay ez_sales_pagerListDisplay = ez_sales_detail_array.get(position);

                if (ez_sales_pagerListDisplay.getLevel() != null) {
                    if (ez_sales_pagerListDisplay.getLevel().equals("All")) {
                        ez_sales_pagerListDisplay.setPvaAchieved(ez_sales_pager.getPvaAchieved());
                        ez_sales_detail_array.set(0, ez_sales_pagerListDisplay);
                        ez_pager_adapter.notifyDataSetChanged();
                    }
                }
                if (ez_sales_pagerListDisplay.getLevel() != null) {
                    if (ez_sales_pagerListDisplay.getLevel().equals("All")) {
                        ez_sales_pagerListDisplay.setPvaAchieved(ez_sales_pager.getPvaAchieved());
                        ez_sales_detail_array.set(0, ez_sales_pagerListDisplay);
                        ez_pager_adapter.notifyDataSetChanged();
                    }
                }

                if (ez_sales_pagerListDisplay.getLevel() != null) {
                    if (ez_sales_pagerListDisplay.getLevel().equals("All")) {
                        {
                            ez_sales_pagerListDisplay.setPvaAchieved(ez_sales_pager.getPvaAchieved());
                            ez_sales_detail_array.set(0, ez_sales_pagerListDisplay);
                            ez_pager_adapter.notifyDataSetChanged();
                        }
                    }
                }
                if (ez_sales_pagerListDisplay.getLevel() != null) {
                    if (ez_sales_pagerListDisplay.getLevel().equals("All")) {
                        ez_sales_pagerListDisplay.setPvaAchieved(ez_sales_pager.getPvaAchieved());
                        ez_sales_detail_array.set(0, ez_sales_pagerListDisplay);
                        ez_pager_adapter.notifyDataSetChanged();
                    }
                }
                if (ez_sales_pagerListDisplay.getLevel() != null) {
                    if (ez_sales_pagerListDisplay.getLevel().equals("All")) {
                        ez_sales_pagerListDisplay.setPvaAchieved(ez_sales_pager.getPvaAchieved());
                        ez_sales_detail_array.set(0, ez_sales_pagerListDisplay);
                        ez_pager_adapter.notifyDataSetChanged();
                    }
                }
            }

        }
//        else if (position == 1)
//        {
//
//            if (ez_sales_pager != null) {
//
//                ez_sznchildRelLayout.setVisibility(View.VISIBLE);
//                ez_rankRelLayout.setVisibility(View.GONE);
//
//                txt_ez_StoreVal_PvASales1.setText("" + Math.round(ez_sales_pager.getPvaAchieved()) + "%");
//                txt_ez_ZonalSalesVal.setText("" + Math.round(ez_sales_pager.getPvaAchievedZonal()) + "%");
//                txt_ez_NationalSalesVal.setText("" + Math.round(ez_sales_pager.getPvaAchievedNational()) + "%");
//
//                txt_ez_PvASales.setBackgroundResource(R.drawable.rounded_edittext2);
//                if (Math.round(ez_sales_pager.getPvaAchieved()) > Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchieved()) > Math.round(ez_sales_pager.getPvaAchievedNational())) {
//
//                    ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                    ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                    ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//
//                } else if (Math.round(ez_sales_pager.getPvaAchievedZonal()) > Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedZonal()) > Math.round(ez_sales_pager.getPvaAchievedNational())) {
//
//                    ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                    ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                    ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                } else if (Math.round(ez_sales_pager.getPvaAchievedNational()) > Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedNational()) > Math.round(ez_sales_pager.getPvaAchievedZonal())) {
//
//                    ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                    ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                    ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                } else if (Math.round(ez_sales_pager.getPvaAchieved()) == Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchieved()) == Math.round(ez_sales_pager.getPvaAchievedNational())
//                        && Math.round(ez_sales_pager.getPvaAchievedZonal()) == Math.round(ez_sales_pager.getPvaAchievedNational()) && Math.round(ez_sales_pager.getPvaAchievedZonal()) == Math.round(ez_sales_pager.getPvaAchieved())
//                        && Math.round(ez_sales_pager.getPvaAchievedNational()) == Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedNational()) == Math.round(ez_sales_pager.getPvaAchievedZonal())) {
//
//                    ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                    ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                    ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                } else if (Math.round(ez_sales_pager.getPvaAchievedZonal()) == Math.round(ez_sales_pager.getPvaAchievedNational()) && Math.round(ez_sales_pager.getPvaAchieved()) < Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchieved()) < Math.round(ez_sales_pager.getPvaAchievedNational())) {
//
//                    ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                    ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                    ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                }
//
//
//                //PvA Sales
//                txt_ez_PvASales.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        pmsyrtxtRelLayout.setTag("PvA%");
//                        ez_sznchildRelLayout.setVisibility(View.VISIBLE);
//                        ez_rankRelLayout.setVisibility(View.GONE);
//
//                        txt_ez_PvASales.setBackgroundResource(R.drawable.rounded_edittext2);
//                        txt_ez_YoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_SellThru.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_MixSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_Rank.setBackgroundResource(R.drawable.rounded_edittext3);
//
//                        txt_ez_StoreVal_PvASales1.setText("" + Math.round(ez_sales_pager.getPvaAchieved()) + "%");
//                        txt_ez_ZonalSalesVal.setText("" + Math.round(ez_sales_pager.getPvaAchievedZonal()) + "%");
//                        txt_ez_NationalSalesVal.setText("" + Math.round(ez_sales_pager.getPvaAchievedNational()) + "%");
//
//                        if (Math.round(ez_sales_pager.getPvaAchieved()) > Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchieved()) > Math.round(ez_sales_pager.getPvaAchievedNational())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//
//                        } else if (Math.round(ez_sales_pager.getPvaAchievedZonal()) > Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedZonal()) > Math.round(ez_sales_pager.getPvaAchievedNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getPvaAchievedNational()) > Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedNational()) > Math.round(ez_sales_pager.getPvaAchievedZonal())) {
//
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getPvaAchieved()) == Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchieved()) == Math.round(ez_sales_pager.getPvaAchievedNational())
//                                && Math.round(ez_sales_pager.getPvaAchievedZonal()) == Math.round(ez_sales_pager.getPvaAchievedNational()) && Math.round(ez_sales_pager.getPvaAchievedZonal()) == Math.round(ez_sales_pager.getPvaAchieved())
//                                && Math.round(ez_sales_pager.getPvaAchievedNational()) == Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedNational()) == Math.round(ez_sales_pager.getPvaAchievedZonal())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getPvaAchievedZonal()) == Math.round(ez_sales_pager.getPvaAchievedNational()) && Math.round(ez_sales_pager.getPvaAchieved()) < Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchieved()) < Math.round(ez_sales_pager.getPvaAchievedNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getPvaAchieved()) == Math.round(ez_sales_pager.getPvaAchievedZonal()) && Math.round(ez_sales_pager.getPvaAchievedNational()) < Math.round(ez_sales_pager.getPvaAchieved()) && Math.round(ez_sales_pager.getPvaAchievedNational()) < Math.round(ez_sales_pager.getPvaAchievedZonal())) {
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                        }
//                    }
//                });
//                //YoY Sales
//                txt_ez_YoYSales.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ez_sznchildRelLayout.setVisibility(View.VISIBLE);
//                        ez_rankRelLayout.setVisibility(View.GONE);
//
//                        txt_ez_PvASales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_MixSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_SellThru.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_YoYSales.setBackgroundResource(R.drawable.rounded_edittext2);
//                        txt_ez_Rank.setBackgroundResource(R.drawable.rounded_edittext3);
//
//                        txt_ez_StoreVal_PvASales1.setText("" + Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) + "%");
//                        txt_ez_ZonalSalesVal.setText("" + Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) + "%");
//                        txt_ez_NationalSalesVal.setText("" + Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) + "%");
//
//                        //YOY Sales
//                        if (Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) > Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) > Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) > Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) > Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) > Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) > Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal())) {
//
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational())
//                                && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPct())
//                                && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) < Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) < Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) == Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) < Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) && Math.round(ez_sales_pager.getYoyNetSalesGrowthPctNational()) < Math.round(ez_sales_pager.getYoyNetSalesGrowthPctZonal())) {
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                        }
//
//
//                    }
//                });
//
//                //Sell Thru
//                txt_ez_SellThru.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ez_sznchildRelLayout.setVisibility(View.VISIBLE);
//                        ez_rankRelLayout.setVisibility(View.GONE);
//
//                        txt_ez_PvASales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_MixSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_SellThru.setBackgroundResource(R.drawable.rounded_edittext2);
//                        txt_ez_YoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_Rank.setBackgroundResource(R.drawable.rounded_edittext3);
//
//                        txt_ez_StoreVal_PvASales1.setText("" + Math.round(ez_sales_pager.getSellThruUnits()) + "%");
//                        txt_ez_ZonalSalesVal.setText("" + Math.round(ez_sales_pager.getSellThruUnitsZonal()) + "%");
//                        txt_ez_NationalSalesVal.setText("" + Math.round(ez_sales_pager.getSellThruUnitsNational()) + "%");
//
//
//                        //Sell Thro
//
//                        if (Math.round(ez_sales_pager.getSellThruUnits()) > Math.round(ez_sales_pager.getSellThruUnitsZonal()) && Math.round(ez_sales_pager.getSellThruUnits()) > Math.round(ez_sales_pager.getSellThruUnitsNational())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getSellThruUnitsZonal()) > Math.round(ez_sales_pager.getSellThruUnits()) && Math.round(ez_sales_pager.getSellThruUnitsZonal()) > Math.round(ez_sales_pager.getSellThruUnitsNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getSellThruUnitsNational()) > Math.round(ez_sales_pager.getSellThruUnits()) && Math.round(ez_sales_pager.getSellThruUnitsNational()) > Math.round(ez_sales_pager.getSellThruUnitsZonal())) {
//
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getSellThruUnits()) == Math.round(ez_sales_pager.getSellThruUnitsNational()) && Math.round(ez_sales_pager.getSellThruUnits()) == Math.round(ez_sales_pager.getSellThruUnitsZonal())
//                                && Math.round(ez_sales_pager.getSellThruUnitsZonal()) == Math.round(ez_sales_pager.getSellThruUnits()) && Math.round(ez_sales_pager.getSellThruUnitsZonal()) == Math.round(ez_sales_pager.getSellThruUnitsNational())
//                                && Math.round(ez_sales_pager.getSellThruUnitsNational()) == Math.round(ez_sales_pager.getSellThruUnits()) && Math.round(ez_sales_pager.getSellThruUnitsNational()) == Math.round(ez_sales_pager.getSellThruUnitsZonal())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getSellThruUnitsZonal()) == Math.round(ez_sales_pager.getSellThruUnitsNational()) && Math.round(ez_sales_pager.getSellThruUnits()) < Math.round(ez_sales_pager.getSellThruUnitsZonal()) && Math.round(ez_sales_pager.getSellThruUnits()) < Math.round(ez_sales_pager.getSellThruUnitsNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getSellThruUnits()) == Math.round(ez_sales_pager.getSellThruUnitsZonal()) && Math.round(ez_sales_pager.getSellThruUnitsNational()) < Math.round(ez_sales_pager.getSellThruUnits()) && Math.round(ez_sales_pager.getSellThruUnitsNational()) < Math.round(ez_sales_pager.getSellThruUnitsZonal())) {
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                        }
//
//                    }
//                });
//
//                //Mix Sales
//                txt_ez_MixSales.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ez_sznchildRelLayout.setVisibility(View.VISIBLE);
//                        ez_rankRelLayout.setVisibility(View.GONE);
//
//                        txt_ez_PvASales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_MixSales.setBackgroundResource(R.drawable.rounded_edittext2);
//                        txt_ez_SellThru.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_YoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_Rank.setBackgroundResource(R.drawable.rounded_edittext3);
//
//                        txt_ez_StoreVal_PvASales1.setText("" + Math.round(ez_sales_pager.getMixSales()) + "%");
//                        txt_ez_ZonalSalesVal.setText("" + Math.round(ez_sales_pager.getMixSalesZonal()) + "%");
//                        txt_ez_NationalSalesVal.setText("" + Math.round(ez_sales_pager.getMixsalesNational()) + "%");
//
//                        //Mix Sales
//                        if (Math.round(ez_sales_pager.getMixSales()) > Math.round(ez_sales_pager.getMixSalesZonal()) && Math.round(ez_sales_pager.getMixSales()) > Math.round(ez_sales_pager.getMixsalesNational())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getMixSalesZonal()) > Math.round(ez_sales_pager.getMixSales()) && Math.round(ez_sales_pager.getMixSalesZonal()) > Math.round(ez_sales_pager.getMixsalesNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//
//                        } else if (Math.round(ez_sales_pager.getMixsalesNational()) > Math.round(ez_sales_pager.getMixSales()) && Math.round(ez_sales_pager.getMixsalesNational()) > Math.round(ez_sales_pager.getMixSalesZonal())) {
//
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//                        } else if (Math.round(ez_sales_pager.getMixSales()) == Math.round(ez_sales_pager.getMixSalesZonal()) && Math.round(ez_sales_pager.getMixSales()) == Math.round(ez_sales_pager.getMixsalesNational())
//                                && Math.round(ez_sales_pager.getMixSalesZonal()) == Math.round(ez_sales_pager.getMixsalesNational()) && Math.round(ez_sales_pager.getMixSalesZonal()) == Math.round(ez_sales_pager.getMixSales())
//                                && Math.round(ez_sales_pager.getMixsalesNational()) == Math.round(ez_sales_pager.getMixSales()) && Math.round(ez_sales_pager.getMixsalesNational()) == Math.round(ez_sales_pager.getMixSalesZonal())) {
//
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//
//
//                        } else if (Math.round(ez_sales_pager.getMixSalesZonal()) == Math.round(ez_sales_pager.getMixsalesNational()) && Math.round(ez_sales_pager.getMixSales()) < Math.round(ez_sales_pager.getMixSalesZonal()) && Math.round(ez_sales_pager.getMixSales()) < Math.round(ez_sales_pager.getMixsalesNational())) {
//
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                        } else if (Math.round(ez_sales_pager.getMixSales()) == Math.round(ez_sales_pager.getMixSalesZonal()) && Math.round(ez_sales_pager.getMixsalesNational()) < Math.round(ez_sales_pager.getMixSales()) && Math.round(ez_sales_pager.getMixsalesNational()) < Math.round(ez_sales_pager.getMixSalesZonal())) {
//                            ez_storesales.setBackgroundResource(R.drawable.rounded_edittext1);
//                            ez_zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                            ez_nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
//                        }
//
//                    }
//                });
//
//                //Rank
//                txt_ez_Rank.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ez_rankRelLayout.setVisibility(View.VISIBLE);
//                        ez_sznchildRelLayout.setVisibility(View.GONE);
//
//                        txt_ez_PvASales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_MixSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_SellThru.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_YoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
//                        txt_ez_Rank.setBackgroundResource(R.drawable.rounded_edittext2);
//
//
//                        txt_ez_PvAZonalRankVal.setText("" + ez_sales_pager.getZonalSalesRank());
//                        txt_ez_PvANationalRankVal.setText("" + ez_sales_pager.getNationalSalesRank());
//                        txt_ez_YoYZonalRankVal.setText("" + ez_sales_pager.getZonalYOYGrowthRank());
//                        txt_ez_YoYNationalRankVal.setText("" + ez_sales_pager.getNationalYOYGrowthRank());
//
//                        //Sales Rank Condtion
//                        if (ez_sales_pager.getZonalSalesRank() > ez_sales_pager.getNationalSalesRank())
//
//                        {
//                            lin_ez_PvAZonalRank.setBackgroundResource(R.color.smdm_green);
//                            lin_ez_PvANationalRank.setBackgroundResource(R.color.smdm_amber);
//                        } else if (ez_sales_pager.getNationalSalesRank() > ez_sales_pager.getZonalSalesRank())
//                        {
//                            lin_ez_PvAZonalRank.setBackgroundResource(R.color.smdm_amber);
//                            lin_ez_PvANationalRank.setBackgroundResource(R.color.smdm_green);
//
//                        } else if (ez_sales_pager.getZonalSalesRank() == ez_sales_pager.getNationalSalesRank())
//                        {
//                            lin_ez_PvAZonalRank.setBackgroundResource(R.color.smdm_green);
//                            lin_ez_PvANationalRank.setBackgroundResource(R.color.smdm_amber);
//                        }
//                        //YoY Rank Condition
//                        if (ez_sales_pager.getZonalYOYGrowthRank() > ez_sales_pager.getNationalYOYGrowthRank())
//                        {
//                            lin_ez_YoYZonalRank.setBackgroundResource(R.color.smdm_green);
//                            lin_ez_YoYNationalRank.setBackgroundResource(R.color.smdm_amber);
//
//                        } else if (ez_sales_pager.getNationalYOYGrowthRank() > ez_sales_pager.getZonalYOYGrowthRank())
//                        {
//                            lin_ez_YoYZonalRank.setBackgroundResource(R.color.smdm_amber);
//                            lin_ez_YoYNationalRank.setBackgroundResource(R.color.smdm_green);
//
//                        } else if (ez_sales_pager.getZonalYOYGrowthRank() == ez_sales_pager.getNationalYOYGrowthRank())
//                        {
//                            lin_ez_YoYZonalRank.setBackgroundResource(R.color.smdm_green);
//                            lin_ez_YoYNationalRank.setBackgroundResource(R.color.smdm_amber);
//
//                        }
//                    }
//                });
//
//            }
//
//
//        }
        else if (position == 1) {
            if (ez_sales_pager != null) {
                double ros = Double.parseDouble(String.format("%.1f", ez_sales_pager.getRos()));
                double fwdwkcover = Double.parseDouble(String.format("%.1f", ez_sales_pager.getFwdWeekCover()));

                txt_ez_SOHVal2.setText(" " + format.format(Math.round(ez_sales_pager.getStkOnhandQty())));
                txt_ez_GITVal.setText(" " + format.format(Math.round(ez_sales_pager.getStkGitQty())));
                txt_ez_ROSVal2.setText(" " + format.format(ros));
                txt_ez_FwdWkCoverVal2.setText(" " + fwdwkcover);
            }

        }
        ez_vwpagersales.setOnPageChangeListener(this);
        // Add viewpager_item.xml to ViewPager
        ez_vwpagersales.addView(itemView);

        return itemView;
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        ImageView img = (ImageView) ez_linear_dots.getChildAt(ez_currentPage);

        img.setImageResource(R.mipmap.dots_unselected);
        ez_currentPage = position;
        ImageView img1 = (ImageView) ez_linear_dots.getChildAt(ez_currentPage);
        img1.setImageResource(R.mipmap.dots_selected);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        (container).removeView((LinearLayout) object);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
