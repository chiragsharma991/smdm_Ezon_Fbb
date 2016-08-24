package apsupportapp.aperotechnologies.com.designapp;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by pamrutkar on 23/08/16.
 */
public class KeyProductAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public KeyProductAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProductName_Fragment tab1 = new ProductName_Fragment();
                //  Coordinates_Fragment tab1 = new Coordinates_Fragment();
                Log.e("1","tab1");
                return tab1;
            case 1:
                Option_Fragment tab2 = new Option_Fragment();
                //  Coordinates_Fragment tab2 = new Coordinates_Fragment();
                Log.e("2","tab2");
                return tab2;
            case 2:
                Sku_Fragment tab3 = new Sku_Fragment();
                Log.e("3","tab3");

                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
