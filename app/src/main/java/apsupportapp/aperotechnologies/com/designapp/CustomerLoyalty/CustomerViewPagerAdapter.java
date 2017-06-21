package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage=2;
    public CustomerViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context=context;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch(position){
            case 0:
                fragment = new CustomerLookup_PageOne();
                break;
            case 1:
                fragment= new CustomerLookup_PageTwo();
                break;
        }
        return fragment;
    }
    @Override
    public int getCount() {
        return totalPage;
    }

}
