package tna.aperotechnologies.com.tna;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TNAActivitywork2 extends Activity {

    private DBHelper helper;
    Spinner spin_tna;
    TextView txt_activity,txt_optionCode,txt_Date;
    String selected_tna;
    SQLiteDatabase sqldb;
    String Query;
    String[] tnas,tnaselection,tnadatechange;
    String TnaId;
    Button btnDone;
    TNAActivitywork2 cont = this;

    Configuration_Parameter m_config;
    View promptsVwActivity;
    ListView lvActivity;
    LayoutInflater li;
    CheckBox chkboxSelect;
    MyCustomAdapter llActivity_Adapter;
    SharedPreferences sharedpreferences;
    int countChecked;

    View promptsVwOptCode;
    ExpandableListView lvOptionCode;
    LayoutInflater liOptCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tna);

        m_config = Configuration_Parameter.getInstance();
        //Initialize dbhelper
        helper = DBHelper.getInstance(cont);
        sqldb = helper.getReadableDatabase();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        spin_tna = (Spinner) findViewById(R.id.spn_TNA);
        txt_activity = (TextView) findViewById(R.id.txt_Activity);
        txt_optionCode = (TextView) findViewById(R.id.txt_OptionCode);
        txt_Date = (TextView) findViewById(R.id.txt_Date);

        final String tna_data[] = getIntent().getExtras().getStringArray("tnas");
        tnas = fillMyArray(tna_data, "TNA");


        ArrayAdapter<String> tnaArray = new ArrayAdapter<String>(TNAActivitywork2.this, android.R.layout.simple_spinner_item, tnas);
        tnaArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_tna.setAdapter(tnaArray);

        String[] tna_selection = getIntent().getExtras().getStringArray("tnaselection");

        for(int i = 0; i< tna_selection.length; i++){
            if(tna_selection[i].equals("true")){
                spin_tna.setSelection(i);
            }

        }


        li = LayoutInflater.from(cont);
        promptsVwActivity = li.inflate(R.layout.activity_pronpts_view, null);
        lvActivity = (ListView) promptsVwActivity.findViewById(R.id.listView2);

        liOptCode = LayoutInflater.from(cont);
        promptsVwOptCode = liOptCode.inflate(R.layout.optcode_prompts_view, null);
        lvOptionCode = (ExpandableListView) promptsVwOptCode.findViewById(R.id.explistView);
        selected_tna =  spin_tna.getSelectedItem().toString().trim();

        ArrayList<ActivityColumnList> actList = new ArrayList<ActivityColumnList>();
        ActivityColumnList activitys;
        String Query = "Select TnaId from Tnas where TnaDesc='" + selected_tna.toString().trim() + "'";
        Cursor c = sqldb.rawQuery(Query, null);
        c.moveToFirst();
        TnaId = c.getString(c.getColumnIndexOrThrow("TnaId"));
        c.close();

        int index = 0;
        String Query1 = "Select ActivityId,ActivityDesc,ActIsChecked from Activitys where TnaActId='" + TnaId + "'";
        Cursor c1 = sqldb.rawQuery(Query1, null);
        final ArrayList act_dataIdChk = new ArrayList();
        //Map<String, String> act_dataId;
        //act_dataId = new HashMap<String, String>();
        final String [] act_data = new String[0];
        ArrayList<ActivityOptions> actOptions = new ArrayList<ActivityOptions>();


        if (c1.moveToFirst()) {
            do {
                String Id = c1.getString(c1.getColumnIndexOrThrow("ActivityId"));
                String Desc = c1.getString(c1.getColumnIndexOrThrow("ActivityDesc"));
                boolean Checked = Boolean.parseBoolean(c1.getString(c1.getColumnIndexOrThrow("ActIsChecked")));

                if(!(act_dataIdChk.contains(Id))){

                    act_dataIdChk.add(Id);
                    actOptions.add(new ActivityOptions(Id, Desc, Checked));
                    //act_dataId.put("Id", Id);
                    //act_dataId.put("Desc", Desc);
                    //act_dataId.put("Checked", Checked);

                }

                //
                // do what ever you want here
            } while (c1.moveToNext());
        }
        c1.close();


        Log.e("Activityoptions "," "+actOptions.size()+actOptions.get(0).getActId());



        //Feel the Listview with style table column names for Activity Fields
        countChecked  = 0;
        for(int i=0;i<act_data.length;i++)
        {

            if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {

                if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(act_data[i])) {
                    activitys = new ActivityColumnList("", act_data[i], true);
                    activitys.setSelected(true);
                    actList.add(activitys);


                } else {
                    activitys = new ActivityColumnList("", act_data[i], false);
                    activitys.setSelected(false);
                    actList.add(activitys);


                }
            }else{

                activitys = new ActivityColumnList("", act_data[i], true);
                activitys.setSelected(true);
                actList.add(activitys);
                countChecked = tna_data.length;

            }


        }

        //create an ArrayAdaptar from the String Array
        llActivity_Adapter = new MyCustomAdapter(cont,R.layout.activity_list_layout, actList);
        lvActivity.setAdapter(llActivity_Adapter);

        txt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarCode();
            }
        });

        txt_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LLGENQuery=generateSelectQuery(sharedpreferences.getString(m_config.selected_brand,"brand"), sharedpreferences.getString(m_config.selected_season,"season"), sharedpreferences.getString(m_config.selected_collection,"collection"), sharedpreferences.getString(m_config.selected_gender,"gender"),sharedpreferences.getString(m_config.selected_status,"status"),sharedpreferences.getString(m_config.selected_category,"category"));

                // Log.i("LLGenQuery 11",LLGENQuery);
                //String brand,String season,String collecton, String gender,String status, String category)
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cont);
                if (promptsVwActivity.getParent() == null) {

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
                            Log.e("isChecked", " " + cb.isChecked());
                            ArrayList<ActivityColumnList> actList = new ArrayList<>();
                            ActivityColumnList activitys;
                            for (int i = 0; i < tna_data.length; i++) {
                                activitys = new ActivityColumnList("", tna_data[i], cb.isChecked());
                                activitys.setSelected(cb.isChecked());
                                actList.add(activitys);
                            }
                            llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
                            lvActivity.setAdapter(llActivity_Adapter);
                        }
                    });

                    alertDialogBuilder.setView(promptsVwActivity);
                    ArrayList<ActivityColumnList> actList = new ArrayList<>();
                    ActivityColumnList activitys;
                    Log.e("second ","--- "+sharedpreferences.getStringSet(m_config.activitySelKey, null));

                    for (int i = 0; i < act_data.length; i++) {

                        if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {
                            countChecked = sharedpreferences.getStringSet(m_config.activitySelKey,null).size();
                            if(act_data.length == sharedpreferences.getStringSet(m_config.activitySelKey,null).size()){
                                chkboxSelect.setChecked(true);
                            }

                            if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(act_data[i])) {
                                activitys = new ActivityColumnList("", act_data[i], true);
                                activitys.setSelected(true);
                                actList.add(activitys);

                            } else {
                                activitys = new ActivityColumnList("", act_data[i], false);
                                activitys.setSelected(false);
                                actList.add(activitys);



                            }
                        }else {
                            activitys = new ActivityColumnList("", act_data[i], true);
                            activitys.setSelected(true);
                            actList.add(activitys);
                            chkboxSelect.setChecked(true);
                            countChecked = act_data.length;

                        }
                    }

                    //create an ArrayAdaptar from the String Array
                    llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
                    lvActivity.setAdapter(llActivity_Adapter);
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
                            Log.e("isChecked", " " + cb.isChecked());
                            ArrayList<ActivityColumnList> actList = new ArrayList<>();
                            ActivityColumnList activitys;
                            for (int i = 0; i < act_data.length; i++) {
                                activitys = new ActivityColumnList("", act_data[i], cb.isChecked());
                                activitys.setSelected(cb.isChecked());
                                actList.add(activitys);
                            }
                            llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
                            lvActivity.setAdapter(llActivity_Adapter);
                        }
                    });
                    alertDialogBuilder.setView(promptsVwActivity);

                    ArrayList<ActivityColumnList> actList = new ArrayList<>();
                    ActivityColumnList activitys;

                    Log.e("third ","--- "+sharedpreferences.getStringSet(m_config.activitySelKey, null));

                    for (int i = 0; i < act_data.length; i++) {

                        if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {
                            countChecked = sharedpreferences.getStringSet(m_config.activitySelKey,null).size();
                            if(tna_data.length == sharedpreferences.getStringSet(m_config.activitySelKey,null).size()){
                                chkboxSelect.setChecked(true);
                            }

                            if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(act_data[i])) {
                                activitys = new ActivityColumnList("", act_data[i], true);
                                activitys.setSelected(true);
                                actList.add(activitys);



                            } else {
                                activitys = new ActivityColumnList("", act_data[i], false);
                                activitys.setSelected(false);
                                actList.add(activitys);



                            }
                        }else{
                            activitys = new ActivityColumnList("", act_data[i], true);
                            activitys.setSelected(true);
                            actList.add(activitys);
                            chkboxSelect.setChecked(true);
                            countChecked = act_data.length;



                        }
                    }

                    //create an ArrayAdaptar from the String Array
                    llActivity_Adapter = new MyCustomAdapter(cont, R.layout.activity_list_layout, actList);
                    lvActivity.setAdapter(llActivity_Adapter);
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
                theButton.setOnClickListener(new AlertDialogCustomListenerLLG(alertDialog));


            }
        });


        txt_optionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    //Activity fields Dialog OK Button Click Listener
    class AlertDialogCustomListenerLLG implements View.OnClickListener
    {
        private final Dialog dialog;
        Configuration_Parameter m_config = Configuration_Parameter.getInstance();

        public AlertDialogCustomListenerLLG(Dialog dialog)
        {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v)
        {
            // get user input and set it to result
            //int count = 0;
            ArrayList responseText = new ArrayList();
            String activityname= "";
            //StringBuffer responseText = new StringBuffer();
            //responseText.append("");
            ArrayList<ActivityColumnList> actList = llActivity_Adapter.actList;
            //Log.e("actList ", "--- " + actList.size());
            for (int i = 0; i < actList.size(); i++)
            {
                ActivityColumnList activitys = actList.get(i);
                if (activitys.isSelected())
                {

                    //responseText.append(activitys.getName());
                    responseText.add(activitys.getdesc());
                    if(activityname.equals("")){
                        activityname = activitys.getdesc();
                    }else{
                        activityname += ", "+activitys.getdesc();
                    }
                    //count++;
                }
            }
            m_config.activitysel = responseText;
            txt_activity.setText(activityname);

            //Log.e("responseText", "--- " + responseText);
            //String temp = responseText.toString();
            //android.util.Log.i("temp---", ""+temp);
            //m_config.selected_parameter = temp.split("\\,");
            //m_config.activitysel = new ArrayList<String>(Arrays.asList( m_config.selected_parameter)); //new ArrayList is only needed if you absolutely need an ArrayList
            //Log.e("m_config.activitysel"," ---"+m_config.activitysel);

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



                        final String tna_data[] = getIntent().getExtras().getStringArray("tnas");
                        Log.e("checked item"," "+countChecked);
                        if(countChecked == tna_data.length){
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
        final String month_string = month_date.format(c.getTime());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int monthn = month + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String currdate = day + "/" + monthn + "/" + year;
        //Log.e("currdate", "value " + currdate);//25/8/2015
        SimpleDateFormat sdfn = new SimpleDateFormat("dd/MM/yyyy");//Tue Aug 25 00:00:00 GMT 2015
        try {
            Date currentDate = sdfn.parse(currdate);
            //Log.e("currentDate", "value " + currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date d = new Date(c.getTimeInMillis());



        DatePickerDialog dp = new DatePickerDialog(TNAActivitywork2.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Log.e("OnDateSet ", "value " + year + " " + monthOfYear + " " + dayOfMonth);//2015 7 16

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
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date birthDate = sdf.parse(newBirthDate);
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




}


/*

{
    code = 200;
    data =     {
        chatMessages =         (
                        {
                groupId = 2986;
                id = 29261;
                incoming = false;
                mediaType =                 (
                );
                mediaURL =                 (
                );
                message = "Test message";
                sender = "Parth Gandhi";
                sponsored = false;
                time = 1444073640000;
                type = 2;
            }
        );
        groups =         (
                        {
                code = "inst @aperopushtest";
                id = 2986;
                timeStamp = 1443996051000;
                title = "Apero Test";
                type = 2;
            },
        );
        user =         {
            pushToken = "(null)";
            student =             {
                AIEEEScore = 0;
                AddnSpecialization = "<null>";
                Aggr = 0;
            };
            teacher = "<null>";
            userType = 3;
        };
    };
    dataArray = "<null>";
    message = "Signup successful. Proceed to Dashboard";
}
 */