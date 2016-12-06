package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
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
    SalesAnalysisAdapter salesadapter;
    ListView listView_SalesAnalysis;
    LayoutInflater inflater;
    public static int currentPage = 0;


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
    TextView txtStoreVal_PvASales, txtZonalVal_PvASales, txtNationalVal_PvASales;
    TextView txtStoreVal_YOYSales, txtZonalVal_YOYSales, txtNationalVal_YOYSales;
    TextView txtStoreVal_SellThro, txtZonalVal_SellThro, txtNationalVal_SellThro;
    TextView txtStoreVal_MixSales, txtZonalVal_MixSales, txtNationalVal_MixSales;

    // ViewPager 2
    TextView txtSOHVal2, txtSOH2;
    TextView txtGITVal, txtGIT;
    TextView txtROSVal2, txtROS2;
    TextView txtFwdWkCoverVal2, txtFwdWkCover2;

    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    String fromWhere;

    public SalesPagerAdapter(Context context, ArrayList<SalesAnalysisViewPagerValue> arrayList, int focusposition, ViewPager vwpagersales, LinearLayout lldots, SalesAnalysisAdapter salesadapter, ListView listView_SalesAnalysis, ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList, String fromWhere) {

        Log.e("in sales adapter", " ---");
        this.context = context;
        this.arrayList = arrayList;
        Log.i("size", "" + arrayList.size());
        this.focusposition = focusposition;
        this.vwpagersales = vwpagersales;
        this.lldots = lldots;
        this.salesAnalysisClassArrayList = salesAnalysisClassArrayList;
        this.fromWhere = fromWhere;


//       Log.i("size",""+arrayList.size());

        this.salesadapter = salesadapter;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
        if (arrayList.size() != 0) {
            salesAnalysis = arrayList.get(0);
        }

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

            if (SalesAnalysisActivity.selectedsegValue.equals("WTD") || SalesAnalysisActivity.selectedsegValue.equals("LW")) {

                txtNetSales.setText("Net Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Net Sales(U)");
                txtSohU.setText("SOH(U)");
//                txtRos0.setText("ROS");
//                txtFwdWkCover0.setText("Fwd Wk Cover");
                txtNetSalesName.setText("WOW Gr%");
                txtPlanSalesName.setText("PvA%");
                txtNetSalesUName.setText("WOW Gr%");


            } else if (SalesAnalysisActivity.selectedsegValue.equals("L4W") || SalesAnalysisActivity.selectedsegValue.equals("YTD")) {

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

            txtStoreVal_PvASales = (TextView) itemView.findViewById(R.id.txtStoreVal_PvASales);
            txtZonalVal_PvASales = (TextView) itemView.findViewById(R.id.txtZonalVal_PvASales);
            txtNationalVal_PvASales = (TextView) itemView.findViewById(R.id.txtNationalVal_PvASales);
            txtStoreVal_YOYSales = (TextView) itemView.findViewById(R.id.txtStoreVal_YOYSales);
            txtZonalVal_YOYSales = (TextView) itemView.findViewById(R.id.txtZonalVal_YOYSales);
            txtNationalVal_YOYSales = (TextView) itemView.findViewById(R.id.txtNationalVal_YOYSales);
            txtStoreVal_SellThro = (TextView) itemView.findViewById(R.id.txtStoreVal_SellThro);
            txtZonalVal_SellThro = (TextView) itemView.findViewById(R.id.txtZonalVal_SellThro);
            txtNationalVal_SellThro = (TextView) itemView.findViewById(R.id.txtNationalVal_SellThro);
            txtStoreVal_MixSales = (TextView) itemView.findViewById(R.id.txtStoreVal_MixSales);
            txtZonalVal_MixSales = (TextView) itemView.findViewById(R.id.txtZonalVal_MixSales);
            txtNationalVal_MixSales = (TextView) itemView.findViewById(R.id.txtNationalVal_MixSales);
            txtPvASales = (TextView) itemView.findViewById(R.id.txtPvASales);
            txtYoySales = (TextView) itemView.findViewById(R.id.txtYoySales);
            txtSellThro = (TextView) itemView.findViewById(R.id.txtSellThro);
            txtMixSales = (TextView) itemView.findViewById(R.id.txtMixSales);

            txtPvASales.setText("PvA Sales% ");
            txtYoySales.setText("YOY Sales% ");
            txtSellThro.setText("Sell Thru(U)% ");
            txtMixSales.setText(" Mix(Sales%)");

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


//        analysisClass = salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition());
//        Log.i("salesAnalysis value",""+salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()));
//        salesAnalysis=arrayList.get(focusposition);
//        Log.e("in sales pager adapter",""+salesAnalysis);


//        Log.e("in sales pager adapter","");


        if (position == 0) {


            if (SalesAnalysisActivity.selectedsegValue.equals("WTD") || SalesAnalysisActivity.selectedsegValue.equals("LW")) {

                if (salesAnalysis != null) {
                    txtNetSalesVal.setText("\u20B9\t"+(int) salesAnalysis.getSaleNetVal());
                    txtPlanSalesVal.setText("\u20B9\t" + (int) salesAnalysis.getPlanSaleNetVal());
                    txtNetSalesUVal.setText("" + (int) salesAnalysis.getSaleTotQty());
                    txtSohUVal.setText("" + (int) salesAnalysis.getStkOnhandQty());
//                    txtRosVal0.setText("" + String.format("%.1f",salesAnalysis.getRos()));
//                    txtFwdWkCoverVal0.setText("" + String.format("%.1f",salesAnalysis.getFwdWeekCover()));
                    txtNetSalesPerc.setText("" + (int) salesAnalysis.getWowNetSalesGrowthPct() + "%");
                    txtPlanSalesPerc.setText("" + (int) salesAnalysis.getPvaAchieved() + "%");
                    txtNetSalesUPerc.setText("" + (int) salesAnalysis.getWowNetSalesUnitsGrowthPct() + "%");


                    //
                    if (salesAnalysis.getWowNetSalesGrowthPct() <= 0) {
                        txtNetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    } else if (salesAnalysis.getWowNetSalesGrowthPct() > 0) {
                        txtNetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    }
                    //
                    ///
                    if (salesAnalysis.getPvaAchieved() < 70) {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    } else if (salesAnalysis.getPvaAchieved() > 90) {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    } else {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                    }
                    ///

                    ////
                    if (salesAnalysis.getWowNetSalesUnitsGrowthPct() <= 0) {
                        txtNetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                    } else if (salesAnalysis.getWowNetSalesUnitsGrowthPct() > 0) {
                        txtNetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                    }
                    ////

                }


            } else if (SalesAnalysisActivity.selectedsegValue.equals("L4W") || SalesAnalysisActivity.selectedsegValue.equals("YTD")) {

                if (salesAnalysis != null) {

                    txtNetSalesVal.setText("\u20B9\t" + (int) salesAnalysis.getSaleNetVal());
                    txtPlanSalesVal.setText("\u20B9\t" + (int) salesAnalysis.getPlanSaleNetVal());
                    txtNetSalesUVal.setText(" " + (int) salesAnalysis.getSaleTotQty());
                    txtSohUVal.setText(" " + (int) salesAnalysis.getStkOnhandQty());
//                    txtRosVal0.setText(" " + String.format("%.1f",salesAnalysis.getInvTurns()));
//                    txtFwdWkCoverVal0.setText(" " +String.format("%.1f", salesAnalysis.getVelocity()));


                    txtNetSalesPerc.setText("" + (int) salesAnalysis.getWowNetSalesGrowthPct() + "%");
                    txtPlanSalesPerc.setText("" + (int) salesAnalysis.getPvaAchieved() + "%");
                    txtNetSalesUPerc.setText("" + (int) salesAnalysis.getWowNetSalesUnitsGrowthPct() + "%");
                    Log.i("saleNetVal IN L4W", "" + salesAnalysis.getSaleNetVal());
                }
                //           txtNetSalesPerc.setText(salesAnalysis.);

                //
                if (salesAnalysis.getWowNetSalesGrowthPct() <= 0) {
                    txtNetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                } else if (salesAnalysis.getWowNetSalesGrowthPct() > 0) {
                    txtNetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                }
                //
                ///
                if (salesAnalysis.getPvaAchieved() < 70) {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                } else if (salesAnalysis.getPvaAchieved() > 90) {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                } else {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                }
                ///

                ////
                if (salesAnalysis.getWowNetSalesUnitsGrowthPct() <= 0) {
                    txtNetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                } else if (salesAnalysis.getWowNetSalesUnitsGrowthPct() > 0) {
                    txtNetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                }
                ////

            }
            // update listview

            SalesAnalysisListDisplay salesAnalysisClass = salesAnalysisClassArrayList.get(0);
            Log.e("listv",""+listView_SalesAnalysis.getFirstVisiblePosition());

            if(listView_SalesAnalysis.getFirstVisiblePosition() == 0) {

                if (salesAnalysisClass.getPlanDept() != null) {
                    if (salesAnalysisClass.getPlanDept().equals("All")) {
                        Log.e("-----", "All");
                        salesAnalysisClass.setPvaAchieved(salesAnalysis.getPvaAchieved());
                        salesAnalysisClassArrayList.set(0, salesAnalysisClass);
                        salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                        listView_SalesAnalysis.setAdapter(salesadapter);
                        salesadapter.notifyDataSetChanged();
                    }

                }

                if (salesAnalysisClass.getPlanCategory() != null) {
                    if (salesAnalysisClass.getPlanCategory().equals("All")) {
                        Log.e("-----", "All");
                        salesAnalysisClass.setPvaAchieved(salesAnalysis.getPvaAchieved());
                        salesAnalysisClassArrayList.set(0, salesAnalysisClass);
                        salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                        listView_SalesAnalysis.setAdapter(salesadapter);
                        salesadapter.notifyDataSetChanged();
                    }

                }

                if (salesAnalysisClass.getPlanClass() != null) {
                    if (salesAnalysisClass.getPlanClass().equals("All")) {
                        Log.e("-----", "All");
                        salesAnalysisClass.setPvaAchieved(salesAnalysis.getPvaAchieved());
                        salesAnalysisClassArrayList.set(0, salesAnalysisClass);
                        salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                        listView_SalesAnalysis.setAdapter(salesadapter);
                        salesadapter.notifyDataSetChanged();
                    }

                }

                if (salesAnalysisClass.getBrandName() != null) {
                    if (salesAnalysisClass.getBrandName().equals("All")) {
                        Log.e("-----", "All");
                        salesAnalysisClass.setPvaAchieved(salesAnalysis.getPvaAchieved());
                        salesAnalysisClassArrayList.set(0, salesAnalysisClass);
                        salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                        listView_SalesAnalysis.setAdapter(salesadapter);
                        salesadapter.notifyDataSetChanged();
                    }

                }

                if (salesAnalysisClass.getBrandplanClass() != null) {
                    if (salesAnalysisClass.getBrandplanClass().equals("All")) {
                        Log.e("-----", "All");
                        salesAnalysisClass.setPvaAchieved(salesAnalysis.getPvaAchieved());
                        salesAnalysisClassArrayList.set(0, salesAnalysisClass);
                        salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                        listView_SalesAnalysis.setAdapter(salesadapter);
                        salesadapter.notifyDataSetChanged();
                    }

                }

            }


//            LinearLayout layout = (LinearLayout) itemView;
//
//            LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
//            RelativeLayout relnetsales = (RelativeLayout) layout1.getChildAt(0);
//            RelativeLayout relplansales = (RelativeLayout) layout1.getChildAt(1);
//
//            relnetsales.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Net Sales", "----");
//                    //updatelistview();
//                }
//            });

//            relplansales.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Plan Sales", "----");
//                    updatelistview("Plan Sales");
//                }
//            });

//            LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
//            RelativeLayout relnetsalesu = (RelativeLayout) layout2.getChildAt(0);
//            RelativeLayout relplansohu = (RelativeLayout) layout2.getChildAt(1);

//            relnetsalesu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Net Sales U", "----");
//                    updatelistview("Net Sales U");
//                }
//            });

            /*relplansohu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("SOH U", "----");
                    updatelistview("SOH U");
                }
            });

            LinearLayout layout3 = (LinearLayout) layout.getChildAt(2);
            RelativeLayout relros = (RelativeLayout) layout3.getChildAt(0);
            RelativeLayout relfwdwkcover0 = (RelativeLayout) layout3.getChildAt(1);

            relros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ROS ", "----");
                    updatelistview("ROS");
                }
            });

            relfwdwkcover0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Fwd Wk Cover 0", "----");
                    updatelistview("Fwd Wk Cover 0");
                }
            });
*/

        } else if (position == 1) {

            if (salesAnalysis != null) {

                txtStoreVal_PvASales.setText("" + (int) salesAnalysis.getPvaAchieved());
                txtZonalVal_PvASales.setText(" " + (int) salesAnalysis.getPvaAchievedZonal());
                txtNationalVal_PvASales.setText(" " + (int) salesAnalysis.getPvaAchievedNational());

                txtStoreVal_YOYSales.setText(" " + (int) salesAnalysis.getYoyNetSalesGrowthPct());
                txtZonalVal_YOYSales.setText(" " + (int) salesAnalysis.getYoyNetSalesGrowthPctZonal());
                txtNationalVal_YOYSales.setText(" " + (int) salesAnalysis.getYoyNetSalesGrowthPctNational());

                txtStoreVal_SellThro.setText(" " + (int) salesAnalysis.getSellThruUnits());
                txtZonalVal_SellThro.setText(" " + (int) salesAnalysis.getSellThruUnitsZonal());
                txtNationalVal_SellThro.setText(" " + (int) salesAnalysis.getSellThruUnitsNational());

                txtStoreVal_MixSales.setText(" " + (int) salesAnalysis.getMixSales());
                txtZonalVal_MixSales.setText(" " + (int) salesAnalysis.getMixSalesZonal());
                txtNationalVal_MixSales.setText(" " + (int) salesAnalysis.getMixsalesNational());

                //PvA Sales

                if ((int) salesAnalysis.getPvaAchieved() > (int) salesAnalysis.getPvaAchievedZonal() && (int) salesAnalysis.getPvaAchieved() > (int) salesAnalysis.getPvaAchievedNational())
                {
                    txtStoreVal_PvASales.setTextColor(Color.GREEN);

                }
                else if ((int) salesAnalysis.getPvaAchievedZonal() > (int) salesAnalysis.getPvaAchieved() && (int) salesAnalysis.getPvaAchievedZonal() > (int) salesAnalysis.getPvaAchievedNational())
                {
                    txtZonalVal_PvASales.setTextColor(Color.GREEN);



                }
                else if ((int) salesAnalysis.getPvaAchievedNational() > (int) salesAnalysis.getPvaAchieved() && (int) salesAnalysis.getPvaAchievedNational() > (int) salesAnalysis.getPvaAchievedZonal())
                {
                    txtNationalVal_PvASales.setTextColor(Color.GREEN);

                }
                else if((int)salesAnalysis.getPvaAchieved() == (int)salesAnalysis.getPvaAchievedZonal() && (int)salesAnalysis.getPvaAchieved() == (int)salesAnalysis.getPvaAchievedNational()
                        && (int)salesAnalysis.getPvaAchievedZonal() == (int)salesAnalysis.getPvaAchievedNational() && (int)salesAnalysis.getPvaAchievedZonal() == (int)salesAnalysis.getPvaAchieved()
                        && (int)salesAnalysis.getPvaAchievedNational() == (int)salesAnalysis.getPvaAchieved() && (int)salesAnalysis.getPvaAchievedNational() == (int)salesAnalysis.getPvaAchievedZonal())
                {
                    txtStoreVal_PvASales.setTextColor(Color.GREEN);

                }
                else if((int)salesAnalysis.getPvaAchievedZonal() == (int)salesAnalysis.getPvaAchievedNational() && (int)salesAnalysis.getPvaAchieved() < (int)salesAnalysis.getPvaAchievedZonal() && (int)salesAnalysis.getPvaAchieved() < (int)salesAnalysis.getPvaAchievedNational())
                {
                    txtZonalVal_PvASales.setTextColor(Color.GREEN);
                }

                //YOY Sales
                if ((int) salesAnalysis.getYoyNetSalesGrowthPct() > (int) salesAnalysis.getYoyNetSalesGrowthPctZonal() && (int) salesAnalysis.getYoyNetSalesGrowthPct() > (int) salesAnalysis.getYoyNetSalesGrowthPctNational())
                {
                    txtStoreVal_YOYSales.setTextColor(Color.GREEN);

                }
                else if ((int) salesAnalysis.getYoyNetSalesGrowthPctZonal() > (int) salesAnalysis.getYoyNetSalesGrowthPct() && (int) salesAnalysis.getYoyNetSalesGrowthPctZonal() > salesAnalysis.getYoyNetSalesGrowthPctNational())
                {
                    txtZonalVal_YOYSales.setTextColor(Color.GREEN);

                }
                else if ((int) salesAnalysis.getYoyNetSalesGrowthPctNational() > (int) salesAnalysis.getYoyNetSalesGrowthPct() && (int) salesAnalysis.getYoyNetSalesGrowthPctNational() > (int) salesAnalysis.getYoyNetSalesGrowthPctZonal())
                {
                    txtNationalVal_YOYSales.setTextColor(Color.GREEN);
                }

                else if((int)salesAnalysis.getYoyNetSalesGrowthPct() == (int)salesAnalysis.getYoyNetSalesGrowthPctZonal() && (int)salesAnalysis.getYoyNetSalesGrowthPct() == (int)salesAnalysis.getYoyNetSalesGrowthPctNational()
                        && (int)salesAnalysis.getYoyNetSalesGrowthPctZonal() == (int)salesAnalysis.getYoyNetSalesGrowthPctNational() && (int)salesAnalysis.getYoyNetSalesGrowthPctZonal() == (int)salesAnalysis.getYoyNetSalesGrowthPct()
                        && (int)salesAnalysis.getYoyNetSalesGrowthPctNational() == (int)salesAnalysis.getYoyNetSalesGrowthPct() && (int)salesAnalysis.getYoyNetSalesGrowthPctNational() == (int)salesAnalysis.getYoyNetSalesGrowthPctZonal())
                {
                    txtStoreVal_YOYSales.setTextColor(Color.GREEN);

                }
                else if((int)salesAnalysis.getYoyNetSalesGrowthPctZonal() == (int)salesAnalysis.getYoyNetSalesGrowthPctNational() && (int)salesAnalysis.getYoyNetSalesGrowthPct() < (int)salesAnalysis.getYoyNetSalesGrowthPctZonal() && (int)salesAnalysis.getYoyNetSalesGrowthPct() < (int)salesAnalysis.getYoyNetSalesGrowthPctNational())
                {
                    txtZonalVal_YOYSales.setTextColor(Color.GREEN);
                }



                //Sell Thro

                if ((int) salesAnalysis.getSellThruUnits() > (int) salesAnalysis.getSellThruUnitsZonal() && (int) salesAnalysis.getSellThruUnits() > (int) salesAnalysis.getSellThruUnitsNational())
                {
                    txtStoreVal_SellThro.setTextColor(Color.GREEN);


                }
                else if ((int) salesAnalysis.getSellThruUnitsZonal() > (int) salesAnalysis.getSellThruUnits() && (int) salesAnalysis.getSellThruUnitsZonal() > salesAnalysis.getSellThruUnitsNational())
                {
                    txtZonalVal_SellThro.setTextColor(Color.GREEN);

                }
                else if ((int) salesAnalysis.getSellThruUnitsNational() > (int) salesAnalysis.getSellThruUnits() && (int) salesAnalysis.getSellThruUnitsNational() > (int) salesAnalysis.getSellThruUnitsZonal())
                {
                    txtNationalVal_SellThro.setTextColor(Color.GREEN);

                }

                else if((int)salesAnalysis.getSellThruUnits() == (int)salesAnalysis.getSellThruUnitsNational() && (int)salesAnalysis.getSellThruUnits() == (int)salesAnalysis.getSellThruUnitsZonal()
                        && (int)salesAnalysis.getSellThruUnitsZonal() == (int)salesAnalysis.getSellThruUnits() && (int)salesAnalysis.getSellThruUnitsZonal() == (int)salesAnalysis.getSellThruUnitsNational()
                        && (int)salesAnalysis.getSellThruUnitsNational() == (int)salesAnalysis.getSellThruUnits() && (int)salesAnalysis.getSellThruUnitsNational() == (int)salesAnalysis.getSellThruUnitsZonal())
                {
                    txtStoreVal_SellThro.setTextColor(Color.GREEN);

                }
                else if((int)salesAnalysis.getSellThruUnitsZonal() == (int)salesAnalysis.getSellThruUnitsNational() && (int)salesAnalysis.getSellThruUnits() < (int)salesAnalysis.getSellThruUnitsZonal() && (int)salesAnalysis.getSellThruUnits() < (int)salesAnalysis.getSellThruUnitsNational())
                {
                    txtZonalVal_SellThro.setTextColor(Color.GREEN);
                }


                //Mix Sales
                if ((int) salesAnalysis.getMixSales() > (int) salesAnalysis.getMixSalesZonal() && (int) salesAnalysis.getMixSales() > (int) salesAnalysis.getMixsalesNational())
                {
                    txtStoreVal_MixSales.setTextColor(Color.GREEN);

                }
                else if ((int) salesAnalysis.getMixSalesZonal() > (int) salesAnalysis.getMixSales() && (int) salesAnalysis.getMixSalesZonal() > salesAnalysis.getMixsalesNational())
                {
                    txtZonalVal_MixSales.setTextColor(Color.GREEN);

                }
                else if ((int) salesAnalysis.getMixsalesNational() > (int) salesAnalysis.getMixSales() && (int) salesAnalysis.getMixsalesNational() > (int) salesAnalysis.getMixSalesZonal())
                {
                    txtNationalVal_MixSales.setTextColor(Color.GREEN);

                }
                else if((int)salesAnalysis.getMixSales() == (int)salesAnalysis.getMixSalesZonal() && (int)salesAnalysis.getMixSales() == (int)salesAnalysis.getMixsalesNational()
                        && (int)salesAnalysis.getMixSalesZonal() == (int)salesAnalysis.getMixsalesNational() && (int)salesAnalysis.getMixSalesZonal() == (int)salesAnalysis.getMixSales()
                        && (int)salesAnalysis.getMixsalesNational() == (int)salesAnalysis.getMixSales() && (int)salesAnalysis.getMixsalesNational() == (int)salesAnalysis.getMixSalesZonal())
                {
                    txtStoreVal_MixSales.setTextColor(Color.GREEN);

                }
                else if((int)salesAnalysis.getMixSalesZonal() == (int)salesAnalysis.getMixsalesNational() && (int)salesAnalysis.getMixSales() < (int)salesAnalysis.getMixSalesZonal() && (int)salesAnalysis.getMixSales() < (int)salesAnalysis.getMixsalesNational())
                {
                    txtZonalVal_MixSales.setTextColor(Color.GREEN);
                }
            }


            /*
            LinearLayout layout = (LinearLayout) itemView;
            LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
            RelativeLayout relpvasales = (RelativeLayout) layout1.getChildAt(0);
            RelativeLayout relyoysales = (RelativeLayout) layout1.getChildAt(1);

            relpvasales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("PVA Sales", "----");
                    updatelistview("PVA Sales");
                }
            });

            relyoysales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("YOY Sales", "----");
                    updatelistview("YOY Sales");
                }
            });

            LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
            RelativeLayout relsellthrou = (RelativeLayout) layout2.getChildAt(0);
            RelativeLayout relmixsales = (RelativeLayout) layout2.getChildAt(1);

            relsellthrou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Sell Thro U", "----");
                    updatelistview("Sell Thro U");
                }
            });

            relmixsales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Mix Sales", "----");
                    updatelistview("Mix Sales");
                }
            });
            */


        } else if (position == 2) {
            if (salesAnalysis != null) {

                txtSOHVal2.setText(" " + (int) salesAnalysis.getStkOnhandQty());
                txtGITVal.setText(" " + (int) salesAnalysis.getStkGitQty());
                txtROSVal2.setText(" " + String.format("%.1f", salesAnalysis.getRos()));
                txtFwdWkCoverVal2.setText(" " + String.format("%.1f", salesAnalysis.getFwdWeekCover()));
            }

            /*LinearLayout layout = (LinearLayout) itemView;
            LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
            RelativeLayout relsoh2 = (RelativeLayout) layout1.getChildAt(0);
            RelativeLayout relgit = (RelativeLayout) layout1.getChildAt(1);

            relsoh2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("SOH 2", "----");
                    updatelistview("SOH 2");
                }
            });

            relgit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("GIT", "----");
                    updatelistview("GIT");
                }
            });

            LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
            RelativeLayout relros2 = (RelativeLayout) layout2.getChildAt(0);
            RelativeLayout relfwdwkcover2 = (RelativeLayout) layout2.getChildAt(1);

            relros2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ROS 2", "----");
                    updatelistview("ROS 2");
                }
            });

            relfwdwkcover2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Fwd Wk Cover 2", "----");
                    updatelistview("Fwd Wk Cover 2");
                }
            });*/


        }

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("view "," "+v.getParent());
//                LinearLayout layout = (LinearLayout) v;
//                LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
//                RelativeLayout layout2 = (RelativeLayout) layout1.getChildAt(0);
//
//                ViewPager pager = (ViewPager) v.getParent();
//                if(pager.getCurrentItem() == 0)
//                {
//                    Log.e("currentItem==","0"+" " +layout2);
//                    salesadapter = new SalesAnalysisAdapter(arrayList, context, "NetSales");
//                    listView_SalesAnalysis.setAdapter(salesadapter);
//                    salesadapter.notifyDataSetChanged();
//                }
//                else if(pager.getCurrentItem() == 1)
//                {
//                    Log.e("currentItem==","1");
//                }
//                else if(pager.getCurrentItem() == 2)
//                {
//                    Log.e("currentItem==","2");
//                }
//
//
//
//            }
//        });

        vwpagersales.setOnPageChangeListener(this);
        // Add viewpager_item.xml to ViewPager
        vwpagersales.addView(itemView);

        return itemView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
