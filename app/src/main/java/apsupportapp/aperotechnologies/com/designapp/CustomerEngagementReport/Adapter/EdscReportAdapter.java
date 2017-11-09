package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.MenbershipRenewalFragment;
import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.RegistrationReportFragment;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.FragmentCategories;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.FragmentHome;

/**
 * Created by pamrutkar on 09/11/17.
 */
public class EdscReportAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public EdscReportAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RegistrationReportFragment tab1 = new RegistrationReportFragment();
                return tab1;
            case 1:
                MenbershipRenewalFragment tab2 = new MenbershipRenewalFragment();
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


