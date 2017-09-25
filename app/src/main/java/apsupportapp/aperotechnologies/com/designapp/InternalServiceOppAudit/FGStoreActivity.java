package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;

public class FGStoreActivity extends AppCompatActivity {

    private Context context;
    private EditText edt_name_author, edt_dateofVisit, edt_dayofweek, edt_timeofVisit, edt_auditorType, edt_observations, edt_suggestions, edt_name_supervisor, edt_time_supervisor,  edt_product_name, edt_customer_name, edt_mobile, edt_keytakeaway;
    private List<Overallratings> list_overallratings;
    private List<Overallratings> list_billing_experience;
    private List<Overallratings> list_staffing;
    private List<Overallratings> list_brandpromoterhelpfulness;
    private List<Overallratings> list_pricemismatch;
    private List<Overallratings> list_storefacilities;
    private List<Overallratings> list_storelookfeel;
    private List<Overallratings> list_storeservices;
    private List<Overallratings> list_vm;
    private RadioButton radio_yes_supervisor, radio_no_supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fgstore);
        context = this;
        list_overallratings = new ArrayList<>();
        list_billing_experience = new ArrayList<>();
        list_staffing = new ArrayList<>();
        list_brandpromoterhelpfulness = new ArrayList<>();
        list_pricemismatch = new ArrayList<>();
        list_storefacilities = new ArrayList<>();
        list_storelookfeel = new ArrayList<>();
        list_storeservices = new ArrayList<>();
        list_vm = new ArrayList<>();



        final LinearLayout lin_lay_overall_ratings = (LinearLayout) findViewById(R.id.lin_lay_overall_ratings);
        create_Smiley(lin_lay_overall_ratings, "overallratings.json", list_overallratings, "overallratings");

        final LinearLayout lin_lay_billing_experience = (LinearLayout) findViewById(R.id.lin_lay_billing_experience);
        create_Smiley(lin_lay_billing_experience, "billingexperience.json", list_billing_experience, "billingexperience");

        final LinearLayout lin_lay_staffing = (LinearLayout) findViewById(R.id.lin_lay_staffing);
        create_Smiley(lin_lay_staffing, "staffing.json", list_staffing, "staffing");

        final LinearLayout lin_lay_brandpromoterhelpfulness = (LinearLayout) findViewById(R.id.lin_lay_brandpromoterhelpfulness);
        create_Smiley(lin_lay_brandpromoterhelpfulness, "brandpromoterhelpfulness.json", list_brandpromoterhelpfulness, "brandpromoterhelpfulness");

        final LinearLayout lin_lay_pricemismatch = (LinearLayout) findViewById(R.id.lin_lay_pricemismatch);
        create_Smiley(lin_lay_pricemismatch, "pricemismatch.json", list_pricemismatch, "pricemismatch");

        final LinearLayout lin_lay_storefacilities = (LinearLayout) findViewById(R.id.lin_lay_storefacilities);
        create_Smiley(lin_lay_storefacilities, "storefacilities.json", list_storefacilities, "storefacilities");

        final LinearLayout lin_lay_storelookfeel = (LinearLayout) findViewById(R.id.lin_lay_storelookfeel);
        create_Smiley(lin_lay_storelookfeel, "storelookfeel.json", list_storelookfeel, "storelookfeel");

        final LinearLayout lin_lay_storeservices = (LinearLayout) findViewById(R.id.lin_lay_storeservices);
        create_Smiley(lin_lay_storeservices, "storeservices.json", list_storeservices, "storeservices");

        final LinearLayout lin_lay_vm = (LinearLayout) findViewById(R.id.lin_lay_vm);
        create_Smiley(lin_lay_vm, "vm.json", list_vm, "vm");


        edt_name_author = (EditText) findViewById(R.id.edt_name_author);
        edt_dateofVisit = (EditText) findViewById(R.id.edt_dateofVisit);
        edt_dayofweek = (EditText) findViewById(R.id.edt_dayofweek);
        edt_timeofVisit = (EditText) findViewById(R.id.edt_timeofVisit);
        edt_auditorType = (EditText) findViewById(R.id.edt_auditorType);
        edt_observations = (EditText) findViewById(R.id.edt_observations);
        edt_suggestions = (EditText) findViewById(R.id.edt_suggestions);
        edt_name_supervisor = (EditText) findViewById(R.id.edt_name_supervisor);
        edt_time_supervisor  = (EditText) findViewById(R.id.edt_time_supervisor);
        edt_product_name = (EditText) findViewById(R.id.edt_product_name);
        edt_customer_name = (EditText) findViewById(R.id.edt_customer_name);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_keytakeaway = (EditText) findViewById(R.id.edt_keytakeaway);
        radio_yes_supervisor = (RadioButton) findViewById(R.id.radio_yes_supervisor);
        radio_no_supervisor = (RadioButton) findViewById(R.id.radio_no_supervisor);




        Button btn_Reset = (Button) findViewById(R.id.btn_reset);
        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_lay_overall_ratings.removeAllViews();
                lin_lay_billing_experience.removeAllViews();
                lin_lay_staffing.removeAllViews();
                lin_lay_brandpromoterhelpfulness.removeAllViews();
                lin_lay_pricemismatch.removeAllViews();
                lin_lay_storefacilities.removeAllViews();
                lin_lay_storelookfeel.removeAllViews();
                lin_lay_storeservices.removeAllViews();
                lin_lay_vm.removeAllViews();

                list_overallratings = new ArrayList<>();
                list_billing_experience = new ArrayList<>();
                list_staffing = new ArrayList<>();
                list_brandpromoterhelpfulness = new ArrayList<>();
                list_pricemismatch = new ArrayList<>();
                list_storefacilities = new ArrayList<>();
                list_storelookfeel = new ArrayList<>();
                list_storeservices = new ArrayList<>();
                list_vm = new ArrayList<>();

                create_Smiley(lin_lay_overall_ratings, "overallratings.json", list_overallratings, "overallratings");
                create_Smiley(lin_lay_billing_experience, "billingexperience.json", list_billing_experience, "billingexperience");
                create_Smiley(lin_lay_staffing, "staffing.json", list_staffing, "staffing");
                create_Smiley(lin_lay_brandpromoterhelpfulness, "brandpromoterhelpfulness.json", list_brandpromoterhelpfulness, "brandpromoterhelpfulness");
                create_Smiley(lin_lay_pricemismatch, "pricemismatch.json", list_pricemismatch, "pricemismatch");
                create_Smiley(lin_lay_storefacilities, "storefacilities.json", list_storefacilities, "storefacilities");
                create_Smiley(lin_lay_storelookfeel, "storelookfeel.json", list_storelookfeel, "storelookfeel");
                create_Smiley(lin_lay_storeservices, "storeservices.json", list_storeservices, "storeservices");
                create_Smiley(lin_lay_vm, "vm.json", list_vm, "vm");

                edt_name_author.setText("");
                edt_dateofVisit.setText("");
                edt_dayofweek.setText("");
                edt_timeofVisit.setText("");
                edt_auditorType.setText("");
                edt_observations.setText("");
                edt_suggestions.setText("");
                edt_name_supervisor.setText("");
                edt_time_supervisor.setText("");
                edt_product_name.setText("");
                edt_customer_name.setText("");
                edt_mobile.setText("");
                edt_keytakeaway.setText("");
                radio_yes_supervisor.setChecked(false);
                radio_no_supervisor.setChecked(false);

                edt_name_author.clearFocus();
                edt_dateofVisit.clearFocus();
                edt_dayofweek.clearFocus();
                edt_timeofVisit.clearFocus();
                edt_auditorType.clearFocus();
                edt_observations.clearFocus();
                edt_suggestions.clearFocus();
                edt_name_supervisor.clearFocus();
                edt_time_supervisor.clearFocus();
                edt_product_name.clearFocus();
                edt_customer_name.clearFocus();
                edt_mobile.clearFocus();
                edt_keytakeaway.clearFocus();

                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
       


        Button btn_Submit = (Button) findViewById(R.id.btn_submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("", "onClick: "+list_overallratings.size());
                Log.e("", "onClick: "+list_billing_experience.size());


                for(int i = 0; i < list_overallratings.size(); i++)
                {
                    Log.e("getHeader "," "+list_overallratings.get(i).getHeader());
//                    Log.e("getCode "," "+list_overallratings.get(i).getCode());
                    Log.e("getSmiley "," "+list_overallratings.get(i).getSmiley());
                }

                for(int i = 0; i < list_billing_experience.size(); i++)
                {
                    Log.e("getHeader "," "+list_billing_experience.get(i).getHeader());
//                    Log.e("getCode "," "+list_billing_experience.get(i).getCode());
                    Log.e("getSmiley "," "+list_billing_experience.get(i).getSmiley());
                }

                for(int i = 0; i < list_staffing.size(); i++)
                {
                    Log.e("getHeader "," "+list_staffing.get(i).getHeader());
//                    Log.e("getCode "," "+list_staffing.get(i).getCode());
                    Log.e("getSmiley "," "+list_staffing.get(i).getSmiley());
                }

                for(int i = 0; i < list_brandpromoterhelpfulness.size(); i++)
                {
                    Log.e("getHeader "," "+list_brandpromoterhelpfulness.get(i).getHeader());
//                    Log.e("getCode "," "+list_brandpromoterhelpfulness.get(i).getCode());
                    Log.e("getSmiley "," "+list_brandpromoterhelpfulness.get(i).getSmiley());
                }

                for(int i = 0; i < list_pricemismatch.size(); i++)
                {
                    Log.e("getHeader "," "+list_pricemismatch.get(i).getHeader());
//                    Log.e("getCode "," "+list_pricemismatch.get(i).getCode());
                    Log.e("getSmiley "," "+list_pricemismatch.get(i).getSmiley());
                }

                for(int i = 0; i < list_storefacilities.size(); i++)
                {
                    Log.e("getHeader "," "+list_storefacilities.get(i).getHeader());
//                    Log.e("getCode "," "+list_storefacilities.get(i).getCode());
                    Log.e("getSmiley "," "+list_storefacilities.get(i).getSmiley());
                }

                for(int i = 0; i < list_storelookfeel.size(); i++)
                {
                    Log.e("getHeader "," "+list_storelookfeel.get(i).getHeader());
//                    Log.e("getCode "," "+list_storelookfeel.get(i).getCode());
                    Log.e("getSmiley "," "+list_storelookfeel.get(i).getSmiley());
                }

                for(int i = 0; i < list_storeservices.size(); i++)
                {
                    Log.e("getHeader "," "+list_storeservices.get(i).getHeader());
//                    Log.e("getCode "," "+list_storeservices.get(i).getCode());
                    Log.e("getSmiley "," "+list_storeservices.get(i).getSmiley());
                }

                for(int i = 0; i < list_vm.size(); i++)
                {
                    Log.e("getHeader "," "+list_vm.get(i).getHeader());
//                    Log.e("getCode "," "+list_vm.get(i).getCode());
                    Log.e("getSmiley "," "+list_vm.get(i).getSmiley());
                }
            }
        });




    }


    public String loadJSONFromAsset(String json_file)
    {
        String json = null;
        try {
            InputStream is = context.getAssets().open(json_file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
    
    
    public void create_Smiley(LinearLayout lin_layout, String json_file, final List<Overallratings> list_ratings, String fromWhere)
    {
        JSONArray m_jArry = null;

        try {
            m_jArry = new JSONArray(loadJSONFromAsset(json_file));
            JSONObject jsonObject;
            Log.e("m_jArry.length() "," "+m_jArry.length());

            for (int i = 0; i < m_jArry.length(); i++)
            {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.smiley, null);
                lin_layout.addView(view);

                jsonObject  = (JSONObject) m_jArry.get(i);


                RelativeLayout rel_layout_header_smiley = (RelativeLayout) view.findViewById(R.id.rel_layout_header_smiley);
                TextView txt_rel_layout_header_smiley = (TextView) rel_layout_header_smiley.findViewById(R.id.header_smiley);
                txt_rel_layout_header_smiley.setText((CharSequence) jsonObject.get("header"));


                final ImageView img_1_unselected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_1_unselected);
                final ImageView img_2_unselected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_2_unselected);
                final ImageView img_3_unselected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_3_unselected);
                final ImageView img_4_unselected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_4_unselected);
                final ImageView img_5_unselected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_5_unselected);


                final ImageView img_1_selected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_1_selected);
                final ImageView img_2_selected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_2_selected);
                final ImageView img_3_selected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_3_selected);
                final ImageView img_4_selected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_4_selected);
                final ImageView img_5_selected = (ImageView) rel_layout_header_smiley.findViewById(R.id.img_5_selected);



                final ImageView[] img_last_selected = new ImageView[1];
                final ImageView[] img_last_unselected = new ImageView[1];

                final JSONObject finalJsonObject = jsonObject;
                final int finalI = i;

                img_1_unselected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(img_last_selected[0] != null)
                        {
                            img_last_selected[0].setVisibility(View.GONE);
                            img_last_unselected[0].setVisibility(View.VISIBLE);
                        }

                        img_1_selected.setVisibility(View.VISIBLE);
                        img_1_unselected.setVisibility(View.GONE);
                        img_last_selected[0] = img_1_selected;
                        img_last_unselected[0] = img_1_unselected;

                        Overallratings overallratings = new Overallratings();
                        try {
                            overallratings.setHeader(finalJsonObject.getString("header"));
                            overallratings.setCode(finalJsonObject.getString("code"));
                            overallratings.setSmiley("1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(finalI, overallratings);
//                                exist = true;
                                break;
                            }

                        }

