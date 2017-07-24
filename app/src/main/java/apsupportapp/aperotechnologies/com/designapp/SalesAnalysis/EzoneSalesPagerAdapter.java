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
        ez_currentPage = ez_vwpagersales.getCurrentItem();
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
            txt_ez_NetSalesU.setText("Sales Units");
            txt_ez_SohU.setText("Margin");
            txt_ez_NetSalesName.setText("WOW Gr%");
            txt_ez_PlanSalesName.setText("PvA%");
            txt_ez_NetSalesUName.setText("WOW Gr%");
            txt_ez_MarginName.setText("Margin%");
        }

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
                    if (ez_sales_pager.getYoyNetSalesGrowthPct() <= 0) {
                        txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                      //  txt_ez_NetSalesVal.setTextColor(Color.parseColor("#fe0000"));
                    } else if (ez_sales_pager.getYoyNetSalesGrowthPct() > 0) {
                        txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                        txt_ez_NetSalesVal.setTextColor(Color.parseColor("#70e503"));
                    }
                    if (ez_sales_pager.getPvaAchieved() < 70) {
                        txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                     //   txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#fe0000"));
                    } else if (ez_sales_pager.getPvaAchieved() > 90) {
                        txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                      //  txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#70e503"));
                    } else {
                        txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                    //    txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#ff7e00"));
                    }
                    if (ez_sales_pager.getYoyNetSalesUnitsGrowthPct() <= 0) {
                        txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                    //    txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#fe0000"));
                    } else if (ez_sales_pager.getYoyNetSalesUnitsGrowthPct() > 0) {
                        txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                    //    txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#70e503"));
                    }
                }


            } else if (SalesAnalysisActivity1.ez_segment_val.equals("MTD") || SalesAnalysisActivity1.ez_segment_val.equals("YTD")) {

                if (ez_sales_pager != null) {

                    txt_ez_NetSalesVal.setText("\u20B9\t" + format.format(Math.round(ez_sales_pager.getSaleNetVal())));
                    txt_ez_PlanSalesVal.setText("\u20B9\t" + format.format(Math.round(ez_sales_pager.getPlanSaleNetVal())));
                    txt_ez_NetSalesUVal.setText("" + format.format(Math.round(ez_sales_pager.getSaleTotQty())));
                    txt_ez_SohUVal.setText("" + format.format(Math.round(ez_sales_pager.getStkOnhandQty())));

                    txt_ez_NetSalesPerc.setText("" + Math.round(ez_sales_pager.getYoyNetSalesGrowthPct()) + "%");
                    txt_ez_PlanSalesPerc.setText("" + Math.round(ez_sales_pager.getPvaAchieved()) + "%");
                    txt_ez_NetSalesUPerc.setText("" + Math.round(ez_sales_pager.getYoyNetSalesUnitsGrowthPct()) + "%");
                    txt_ez_MarginPerc.setText("" + Math.round(ez_sales_pager.getMarginPct()) + "%");

                }
                if (ez_sales_pager.getYoyNetSalesGrowthPct() <= 0) {
                    txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                  //  txt_ez_NetSalesVal.setTextColor(Color.parseColor("#fe0000"));

                } else if (ez_sales_pager.getYoyNetSalesGrowthPct() > 0) {
                    txt_ez_NetSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                  //  txt_ez_NetSalesVal.setTextColor(Color.parseColor("#70e503"));
                }

                if (ez_sales_pager.getPvaAchieved() < 70) {
                    txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.red_arrow);
                 //   txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#fe0000"));

                } else if (ez_sales_pager.getPvaAchieved() > 90) {
                    txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.green_arrow);
                  //  txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#70e503"));

                } else {
                    txt_ez_PlanSalesImage.setBackgroundResource(R.mipmap.yellow_arrow);
                  //  txt_ez_PlanSalesVal.setTextColor(Color.parseColor("#ff7e00"));
                }

                if (ez_sales_pager.getYoyNetSalesUnitsGrowthPct() <= 0) {
                    txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.red_arrow);
                  //  txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#fe0000"));
                } else if (ez_sales_pager.getYoyNetSalesUnitsGrowthPct() > 0) {
                    txt_ez_NetSalesUImage.setBackgroundResource(R.mipmap.green_arrow);
                  //  txt_ez_NetSalesUVal.setTextColor(Color.parseColor("#70e503"));
                }

            }
            // Add all values
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
        else if (position == 1) {
            if (ez_sales_pager != null) {
                double ros = Double.parseDouble(String.format("%.1f", ez_sales_pager.getRos()));
                double fwdwkcover = Double.parseDouble(String.format("%.1f", ez_sales_pager.getFwdWeekCover()));

                txt_ez_SOHVal2.setText("" + format.format(Math.round(ez_sales_pager.getStkOnhandQty())));
                txt_ez_GITVal.setText("" + format.format(Math.round(ez_sales_pager.getStkGitQty())));
                txt_ez_ROSVal2.setText("" + format.format(ros));
                txt_ez_FwdWkCoverVal2.setText("" + fwdwkcover);
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
