package com.example.love.to_do.db;

/**
 * Created by Love on 9/8/2017.
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_DATE = "date";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_IMAGE_HEADER = "header";
        public static final String COL_TIME = "time";
        public static final String COL_PRIORITY = "priority";
    }
}