//                        if(!exist)
//                        {
//                            list_ratings.add(finalI, overallratings);
//                        }


                    }
                });

                img_2_unselected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(img_last_selected[0] != null)
                        {
                            img_last_selected[0].setVisibility(View.GONE);
                            img_last_unselected[0].setVisibility(View.VISIBLE);
                        }
                        img_2_selected.setVisibility(View.VISIBLE);
                        img_2_unselected.setVisibility(View.GONE);
                        img_last_selected[0] = img_2_selected;
                        img_last_unselected[0] = img_2_unselected;

                        Overallratings overallratings = new Overallratings();
                        try {
                            overallratings.setHeader(finalJsonObject.getString("header"));
                            overallratings.setCode(finalJsonObject.getString("code"));
                            overallratings.setSmiley("2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
//                                exist = true;
                                break;
                            }

                        }

//                        if(!exist)
//                        {
//                            list_ratings.add(overallratings);
//                        }


                    }
                });

                img_3_unselected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(img_last_selected[0] != null)
                        {
                            img_last_selected[0].setVisibility(View.GONE);
                            img_last_unselected[0].setVisibility(View.VISIBLE);
                        }
                        img_3_selected.setVisibility(View.VISIBLE);
                        img_3_unselected.setVisibility(View.GONE);
                        img_last_selected[0] = img_3_selected;
                        img_last_unselected[0] = img_3_unselected;

                        Overallratings overallratings = new Overallratings();
                        try {
                            overallratings.setHeader(finalJsonObject.getString("header"));
                            overallratings.setCode(finalJsonObject.getString("code"));
                            overallratings.setSmiley("3");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
//                                exist = true;
                                break;
                            }

                        }

