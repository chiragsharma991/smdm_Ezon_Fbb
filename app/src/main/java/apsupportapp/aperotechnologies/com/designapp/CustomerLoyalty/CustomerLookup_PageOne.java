package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerLookup_PageOne extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_custlookup_pageone, null);
        return root;
    }

}
