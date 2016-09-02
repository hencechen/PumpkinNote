package hence.com.pumpkinnote.database;

/**
 * Created by Hence on 2016/8/8.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Hence on 2016/7/31.
 */
public class noteDatabase {

    //SQL
    public static final String LOG_TAG = "Log";
    noteHelper helper;
    private voiceDatabase vdb;
    Context context;
    private int[] cnt={0,0,0,0,0,0};



    public noteDatabase(Context context) {

        this.context=context;
        Log.d(LOG_TAG, "create database");
        helper = new noteHelper(context);
        Log.d(LOG_TAG, "create database2");
        SQLiteDatabase db = helper.getWritableDatabase();
        Log.d(LOG_TAG, "create database3");
    }

    public boolean insertData(String noteid, String notetype, String notedate, String notedetail) {


        Log.d(LOG_TAG, "note id when insert " + noteid);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(noteHelper.NOTE_DATE, notedate);
        contentValues.put(noteHelper.NOTE_ID, noteid);
        contentValues.put(noteHelper.NOTE_DETAIL, notedetail);
        contentValues.put(noteHelper.NOTE_TYPE, notetype);
        contentValues.put(noteHelper.NOTE_IMG, " ");
        contentValues.put(noteHelper.NOTE_VOICE, " ");
        long resp = db.insert(noteHelper.TABLE_NAME, null, contentValues);
        Log.d(LOG_TAG, "after insert " + String.valueOf(resp));
        if (resp < 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteData(String noteid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(noteHelper.TABLE_NAME, noteHelper.NOTE_ID + "=?", new String[]{noteid});
        vdb=new voiceDatabase(context);
        vdb.deleteVoice(noteid);

    }

    public void updateData(String noteid, String notedetail) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(noteHelper.NOTE_DETAIL,notedetail);
        String []whereArgs={noteid};
        Log.d(LOG_TAG, "note update" + noteid);
        db.update(noteHelper.TABLE_NAME, contentValues,noteHelper.NOTE_ID+" =?",whereArgs);
    }
    public void updateType(String noteid, String notetype) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(noteHelper.NOTE_TYPE,notetype);
        String []whereArgs={noteid};
        Log.d(LOG_TAG, "note type update " + notetype+"note id"+ noteid);
        db.update(noteHelper.TABLE_NAME, contentValues,noteHelper.NOTE_ID+" =?",whereArgs);
    }

