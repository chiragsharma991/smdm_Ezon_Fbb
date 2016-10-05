package apsupportapp.aperotechnologies.com.designapp.VisualAssortment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;

/**
 * Created by mpatil on 19/07/16.
 */
//Adapter for requestant fragments
public class VisualAssortmentPagerAdapter extends FragmentStatePagerAdapter
{

    ArrayList<VisualAssort> visualassortmentlist;
    Context cont;

    //Constructor to the class
    public VisualAssortmentPagerAdapter(Context cont, FragmentManager fm, ArrayList<VisualAssort> visualassortmentlist)
    {
        super(fm);
        this.visualassortmentlist = visualassortmentlist;
        this.cont = cont;
    }

    public VisualAssortmentPagerAdapter(FragmentManager supportFragmentManager)
    {
        super(supportFragmentManager);
    }


    //Overriding method getItem
    @Override
    public Fragment getItem(int position)
    {
        //Returning the current tabs
        VisualAssortmentFragment tab1 = new VisualAssortmentFragment(cont,position,visualassortmentlist);
        return tab1;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount()
    {

        return visualassortmentlist.size();
    }
}