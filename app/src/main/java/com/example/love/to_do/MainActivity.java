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

        int id = item.getItemId();

        if (id == R.id.action_add_task) {
            Intent Addtodo = new Intent(this, AddToDo.class);
            startActivity(Addtodo);
        }

        if (id == R.id.reschedule) {
            //UpdateData();
            updateDates2();
            UpdateUI();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.toDoListItemTextview);
        String task = String.valueOf(taskTextView.getText());
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

    public void updateDates2() {
        //ArrayList ListDates = {""};
        String TEMPPCOLNAME = "checkdate";
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor csr;
        csr = db.query(TaskContract.TaskEntry.TABLE_NAME,new String[]{
                        // All existing columns
                        "*",
                        "rowid as uid",
                        // generate reformatted date column named checkdate usable by date functions
                        // i.e. converts dd/mm/yyyy to yyyy-mm-yy
                        "substr(" + TaskContract.TaskEntry.COLUMN_DATE + ",7,4)||'-'||" +
                                "substr(" + TaskContract.TaskEntry.COLUMN_DATE + ",4,2)||'-'||" +
                                "substr( " + TaskContract.TaskEntry.COLUMN_DATE + ",1,2) AS " + TEMPPCOLNAME },
                // where clause to only include properly formatted dates and those who date is less
                // than or equaly to today's date
                TEMPPCOLNAME + "<= date('now') AND " +
                        "substr(" + TaskContract.TaskEntry.COLUMN_DATE + ",3,1) = '/' AND " +
                        "substr(" + TaskContract.TaskEntry.COLUMN_DATE + ",6,1) = '/'",
                null,null,null,null
        );
        while (csr.moveToNext()) {
            String testdate = "02/11/2000";
            ContentValues cv = new ContentValues();
            cv.put(TaskContract.TaskEntry.COLUMN_DATE,
                    new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
            if (
                    db.update(TaskContract.TaskEntry.TABLE_NAME,cv,"rowid=?",new String[]{
                            String.valueOf(csr.getLong(csr.getColumnIndex("uid")))}
                    ) > 0) {
                Log.d("UPDT2","Row Updated OK.");
            } else {
                Log.d("UPDT2", "Update failed.");
            }
        }
        csr.close();
    }

    /*public void UpdateData() {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry._ID,
                        TaskContract.TaskEntry.COLUMN_DATE,
                },
                null, null, null, null, null);

        while (cursor.moveToNext())
        {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(c.getTime());
            String DateDB = cursor.getString(cursor.getColumnIndex("date"));
            Toast.makeText(this, DateDB, Toast.LENGTH_LONG).show();
            /*try {
                Date TaskDateDB = null;
                Date CurrentDate = null;
                TaskDateDB = formatter.parse(DateDB);
                CurrentDate = formatter.parse(formattedDate);
                if (TaskDateDB.before(CurrentDate)) {
                    Toast.makeText(this, "Under If", Toast.LENGTH_LONG).show();
                    ContentValues cv = new ContentValues();
                    int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DATE);
                    cv.put(TaskContract.TaskEntry.COLUMN_DATE, formattedDate);
                    db.update(TaskContract.TaskEntry.TABLE_NAME, cv, String.valueOf(idx1), null);
                    return;
                }
                else if (CurrentDate.before(TaskDateDB)){
                    Toast.makeText(this, "Under esle-if", Toast.LENGTH_LONG).show();
                }
            }
            try {
                Date CurrentDate = null;
                CurrentDate = formatter.parse(formattedDate);
                ContentValues cv = new ContentValues();
                cv.put(TaskContract.TaskEntry.COLUMN_DATE, formattedDate);
                db.update(TaskContract.TaskEntry.TABLE_NAME, cv, "strftime('%d/%m/%Y', DATE)" <, "strftime('%d/%m/%Y',CurrentDate)");
                return;
            }

            catch(ParseException e){

            }

        }*/

    }


















