package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerLookupActivity;
import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneBestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneFreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.Feedback.Feedback;
import apsupportapp.aperotechnologies.com.designapp.Feedback.FeedbackList;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.ProductAvailability_notify_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServices;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServicesHO.OurStoreServices_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeRefund;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.PolicyExchangeRefund_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.PricePromotion_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.Price_Promotion;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductAvailability_Notify;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductQualityRange;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductQualityRangeHO.ProductQualityRange_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisiorStaff;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO.SupervisorStaff_HO;
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence;
import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductPlanActivity;
import apsupportapp.aperotechnologies.com.designapp.ListAdapter;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_activity;
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionBeginActivity;
import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionHistoryActivity;
import apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions.TargetStockExceptionActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualReportActivity;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.UpcomingPromo.UpcomingPromo;
import apsupportapp.aperotechnologies.com.designapp.ExpiringPromo.ExpiringPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.BestPerformerActivity;
import apsupportapp.aperotechnologies.com.designapp.BORIS.MobileScreenActivity;

/**
 * Created by csuthar on 10/07/17.
 */

public class SwitchingActivity extends AppCompatActivity

{
    boolean loginFromFbb;
    private Context context = this;
    ListAdapter spinnerArrayAdapter;
    String SelectedStoreCode, storeDescription, from;
    SharedPreferences sharedPreferences;
    String userId, bearertoken,storeCode,geoLevel2Code, lobId, body_geoLevel2Code;
    private AlertDialog dialog;
    ListView select_storeList;
    String auth_code;
    RequestQueue queue;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListbody;
    public void moveTo(String value, Context context){

//        if(loginFromFbb)
//        {



            switch (value) {

                case "001":
                    Intent StyleActivity = new Intent(context, StyleActivity.class);
                    startActivity(StyleActivity);
                    break;
                case "002":
//                    Intent VisualAssortmentActivity = new Intent(context, VisualAssortmentActivity.class);
//                    startActivity(VisualAssortmentActivity);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("VisualAssortmentActivity");
                    break;
                case "003":
//                    Intent VisualReportActivity = new Intent(context, VisualReportActivity.class);
//                    startActivity(VisualReportActivity);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("VisualReportActivity");
                    break;

                case "004":
                    Intent SalesAnalysisActivity1 = new Intent(context, SalesAnalysisActivity1.class);
                    startActivity(SalesAnalysisActivity1);
                    break;
                case "005":
                    Intent SalesPvAActivity = new Intent(context, SalesPvAActivity.class);
                    startActivity(SalesPvAActivity);
                    break;
                case "018":
                    Intent KeyProductPlanActivity = new Intent(context, KeyProductPlanActivity.class);
                    startActivity(KeyProductPlanActivity);
                    break;
                case "028":
                    Intent HourlyPerformence = new Intent(context, HourlyPerformence.class);
                    startActivity(HourlyPerformence);
                    break;
                case "006":
                    Intent FreshnessIndexActivity = new Intent(context, FreshnessIndexActivity.class);
                    startActivity(FreshnessIndexActivity);
                    break;
                case "007":
                    Intent OptionEfficiencyActivity = new Intent(context, OptionEfficiencyActivity.class);
                    startActivity(OptionEfficiencyActivity);
                    break;
                case "008":
                    Intent SkewedSizesActivity = new Intent(context, SkewedSizesActivity.class);
                    startActivity(SkewedSizesActivity);
                    break;
                case "009":
                    Intent BestPerformerInventory = new Intent(context, BestPerformerInventory.class);
                    startActivity(BestPerformerInventory);
                    break;
                case "010":
                    Intent StockAgeingActivity = new Intent(context, StockAgeingActivity.class);
                    startActivity(StockAgeingActivity);
                    break;
                case "011":
                    Intent FloorAvailabilityActivity = new Intent(context, FloorAvailabilityActivity.class);
                    startActivity(FloorAvailabilityActivity);
                    break;
                case "012":
                    Intent TargetStockExceptionActivity = new Intent(context, TargetStockExceptionActivity.class);
                    startActivity(TargetStockExceptionActivity);
                    break;
                case "013":
                    Intent SaleThruInventory = new Intent(context, SaleThruInventory.class);
                    startActivity(SaleThruInventory);
                    break;

                case "014":
                    Intent RunningPromoActivity = new Intent(context, RunningPromoActivity.class);
                    startActivity(RunningPromoActivity);
                    break;
                case "015":
                    Intent UpcomingPromo = new Intent(context, UpcomingPromo.class);
                    startActivity(UpcomingPromo);
                    break;
                case "016":
                    Intent ExpiringPromoActivity = new Intent(context, ExpiringPromoActivity.class);
                    startActivity(ExpiringPromoActivity);
                    break;
                case "017":
                    Intent BestPerformerActivity = new Intent(context, BestPerformerActivity.class);
                    startActivity(BestPerformerActivity);
                    break;

                case "020":
//                    Intent To_Do = new Intent(context, To_Do.class);
//                    startActivity(To_Do);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("To_Do");
                    break;
                case "021":
//                    Intent StatusActivity = new Intent(context, StatusActivity.class);
//                    startActivity(StatusActivity);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("StatusActivity");
                    break;
                case "030":
//                    Intent ProductAvailability_Notify = new Intent(context, ProductAvailability_notify_HO.class);
//                    startActivity(ProductAvailability_Notify);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("ProductAvailability_notify_HO");
                    break;

                case "031":
//                    Intent PolicyExchangeRefund = new Intent(context, PolicyExchangeRefund_HO.class);
//                    startActivity(PolicyExchangeRefund);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("PolicyExchangeRefund_HO");
                    break;
                case "032":
//                    Intent Price_Promotion = new Intent(context, PricePromotion_HO.class);
//                    startActivity(Price_Promotion);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("PricePromotion_HO");
                    break;
                case "033":
//                    Intent ProductQualityRange = new Intent(context, ProductQualityRange_HO.class);
//                    startActivity(ProductQualityRange);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("ProductQualityRange_HO");
                    break;
                case "034":
//                    Intent OurStoreServices = new Intent(context, OurStoreServices_HO.class);
//                    startActivity(OurStoreServices);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("OurStoreServices_HO");
                    break;
                case "035":
//                    Intent SupervisiorStaff = new Intent(context, SupervisorStaff_HO.class);
//                    startActivity(SupervisiorStaff);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("SupervisorStaff_HO");
                    break;
                case "022":
//                    Intent Feedback = new Intent(context, Feedback.class);
//                    startActivity(Feedback);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("Feedback");
                    break;
                case "023":
//                    Intent FeedbackList = new Intent(context, FeedbackList.class);
//                    startActivity(FeedbackList);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("FeedbackList");
                    break;
                case "024":
//                    Intent InspectionBeginActivity = new Intent(context, InspectionBeginActivity.class);
//                    startActivity(InspectionBeginActivity);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("InspectionBeginActivity");
                    break;
                case "025":
//                    Intent InspectionHistoryActivity = new Intent(context, InspectionHistoryActivity.class);
//                    startActivity(InspectionHistoryActivity);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("InspectionHistoryActivity");
                    break;
                case "026":
                    Intent mpm_activity = new Intent(context, mpm_activity.class);
                    startActivity(mpm_activity);
                    break;
                case "027":
                    Intent CustomerLookupActivity = new Intent(context, CustomerLookupActivity.class);
                    startActivity(CustomerLookupActivity);
                    break;
                case "029":
                    Intent MobileScreenActivity = new Intent(context, MobileScreenActivity.class);
                    startActivity(MobileScreenActivity);
                    break;
                case "036":
//                    Intent ProductAvailability_Notify= new Intent(context, apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductAvailability_Notify.class);
//                    startActivity(ProductAvailability_Notify);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("ProductAvailability_Notify");
                    break;
                case "037":
//                    Intent PolicyExchangeRefund= new Intent(context, PolicyExchangeRefund.class);
//                    startActivity(PolicyExchangeRefund);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("PolicyExchangeRefund");
                    break;
                case "038":
//                    Intent Price_Promotion = new Intent(context, Price_Promotion.class);
//                    startActivity(Price_Promotion);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("Price_Promotion");
                    break;
                case "039":
//                    Intent ProductQualityRange = new Intent(context, ProductQualityRange.class);
//                    startActivity(ProductQualityRange);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("ProductQualityRange");
                    break;
                case "040":
//                    Intent OurStoreServices = new Intent(context,OurStoreServices.class);
//                    startActivity(OurStoreServices);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("OurStoreServices");
                    break;
                case "041":
//                    Intent SupervisiorStaff = new Intent(context, SupervisiorStaff.class);
//                    startActivity(SupervisiorStaff);
                    Reusable_Functions.sDialog(context, "Fetching...");
                    commentDialog("SupervisiorStaff");
                    break;

                case "042":
                    SalesAnalysisActivity1 = new Intent(context, EzoneSalesAnalysisActivity1.class);
                    startActivity(SalesAnalysisActivity1);
                    break;
                case "043":
                    SalesPvAActivity = new Intent(context, EzoneSalesPvAActivity.class);
                    startActivity(SalesPvAActivity);
                    break;
                case "044":
                    FreshnessIndexActivity = new Intent(context, EzoneFreshnessIndexActivity.class);
                    startActivity(FreshnessIndexActivity);
                    break;
                case "045":
                    BestPerformerInventory = new Intent(context, EzoneBestPerformerInventory.class);
                    startActivity(BestPerformerInventory);
                    break;


            }
//        }
//        else{

//            switch (value){

              /*  case 0:
                    Intent SalesAnalysisActivity1 = new Intent(context, SalesAnalysisActivity1.class);
                    startActivity(SalesAnalysisActivity1);
                    break;
                case 1:
                    Intent SalesPvAActivity = new Intent(context, SalesPvAActivity.class);
                    startActivity(SalesPvAActivity);
                    break;
                case 10:
                    Intent FreshnessIndexActivity = new Intent(context, FreshnessIndexActivity.class);
                    startActivity(FreshnessIndexActivity);
                    break;
                case 11:
                    Intent BestPerformerInventory = new Intent(context, BestPerformerInventory.class);
                    startActivity(BestPerformerInventory);
                    break;
                case 20:
                    Intent CustomerLookupActivity = new Intent(context,CustomerLookupActivity.class);
                    startActivity(CustomerLookupActivity);
                    break;
                case 2:
                    Intent HourlyPerformence = new Intent(context,HourlyPerformence.class);
                    startActivity(HourlyPerformence);
                    break;


                case 40:
                    Intent ProductAvailability_Notify= new Intent(context, apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductAvailability_Notify.class);
                    startActivity(ProductAvailability_Notify);
                    break;
                case 41:
                    Intent PolicyExchangeRefund= new Intent(context, PolicyExchangeRefund.class);
                    startActivity(PolicyExchangeRefund);
                    break;
                case 42:
                    Intent Price_Promotion = new Intent(context, Price_Promotion.class);
                    startActivity(Price_Promotion);
                    break;
                case 43:
                    Intent ProductQualityRange = new Intent(context, ProductQualityRange.class);
                    startActivity(ProductQualityRange);
                    break;
                case 44:
                    Intent OurStoreServices = new Intent(context,OurStoreServices.class);
                    startActivity(OurStoreServices);
                    break;
                case 45:
                    Intent SupervisiorStaff = new Intent(context, SupervisiorStaff.class);
                    startActivity(SupervisiorStaff);
                    break;*/

//            }
 //       }
    }


