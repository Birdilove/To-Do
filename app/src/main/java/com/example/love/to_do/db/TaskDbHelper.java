package com.example.love.to_do.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL )" ;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE ADD_NAME ADD COLUMN NEW_COLOUM INTEGER DEFAULT 0"); // You can add TEXT Field
        }
        onCreate(db);
    }
}