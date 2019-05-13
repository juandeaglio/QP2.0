package com.example.qp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class TaskCardRecyclerAdapter extends RecyclerView.Adapter<TaskCardRecyclerAdapter.TaskCardViewHolder> {


    private DatabaseHelper db ;
    private ArrayList<Task> taskList;
    MainActivity mainActivity = new MainActivity();
    private Context context;
    private ColorManager colorManager;
    private Toast toast;
    private String taskIDV;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private NumberPicker.OnValueChangeListener mNumberSetListener;



    public class TaskCardViewHolder extends RecyclerView.ViewHolder {
        CardView taskCard;
        TextView taskName;
        TextView priority;
        TextView dueDate;
        TextView timeDue;
        CheckBox checkBox;
        UUID taskID;

        public TaskCardViewHolder(View v)
        {
            super(v);
            taskCard = v.findViewById(R.id.task_card);
            taskName = (TextView) v.findViewById(R.id.card_task_name);
            priority = (TextView) v.findViewById(R.id.card_priority);
            dueDate = (TextView) v.findViewById(R.id.card_due_date);
            checkBox = v.findViewById(R.id.card_check_box);
            timeDue = (TextView) v.findViewById(R.id.card_time);
        }
    }

    public TaskCardRecyclerAdapter (ArrayList<Task> taskList, Context context)
    {
        this.taskList = taskList;
        this.db = new DatabaseHelper(context);
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void onBindViewHolder(final TaskCardViewHolder taskCardViewHolder, int i)
    {
        final Task task = taskList.get(i);
        this.taskIDV = task.getTaskId().toString();
        colorManager = MainActivity.colorManager;
        taskCardViewHolder.taskName.setText(task.getTaskName());
        taskCardViewHolder.priority.setTextColor(Color.parseColor("#000000"));
        taskCardViewHolder.priority.setText(Integer.toString(task.getPriority()));
        taskCardViewHolder.dueDate.setText(dateCorrection(task.getDueDate()));
        taskCardViewHolder.timeDue.setText(task.getTimeDueDate());
        taskCardViewHolder.taskID = task.getTaskId();

        taskCardViewHolder.checkBox.setOnCheckedChangeListener(null);
        taskCardViewHolder.checkBox.setButtonTintList(ColorStateList.valueOf(colorManager.getColorAccent()));

        taskCardViewHolder.taskName.setTextColor(colorManager.getColorText());
        taskCardViewHolder.dueDate.setTextColor(colorManager.getColorText());
        taskCardViewHolder.priority.setTextColor(colorManager.getColorText());
        taskCardViewHolder.timeDue.setTextColor(colorManager.getColorText());
        if(task.getCompleted() == 0)
        {
            taskCardViewHolder.checkBox.setChecked(false);
        }
        else
            taskCardViewHolder.checkBox.setChecked(true);
        taskCardViewHolder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(context, ViewTask.class);
//                intent.putExtra("taskid", task.getTaskId().toString());
//                context.startActivity(intent);

                showViewTaskDialog(task.getTaskId().toString());
            }


        });

        taskCardViewHolder.taskCard.setBackgroundColor(colorManager.getColorPrimaryDark());
       // mainActivity.registerForContextMenu(taskCardViewHolder.taskCard);

        taskCardViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(db.markTaskCompleted(task.getTaskId().toString())){
                        // System.out.println("True");
                        mainActivity.populateArrayList(db, mainActivity.sortSelector);
                        mainActivity.populateCompletedTaskList(db, mainActivity.sortSelector);
                        updateData();
                    }
                    else {
                        System.out.println("False");
                    }
                }
                else
                {
                    if(db.unCheckCompletedTask(task.getTaskId().toString())){
                        mainActivity.populateArrayList(db, mainActivity.sortSelector);
                        mainActivity.populateCompletedTaskList(db, mainActivity.sortSelector);
                        updateData();
                    }
                }

            }
        }
        );

    }


    public Task getTaskFromList(String taskID){
        Task currentTask = new Task();
        for (Task loopTask: MainActivity.globalTaskList) {
            if (loopTask.getTaskId().toString().equals(taskID)){
                currentTask = loopTask;
                return currentTask;
            }
        }
        return null; //Error
    }


    public void showViewTaskDialog(final String taskID){
        final Dialog vtDialog = new Dialog(context);
        vtDialog.setTitle("View Task");
        vtDialog.setContentView(R.layout.view_task_dialog);
        vtDialog.findViewById(R.id.toolbar3).setBackgroundColor(colorManager.getColorAccent());
        vtDialog.show();
        Task currentTask = getTaskFromList(taskID);

        TextView viewTaskName = (TextView)vtDialog.findViewById(R.id.stageNameDialog);
        viewTaskName.setText(currentTask.getTaskName());

        TextView viewTaskTime = (TextView) vtDialog.findViewById(R.id.stageTime);
        viewTaskTime.setText(currentTask.getTimeDueDate());

        viewTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = Calendar.HOUR_OF_DAY;
                int minute = Calendar.MINUTE;
                TimePickerDialog timePicker = new TimePickerDialog(context, mTimeSetListener,hourOfDay,minute, false );

                //timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TextView taskTime = (TextView) vtDialog.findViewById(R.id.stageTime);
                String am_pm = "";
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    am_pm = "AM";
                } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    am_pm = "PM";
                }
                String tempText = (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + "";
                String minuteStr = "";

                if (minute <= 9) {
                    minuteStr = "0" + String.valueOf(minute);
                } else {
                    minuteStr = String.valueOf(minute);
                }
                //taskTimeValue = tempText + ":" + minuteStr + " " + am_pm;
                taskTime.setText(tempText + ":" + minuteStr + " " + am_pm);
            }
        };





        //TODO: Make taskTime and taskDueDate listeners like in create Task dialog - Ethan

        final TextView viewTaskDueDate = (TextView) vtDialog.findViewById(R.id.stageDueDate);
        viewTaskDueDate.setText(currentTask.getDueDate());

        viewTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Material_Dialog_Alert, mDateSetListener, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                viewTaskDueDate.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                //taskDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);

            }
        };

        EditText viewTaskDescription = (EditText) vtDialog.findViewById(R.id.taskDescription);
        viewTaskDescription.setText(currentTask.getDescription());

        final TextView viewTaskPriority = (TextView) vtDialog.findViewById(R.id.taskPriorityDialog);
        viewTaskPriority.setText(String.valueOf(currentTask.getPriority()));

        viewTaskPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker();

            }
        });
        mNumberSetListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewTaskPriority.setText(String.valueOf(newVal));
            }
        };


        final Button deleteButton = (Button) vtDialog.findViewById(R.id.deleteButton);
        deleteButton.setBackgroundColor(colorManager.getColorAccent());
        //deleteButton.setCompoundDrawableTintList(ColorStateList.valueOf(colorManager.getColorText()));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePrompt deletePrompt = new DeletePrompt();
                DatabaseHelper db = new DatabaseHelper(context);
                db.deleteTask(taskID);

                mainActivity.populateArrayList(db, mainActivity.sortSelector);
                updateData();
                vtDialog.dismiss();


            }
        });

        Button saveButton = (Button) vtDialog.findViewById(R.id.saveTaskButtonDialog);
        saveButton.setBackgroundColor(colorManager.getColorAccent());
        //saveButton.setCompoundDrawableTintList(ColorStateList.valueOf(colorManager.getColorText()));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText taskName = (EditText) vtDialog.findViewById(R.id.stageNameDialog);
                if(taskName.getText().length() == 0){
                    taskName.setError("Task Name cannot be Blank");
                    return;
                }

                TextView dueDate = vtDialog.findViewById(R.id.stageDueDate);
                if(dueDate.getText().toString().length() == 0){
                    dueDate.setError("Task Due Date cannot be blank");
                    return;
                }

                TextView taskTime = (TextView) vtDialog.findViewById(R.id.stageTime);
                if(taskTime.getText().toString().length() == 0){
                    taskTime.setError("Task Time cannot be blank");
                    return;
                }

                EditText taskNotes = (EditText) vtDialog.findViewById(R.id.taskDescription);
                if(taskNotes.getText().length() == 0){
                    taskNotes.setText(""); // IIf user didn't specify priority just set to 1
                }

                TextView priority = (TextView) vtDialog.findViewById(R.id.taskPriorityDialog);
                if(priority.getText().length() == 0){
                    priority.setText("1"); // IIf user didn't specify priority just set to 1
                }

                boolean updateCompleted = db.updateTable(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()),dueDate.getText().toString(), taskNotes.getText().toString(), 0, UUID.fromString(taskIDV), taskTime.getText().toString());

                //TODO: when having 2 tasks on home screen. If you change the details of one task the task will overwrite itself with data form other task

                if(updateCompleted)
                {
                    Calendar calendar = Calendar.getInstance();
                    Intent intent1 = new Intent(context, BroadCastService.class);
                    intent1.putExtra("Task Name",taskName.getText().toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

                    String taskDueDate = taskTime.getText().toString();
                    String hour = taskDueDate.substring( 0, taskDueDate.indexOf(":"));
                    String minute = taskDueDate.substring(taskDueDate.indexOf(":")+1, taskDueDate.indexOf(" "));

                    int hourToInt = Integer.parseInt(hour);
                    int minuteToInt = Integer.parseInt(minute);

                    calendar.set(Calendar.HOUR_OF_DAY, hourToInt);
                    calendar.set(Calendar.MINUTE, minuteToInt);
                    calendar.set(Calendar.SECOND,0);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
                    mainActivity.populateArrayList(db, mainActivity.sortSelector);
                    updateData();
                    toast = Toast.makeText(context, "Task edits saved!", Toast.LENGTH_SHORT);
                    toast.show();
                    vtDialog.dismiss();
                }
                else
                {
                    toast = Toast.makeText(mainActivity,"Failed to save Task edits", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });



    }


    public void showNumberPicker() {
        final Dialog numPicker = new Dialog(context);
        numPicker.setTitle("NumberPicker");
        numPicker.setContentView(R.layout.number_picker_dialog);
        Button b1 = (Button) numPicker.findViewById(R.id.button1);
        Button b2 = (Button) numPicker.findViewById(R.id.button2);
        final NumberPicker np = numPicker.findViewById(R.id.numberPicker1);

        np.setMaxValue(5); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(mNumberSetListener);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nPDialog.setText(String.valueOf(np.getValue()));
                numPicker.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numPicker.dismiss(); // dismiss the dialog
            }
        });
        numPicker.show();


    }




    private String dateCorrection(String date)
    {
        String dateArr [];
        dateArr = date.split("/");
        int month = Integer.parseInt(dateArr[0]);
        date = month + "/" + dateArr[1] + "/" + dateArr[2];
        return date;
    }

    @Override
    public TaskCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);
        return new TaskCardViewHolder(itemView);
    }



    public void updateData()
    {
        notifyDataSetChanged();
    }


}
