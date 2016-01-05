package tna.aperotechnologies.com.tna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * Created by hasai on 18/12/15.
 */



public class ActivityDetails extends Activity{
    Configuration_Parameter m_config;
    ReUsableFunctions reuse = new ReUsableFunctions();
    PredefinedFunc pfunc = new PredefinedFunc();
    TableLayout tablelayout;
    private DBHelper helper;
    SQLiteDatabase sqldb;
    Button btnSave;
    SharedPreferences sharedpreferences;
    ProgressDialog pDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        m_config = Configuration_Parameter.getInstance();
        tablelayout = (TableLayout) findViewById(R.id.tablelayout);
        btnSave = (Button) findViewById(R.id.btnSave);
        //Initialize preferences
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(ActivityDetails.this);
        pDialog = new ProgressDialog(ActivityDetails.this);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                pDialog.setMessage("Please Wait...");
                pDialog.setCancelable(false);
                if (!pDialog.isShowing()) {
                    pDialog.show();
                }




                Log.e("--- ","m_config.fieldCode "+m_config.fieldCode);
                Log.e("m_config.enteredActDet", " " + m_config.enteredActDet.size());

                String Message= "";
                for(int i = 0; i< m_config.enteredActDet.size(); i++){

//                    Log.e("enteredActDet activityid ", " "+m_config.enteredActDet.get(i).getFieldCode());
//                    Log.e("enteredActDet fieldcode ", " "+m_config.enteredActDet.get(i).getFieldCode());
//                    Log.e("enteredActDet fieldname ", " "+m_config.enteredActDet.get(i).getFieldName());
//                    Log.e("enteredActDet value ", " "+m_config.enteredActDet.get(i).getValue());
//                    Log.e("check "," "+m_config.enteredActDet.get(i).getValue().equals(""));

                    if(m_config.enteredActDet.get(i).getValue().equals("") || m_config.enteredActDet.get(i).getValue().equals("0") || m_config.enteredActDet.get(i).getValue().equals(" ")){
                        Message += "\n " +m_config.enteredActDet.get(i).getFieldName();
                    }


                }



