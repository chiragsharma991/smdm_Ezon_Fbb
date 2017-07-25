package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by rkanawade on 25/07/17.
 */

public class PricePromotion_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PricePromotion_ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PricePromotion_Reports tab1 = new PricePromotion_Reports();
                return tab1;
            case 1:
                PricePromotion_Feedback tab2 = new PricePromotion_Feedback();
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
