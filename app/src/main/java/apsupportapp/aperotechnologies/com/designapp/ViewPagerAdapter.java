package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by ifattehkhan on 22/08/16.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Details_Fragment tab1 = new Details_Fragment();
              //  Coordinates_Fragment tab1 = new Coordinates_Fragment();
                Log.e("1","tab1");
                return tab1;
            case 1:
                Style_Fragment tab2 = new Style_Fragment();
              //  Coordinates_Fragment tab2 = new Coordinates_Fragment();
                Log.e("2","tab2");
                return tab2;
            case 2:
                Coordinate_Fragment tab3 = new Coordinate_Fragment();
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