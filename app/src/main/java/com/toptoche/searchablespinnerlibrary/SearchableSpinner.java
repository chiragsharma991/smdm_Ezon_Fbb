package com.toptoche.searchablespinnerlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.StyleActivity;

public class SearchableSpinner extends Spinner implements View.OnTouchListener,
        SearchableListDialog.SearchableItem {

    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private List _items;

    private SearchableListDialog _searchableListDialog;

    private boolean _isDirty;
    public static ArrayAdapter _arrayAdapter;
    private String _strHintText;
    private boolean _isFromInit;
    String title;

    public SearchableSpinner(Context context) {


        super(context);
        this._context = context;
        init();
        Log.e("SearchableSpinner "," a");
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
        Log.e("SearchableSpinner "," b");
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
        Log.e("SearchableSpinner "," c");
    }

    private void init() {

        Log.e("SearchableSpinner "," init");

        _items = new ArrayList();
        _searchableListDialog = SearchableListDialog.newInstance
                (_items);
        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

        _arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                    .simple_list_item_1, new String[]{_strHintText});
            _isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            Log.e("SearchableSpinner "," onTouch "+title);

            if (null != _arrayAdapter) {

                // Refresh content #6
                // Change Start
                // Description: The items were only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                _items.clear();

                //Log.e("_items after clear "," "+_items.size());

                if(title.equals("Select Collection"))
                {
                    //Log.e("_in "," Select Collection");

                    if(DashBoardActivity._collectionitems.size() == 0)
                    {
                        //Log.e("_in "," _collectionitems size zero");

                        for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                            _items.add(_arrayAdapter.getItem(i));

                        }
                        DashBoardActivity._collectionitems.addAll(_items);
                    }
                    else
                    {

                        //Log.e("_in "," _collectionitems size not equal to zero");
                        _items.addAll(DashBoardActivity._collectionitems);
                        SearchableListDialog.listAdapter.notifyDataSetChanged();

                    }
                }else {
                    //Log.e("_in "," Select Option");

                    for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                        _items.add(_arrayAdapter.getItem(i));

                    }
                }

//                for(int i=0; i <_items.size();i++){
//                    Log.e("items ====>"," "+_items.get(i));
//                }

                // Change end.
                _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");
            }
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {

        Log.e("SearchableSpinner "," setAdapter");

        if (!_isFromInit) {
            _arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                        .simple_list_item_1, new String[]{_strHintText});
                super.setAdapter(arrayAdapter);
                Log.e("arrayadapter","====="+arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            } else {
                super.setAdapter(adapter);
                Log.e("adapter","====="+adapter);
            }

        } else {
            _isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    @Override
    public void onSearchableItemClicked(Object item, int position) {
        Log.e("SearchableSpinner "," onSearchableItemClicked " + item);

        setSelection(_items.indexOf(item));


        if (!_isDirty) {
            _isDirty = true;
            setAdapter(_arrayAdapter);
            setSelection(_items.indexOf(item));
        }
    }

    public void setTitle(String strTitle) {
        Log.e("SearchableSpinner "," setTitle");

        title = strTitle;
        _searchableListDialog.setTitle(strTitle);
    }



    private Activity scanForActivity(Context cont) {

        Log.e("SearchableSpinner "," scanForActivity");

        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        Log.e("SearchableSpinner "," getSelectedItemPosition");

        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        Log.e("SearchableSpinner "," getSelectedItem");

        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }
}