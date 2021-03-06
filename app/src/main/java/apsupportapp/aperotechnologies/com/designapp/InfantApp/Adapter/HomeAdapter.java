package apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import apsupportapp.aperotechnologies.com.designapp.InfantApp.FragmentCategories;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.FragmentHome;

/**
 * Created by rkanawade on 02/11/17.
 */

public class HomeAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public HomeAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentHome tab1 = new FragmentHome();
                return tab1;
            case 1:
                FragmentCategories tab2 = new FragmentCategories();
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
