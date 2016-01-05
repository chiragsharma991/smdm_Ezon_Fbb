package tna.aperotechnologies.com.tna;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

public class TNAActivityWork extends Activity {

    private DBHelper helper;
    Spinner spin_tna;
    TextView txt_activity,txt_optionCode,txt_Date;
    String selected_tna;
    SQLiteDatabase sqldb;
    String Query;
    String[] tnas;
    Button btnDone;
    TNAActivityWork cont = this;

    Configuration_Parameter m_config;
    View promptsVwActivity;
    ListView lvActivity;
    LayoutInflater li;
    MyCustomAdapter llActivity_Adapter;
    SharedPreferences sharedpreferences;


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

        ArrayAdapter<String> tnaArray = new ArrayAdapter<String>(TNAActivityWork.this, android.R.layout.simple_spinner_item, tnas);
        tnaArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_tna.setAdapter(tnaArray);
        li = LayoutInflater.from(cont);
        promptsVwActivity = li.inflate(R.layout.activity_pronpts_view,null);
        lvActivity = (ListView) promptsVwActivity.findViewById(R.id.listView2);
        ArrayList<ActivityColumnList> actList = new ArrayList<ActivityColumnList>();
        ActivityColumnList activitys;

        //Feel the Listview with style table column names for Change Fields
        for(int i=0;i<tna_data.length;i++)
        {

            Log.e("first ","--- "+sharedpreferences.getStringSet(m_config.activitySelKey,null));

            if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {

                if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(tna_data[i])) {
                    activitys = new ActivityColumnList("", tna_data[i], true);
                    activitys.setSelected(true);
                    actList.add(activitys);
                } else {
                    activitys = new ActivityColumnList("", tna_data[i], false);
                    activitys.setSelected(false);
                    actList.add(activitys);
                }
            }else{
                activitys = new ActivityColumnList("", tna_data[i], false);
                activitys.setSelected(false);
                actList.add(activitys);
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
                    alertDialogBuilder.setView(promptsVwActivity);
                    ArrayList<ActivityColumnList> actList = new ArrayList<>();
                    ActivityColumnList activitys;

                    for (int i = 0; i < tna_data.length; i++) {

                        Log.e("second ","--- "+sharedpreferences.getStringSet(m_config.activitySelKey,null));

                        if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {
                            if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(tna_data[i])) {
                                activitys = new ActivityColumnList("", tna_data[i], true);
                                activitys.setSelected(true);
                                actList.add(activitys);
                            } else {
                                activitys = new ActivityColumnList("", tna_data[i], false);
                                activitys.setSelected(false);
                                actList.add(activitys);
                            }
                        }else{
                            activitys = new ActivityColumnList("", tna_data[i], false);
                            activitys.setSelected(false);
                            actList.add(activitys);
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
                    alertDialogBuilder.setView(promptsVwActivity);
                    ArrayList<ActivityColumnList> actList = new ArrayList<>();
                    ActivityColumnList activitys;

                    //Feel the Listview with style table column names for Change Fields
                    for (int i = 0; i < tna_data.length; i++) {

                        Log.e("third ","--- "+sharedpreferences.getStringSet(m_config.activitySelKey,null));

                        if(sharedpreferences.getStringSet(m_config.activitySelKey,null) != null) {
                            if (sharedpreferences.getStringSet(m_config.activitySelKey, null).contains(tna_data[i])) {
                                activitys = new ActivityColumnList("", tna_data[i], true);
                                activitys.setSelected(true);
                                actList.add(activitys);
                            } else {
                                activitys = new ActivityColumnList("", tna_data[i], false);
                                activitys.setSelected(false);
                                actList.add(activitys);
                            }
                        }else{
                            activitys = new ActivityColumnList("", tna_data[i], false);
                            activitys.setSelected(false);
                            actList.add(activitys);
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


    }

    //Chenge fields Dialog OK Button Click Listener
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
            //StringBuffer responseText = new StringBuffer();
            //responseText.append("");
            ArrayList<ActivityColumnList> actList = llActivity_Adapter.actList;
            Log.e("actList ", "--- " + actList.size());
            for (int i = 0; i < actList.size(); i++)
            {
                ActivityColumnList activitys = actList.get(i);
                if (activitys.isSelected())
                {

                    //responseText.append(activitys.getName());
                    responseText.add(activitys.getdesc());
                    //count++;
                }
            }
            m_config.activitysel = responseText;

            //Log.e("responseText", "--- " + responseText);
            //String temp = responseText.toString();
            //android.util.Log.i("temp---", ""+temp);
            //m_config.selected_parameter = temp.split("\\,");
            //m_config.activitysel = new ArrayList<String>(Arrays.asList( m_config.selected_parameter)); //new ArrayList is only needed if you absolutely need an ArrayList
            Log.e("m_config.activitysel"," ---"+m_config.activitysel);

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
                        Log.e("getTag", " " + activitys);
                        activitys.setSelected(cb.isChecked());
                        //  notifyDataSetChanged();
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



        DatePickerDialog dp = new DatePickerDialog(TNAActivityWork.this, new DatePickerDialog.OnDateSetListener() {
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
        dp.getDatePicker().setMaxDate(c.getTimeInMillis());
        dp.setTitle("");
        dp.setMessage("Select Date");
        dp.show();
        return dp;
    }

    //All Spinners Adapter data arrays initialization
    public String[] fillMyArray(String[] data_Array, String selector) {
        if (data_Array.length == 1 && data_Array[0].contains("No") && data_Array[0].contains("Options")) {
            String data[] = data_Array;
            String localArray[] = new String[(data_Array.length + 1)];
            localArray[0] = "Select " + selector;
            for (int i = 1; i < localArray.length; i++) {
                localArray[i] = data[i - 1];


            }
            return localArray;
        } else {
            // Log.i("data array for "+ selector,data_Array.length+"");
            String data[] = data_Array;
            //String localArray[]=new String[(data_Array.length+2)];
            //localArray[1]="All";
            //for(int i=2;i<localArray.length;i++
            String localArray[] = new String[(data_Array.length + 1)];
            localArray[0] = "Select " + selector;
            for (int i = 1; i < localArray.length; i++) {
                localArray[i] = data[i - 1];
            }
            return localArray;
        }
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