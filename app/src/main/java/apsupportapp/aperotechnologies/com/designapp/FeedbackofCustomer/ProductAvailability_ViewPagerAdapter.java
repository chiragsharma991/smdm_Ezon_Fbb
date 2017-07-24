package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.json.JSONObject;

/**
 * Created by rkanawade on 24/07/17.
 */

public class ProductAvailability_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public ProductAvailability_ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProductAvailability_Reports tab1 = new ProductAvailability_Reports();
                return tab1;
            case 1:
                ProductAvailability_Feedback tab2 = new ProductAvailability_Feedback();
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
