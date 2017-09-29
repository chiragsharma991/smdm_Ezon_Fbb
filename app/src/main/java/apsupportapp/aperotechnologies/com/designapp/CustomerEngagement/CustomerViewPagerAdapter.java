package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RelativeLayout;

/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
    public  int totalPage=2;
    private String from, storeCode;
    private RelativeLayout imgfilter;

    public CustomerViewPagerAdapter(Context context, FragmentManager fm, String from, String storeCode, RelativeLayout imgfilter) {
        super(fm);
        _context=context;
        this.from = from;
        this.storeCode = storeCode;
        this.imgfilter = imgfilter;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch(position){
            case 0:
                fragment = new CustomerLookup_PageOne(from, storeCode,imgfilter);
                break;
            case 1:
                fragment= new CustomerLookup_PageTwo(imgfilter);
                break;
        }
        return fragment;
    }
    @Override
    public int getCount() {
        return totalPage;
    }

}
