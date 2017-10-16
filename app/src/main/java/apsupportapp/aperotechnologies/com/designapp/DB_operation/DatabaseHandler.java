package apsupportapp.aperotechnologies.com.designapp.DB_operation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.model.Login_StoreList;
import apsupportapp.aperotechnologies.com.designapp.model.TestModel;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "storeDetails";

    private static final String TABLE_NAME = "storeMapping";

    private String TAG="DatabaseHandler";

    private static final String KEY_ID = "id";
    private static final String KEY_ONE = "geoLevel2Code";
    private static final String KEY_TWO = "kpiId";
    private static final String KEY_THREE = "lobId";
    private static final String KEY_FOUR = "lobName";
    private static final String KEY_FIVE = "geoLevel2Desc";
    private static final String KEY_SIX = "hierarchyLevels";
    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ---" );



        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ONE + " VARCHAR,"
                + KEY_TWO + " VARCHAR," + KEY_THREE + " VARCHAR,"+ KEY_FOUR + " VARCHAR,"
                + KEY_FIVE + " VARCHAR,"+ KEY_SIX + " VARCHAR" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade: -old ver--"+oldVersion+" new ver "+newVersion );

        /*if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE storeMapping ADD COLUMN hierarchyLevels VARCHAR");
        }*/
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void db_AddData(ArrayList<Login_StoreList> loginStoreArray) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i <loginStoreArray.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_ONE, loginStoreArray.get(i).getGeoLevel2Code());
            values.put(KEY_TWO, loginStoreArray.get(i).getKpiId());
            values.put(KEY_THREE, loginStoreArray.get(i).getLobId());
            values.put(KEY_FOUR, loginStoreArray.get(i).getLobName());
            values.put(KEY_FIVE, loginStoreArray.get(i).getGeoLevel2Desc());
            values.put(KEY_SIX, loginStoreArray.get(i).getHierarchyLevels());
            db.insert(TABLE_NAME, null, values);
        }
        db.close(); // Closing database connection
       // saveintoSdcard();
        db_GetContactsCount();
    }

    private void saveintoSdcard() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            Log.e(TAG, "sd: "+sd );

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/storeDetails";
                Log.e(TAG, "currentDBPath: "+currentDBPath );
                String backupDBPath = "store.db";
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

    public Login_StoreList db_GetOneRowDetails(String lobName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query( TABLE_NAME, new String[] { KEY_ID,
                        KEY_ONE, KEY_TWO,KEY_THREE,KEY_FOUR,KEY_FIVE,KEY_SIX }, KEY_FOUR + "=?",
                new String[] { String.valueOf(lobName) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Login_StoreList data = new Login_StoreList();
        data.setGeoLevel2Code(cursor.getString(1));
        data.setKpiId(cursor.getString(2));
        data.setLobId(cursor.getString(3));
        data.setLobName(cursor.getString(4));
        data.setGeoLevel2Desc(cursor.getString(5));
        data.setHierarchyLevels(cursor.getString(6));
        return data;
    }

    public List<Login_StoreList> db_GetListWhereClause(String geoLevel2Desc) {
        List<Login_StoreList> dataList = new ArrayList<Login_StoreList>();
        //Cursor cursorr =  db.rawQuery("select * from " + DATABASE_TABLE_EHS + " where " + TASK_ID + "='" + taskid + "'" , null);

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE geoLevel2Desc = '"+geoLevel2Desc+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Login_StoreList data = new Login_StoreList();
                data.setGeoLevel2Code(cursor.getString(1));
                data.setKpiId(cursor.getString(2));
                data.setLobId(cursor.getString(3));
                data.setLobName(cursor.getString(4));
                data.setGeoLevel2Desc(cursor.getString(5));
                data.setHierarchyLevels(cursor.getString(6));
                Log.i(TAG, "db_GetListWhereClause: "+data.getGeoLevel2Code()+" and 2 "+data.getKpiId()+" and 3 "+data.getLobId()+" and 4 "+data.getLobName()+" and 5 is "+data.getGeoLevel2Desc() );
                // Adding contact to list
                dataList.add(data);
                // Log.i(TAG, "cursor GetAllContacts: "+cursor.moveToNext() );
            } while (cursor.moveToNext());
        }
        return dataList;
    }
    public List<Login_StoreList> db_GetListMulipleWhereClause(String lobName, String selectconcept) {
        List<Login_StoreList> dataList = new ArrayList<Login_StoreList>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE lobName = '"+lobName+"' AND geoLevel2Desc = '"+selectconcept+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Login_StoreList data = new Login_StoreList();
                data.setGeoLevel2Code(cursor.getString(1));
                data.setKpiId(cursor.getString(2));
                data.setLobId(cursor.getString(3));
                data.setLobName(cursor.getString(4));
                data.setGeoLevel2Desc(cursor.getString(5));
                data.setHierarchyLevels(cursor.getString(6));
                Log.i(TAG, "db_GetListMulipleWhereClause: "+data.getGeoLevel2Code()+" and 2 "+data.getKpiId()+" and 3 "+data.getLobId()+" and 4 "+data.getLobName()+" and 5 is "+data.getGeoLevel2Desc() );
                // Adding contact to list
                dataList.add(data);
                // Log.i(TAG, "cursor GetAllContacts: "+cursor.moveToNext() );
            } while (cursor.moveToNext());
        }
        return dataList;
    }

    public List<Login_StoreList> db_GetAllContacts() {
        List<Login_StoreList> dataList = new ArrayList<Login_StoreList>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Login_StoreList data = new Login_StoreList();
                data.setGeoLevel2Code(cursor.getString(1));
                data.setKpiId(cursor.getString(2));
                data.setLobId(cursor.getString(3));
                data.setLobName(cursor.getString(4));
                data.setGeoLevel2Desc(cursor.getString(5));
                data.setHierarchyLevels(cursor.getString(6));
                // Adding contact to list
                dataList.add(data);
               // Log.i(TAG, "cursor GetAllContacts: "+cursor.moveToNext() );
            } while (cursor.moveToNext());
        }
        Log.i(TAG, "db_GetAllContacts: "+dataList.size() );
        return dataList;
    }

    public int db_UpdateContact(TestModel model,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ONE, model.getAttribute1());
        values.put(KEY_TWO, model.getAttribute2());
        values.put(KEY_THREE, model.getAttribute3());
        values.put(KEY_FOUR, 999);
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void db_DeleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }


    public int db_GetContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.e(TAG, "db_GetContactsCount: -***- "+cursor.getCount() );
        cursor.close();
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