                if(Message.equals("")){
                    if (chkStatus()) {
                        funcFillActDet();
                    } else {
                        Toast.makeText(ActivityDetails.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        if(pDialog != null){
                            if (pDialog.isShowing()){
                                pDialog.dismiss();

                            }
                        }
                    }

                }else{

                    Toast.makeText(ActivityDetails.this, "Please fill : " + Message, Toast.LENGTH_LONG).show();
                    if(pDialog != null){
                        if (pDialog.isShowing()){
                            pDialog.dismiss();

                        }
                    }
                }






                //Toast.makeText(ActivityDetails.this, "Please fill "+ msg.length, Toast.LENGTH_SHORT).show();


            }
        });

        helper = DBHelper.getInstance(ActivityDetails.this);
        sqldb = helper.getReadableDatabase();
        String TnaId = getIntent().getExtras().getString("TnaId");
        String ActId = getIntent().getExtras().getString("activityCode");
        ActId  = ActId.replace(", "," or ActivityId = ");
        //Log.e("ActId ", " " + ActId);



        String Query = "Select TnaDateChangeable from TNAs where TnaId='" + TnaId +"'";
        Cursor c = sqldb.rawQuery(Query, null);
        c.moveToFirst();
        String datechange = c.getString(c.getColumnIndexOrThrow("TnaDateChangeable"));
        c.close();

        String Query1 = "Select * from Activitys where TnaActId='" + TnaId + "' AND (ActivityId = " + ActId +")";
        Log.e("Query1", " "+Query1);
        Cursor c1 = sqldb.rawQuery(Query1, null);
        Log.e("cursor count ", "c1" + c1.getCount());
        String name = "";

        if (c1.moveToFirst()) {
            do {

                String ActivityId = c1.getString(c1.getColumnIndexOrThrow("ActivityId"));
                String ActivityName = c1.getString(c1.getColumnIndexOrThrow("ActivityDesc"));
                String FieldCode = c1.getString(c1.getColumnIndexOrThrow("FieldCode"));
                String FieldName = c1.getString(c1.getColumnIndexOrThrow("FieldName"));
                String DataType = c1.getString(c1.getColumnIndexOrThrow("DataType"));
                String Value = c1.getString(c1.getColumnIndexOrThrow("Value"));
                String[] StatValue = Value.split(",");

                //Log.e("ActivityName ", " " + ActivityName);
                //Log.e("FieldCode ", " " + FieldCode);
                //Log.e("FieldName ", " " + FieldName);
                //Log.e("DataType ", " " + DataType);
                Log.e("Value ", " " + Value);

                if(FieldCode.equals("T900") || FieldCode.equals("T901")){

                }else {

                    if (!name.equals(ActivityName)) {
                        name = ActivityName;
                        pfunc.funclblActName(tablelayout, ActivityDetails.this, name, ActivityId);


                    }

                    if (DataType.equals("INT")) {
                        pfunc.funcIntegerQty(tablelayout, ActivityDetails.this, FieldName, FieldCode, ActivityId);

                    }

                    if (DataType.equals("VAR") && StatValue.length == 1) {
                        pfunc.funcComment(tablelayout, ActivityDetails.this, FieldName, FieldCode, ActivityId);

                    }

                    if (DataType.equals("VAR") && StatValue.length > 1) {
                        pfunc.funcStatus(tablelayout, ActivityDetails.this, FieldName, FieldCode, Value, ActivityId);

                    }

                    if(DataType.equals("DATE") && datechange.equals("true")) {
                        pfunc.funcDate(tablelayout, ActivityDetails.this, FieldName, FieldCode, ActivityId);

                    }else if(DataType.equals("DATE") && datechange.equals("false")) {
                        pfunc.funcsendDate(tablelayout, ActivityDetails.this, FieldName, FieldCode, ActivityId);
                    }

                }


            } while (c1.moveToNext());
        }
        c1.close();


    }

    public void funcFillActDet(){




        String TNA = getIntent().getExtras().getString("TnaId");
        String OptionCode = getIntent().getExtras().getString("OptionCode");
        String CostingSrNo = getIntent().getExtras().getString("CostingSrNo");
        String Date = pfunc.getTodaysDay();

        JSONArray jsonActivitiesarr = new JSONArray();
        JSONObject jsonActivities = new JSONObject();
        JSONArray jsonFieldValuearr = new JSONArray();

        ArrayList arrAct = new ArrayList();

        for(int i = 0; i< m_config.enteredActDet.size(); i++){

            JSONObject jsonFieldObj = null;
            String ActivityId = m_config.enteredActDet.get(i).getActivityId();
            try {

                if(!arrAct.contains(ActivityId)){
                    arrAct.add(ActivityId);
                    jsonFieldValuearr = new JSONArray();
                    if(jsonActivities.length() != 0){
                        jsonActivitiesarr.put(jsonActivities);
                    }
                    jsonActivities = new JSONObject();

                }


                jsonFieldObj = new JSONObject();
                String FieldCode = m_config.enteredActDet.get(i).getFieldCode();
                String Value = m_config.enteredActDet.get(i).getValue();
                jsonFieldObj.put("FieldCode", FieldCode);
                jsonFieldObj.put("VALUE", Value);
                //Log.e("jsonFieldObj", " " + jsonFieldObj);
                jsonFieldValuearr.put(jsonFieldObj);
                //Log.e("jsonFieldValuearr", " " + jsonFieldValuearr);

                jsonActivities.put("ActivityId", ActivityId);
                jsonActivities.put("FieldValue", jsonFieldValuearr);

                //Log.e("jsonActivities", " " + jsonActivities);




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        jsonActivitiesarr.put(jsonActivities);
        //Log.e("jsonActivitiesarr", " " + jsonActivitiesarr);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TNAId", TNA);
            jsonObject.put("OptionCode", OptionCode);
            jsonObject.put("CostingSrNo", CostingSrNo);
            jsonObject.put("Date", Date);
            jsonObject.put("Activities", jsonActivitiesarr);
            Log.e("jsonObject ", " " + jsonObject);
            new MultipartRequest(jsonObject).execute();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private class MultipartRequest extends AsyncTask<Void, Void, Integer> {

        JSONObject object;
        public MultipartRequest(JSONObject obj) {
            super();
            object = obj;

            // do stuff
        }

        protected Integer doInBackground(Void... param) {

            String User_Id = String.valueOf(sharedpreferences.getInt("UserId",0));
            String url = "http://114.143.218.114:91/api/tna/vendors/save/" + User_Id +"/formdata/v1";

            Log.e("url "," "+url);
            int StatusCode = 0;
            HttpResponse response = null;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("text", new StringBody(object + ""));
                post.setEntity(reqEntity);
                response = client.execute(post);
                StatusCode = Integer.parseInt(response.getStatusLine().getStatusCode() + "");

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return StatusCode;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected void onPostExecute(Integer StatusCode) {


            Log.e("StatusCode"," "+ StatusCode);

            if (StatusCode == 200)
            {
                if(pDialog != null) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }


            }
            else
            {
                if(pDialog != null) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }

        }

    }

    public boolean chkStatus()
    {
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()  || mobile.isConnectedOrConnecting ())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        reuse.requesttnas("http://114.143.218.114:91", ActivityDetails.this);
        m_config.enteredActDet = new ArrayList<>();
        m_config.fieldCode = new ArrayList();
    }
}
///// compile('org.apache.httpcomponents:httpmime:4.+') {
//
//}
//        compile('org.apache.httpcomponents:httpcore:4.+') {
//
//        }

//packagingOptions {
//        exclude 'META-INF/DEPENDENCIES'
//        exclude 'META-INF/NOTICE'
//        exclude 'META-INF/LICENSE'
//        }