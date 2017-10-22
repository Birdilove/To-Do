package com.example.love.to_do;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.love.to_do.db.TaskContract;
import com.example.love.to_do.db.TaskDbHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.id;

@TargetApi(Build.VERSION_CODES.N)
public class Reschedule_Activity extends AppCompatActivity {

    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private SimpleCursorAdapter mAdapter;
    private Date d;
    private String date;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.reschedule_list);
        UpdateUI();
        UpdateData();

    }

    private void UpdateUI() {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry._ID,
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COLUMN_DATE,
                        TaskContract.TaskEntry.COL_TIME,
                        TaskContract.TaskEntry.COL_PRIORITY,
                        TaskContract.TaskEntry.COL_IMAGE_HEADER,
                },
                null, null, null, null, null);
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.item_todo,
                cursor,
                // Displayed data source column names
                new String[]{
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COLUMN_DATE,
                        TaskContract.TaskEntry.COL_TIME,
                        TaskContract.TaskEntry.COL_PRIORITY,
                        TaskContract.TaskEntry.COL_IMAGE_HEADER},
                // respective layout id's for the displayed data
                new int[]{
                        R.id.toDoListItemTextview,
                        R.id.Task_date_View,
                        R.id.Task_Time_View,
                        R.id.priority,
                        R.id.toDoListItemColorImageView},
                0
        );
        //SimpleDateFormat formatter  = new SimpleDateFormat("dd/MM/yyyy");
        //String str1 = TaskContract.TaskEntry.COLUMN_DATE;
        //Date date1 = null;
        //date1 = formatter.parse(str1);

        //Calendar c = Calendar.getInstance();
        //if (c.before(date1)){
        //    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
        //}
        mTaskListView.setAdapter(mAdapter);
    }

    public void UpdateData() {
        ContentValues cv = new ContentValues();
        cv.put("title", "Bob"); //These Fields should be your String values of actual column names
        cv.put("date", "19");
        cv.put("priority", "Male");
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.update(TaskContract.TaskEntry.TABLE_NAME, cv, "_id="+id, null);
        finish();
    }
}
