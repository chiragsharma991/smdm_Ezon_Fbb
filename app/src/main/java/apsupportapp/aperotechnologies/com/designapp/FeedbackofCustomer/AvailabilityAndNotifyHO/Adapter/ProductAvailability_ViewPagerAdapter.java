package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.ProductAvailability_Feedback;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.ProductAvailability_Reports;

/**
 * Created by rkanawade on 24/07/17.
 */

public class ProductAvailability_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    String storeCode;

    public ProductAvailability_ViewPagerAdapter(FragmentManager fm, int NumOfTabs , String storeCode) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.storeCode = storeCode;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProductAvailability_Reports tab1 = new ProductAvailability_Reports(storeCode);
                return tab1;
            case 1:
                ProductAvailability_Feedback tab2 = new ProductAvailability_Feedback(storeCode);
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
