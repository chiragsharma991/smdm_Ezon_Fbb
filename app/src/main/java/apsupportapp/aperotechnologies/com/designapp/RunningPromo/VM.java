package apsupportapp.aperotechnologies.com.designapp.RunningPromo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import apsupportapp.aperotechnologies.com.designapp.R;


public class VM extends AppCompatActivity {
    public static ArrayList<String>list;
    private GridView gridView;
    RelativeLayout ImageBtnBack;
    private TextView tool;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vm);
        getSupportActionBar().hide();
        data=getIntent().getExtras();
        intialize();
        tool.setText(data.getString("VM"));
        list=new ArrayList<String>();
        list.add("http://lynnwoodtoday.com/wp-content/uploads/sites/4/2014/01/Jack-and-Jill-consignment-sale1.jpg");
        list.add("http://media.treehugger.com/assets/images/2014/08/clothes_761.jpg.662x0_q70_crop-scale.jpg");
        list.add("http://az616578.vo.msecnd.net/files/2016/04/16/6359636369325365532128639689_fashion2.jpg");
        list.add("http://showlovedress.com/wp-content/uploads/2016/03/fashion-models.jpg");

        VmAdapter vmAdapter=new VmAdapter(this,R.layout.activity_vm,list);
        gridView.setAdapter(vmAdapter);

    }

    private void intialize()
    {
         gridView=(GridView)findViewById(R.id.vmGrid);
         tool=(TextView)findViewById(R.id.toolbar_title);
         ImageBtnBack=(RelativeLayout)findViewById(R.id.vm_ImageBtnBack);

         ImageBtnBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(data.getString("FROM").equals("RunningPromo"))
                 {
                  finish();
                 }
                 else if(data.getString("FROM").equals("ExpirePromo"))
                 {
                   finish();
                 }
                 else {
                     finish();
                 }
             }
         });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(data.getString("FROM").equals("RunningPromo")) {

            finish();
        }
        else if(data.getString("FROM").equals("ExpirePromo"))
        {
            finish();
        }
        else
        {
          finish();
        }
    }
}
