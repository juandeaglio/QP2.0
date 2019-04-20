package com.example.qp;

import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class SwipeControllerCalendar extends SwipeController {

    public SwipeControllerCalendar (SwipeControllerActions swipeActions){
        super(swipeActions);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){
        CalendarRecyclerAdapter.TaskCardViewHolder taskView = (CalendarRecyclerAdapter.TaskCardViewHolder) viewHolder;
        if(direction == RIGHT){
            swipeActions.onRightSwiped(taskView.taskID);
        }
        else{
            swipeActions.onLeftSwiped(taskView.taskID);
        }
    }
}
