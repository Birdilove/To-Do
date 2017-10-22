package com.example.love.to_do;

//import android.app.DatePickerDialog;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.example.love.to_do.db.TaskContract;
import com.example.love.to_do.db.TaskDbHelper;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ListView mTaskListView1;
    //private ArrayAdapter<String> mAdapter;
    //private ArrayAdapter<String> mAdapter1;
    SimpleCursorAdapter mAdapter;
    private Cursor mCursor;
    private String orderBy;
    private SQLiteDatabase mDB;
    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        UpdateUI();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent Addtodo = new Intent(this, AddToDo.class);
                startActivity(Addtodo);
            case R.id.reschedule:
                //Intent reschedule = new Intent(this, Reschedule_Activity.class);
                //startActivity(reschedule);
                UpdateData();
                UpdateUI();
        }
        return false;
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.toDoListItemTextview);
        //TextView taskDateView = (TextView) parent.findViewById(R.id.dateTextView);
        String task = String.valueOf(taskTextView.getText());
        //String taskdate = String.valueOf(taskDateView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        UpdateUI();
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
        mTaskListView.setAdapter(mAdapter);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }


    public void UpdateData() {
        Calendar cal = Calendar.getInstance();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry._ID,
                        TaskContract.TaskEntry.COLUMN_DATE,
                },
                null, null, null, null, null);
        while (cursor.moveToFirst())
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(c.getTime());
            String GetDate = cursor.getString(cursor.getColumnIndex("date"));

            try {
                Date date1 = null;
                Date date2 = null;
                date1 = formatter.parse(GetDate);
                date2 = formatter.parse(formattedDate);

                if (date1.compareTo(date2) < 0) {

                    ContentValues cv = new ContentValues();
                    cv.put(TaskContract.TaskEntry.COLUMN_DATE, formattedDate); //These Fields should be your String values of actual column names
                    db.update(TaskContract.TaskEntry.TABLE_NAME, cv, "_id="+1, null);
                    Toast.makeText(this, "DAMN! IT WORKS", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            catch(ParseException e){

            }
        }
            cursor.close();
    }

}
















