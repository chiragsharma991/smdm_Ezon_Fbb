package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.PolicyExchange_Feedback;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.PolicyExchange_Reports;

/**
 * Created by rkanawade on 24/07/17.
 */

public class PolicyExchange_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PolicyExchange_ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PolicyExchange_Reports tab1 = new PolicyExchange_Reports();
                return tab1;
            case 1:
                PolicyExchange_Feedback tab2 = new PolicyExchange_Feedback();
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
