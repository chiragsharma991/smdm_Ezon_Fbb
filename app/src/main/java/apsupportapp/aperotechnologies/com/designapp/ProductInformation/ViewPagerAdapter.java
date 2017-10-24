package apsupportapp.aperotechnologies.com.designapp.ProductInformation;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String storeCode, articleOptionCode, check, content, selStoreName;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String storeCode, String articleOptionCode, String check, String content, String selStoreName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.storeCode = storeCode;
        this.articleOptionCode = articleOptionCode;
        this.check = check;
        this.content = content;
        this.selStoreName = selStoreName;
//        Log.e("", "vwpager adapter articleOptionCode:== "+articleOptionCode.replaceAll(" ", "%20").replaceAll("&", "%26").replaceAll(",", "%2c"));
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Details_Fragment tab1 = new Details_Fragment(selStoreName);
                return tab1;
            case 1:
                Style_Fragment tab2 = new Style_Fragment(storeCode, articleOptionCode, check, content, selStoreName);
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