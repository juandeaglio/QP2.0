package com.example.qp;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.qp.R;

import org.w3c.dom.Text;

import java.util.Calendar;


public class CreateTaskDialogHandler extends AppCompatDialogFragment implements  TimePickerDialog.OnTimeSetListener {
    private TextView taskNameDialog;
   // private ExampleDialogListener listener;
   private String taskTimeValue = "";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_task_dialog, null);

        builder.setView(view)
                .setTitle("Create Task");
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                       // String password = editTextPassword.getText().toString();
//                        //listener.applyTexts(taskName, "");
//                    }
//                });


        TextView time = view.findViewById(R.id.taskTimeDialog);

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DialogFragment timePicker = new TimePickerFragment();
                //todo: Finish getting the suppoertFragmentManager()
                //timePicker.show(M.getSupportFragmentManager(), "time picker");

            }
        });

        Button saveButtonDialog = view.findViewById(R.id.saveTaskButtonDialog);

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String taskName = taskNameDialog.getText().toString();

            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView taskTime = (TextView) view.findViewById(R.id.taskNameDialog);
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
        taskTime.setText(tempText + ":" + minuteStr + " " + am_pm);
        taskTime.setVisibility(View.VISIBLE);

        this.taskTimeValue = String.valueOf(tempText) + ":" + minuteStr + " " + am_pm;

    }
}