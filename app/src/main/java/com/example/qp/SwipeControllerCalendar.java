package com.example.qp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class SwipeControllerCalendar extends SwipeController {

    public SwipeControllerCalendar (SwipeControllerActions swipeActions, Context context){
        super(swipeActions, context);
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
