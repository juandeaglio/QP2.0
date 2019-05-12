package com.example.qp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;
import com.tooltip.Tooltip;

import maes.tech.intentanim.CustomIntent;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Customization extends AppCompatActivity
{
    final int MAX_NUMBER_OF_COLORS = 3;

    int currentIndex = 0;
    int currentColor;
    ColorManager colorManager;
    View colorView;
    View complementaryColorView;
    ImageView colorBox;
    ImageView colorWheel;
    Toast toast;
    Bitmap bitmap;
    Context context;
    Intent intent;
    int [] colorArr = new int[4];

    DatabaseHelper db = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        intent = getIntent();
        context = this;
        colorManager = MainActivity.colorManager;
        toolbar.setBackgroundColor(colorManager.getColorAccent());
        int defaultColorPrimary = colorManager.getColorPrimary();
        int defaultColorPrimaryDark = colorManager.getColorPrimaryDark();
        int defaultColorAccent = colorManager.getColorAccent();
        int defaultColorText = colorManager.getColorText();


        this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);


        if(defaultColorPrimaryDark == getResources().getColor(R.color.colorPrimaryDark))
        {
            Tooltip cardToolTip = new Tooltip.Builder(findViewById(R.id.card3))
                    .setText("Tap on a card to change the color for cards.")
                    .setBackgroundColor(defaultColorAccent)
                    .setGravity(Gravity.BOTTOM)
                    .setCancelable(true)
                    .show();
        }
        if(defaultColorAccent == getResources().getColor(R.color.colorAccent))
        {
            Tooltip cardToolTip = new Tooltip.Builder(findViewById(R.id.toolbar2))
                    .setText("Tap on the header to change the color for the header.")
                    .setBackgroundColor(defaultColorPrimaryDark)
                    .setGravity(Gravity.START)
                    .setCancelable(true)
                    .show();
        }
        if(defaultColorText == getResources().getColor(R.color.colorBackgroundReminder))
        {
            Tooltip cardToolTip = new Tooltip.Builder(findViewById(R.id.textColorButton))
                    .setText("Tap here to change the text color.")
                    .setTextColor(defaultColorText)
                    .setGravity(Gravity.BOTTOM)
                    .setCancelable(true)
                    .show();
        }

        Button saveTaskBtn = (Button)findViewById(R.id.saveColorButton);
        Button cancelButton = (Button)findViewById(R.id.cancelColorButton);
        Button defaultButton = (Button)findViewById(R.id.defaultButton);
        saveTaskBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(saveColors())
                {
                    goBackToHomepage();
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

        defaultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                colorManager.setColorPrimary(getResources().getColor(R.color.colorPrimary));
                colorManager.setColorPrimaryDark(getResources().getColor(R.color.colorPrimaryDark));
                colorManager.setColorAccent(getResources().getColor(R.color.colorAccent));
                colorManager.setColorText(getResources().getColor(R.color.colorBackgroundReminder));
                finish();
                startActivity(getIntent());
            }
        });
        colorArr = new int[]{defaultColorPrimary ,defaultColorPrimaryDark, defaultColorAccent, defaultColorText};

        colorArr[0] = defaultColorPrimary;
        colorArr[1] = defaultColorPrimaryDark;
        colorArr[2] = defaultColorAccent;
        colorArr[3] = defaultColorText;

        toolbar.setBackgroundColor(defaultColorAccent);
        //setStatusBarColor(findViewById(R.id.statusBarBackground), defaultColorAccent);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(defaultColorAccent);

        CardView card0 = (CardView) findViewById(R.id.card1);
        CardView card1 = (CardView) findViewById(R.id.card2);
        CardView card2 = (CardView) findViewById(R.id.card3);

        CardView[] cardArr = new CardView[]{card0, card1, card2};

        String nameArr[] = {"Test Task 1","Test Task 2", "Test Task 3"};
        String priorityArr[] = {"1", "2", "4"};
        String dueDatesArr[] = {"5/06/2040  ", "4/21/2086", "5/15/2019"};
        String timesArr[] = {"10:00 am", "4:39 pm", "2:00 pm"};


        //Creates the hardcoded cards, not really important.
        //toolbar.setTitleTextColor(defaultColorText);
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

            taskName1.setTextColor(defaultColorText);
            priority1.setTextColor(defaultColorText);
            dueDate1.setTextColor(defaultColorText);
            time1.setTextColor(defaultColorText);
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
        Button changeTextColor = findViewById(R.id.textColorButton);
        changeTextColor.setTextColor(defaultColorText);
        changeTextColor.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                currentIndex = 3;
                currentColor = colorArr[3];
                openColorPicker();
            }
        });

        ImageView colorWheelButton = findViewById(R.id.colorWheelButton);
        colorWheelButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                showColorWheelDialog();
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
        startActivity(new Intent(this, MainActivity.class));
        CustomIntent.customType(this, "right-to-left");
        this.finish();
    }
    public boolean saveColors()
    {
        if(colorManager != null)
        {
            db.updateColordata(colorManager.getColorPrimary(), colorManager.getColorPrimaryDark(), colorManager.getColorAccent(), colorManager.getColorText());
            return true;
        }
        return false;
    }
    int shadeColor(int color)
    {
        int darkVal = Integer.decode("0xAA0000");
        return color - darkVal;
    }

    public void showColorWheelDialog()
    {
        final Dialog vtDialog = new Dialog(context);
        vtDialog.setTitle("Color Wheel");
        vtDialog.setContentView(R.layout.color_wheel_dialog);
        vtDialog.findViewById(R.id.toolbar3).setBackgroundColor(colorManager.getColorAccent());
        vtDialog.show();

        colorWheel = vtDialog.findViewById(R.id.colorWheelImage);

        colorView = vtDialog.findViewById(R.id.colorView);
        complementaryColorView = vtDialog.findViewById(R.id.complementaryColorView);
        colorWheel.setDrawingCacheEnabled(true);
        colorWheel.buildDrawingCache(true);

        colorWheel.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    bitmap = colorWheel.getDrawingCache();
                    int pixel;
                    if(event.getX() < colorWheel.getMaxWidth() && event.getY() < colorWheel.getMaxHeight())
                        pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
                    else
                        pixel = 0;
                    String hex = "#" + Integer.toHexString(pixel);

                    currentColor = pixel;
                    colorArr[2] = currentColor;

                    colorArr[1] = getOppositeColor(currentColor);
                    colorView.setBackgroundColor(currentColor);
                    complementaryColorView.setBackgroundColor(colorArr[1]);

                }
                return true;
            }

        });
        Button finishButton = vtDialog.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(colorArr[0] != colorManager.getColorPrimary() || colorArr[1] != colorManager.getColorPrimaryDark() || colorArr[2] != colorManager.getColorAccent() || colorArr[3] != colorManager.getColorText())
                {
                    colorManager.setColorPrimary(colorArr[0]);
                    colorManager.setColorPrimaryDark(colorArr[1]);
                    colorManager.setColorAccent(colorArr[2]);
                    colorManager.setColorText(colorArr[3]);
                }
                vtDialog.dismiss();
                finish();
                startActivity(intent);
            }
        });
    }

    int getOppositeColor(int c)
    {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(c), Color.green(c),
                Color.blue(c), hsv);
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }

}

