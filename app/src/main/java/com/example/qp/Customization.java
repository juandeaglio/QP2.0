package com.example.qp;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import maes.tech.intentanim.CustomIntent;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Customization extends AppCompatActivity
{
    final int MAX_NUMBER_OF_COLORS = 3;

    int currentIndex = 0;
    int currentColor;
    int[] colorArr;
    ColorManager colorManager;
    Toast toast;
    DatabaseHelper db = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        colorManager = MainActivity.colorManager;
        toolbar.setBackgroundColor(colorManager.getColorAccent());
        int defaultColorPrimary = colorManager.getColorPrimary();
        int defaultColorPrimaryDark = colorManager.getColorPrimaryDark();
        int defaultColorAccent = colorManager.getColorAccent();
        int defaultColorText = colorManager.getColorText();


        this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);
        Button saveTaskBtn = findViewById(R.id.saveTaskButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveColors())
                {
                    goBackToHomepage();
                }
                else
                {
                    toast = Toast.makeText(getApplicationContext(), "Task failed to save", Toast.LENGTH_LONG);
                    toast.show();
                }
            }


        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cursor colorVals = db.getColorValues();
                colorManager.setColorPrimary(colorVals.getInt(0));
                colorManager.setColorPrimaryDark(colorVals.getInt(1));
                colorManager.setColorAccent(colorVals.getInt(2));
                colorManager.setColorText(colorVals.getInt(3));
                goBackToHomepage();
            }
        });

        colorArr = new int[]{defaultColorPrimary ,defaultColorPrimaryDark, defaultColorAccent, defaultColorText};
        toolbar.setBackgroundColor(defaultColorAccent);
        //setStatusBarColor(findViewById(R.id.statusBarBackground), defaultColorAccent);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(defaultColorAccent);

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
            currentCard.setCardBackgroundColor(defaultColorPrimaryDark);

            taskName1.setText(nameArr[i]);
            priority1.setText(priorityArr[i]);
            dueDate1.setText(dueDatesArr[i]);
            time1.setText(timesArr[i]);
        }
        currentIndex = 0;
        Button changeColorButton = findViewById(R.id.cardButton);
        changeColorButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                currentIndex = 1;
                currentColor = colorArr[1];
                openColorPicker();
            }
        });
        Button changeHeaderColor = findViewById(R.id.headerButton);
        changeHeaderColor.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                currentIndex = 2;
                currentColor = colorArr[2];
                openColorPicker();
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
                currentIndex = 0;
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color)
            {
                currentColor = color;
                colorArr[currentIndex] = currentColor;
                if(colorArr[0] != colorManager.getColorPrimary() || colorArr[1] != colorManager.getColorPrimaryDark() || colorArr[2] != colorManager.getColorAccent() || colorArr[3] != colorManager.getColorText())
                {
                    colorManager.setColorPrimary(colorArr[0]);
                    colorManager.setColorPrimaryDark(colorArr[1]);
                    colorManager.setColorAccent(colorArr[2]);
                    colorManager.setColorText(colorArr[3]);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        colorPicker.show();

    }

    public void goBackToHomepage()
    {
        //startActivity(new Intent(this, MainActivity.class));
        CustomIntent.customType(this, "right-to-left");

        this.finish();
    }
    public boolean saveColors()
    {
        if(colorManager != null)
        {
            db.inserColorData(colorManager.getColorPrimary(), colorManager.getColorPrimaryDark(), colorManager.getColorAccent(), colorManager.getColorText());
            return true;
        }
        return false;
    }
}

