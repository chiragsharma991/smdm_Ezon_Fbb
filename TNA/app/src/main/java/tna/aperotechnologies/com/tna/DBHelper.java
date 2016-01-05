package tna.aperotechnologies.com.tna;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hasai
 * on 18/12/15.
  */
// Class For Creating Tables
//Table creation is done here
public class DBHelper extends SQLiteOpenHelper
{

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "TNA.db";

    private static DBHelper mInstance = null;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //LOGIN TABLE CREATION
       String CREATE_TABLE_LOGIN = "CREATE TABLE " + LL_Table_Columns.LOGINTABLE + "("
                + LL_Table_Columns.Login_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + LL_Table_Columns.Login_USER_ID + " TEXT, "
                + LL_Table_Columns.Login_DESC + " TEXT) ";

                db.execSQL(CREATE_TABLE_LOGIN);

        

        //TNAs TABLE CREATION
        String CREATE_TABLE_TNA = "CREATE TABLE " + LL_Table_Columns.TNATABLE + "("
                + " Id " + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + LL_Table_Columns.TNA_ID + " TEXT, "
                + LL_Table_Columns.TNA_DESC + " TEXT, "
                + LL_Table_Columns.TNA_SELECT + " TEXT, "
                + LL_Table_Columns.TNA_DATECHANGE + " TEXT) ";
        db.execSQL(CREATE_TABLE_TNA);


        //Activitys TABLE CREATION
        String CREATE_TABLE_ACTIVITY = "CREATE TABLE " + LL_Table_Columns.ACTIVITYTABLE + "("
                + " Id " + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + LL_Table_Columns.TNAACT_ID + " TEXT, "
                + LL_Table_Columns.ACT_ID + " TEXT, "
                + LL_Table_Columns.ACT_DESC + " TEXT, "
                + LL_Table_Columns.ACT_TLMVALUEID + " INTEGER, "
                + LL_Table_Columns.ACT_FIELDNAME + " TEXT, "
                + LL_Table_Columns.ACT_DATATYPE + " TEXT, "
                + LL_Table_Columns.ACT_FIELDCODE + " TEXT, "
                + LL_Table_Columns.ACT_VALUES + " TEXT, "
                + LL_Table_Columns.ACT_ISCHECKED + " TEXT) ";

        db.execSQL(CREATE_TABLE_ACTIVITY);


        //OptionCodes TABLE CREATION
        String CREATE_TABLE_OPTIONCODE = "CREATE TABLE " + LL_Table_Columns.OPTIONCODETABLE + "("
                + " Id " + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + LL_Table_Columns.TNAOPT_ID + " TEXT, "
                + LL_Table_Columns.OPT_PARENT_VAL + " TEXT, "
                + LL_Table_Columns.OPT_CHILD_VAL + " TEXT) ";

        db.execSQL(CREATE_TABLE_OPTIONCODE);

        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LL_Table_Columns.LOGINTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LL_Table_Columns.TNATABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LL_Table_Columns.ACTIVITYTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LL_Table_Columns.OPTIONCODETABLE);
    }
}

