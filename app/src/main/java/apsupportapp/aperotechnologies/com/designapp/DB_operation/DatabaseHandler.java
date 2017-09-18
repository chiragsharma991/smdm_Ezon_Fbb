package apsupportapp.aperotechnologies.com.designapp.DB_operation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import apsupportapp.aperotechnologies.com.designapp.model.TestModel;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "storeDetails";

    // Contacts table name
    private static final String TABLE_NAME = "storeMapping";

    private String TAG="DatabaseHandler";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ONE = "geo_level2_desc";
    private static final String KEY_TWO = "geo_level2_code";
    private static final String KEY_THREE = "kpi_id";
    private static final String KEY_FOUR = "lob_name";
    private static final String KEY_FIVE = "lob_id";
    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ---" );



        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ONE + " TEXT,"
                + KEY_TWO + " TEXT," + KEY_THREE + " TEXT,"
                + KEY_FOUR + " TEXT," + KEY_FIVE + " INTEGER"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade: ---" );

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void db_AddData(TestModel model) {
        Log.e(TAG, "db_AddData: " );

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ONE, model.getAttribute1());
        values.put(KEY_TWO, model.getAttribute2());
        values.put(KEY_THREE, model.getAttribute3());
        values.put(KEY_FOUR, model.getAttribute4());
        values.put(KEY_FIVE, model.getAttribute5());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
        Toast.makeText(context,"add data success",Toast.LENGTH_SHORT).show();

         db_GetContactsCount();
    }

    // Getting single contact
    TestModel db_GetContact(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query( TABLE_NAME, new String[] { KEY_ID,
                        KEY_ONE, KEY_TWO,KEY_THREE,KEY_FOUR,KEY_FIVE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TestModel data = new TestModel(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));
        // return contact
        return data;
    }

    // Getting All Contacts
    public List<TestModel> db_GetAllContacts() {
        List<TestModel> dataList = new ArrayList<TestModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TestModel data = new TestModel();
                data.setId(cursor.getInt(0));
                data.setAttribute1(cursor.getString(1));
                data.setAttribute2(cursor.getString(2));
                data.setAttribute3(cursor.getString(3));
                data.setAttribute4(cursor.getString(4));
                data.setAttribute5(cursor.getInt(5));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataList;
    }

    // Updating single contact
    public int db_UpdateContact(TestModel model,int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ONE, model.getAttribute1());
        values.put(KEY_TWO, model.getAttribute2());
        values.put(KEY_THREE, model.getAttribute3());
        values.put(KEY_FOUR, model.getAttribute4());
        values.put(KEY_FOUR, 999);


        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // Deleting single contact
    public void db_DeleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }


    // Getting contacts Count
    public int db_GetContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.e(TAG, "db_GetContactsCount: "+cursor.getCount() );
        cursor.close();


        // return count
        return cursor.getCount();
    }

    public void deleteAllData()
    {
        SQLiteDatabase sdb= this.getWritableDatabase();
        sdb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
       // sdb.delete(TABLE_NAME, null, null);
        onCreate(sdb);

    }

}