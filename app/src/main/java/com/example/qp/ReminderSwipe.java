package com.example.qp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.UUID;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class ReminderSwipe extends SwipeController {

    public ReminderSwipe(SwipeControllerActions swipeActions, Context context)
    {
        super(swipeActions,context);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        ReminderRecyclerAdapter.ReminderCardViewHolder reminderView = (ReminderRecyclerAdapter.ReminderCardViewHolder) viewHolder;
        if(direction == LEFT){
            UUID.fromString(reminderView.reminderID);
            swipeActions.onRightSwiped(UUID.fromString(reminderView.reminderID));
        }

    }
}
