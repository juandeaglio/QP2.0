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
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private int colorComplete;
    private int colorDelete;
    private Paint paint = new Paint();
    private final RectF background;
    private StageTouchHelperAdapter mItemTouchHelper;
    public SwipeController(SwipeControllerActions swipeActions, Context context, StageTouchHelperAdapter itemTouchHelper) {
        this.swipeActions = swipeActions;
        icon = ContextCompat.getDrawable(context, R.drawable.delete_icon);
        icon2 = ContextCompat.getDrawable(context, R.drawable.ic_check_white_24dp);
        background = new RectF(0,0,0,0);
        colorDelete = context.getResources().getColor(R.color.colorDelete);
        colorComplete = context.getResources().getColor(R.color.colorComplete);
        mItemTouchHelper = itemTouchHelper;
    }
    public SwipeController(SwipeControllerActions swipeActions, Context context) {
        this.swipeActions = swipeActions;
        icon = ContextCompat.getDrawable(context, R.drawable.delete_icon);
        icon2 = ContextCompat.getDrawable(context, R.drawable.ic_check_white_24dp);
        background = new RectF(0,0,0,0);
        colorDelete = context.getResources().getColor(R.color.colorDelete);
        colorComplete = context.getResources().getColor(R.color.colorComplete);
        mItemTouchHelper = null;
    }

    public void setItemTouchHelper(StageTouchHelperAdapter stageTouchHelperAdapter){
        mItemTouchHelper = stageTouchHelperAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(UP|DOWN, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mItemTouchHelper.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
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
    public boolean isLongPressDragEnabled(){
        return false;
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
            background.set(itemView.getLeft(), itemView.getTop(),
                    itemView.getRight() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
            paint.setColor(colorComplete);
            c.drawRoundRect(background,30,30, paint);
            icon2.draw(c);
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            paint.setColor(colorDelete);

            background.set(itemView.getLeft() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            c.drawRoundRect(background, 30,30, paint);
            icon.draw(c);
        } else { // view is unSwiped
            background.set(0, 0, 0, 0);
            c.drawRoundRect(background, 30,30,paint);
        }
    }
}