package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.PricePromotion_Feedback;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.PricePromotion_Reports;

/**
 * Created by rkanawade on 25/07/17.
 */

public class PricePromotion_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    String storeCode;

    public PricePromotion_ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String storeCode) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.storeCode = storeCode;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PricePromotion_Reports tab1 = new PricePromotion_Reports(storeCode);
                return tab1;
            case 1:
                PricePromotion_Feedback tab2 = new PricePromotion_Feedback(storeCode);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
