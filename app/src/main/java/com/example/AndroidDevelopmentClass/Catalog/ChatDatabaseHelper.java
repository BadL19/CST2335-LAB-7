package com.example.AndroidDevelopmentClass.Catalog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ARSIA on 11/13/2016.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static  final String TABLE_NAME = "CHATS";
    private static final String DATABASE_NAME = "Chats.db";
    private static final int VERSION_NUM = 1;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MESSAGE = "_empty msg";

    //Database creation sql statement:
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "( "
            + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_MESSAGE
            + " text not null);";



    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ChatDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }
}
