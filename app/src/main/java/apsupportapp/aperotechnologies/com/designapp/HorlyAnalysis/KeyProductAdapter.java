package apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;


public class KeyProductAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    public KeyProductAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProductName_Fragment tab1 = new ProductName_Fragment();
                return tab1;
            case 1:
                Option_Fragment tab2 = new Option_Fragment();
                return tab2;
            case 2:
                Sku_Fragment tab3 = new Sku_Fragment();
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

    @Nullable
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
