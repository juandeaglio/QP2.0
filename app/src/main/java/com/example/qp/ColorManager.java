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
    private int cardTextColor;
    private int headerTextcolor;

    public ColorManager(int colorPrimary, int colorAccent, int colorPrimaryDark, int cardTextColor, int headerTextcolor)
    {
        this.colorPrimary = colorPrimary;
        this.colorPrimaryDark = colorPrimaryDark;
        this.colorAccent = colorAccent;
        this.cardTextColor = cardTextColor;
        this.headerTextcolor = headerTextcolor;
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

    public int getCardTextColor()
    {
        return this.cardTextColor;
    }

    public void setCardTextColor(int cardTextColor)
    {
        this.cardTextColor = cardTextColor;
    }

    public int getHeaderTextColor()
    {
        return this.headerTextcolor;
    }

    public void setHeaderTextColor(int headerTextcolor)
    {
        this.headerTextcolor = headerTextcolor;
    }
}
