package com.example.love.to_do;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.love.to_do.db.TaskContract;
import com.example.love.to_do.db.TaskDbHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static com.wdullaer.materialdatetimepicker.R.attr.colorPrimary;
import static java.util.logging.Logger.global;

public class AddToDo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private String date;
    private String time;
    private String Priority;
    private TextView Priority_TextView;
    private Date d;
    private DatePicker check;
    private Button ToDoDate;


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(dayOfMonth < 10 || monthOfYear < 10) {
            date = "0"+dayOfMonth + "/" + "0"+(monthOfYear + 1) + "/" + year;
            setDate(dayOfMonth,monthOfYear + 1,year);
        }
        else {
            date = +dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            setDate(dayOfMonth,monthOfYear + 1,year);
        }

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        time = "Time:"+hourOfDay+"h:"+minute+"m";
        Button ToDoTime = (Button) findViewById(R.id.newToDoChooseTimeButton);
        ToDoTime.setText(time);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        priorities();
        FloatingActionButton AddTask = (FloatingActionButton) findViewById(R.id.makeToDoFloatingActionButton);
        AddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTextInputLayout ToDoTask = (CustomTextInputLayout) findViewById(R.id.toDoCustomTextInput);

                Button ToDoTime = (Button) findViewById(R.id.newToDoChooseTimeButton);
                ToDoTime.setText(time);
                Button ToDoDate = (Button) findViewById(R.id.newToDoChooseDateButton);
                ToDoDate.setText(date);
                String task = String.valueOf(ToDoTask.getEditText().getText());
                String ImageHeader = task.substring(0,1);
                String taskdate = String.valueOf(ToDoDate.getText());
                String tasktime = String.valueOf(ToDoTime.getText());
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                values.put(TaskContract.TaskEntry.COLUMN_DATE, taskdate);
                values.put(TaskContract.TaskEntry.COL_IMAGE_HEADER, ImageHeader);
                values.put(TaskContract.TaskEntry.COL_TIME, tasktime);
                values.put(TaskContract.TaskEntry.COL_PRIORITY, Priority);
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

        Button ToDoTime = (Button) findViewById(R.id.newToDoChooseTimeButton);
        ToDoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddToDo.this, hour, minute, DateFormat.is24HourFormat(AddToDo.this));
                timePickerDialog.show(getFragmentManager(), "TimeFragment");
            }
        });
    }

    private void datatransfer(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void priorities(){
        final ToggleButton HighPriority = (ToggleButton)findViewById(R.id.HighPriority);
        HighPriority.setText("High");
        final ToggleButton MediumPriority = (ToggleButton)findViewById(R.id.MediumPriority);
        MediumPriority.setText("Medium");
        final ToggleButton LowPriority = (ToggleButton)findViewById(R.id.LowPriority);
        LowPriority.setText("Low");
        Priority_TextView = (TextView)findViewById(R.id.priority);
        HighPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MediumPriority.isChecked() || LowPriority.isChecked()){
                    MediumPriority.setChecked(false);
                    MediumPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    MediumPriority.setText("Medium");
                    LowPriority.setChecked(false);
                    LowPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    LowPriority.setText("Low");
                }

                if (HighPriority.isChecked()) {
                    HighPriority.setBackgroundResource(R.color.colorAccent);
                    HighPriority.setText("High");
                    Priority = "High";

                }
                else {
                    HighPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    HighPriority.setText("High");
                }
            }
        });

        MediumPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HighPriority.isChecked() || LowPriority.isChecked()){
                    HighPriority.setChecked(false);
                    HighPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    HighPriority.setText("High");
                    LowPriority.setChecked(false);
                    LowPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    LowPriority.setText("Low");
                }

                if (MediumPriority.isChecked()) {
                    MediumPriority.setBackgroundResource(R.color.colorAccent);
                    MediumPriority.setText("Medium");
                    Priority = "Medium";

                }
                else {
                    MediumPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    MediumPriority.setText("Medium");
                }
            }
        });

        LowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MediumPriority.isChecked() || HighPriority.isChecked()){
                    MediumPriority.setChecked(false);
                    MediumPriority.setText("Medium");
                    MediumPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    HighPriority.setChecked(false);
                    HighPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    HighPriority.setText("High");
                }

                if (LowPriority.isChecked()) {
                    LowPriority.setBackgroundResource(R.color.colorAccent);
                    LowPriority.setText("Low");
                    Priority = "Low";

                }
                else {
                    LowPriority.setBackgroundResource(R.color.colorPrimaryDark);
                    LowPriority.setText("Low");
                }
            }
        });
    }

    public void setDate(int year, int month, int day) {
        Calendar reminderCalendar = Calendar.getInstance();
        if(day < 10){
            day  = Integer.parseInt("0" + day);
        }
        reminderCalendar.set(year, month, day);
        ToDoDate = (Button) findViewById(R.id.newToDoChooseDateButton);
        ToDoDate.setText(date);

    }
}



