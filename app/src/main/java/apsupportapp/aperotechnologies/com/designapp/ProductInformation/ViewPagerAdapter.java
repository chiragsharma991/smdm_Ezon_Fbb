package apsupportapp.aperotechnologies.com.designapp.ProductInformation;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String storeCode;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String storeCode) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.storeCode = storeCode;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Details_Fragment tab1 = new Details_Fragment();
                return tab1;
            case 1:
                Style_Fragment tab2 = new Style_Fragment(storeCode);
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