//                        if(!exist)
//                        {
//                            list_ratings.add(overallratings);
//                        }

                    }
                });


                img_4_unselected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(img_last_selected[0] != null)
                        {
                            img_last_selected[0].setVisibility(View.GONE);
                            img_last_unselected[0].setVisibility(View.VISIBLE);
                        }
                        img_4_selected.setVisibility(View.VISIBLE);
                        img_4_unselected.setVisibility(View.GONE);
                        img_last_selected[0] = img_4_selected;
                        img_last_unselected[0] = img_4_unselected;

                        Overallratings overallratings = new Overallratings();
                        try {
                            overallratings.setHeader(finalJsonObject.getString("header"));
                            overallratings.setCode(finalJsonObject.getString("code"));
                            overallratings.setSmiley("4");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
//                                exist = true;
                                break;
                            }

                        }

//                        if(!exist)
//                        {
//                            list_ratings.add(overallratings);
//                        }

                    }
                });


                img_5_unselected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(img_last_selected[0] != null)
                        {
                            img_last_selected[0].setVisibility(View.GONE);
                            img_last_unselected[0].setVisibility(View.VISIBLE);
                        }
                        img_5_selected.setVisibility(View.VISIBLE);
                        img_5_unselected.setVisibility(View.GONE);
                        img_last_selected[0] = img_5_selected;
                        img_last_unselected[0] = img_5_unselected;

                        Overallratings overallratings = new Overallratings();
                        try {
                            overallratings.setHeader(finalJsonObject.getString("header"));
                            overallratings.setCode(finalJsonObject.getString("code"));
                            overallratings.setSmiley("5");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(finalI, overallratings);
//                                exist = true;
                                break;
                            }

                        }

//                        if(!exist)
//                        {
//                            list_ratings.add(finalI,overallratings);
//                        }

                    }
                });


                if(fromWhere.equals("overallratings"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_overallratings.add(overallratings);
                }
                if(fromWhere.equals("billingexperience"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_billing_experience.add(overallratings);
                }

                if(fromWhere.equals("staffing"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_staffing.add(overallratings);
                }

                if(fromWhere.equals("brandpromoterhelpfulness"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_brandpromoterhelpfulness.add(overallratings);
                }

                if(fromWhere.equals("pricemismatch"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_pricemismatch.add(overallratings);
                }

                if(fromWhere.equals("storefacilities"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_storefacilities.add(overallratings);
                }

                if(fromWhere.equals("storelookfeel"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_storelookfeel.add(overallratings);
                }

                if(fromWhere.equals("storeservices"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_storeservices.add(overallratings);
                }

                if(fromWhere.equals("vm"))
                {
                    Overallratings overallratings = new Overallratings();
                    overallratings.setHeader(jsonObject.getString("header"));
                    overallratings.setCode(jsonObject.getString("code"));
                    overallratings.setSmiley(jsonObject.getString("smiley"));
                    list_vm.add(overallratings);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
