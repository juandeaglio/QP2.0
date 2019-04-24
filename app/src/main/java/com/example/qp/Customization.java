package com.example.qp;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Customization extends AppCompatActivity
{
    final int MAX_NUMBER_OF_COLORS = 3;
    int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //int defaultColorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        //int defaultColorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        //int defaultColorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        //int[] colorArr = {defaultColorPrimary, defaultColorPrimaryDark, defaultColorAccent};
        CardView card0 = (CardView) findViewById(R.id.card1);
        CardView card1 = (CardView) findViewById(R.id.card2);
        CardView card2 = (CardView) findViewById(R.id.card3);
        CardView[] cardArr = new CardView[]{card0, card1, card2};


        String nameArr[] = {"Operating Systems Final","Some sort of task", "Not an interesting task."};
        String priorityArr[] = {"1", "2", "4"};
        String dueDatesArr[] = {"Who knows?", "4/21/2086", "5/15/2019"};
        String timesArr[] = {"10:00 am", "4:39 pm", "2:00 pm"};


        //Creates the hardcoded cards, not really important.
        for(int i = 0; i < cardArr.length; i++)
        {
            CardView currentCard = cardArr[i];
            TextView taskName1 = (TextView) currentCard.findViewById(R.id.card_task_name);
            TextView priority1 = (TextView) currentCard.findViewById(R.id.card_priority);
            TextView dueDate1 = (TextView) currentCard.findViewById(R.id.card_due_date);
            TextView time1 = (TextView) currentCard.findViewById(R.id.card_time);

            taskName1.setText(nameArr[i]);
            priority1.setText(priorityArr[i]);
            dueDate1.setText(dueDatesArr[i]);
            time1.setText(timesArr[i]);
        }

        Button changeColorButton = findViewById(R.id.changeColorButton);
        changeColorButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                int currentColor = 0;
                for(int i = 0; i < MAX_NUMBER_OF_COLORS; i++)
                {
                    //currentColor = colorArr[i];
                    openColorPicker();
                    toolbar.setBackgroundColor(currentColor);
                }
            }
        });
    }
    public void openColorPicker()
    {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener()
        {
            @Override
            public void onCancel(AmbilWarnaDialog dialog)
            {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color)
            {
                currentColor = color;
            }
        });
        colorPicker.show();

    }

}