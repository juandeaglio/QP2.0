package com.example.qp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeController extends Callback {


    protected SwipeControllerActions swipeActions = null;

    private Drawable icon;
    private Drawable icon2;
    private final ColorDrawable background;
    private final ColorDrawable background2;

    public SwipeController(SwipeControllerActions swipeActions, Context context) {
        this.swipeActions = swipeActions;
        icon = ContextCompat.getDrawable(context, R.drawable.delete_icon);
        icon2 = ContextCompat.getDrawable(context, R.drawable.ic_check_white_24dp);
        background = new ColorDrawable(Color.RED);
        background2 = new ColorDrawable(Color.BLUE);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        TaskCardRecyclerAdapter.TaskCardViewHolder taskView = (TaskCardRecyclerAdapter.TaskCardViewHolder) viewHolder;
        if(direction == RIGHT){
            swipeActions.onRightSwiped(taskView.taskID);
        }
        else{
            swipeActions.onLeftSwiped(taskView.taskID);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconLeft = itemView.getLeft() + iconMargin;
            icon2.setBounds(iconLeft, iconTop, iconRight, iconBottom);


            background2.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
            background2.draw(c);
            icon2.draw(c);
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);
            icon.draw(c);
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
            background2.setBounds(0,0,0,0);
        }
    }
}