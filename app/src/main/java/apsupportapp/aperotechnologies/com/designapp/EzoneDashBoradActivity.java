package apsupportapp.aperotechnologies.com.designapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by pamrutkar on 18/05/17.
 */
public class EzoneDashBoradActivity extends DashBoardActivity
{
        public void Ezone_DshBoard(View view)
        {
            TextView textView = (TextView)view.findViewById(R.id.textView1);
            textView.setText("Welcome");
        }
}
