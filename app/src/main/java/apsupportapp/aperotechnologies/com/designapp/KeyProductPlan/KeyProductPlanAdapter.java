package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;


public class

KeyProductPlanAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    public KeyProductPlanAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               Plan_Product tab1= new Plan_Product();
               return tab1;
            case 1:
                Plan_Option_Fragment tab2= new Plan_Option_Fragment();
                return tab2;
            case 2:
                Plan_SKU_Fragment tab3= new Plan_SKU_Fragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }



    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
