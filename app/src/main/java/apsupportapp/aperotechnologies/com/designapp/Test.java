package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSummaryAdapter;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.clickChild;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class Test extends AppCompatActivity implements clickChild {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ArrayList<String>list=new ArrayList<>();
        list.add("Blue");
        list.add("Green");
        list.add("Orange");
        list.add("Black");

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.colleboration_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        recyclerView.setOnFlingListener(null);
        new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
        TestingAdapter testingAdapter = new TestingAdapter(list,this);
        recyclerView.setAdapter(testingAdapter);
        Log.e("TAG", "list size is: "+list.size());
    }


    @Override
    public void onClick(String value) {

    }
}

class TestingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener
{

    private final Context context;
    private final ArrayList<String> list;
    private static boolean check=false;
    private clickChild Interface_clickListner;


    public TestingAdapter(ArrayList<String> list, Context context) {
        this.list=list;
        this.context=context;
        this.Interface_clickListner=(clickChild)context;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_test_child, parent, false);
        return new TestingAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TestingAdapter.Holder) {
            if(position < list.size()) {

                ((Holder)holder).colour_selection.setText(list.get(position));
                ((TestingAdapter.Holder)holder).colour_selection.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_right_black_24dp,0);


                ((TestingAdapter.Holder)holder).colour_selection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Interface_clickListner.onClick("1");
                        if(check==false)
                        {
                            check=true;
                            ((Holder) holder).discrition.setVisibility(View.VISIBLE);
                            ((TestingAdapter.Holder)holder).colour_selection.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_down_black_24dp,0);

                        }else
                        {
                            check=false;
                            ((Holder) holder).discrition.setVisibility(View.GONE);
                            ((TestingAdapter.Holder)holder).colour_selection.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_right_black_24dp,0);


                        }

                    }
                });




            }
        }


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onSnap(int position) {

    }



    private static class Holder extends RecyclerView.ViewHolder {

        TextView colour_selection;
        LinearLayout discrition;
        public Holder(View itemView) {
            super(itemView);
            colour_selection=(TextView)itemView.findViewById(R.id.colour_text);
            discrition=(LinearLayout) itemView.findViewById(R.id.discrition_option);



        }

    }
}
