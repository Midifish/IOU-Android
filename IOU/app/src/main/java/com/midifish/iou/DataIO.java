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
    private ArrayList<String> favorChoicesStrings;
    private ArrayList<String> favorsYouRequestedStrings;

    public DataIO(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        favorChoicesStrings = new ArrayList<>();
        favorsYouRequestedStrings = new ArrayList<>();
        refreshFavorChoicesStrings();
        refreshFavorsYouRequestedStrings();
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
    }

    public void addFavorsYouRequested(Favor favor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, favor.getDescription());
        contentValues.put(COLUMN_VALUE, favor.getValue());
        db.insert(FAVORS_YOU_REQUESTED_TABLE_NAME, null, contentValues);
    }

    public void deleteFavorChoice (Favor favor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVOR_CHOICES_TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[]{Integer.toString(favor.getId())});
    }

    public void deleteFavorYouRequested (Favor favor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVORS_YOU_REQUESTED_TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[]{Integer.toString(favor.getId())});
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
        return favors;
    }

    public void refreshFavorChoicesStrings(){
        favorChoicesStrings.clear();
        ArrayList<Favor> choices = getFavorChoices();
        //build string list for favor choices
        String current = "";
        for (Favor favor : choices) {
            current = favor.getDescription();
            current += " " + Integer.toString(favor.getValue());
            favorChoicesStrings.add(current);
        }
    }

    public void refreshFavorsYouRequestedStrings(){
        favorsYouRequestedStrings.clear();
        ArrayList<Favor> yourRequests = getFavorsYouRequested();
        //build string list for favor choices
        String current = "";
        for (Favor favor : yourRequests) {
            current = favor.getDescription();
            current += " " + Integer.toString(favor.getValue());
            favorsYouRequestedStrings.add(current);
        }
    }

    public ArrayList<String> getFavorChoicesStrings() {
        return favorChoicesStrings;
    }

    public ArrayList<String> getFavorsYouRequestedStrings() {
        return favorsYouRequestedStrings;
    }

    public void close() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.close();
    }
}