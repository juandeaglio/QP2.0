package com.example.qp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
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
    //TODO: toolbar colors need to be done, color presets, one for reach group.
    final int MAX_NUMBER_OF_COLORS = 3;
    private Paint m_paint = new Paint();
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
        int defaultCardTextColor = colorManager.getCardTextColor();
        int defaultHeaderTextColor = colorManager.getHeaderTextColor();



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

        Button saveTaskBtn = (Button)findViewById(R.id.saveColorButton);
        saveTaskBtn.setBackgroundColor(defaultColorAccent);
        Button cancelButton = (Button)findViewById(R.id.cancelColorButton);
        cancelButton.setBackgroundColor(defaultColorAccent);
        Button defaultButton = (Button)findViewById(R.id.defaultButton);
        defaultButton.setBackgroundColor(defaultColorAccent);
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

        initThemeButtons();
        saveTaskBtn.setTextColor(colorManager.getHeaderTextColor());
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cursor colorVals = db.getColorValues();
                colorManager.setColorPrimary(colorVals.getInt(0));
                colorManager.setColorPrimaryDark(colorVals.getInt(1));
                colorManager.setColorAccent(colorVals.getInt(2));
                colorManager.setCardTextColor(colorVals.getInt(3));
                colorManager.setHeaderTextColor(colorVals.getInt(4));
                goBackToHomepage();
            }
        });
        cancelButton.setTextColor(colorManager.getHeaderTextColor());

        defaultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                colorManager.setColorPrimary(getResources().getColor(R.color.colorPrimary));
                colorManager.setColorPrimaryDark(getResources().getColor(R.color.colorPrimaryDark));
                colorManager.setColorAccent(getResources().getColor(R.color.colorAccent));
                colorManager.setCardTextColor(getResources().getColor(R.color.colorBackgroundReminder));
                colorManager.setHeaderTextColor(Color.WHITE);
                finish();
                startActivity(getIntent());
            }
        });
        defaultButton.setTextColor(colorManager.getHeaderTextColor());
        colorArr = new int[]{defaultColorPrimary ,defaultColorPrimaryDark, defaultColorAccent, defaultCardTextColor, defaultHeaderTextColor};

        colorArr[0] = defaultColorPrimary;
        colorArr[1] = defaultColorPrimaryDark;
        colorArr[2] = defaultColorAccent;
        colorArr[3] = defaultCardTextColor;
        colorArr[4] = defaultHeaderTextColor;

        toolbar.setBackgroundColor(defaultColorAccent);
        toolbar.setTitleTextColor(defaultHeaderTextColor);
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
        toolbar.setTitleTextColor(defaultHeaderTextColor);
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

            taskName1.setTextColor(defaultCardTextColor);
            priority1.setTextColor(defaultCardTextColor);
            dueDate1.setTextColor(defaultCardTextColor);
            time1.setTextColor(defaultCardTextColor);
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

        Button colorWheelButton = findViewById(R.id.complementaryColorButton);
        //colorWheelButton.setBackgroundColor(defaultColorAccent);
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

                if(colorArr[0] != colorManager.getColorPrimary() || colorArr[1] != colorManager.getColorPrimaryDark()
                        || colorArr[2] != colorManager.getColorAccent() || colorArr[3] != colorManager.getCardTextColor() || colorArr[4] != colorManager.getHeaderTextColor() )
                {
                    colorManager.setColorPrimary(shadeColor(colorArr[2]));
                    colorManager.setColorPrimaryDark(colorArr[1]);
                    colorManager.setColorAccent(colorArr[2]);
                    colorManager.setCardTextColor(getContrastColor(colorArr[1]));
                    colorManager.setHeaderTextColor(getContrastColor(colorArr[2]));
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
            db.updateColordata(colorManager.getColorPrimary(), colorManager.getColorPrimaryDark(),
                    colorManager.getColorAccent(), colorManager.getCardTextColor(), colorManager.getHeaderTextColor());
            return true;
        }
        return false;
    }
    int shadeColor(int color)
    {
        int darkVal = Integer.decode("0xAA0000");
        return color - darkVal;
    }
    public static int getContrastColor(int color)
    {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
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
                if(colorArr[0] != colorManager.getColorPrimary() || colorArr[1] != colorManager.getColorPrimaryDark()
                        || colorArr[2] != colorManager.getColorAccent() || colorArr[3] != colorManager.getCardTextColor() || colorArr[4] != colorManager.getHeaderTextColor())
                {
                    colorManager.setColorPrimary(shadeColor(colorArr[2]));
                    colorManager.setColorPrimaryDark(colorArr[1]);
                    colorManager.setColorAccent(colorArr[2]);
                    colorManager.setCardTextColor(getContrastColor(colorArr[1]));
                    colorManager.setHeaderTextColor(getContrastColor(colorArr[2]));
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

    void initThemeButtons()
    {
        Button themeButton1 = (Button)findViewById(R.id.theme_1);
        themeButton1.setBackgroundColor(colorManager.getColorAccent());
        themeButton1.setTextColor(colorManager.getHeaderTextColor());
        themeButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int primaryDark = Color.parseColor("#bca7d6");
                int accent = Color.parseColor("#5311a3");
                int cardTextColor = getContrastColor(primaryDark);
                int headerTextColor = getContrastColor(accent);

                colorManager.setColorPrimaryDark(primaryDark);
                colorManager.setColorPrimary(shadeColor(primaryDark));
                colorManager.setColorAccent(accent);
                colorManager.setCardTextColor(cardTextColor);
                colorManager.setHeaderTextColor(headerTextColor);
                finish();
                startActivity(getIntent());
            }
        });

        Button themeButton2 = (Button)findViewById(R.id.theme_2);
        themeButton2.setBackgroundColor(colorManager.getColorAccent());
        themeButton2.setTextColor(colorManager.getHeaderTextColor());
        themeButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int primaryDark = Color.parseColor("#000000");
                int accent = Color.parseColor("#cc2a2a");
                int cardTextColor = getContrastColor(primaryDark);
                int headerTextColor = getContrastColor(accent);

                colorManager.setColorPrimaryDark(primaryDark);
                colorManager.setColorPrimary(shadeColor(primaryDark));
                colorManager.setColorAccent(accent);
                colorManager.setCardTextColor(cardTextColor);
                colorManager.setHeaderTextColor(headerTextColor);
                finish();
                startActivity(getIntent());
            }
        });

        Button themeButton3 = (Button)findViewById(R.id.theme_3);
        themeButton3.setBackgroundColor(colorManager.getColorAccent());
        themeButton3.setTextColor(colorManager.getHeaderTextColor());
        themeButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int primaryDark = Color.parseColor("#e00f90");
                int accent = Color.parseColor("#3f3f3f");
                int cardTextColor = getContrastColor(primaryDark);
                int headerTextColor = getContrastColor(accent);

                colorManager.setColorPrimaryDark(primaryDark);
                colorManager.setColorPrimary(shadeColor(primaryDark));
                colorManager.setColorAccent(accent);
                colorManager.setCardTextColor(cardTextColor);
                colorManager.setHeaderTextColor(headerTextColor);
                finish();
                startActivity(getIntent());
            }
        });
        Button themeButton4 = (Button)findViewById(R.id.theme_4);
        themeButton4.setBackgroundColor(colorManager.getColorAccent());
        themeButton4.setTextColor(colorManager.getHeaderTextColor());
        themeButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int primaryDark = Color.parseColor("#000000");
                int accent = Color.parseColor("#000000");
                int cardTextColor = getContrastColor(primaryDark);
                int headerTextColor = getContrastColor(accent);

                colorManager.setColorPrimaryDark(primaryDark);
                colorManager.setColorPrimary(shadeColor(primaryDark));
                colorManager.setColorAccent(accent);
                colorManager.setCardTextColor(cardTextColor);
                colorManager.setHeaderTextColor(headerTextColor);
                finish();
                startActivity(getIntent());
            }
        });

    }

}

