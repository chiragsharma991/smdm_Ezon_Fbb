package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;


/**
 * Created by hasai on 20/09/16.
 */
public class

SalesPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {


    Context context;
    SalesAnalysisListDisplay analysisClass;
    ArrayList<SalesAnalysisViewPagerValue> arrayList;
    int focusposition;
    SalesAnalysisViewPagerValue salesAnalysis;
    ViewPager vwpagersales;
    LinearLayout lldots;
    int currentIndex;
    SalesAnalysisSnapAdapter salesadapter;
    RecyclerView listView_SalesAnalysis;
    LayoutInflater inflater;
    public static int currentPage ;
    SalesPagerAdapter pagerAdapter;


    // ViewPager 0
    ImageView txtNetSalesImage, txtPlanSalesImage, txtNetSalesUImage;
    TextView txtPvASales, txtYoySales, txtSellThro, txtMixSales;
    TextView txtNetSalesVal, txtNetSales, txtNetSalesPerc, txtNetSalesName;
    TextView txtPlanSalesVal, txtPlanSales, txtPlanSalesPerc, txtPlanSalesName;
    TextView txtNetSalesUVal, txtNetSalesU, txtNetSalesUPerc, txtNetSalesUName;
    TextView txtSohUVal, txtSohU;
    TextView txtRosVal0, txtRos0;
    TextView txtFwdWkCoverVal0, txtFwdWkCover0;

    // ViewPager 1
//    TextView txtStoreVal_PvASales, txtZonalVal_PvASales, txtNationalVal_PvASales;
//    TextView txtStoreVal_YOYSales, txtZonalVal_YOYSales, txtNationalVal_YOYSales;
//    TextView txtStoreVal_SellThro, txtZonalVal_SellThro, txtNationalVal_SellThro;
//    TextView txtStoreVal_MixSales, txtZonalVal_MixSales, txtNationalVal_MixSales;
    TextView txtStoreVal_PvASales1, txtZonalSalesVal, txtNationalSalesVal;
    TextView relPvASales, relYoYSales, relSellThru, relMixSales, relRank;
    TextView txtPvAZonalRankVal, txtPvANationalRankVal, txtYoYZonalRankVal, txtYoYNationalRankVal;
    RelativeLayout storesales, zonalsales, nationalsales, sznchildRelLayout, rankRelLayout,pmsyrtxtRelLayout;
    LinearLayout linPvAZonalRank,linPvANationalRank,linYoYZonalRank,linYoYNationalRank;

    // ViewPager 2
    TextView txtSOHVal2, txtSOH2;
    TextView txtGITVal, txtGIT;
    TextView txtROSVal2, txtROS2;
    TextView txtFwdWkCoverVal2, txtFwdWkCover2;

    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    String fromWhere;

    public SalesPagerAdapter(Context context, ArrayList<SalesAnalysisViewPagerValue> arrayList, int focusposition, ViewPager vwpagersales, LinearLayout lldots, SalesAnalysisSnapAdapter salesadapter, RecyclerView listView_SalesAnalysis, ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList, String fromWhere, SalesPagerAdapter pagerAdapter) {

        Log.e("in sales adapter", " ---");
        this.context = context;
        this.arrayList = arrayList;
        // Log.i("size", "" + arrayList.size());
       // Log.i("size", "" + arrayList.size());
        this.focusposition = focusposition;
        this.vwpagersales = vwpagersales;
        this.lldots = lldots;
        this.salesAnalysisClassArrayList = salesAnalysisClassArrayList;
        this.fromWhere = fromWhere;
        this.pagerAdapter = pagerAdapter;



//       Log.i("size",""+arrayList.size());

        this.salesadapter = salesadapter;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
        if (arrayList.size() != 0) {
            salesAnalysis = arrayList.get(0);
        }

        currentPage = vwpagersales.getCurrentItem();
        Log.e("currentPage "," "+currentPage);


    }

    public SalesPagerAdapter() {

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        // Declare Variables
        // Log.e("in sales adapter", " 0");

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = null;


        if (position == 0) {
            itemView = inflater.inflate(R.layout.child_sales_viewpager, container,
                    false);
            txtNetSalesImage = (ImageView) itemView.findViewById(R.id.txtNetSalesImage);
            txtPlanSalesImage = (ImageView) itemView.findViewById(R.id.txtPlanSalesImage);
            txtNetSalesUImage = (ImageView) itemView.findViewById(R.id.txtNetSalesUImage);

            txtNetSalesVal = (TextView) itemView.findViewById(R.id.txtNetSalesVal);
            txtNetSales = (TextView) itemView.findViewById(R.id.txtNetSales);
            txtNetSalesPerc = (TextView) itemView.findViewById(R.id.txtNetSalesPerc);
            txtPlanSalesVal = (TextView) itemView.findViewById(R.id.txtPlanSalesVal);
            txtPlanSales = (TextView) itemView.findViewById(R.id.txtPlanSales);
            txtPlanSalesPerc = (TextView) itemView.findViewById(R.id.txtPlanSalesPerc);
            txtNetSalesUVal = (TextView) itemView.findViewById(R.id.txtNetSalesUVal);
            txtNetSalesU = (TextView) itemView.findViewById(R.id.txtNetSalesU);
            txtNetSalesUPerc = (TextView) itemView.findViewById(R.id.txtNetSalesUPerc);
            txtSohUVal = (TextView) itemView.findViewById(R.id.txtSoHUVal);
            txtSohU = (TextView) itemView.findViewById(R.id.txtSoHU);
//            txtRosVal0 = (TextView) itemView.findViewById(R.id.txtRosVal0);
//            txtRos0 = (TextView) itemView.findViewById(R.id.txtRos0);
//            txtFwdWkCoverVal0 = (TextView) itemView.findViewById(R.id.txtFwdWkCoverVal0);
//            txtFwdWkCover0 = (TextView) itemView.findViewById(R.id.txtFwdWkCover0);
            txtNetSalesPerc = (TextView) itemView.findViewById(R.id.txtNetSalesPerc);
            txtPlanSalesPerc = (TextView) itemView.findViewById(R.id.txtPlanSalesPerc);
            txtNetSalesUPerc = (TextView) itemView.findViewById(R.id.txtNetSalesUPerc);
            txtNetSalesName = (TextView) itemView.findViewById(R.id.txtNetSalesName);
            txtPlanSalesName = (TextView) itemView.findViewById(R.id.txtPlanSalesName);
            txtNetSalesUName = (TextView) itemView.findViewById(R.id.txtNetSalesUName);

            if (SalesAnalysisActivity1.selectedsegValue.equals("WTD") || SalesAnalysisActivity1.selectedsegValue.equals("LW")) {

                txtNetSales.setText("Net Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Net Sales(U)");
                txtSohU.setText("SOH(U)");
//                txtRos0.setText("ROS");
//                txtFwdWkCover0.setText("Fwd Wk Cover");
                txtNetSalesName.setText("WOW Gr%");
                txtPlanSalesName.setText("PvA%");
                txtNetSalesUName.setText("WOW Gr%");


            } else if (SalesAnalysisActivity1.selectedsegValue.equals("L4W") || SalesAnalysisActivity1.selectedsegValue.equals("YTD")) {

                txtNetSales.setText("Net Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Net Sales(U)");
                txtSohU.setText("SOH(U)");
//                txtRos0.setText("Inv Turn");
//                txtFwdWkCover0.setText("Velocity");
                txtNetSalesName.setText("YOY Gr%");
                txtPlanSalesName.setText("PvA%");
                txtNetSalesUName.setText("YOY Gr%");

            }

        } else if (position == 1) {
            itemView = inflater.inflate(R.layout.child_sales_viewpager1, container,
                    false);

//            txtStoreVal_PvASales = (TextView) itemView.findViewById(R.id.txtStoreVal_PvASales);
//            txtZonalVal_PvASales = (TextView) itemView.findViewById(R.id.txtZonalVal_PvASales);
//            txtNationalVal_PvASales = (TextView) itemView.findViewById(R.id.txtNationalVal_PvASales);
//            txtStoreVal_YOYSales = (TextView) itemView.findViewById(R.id.txtStoreVal_YOYSales);
//            txtZonalVal_YOYSales = (TextView) itemView.findViewById(R.id.txtZonalVal_YOYSales);
//            txtNationalVal_YOYSales = (TextView) itemView.findViewById(R.id.txtNationalVal_YOYSales);
//            txtStoreVal_SellThro = (TextView) itemView.findViewById(R.id.txtStoreVal_SellThro);
//            txtZonalVal_SellThro = (TextView) itemView.findViewById(R.id.txtZonalVal_SellThro);
//            txtNationalVal_SellThro = (TextView) itemView.findViewById(R.id.txtNationalVal_SellThro);
//            txtStoreVal_MixSales = (TextView) itemView.findViewById(R.id.txtStoreVal_MixSales);
//            txtZonalVal_MixSales = (TextView) itemView.findViewById(R.id.txtZonalVal_MixSales);
//            txtNationalVal_MixSales = (TextView) itemView.findViewById(R.id.txtNationalVal_MixSales);
//            txtPvASales = (TextView) itemView.findViewById(R.id.txtPvASales);
//            txtYoySales = (TextView) itemView.findViewById(R.id.txtYoySales);
//            txtSellThro = (TextView) itemView.findViewById(R.id.txtSellThro);
//            txtMixSales = (TextView) itemView.findViewById(R.id.txtMixSales);
//
//            txtPvASales.setText("PvA Sales% ");
//            txtYoySales.setText("YOY Sales% ");
//            txtSellThro.setText("Sell Thru(U)% ");
//            txtMixSales.setText(" Mix(Sales%)");

            txtStoreVal_PvASales1 = (TextView) itemView.findViewById(R.id.txtStoreVal_PvASales1);
            txtZonalSalesVal = (TextView) itemView.findViewById(R.id.txtZonalSalesVal);
            txtNationalSalesVal = (TextView) itemView.findViewById(R.id.txtNationalSalesVal);
            relPvASales = (TextView) itemView.findViewById(R.id.relPvASales);
            relYoYSales = (TextView) itemView.findViewById(R.id.relYoYSales);
            relSellThru = (TextView) itemView.findViewById(R.id.relSellThru);
            relMixSales = (TextView) itemView.findViewById(R.id.relMixSales);
            relRank = (TextView) itemView.findViewById(R.id.relRank);
            //Rank Layout
            txtPvAZonalRankVal = (TextView) itemView.findViewById(R.id.txtPvAZonalRankVal);
            txtPvANationalRankVal = (TextView) itemView.findViewById(R.id.txtPvANationalRankVal);
            txtYoYZonalRankVal = (TextView) itemView.findViewById(R.id.txtYoYZonalRankVal);
            txtYoYNationalRankVal = (TextView) itemView.findViewById(R.id.txtYoYNationalRankVal);
            storesales = (RelativeLayout) itemView.findViewById(R.id.storesales);
            zonalsales = (RelativeLayout) itemView.findViewById(R.id.zonalsales);
            nationalsales = (RelativeLayout) itemView.findViewById(R.id.nationalsales);
            sznchildRelLayout = (RelativeLayout) itemView.findViewById(R.id.sznchildRelLayout);
            sznchildRelLayout.setVisibility(View.VISIBLE);
            rankRelLayout = (RelativeLayout) itemView.findViewById(R.id.rankRelLayout);
            rankRelLayout.setVisibility(View.GONE);
            pmsyrtxtRelLayout = (RelativeLayout) itemView.findViewById(R.id.pmsyrtxtRelLayout);
            linPvAZonalRank = (LinearLayout) itemView.findViewById(R.id.linPvAZonalRank);
            linPvANationalRank = (LinearLayout) itemView.findViewById(R.id.linPvANationalRank);
            linYoYZonalRank = (LinearLayout) itemView.findViewById(R.id.linYoYZonalRank);
            linYoYNationalRank = (LinearLayout) itemView.findViewById(R.id.linYoYNationalRank);

        } else if (position == 2) {
            itemView = inflater.inflate(R.layout.child_sales_viewpager2, container,
                    false);

            txtSOHVal2 = (TextView) itemView.findViewById(R.id.txtSOHUVal2);
            txtSOH2 = (TextView) itemView.findViewById(R.id.txtSOH2);
            txtGITVal = (TextView) itemView.findViewById(R.id.txtGITVal);
            txtGIT = (TextView) itemView.findViewById(R.id.txtGIT);
            txtROSVal2 = (TextView) itemView.findViewById(R.id.txtROSVal2);
            txtROS2 = (TextView) itemView.findViewById(R.id.txtROS2);
            txtFwdWkCoverVal2 = (TextView) itemView.findViewById(R.id.txtFwdWkCoverVal2);
            txtFwdWkCover2 = (TextView) itemView.findViewById(R.id.txtFwdWkCover2);

        }


        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));

        if (position == 0) {


            if (SalesAnalysisActivity1.selectedsegValue.equals("WTD") || SalesAnalysisActivity1.selectedsegValue.equals("LW")) {

                if (salesAnalysis != null) {
                    Log.e("Round val", "" + Math.round(salesAnalysis.getSaleNetVal()));
                    txtNetSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getSaleNetVal())));
                    txtPlanSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getPlanSaleNetVal())));
                    txtNetSalesUVal.setText("" + format.format(Math.round(salesAnalysis.getSaleTotQty())));
                    txtSohUVal.setText("" + format.format(Math.round(salesAnalysis.getStkOnhandQty())));
