package apsupportapp.aperotechnologies.com.designapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by pamrutkar on 18/05/17.
 */
public class EzoneDashBoradActivity extends DashBoardActivity1
{
        public void Ezone_DshBoard(ViewGroup view)
        {
            TextView textView = (TextView)findViewById(R.id.textView1);
            textView.setText("Welcome");
        }
}
