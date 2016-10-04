package apsupportapp.aperotechnologies.com.designapp.VisualAssortment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by mpatil on 19/07/16.
 */
//Adapter for requestant fragm,ents
public class VisualAssortmentPagerAdapter1 extends FragmentStatePagerAdapter
{

    //integer to count number of tabs
    ArrayList<String> visualassortmentlist;
    Context cont;


    //Constructor to the class
    public VisualAssortmentPagerAdapter1(Context cont, FragmentManager fm, ArrayList<String> visualassortmentlist)
    {
        super(fm);
        //Initializing tab count
        this.visualassortmentlist = visualassortmentlist;
        this.cont = cont;
    }

    public VisualAssortmentPagerAdapter1(FragmentManager supportFragmentManager)
    {
        super(supportFragmentManager);
    }


    //Overriding method getItem
    @Override
    public Fragment getItem(int position)
    {
        //Returning the current tabs
        //VisualAssortmentFragment tab1 = new VisualAssortmentFragment(cont,position,visualassortmentlist);
        //return tab1;
        return null;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount()
    {

        //Log.e("-- "," "+status.size()+ " "+facebookId.size());
        return visualassortmentlist.size();
    }
}