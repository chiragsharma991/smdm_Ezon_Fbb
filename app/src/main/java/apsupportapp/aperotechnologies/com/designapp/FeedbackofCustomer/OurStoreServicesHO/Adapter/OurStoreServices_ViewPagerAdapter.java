package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServicesHO.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServicesHO.OurStoreServices_Feedback;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServicesHO.OurStoreServices_Reports;

/**
 * Created by rkanawade on 25/07/17.
 */

public class OurStoreServices_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    String storeCode;

    public OurStoreServices_ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String storeCode) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.storeCode = storeCode;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OurStoreServices_Reports tab1 = new OurStoreServices_Reports(storeCode);
                return tab1;
            case 1:
                OurStoreServices_Feedback tab2 = new OurStoreServices_Feedback(storeCode);
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
