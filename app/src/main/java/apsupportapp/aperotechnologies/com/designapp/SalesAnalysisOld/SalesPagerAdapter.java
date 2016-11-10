package apsupportapp.aperotechnologies.com.designapp.SalesAnalysisOld;

import android.content.Context;
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

import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by hasai on 20/09/16.
 */
public class SalesPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {


    Context context;
    ArrayList<ProductNameBean> arrayList;
    int focusposition;
    ViewPager vwpagersales;
    LinearLayout lldots;
    SalesAnalysisAdapter salesadapter;
    ListView listView_SalesAnalysis;
    LayoutInflater inflater;
    public static int currentPage = 0;

    // ViewPager 0
    TextView txtNetSalesVal, txtNetSales, txtNetSalesPerc;
    TextView txtPlanSalesVal, txtPlanSales, txtPlanSalesPerc;
    TextView txtNetSalesUVal, txtNetSalesU, txtNetSalesUPerc;
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


    public SalesPagerAdapter(Context context, ArrayList<ProductNameBean> arrayList, int focusposition, ViewPager vwpagersales, LinearLayout lldots, SalesAnalysisAdapter salesadapter, ListView listView_SalesAnalysis) {

        Log.e("in sales adapter", " ---");
        this.context = context;
        this.arrayList = arrayList;
        this.focusposition = focusposition;
        this.vwpagersales = vwpagersales;
        this.lldots = lldots;
        this.salesadapter = salesadapter;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
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
        Log.e("in sales adapter", " 0");

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = null;

        if(position == 0)
        {
            itemView = inflater.inflate(R.layout.child_sales_viewpager, container,
                    false);
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
            txtRosVal0 = (TextView) itemView.findViewById(R.id.txtRosVal0);
            txtRos0 = (TextView) itemView.findViewById(R.id.txtRos0);
            txtFwdWkCoverVal0 = (TextView) itemView.findViewById(R.id.txtFwdWkCoverVal0);
            txtFwdWkCover0 = (TextView) itemView.findViewById(R.id.txtFwdWkCover0);

            if(SalesAnalysisActivity.selectedsegValue.equals("WTD") || SalesAnalysisActivity.selectedsegValue.equals("LW"))
            {
                txtNetSales.setText("Net Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Net Sales(U)");
                txtSohU.setText("S O H(U)");
                txtRos0.setText("ROS");
                txtFwdWkCover0.setText("Fwd Wk Cover");
            }
            else if(SalesAnalysisActivity.selectedsegValue.equals("L4W") || SalesAnalysisActivity.selectedsegValue.equals("YTD"))
            {
                txtNetSales.setText("Avg Wkly Sales");
                txtPlanSales.setText("Plan Sales");
                txtNetSalesU.setText("Avg Wkly Sales(U)");
                txtSohU.setText("S O H(U)");
                txtRos0.setText("Inv Turn");
                txtFwdWkCover0.setText("Velocity");
            }

        }
        else if(position == 1)
        {
            itemView = inflater.inflate(R.layout.child_sales_viewpager1, container,
                    false);

            txtStoreVal_PvASales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtZonalVal_PvASales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtNationalVal_PvASales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtStoreVal_YOYSales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtZonalVal_YOYSales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtNationalVal_YOYSales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtStoreVal_SellThro = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtZonalVal_SellThro = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtNationalVal_SellThro = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtStoreVal_MixSales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtZonalVal_MixSales = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtNationalVal_MixSales = (TextView) itemView.findViewById(R.id.txtSOHVal2);

        }
        else if(position == 2)
        {
            itemView = inflater.inflate(R.layout.child_sales_viewpager2, container,
                    false);

            txtSOHVal2 = (TextView) itemView.findViewById(R.id.txtSOHVal2);
            txtSOH2 = (TextView) itemView.findViewById(R.id.txtSOH2);
            txtGITVal = (TextView) itemView.findViewById(R.id.txtGITVal);
            txtGIT = (TextView) itemView.findViewById(R.id.txtGIT);
            txtROSVal2 = (TextView) itemView.findViewById(R.id.txtROSVal2);
            txtROS2 = (TextView) itemView.findViewById(R.id.txtROS2);
            txtFwdWkCoverVal2 = (TextView) itemView.findViewById(R.id.txtFwdWkCoverVal2);
            txtFwdWkCover2 = (TextView) itemView.findViewById(R.id.txtFwdWkCover2);

        }



        ProductNameBean productNameBean = arrayList.get(focusposition);
        Log.e("in sales pager adapter","");


        if(position == 0)
        {
            txtNetSalesVal.setText(productNameBean.getArticleOption().toLowerCase());
//            txtNetSalesPerc.setText(productNameBean.getProductName());
//            txtPlanSalesVal.setText(productNameBean.getArtileCode());
//            txtPlanSalesPerc.setText(productNameBean.getColor());
//            txtNetSalesUVal.setText(productNameBean.getColor());
//            txtNetSalesUPerc.setText("");
//            txtSohUVal.setText("");
//            txtRosVal0.setText("");
//            txtFwdWkCoverVal0.setText("");


            LinearLayout layout = (LinearLayout) itemView;
            LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
            RelativeLayout relnetsales = (RelativeLayout) layout1.getChildAt(0);
            RelativeLayout relplansales = (RelativeLayout) layout1.getChildAt(1);

            relnetsales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Net Sales","----");
                    updatelistview("Net Sales");
                }
            });

            relplansales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Plan Sales","----");
                    updatelistview("Plan Sales");
                }
            });

            LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
            RelativeLayout relnetsalesu = (RelativeLayout) layout2.getChildAt(0);
            RelativeLayout relplansohu = (RelativeLayout) layout2.getChildAt(1);

            relnetsalesu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Net Sales U","----");
                    updatelistview("Net Sales U");
                }
            });

            relplansohu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("SOH U","----");
                    updatelistview("SOH U");
                }
            });

            LinearLayout layout3 = (LinearLayout) layout.getChildAt(2);
            RelativeLayout relros = (RelativeLayout) layout3.getChildAt(0);
            RelativeLayout relfwdwkcover0 = (RelativeLayout) layout3.getChildAt(1);

            relros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ROS ","----");
                    updatelistview("ROS");
                }
            });

            relfwdwkcover0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Fwd Wk Cover 0","----");
                    updatelistview("Fwd Wk Cover 0");
                }
            });



        }
        else if(position == 1)
        {
//            txtStoreVal_PvASales.setText("");
//            txtZonalVal_PvASales.setText("");
//            txtNationalVal_PvASales.setText("");
//
//            txtStoreVal_YOYSales.setText("");
//            txtZonalVal_YOYSales.setText("");
//            txtNationalVal_YOYSales.setText("");
//
//            txtStoreVal_SellThro.setText("");
//            txtZonalVal_SellThro.setText("");
//            txtNationalVal_SellThro.setText("");
//
//            txtStoreVal_MixSales.setText("");
//            txtZonalVal_MixSales.setText("");
//            txtNationalVal_MixSales.setText("");

            LinearLayout layout = (LinearLayout) itemView;
            LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
            RelativeLayout relpvasales = (RelativeLayout) layout1.getChildAt(0);
            RelativeLayout relyoysales = (RelativeLayout) layout1.getChildAt(1);

            relpvasales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("PVA Sales","----");
                    updatelistview("PVA Sales");
                }
            });

            relyoysales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("YOY Sales","----");
                    updatelistview("YOY Sales");
                }
            });

            LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
            RelativeLayout relsellthrou = (RelativeLayout) layout2.getChildAt(0);
            RelativeLayout relmixsales = (RelativeLayout) layout2.getChildAt(1);

            relsellthrou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Sell Thro U","----");
                    updatelistview("Sell Thro U");
                }
            });

            relmixsales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Mix Sales","----");
                    updatelistview("Mix Sales");
                }
            });


        }
        else if(position == 2)
        {
//            txtSOHVal2.setText(productNameBean.getArticleOption() + "2");
//            txtGITVal.setText("");
//            txtROSVal2.setText("");
//            txtFwdWkCoverVal2.setText("");

            LinearLayout layout = (LinearLayout) itemView;
            LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
            RelativeLayout relsoh2 = (RelativeLayout) layout1.getChildAt(0);
            RelativeLayout relgit = (RelativeLayout) layout1.getChildAt(1);

            relsoh2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("SOH 2","----");
                    updatelistview("SOH 2");
                }
            });

            relgit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("GIT","----");
                    updatelistview("GIT");
                }
            });

            LinearLayout layout2 = (LinearLayout) layout.getChildAt(1);
            RelativeLayout relros2 = (RelativeLayout) layout2.getChildAt(0);
            RelativeLayout relfwdwkcover2 = (RelativeLayout) layout2.getChildAt(1);

            relros2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ROS 2","----");
                    updatelistview("ROS 2");
                }
            });

            relfwdwkcover2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Fwd Wk Cover 2","----");
                    updatelistview("Fwd Wk Cover 2");
                }
            });


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

        ImageView img = (ImageView)  lldots.getChildAt(currentPage);
        img.setImageResource(R.mipmap.dots_unselected);
        currentPage = position;
        ImageView img1 = (ImageView)  lldots.getChildAt(currentPage);
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


    public void updatelistview(String fromwhere)
    {
        salesadapter = new SalesAnalysisAdapter(arrayList, context, fromwhere);
        listView_SalesAnalysis.setAdapter(salesadapter);
        salesadapter.notifyDataSetChanged();
    }
}
