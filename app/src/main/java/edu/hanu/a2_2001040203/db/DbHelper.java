package edu.hanu.a2_2001040203.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "product.db";
    private static final int DB_VERSION = 2;
    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "thumbnail TEXT, " +
                "name TEXT, " +
                "price TEXT, " +
                "amount INTEGER, " +
                "category TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        onCreate(db);
    }
}
