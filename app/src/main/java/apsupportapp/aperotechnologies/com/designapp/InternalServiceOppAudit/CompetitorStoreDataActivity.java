package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import apsupportapp.aperotechnologies.com.designapp.ExternalServiceOppAudit.CustomSeekBar;
import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.Constants.overall_progress;

public class CompetitorStoreDataActivity extends AppCompatActivity {

    private Context context;
    private TextView txt_nameofauditor, txt_dateofvisit, txt_dayofweek, txt_timeofvisit, txt_auditortype, txt_name, txt_time, txt_custname, txt_mobile, txt_keytakeawys;
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
    private RelativeLayout imageBtnBack1;
    private Button btn_approve, btn_reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_store_data);
        getSupportActionBar().hide();

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
        overall_progress = 0;

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

        txt_nameofauditor = (TextView) findViewById(R.id.txt_nameofauditor);
        txt_dateofvisit = (TextView) findViewById(R.id.txt_dateofvisit);
        txt_dayofweek = (TextView) findViewById(R.id.txt_dayofweek);
        txt_timeofvisit = (TextView) findViewById(R.id.txt_timeofvisit);
        txt_auditortype = (TextView) findViewById(R.id.txt_auditortype);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_custname = (TextView) findViewById(R.id.txt_custname);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_keytakeawys = (TextView) findViewById(R.id.txt_keytakeawys);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        btn_approve = (Button) findViewById(R.id.btn_approve);
        btn_reject = (Button) findViewById(R.id.btn_reject);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout mSeekLin = (LinearLayout) findViewById(R.id.lin1);
        CustomSeekBar customSeekBar = new CustomSeekBar(this, 10, Color.BLACK);
        customSeekBar.addSeekBar(mSeekLin);

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
