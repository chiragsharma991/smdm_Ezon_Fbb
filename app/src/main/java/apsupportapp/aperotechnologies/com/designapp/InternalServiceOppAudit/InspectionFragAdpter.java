package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class InspectionFragAdpter extends FragmentPagerAdapter {


       int tabCount;

        public InspectionFragAdpter(FragmentManager manager, int tabCount) {
            super(manager);
            this.tabCount = tabCount;
        }
 
        @Override
        public Fragment getItem(int position)
        {
//
            switch (position) {
                case 0:
                    InspectionFragment tab1 = new InspectionFragment();
                    return tab1;
                case 1:
                    InspectionFragment1 tab2 = new InspectionFragment1();
                    return tab2;
                case 2:
                    InspectionFragment2 tab3 = new InspectionFragment2();
                    return tab3;
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