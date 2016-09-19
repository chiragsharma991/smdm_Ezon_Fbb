package com.toptoche.searchablespinnerlibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

public class SearchableListDialog extends DialogFragment implements
         TextWatcher {

    private static final String ITEMS = "items";

    public static  ListAdapter listAdapter;

    private ListView _listViewItems;

    private SearchableItem _searchableItem;


    private EditText _searchView;

    private String _strTitle;

    private String _strPositiveButtonText;

    private DialogInterface.OnClickListener _onClickListener;

    public SearchableListDialog() {
        Log.e("SearchableListDialog "," blank constructor");
    }

    public static SearchableListDialog newInstance(List items) {

        Log.e("SearchableListDialog ","  newInstance");

        SearchableListDialog multiSelectExpandableFragment = new
                SearchableListDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);

        multiSelectExpandableFragment.setArguments(args);

        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("SearchableListDialog ","  onCreate");

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("SearchableListDialog ","  onCreateView");

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.e("SearchableListDialog ","  onCreateDialog");

        // Getting the layout inflater to inflate the view in an alert dialog.
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (null != savedInstanceState) {
            _searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }
        // Change End

        View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView, _strTitle);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

        String strPositiveButton = _strPositiveButtonText == null ? "CLOSE" : _strPositiveButtonText;
        alertDialog.setPositiveButton(strPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                _searchView.clearFocus();
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(_searchView.getWindowToken(), 0);
            }
        });

        String strTitle = _strTitle == null ? "Select Item" : _strTitle;

        Log.e("strTitle"," "+strTitle);
        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);



        return dialog;
    }

    // Crash on orientation change #7
    // Change Start
    // Description: Saving the instance of searchable item instance.
    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.e("SearchableListDialog ","  onSaveInstanceState");

        outState.putSerializable("item", _searchableItem);
        super.onSaveInstanceState(outState);
    }
    // Change End

    public void setTitle(String strTitle) {
        Log.e("SearchableListDialog ","  setTitle");

        _strTitle = strTitle;
    }



    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {

        Log.e("SearchableListDialog ","  setOnSearchableItemClickListener");
        this._searchableItem = searchableItem;
    }


    private void setData(View rootView, String _strTitle) {
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
//                .SEARCH_SERVICE);
        Log.e("SearchableListDialog ","  setData"+" "+_strTitle);

        _searchView = (EditText) rootView.findViewById(R.id.search);

        _searchView.addTextChangedListener(this);
        _searchView.clearFocus();

        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(_searchView.getWindowToken(), 0);

        List items = (List) getArguments().getSerializable(ITEMS);

        _listViewItems = (ListView) rootView.findViewById(R.id.listItems);


        listAdapter = new ListAdapter(items, getActivity());

        //attach the adapter to the list
        _listViewItems.setAdapter(listAdapter);

        _listViewItems.setTextFilterEnabled(true);

        _listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("here "," "+listAdapter.getItem(position));
                _searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });
    }


//
//    @Override
//    public boolean onQueryTextSubmit(String s) {
//        _searchView.clearFocus();
//        return true;
//    }

//    @Override
//    public boolean onQueryTextChange(String s) {
////        listAdapter.filterData(s);
//
//        //Log.e("s"," ----- "+s);
//
//        if (TextUtils.isEmpty(s)) {
////                _listViewItems.clearTextFilter();
//            ((ArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(null);
//        } else {
//            ((ArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(s);
//        }
//        if (null != _onSearchTextChanged) {
//            _onSearchTextChanged.onSearchTextChanged(s);
//        }
//        return true;
//    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        listAdapter.getFilter().filter(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        listAdapter.getFilter().filter(s);
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }


}
