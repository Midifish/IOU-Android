package com.midifish.iou;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataIO extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorDatabase.db";
    public static final String FAVOR_CHOICES_TABLE_NAME = "favorChoices";
    public static final String FAVORS_YOU_REQUESTED_TABLE_NAME = "favorsYouRequested";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPTION = "favorDescription";
    public static final String COLUMN_VALUE = "favorValue";

    public DataIO(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + FAVOR_CHOICES_TABLE_NAME
                        + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_DESCRIPTION + " TEXT,"
                        + COLUMN_VALUE + " INTEGER"
                        + ");"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + FAVORS_YOU_REQUESTED_TABLE_NAME
                        + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_DESCRIPTION + " TEXT,"
                        + COLUMN_VALUE + " INTEGER"
                        + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + FAVOR_CHOICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORS_YOU_REQUESTED_TABLE_NAME);
        onCreate(db);
    }

    public void addFavorChoice(String desc, int value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, desc);
        contentValues.put(COLUMN_VALUE, value);
        db.insert(FAVOR_CHOICES_TABLE_NAME, null, contentValues);
        db.close();
    }

    public void addFavorsYouRequested(String desc, int value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, desc);
        contentValues.put(COLUMN_VALUE, value);
        db.insert(FAVORS_YOU_REQUESTED_TABLE_NAME, null, contentValues);
        db.close();
    }

    public void deleteFavorChoice (Favor favor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVOR_CHOICES_TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[]{Integer.toString(favor.getId())});
        db.close();
    }

    public void deleteFavorsYouRequested (Favor favor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVORS_YOU_REQUESTED_TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[]{Integer.toString(favor.getId())});
        db.close();
    }

    public ArrayList<Favor> getFavorChoices(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + FAVOR_CHOICES_TABLE_NAME, null);
        ArrayList<Favor> favors = new ArrayList<>();
        Favor favor = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                favor = new Favor();
                favor.setId(cursor.getInt(0));
                favor.setDescription(cursor.getString(1));
                favor.setValue(cursor.getInt(2));
                favors.add(favor);
            } while (cursor.moveToNext());
        }
        db.close();
        return favors;
    }

    public ArrayList<Favor> getFavorsYouRequested(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + FAVORS_YOU_REQUESTED_TABLE_NAME, null);
        ArrayList<Favor> favors = new ArrayList<>();
        Favor favor = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                favor = new Favor();
                favor.setId(cursor.getInt(0));
                favor.setDescription(cursor.getString(1));
                favor.setValue(cursor.getInt(2));
                favors.add(favor);
            } while (cursor.moveToNext());
        }
        db.close();
        return favors;
    }
}