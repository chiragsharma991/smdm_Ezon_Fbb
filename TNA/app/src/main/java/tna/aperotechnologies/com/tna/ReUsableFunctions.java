package tna.aperotechnologies.com.tna;

/**
 * Created by hasai on 18/12/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasai on 22/12/15.
 */

//Contains some functions which are used in multiple activities
public  class ReUsableFunctions
{
    Configuration_Parameter m_config=Configuration_Parameter.getInstance();
    DBHelper helper;
    String tna[];
    String[] tna_id,tna_desc;
    String[] tna_Selection,tna_datechange;
    String[] act_id,act_desc;
    String[] opt_par_val;
    String[] act_checked;
    RequestQueue queue;
    Context context;
    String TNA_Exists;
    SharedPreferences sharedpreferences;

    //function for TNAs Settings and Filters
    public void requesttnas(final String URL,Context cont)
    {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(cont);
        context=cont;
        helper = new DBHelper(context);
        final SQLiteDatabase db = helper.getWritableDatabase();
        queue = Volley.newRequestQueue(context);
        String tna_URL;
        String UserId = String.valueOf(sharedpreferences.getInt("UserId",0));
        tna_URL = URL+"/api/tna/"+UserId+"/vendors";

        final StringRequest postRequest = new StringRequest(Request.Method.GET, tna_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        /*if (response.contains("{") && response.contains("$id"))
                        {
                            new Processtnas().execute(response);
                        }*/

                        new Processtnas().execute(response);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // hidepDialog();
                        if (m_config.pDialog.isShowing()){
                            m_config.pDialog.dismiss();
                        }

                        db.close();

                        error.printStackTrace();
                        // TODO Auto-generated method stub

                        // Log.i("ERROR","error => "+error.toString());// + " Stats Code " + errorcode +"");

                        if(error instanceof TimeoutError)
                        {
                            Toast.makeText(context, "Server Not Available", Toast.LENGTH_LONG).show();
                        }

                        else if ( error instanceof NoConnectionError ||error instanceof NetworkError)
                        {
                            //TODO
                            // Log.i("error.getMessage()", error.getMessage().toString() + "");
                            if(error.getMessage().toString().contains("No authentication challenges found"))
                            {
                                Toast.makeText(context,"Invalid Login Credentials",Toast.LENGTH_LONG).show();
                            }
                            else if(error.getMessage().toString().contains("UnknownHostException"))
                            {
                                Toast.makeText(context,"Invalid Web API",Toast.LENGTH_LONG).show();
                            }
                            else if(error.getMessage().toString().contains("ConnectException") || error.getMessage().toString().contains("SocketException") )
                            {
                                if(error.getMessage().toString().contains("Connection refused")) {
                                    Toast.makeText(context,"Invalid Web API",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(context,"Failed to Connect. Server not availble",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Check Your Network Connection",Toast.LENGTH_LONG).show();
                            }
                        }
                        else if (error instanceof AuthFailureError)
                        {
                            //TODO
                            Toast.makeText(context,"Invalid Login Credentials",Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof ServerError)
                        {
                            //TODO
                            Toast.makeText(context,"Server Not Available",Toast.LENGTH_LONG).show();
                        }
                        else if(error instanceof VolleyError)
                        {
                            if (error.getMessage().toString().contains("Bad URL")) {
                                Toast.makeText(context, "Invalid Web API", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //  Log.i("Auth Code from tnas 22",sharedpreferences.getString(m_config.Auth_Code,"Auth"));
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("Authorization", sharedpreferences.getString(m_config.Auth_Code, "Auth_Code"));
                return params;

            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    class Processtnas extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            helper = new DBHelper(context);
            SQLiteDatabase sqldb ;
            String response = params[0];
            //Log.i("tna Response", response);
            sqldb = helper.getWritableDatabase();
            sqldb.execSQL("Delete from Activitys");
            sqldb.execSQL("Delete from OptionCodes");


            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                JSONObject jsonObj = jsonArray.getJSONObject(0);
                JSONArray TNAarray = (JSONArray) jsonObj.get("TNA");
                //Log.e("jsonArray "," "+jsonArray);
                //Log.e("TNAarray "," ------  "+TNAarray);
                tna_id=new String[TNAarray.length()];
                tna_desc=new String[TNAarray.length()];
                tna_Selection=new String[TNAarray.length()];
                tna_datechange=new String[TNAarray.length()];

                for(int i = 0; i< TNAarray.length(); i++){
                    JSONObject TNAobj = TNAarray.getJSONObject(i);
                    tna_id[i] = TNAobj.getString("TNAId");
                    tna_desc[i] = TNAobj.getString("Description");
                    tna_Selection[i] = TNAobj.getString("IsSelected");
                    tna_datechange[i] = TNAobj.getString("IsDateChangeable");
                    //Log.e("TNAobj","--- "+TNAobj);
                    //Log.e("TNAId","--- "+tna_id[i]);
                    //Log.e("TNADesc","--- "+tna_desc[i]);

                    //Storing data in tna table
                    sqldb=helper.getWritableDatabase();
                    String Query = "Select * from Tnas where TnaId= '" + tna_id[i] +"'";
                    //  Log.i("tna Query  : ", Query);
                    Cursor cursor = sqldb.rawQuery(Query, null);
                    if(cursor.getCount() == 0)
                    {
                        //  Insert Values in TNAs Table
                        ContentValues values = new ContentValues();
                        values.put(LL_Table_Columns.TNA_ID, tna_id[i] + "");
                        values.put(LL_Table_Columns.TNA_DESC, tna_desc[i] + "");
                        values.put(LL_Table_Columns.TNA_SELECT, tna_Selection[i] + "");
                        values.put(LL_Table_Columns.TNA_DATECHANGE, tna_datechange[i] + "");
                        sqldb.insert(LL_Table_Columns.TNATABLE, null, values);
                    }
                    else
                    {
                        //Update Values in TNAs Table
                        String Update = "Update Tnas set "
                                + "TnaDesc='" + tna_desc[i].toString().trim() + "', "
                                + "TnaSelect='" + tna_Selection[i].toString().trim() +"', "
                                + "TnaDateChangeable='" + tna_datechange[i].toString().trim() + "' "
                                + " where TnaId='" + tna_id[i] + "'";

                        sqldb.execSQL(Update);
                    }
                    cursor.close();


                    JSONArray Activityarray = (JSONArray) TNAobj.get("Activities");
                    act_id=new String[Activityarray.length()];
                    act_desc=new String[Activityarray.length()];
                    act_checked=new String[Activityarray.length()];

                    for(int j = 0; j< Activityarray.length(); j++){
                        JSONObject Activityobj = Activityarray.getJSONObject(j);
                        act_id[j] = Activityobj.getString("ActivityId");
                        act_desc[j] = Activityobj.getString("Description");
                        act_checked[j] = Activityobj.getString("IsChecked");
                        //Log.e("Activityobj","--- "+Activityobj);
                        //Log.e("ActivityId","--- "+act_id[j]);
                        //Log.e("ActivityDesc","--- "+act_desc[j]);
                        //Log.e("ActivityIsChecked","--- "+act_checked[j]);

                        JSONArray StructureActivityarr = (JSONArray) Activityobj.get("StructureData");
                        for(int k = 0; k< StructureActivityarr.length(); k++) {
                            JSONObject StructureActivityobj = StructureActivityarr.getJSONObject(k);
                            Integer TLMValueId = (Integer) StructureActivityobj.get("TLMValueId");
                            String FieldName = StructureActivityobj.getString("FieldName");
                            String DataType = StructureActivityobj.getString("DataType");
                            String FieldCode = StructureActivityobj.getString("FieldCode");
                            JSONArray Valuearr = null;
                            //Log.e("jsonVal ", " " + StructureActivityobj.get("Value").equals(null));
                            if (StructureActivityobj.get("Value").equals(null)) {
                                Valuearr = new JSONArray();
                            } else {
                                Valuearr = (JSONArray) StructureActivityobj.get("Value");
                            }


                            String Query1 = "Select * from Activitys where TnaActId='" + tna_id[i] + "' AND TLMValueId='" + TLMValueId +"'";
                            Cursor cursor1 = sqldb.rawQuery(Query1, null);
                            JSONObject json = new JSONObject();
                            if(cursor1.getCount() == 0)
                            {
                                // Insert Values in Activitys Table
                                ContentValues values = new ContentValues();
                                values.put(LL_Table_Columns.TNAACT_ID, tna_id[i] + "");
                                values.put(LL_Table_Columns.ACT_ID, act_id[j] + "");
                                values.put(LL_Table_Columns.ACT_DESC, act_desc[j] + "");
                                values.put(LL_Table_Columns.ACT_ISCHECKED, act_checked[j] + "");
                                values.put(LL_Table_Columns.ACT_TLMVALUEID, TLMValueId + "");
                                values.put(LL_Table_Columns.ACT_FIELDNAME, FieldName + "");
                                values.put(LL_Table_Columns.ACT_DATATYPE, DataType + "");
                                values.put(LL_Table_Columns.ACT_FIELDCODE, FieldCode + "");
                                json.put("values", Valuearr);
                                values.put(LL_Table_Columns.ACT_VALUES, String.valueOf(json.getJSONArray("values")));
                                sqldb.insert(LL_Table_Columns.ACTIVITYTABLE, null, values);
                            }
                            else
                            {

                                json.put("values", Valuearr);
                                // Update Values in Activitys Table
                                String Update = "Update Activitys set "
                                        + "FieldName='" + FieldName + "', "
                                        + "DataType='" + DataType +"', "
                                        + "FieldCode='" + FieldCode + "', "
                                        + "Value='" + String.valueOf(json.getJSONArray("values")) + "' "
                                        + " where TnaActId='" + tna_id[i] + "' AND TLMValueId='" + TLMValueId + "'";

                                sqldb.execSQL(Update);
                            }

                            cursor1.close();


                        }
                    }


                    JSONArray OptionCodearray = (JSONArray) TNAobj.get("OptionCodes");
                    opt_par_val=new String[OptionCodearray.length()];
                    for(int l = 0; l< OptionCodearray.length(); l++) {
                        JSONObject OptionCodeobj = OptionCodearray.getJSONObject(l);
                        opt_par_val[l] = OptionCodeobj.getString("Value");

                        JSONArray ChildOptCodearr = (JSONArray) OptionCodeobj.get("ChildData");
                        for (int m = 0; m < ChildOptCodearr.length(); m++) {
                            JSONObject ChildOptCodearrobj = ChildOptCodearr.getJSONObject(m);
                            String opt_child_Val = ChildOptCodearrobj.getString("Value");

                            String Query1 = "Select * from OptionCodes where TnaOptId='" + tna_id[i] + "' AND OptParValue='" + opt_par_val[l] +"' AND OptChildValue='" + opt_child_Val +"'";
                            Cursor cursor1 = sqldb.rawQuery(Query1, null);
                            Log.e("cursor1 op "," "+cursor1.getCount());

                            if(cursor1.getCount() == 0)
                            {
                                //Insert Values in Optioncodes Table
                                ContentValues values = new ContentValues();
                                values.put(LL_Table_Columns.TNAOPT_ID, tna_id[i] + "");
                                values.put(LL_Table_Columns.OPT_PARENT_VAL, opt_par_val[l] + "");
                                values.put(LL_Table_Columns.OPT_CHILD_VAL, opt_child_Val + "");
                                sqldb.insert(LL_Table_Columns.OPTIONCODETABLE, null, values);


                            }
                            else
                            {
                                //Update  Values in Optioncodes Table
                                String Update = "Update OptionCodes set "
                                        + "OptParValue='" + opt_par_val[l] + "', "
                                        + "OptChildValue='" + opt_child_Val + "' "
                                        + " where TnaOptId='" + tna_id[i] + "'  AND OptParValue='" + opt_par_val[l] +"' AND OptChildValue='" + opt_child_Val + "'";

                                sqldb.execSQL(Update);
                            }
                            cursor1.close();
                        }
                    }



                        }

                //Delete Data from TNA Table
                sqldb=helper.getWritableDatabase();
                String selecttnas = "Select * from Tnas";
                Cursor tnas_data=sqldb.rawQuery(selecttnas, null);

                if(tnas_data.getCount()>0)
                {
                    tnas_data.moveToFirst();
                    do //compares db valu with api values
                    {
                        String dbtna_id = tnas_data.getString(tnas_data.getColumnIndexOrThrow("TnaId")).trim();//
                        for(int cou=0;cou<tna_id.length;cou++)
                        {

                            if(dbtna_id.equals(tna_id[cou]))
                            {
                                TNA_Exists="Yes";
                                break;
                            }
                            else
                            {
                                TNA_Exists="No";

                            }
                        }

                        if(TNA_Exists.equals("No"))
                        {
                            sqldb.delete("Tnas", "TnaId='" + dbtna_id + "'", null);

                        }
                    }while(tnas_data.moveToNext());
                }
                tnas_data.close();



            }
            catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // call to UI of Settings and Filters
            createTNAElements(context);

        }


    }


    //function for TNA Elements
    public void createTNAElements(Context context){
        helper = DBHelper.getInstance(context);
        SQLiteDatabase sqldb = helper.getReadableDatabase();
        int index = 0;
        //Fetch Tnas Data
        String Query = "Select * from Tnas";
        // Log.i("Tna Query  : ",Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        String[] tna;
        String[] tnaselection;
        String[] tnadatechange;

        tna = new String[cursor.getCount()];
        tnaselection = new String[cursor.getCount()];
        tnadatechange = new String[cursor.getCount()];

            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex("TnaDesc"));
                    String sel = cursor.getString(cursor.getColumnIndex("TnaSelect"));
                    String datechange = cursor.getString(cursor.getColumnIndex("TnaDateChangeable"));
                    tna[index] = data;
                    tnaselection[index] = sel;
                    tnadatechange[index] = datechange;
                    index++;
                    // do what ever you want here
                } while (cursor.moveToNext());
            }
            cursor.close();




        if (sqldb != null && sqldb.isOpen())
            sqldb.close();

        //Log.e("tnaSelection ", " " +  tna[0]+" "+tna[1]+"" +tna[2]+" "+tnaselection.length);

        Bundle bundletna = new Bundle();
        bundletna.putStringArray("tnas", tna);// bundle for tna dropdown

        Bundle bundletnaselection = new Bundle();
        bundletnaselection.putStringArray("tnaselection",tnaselection);// bundle for tna default selection

        Bundle bundledatechange = new Bundle();
        bundledatechange.putStringArray("tnadatechange",tnadatechange);// bundle for tna datechangeable


        Activity activity = (Activity) context;
        Intent i = new Intent(context.getApplicationContext(), TNAActivity.class);
        i.putExtras(bundletna);
        i.putExtras(bundletnaselection);
        i.putExtras(bundledatechange);
        activity.startActivity(i);

        if(m_config.pDialog != null){
            if (m_config.pDialog.isShowing()){
                m_config.pDialog.dismiss();

            }
        }

    }



}

