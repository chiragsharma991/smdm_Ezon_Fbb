package tna.aperotechnologies.com.tna;

import android.app.ProgressDialog;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hasai on 18/12/15.
 */


//Singleton pattern class. Variables used across multiple activities are used here
public class Configuration_Parameter
{
    private static Configuration_Parameter myObj;
    String MyPREFERENCES = "TNAPrefs" ;
    ProgressDialog pDialog;
    List<String> activitysel = new ArrayList<>();
    String activitySelKey = null;
    String activityselCode = null;
    String OptCodePar = "";
    String OptCodeChild = "Option Code";
    CheckBox lastcheckedBox;
    ArrayList<StructureDataActDet> enteredActDet = new ArrayList<>();
    ArrayList fieldCode = new ArrayList();




    /**
     * Create private constructor
     */
    private Configuration_Parameter()
    {
    }
    /**
     * Create a static method to get instance.
     */
    public static Configuration_Parameter getInstance()
    {
        if(myObj == null)
        {
            myObj = new Configuration_Parameter();
        }
        return myObj;
    }
}
