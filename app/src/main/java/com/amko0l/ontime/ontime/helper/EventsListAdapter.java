package com.amko0l.ontime.ontime.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amko0l.ontime.ontime.R;

import java.util.ArrayList;

/**
 * Created by Lakshmisagar on 4/14/2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> eventsList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView eventdetail;
        public ViewHolder(View v) {
            super(v);
            eventTitle = (TextView) v.findViewById(R.id.title);
            eventdetail = (TextView) v.findViewById(R.id.daytime);
        }
    }

    public void eventsList(int position, ArrayList<String> item) {
        eventsList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = eventsList.indexOf(item);
        eventsList.remove(position);
        notifyItemRemoved(position);
    }

    public EventsListAdapter(ArrayList<ArrayList<String>> myDataset) {
        eventsList = myDataset;
    }

    @Override
    public EventsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventsrow, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<String> event  = eventsList.get(position);
        holder.eventTitle.setText(event.get(0));
        if(event.size()>1) {
            Log.d("Days :",""+event.get(3));
            char[] days = event.get(3).toCharArray();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < days.length; i++) {
                Log.d("Day :",""+days[i]);
                if (Character.getNumericValue(days[i]) == 1) {
                    builder = builder.append('M');
                } else if (Character.getNumericValue(days[i]) == 2) {
                    builder = builder.append('T');
                } else if (Character.getNumericValue(days[i]) == 3) {
                    builder = builder.append('W');
                } else if (Character.getNumericValue(days[i]) == 4) {
                    builder = builder.append("Th");
                } else if (Character.getNumericValue(days[i]) == 5) {
                    builder = builder.append('F');
                }
            }
            builder.append(" " + event.get(1) + ":" + event.get(2));
            holder.eventdetail.setText(builder);
        }
        holder.eventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove(name);    // Do something
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

}