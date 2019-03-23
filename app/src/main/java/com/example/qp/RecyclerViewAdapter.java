package com.example.qp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<String> mTaskNames = new ArrayList<>();
    private Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        RelativeLayout parentLayout;
        public ViewHolder (View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.recycle_text);
            parentLayout = itemView.findViewById(R.id.recycler_layout);
        }

    }

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames)
    {
        mContext = context;
        mTaskNames = imageNames;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder");

        viewHolder.text.setText(mTaskNames.get(i));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked on task");
                mContext.startActivity(new Intent(mContext, ViewTask.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskNames.size();
    }

    public void updateData(ArrayList<String> taskNames)
    {
        mTaskNames.clear();
        mTaskNames.addAll(taskNames);
        notifyDataSetChanged();
    }

}
