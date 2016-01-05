package tna.aperotechnologies.com.tna;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class TNAActivity extends Activity {

    private DBHelper helper;
    Spinner spin_tna; //Spinner for tna dropdown
    TextView txt_activity,txt_optionCode,txt_Date; //Edittext for Activity, Option Code and Date
    String selected_tna; // variable for storing selected tna value
    SQLiteDatabase sqldb;
    String[] tnas;
    //tnaselection,tnadatechange;
    String TnaId;
    Button btnDone;
    TNAActivity cont = this;

    Configuration_Parameter m_config;
    SharedPreferences sharedpreferences;

    ArrayList<ActivityOptions> actOptions;// ArrayList of Activities for particular tna

    // View, Listview, LayoutInflater for Activity
    View promptsVwActivity;
    ListView lvActivity;
    LayoutInflater li;
    CheckBox chkboxSelect; // Checkbox for Select All
    MyCustomAdapter llActivity_Adapter; // Adapter for Activity
    int countChecked; //count for Activity Selected

    // View, ExpListview, LayoutInflater for OptionCode
    View promptsVwOptCode;
    ExpandableListView explvOptionCode;
    LayoutInflater liOptCode;
    FilterExpCheckAdapter OptCode_Adapter; // Adapter for OptionCode
    //private CheckBox checkedBox;
    String initialTNA; // store value of default TNA selected


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tna);

        //Initialize Configuration Parameter
        m_config = Configuration_Parameter.getInstance();

        //Initialize dbhelper
        helper = DBHelper.getInstance(cont);
        sqldb = helper.getReadableDatabase();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        spin_tna = (Spinner) findViewById(R.id.spn_TNA);
        txt_activity = (TextView) findViewById(R.id.txt_Activity);
        txt_optionCode = (TextView) findViewById(R.id.txt_OptionCode);
        btnDone = (Button) findViewById(R.id.done);


        final String tna_data[] = getIntent().getExtras().getStringArray("tnas");
        tnas = fillMyArray(tna_data, "TNA");

        ArrayAdapter<String> tnaArray = new ArrayAdapter<String>(TNAActivity.this, android.R.layout.simple_spinner_item, tnas);
        tnaArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_tna.setAdapter(tnaArray);

        li = LayoutInflater.from(cont);
        promptsVwActivity = li.inflate(R.layout.activity_pronpts_view, null);
        lvActivity = (ListView) promptsVwActivity.findViewById(R.id.listView2);
        chkboxSelect = (CheckBox) promptsVwActivity.findViewById(R.id.checkboxSelect);


        String[] tna_selection = getIntent().getExtras().getStringArray("tnaselection");

        // set default TNA based on Tna_selection in spinner
        for(int i = 0; i< tna_selection.length; i++){
            if(tna_selection[i].equals("true")){
                spin_tna.setSelection(i);
            }
        }

        initialTNA = spin_tna.getSelectedItem().toString().trim();

        spin_tna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selected_tna = (String) parentView.getItemAtPosition(position); //store Value of Selected TNA
                //Log.e("selected_tna ", " " + selected_tna);
                countChecked = 0;
                actOptions = new ArrayList<ActivityOptions>();
                m_config.activitysel = new ArrayList<>();
                m_config.activitySelKey = null;
                m_config.activityselCode = null;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(m_config.activitySelKey);
                editor.commit();

                //checks whether default TNA and selected TNA is different
                if(!initialTNA.equals(selected_tna)){

                    //sets all ActIschecked as true for All activities selection
                    String Query1 = "Select * from Activitys";
                    Cursor cursor1 = sqldb.rawQuery(Query1, null);
                    if(cursor1.getCount() == 0)
                    {

                    }
                    else
                    {

                        String Update = "Update Activitys set "
                                + "ActIsChecked='true' ";

                        sqldb.execSQL(Update);
                    }

                    cursor1.close();

                }

                if(selected_tna != null) {
                    getActivityList("noclick");
                }

                m_config.OptCodePar = "";
                m_config.OptCodeChild = "Option Code";
                m_config.lastcheckedBox = null;

                if(selected_tna != null) {
                    getOptionCodeList("tna selection");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        txt_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tna == null){
                    return;
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cont);
                if (promptsVwActivity.getParent() == null) {

                    promptsVwActivity = null;
                    li = null;
                    li = LayoutInflater.from(cont);
                    promptsVwActivity = li.inflate(R.layout.activity_pronpts_view, null);
                    lvActivity = (ListView) promptsVwActivity.findViewById(R.id.listView2);
                    chkboxSelect = (CheckBox) promptsVwActivity.findViewById(R.id.checkboxSelect);

                    //click of SelectAll
                    chkboxSelect.setOnClickListener( new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            CheckBox cb = (CheckBox) v ;
                            Log.e("isChecked", " " + cb.isChecked());
                            //when select all checkbox will be set to true or false
                            if(cb.isChecked() == false){
                                countChecked  = 0;
                            }else{
                                countChecked  = actOptions.size();
                            }

                            ArrayList<ActivityColumnList> actList = new ArrayList<>();
                            ActivityColumnList activitys;
                            for (int i = 0; i < actOptions.size(); i++) {
                                activitys = new ActivityColumnList(actOptions.get(i).getActId(),actOptions.get(i).getActDesc(), cb.isChecked());
                                activitys.setSelected(cb.isChecked());
                                actList.add(activitys);
                            }
                            llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
                            lvActivity.setAdapter(llActivity_Adapter);
                        }
                    });

                    alertDialogBuilder.setView(promptsVwActivity);
                    Log.e("second ","--- "+sharedpreferences.getStringSet(m_config.activitySelKey, null));
                    countChecked  = 0;
                    actOptions = new ArrayList<ActivityOptions>();
                    getActivityList("onclick");

                } else {
                    promptsVwActivity = null;
                    li = null;
                    li = LayoutInflater.from(cont);
                    promptsVwActivity = li.inflate(R.layout.activity_pronpts_view, null);
                    lvActivity = (ListView) promptsVwActivity.findViewById(R.id.listView2);
                    chkboxSelect = (CheckBox) promptsVwActivity.findViewById(R.id.checkboxSelect);
                    chkboxSelect.setOnClickListener( new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            CheckBox cb = (CheckBox) v ;
                            //Log.e("isChecked", " " + cb.isChecked());
                            if(cb.isChecked() == false){
                                countChecked  = 0;
                            }else{
                                countChecked  = actOptions.size();
                            }
                            ArrayList<ActivityColumnList> actList = new ArrayList<>();
                            ActivityColumnList activitys;
                            for (int i = 0; i < actOptions.size(); i++) {
                                activitys = new ActivityColumnList(actOptions.get(i).getActId(),actOptions.get(i).getActDesc(), cb.isChecked());
                                activitys.setSelected(cb.isChecked());
                                actList.add(activitys);
                            }
                            llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
                            lvActivity.setAdapter(llActivity_Adapter);
                        }
                    });
                    alertDialogBuilder.setView(promptsVwActivity);

                    Log.e("third ", "--- " + sharedpreferences.getStringSet(m_config.activitySelKey, null));
                    countChecked  = 0;
                    actOptions = new ArrayList<ActivityOptions>();
                    getActivityList("onclick");
                }

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                theButton.setOnClickListener(new ActivityDialogDoneClick(alertDialog));


            }
        });


        txt_optionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_tna != null){
                    getOptionCodeList("txtoptCodeclick");
                }

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Userid", " " + sharedpreferences.getInt("UserId", 0));
                Log.e("LoginFlag", " " + sharedpreferences.getBoolean("LoginFlag", false));

                if (selected_tna.equals("") || selected_tna == null) {
                    Toast.makeText(cont, "Please Select TNA", Toast.LENGTH_SHORT).show();
                } else if (txt_activity.getText().equals("0 Item Checked")) {
                    Toast.makeText(cont, "Please Select Activity", Toast.LENGTH_SHORT).show();
                } else if (txt_optionCode.getText().equals("Option Code")) {
                    Toast.makeText(cont, "Please Select Option code", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(TNAActivity.this, ActivityDetails.class);
                    i.putExtra("TnaId", TnaId);
                    i.putExtra("OptionCode", m_config.OptCodePar);
                    i.putExtra("CostingSrNo", m_config.OptCodeChild);
                    i.putExtra("activityCode", m_config.activityselCode);
                    startActivity(i);

                    String UserId = String.valueOf(sharedpreferences.getInt("UserId", 0));
                    RequestQueue queue = Volley.newRequestQueue(TNAActivity.this);
                    String url = "http://114.143.218.114:91/api/tna/vendors/save/" + UserId + "/filters";
                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("TnaId", Integer.parseInt(TnaId));
                        obj.put("ActivityIds", m_config.activityselCode);
                        Log.e("obj", "" + obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                    try {
                                        if (response.getInt("Status") == 1) {
                                            //Toast.makeText(getApplicationContext(), response.getString("Description"), Toast.LENGTH_LONG).show();

                                        } else {

                                            //Toast.makeText(getApplicationContext(), response.getString("Description"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            error.printStackTrace();
                            // TODO Auto-generated method stub
                            if (error instanceof TimeoutError) {
                                //Toast.makeText(getApplicationContext(), "Server Not Available", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NoConnectionError || error instanceof NetworkError) {
                                //TODO
                                // Log.i("error.getMessage()", error.getMessage().toString() + "");
                                if (error.getMessage().toString().contains("No authentication challenges found")) {
                                    //Toast.makeText(getApplicationContext(), "Authorization Error", Toast.LENGTH_SHORT).show();
                                } else if (error.getMessage().toString().contains("UnknownHostException")) {
                                    //Toast.makeText(getApplicationContext(), "Invalid Web API", Toast.LENGTH_SHORT).show();
                                } else if (error.getMessage().toString().contains("ConnectException") || error.getMessage().toString().contains("SocketException")) {
                                    if (error.getMessage().toString().contains("Connection refused")) {
                                        //Toast.makeText(getApplicationContext(), "Invalid Web API", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Toast.makeText(getApplicationContext(), "Failed to Connect. Server not availble", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                                }
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                                //Toast.makeText(getApplicationContext(),"Invalid Login Credentials",Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                //TODO
                                //Toast.makeText(getApplicationContext(), "Server Not Available", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof VolleyError) {
                                if (error.getMessage().toString().contains("Bad URL")) {
                                    //Toast.makeText(getApplicationContext(), "Invalid Web API", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    });
                    int socketTimeout = 5000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    req.setRetryPolicy(policy);
                    queue.add(req);

                }
            }
        });



    }

    private String getTodaysDay() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String birth = "";
        int selectedMonth = month + 1;
        String monthString = new DateFormatSymbols().getMonths()[month];
        switch(monthString){
            case "January":
                monthString = "Jan";
                break;
            case "February":
                monthString = "Feb";
                break;
            case "March":
                monthString = "Mar";
                break;
            case "April":
                monthString = "Apr";
                break;
            case "May":
                monthString = "May";
                break;
            case "June":
                monthString = "Jun";
                break;
            case "July":
                monthString = "Jul";
                break;
            case "August":
                monthString = "Aug";
                break;
            case "September":
                monthString = "Sep";
                break;
            case "October":
                monthString = "Oct";
                break;
            case "November":
                monthString = "Nov";
                break;
            case "December":
                monthString = "Dec";
                break;


        }

        birth = day + "  " + monthString + "  " + year;
        return birth;
    }

    //Activity fields Dialog OK Button Click Listener
    class ActivityDialogDoneClick implements View.OnClickListener
    {
        private final Dialog dialog;
        Configuration_Parameter m_config = Configuration_Parameter.getInstance();

        public ActivityDialogDoneClick(Dialog dialog)
        {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v)
        {

            onActivitySelected();
            dialog.dismiss();
            dialog.cancel();
            //new ProgressTaskNew().execute();
            //Set the values
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Set<String> set = new HashSet<String>();
            set.addAll(m_config.activitysel);
            editor.putStringSet(m_config.activitySelKey, set);
            editor.commit();

        }


    }



    //For LIstView with Checkboxes for Change Fields
    private class MyCustomAdapter extends ArrayAdapter<ActivityColumnList>
    {
        private ArrayList<ActivityColumnList> actList;
        private LayoutInflater inflater;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<ActivityColumnList> actList)
        {
            super(context, textViewResourceId, actList);
            this.actList = new ArrayList<ActivityColumnList>();
            this.actList.addAll(actList);
            inflater = LayoutInflater.from(context);
        }

        private class ViewHolder
        {
            CheckBox chkboxactivity;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.activity_list_layout, null);
                holder = new ViewHolder();
                holder.chkboxactivity = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.chkboxactivity.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v ;
                        ActivityColumnList activitys = (ActivityColumnList) cb.getTag();
                        //Log.e("getTag", " " + cb.getTag() + " " + cb.isChecked());
                        activitys.setSelected(cb.isChecked());
                        notifyDataSetChanged();

                        if(cb.isChecked() == true){
                            countChecked++;
                        }else{
                            countChecked--;
                        }


                        if(countChecked == actOptions.size()){
                            chkboxSelect.setChecked(true);
                        }else{
                            chkboxSelect.setChecked(false);
                        }




                    }
                });
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            ActivityColumnList activitys = actList.get(position);
            holder.chkboxactivity.setText(activitys.getdesc());
            holder.chkboxactivity.setChecked(activitys.isSelected());
            holder.chkboxactivity.setTag(activitys);
            return convertView;
        }

    }

    private DatePickerDialog CalendarCode() {
        final Calendar c = Calendar.getInstance();

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(TNAActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String birth = "";
                int selectedMonth = monthOfYear + 1;
                String monthString = new DateFormatSymbols().getMonths()[monthOfYear];
                switch(monthString){
                    case "January":
                        monthString = "Jan";
                        break;
                    case "February":
                        monthString = "Feb";
                        break;
                    case "March":
                        monthString = "Mar";
                        break;
                    case "April":
                        monthString = "Apr";
                        break;
                    case "May":
                        monthString = "May";
                        break;
                    case "June":
                        monthString = "Jun";
                        break;
                    case "July":
                        monthString = "Jul";
                        break;
                    case "August":
                        monthString = "Aug";
                        break;
                    case "September":
                        monthString = "Sep";
                        break;
                    case "October":
                        monthString = "Oct";
                        break;
                    case "November":
                        monthString = "Nov";
                        break;
                    case "December":
                        monthString = "Dec";
                        break;


                }

                birth = dayOfMonth + "  " + monthString + "  " + year;
                String newBirthDate = dayOfMonth + "/" + selectedMonth + "/" + year;
                //Log.e("selectedMonth ", "value " +newBirthDate);//8---August--- 16  August  2015 --- 16/8/2015

                try {
                    txt_Date.setText(birth);

                } catch (Exception e) {
                }
            }

        }, year, month, day);

        //Log.e("month ","---- "+c.getTimeInMillis()+ new Date(c.getTimeInMillis()));
        //dp.getDatePicker().setMaxDate(c.getTimeInMillis());
        //dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dp.setTitle("");
        dp.setMessage("Select Date");
        dp.show();
        return dp;
    }

    //All Spinners Adapter data arrays initialization
    public String[] fillMyArray(String[] data_Array, String selector) {


        String data[] = data_Array;
        String localArray[] = new String[data_Array.length];

        for (int i = 0; i < localArray.length; i++) {
            localArray[i] = data[i];
        }
        return localArray;
    }


    public void getActivityList(String check){

        //fetch list of all activities for selected tna

        String Query = "Select TnaId from Tnas where TnaDesc='" + selected_tna.toString().trim() + "'";
        Cursor c = sqldb.rawQuery(Query, null);
        c.moveToFirst();
        TnaId = c.getString(c.getColumnIndexOrThrow("TnaId"));
        c.close();

        String Query1 = "Select ActivityId,ActivityDesc,ActIsChecked from Activitys where TnaActId='" + TnaId + "'";
        //String Query1 = "Select Distinct ActivityId,ActivityDesc,ActIsChecked from Activitys where TnaActId='" + TnaId + "'";
        Cursor c1 = sqldb.rawQuery(Query1, null);
        final ArrayList act_dataIdChk = new ArrayList();//used for checking activityid exist in array or not
        final ArrayList actChecked = new ArrayList();// used for storing isChecked of Activity



        if (c1.moveToFirst()) {
            do {
                String Id = c1.getString(c1.getColumnIndexOrThrow("ActivityId"));
                String Desc = c1.getString(c1.getColumnIndexOrThrow("ActivityDesc"));
                boolean Checked = Boolean.parseBoolean(c1.getString(c1.getColumnIndexOrThrow("ActIsChecked")));

                if(!(act_dataIdChk.contains(Id))){
                    act_dataIdChk.add(Id);
                    actOptions.add(new ActivityOptions(Id, Desc, Checked));
                    //stores id, description and isChecked Activity in structured array
                    actChecked.add(Checked);


                }

                //
                // do what ever you want here
            } while (c1.moveToNext());
        }
        c1.close();

        Log.e("Activityoptions first ", " " + actOptions.size());

        ArrayList<ActivityColumnList> actList = new ArrayList<ActivityColumnList>();
        ActivityColumnList activitys;
        for(int i=0;i<actOptions.size();i++)
        {
            if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {
                countChecked = sharedpreferences.getStringSet(m_config.activitySelKey,null).size();

                if(actOptions.size() == sharedpreferences.getStringSet(m_config.activitySelKey,null).size()){
                    chkboxSelect.setChecked(true);
                }
                if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(actOptions.get(i).getActDesc())) {
                    activitys = new ActivityColumnList(actOptions.get(i).getActId(),actOptions.get(i).getActDesc(),true);
                    activitys.setSelected(true);
                    actList.add(activitys);
                } else {
                    activitys = new ActivityColumnList(actOptions.get(i).getActId(),actOptions.get(i).getActDesc(),false);
                    activitys.setSelected(false);
                    actList.add(activitys);
                }
            }else{

                // checks actChecked array contains only false isChecked
                if(!actChecked.contains(true)){
                    // for very first time we have to show all checkbox selected
                    activitys = new ActivityColumnList(actOptions.get(i).getActId(), actOptions.get(i).getActDesc(), true);
                    activitys.setSelected(true);
                    actList.add(activitys);

                    String Query2 = "Select * from Activitys";
                    Cursor cursor1 = sqldb.rawQuery(Query2, null);
                    if(cursor1.getCount() == 0)
                    {
                    }
                    else
                    {

                        String Update = "Update Activitys set "
                                + "ActIsChecked='true' ";
                        sqldb.execSQL(Update);
                    }

                    cursor1.close();

                }else{
                    // if actChecked contains true as well as false isChecked
                    if(Boolean.parseBoolean(String.valueOf(actOptions.get(i).getActChecked() == true))){
                        countChecked++ ;
                        if(countChecked == actOptions.size()){
                            chkboxSelect.setChecked(true);

                        }
                    }

                    //set isChecked if activity is a combo of isChecked true and false
                    activitys = new ActivityColumnList(actOptions.get(i).getActId(), actOptions.get(i).getActDesc(), actOptions.get(i).getActChecked());
                    activitys.setSelected(Boolean.parseBoolean(String.valueOf(actOptions.get(i).getActChecked())));
                    actList.add(activitys);

                }
            }

        }


        //create an ArrayAdaptar from the String Array
        llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
        lvActivity.setAdapter(llActivity_Adapter);
        if(check == "noclick"){
            onActivitySelected();
        }



    }

    public void onActivitySelected(){
        // get activity selected and set it to result

        ArrayList responseText = new ArrayList();
        String activityName= "";
        String activityCode="";
        ArrayList<ActivityColumnList> actList = llActivity_Adapter.actList;


        for (int i = 0; i < actList.size(); i++)
        {
            ActivityColumnList activitys = actList.get(i);
            if (activitys.isSelected())
            {

                responseText.add(activitys.getdesc());

                if(activityName.equals("")){
                    activityName = activitys.getdesc();
                    activityCode = activitys.getCode();
                }else{
                    activityName += ", "+activitys.getdesc();
                    activityCode += ", "+activitys.getCode();
                }
            }
        }

        if(responseText.size() == 0){
            //Toast.makeText(TNAActivity.this, "Please select at least one activity", Toast.LENGTH_LONG).show();
            txt_activity.setText("0 Item Checked");
            return;
        }

        m_config.activitysel = responseText;
        m_config.activityselCode = activityCode;
        String count = String.valueOf(m_config.activitysel.size());


        if(m_config.activitysel.size() == actOptions.size()){
            txt_activity.setText("All Items checked");
        }else{
            if(count.equals("1")){
                txt_activity.setText(count + " Item checked");
            }else{
                txt_activity.setText(count + " Items checked");
            }

        }


        Log.e("responseText", "--- " + responseText + " ");
        Log.e("activityselCode", " --- " + m_config.activityselCode);
    }

    public void getOptionCodeList(String check){

        //fetch list of all Option Code for selected tna

        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(cont);

        promptsVwOptCode = null;
        liOptCode = null;
        liOptCode = LayoutInflater.from(cont);
        promptsVwOptCode = liOptCode.inflate(R.layout.optcode_prompts_view, null);
        explvOptionCode = (ExpandableListView) promptsVwOptCode.findViewById(R.id.explistView);

        String Query = "Select TnaId from Tnas where TnaDesc='" + selected_tna.toString().trim() + "'";
        Cursor c = sqldb.rawQuery(Query, null);
        c.moveToFirst();
        TnaId = c.getString(c.getColumnIndexOrThrow("TnaId"));
        c.close();

        String Query1 = "Select * from OptionCodes where TnaOptId='" + TnaId + "'";
        Cursor c1 = sqldb.rawQuery(Query1, null);
        //ArrayList<ActivityOptions> arrOptChildVal  = new ArrayList<ActivityOptions>();
        final ArrayList arrOptParVal = new ArrayList();// array for Option Code Parent Value
        ArrayList<ActivityOptions> optCode = new ArrayList<>(); // array for Option Code Child Value
        ArrayList<ArrayList<ActivityOptions>> arrOptChildVal = new ArrayList<>();
        //Log.e("c1 ", " " + c1.getCount());


        if (c1.moveToFirst()) {
            do {
                String OptParValue  = c1.getString(c1.getColumnIndexOrThrow("OptParValue"));
                String OptChildValue = c1.getString(c1.getColumnIndexOrThrow("OptChildValue"));



                if(arrOptParVal.indexOf(OptParValue) == -1) {
                    optCode = new ArrayList<>();
                    arrOptParVal.add(OptParValue);
                    if(m_config.OptCodeChild.contains(OptChildValue) && m_config.OptCodePar.contains(OptParValue)){
                        optCode.add(new ActivityOptions(OptParValue, OptChildValue, true));
                    }else{
                        optCode.add(new ActivityOptions(OptParValue, OptChildValue, false));
                    }

                    arrOptChildVal.add(optCode);

                }else{
                    if(arrOptParVal.contains(OptParValue)){
                        if(m_config.OptCodeChild.contains(OptChildValue) && m_config.OptCodePar.contains(OptParValue)){
                            optCode.add(new ActivityOptions(OptParValue, OptChildValue, true));
                        }else{
                            optCode.add(new ActivityOptions(OptParValue, OptChildValue, false));
                        }
                        arrOptChildVal.remove(arrOptParVal.indexOf(OptParValue));
                        arrOptChildVal.add(optCode);


                    }

                }


                //
                // do what ever you want here
            } while (c1.moveToNext());
        }
        c1.close();
        //Log.e("arrOptChildVal", " " + arrOptChildVal);


        OptCode_Adapter=new FilterExpCheckAdapter(cont,arrOptParVal,arrOptChildVal, explvOptionCode);
        explvOptionCode.setAdapter(OptCode_Adapter);
        alertDialogBuilder1.setView(promptsVwOptCode);

        // set dialog message

        alertDialogBuilder1
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                m_config.OptCodeChild = txt_optionCode.getText().toString();
//                                m_config.OptCodePar = txt_optionCode.getTag().toString();
//
//                                if(m_config.OptCodeChild.equals("Option Code")){
//                                    m_config.lastcheckedBox = null;
//                                }else{
//
//                                }
//
//
//
//                                dialog.dismiss();
//                                dialog.cancel();
//                            }
//                        });



        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder1.create();
        // show it
        if(check == "txtoptCodeclick"){
            alertDialog.show();
            Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            theButton.setOnClickListener(new OptionCodeDialogDoneClick(alertDialog));
        }

        OptCodeDoneClick();


    }

    //OptionCode Dialog OK Button Click Listener
    class OptionCodeDialogDoneClick implements View.OnClickListener
    {
        private final Dialog dialog;
        Configuration_Parameter m_config = Configuration_Parameter.getInstance();

        public OptionCodeDialogDoneClick(Dialog dialog)
        {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v)
        {

            OptCodeDoneClick();
            dialog.dismiss();
            dialog.cancel();

//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            Set<String> set = new HashSet<String>();
//            set.addAll(m_config.activitysel);
//            editor.putStringSet(m_config.activitySelKey, set);
//            editor.commit();

        }


    }

    public void OptCodeDoneClick(){
        // get user input and set it to result
        Log.e("m_config.OptCodeChild", " " + m_config.OptCodeChild + " "+m_config.OptCodePar);
        txt_optionCode.setText(m_config.OptCodeChild);
        txt_optionCode.setTag(m_config.OptCodePar);


    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Logout?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("UserId", 0);
                        editor.putBoolean("LoginFlag", false);
                        editor.commit();
                        finish();
                        Intent i = new Intent(TNAActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                }).create().show();

    }

    public void onPause(){
        super.onPause();
    }

}

