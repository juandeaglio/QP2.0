package com.example.qp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class SwipeControllerProjects extends SwipeController {

    public SwipeControllerProjects(SwipeControllerActions swipeActions, Context context){
        super(swipeActions, context);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        ProjectRecyclerAdapter.ProjectViewHolder projectViewHolder = (ProjectRecyclerAdapter.ProjectViewHolder) viewHolder;
        if(direction == RIGHT){
            swipeActions.onRightSwiped(projectViewHolder.uuid);
        }
        else{
            swipeActions.onLeftSwiped(projectViewHolder.uuid);
        }
    }

}
