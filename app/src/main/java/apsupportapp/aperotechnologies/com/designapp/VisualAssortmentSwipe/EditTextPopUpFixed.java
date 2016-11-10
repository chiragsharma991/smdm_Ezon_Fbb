package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mpatil on 29/01/16.
 */

//For editText Focus clearance on back button press in keyboard
public class EditTextPopUpFixed extends AppCompatEditText
{
    private static final int TIME_SLEEP = 500;
    private Context ctx;
    private EditTextListener mListener;
    private int backTimes = 0;

    public EditTextPopUpFixed(Context context)
    {
        super(context);
        ctx = context;
    }

    public EditTextPopUpFixed(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        ctx = context;
    }

    public EditTextPopUpFixed(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        ctx = context;
    }

    public void setEditTextListener(EditTextListener lis)
    {
        mListener = lis;
        this.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                backTimes = 0;
                return false;
            }
        });
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
        {
            InputMethodManager mgr = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

                mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            this.clearFocus();

            return true;
        }
        return false;
    }

    public interface EditTextListener
    {
        void revalidateEditText(); //Revalidates the EditText to refresh the Popup (Must revalidate the EditText in its implementation)
        void close(); // Method to close the activity or fragment (Must finish activity or go back in its implementation)
    }
}