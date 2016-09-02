package hence.com.pumpkinnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hence on 2016/8/22.
 */

public class imageDatabase {
    //SQL
    public static final String LOG_TAG = "Log";
    imageHelper helper;

    public imageDatabase(Context context) {

        helper = new imageHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        Log.d(LOG_TAG, "create  image database3");
    }

    public boolean insertData(String imageId, String imagefile) {
        Log.d(LOG_TAG, "image b4 insert " + imageId + "/" + imagefile);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(imageHelper.IMAGE_ID, imageId);
        contentValues.put(imageHelper.IMAGE_FILE, imagefile);
        long resp = db.insert(imageHelper.TABLE_NAME, null, contentValues);
        Log.d(LOG_TAG, "voice after insert " + String.valueOf(resp));
        if (resp < 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateData(String voiceid, String voicefile) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(imageHelper.IMAGE_FILE, voicefile);
        String[] whereArgs = {voiceid};
        Log.d(LOG_TAG, "note update" + voiceid);
        db.update(imageHelper.TABLE_NAME, contentValues, imageHelper.IMAGE_ID + " =?", whereArgs);
    }

    public ArrayList<String> getAllImageData(String voiceId) {

        ArrayList<String> imageFile = new ArrayList<>();
        Log.d(LOG_TAG, "image get" + voiceId);
        String filepath = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] col = {imageHelper.IMAGE_FILE};
        String selection = imageHelper.IMAGE_ID + "=?";
        String[] selectionArgs = new String[]{voiceId};
        Cursor cursor;
        cursor = db.query(imageHelper.TABLE_NAME, col, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            filepath = cursor.getString(0);
            imageFile.add(filepath);
        }
        Log.d(LOG_TAG, "voice path get" + filepath);
        return imageFile;
    }

    public boolean deleteImagebyId(String imageId) {
        ArrayList<String> mFileName = getAllImageData(imageId);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(imageHelper.TABLE_NAME, imageHelper.IMAGE_ID + "=?", new String[]{imageId});

        if (mFileName == null) {
            return false;
        }
        for (int i = 0; i < mFileName.size(); i++) {
            File imageFIle = new File(mFileName.get(i));
            imageFIle.delete();
        }
       return true;

    }

    public boolean deleteImagebyPath(String imagePath) {

        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(imageHelper.TABLE_NAME, imageHelper.IMAGE_FILE + "=?", new String[]{imagePath});
        Log.d(LOG_TAG, "voice path get" + imagePath);

            File imageFIle = new File(imagePath);
            imageFIle.delete();

        return true;

    }


    static class imageHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "pumkinimage";
        private static final String TABLE_NAME = "IMAGETABLE";
        private static final String IMAGE_ID = "IMGID";
        private static final String IMAGE_FILE = "IMGPATH";

        private static final int DATE_BASE_VER = 2;
        private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        imageHelper(Context context) {

            super(context, DATABASE_NAME, null, DATE_BASE_VER);
            SQLiteDatabase db = getWritableDatabase();
            Log.d(LOG_TAG, "create IMAGE database inner");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(LOG_TAG, "Start to create table");
            try {
                String sql = "CREATE TABLE " + TABLE_NAME + " (" + IMAGE_ID + " INT NOT NULL, " + IMAGE_FILE + " VARCHAR(255)" +
                        ");";
                db.execSQL(sql);
                Log.d(LOG_TAG, "IMAGE DB created");

            } catch (SQLiteException se) {
                se.printStackTrace();
                Log.d(LOG_TAG, "failed in creation IMAGE DB" + se.toString());
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, " IMAGE DB upgrade");
            db.execSQL(SQL_DELETE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "IMAGE DB downgrade");
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}