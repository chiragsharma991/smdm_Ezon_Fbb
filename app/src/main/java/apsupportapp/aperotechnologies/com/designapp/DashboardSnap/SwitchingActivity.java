package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.BORIS.MobileScreenActivity;
import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.BestPerformerActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerLookupActivity;

import apsupportapp.aperotechnologies.com.designapp.ExpiringPromo.ExpiringPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.Feedback.Feedback;
import apsupportapp.aperotechnologies.com.designapp.Feedback.FeedbackList;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.ProductAvailability_notify_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServices;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServicesHO.OurStoreServices_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeRefund;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.PolicyExchangeRefund_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.PricePromotion_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.Price_Promotion;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductQualityRange;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductQualityRangeHO.ProductQualityRange_HO;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisiorStaff;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO.SupervisorStaff_HO;
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence;
import apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit.FGStoreActivity;
import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductPlanActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.Samplebarcode;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_activity;
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionBeginActivity;
import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionHistoryActivity;
import apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions.TargetStockExceptionActivity;
import apsupportapp.aperotechnologies.com.designapp.UpcomingPromo.UpcomingPromo;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualReportActivity;


/**
 * Created by csuthar on 10/07/17.
 */

public class SwitchingActivity extends AppCompatActivity

