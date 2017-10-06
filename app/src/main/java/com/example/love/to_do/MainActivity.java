package com.example.love.to_do;

//import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.example.love.to_do.db.TaskContract;
import com.example.love.to_do.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ListView mTaskListView1;
    //private ArrayAdapter<String> mAdapter;
    //private ArrayAdapter<String> mAdapter1;
    SimpleCursorAdapter mAdapter;
    private Cursor mCursor;
    private SQLiteDatabase mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView)findViewById(R.id.list_todo);
        mTaskListView1 = (ListView)findViewById(R.id.list_todo);
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
        }
        return false;
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        //TextView taskDateView = (TextView) parent.findViewById(R.id.dateTextView);
        String task = String.valueOf(taskTextView.getText());
        //String taskdate = String.valueOf(taskDateView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry.COL_TASK_TITLE  +  " = ?",
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
                },
                null, null, null, null, null);
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.item_todo,
                cursor,
                // Displayed data source column names
                new String[]{
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COLUMN_DATE},
                // respective layout id's for the displayed data
                new int[]{
                        R.id.task_title,
                        R.id.dateTextView},
                0
        );

        mTaskListView.setAdapter(mAdapter);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }
}







    /*private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String> taskList1 = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry._ID,
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COLUMN_DATE,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
            Log.d("DBTITLE","Title extracted from Cursor = " + cursor.getString(idx) + " for Row " + cursor.getPosition());
            int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DATE);
            taskList1.add(cursor.getString(idx1));

        }

        if   (mAdapter == null) {
            final ArrayList<String> filteredList = new ArrayList<>();

            SomeCustomAdapter adapter = new SomeCustomAdapter(this, taskList);


            //mAdapter = new ArrayAdapter<>(this, R.layout.item_todo, R.id.task_title, taskList);
            //mAdapter1 = new ArrayAdapter<>(this, R.layout.item_todo, R.id.dateTextView, taskList1);
            //mTaskListView.setAdapter(mAdapter);
            //mTaskListView1.setAdapter(mAdapter1);
        }
        else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
            //mAdapter1.clear();
            //mAdapter1.addAll(taskList1);
            //mAdapter1.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }







/* final EditText taskEditText = new EditText(this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Add a new task")
                    .setMessage("What do you want to do next?")
                    .setView(taskEditText);

                LayoutInflater inflater = this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.multipleedittext, null));
                builder.setNeutralButton("Select Date", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance( MainActivity.this,
                               now.get(Calendar.YEAR),
                               now.get(Calendar.MONTH),
                               now.get(Calendar.DAY_OF_MONTH)
                                );
                                dpd.show(getFragmentManager(), "Datepickerdialog");

                            }
                })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                builder.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);*/
////