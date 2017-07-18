package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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



public class SalesPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    Context context;
    SalesAnalysisListDisplay analysisClass;
    ArrayList<SalesAnalysisViewPagerValue> arrayList;
    int focusposition;
    SalesAnalysisViewPagerValue salesAnalysis;
    ViewPager vwpagersales;
    LinearLayout lldots;
    SalesAnalysisSnapAdapter salesadapter;
    RecyclerView listView_SalesAnalysis;
    LayoutInflater inflater;
    public static int currentPage ;
    SalesPagerAdapter pagerAdapter;


    // ViewPager 0
    ImageView txtNetSalesImage, txtPlanSalesImage, txtNetSalesUImage;
    TextView txtNetSalesVal, txtNetSales, txtNetSalesPerc, txtNetSalesName;
    TextView txtPlanSalesVal, txtPlanSales, txtPlanSalesPerc, txtPlanSalesName;
    TextView txtNetSalesUVal, txtNetSalesU, txtNetSalesUPerc, txtNetSalesUName;
    TextView txtSohUVal, txtSohU;

    TextView txtStoreVal_PvASales1, txtZonalSalesVal, txtNationalSalesVal;
    TextView relPvASales, relYoYSales, relSellThru, relMixSales, relRank;
    TextView txtPvASales_line,txtYoYSales_line,txtSellThru_line,txtMixSales_line,txtRank_line;
    TextView txtPvAZonalRankVal, txtPvANationalRankVal, txtYoYZonalRankVal, txtYoYNationalRankVal;
    RelativeLayout sznchildRelLayout, rankRelLayout;
    LinearLayout linPvAZonalRank,linPvANationalRank,linYoYZonalRank,linYoYNationalRank,pmsyrtxtRelLayout;

    // ViewPager 2
    TextView txtSOHVal2, txtSOH2;
    TextView txtGITVal, txtGIT;
    TextView txtROSVal2, txtROS2;
    TextView txtFwdWkCoverVal2, txtFwdWkCover2;

    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    String fromWhere;

    public SalesPagerAdapter(Context context, ArrayList<SalesAnalysisViewPagerValue> arrayList, int focusposition, ViewPager vwpagersales, LinearLayout lldots, SalesAnalysisSnapAdapter salesadapter, RecyclerView listView_SalesAnalysis, ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList, String fromWhere, SalesPagerAdapter pagerAdapter) {

        this.context = context;
        this.arrayList = arrayList;
        this.focusposition = focusposition;
        this.vwpagersales = vwpagersales;
        this.lldots = lldots;
        this.salesAnalysisClassArrayList = salesAnalysisClassArrayList;
        this.fromWhere = fromWhere;
        this.pagerAdapter = pagerAdapter;
        this.salesadapter = salesadapter;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
        if (arrayList.size() != 0) {
            salesAnalysis = arrayList.get(0);
        }
        currentPage = vwpagersales.getCurrentItem();
    }

    public SalesPagerAdapter() {

    }

    @Override
    public int getCount() {
        return 3;
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

            txtNetSalesPerc = (TextView) itemView.findViewById(R.id.txtNetSalesPerc);
            txtPlanSalesPerc = (TextView) itemView.findViewById(R.id.txtPlanSalesPerc);
            txtNetSalesUPerc = (TextView) itemView.findViewById(R.id.txtNetSalesUPerc);
            txtNetSalesName = (TextView) itemView.findViewById(R.id.txtNetSalesName);
            txtPlanSalesName = (TextView) itemView.findViewById(R.id.txtPlanSalesName);
            txtNetSalesUName = (TextView) itemView.findViewById(R.id.txtNetSalesUName);

            if (SalesAnalysisActivity1.selectedsegValue.equals("WTD") || SalesAnalysisActivity1.selectedsegValue.equals("LW")) {

                txtNetSales.setText("Net Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Sales Units");
                txtSohU.setText("SOH Units");

                txtNetSalesName.setText("WOW Gr%");
                txtPlanSalesName.setText("PvA%");
                txtNetSalesUName.setText("WOW Gr%");


            } else if (SalesAnalysisActivity1.selectedsegValue.equals("L4W") || SalesAnalysisActivity1.selectedsegValue.equals("STD")) {

                txtNetSales.setText("Net Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Sales Units");
                txtSohU.setText("SOH Units");

                txtNetSalesName.setText("YOY Gr%");
                txtPlanSalesName.setText("PvA%");
                txtNetSalesUName.setText("YOY Gr%");

            }

        } else if (position == 1) {
            itemView = inflater.inflate(R.layout.child_sales_pager1, container,
                    false);
            txtStoreVal_PvASales1 = (TextView) itemView.findViewById(R.id.txtStoreVal_PvASales1);
            txtZonalSalesVal = (TextView) itemView.findViewById(R.id.txtZonalSalesVal);
            txtNationalSalesVal = (TextView) itemView.findViewById(R.id.txtNationalSalesVal);
            relPvASales = (TextView) itemView.findViewById(R.id.relPvASales);
            relYoYSales = (TextView) itemView.findViewById(R.id.relYoYSales);
            relSellThru = (TextView) itemView.findViewById(R.id.relSellThru);
            relMixSales = (TextView) itemView.findViewById(R.id.relMixSales);
            relRank = (TextView) itemView.findViewById(R.id.relRank);
            txtPvASales_line = (TextView)itemView.findViewById(R.id.txtPvASales_line);
            txtYoYSales_line = (TextView)itemView.findViewById(R.id.txtYoYSales_line);
            txtSellThru_line = (TextView)itemView.findViewById(R.id.txtSellThru_line);
            txtMixSales_line = (TextView)itemView.findViewById(R.id.txtMixSales_line);
            txtRank_line = (TextView)itemView.findViewById(R.id.txtRank_line);
            //Rank Layout
            txtPvAZonalRankVal = (TextView) itemView.findViewById(R.id.txt_sales_zone_val);
            txtPvANationalRankVal = (TextView) itemView.findViewById(R.id.txt_sales_national_val);
            txtYoYZonalRankVal = (TextView) itemView.findViewById(R.id.txt_growth_zone_val);
            txtYoYNationalRankVal = (TextView) itemView.findViewById(R.id.txt_growth_national_val);
            sznchildRelLayout = (RelativeLayout) itemView.findViewById(R.id.sznchildRelLayout);
            sznchildRelLayout.setVisibility(View.VISIBLE);
            rankRelLayout = (RelativeLayout) itemView.findViewById(R.id.rankRelLayout);
            rankRelLayout.setVisibility(View.GONE);
            pmsyrtxtRelLayout = (LinearLayout) itemView.findViewById(R.id.pmsyrtxtRelLayout);
            linPvAZonalRank = (LinearLayout) itemView.findViewById(R.id.linear_sales_zone);
            linPvANationalRank = (LinearLayout) itemView.findViewById(R.id.linear_sales_national);
            linYoYZonalRank = (LinearLayout) itemView.findViewById(R.id.linear_growth_zone);
            linYoYNationalRank = (LinearLayout) itemView.findViewById(R.id.linear_growth_national);

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

                if (salesAnalysis != null)
                {
                    txtNetSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getSaleNetVal())));
                    txtNetSalesVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtPlanSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getPlanSaleNetVal())));
                    txtPlanSalesVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtNetSalesUVal.setText("" + format.format(Math.round(salesAnalysis.getSaleTotQty())));
                    txtNetSalesUVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtSohUVal.setText("" + format.format(Math.round(salesAnalysis.getStkOnhandQty())));
                    txtSohUVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtNetSalesPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) + "%");
                    txtPlanSalesPerc.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                    txtNetSalesUPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesUnitsGrowthPct()) + "%");

                    // Color Condition for Wow Net Sale, Pva Achieved , Wow net sale Growth
                    if (salesAnalysis.getWowNetSalesGrowthPct() <= 0)
                    {
                        txtNetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                        txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);

                    } else if (salesAnalysis.getWowNetSalesGrowthPct() > 0) {
                        txtNetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);

                    }
                    if (salesAnalysis.getPvaAchieved() < 70)
                    {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                        txtPlanSalesPerc.setTypeface(Typeface.DEFAULT_BOLD);

                    }
                    else if (salesAnalysis.getPvaAchieved() > 90) {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txtPlanSalesPerc.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    else
                    {
                        txtPlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                        txtPlanSalesPerc.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    if (salesAnalysis.getWowNetSalesUnitsGrowthPct() <= 0) {
                        txtNetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                        txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    else if (salesAnalysis.getWowNetSalesUnitsGrowthPct() > 0) {
                        txtNetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                        txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);

                    }
            }


            } else if (SalesAnalysisActivity1.selectedsegValue.equals("L4W") || SalesAnalysisActivity1.selectedsegValue.equals("STD")) {

                if (salesAnalysis != null) {

                    txtNetSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getSaleNetVal())));
                    txtNetSalesVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtPlanSalesVal.setText("\u20B9\t" + format.format(Math.round(salesAnalysis.getPlanSaleNetVal())));
                    txtPlanSalesVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtNetSalesUVal.setText("" + format.format(Math.round(salesAnalysis.getSaleTotQty())));
                    txtNetSalesUVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtSohUVal.setText("" + format.format(Math.round(salesAnalysis.getStkOnhandQty())));
                    txtSohUVal.setTypeface(Typeface.DEFAULT_BOLD);
                    txtNetSalesPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) + "%");
                    txtPlanSalesPerc.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                    txtNetSalesUPerc.setText("" + Math.round(salesAnalysis.getYoyNetSalesUnitsGrowthPct()) + "%");
                }
                if (salesAnalysis.getWowNetSalesGrowthPct() <= 0)
                {
                    txtNetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);

                } else if (salesAnalysis.getWowNetSalesGrowthPct() > 0) {
                    txtNetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);

                }
                if (salesAnalysis.getPvaAchieved() < 70)
                {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                    txtPlanSalesPerc.setTypeface(Typeface.DEFAULT_BOLD);

                }
                else if (salesAnalysis.getPvaAchieved() > 90) {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                    txtPlanSalesPerc.setTypeface(Typeface.DEFAULT_BOLD);
                }
                else
                {
                    txtPlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                    txtPlanSalesPerc.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (salesAnalysis.getWowNetSalesUnitsGrowthPct() <= 0) {
                    txtNetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                    txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);
                }
                else if (salesAnalysis.getWowNetSalesUnitsGrowthPct() > 0) {
                    txtNetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                    txtNetSalesUPerc.setTypeface(Typeface.DEFAULT_BOLD);

                }
            }
            if(salesAnalysisClassArrayList.size()!=0){
            SalesAnalysisListDisplay salesAnalysisListDisplay = salesAnalysisClassArrayList.get(position);

            if (salesAnalysisListDisplay.getPlanDept() != null) {
                if (salesAnalysisListDisplay.getPlanDept().equals("All")) {
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    salesadapter.notifyDataSetChanged();

                }
            }
            if (salesAnalysisListDisplay.getPlanCategory() != null) {
                if (salesAnalysisListDisplay.getPlanCategory().equals("All")) {
                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    salesadapter.notifyDataSetChanged();

                }
            }

            if (salesAnalysisListDisplay.getPlanClass() != null) {
                if (salesAnalysisListDisplay.getPlanClass().equals("All")) {

                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    salesadapter.notifyDataSetChanged();

                }
            }
            if (salesAnalysisListDisplay.getBrandName() != null) {
                if (salesAnalysisListDisplay.getBrandName().equals("All")) {

                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
                    salesadapter.notifyDataSetChanged();

                }
            }
            if (salesAnalysisListDisplay.getBrandplanClass() != null) {
                if (salesAnalysisListDisplay.getBrandplanClass().equals("All")) {

                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysis.getPvaAchieved());
                    salesAnalysisClassArrayList.set(0, salesAnalysisListDisplay);
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

                txtPvASales_line.setBackgroundColor(Color.parseColor("#e8112d"));
                txtYoYSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                txtSellThru_line.setBackgroundColor(Color.parseColor("#ffffff"));
                txtMixSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                txtRank_line.setBackgroundColor(Color.parseColor("#ffffff"));

                if (Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedNational())) {

                   txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                    txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchievedNational())) {

                    txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                    txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                } else if (Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchievedZonal())) {

                    txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtNationalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));

                } else if (Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedNational())
                        && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchieved())
                        && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchievedZonal())) {

                    txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                    txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));


                } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedNational())) {

                    txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                    txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                }


                //PvA Sales
                relPvASales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pmsyrtxtRelLayout.setTag("PvA%");
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        txtPvASales_line.setBackgroundColor(Color.parseColor("#e8112d"));
                        txtYoYSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtSellThru_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtMixSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtRank_line.setBackgroundColor(Color.parseColor("#ffffff"));

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getPvaAchieved()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getPvaAchievedZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getPvaAchievedNational()) + "%");

                        if (Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) > Math.round(salesAnalysis.getPvaAchievedNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedZonal()) > Math.round(salesAnalysis.getPvaAchievedNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) > Math.round(salesAnalysis.getPvaAchievedZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));

                        } else if (Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedNational())
                                && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchieved())
                                && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) == Math.round(salesAnalysis.getPvaAchievedZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));


                        } else if (Math.round(salesAnalysis.getPvaAchievedZonal()) == Math.round(salesAnalysis.getPvaAchievedNational()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchieved()) < Math.round(salesAnalysis.getPvaAchievedNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                        }
                        else if (Math.round(salesAnalysis.getPvaAchieved()) == Math.round(salesAnalysis.getPvaAchievedZonal()) && Math.round(salesAnalysis.getPvaAchievedNational()) < Math.round(salesAnalysis.getPvaAchieved()) && Math.round(salesAnalysis.getPvaAchievedNational()) < Math.round(salesAnalysis.getPvaAchievedZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        }


                    }
                });
                //YoY Sales
                relYoYSales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        txtPvASales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtYoYSales_line.setBackgroundColor(Color.parseColor("#e8112d"));
                        txtSellThru_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtMixSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtRank_line.setBackgroundColor(Color.parseColor("#ffffff"));

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) + "%");

                        //YOY Sales
                        if (Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) > Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));

                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())
                                && Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPct())
                                && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));


                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational())) {


                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));


                        } else if (Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) == Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPct()) && Math.round(salesAnalysis.getYoyNetSalesGrowthPctNational()) < Math.round(salesAnalysis.getYoyNetSalesGrowthPctZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        }


                    }
                });

                //Sell Thru
                relSellThru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        txtPvASales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtYoYSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtSellThru_line.setBackgroundColor(Color.parseColor("#e8112d"));
                        txtMixSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtRank_line.setBackgroundColor(Color.parseColor("#ffffff"));

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getSellThruUnits()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getSellThruUnitsZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getSellThruUnitsNational()) + "%");


                        //Sell Thro
                        if (Math.round(salesAnalysis.getSellThruUnits()) > Math.round(salesAnalysis.getSellThruUnitsZonal()) && Math.round(salesAnalysis.getSellThruUnits()) > Math.round(salesAnalysis.getSellThruUnitsNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));


                        } else if (Math.round(salesAnalysis.getSellThruUnitsZonal()) > Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsZonal()) > Math.round(salesAnalysis.getSellThruUnitsNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getSellThruUnitsNational()) > Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsNational()) > Math.round(salesAnalysis.getSellThruUnitsZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));

                        } else if (Math.round(salesAnalysis.getSellThruUnits()) == Math.round(salesAnalysis.getSellThruUnitsNational()) && Math.round(salesAnalysis.getSellThruUnits()) == Math.round(salesAnalysis.getSellThruUnitsZonal())
                                && Math.round(salesAnalysis.getSellThruUnitsZonal()) == Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsZonal()) == Math.round(salesAnalysis.getSellThruUnitsNational())
                                && Math.round(salesAnalysis.getSellThruUnitsNational()) == Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsNational()) == Math.round(salesAnalysis.getSellThruUnitsZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getSellThruUnitsZonal()) == Math.round(salesAnalysis.getSellThruUnitsNational()) && Math.round(salesAnalysis.getSellThruUnits()) < Math.round(salesAnalysis.getSellThruUnitsZonal()) && Math.round(salesAnalysis.getSellThruUnits()) < Math.round(salesAnalysis.getSellThruUnitsNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getSellThruUnits()) == Math.round(salesAnalysis.getSellThruUnitsZonal()) && Math.round(salesAnalysis.getSellThruUnitsNational()) < Math.round(salesAnalysis.getSellThruUnits()) && Math.round(salesAnalysis.getSellThruUnitsNational()) < Math.round(salesAnalysis.getSellThruUnitsZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        }

                    }
                });

                //Mix Sales
                relMixSales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sznchildRelLayout.setVisibility(View.VISIBLE);
                        rankRelLayout.setVisibility(View.GONE);

                        txtPvASales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtYoYSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtSellThru_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtMixSales_line.setBackgroundColor(Color.parseColor("#e8112d"));
                        txtRank_line.setBackgroundColor(Color.parseColor("#ffffff"));

                        txtStoreVal_PvASales1.setText("" + Math.round(salesAnalysis.getMixSales()) + "%");
                        txtZonalSalesVal.setText("" + Math.round(salesAnalysis.getMixSalesZonal()) + "%");
                        txtNationalSalesVal.setText("" + Math.round(salesAnalysis.getMixsalesNational()) + "%");

                        //Mix Sales
                        if (Math.round(salesAnalysis.getMixSales()) > Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixSales()) > Math.round(salesAnalysis.getMixsalesNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getMixSalesZonal()) > Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixSalesZonal()) > Math.round(salesAnalysis.getMixsalesNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getMixsalesNational()) > Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixsalesNational()) > Math.round(salesAnalysis.getMixSalesZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));

                        } else if (Math.round(salesAnalysis.getMixSales()) == Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixSales()) == Math.round(salesAnalysis.getMixsalesNational())
                                && Math.round(salesAnalysis.getMixSalesZonal()) == Math.round(salesAnalysis.getMixsalesNational()) && Math.round(salesAnalysis.getMixSalesZonal()) == Math.round(salesAnalysis.getMixSales())
                                && Math.round(salesAnalysis.getMixsalesNational()) == Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixsalesNational()) == Math.round(salesAnalysis.getMixSalesZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getMixSalesZonal()) == Math.round(salesAnalysis.getMixsalesNational()) && Math.round(salesAnalysis.getMixSales()) < Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixSales()) < Math.round(salesAnalysis.getMixsalesNational())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#70e503"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        } else if (Math.round(salesAnalysis.getMixSales()) == Math.round(salesAnalysis.getMixSalesZonal()) && Math.round(salesAnalysis.getMixsalesNational()) < Math.round(salesAnalysis.getMixSales()) && Math.round(salesAnalysis.getMixsalesNational()) < Math.round(salesAnalysis.getMixSalesZonal())) {

                            txtStoreVal_PvASales1.setBackgroundColor(Color.parseColor("#70e503"));
                            txtZonalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));
                            txtNationalSalesVal.setBackgroundColor(Color.parseColor("#ff7e00"));

                        }

                    }
                });

                //Rank
                relRank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankRelLayout.setVisibility(View.VISIBLE);
                        sznchildRelLayout.setVisibility(View.GONE);

                        txtPvASales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtYoYSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtSellThru_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtMixSales_line.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtRank_line.setBackgroundColor(Color.parseColor("#e8112d"));

                        txtPvAZonalRankVal.setText("" + salesAnalysis.getZonalSalesRank());
                        txtPvANationalRankVal.setText("" + salesAnalysis.getNationalSalesRank());
                        txtYoYZonalRankVal.setText("" + salesAnalysis.getZonalYOYGrowthRank());
                        txtYoYNationalRankVal.setText("" + salesAnalysis.getNationalYOYGrowthRank());

                        //Sales Rank Condtion
                        if (salesAnalysis.getZonalSalesRank() > salesAnalysis.getNationalSalesRank())
                        {
                            linPvAZonalRank.setBackgroundResource(R.color.smdm_green);
                            linPvANationalRank.setBackgroundResource(R.color.smdm_amber);
                        }
                        else if (salesAnalysis.getNationalSalesRank() > salesAnalysis.getZonalSalesRank())
                        {
                            linPvAZonalRank.setBackgroundResource(R.color.smdm_amber);
                            linPvANationalRank.setBackgroundResource(R.color.smdm_green);
                        }
                        else if (salesAnalysis.getZonalSalesRank() == salesAnalysis.getNationalSalesRank())
                        {
                            linPvAZonalRank.setBackgroundResource(R.color.smdm_green);
                            linPvANationalRank.setBackgroundResource(R.color.smdm_amber);
                        }
                        //YoY Rank Condition
                        if (salesAnalysis.getZonalYOYGrowthRank() > salesAnalysis.getNationalYOYGrowthRank())
                        {
                            linYoYZonalRank.setBackgroundResource(R.color.smdm_green);
                            linYoYNationalRank.setBackgroundResource(R.color.smdm_amber);
                        }
                        else if (salesAnalysis.getNationalYOYGrowthRank() > salesAnalysis.getZonalYOYGrowthRank())
                        {
                            linYoYZonalRank.setBackgroundResource(R.color.smdm_amber);
                            linYoYNationalRank.setBackgroundResource(R.color.smdm_green);
                        }
                        else if (salesAnalysis.getZonalYOYGrowthRank() == salesAnalysis.getNationalYOYGrowthRank())
                        {
                            linYoYZonalRank.setBackgroundResource(R.color.smdm_green);
                            linYoYNationalRank.setBackgroundResource(R.color.smdm_amber);

                        }
                    }
                });
            }
        }
        else if (position == 2)
        {
            if (salesAnalysis != null) {
               // String ros = format.format(salesAnalysis.getRos());
                double ros = Double.parseDouble(String.format("%.1f", salesAnalysis.getRos()));
                double fwdwkcover = Double.parseDouble(String.format("%.1f", salesAnalysis.getFwdWeekCover()));
                txtSOHVal2.setText(" " + format.format(Math.round(salesAnalysis.getStkOnhandQty())));
                txtSOHVal2.setTypeface(Typeface.DEFAULT_BOLD);
                txtGITVal.setText(" " + format.format(Math.round(salesAnalysis.getStkGitQty())));
                txtGITVal.setTypeface(Typeface.DEFAULT_BOLD);
                txtROSVal2.setText(" "+ format.format(ros));
                txtROSVal2.setTypeface(Typeface.DEFAULT_BOLD);
                txtFwdWkCoverVal2.setText(" " + fwdwkcover);
                txtFwdWkCoverVal2.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }
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
        (container).removeView((LinearLayout) object);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