    protected List<App> getProduct(int i , List<String> kpiIdArray) {

        List<App> apps = new ArrayList<>();


        switch (i){

            case 0 :
                apps.add(new App("Product Info", R.mipmap.productinfo,"001"));
                break;

            case 1 :

                if (kpiIdArray.contains("002"))
                    apps.add(new App("Visual Assortment", R.mipmap.visualassortment,"002"));

                if (kpiIdArray.contains("003"))
                    apps.add(new App("Option Report", R.mipmap.ageingexceptions,"003"));

                break;

            case 2 :

                if (kpiIdArray.contains("004"))
                    apps.add(new App("Sales", R.mipmap.salesanalysis,"004"));

                if (kpiIdArray.contains("005"))
                    apps.add(new App("Plan Vs Actual ", R.mipmap.planvsactual,"005"));

                if (kpiIdArray.contains("018"))
                    apps.add(new App("Key Product PVA", R.mipmap.planvsactual,"018"));

                if (kpiIdArray.contains("028"))
                    apps.add(new App("Hourly Performance", R.mipmap.hourlyperformance,"028"));

                break;

            case 3 :

                if (kpiIdArray.contains("006"))
                    apps.add(new App("Freshness Index", R.mipmap.freshnessindex,"006"));

                if (kpiIdArray.contains("007"))
                    apps.add(new App("Option Efficiency", R.mipmap.optionefficiency,"007"));

                if (kpiIdArray.contains("008"))
                    apps.add(new App("Skewed Sizes", R.mipmap.skewedsizes,"008"));

                if (kpiIdArray.contains("009"))
                    apps.add(new App("Best/Worst Performers", R.mipmap.bestworstperformers,"009"));

                if (kpiIdArray.contains("010"))
                    apps.add(new App("Stock Ageing Exception", R.mipmap.ageingexceptions,"010"));

                if (kpiIdArray.contains("011"))
                    apps.add(new App("Floor Availability", R.mipmap.flooravailability,"011"));

                if (kpiIdArray.contains("012"))
                    apps.add(new App("Target Stock Exceptions", R.mipmap.targetstockexceptions,"012"));

                if (kpiIdArray.contains("013"))
                    apps.add(new App("Sell Thru Exceptions", R.mipmap.sellthruexceptions,"013"));
                break;


            case 4 :

                if (kpiIdArray.contains("014"))
                    apps.add(new App("Running Promo", R.mipmap.runningpromo,"014"));

                if (kpiIdArray.contains("015"))
                    apps.add(new App("Upcoming Promo", R.mipmap.upcomingpromo,"015"));

                if (kpiIdArray.contains("016"))
                    apps.add(new App("Expiring Promo", R.mipmap.expiringpromo,"016"));

                if (kpiIdArray.contains("017"))
                    apps.add(new App("Best/Worst Promo", R.mipmap.bestworstperformers,"017"));

                break;



            case 5 :

                if (kpiIdArray.contains("020"))
                    apps.add(new App("To Do", R.mipmap.stocktransfer,"020"));

                if (kpiIdArray.contains("021"))
                    apps.add(new App("Status", R.mipmap.stocktransfer,"021"));


                break;


            case 6 :


                if (kpiIdArray.contains("022"))
                    apps.add(new App("Best Worst Performer", R.mipmap.feedback,"022"));

                if (kpiIdArray.contains("023"))
                    apps.add(new App("Best Worst Performer List", R.mipmap.feedbacklist,"023"));

                if (kpiIdArray.contains("024"))
                    apps.add(new App("Begin Inspection", R.mipmap.storeinspection,"024"));

                if (kpiIdArray.contains("025"))
                    apps.add(new App("Inspection History", R.mipmap.storeinspection,"025"));

                break;


            case 7 :

                if (kpiIdArray.contains("026"))
                    apps.add(new App("Season Catalogue", R.mipmap.seasoncatalogue,"026"));

                break;



            case 8 :

                if (kpiIdArray.contains("027"))
                    apps.add(new App("Customer Engagement", R.mipmap.customerengagement,"027"));

                break;

            case 10 :

                if (kpiIdArray.contains("029"))
                    apps.add(new App("Boris",R.mipmap.customerengagement,"029"));

                break;


            case 11 :

                if (kpiIdArray.contains("030"))
                    apps.add(new App("Product Availability & Notify Me",R.mipmap.productavailability,"030"));

                if (kpiIdArray.contains("031"))
                    apps.add(new App("Policy - Exchange Refund",R.mipmap.policyexchangereturn,"031"));

                if (kpiIdArray.contains("032"))
                    apps.add(new App("Price & Promotion",R.mipmap.priceandpromotion,"032"));

                if (kpiIdArray.contains("033"))
                    apps.add(new App("Product Quality & Range",R.mipmap.productqualityandrange,"033"));

                if (kpiIdArray.contains("034"))
                    apps.add(new App("Our Store Services",R.mipmap.ourstoreservices,"034"));

                if (kpiIdArray.contains("035"))
                    apps.add(new App("Supervisior & Staff",R.mipmap.supervisorandstaff,"035"));

                break;

            case 12 :

                if (kpiIdArray.contains("036"))
                    apps.add(new App("Product Availability & Notify Me",R.mipmap.productavailability,"036"));

                if (kpiIdArray.contains("037"))
                    apps.add(new App("Policy - Exchange Refund",R.mipmap.policyexchangereturn,"037"));

                if (kpiIdArray.contains("038"))
                    apps.add(new App("Price & Promotion",R.mipmap.priceandpromotion,"038"));

                if (kpiIdArray.contains("039"))
                    apps.add(new App("Product Quality & Range",R.mipmap.productqualityandrange,"039"));

                if (kpiIdArray.contains("040"))
                    apps.add(new App("Our Store Services",R.mipmap.ourstoreservices,"040"));

                if (kpiIdArray.contains("041"))
                    apps.add(new App("Supervisior & Staff",R.mipmap.supervisorandstaff,"041"));

                break;

            case 13 :

                if (kpiIdArray.contains("042"))
                    apps.add(new App("Sales Ezone", R.mipmap.salesanalysis,"042"));

                if (kpiIdArray.contains("043"))
                    apps.add(new App("PvA Ezone", R.mipmap.planvsactual,"043"));

                break;

            case 14 :

                if (kpiIdArray.contains("044"))
                    apps.add(new App("Assortment Analysis Ezone", R.mipmap.freshnessindex,"044"));

                if (kpiIdArray.contains("045"))
                    apps.add(new App("Best Worst Performer Ezone", R.mipmap.bestworstperformers,"045"));

                break;


            default:
                Log.e("TAG", "not found: Activity");
                break;
        }

        return apps;
    }
    private void commentDialog(String from)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.storecode_list, null);
        select_storeList = (ListView) v.findViewById(R.id.select_storeList);
        // search function for search store code from list.
        final EditText search = (EditText) v.findViewById(R.id.search_store);
        arrayList = new ArrayList<String>();
        arrayListbody = new ArrayList<String>();

        spinnerArrayAdapter = new ListAdapter(arrayList, SwitchingActivity.this);
        select_storeList.setAdapter(spinnerArrayAdapter);
        select_storeList.setTextFilterEnabled(true);
        spinnerArrayAdapter.notifyDataSetChanged();

        requestLoginWithStoreAPI(from);
        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                spinnerArrayAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerArrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                spinnerArrayAdapter.getFilter().filter(s);
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO)) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        builder.setView(v);
        dialog = builder.create();

    }

    private void requestLoginWithStoreAPI(final String from)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");
        geoLevel2Code = sharedPreferences.getString("concept","");
        Log.e("geoLevel2Code:", "" + geoLevel2Code);

        lobId = sharedPreferences.getString("lobid","");
        Log.e("lobId :", "" + lobId);

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        String url = ConstsCore.web_url + "/v1/display/storeselection/" + userId +"?geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
                 //ConstsCore.web_url+ + "/v1/login/userId";  // + "?geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId
        Log.e("url store :", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response "," "+response);
                        Reusable_Functions.hDialog();
                        try {
                            if (response.equals("") || response == null || response.length() == 0 )
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SwitchingActivity.this, "No collection data found", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    storeCode = collectionName.getString("storeCode");
                                    body_geoLevel2Code = collectionName.getString("geoLevel2Code");
                                    arrayList.add(storeCode);
                                    arrayListbody.add(body_geoLevel2Code);

                                }
                                dialog.show();
                            }

