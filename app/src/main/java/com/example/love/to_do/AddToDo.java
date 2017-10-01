package com.example.love.to_do;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.love.to_do.db.TaskContract;
import com.example.love.to_do.db.TaskDbHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class AddToDo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    String date;

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = +dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        EditText ToDoDate = (EditText) findViewById(R.id.TodoDate);
        ToDoDate.setText(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        Button AddTask = (Button) findViewById(R.id.Addbutton);
        AddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ToDoTask = (EditText) findViewById(R.id.TodoTask);
                EditText ToDoDate = (EditText) findViewById(R.id.TodoDate);
                ToDoDate.setText(date);
                String task = String.valueOf(ToDoTask.getText());
                String taskdate = String.valueOf(ToDoDate.getText());
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues valuesdate = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                //values.put(TaskContract.TaskEntry.COL_TASK_DATE, taskdate);
                //valuesdate.put(TaskContract.TaskEntry.COL_TASK_DATE, taskdate);
                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                //db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, valuesdate, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
            }
        });

        EditText ToDoDate = (EditText) findViewById(R.id.TodoDate);
        ToDoDate.setOnClickListener(new View.OnClickListener()

        {
            public void onClick (View view){
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(AddToDo.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }
}
