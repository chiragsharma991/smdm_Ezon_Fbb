package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by rkanawade on 25/07/17.
 */

public class SupervisorStaff_ViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public SupervisorStaff_ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SupervisorStaff_Reports tab1 = new SupervisorStaff_Reports();
                return tab1;
            case 1:
                SupervisorStaff_Feedback tab2 = new SupervisorStaff_Feedback();
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
