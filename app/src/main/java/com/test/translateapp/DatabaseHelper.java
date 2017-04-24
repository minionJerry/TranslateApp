package com.test.translateapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.translateapp.models.TextModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Каныкей on 22.03.2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TranslatedText.db";
    public static final String TABLE_NAME = "favorite";
    public static final String TABLE_TEXT = "text";
    public static final String KEY_ID = "id";
    public static final String KEY_TEXT = "text";
    public static final String KEY_TRANSLTEXT = "translatedText";
    public static final String KEY_LANG = "lang";
    public static final String KEY_ISFAVORITE = "isFavorite";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
   }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String request = "CREATE TABLE  IF NOT EXISTS "+ TABLE_TEXT + "(" + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TEXT
        + " TEXT," + KEY_TRANSLTEXT + " TEXT," + KEY_LANG + " TEXT," +  KEY_ISFAVORITE + " NUMERIC,"+  "UNIQUE(" + KEY_TEXT + "," + KEY_LANG + ")" + "ON CONFLICT REPLACE)";
        db.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT);
        onCreate(db);
    }

    public void addText(TextModel text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, text.getText());
        values.put(KEY_TRANSLTEXT, text.getTranslateText());
        values.put(KEY_LANG, text.getLang());
        values.put(KEY_ISFAVORITE, text.isFavorite());
        db.insert(TABLE_TEXT,null,values);
        db.close();
    }

    public void changeToPositiveStatus(TextModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        int newValue = 1;
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISFAVORITE, newValue);
        db.update(TABLE_TEXT, cv, KEY_ID + "= ?", new String[] {String.valueOf(model.getId())});
        db.close();
    }

    public void changeToNegativeStatus(TextModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        int newValue = 0;
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISFAVORITE, newValue);
        db.update(TABLE_TEXT, cv, KEY_ID + "= ?", new String[] {String.valueOf(model.getId())});
        db.close();
    }

    public TextModel getText(String text,String translatedText,String destLang) {
        SQLiteDatabase db = this.getReadableDatabase();
        TextModel newText = null;
        Cursor cursor = db.query(TABLE_TEXT, new String[] { KEY_ID,
                        KEY_TEXT, KEY_TRANSLTEXT, KEY_LANG, KEY_ISFAVORITE }, KEY_TEXT + "=? AND " + KEY_TRANSLTEXT + "=? AND " + KEY_LANG + "=? ",
                new String[] { String.valueOf(text), String.valueOf(translatedText), String.valueOf(destLang)}, null, null, null, null);
        if (cursor!=null && cursor.moveToFirst()) {
            newText = new TextModel(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getInt(4));
            cursor.close();
            db.close();
        }
        return newText;
    }

    public List<TextModel> getAllTexts() {
        List<TextModel> textList = new ArrayList<TextModel>();
        String request = "SELECT * FROM " + TABLE_TEXT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(request,null);

        if(cursor.moveToFirst()){
            do {
                TextModel favorite = new TextModel();
                favorite.setId(cursor.getInt(0));
                favorite.setText(cursor.getString(1));
                favorite.setTranslateText(cursor.getString(2));
                favorite.setLang(cursor.getString(3));
                favorite.setFavorite(cursor.getInt(4));
                textList.add(favorite);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return textList;
    }

    public List<TextModel> getAllFavoriteTexts() {
        List<TextModel> textList = new ArrayList<TextModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEXT, new String[] { KEY_ID,
                        KEY_TEXT, KEY_TRANSLTEXT, KEY_LANG, KEY_ISFAVORITE }, KEY_ISFAVORITE + "=?",
                new String[] {"1"}, null, null, null, null);
            if(cursor.moveToFirst()){
                do {
                    TextModel favorite = new TextModel();
                    favorite.setId(cursor.getInt(0));
                    favorite.setText(cursor.getString(1));
                    favorite.setTranslateText(cursor.getString(2));
                    favorite.setLang(cursor.getString(3));
                    favorite.setFavorite(cursor.getInt(4));
                    textList.add(favorite);
                }while (cursor.moveToNext());
            };
                cursor.close();
                db.close();
        return textList;
    }

    public int getTextCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TEXT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }

    public int updateText(TextModel text) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, text.getText());
        values.put(KEY_TRANSLTEXT, text.getTranslateText());
        values.put(KEY_LANG, text.getLang());
        values.put(KEY_ISFAVORITE,text.isFavorite());
        db.close();
        return db.update(TABLE_TEXT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(text.getId()) });
    }

    public void deleteText(TextModel text) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEXT, KEY_ID + " = ?",
                new String[] { String.valueOf(text.getId()) });
        db.close();
    }
}