    public void updateImgflag(String noteid, String flag) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(noteHelper.NOTE_IMG,flag);
        String []whereArgs={noteid};
        Log.d(LOG_TAG, "note img update " + flag+"note id"+ noteid);
        db.update(noteHelper.TABLE_NAME, contentValues,noteHelper.NOTE_ID+" =?",whereArgs);
    }

    public void updateVicflag(String noteid, String flag) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(noteHelper.NOTE_VOICE,flag);
        String []whereArgs={noteid};
        Log.d(LOG_TAG, "note vic update " + flag+"note id"+ noteid);
        db.update(noteHelper.TABLE_NAME, contentValues,noteHelper.NOTE_ID+" =?",whereArgs);
    }

    public ArrayList<note> getAlldata() {

        int tcnt=0;
        int hcnt=0;
        int wcnt=0;
        int pcnt=0;
        int qcnt=0;
        int icnt=0;
        ArrayList<note> noteList = new ArrayList<note>();
        String[] col = {noteHelper.NOTE_ID, noteHelper.NOTE_TYPE, noteHelper.NOTE_DATE, noteHelper.NOTE_DETAIL,
                noteHelper.NOTE_IMG,noteHelper.NOTE_VOICE};
        note noted;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor;
        //      Log.d(LOG_TAG, "start to get data");
        cursor = db.query(noteHelper.TABLE_NAME, col, null, null, null, null, noteHelper.NOTE_ID+ " DESC");
        while (cursor.moveToNext()) {
            //         Log.d(LOG_TAG, "cursor moved"+ cursor.getString(3));

            noted = new note(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4) ,cursor.getString(5));
            noteList.add(noted);
            tcnt++;
            switch (noted.getNotetype()){
                case "1":
                    hcnt++;
                    break;
                case "2":
                    wcnt++;
                    break;
                case "3":
                    pcnt++;
                    break;
                case "4":
                    qcnt++;
                    break;
                case "5":
                    icnt++;
                    break;

            }
        }


        cnt[0]=tcnt;
        cnt[1]=hcnt;
        cnt[2]=wcnt;
        cnt[3]=pcnt;
        cnt[4]=qcnt;
        cnt[5]=icnt;

        Log.d(LOG_TAG,"array int cnt "+ tcnt + hcnt + wcnt + pcnt + qcnt+ icnt);

        return noteList;
    }

    public ArrayList<note> getAlldataByType(String type) {


        ArrayList<note> noteList = new ArrayList<note>();
        String[] col = {noteHelper.NOTE_ID, noteHelper.NOTE_TYPE, noteHelper.NOTE_DATE, noteHelper.NOTE_DETAIL,
                noteHelper.NOTE_IMG,noteHelper.NOTE_VOICE};
        note noted;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor;
        String []whereArgs={type};
        //      Log.d(LOG_TAG, "start to get data");
        cursor = db.query(noteHelper.TABLE_NAME, col, noteHelper.NOTE_TYPE+" =?",whereArgs, null, null, noteHelper.NOTE_ID+ " DESC");
        while (cursor.moveToNext()) {
            //         Log.d(LOG_TAG, "cursor moved"+ cursor.getString(3));

            noted = new note(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4) ,cursor.getString(5));
            noteList.add(noted);

        }

        return noteList;
    }

    public long getTottalcnt(){

        SQLiteDatabase db = helper.getReadableDatabase();
        long cnt= DatabaseUtils.queryNumEntries(db, helper.TABLE_NAME);
        return cnt;
    }
    public int[] getCnt() {
        return cnt;
    }

    public note getNote(String noteid){
        String[] col = {noteHelper.NOTE_ID, noteHelper.NOTE_TYPE, noteHelper.NOTE_DATE, noteHelper.NOTE_DETAIL,
                noteHelper.NOTE_IMG,noteHelper.NOTE_VOICE};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor;
        note noted=new note();
        String []whereArgs={noteid};
        cursor = db.query(noteHelper.TABLE_NAME, col,noteHelper.NOTE_ID+" =?",whereArgs, null, null, null);
        if (cursor.moveToNext()){
            noted = new note(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4) ,cursor.getString(5));
        }
        return noted;
    }

    static class noteHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "pumkinnote";
        private static final String TABLE_NAME = "NOTETABLE";
        private static final String NOTE_ID = "NOTEID";
        private static final String NOTE_TYPE = "NOTETYPE";
        private static final String NOTE_DATE = "NOTEDATE";
        private static final String NOTE_DETAIL = "NOTEDETAIL";
        private static final String NOTE_IMG = "NOTEIMGFLAG";
        private static final String NOTE_VOICE = "NOTEVICFLAG";
        private static final int DATE_BASE_VER = 3;
        private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;


        noteHelper(Context context) {

            super(context, DATABASE_NAME, null, DATE_BASE_VER);
            SQLiteDatabase db = getWritableDatabase();
            Log.d(LOG_TAG, "create database inner");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(LOG_TAG, "Start to create table");
            try {
                String sql = "CREATE TABLE " + TABLE_NAME + " (" + NOTE_ID + " INT NOT NULL, "
                        + NOTE_TYPE + " VARCHAR(1),"
                        + NOTE_DATE + " VCHAR(12),"
                        + NOTE_DETAIL + " VCHAR(255),"
                        + NOTE_IMG + " VARCHAR(1),"
                        + NOTE_VOICE + " VARCHAR(1),"
                        + " PRIMARY KEY (" + NOTE_ID + "));";
                db.execSQL(sql);
                Log.d(LOG_TAG, "DB created");

            } catch (SQLiteException se) {
                se.printStackTrace();
                Log.d(LOG_TAG, "failed in creation DB" + se.toString());
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "DB upgrade");
            db.execSQL(SQL_DELETE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "DB downgrade");
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}
