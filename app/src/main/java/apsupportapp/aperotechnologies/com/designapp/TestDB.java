package apsupportapp.aperotechnologies.com.designapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.DB_operation.DatabaseHandler;
import apsupportapp.aperotechnologies.com.designapp.model.TestModel;

public class TestDB extends AppCompatActivity {

    private DatabaseHandler db;
    private String TAG="TestDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);
        db = new DatabaseHandler(this);


        String str ="abc,cde,def,fgh";
        String kept = str.substring( 0, str.indexOf(","));
        String remainder = str.substring(str.indexOf(",")+1, str.length());

    }

    public void onCount(View view){
       // db.db_GetContactsCount();

    }

    public void onDelete(View view){
        db.db_DeleteContact();

    }
    public void onUpdate(View view){
        TestModel model=new TestModel();
        model.setAttribute1("update");
        model.setAttribute2("update");
        model.setAttribute3("update");
        model.setAttribute4("update");
        model.setAttribute5(999);
        db.db_UpdateContact(model,1);

    }

    public void onAllList(View view){
        ArrayList<TestModel>list= (ArrayList<TestModel>) db.db_GetAllContacts();
        for (TestModel model : list){
            Log.e(TAG, "onAllList: Lname is "+model.getAttribute4()+" Lid is "+model.getAttribute5() );
        }

    }

    public void addEvent(View view){
        EditText lob_name = (EditText) findViewById(R.id.lob_name);
        EditText lob_id = (EditText) findViewById(R.id.lob_id);
        String Lname=lob_name.getText().toString();
        int Lid=Integer.parseInt(lob_id.getText().toString());
        String kpi_id="kpi_id";
        String geo_level2_code="geo_level2_code";
        String geo_level2_desc="geo_level2_desc";
        TestModel model=new TestModel();
        model.setAttribute1(kpi_id);
        model.setAttribute2(geo_level2_code);
        model.setAttribute3(geo_level2_desc);
        model.setAttribute4(Lname);
        model.setAttribute5(Lid);
        db.db_AddData(model);



        try {
            File sd = Environment.getExternalStorageDirectory();
            Log.e(TAG, "sd: "+sd );

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/storeDetails";
                Log.e(TAG, "currentDBPath: "+currentDBPath );
                String backupDBPath = "backupname.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    Log.e(TAG, "currentDB: exists");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }


    }
}