{
    boolean loginFromFbb;
    public void moveTo(int value, Context context){

        if(loginFromFbb){

            switch (value){

                case 0:
                    Intent StyleActivity = new Intent(context, StyleActivity.class);
                    startActivity(StyleActivity);
                    break;
                case 10:
                    Intent VisualAssortmentActivity = new Intent(context, VisualAssortmentActivity.class);
                    startActivity(VisualAssortmentActivity);
                    break;
                case 11:
                    Intent VisualReportActivity = new Intent(context, VisualReportActivity.class);
                    startActivity(VisualReportActivity);
                    break;
                case 20:
                    Intent SalesAnalysisActivity1 = new Intent(context, SalesAnalysisActivity1.class);
                    startActivity(SalesAnalysisActivity1);
                    break;
                case 21:
                    Intent SalesPvAActivity = new Intent(context, SalesPvAActivity.class);
                    startActivity(SalesPvAActivity);
                    break;
                case 22:
                    Intent KeyProductPlanActivity = new Intent(context,KeyProductPlanActivity.class);
                    startActivity(KeyProductPlanActivity);
                    break;
                case 23:
                    Intent HourlyPerformence = new Intent(context,HourlyPerformence.class);
                    startActivity(HourlyPerformence);
                    break;
                case 30:
                    Intent FreshnessIndexActivity = new Intent(context, FreshnessIndexActivity.class);
                    startActivity(FreshnessIndexActivity);
                    break;
                case 31:
                    Intent OptionEfficiencyActivity = new Intent(context, OptionEfficiencyActivity.class);
                    startActivity(OptionEfficiencyActivity);
                    break;
                case 32:
                    Intent SkewedSizesActivity = new Intent(context, SkewedSizesActivity.class);
                    startActivity(SkewedSizesActivity);
                    break;
                case 33:
                    Intent BestPerformerInventory = new Intent(context, BestPerformerInventory.class);
                    startActivity(BestPerformerInventory);
                    break;
                case 34:
                    Intent StockAgeingActivity = new Intent(context, StockAgeingActivity.class);
                    startActivity(StockAgeingActivity);
                    break;
                case 35:
                    Intent FloorAvailabilityActivity = new Intent(context, FloorAvailabilityActivity.class);
                    startActivity(FloorAvailabilityActivity);
                    break;
                case 36:
                    Intent TargetStockExceptionActivity = new Intent(context, TargetStockExceptionActivity.class);
                    startActivity(TargetStockExceptionActivity);
                    break;
                case 37:
                    Intent SaleThruInventory = new Intent(context,SaleThruInventory.class);
                    startActivity(SaleThruInventory);
                    break;
                   // Hide promo for release build
//                case 40:
//                    Intent RunningPromoActivity = new Intent(context,RunningPromoActivity.class);
//                    startActivity(RunningPromoActivity);
//                    break;
//                case 41:
//                    Intent UpcomingPromo = new Intent(context,UpcomingPromo.class);
//                    startActivity(UpcomingPromo);
//                    break;
//                case 42:
//                    Intent ExpiringPromoActivity = new Intent(context,ExpiringPromoActivity.class);
//                    startActivity(ExpiringPromoActivity);
//                    break;
//                case 43:
//                    Intent BestPerformerActivity = new Intent(context,BestPerformerActivity.class);
//                    startActivity(BestPerformerActivity);
//                    break;

                case 40:
                    Intent To_Do = new Intent(context,To_Do.class);
                    startActivity(To_Do);
                    break;
                case 41:
                    Intent StatusActivity = new Intent(context,StatusActivity.class);
                    startActivity(StatusActivity);
                    break;
                case 50:
                    Intent ProductAvailability_Notify= new Intent(context,ProductAvailability_notify_HO.class);
                    startActivity(ProductAvailability_Notify);
                    break;

                case 51:
                    Intent PolicyExchangeRefund= new Intent(context, PolicyExchangeRefund_HO.class);
                    startActivity(PolicyExchangeRefund);
                    break;
                case 52:
                    Intent Price_Promotion = new Intent(context, PricePromotion_HO.class);
                    startActivity(Price_Promotion);
                    break;
                case 53:
                    Intent ProductQualityRange = new Intent(context, ProductQualityRange_HO.class);
                    startActivity(ProductQualityRange);
                    break;
                case 54:
                    Intent OurStoreServices = new Intent(context,OurStoreServices_HO.class);
                    startActivity(OurStoreServices);
                    break;
                case 55:
                    Intent SupervisiorStaff = new Intent(context, SupervisorStaff_HO.class);
                    startActivity(SupervisiorStaff);
                    break;
                case 60:
                    Intent Feedback = new Intent(context,Feedback.class);
                    startActivity(Feedback);
                    break;
                case 61:
                    Intent FeedbackList = new Intent(context,FeedbackList.class);
                    startActivity(FeedbackList);
                    break;
                case 62:
                    Intent InspectionBeginActivity = new Intent(context,InspectionBeginActivity.class);
                    startActivity(InspectionBeginActivity);
                    break;
                case 63:
                    Intent InspectionHistoryActivity = new Intent(context,InspectionHistoryActivity.class);
                    startActivity(InspectionHistoryActivity);
                    break;
                case 70:
                    Intent mpm_activity = new Intent(context,mpm_activity.class);
                    startActivity(mpm_activity);
                    break;
                case 80:
                    Intent CustomerLookupActivity = new Intent(context,CustomerLookupActivity.class);
                    startActivity(CustomerLookupActivity);
                    break;
                // hide boris module for release build
//                case 90:
//                    Intent MobileScreenActivity = new Intent(context,MobileScreenActivity.class);
//                    startActivity(MobileScreenActivity);
//                    break;

                case 90:
                    Intent FGStoreActivity = new Intent(context, FGStoreActivity.class);
                    startActivity(FGStoreActivity);
                    break;


            }

        }else{

            switch (value){

                case 0:
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
                    break;



            }
    }
    }


