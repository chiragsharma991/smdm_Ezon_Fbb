package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by rkanawade on 10/10/17.
 */

public class ReportsAdapter extends FragmentPagerAdapter {

    int tabCount;

    public ReportsAdapter(FragmentManager manager, int tabCount) {
        super(manager);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position)
    {
//
        switch (position) {
            case 0:
                FGStoreFragment tab1 = new FGStoreFragment();
                return tab1;
            case 1:
                CompetitorStoreFragment tab2 = new CompetitorStoreFragment();
                return tab2;
            default:
                return null;

        }

//            return InspectionFragment.newInstance(position);

    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
