package com.example.qp;
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class ColorManager
{
    private int colorPrimary;
    private int colorPrimaryDark;
    private int colorAccent;


    public ColorManager(Activity activity)
    {
        colorPrimary = ContextCompat.getColor(activity, R.color.colorPrimary);
        colorAccent = ContextCompat.getColor(activity, R.color.colorAccent);
        colorPrimaryDark = ContextCompat.getColor(activity, R.color.colorPrimaryDark);
    }

    public int getColorPrimary()
    {
        return colorPrimary;
    }

    public void setColorPrimary(int colorPrimary)
    {
        this.colorPrimary = colorPrimary;
    }

    public int getColorPrimaryDark()
    {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(int colorPrimaryDark)
    {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public int getColorAccent()
    {
        return colorAccent;
    }

    public void setColorAccent(int colorAccent)
    {
        this.colorAccent = colorAccent;
    }
}
