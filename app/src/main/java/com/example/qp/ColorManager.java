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
    private int colorText;

    public ColorManager(int colorPrimary, int colorAccent, int colorPrimaryDark, int colorText)
    {
        this.colorPrimary = colorPrimary;
        this.colorPrimaryDark = colorPrimaryDark;
        this.colorAccent = colorAccent;
        this.colorText = colorText;
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

    public void setColorPrimaryDark(int colorPrimaryDark) { this.colorPrimaryDark = colorPrimaryDark; }

    public int getColorAccent()
    {
        return colorAccent;
    }

    public void setColorAccent(int colorAccent)
    {
        this.colorAccent = colorAccent;
    }

    public int getColorText()
    {
        return colorText;
    }

    public void setColorText(int colorText)
    {
        this.colorAccent = colorText;
    }
}
