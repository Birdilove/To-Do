package com.example.love.to_do;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.love.to_do.db.TaskContract;
import com.example.love.to_do.db.TaskDbHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class AddToDo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    //Button tododate = (Button)findViewById(R.id.newToDoChooseDateButton);
    String date;

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = +dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        //tododate.setText(date);
        //EditText ToDoDate = (EditText) findViewById(R.id.toDoCustomTextInput);
       // ToDoDate.setText(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        FloatingActionButton AddTask = (FloatingActionButton) findViewById(R.id.makeToDoFloatingActionButton);
        AddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTextInputLayout ToDoTask = (CustomTextInputLayout) findViewById(R.id.toDoCustomTextInput);

                Button ToDoDate = (Button) findViewById(R.id.newToDoChooseDateButton);
                ToDoDate.setText(date);
                String task = String.valueOf(ToDoTask.getEditText().getText());
                String taskdate = String.valueOf(ToDoDate.getText());
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                values.put(TaskContract.TaskEntry.COLUMN_DATE, taskdate);
                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                datatransfer();
                finish();
                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
            }
        });

        Button ToDoDate = (Button) findViewById(R.id.newToDoChooseDateButton);
        ToDoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void datatransfer(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}



