package hence.com.pumpkinnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by Hence on 2016/8/20.
 */

public class voiceDatabase {

    //SQL
    public static final String LOG_TAG = "Log";
    voiceHelper helper;

    public voiceDatabase (Context context) {

        helper = new voiceHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        Log.d(LOG_TAG, "create  voice database3");
    }

    public boolean insertData(String voiceid, String voicefile) {
        Log.d(LOG_TAG, "voice b4 insert " + voiceid+"/"+voicefile);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(voiceHelper.VOICE_ID, voiceid);
        contentValues.put(voiceHelper.VOICE_FILE, voicefile);
        long resp = db.insert(voiceHelper.TABLE_NAME, null, contentValues);
        Log.d(LOG_TAG, "voice after insert " + String.valueOf(resp));
        if (resp < 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateData(String voiceid, String voicefile){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(voiceHelper.VOICE_FILE,voicefile);
        String []whereArgs={voiceid};
        Log.d(LOG_TAG, "note update" + voiceid);
        db.update(voiceHelper.TABLE_NAME, contentValues, voiceHelper.VOICE_ID+" =?",whereArgs);
    }

    public String getData(String voiceId){

        Log.d(LOG_TAG, "voice get"+voiceId);
        String filepath=null;
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] col = {voiceHelper.VOICE_FILE};
        String selection = voiceHelper.VOICE_ID+"=?";
        String[] selectionArgs = new  String[]{ voiceId};
        Cursor cursor;
        cursor=db.query(voiceHelper.TABLE_NAME,col,selection,selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            filepath=cursor.getString(0);
        }
        Log.d(LOG_TAG, "voice path get" +filepath );
        return filepath;
    }

    public boolean deleteVoice(String voiceId){
        String mFileName=getData(voiceId);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(voiceHelper.TABLE_NAME, voiceHelper.VOICE_ID + "=?", new String[]{voiceId});
        if (mFileName==null){
            return false;
        }
        File audioVoice = new File(mFileName);
        return ((audioVoice.delete()? true:false));

    }


    static class voiceHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "pumkinvoice";
        private static final String TABLE_NAME = "VOICETABLE";
        private static final String VOICE_ID = "NOTEID";
        private static final String VOICE_FILE = "NOTETYPE";

        private static final int DATE_BASE_VER = 1;
        private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        voiceHelper(Context context) {

            super(context, DATABASE_NAME, null, DATE_BASE_VER);
            SQLiteDatabase db = getWritableDatabase();
            Log.d(LOG_TAG, "create voice database inner");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(LOG_TAG, "Start to create table");
            try {
                String sql = "CREATE TABLE " + TABLE_NAME + " (" + VOICE_ID + " INT NOT NULL, " + VOICE_FILE + " VARCHAR(255)," +
                        " PRIMARY KEY (" + VOICE_ID + "));";
                db.execSQL(sql);
                Log.d(LOG_TAG, "Voice DB created");

            } catch (SQLiteException se) {
                se.printStackTrace();
                Log.d(LOG_TAG, "failed in creation DB" + se.toString());
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, " voice DB upgrade");
            db.execSQL(SQL_DELETE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "voice DB downgrade");
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
