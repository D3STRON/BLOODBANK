package blood.com.bloodbank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "BLOOD_BANK";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BANK = "bank";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BANK_TABLE = "CREATE TABLE IF NOT EXISTS bank ( location TEXT, name TEXT, email TEXT, contact TEXT);";
        db.execSQL(CREATE_BANK_TABLE);
        Log.d(TAG, "bank table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bank");
        onCreate(db);
    }

    public void addInfo(String c, String e, String l, String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO bank VALUES ('"+l+"','"+n+"','"+e+"','"+c+"');";
        db.execSQL(insert);
        Log.d(TAG, "Values inserted sucessfully");
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("bank", null, null);
        db.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public boolean isExist(){
        String select = "SELECT * FROM bank;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        if(cursor.getCount() < 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public HashMap<String, String> getAllValues() {
        HashMap<String, String> values = new HashMap<>();
        String select = "SELECT * FROM bank;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            values.put("location", cursor.getString(0));
            values.put("name", cursor.getString(1));
            values.put("email", cursor.getString(2));
            values.put("contact", cursor.getString(3));
        } else {
            Log.e(TAG,"CRITICAL ERROR!!");
            return null;
        }
        cursor.close();
        Log.d(TAG,"Fetching bank: " + values.toString());
        return  values;
    }

}