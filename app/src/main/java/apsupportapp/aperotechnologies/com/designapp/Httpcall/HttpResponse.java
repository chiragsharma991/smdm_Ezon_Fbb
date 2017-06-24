package apsupportapp.aperotechnologies.com.designapp.Httpcall;



import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;

/**
 * Created by csuthar on 22/02/17.
 */

public interface HttpResponse
{
     void response(ArrayList<mpm_model> list,int id);

     void nodatafound();

}

