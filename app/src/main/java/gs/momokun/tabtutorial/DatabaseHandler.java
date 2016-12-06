package gs.momokun.tabtutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ElmoTan on 12/6/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {



    private static final int DB_VER = 1;
    private static final String DB_NAME = "sensorLoggingManager";
    private static final String TABLE_SENSOR = "sensor";

    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TEMP = "temp";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SENSOR + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_TEMP + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR);

        // Create tables again
        onCreate(db);
    }

    public void addContact(DataLogging dl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, dl.get_date()); // Contact Name
        values.put(KEY_TEMP, dl.get_temp()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_SENSOR, null, values);
        db.close(); // Closing database connection
    }

    public List<DataLogging> getAllContacts() {
        List<DataLogging> dataLogList = new ArrayList<DataLogging>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SENSOR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataLogging contact = new DataLogging();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.set_date(cursor.getString(1));
                contact.set_temp(cursor.getString(2));
                // Adding contact to list
                dataLogList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataLogList;
    }
}