//                            Collections.sort(arrayList);
//                            Collections.sort(arrayListbody);
                            Log.e("arrayList "," "+arrayList.toString());
                            Log.e("arrayListbody "," "+arrayListbody.toString());

                            // arrayList.add(0, "Select Storecode");
                            spinnerArrayAdapter.notifyDataSetChanged();
                            select_storeList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {

                                    storeCode = (String) spinnerArrayAdapter.getItem(position);
                                    body_geoLevel2Code = arrayListbody.get(position);
                                    Log.e("storeCode "," "+storeCode);
                                    Log.e("body_geoLevel2Code "," "+body_geoLevel2Code);

                                    dialog.dismiss();
                                    if(from.equals("VisualAssortmentActivity")) {
                                        Intent intent = new Intent(SwitchingActivity.this, VisualAssortmentActivity.class);
                                        intent.putExtra("storeCode", storeCode);
                                        intent.putExtra("body_geoLevel2Code", body_geoLevel2Code);
                                        startActivity(intent);
                                    }
                                    else if(from.equals("VisualReportActivity")){
                                        Intent intent = new Intent(SwitchingActivity.this, VisualReportActivity.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }
                                    else if(from.equals("ProductAvailability_notify_HO")){
                                        Intent intent = new Intent(SwitchingActivity.this, ProductAvailability_notify_HO.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);

                                    }
                                    else if(from.equals("PolicyExchangeRefund_HO")){
                                        Intent intent = new Intent(SwitchingActivity.this, PolicyExchangeRefund_HO.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("OurStoreServices_HO")){
                                        Intent intent = new Intent(SwitchingActivity.this, OurStoreServices_HO.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("ProductQualityRange_HO")){
                                        Intent intent = new Intent(SwitchingActivity.this, ProductQualityRange_HO.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("PricePromotion_HO")){
                                        Intent intent = new Intent(SwitchingActivity.this, PricePromotion_HO.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("SupervisorStaff_HO")){
                                        Intent intent = new Intent(SwitchingActivity.this, SupervisorStaff_HO.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("Feedback")){
                                        Intent intent = new Intent(SwitchingActivity.this, Feedback.class);
                                        intent.putExtra("storeCode", storeCode);
                                        intent.putExtra("body_geoLevel2Code", body_geoLevel2Code);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("FeedbackList")){
                                        Intent intent = new Intent(SwitchingActivity.this, FeedbackList.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("To_Do")){
                                        Intent intent = new Intent(SwitchingActivity.this, To_Do.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("StatusActivity")){
                                        Intent intent = new Intent(SwitchingActivity.this, StatusActivity.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("InspectionBeginActivity")){
                                        Intent intent = new Intent(SwitchingActivity.this, InspectionBeginActivity.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("InspectionHistoryActivity")){
                                        Intent intent = new Intent(SwitchingActivity.this, InspectionHistoryActivity.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("ProductAvailability_Notify")){
                                        Intent intent = new Intent(SwitchingActivity.this, ProductAvailability_Notify.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("PolicyExchangeRefund")){
                                        Intent intent = new Intent(SwitchingActivity.this, PolicyExchangeRefund.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("Price_Promotion")){
                                        Intent intent = new Intent(SwitchingActivity.this, Price_Promotion.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("ProductQualityRange")){
                                        Intent intent = new Intent(SwitchingActivity.this, ProductQualityRange.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("OurStoreServices")){
                                        Intent intent = new Intent(SwitchingActivity.this, OurStoreServices.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }

                                    else if(from.equals("SupervisiorStaff")){
                                        Intent intent = new Intent(SwitchingActivity.this, SupervisiorStaff.class);
                                        intent.putExtra("storeCode", storeCode);
                                        startActivity(intent);
                                    }
                                }
                            });


                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}