    protected List<App> getProduct(int i) {
        List<App> apps = new ArrayList<>();

        switch (i){
            case 0 :
                apps.add(new App("Product Info", R.mipmap.productinfo));
                break;
            case 1 :
                apps.add(new App("Visual Assortment", R.mipmap.visualassortment));
                apps.add(new App("Option Report", R.mipmap.ageingexceptions));
                break;
            case 2 :
                apps.add(new App("Sales", R.mipmap.salesanalysis));
                apps.add(new App("Plan Vs Actual ", R.mipmap.planvsactual));
                apps.add(new App("Key Product PVA", R.mipmap.planvsactual));
                apps.add(new App("Hourly Performance", R.mipmap.hourlyperformance));
                break;

            case 3 :
                apps.add(new App("Freshness Index", R.mipmap.freshnessindex));
                apps.add(new App("Option Efficiency", R.mipmap.optionefficiency));
                apps.add(new App("Skewed Sizes", R.mipmap.skewedsizes));
                apps.add(new App("Best/Worst Performers", R.mipmap.bestworstperformers));
                apps.add(new App("Stock Ageing Exception", R.mipmap.ageingexceptions));
                apps.add(new App("Floor Availability", R.mipmap.flooravailability));
                apps.add(new App("Target Stock Exceptions", R.mipmap.targetstockexceptions));
                apps.add(new App("Sell Thru Exceptions", R.mipmap.sellthruexceptions));
                break;
               //hide promo for release build
//            case 4 :
//                apps.add(new App("Running Promo", R.mipmap.runningpromo));
//                apps.add(new App("Upcoming Promo", R.mipmap.upcomingpromo));
//                apps.add(new App("Expiring Promo", R.mipmap.expiringpromo));
//                apps.add(new App("Best/Worst Promo", R.mipmap.bestworstperformers));
//                break;


            case 4 :
                apps.add(new App("To Do", R.mipmap.stocktransfer));
                apps.add(new App("Status", R.mipmap.stocktransfer));
                break;

            case 5:
                apps.add(new App("Product Availability & Notify Me",R.mipmap.productavailability));
                apps.add(new App("Policy - Exchange Refund",R.mipmap.policyexchangereturn));
                apps.add(new App("Price & Promotion",R.mipmap.priceandpromotion));
                apps.add(new App("Product Quality & Range",R.mipmap.productqualityandrange));
                apps.add(new App("Our Store Services",R.mipmap.ourstoreservices));
                apps.add(new App("Supervisior & Staff",R.mipmap.supervisorandstaff));
                break;


            case 6 :
                apps.add(new App("Best Worst Performer", R.mipmap.feedback));
                apps.add(new App("Best Worst Performer List", R.mipmap.feedbacklist));
                apps.add(new App("Begin Inspection", R.mipmap.storeinspection));
                apps.add(new App("Inspection History", R.mipmap.storeinspection));
                break;
            case 7 :
                apps.add(new App("Season Catalogue", R.mipmap.seasoncatalogue));
                break;
            case 8 :
                apps.add(new App("Customer Engagement", R.mipmap.customerengagement));
                break;
            case 9 :
                apps.add(new App("Service Opportunity Audit", R.mipmap.customerengagement));
                break;
             // hide boris module for release build
//            case 10 :
//                apps.add(new App("Boris",R.mipmap.customerengagement));
//                break;


            // switch for ezone user
            case 21 :
                apps.add(new App("Sales", R.mipmap.salesanalysis));
                apps.add(new App("Plan Vs Actual",R.mipmap.planvsactual));
                apps.add(new App("Hourly Performance", R.mipmap.hourlyperformance));

                break;
            case 22 :
                apps.add(new App("Assortment Analysis", R.mipmap.freshnessindex));
                apps.add(new App("Best/Worst Performers", R.mipmap.bestworstperformers));
                break;
            case 23 :
                apps.add(new App("Customer Engagement", R.mipmap.customerengagement));
                break;
            case 24 :
                apps.add(new App("Hourly Performance", R.mipmap.hourlyperformance));
                break;
            case 25 :
                apps.add(new App("Product Availability & Notify Me",R.mipmap.productavailability));
                apps.add(new App("Policy - Exchange Refund",R.mipmap.policyexchangereturn));
                apps.add(new App("Price & Promotion",R.mipmap.priceandpromotion));
                apps.add(new App("Product Quality & Range",R.mipmap.productqualityandrange));
                apps.add(new App("Our Store Services",R.mipmap.ourstoreservices));
                apps.add(new App("Supervisior & Staff",R.mipmap.supervisorandstaff));
                break;

            default:
                Log.e("TAG", "not found: Activity");
                break;
        }

        return apps;
    }

}

