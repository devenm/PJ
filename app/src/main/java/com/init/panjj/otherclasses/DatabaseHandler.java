package com.init.panjj.otherclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.init.panjj.model.RecomendBean;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recommendManager";
    private static final int DATABASE_VERSION = 2;
    private static final String KEY_ALBUMNAME = "albumname";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_ID = "id";
    private static final String KEY_PH_NO = "phone";
    private static final String TABLE_RECOMMEND = "recommend";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recommend(id INTEGER PRIMARY KEY,genre TEXT,phone TEXT,artist TEXT,albumname TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recommend");
        onCreate(db);
    }

    public void addRecommend(RecomendBean recomendBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GENRE, recomendBean.getGenre());
        values.put(KEY_PH_NO, recomendBean.getPhoneNumber());
        values.put(KEY_ARTIST, recomendBean.getArtist());
        values.put(KEY_ALBUMNAME, recomendBean.getAlbumname());
        db.insert(TABLE_RECOMMEND, null, values);
        db.close();
    }

    public RecomendBean getRecommend(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_RECOMMEND, new String[]{KEY_ID, KEY_GENRE, KEY_PH_NO, KEY_ARTIST, KEY_ALBUMNAME}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new RecomendBean(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(DATABASE_VERSION));
    }

    public List<RecomendBean> getAllRecommend() {
        List<RecomendBean> recomendBeanList = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT  * FROM recommend", null);
        if (cursor.moveToFirst()) {
            do {
                RecomendBean recomendBean = new RecomendBean();
                recomendBean.setID(Integer.parseInt(cursor.getString(0)));
                recomendBean.setName(cursor.getString(1));
                recomendBean.setPhoneNumber(cursor.getString(DATABASE_VERSION));
                recomendBeanList.add(recomendBean);
            } while (cursor.moveToNext());
        }
        return recomendBeanList;
    }

    public int updateRecommend(RecomendBean recomendBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GENRE, recomendBean.getGenre());
        values.put(KEY_PH_NO, recomendBean.getPhoneNumber());
        values.put(KEY_ARTIST, recomendBean.getArtist());
        values.put(KEY_ALBUMNAME, recomendBean.getAlbumname());
        return db.update(TABLE_RECOMMEND, values, "id = ?", new String[]{String.valueOf(recomendBean.getID())});
    }

    public void deleteRecommend(RecomendBean recomendBean) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_RECOMMEND, "id = ?", new String[]{String.valueOf(recomendBean.getID())});
        db.close();
    }

    public int getRecommendCount() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM recommend", null);
        cursor.close();
        return cursor.getCount();
    }
}