//                    txtRosVal0.setText("" + String.format("%.1f",salesAnalysis.getRos()));
//                    txtFwdWkCoverVal0.setText("" + String.format("%.1f",salesAnalysis.getFwdWeekCover()));
                    txtNetSalesPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) + "%");
                    txtPlanSalesPerc.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                    txtNetSalesUPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesUnitsGrowthPct()) + "%");


                    //
                    if (salesAnalysis.getWowNetSalesGrowthPct() <= 0) {
                        txtNetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                        txtNetSalesVal.setTextColor(Color.parseColor("#fe1a01"));
                    } else if (salesAnalysis.getWowNetSalesGrowthPct() > 0) {
                        txtNetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txtNetSalesVal.setTextColor(Color.parseColor("#70e503"));
                    }
                    //
                    ///
                    if (salesAnalysis.getPvaAchieved() < 70) {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                        txtPlanSalesVal.setTextColor(Color.parseColor("#fe1a01"));
                    } else if (salesAnalysis.getPvaAchieved() > 90) {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txtPlanSalesVal.setTextColor(Color.parseColor("#70e503"));
                    } else {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                        txtPlanSalesVal.setTextColor(Color.parseColor("#ff7e00"));
                    }
                    ///

                    ////
                    if (salesAnalysis.getWowNetSalesUnitsGrowthPct() <= 0) {
                        txtNetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                        txtNetSalesUVal.setTextColor(Color.parseColor("#fe1a01"));
                    } else if (salesAnalysis.getWowNetSalesUnitsGrowthPct() > 0) {
                        txtNetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                        txtNetSalesUVal.setTextColor(Color.parseColor("#70e503"));
                    }
                    ////

                }


            } else if (SalesAnalysisActivity1.selectedsegValue.equals("L4W") || SalesAnalysisActivity1.selectedsegValue.equals("YTD")) {

                if (salesAnalysis != null) {

                    txtNetSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getSaleNetVal())));
                    txtPlanSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getPlanSaleNetVal())));
                    txtNetSalesUVal.setText(" " + format.format(Math.round(salesAnalysis.getSaleTotQty())));
                    txtSohUVal.setText(" " + format.format(Math.round(salesAnalysis.getStkOnhandQty())));
