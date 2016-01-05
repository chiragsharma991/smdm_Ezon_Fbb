package tna.aperotechnologies.com.tna;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hasai on 29/12/15.
 */

public class PredefinedFunc {

    Context c;
    LinearLayout layout;
    Configuration_Parameter m_config;




    public void funclblActName(TableLayout tablelayout, final Context c, String activityName, String activityId) {

        View vw = new View(c);
        vw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
        tablelayout.addView(vw);

        TableRow tablerow = new TableRow(c);

        TextView lblActName = new TextView(c);
        lblActName.setText(activityName + " :");
        lblActName.setTextColor(Color.BLACK);
        lblActName.setPadding(5, 5, 5, 5);
        tablerow.addView(lblActName);// add the column to the table row here

        tablelayout.addView(tablerow, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void funcIntegerQty(TableLayout tablelayout, final Context c, final String fieldName, final String fieldCode, final String activityId){
        m_config = Configuration_Parameter.getInstance();
        View vw = new View(c);
        vw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
        tablelayout.addView(vw);

        TableRow tablerow = new TableRow(c);
        tablerow.setPadding(5, 5, 5, 5);

        TextView lblQty = new TextView(c);
        lblQty.setText(fieldName + " :");
        lblQty.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(lblQty);// add the column to the table row here


        final EditText edtQty = new EditText(c);
        int maxLength = 4;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        edtQty.setFilters(FilterArray);
        //edtQty.setPadding(5, 5, 5, 5); // set the padding (if required)
        edtQty.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtQty.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        tablerow.addView(edtQty);
        m_config.enteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, ""));
        m_config.fieldCode.add(fieldCode);
        edtQty.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));


        //Code to clear Focus from editText
        edtQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    edtQty.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    handled = true;

                }
                return handled;
            }
        });

        edtQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) { //do your work here }
                    int pos = m_config.fieldCode.indexOf(fieldCode);
                    //Log.e("pos "," --- "+pos);
                    m_config.enteredActDet.set(pos, new StructureDataActDet(m_config.enteredActDet.get(pos).getActivityId(), m_config.enteredActDet.get(pos).getFieldName(), m_config.enteredActDet.get(pos).getFieldCode(), edtQty.getText().toString()));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        /*edtQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    //m_config.enteredActDet..add(new StructureDataActDet(activityId, fieldName, fieldCode, edtQty.getText().toString()));
                    //funcSave(activityId,  fieldName,  fieldCode,  edtQty.getText().toString());
                    m_config.finalenteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, edtQty.getText().toString()));
                    

                }
            }
        });*/

            /*tablelayout.addView(tablerow, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,2f));*/
        tablelayout.addView(tablerow);


        }


    public void funcComment(TableLayout tablelayout, final Context c, final String fieldName, final String fieldCode, final String activityId){
        m_config = Configuration_Parameter.getInstance();

        View vw = new View(c);
        vw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
        tablelayout.addView(vw);

        TableRow tablerow = new TableRow(c);
        tablerow.setPadding(5, 5, 5, 5);
        TextView lblComment = new TextView(c);
        lblComment.setText(fieldName+" :");
        lblComment.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(lblComment);// add the column to the table row here


        final EditText edtComment = new EditText(c);
        int maxLength = 50;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        edtComment.setFilters(FilterArray);
        edtComment.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edtComment.setSingleLine();
        edtComment.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtComment.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(edtComment);
        m_config.enteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, ""));
        m_config.fieldCode.add(fieldCode);

        edtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    edtComment.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    handled = true;

                }
                return handled;
            }
        });




        edtComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) { //do your work here }
                    int pos = m_config.fieldCode.indexOf(fieldCode);
                    //Log.e("pos "," --- "+pos);
                    m_config.enteredActDet.set(pos, new StructureDataActDet(m_config.enteredActDet.get(pos).getActivityId(), m_config.enteredActDet.get(pos).getFieldName(), m_config.enteredActDet.get(pos).getFieldCode(), edtComment.getText().toString()));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tablelayout.addView(tablerow);




    }


    public void funcsendDate(TableLayout tablelayout, final Context c, final String fieldName, final String fieldCode, final String activityId) {
        m_config = Configuration_Parameter.getInstance();
        m_config.enteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, getTodaysDay()));
        m_config.fieldCode.add(fieldCode);
    }


    public void funcDate(TableLayout tablelayout, final Context c, final String fieldName, final String fieldCode, final String activityId){
        m_config = Configuration_Parameter.getInstance();
        View vw = new View(c);
        vw.setLayoutParams(new ViewGroup.LayoutParams(5, 10));
        tablelayout.addView(vw);

        TableRow tablerow = new TableRow(c);
        tablerow.setPadding(5, 5, 5, 5);
        TextView lbldate = new TextView(c);
        lbldate.setText(fieldName + " :");
        lbldate.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(lbldate);// add the column to the table row here


        final TextView edtsetDate = new TextView(c);
        edtsetDate.setTextColor(Color.BLACK);
        edtsetDate.setText(getTodaysDay());
        edtsetDate.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(edtsetDate);// add the column to the table row here

        m_config.enteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, edtsetDate.getText().toString()));
        m_config.fieldCode.add(fieldCode);

        tablelayout.addView(tablerow);


        edtsetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarCode(edtsetDate, c, activityId, fieldName, fieldCode);
            }
        });

    }


    public void funcStatus(TableLayout tablelayout, Context c, final String fieldName, final String fieldCode, String value, final String activityId){
        m_config = Configuration_Parameter.getInstance();

        String Statvalue = value;

        String[] StatValue1 = Statvalue.split("\\},");
        Log.e("StatValue1 length"," "+StatValue1.length);
        final ArrayList MasterValue = new ArrayList();
        final ArrayList MasterValueId = new ArrayList();
        final String[] selected_status = new String[1];
        final String[] selected_statusId = new String[1];

        for(int i = 0 ;i< StatValue1.length;i++){
            StatValue1[i]=StatValue1[i].replace("[", "");
            StatValue1[i]=StatValue1[i]+"}";
            StatValue1[i]=StatValue1[i].replace("}]}","}");
            try {
                JSONObject jsonVal = new JSONObject(StatValue1[i]);
                MasterValue.add(jsonVal.getString("MasterValue"));
                MasterValueId.add(jsonVal.getString("MasterValueId"));

                Log.e("MasterValue "," "+MasterValue);
                Log.e("MasterValueId ", " " + MasterValueId);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        View vw = new View(c);
        vw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
        tablelayout.addView(vw);

        TableRow tablerow = new TableRow(c);
        tablerow.setPadding(5, 5, 5, 5);
        TextView lblStatus = new TextView(c);
        lblStatus.setText(fieldName+" :");
        lblStatus.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(lblStatus);// add the column to the table row here

        final Spinner spnStatus = new Spinner(c);
        //final Spinner spnStatus = new Spinner(c);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, MasterValue);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(spinnerArrayAdapter);
        spnStatus.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tablerow.addView(spnStatus);

        m_config.enteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, selected_statusId[0]));
        m_config.fieldCode.add(fieldCode);

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                selected_status[0] = (String) parentView.getItemAtPosition(position);
                selected_statusId[0] = (String) MasterValueId.get(MasterValue.indexOf(selected_status[0]));
                //Log.e("selected_status ", "" + selected_status[0] + " " + selected_statusId[0]);

                //m_config.enteredActDet..add(new StructureDataActDet(activityId, fieldName, fieldCode, selected_statusId[0]));
                //funcSave(activityId, fieldName, fieldCode, selected_statusId[0]);
                int pos = m_config.fieldCode.indexOf(fieldCode);
                //Log.e("pos "," --- "+pos);

                m_config.enteredActDet.set(pos, new StructureDataActDet(m_config.enteredActDet.get(pos).getActivityId(), m_config.enteredActDet.get(pos).getFieldName(), m_config.enteredActDet.get(pos).getFieldCode(), selected_statusId[0]));
                

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tablelayout.addView(tablerow);

    }


    public String getTodaysDay() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String birth = "";
        int selectedMonth = month + 1;
        birth = day + "-" + selectedMonth + "-" + year;
        return birth;
    }

    private DatePickerDialog CalendarCode(final TextView edtsetDate, Context cont, final String activityId, final String fieldName, final String fieldCode) {
        final Calendar c = Calendar.getInstance();

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(cont, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String birth = "";
                int selectedMonth = monthOfYear + 1;


                birth = dayOfMonth + "-" + selectedMonth + "-" + year;

                //Log.e("selectedMonth ", "value " +newBirthDate);//8---August--- 16  August  2015 --- 16/8/2015

                try {
                    edtsetDate.setText(birth);
                    //m_config.enteredActDet.add(new StructureDataActDet(activityId, fieldName, fieldCode, edtsetDate.getText().toString()));
                    //funcSave(activityId, fieldName, fieldCode, edtsetDate.getText().toString());
                    int pos = m_config.fieldCode.indexOf(fieldCode);
                    //Log.e("pos "," --- "+pos);
                    m_config.enteredActDet.set(pos,new StructureDataActDet(m_config.enteredActDet.get(pos).getActivityId(), m_config.enteredActDet.get(pos).getFieldName(), m_config.enteredActDet.get(pos).getFieldCode(), edtsetDate.getText().toString()));



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


}
