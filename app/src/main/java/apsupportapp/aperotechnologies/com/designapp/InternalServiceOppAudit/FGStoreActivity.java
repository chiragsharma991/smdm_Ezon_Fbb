package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<Overallratings> list_overallratings;
    private List<Overallratings> list_billing_experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fgstore);
        context = this;
        list_overallratings = new ArrayList<>();
        list_billing_experience = new ArrayList<>();


        LinearLayout lin_lay_overall_ratings = (LinearLayout) findViewById(R.id.lin_lay_overall_ratings);

        create_Smiley(lin_lay_overall_ratings, "overallratings.json", list_overallratings, "overallratings");

        LinearLayout lin_lay_billing_experience = (LinearLayout) findViewById(R.id.lin_lay_billing_experience);
        create_Smiley(lin_lay_billing_experience, "billingexperience.json", list_billing_experience, "billingexperience");
       


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



                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
                                exist = true;
                                break;
                            }

                        }

                        if(!exist)
                        {
                            list_ratings.add(overallratings);
                        }


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



                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
                                exist = true;
                                break;
                            }

                        }

                        if(!exist)
                        {
                            list_ratings.add(overallratings);
                        }


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



                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
                                exist = true;
                                break;
                            }

                        }

                        if(!exist)
                        {
                            list_ratings.add(overallratings);
                        }

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



                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
                                exist = true;
                                break;
                            }

                        }

                        if(!exist)
                        {
                            list_ratings.add(overallratings);
                        }

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



                        Boolean exist = false;

                        for(int j = 0; j < list_ratings.size(); j++)
                        {
                            if(list_ratings.get(j).getHeader().equals(overallratings.getHeader()))
                            {
                                list_ratings.set(j, overallratings);
                                exist = true;
                                break;
                            }

                        }

                        if(!exist)
                        {
                            list_ratings.add(overallratings);
                        }

                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