//                    txtRosVal0.setText(" " + String.format("%.1f",salesAnalysis.getInvTurns()));
//                    txtFwdWkCoverVal0.setText(" " +String.format("%.1f", salesAnalysis.getVelocity()));


                    txtNetSalesPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) + "%");
                    txtPlanSalesPerc.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                    txtNetSalesUPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesUnitsGrowthPct()) + "%");
                    Log.i("saleNetVal IN L4W", "" + salesAnalysis.getSaleNetVal());
                }
                //           txtNetSalesPerc.setText(salesAnalysis.);

                //
                if (salesAnalysis.getWowNetSalesGrowthPct() <= 0) {
                    txtNetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    txtNetSalesVal.setTextColor(Color.parseColor("#fe1a01"));
                } else if (salesAnalysis.getWowNetSalesGrowthPct() > 0) {
                    txtNetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    txtNetSalesVal.setTextColor(Color.parseColor("#70e503"));
                }
                //
                ///
                if (salesAnalysis.getPvaAchieved() < 70) {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    txtPlanSalesVal.setTextColor(Color.parseColor("#fe1a01"));
                } else if (salesAnalysis.getPvaAchieved() > 90) {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    txtPlanSalesVal.setTextColor(Color.parseColor("#70e503"));
                } else {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                    txtPlanSalesVal.setTextColor(Color.parseColor("#ff7e00"));
                }
                ///

                ////
                if (salesAnalysis.getWowNetSalesUnitsGrowthPct() <= 0) {
                    txtNetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                    txtNetSalesUVal.setTextColor(Color.parseColor("#fe1a01"));
                } else if (salesAnalysis.getWowNetSalesUnitsGrowthPct() > 0) {
                    txtNetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                    txtNetSalesUVal.setTextColor(Color.parseColor("#70e503"));

                }

            }
            if(salesAnalysisClassArrayList.size()!=0){
            SalesAnalysisListDisplay salesAnalysisListDisplay = salesAnalysisClassArrayList.get(position);
            LinearLayoutManager llm = (LinearLayoutManager) listView_SalesAnalysis.getLayoutManager();
            int firstVisibleItemPosition = llm.findFirstVisibleItemPosition();
            Log.e("instantiateItem: ", "" + firstVisibleItemPosition);

            //if (firstVisibleItemPosition == 0) {
            if (salesAnalysisListDisplay.getPlanDept() != null) {
                if (salesAnalysisListDisplay.getPlanDept().equals("All")) {
                    Log.e("-----", "All");
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    // salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                    //listView_SalesAnalysis.setAdapter(salesadapter);
                    salesadapter.notifyDataSetChanged();

                }
            }
            if (salesAnalysisListDisplay.getPlanCategory() != null) {
                if (salesAnalysisListDisplay.getPlanCategory().equals("All")) {
                    Log.e("-----", "All");
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    // salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                    // listView_SalesAnalysis.setAdapter(salesadapter);
                    salesadapter.notifyDataSetChanged();

                }
            }

            if (salesAnalysisListDisplay.getPlanClass() != null) {
                if (salesAnalysisListDisplay.getPlanClass().equals("All")) {
                    Log.e("-----", "All");
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    // salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                    // listView_SalesAnalysis.setAdapter(salesadapter);
                    salesadapter.notifyDataSetChanged();

                }
            }
            if (salesAnalysisListDisplay.getBrandName() != null) {
                if (salesAnalysisListDisplay.getBrandName().equals("All")) {
                    Log.e("-----", "All");
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    // salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                    // listView_SalesAnalysis.setAdapter(salesadapter);
                    salesadapter.notifyDataSetChanged();

                }
            }
            if (salesAnalysisListDisplay.getBrandplanClass() != null) {
                if (salesAnalysisListDisplay.getBrandplanClass().equals("All")) {
                    Log.e("-----", "All");
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    // salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                    // listView_SalesAnalysis.setAdapter(salesadapter);
                    salesadapter.notifyDataSetChanged();

                }
            }
         }

        } else if (position == 1) {

            if (salesAnalysis != null) {

                sznchildRelLayout.setVisibility(View.VISIBLE);
                rankRelLayout.setVisibility(View.GONE);

                txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getPvaAchievedZonal()) + "%");
                txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getPvaAchievedNational()) + "%");

                relPvASales.setBackgroundResource(R.drawable.rounded_edittext2);
                if (Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedNational())) {
//                    txtStoreVal_PvASales.setTextColor(Color.GREEN);
                    storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                    zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                    nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);


                } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchievedNational())) {
//                    txtZonalVal_PvASales.setTextColor(Color.GREEN);
                    zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                    storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                    nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                } else if (Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchievedZonal())) {
//                    txtNationalVal_PvASales.setTextColor(Color.GREEN);
                    nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                    storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                    zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                } else if (Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedNational())
                        && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchieved())
                        && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchievedZonal())) {
//                    txtStoreVal_PvASales.setTextColor(Color.GREEN);
                    storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                    nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                    zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedNational())) {
//                    txtZonalVal_PvASales.setTextColor(Color.GREEN);
                    zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                    storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                    nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                }


                //PvA Sales
                relPvASales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pmsyrtxtRelLayout.setTag("PvA%");
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        relPvASales.setBackgroundResource(R.drawable.rounded_edittext2);
                        relMixSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relSellThru.setBackgroundResource(R.drawable.rounded_edittext3);
                        relYoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relRank.setBackgroundResource(R.drawable.rounded_edittext3);

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getPvaAchievedZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getPvaAchievedNational()) + "%");

                        if (Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedNational())) {
//                        txtStoreVal_PvASales.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);


                        } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchievedNational())) {
//                        txtZonalVal_PvASales.setTextColor(Color.GREEN);)
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchievedZonal())) {
//                        txtNationalVal_PvASales.setTextColor(Color.GREEN);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedNational())
                                && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchieved())
                                && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchievedZonal())) {
//                        txtStoreVal_PvASales.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedNational())) {
//                       txtZonalVal_PvASales.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        } else if (Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchievedNational()) < Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) < Math.round(salesAnalysis.getPvaAchievedZonal())) {
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        }
                    }
                });
                //YoY Sales
                relYoYSales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        relPvASales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relMixSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relSellThru.setBackgroundResource(R.drawable.rounded_edittext3);
                        relYoYSales.setBackgroundResource(R.drawable.rounded_edittext2);
                        relRank.setBackgroundResource(R.drawable.rounded_edittext3);

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) + "%");

                        //YOY Sales
                        if (Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())) {
//                    txtStoreVal_YOYSales.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())) {
//                    txtZonalVal_YOYSales.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal())) {
//                    txtNationalVal_YOYSales.setTextColor(Color.GREEN);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())
                                && Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPct())
                                && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal())) {
//                    txtStoreVal_YOYSales.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())) {
//                    txtZonalVal_YOYSales.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal())) {
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        }


                    }
                });

                //Sell Thru
                relSellThru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        relPvASales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relMixSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relSellThru.setBackgroundResource(R.drawable.rounded_edittext2);
                        relYoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relRank.setBackgroundResource(R.drawable.rounded_edittext3);

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getSellThruUnits()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getSellThruUnitsZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getSellThruUnitsNational()) + "%");


                        //Sell Thro

                        if (Math.round(salesAnalysis.getSellThruUnits()) > Math.round(salesAnalysis.getSellThruUnitsZonal()) && Math.round(salesAnalysis.getSellThruUnits()) > Math.round(salesAnalysis.getSellThruUnitsNational())) {
//                    txtStoreVal_SellThro.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getSellThruUnitsZonal()) > Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsZonal()) > Math.round(salesAnalysis.getSellThruUnitsNational())) {
//                    txtZonalVal_SellThro.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getSellThruUnitsNational()) > Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsNational()) > Math.round(salesAnalysis.getSellThruUnitsZonal())) {
//                    txtNationalVal_SellThro.setTextColor(Color.GREEN);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getSellThruUnits()) == Math.round(salesAnalysis.getSellThruUnitsNational()) && Math.round(salesAnalysis.getSellThruUnits()) == Math.round(salesAnalysis.getSellThruUnitsZonal())
                                && Math.round(salesAnalysis.getSellThruUnitsZonal()) == Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsZonal()) == Math.round(salesAnalysis.getSellThruUnitsNational())
                                && Math.round(salesAnalysis.getSellThruUnitsNational()) == Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsNational()) == Math.round(salesAnalysis.getSellThruUnitsZonal())) {
//                    txtStoreVal_SellThro.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getSellThruUnitsZonal()) == Math.round(salesAnalysis.getSellThruUnitsNational()) && Math.round(salesAnalysis.getSellThruUnits()) < Math.round(salesAnalysis.getSellThruUnitsZonal()) && Math.round(salesAnalysis.getSellThruUnits()) < Math.round(salesAnalysis.getSellThruUnitsNational())) {
//                    txtZonalVal_SellThro.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getSellThruUnits()) == Math.round(salesAnalysis.getSellThruUnitsZonal()) && Math.round(salesAnalysis.getSellThruUnitsNational()) < Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsNational()) < Math.round(salesAnalysis.getSellThruUnitsZonal())) {
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        }

                    }
                });

                //Mix Sales
                relMixSales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        relPvASales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relMixSales.setBackgroundResource(R.drawable.rounded_edittext2);
                        relSellThru.setBackgroundResource(R.drawable.rounded_edittext3);
                        relYoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relRank.setBackgroundResource(R.drawable.rounded_edittext3);

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getMixSales()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getMixSalesZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getMixsalesNational()) + "%");

                        //Mix Sales
                        if (Math.round(salesAnalysis.getMixSales()) > Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixSales()) > Math.round(salesAnalysis.getMixsalesNational())) {
//                    txtStoreVal_MixSales.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getMixSalesZonal()) > Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixSalesZonal()) > Math.round(salesAnalysis.getMixsalesNational())) {
//                    txtZonalVal_MixSales.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);


                        } else if (Math.round(salesAnalysis.getMixsalesNational()) > Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixsalesNational()) > Math.round(salesAnalysis.getMixSalesZonal())) {
//                    txtNationalVal_MixSales.setTextColor(Color.GREEN);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);

                        } else if (Math.round(salesAnalysis.getMixSales()) == Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixSales()) == Math.round(salesAnalysis.getMixsalesNational())
                                && Math.round(salesAnalysis.getMixSalesZonal()) == Math.round(salesAnalysis.getMixsalesNational()) && Math.round(salesAnalysis.getMixSalesZonal()) == Math.round(salesAnalysis.getMixSales())
                                && Math.round(salesAnalysis.getMixsalesNational()) == Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixsalesNational()) == Math.round(salesAnalysis.getMixSalesZonal())) {
//                    txtStoreVal_MixSales.setTextColor(Color.GREEN);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);


                        } else if (Math.round(salesAnalysis.getMixSalesZonal()) == Math.round(salesAnalysis.getMixsalesNational()) && Math.round(salesAnalysis.getMixSales()) < Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixSales()) < Math.round(salesAnalysis.getMixsalesNational())) {
//                    txtZonalVal_MixSales.setTextColor(Color.GREEN);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext1);
                            storesales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        } else if (Math.round(salesAnalysis.getMixSales()) == Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixsalesNational()) < Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixsalesNational()) < Math.round(salesAnalysis.getMixSalesZonal())) {
                            storesales.setBackgroundResource(R.drawable.rounded_edittext1);
                            zonalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                            nationalsales.setBackgroundResource(R.drawable.rounded_edittext4);
                        }

                    }
                });

                //Rank
                relRank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankRelLayout.setVisibility(View.VISIBLE);
                        sznchildRelLayout.setVisibility(View.GONE);

                        relPvASales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relMixSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relSellThru.setBackgroundResource(R.drawable.rounded_edittext3);
                        relYoYSales.setBackgroundResource(R.drawable.rounded_edittext3);
                        relRank.setBackgroundResource(R.drawable.rounded_edittext2);


                        txtPvAZonalRankVal.setText("" + salesAnalysis.getZonalSalesRank());
                        txtPvANationalRankVal.setText("" + salesAnalysis.getNationalSalesRank());
                        txtYoYZonalRankVal.setText("" + salesAnalysis.getZonalYOYGrowthRank());
                        txtYoYNationalRankVal.setText("" + salesAnalysis.getNationalYOYGrowthRank());

                        //Sales Rank Condtion
                        if (salesAnalysis.getZonalSalesRank() > salesAnalysis.getNationalSalesRank())

                        {
                            Log.e("Rank Condition", "" + salesAnalysis.getZonalSalesRank());
                            linPvAZonalRank.setBackgroundResource(R.color.smdm_green);
                            linPvANationalRank.setBackgroundResource(R.color.smdm_amber);
                        } else if (salesAnalysis.getNationalSalesRank() > salesAnalysis.getZonalSalesRank()) {
                            linPvAZonalRank.setBackgroundResource(R.color.smdm_amber);
                            linPvANationalRank.setBackgroundResource(R.color.smdm_green);
                        } else if (salesAnalysis.getZonalSalesRank() == salesAnalysis.getNationalSalesRank()) {
                            linPvAZonalRank.setBackgroundResource(R.color.smdm_green);
                            linPvANationalRank.setBackgroundResource(R.color.smdm_amber);
                        }
                        //YoY Rank Condition
                        if (salesAnalysis.getZonalYOYGrowthRank() > salesAnalysis.getNationalYOYGrowthRank()) {
                            linYoYZonalRank.setBackgroundResource(R.color.smdm_green);
                            linYoYNationalRank.setBackgroundResource(R.color.smdm_amber);
                        } else if (salesAnalysis.getNationalYOYGrowthRank() > salesAnalysis.getZonalYOYGrowthRank()) {
                            linYoYZonalRank.setBackgroundResource(R.color.smdm_amber);
                            linYoYNationalRank.setBackgroundResource(R.color.smdm_green);
                        } else if (salesAnalysis.getZonalYOYGrowthRank() == salesAnalysis.getNationalYOYGrowthRank()) {
                            linYoYZonalRank.setBackgroundResource(R.color.smdm_green);
                            linYoYNationalRank.setBackgroundResource(R.color.smdm_amber);

                        }
                    }
                });

            }


        } else if (position == 2) {
            if (salesAnalysis != null) {
                double ros = Double.parseDouble(String.format("%.1f", salesAnalysis.getRos()));
                Log.e("ros", "" + ros);
                double fwdwkcover = Double.parseDouble(String.format("%.1f", salesAnalysis.getFwdWeekCover()));
                Log.e("fwdwkcover", "" + fwdwkcover);

                txtSOHVal2.setText(" " + format.format(Math.round(salesAnalysis.getStkOnhandQty())));
                txtGITVal.setText(" " + format.format(Math.round(salesAnalysis.getStkGitQty())));
                txtROSVal2.setText(format.format(ros));
                txtFwdWkCoverVal2.setText("" + fwdwkcover);
            }

        }








        vwpagersales.setOnPageChangeListener(this);
        // Add viewpager_item.xml to ViewPager
        vwpagersales.addView(itemView);

        return itemView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Log.e("Scroll Pos",""+position);

    }

    @Override
    public void onPageSelected(int position) {

        ImageView img = (ImageView) lldots.getChildAt(currentPage);
        img.setImageResource(R.mipmap.dots_unselected);
        currentPage = position;
        ImageView img1 = (ImageView) lldots.getChildAt(currentPage);
        img1.setImageResource(R.mipmap.dots_selected);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
