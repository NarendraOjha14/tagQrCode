package tag.tagqrcode;


import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupak Adhikari on 1/16/2015.
 * Handles the sqlite database operation
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="qrcodest.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "qrcodest";
    public static final String KEY_ID = "_id";
    public static final String KEY_CODE = "_qr_code";
    public static final String KEY_DATE ="_read_date";
  //final  SQLiteDatabase db = this.getWritableDatabase();

    public DataBaseHandler(Context context){

        super(context,DATABASE_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CODE +" NVARCHAR(255), " + KEY_DATE + " NVARCHAR(255) );";
                 db.execSQL(DATABASE_CREATE);
 }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS" +TABLE_NAME);
        onCreate(db);

 }
  /*
  * insert the value into sqlite database
  * and return an objecttype of Qrcode
  *
  * */
    public QrCode addQr(QrCode qr){
        ContentValues values = new ContentValues();
        values.put(KEY_CODE,qr.getQr_Code());
        values.put(KEY_DATE,qr.getDate());
        SQLiteDatabase db = this.getWritableDatabase();

        long qrdID = db.insert(TABLE_NAME,null,values);
        qr.setId((int) qrdID);
        db.close();
        return qr;
    }

/*
* get the selection from database and return list of that values
* */
    public List<QrCode> getAll(){
        List<QrCode> qrCodes = new ArrayList<QrCode>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null );
        if(cursor.moveToFirst()){
            do{
                QrCode q = new QrCode(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
                 qrCodes.add(q);
            }while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return qrCodes;
    }
/*
* clears the all value of table
*
*
* */
    public void  clearTable()   {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null,null);


    }
}
