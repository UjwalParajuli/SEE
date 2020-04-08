package com.ujwal.see;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    ArrayList<EventModel> eventArrayList;
    Context context;


    public EventAdapter(ArrayList<EventModel> eventArrayList, Context context) {
        this.eventArrayList = eventArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.event_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder viewHolder, final int i) {
        final EventModel eventModel = eventArrayList.get(i);

        ImageView bannerImage = viewHolder.image_banner;
        TextView dateText = viewHolder.event_date;
        TextView titleText = viewHolder.event_title;
        TextView venueText = viewHolder.event_venue;
        TextView totalPeople = viewHolder.no_of_people;
        Button btn_view_more = viewHolder.btn_view_more;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = null, dateEnd = null;
        long finalDateStart = 0, finalDateEnd = 0;
        try {
            dateStart = format.parse(eventModel.getStart_date());
            finalDateStart = dateStart.getTime();
            dateEnd = format.parse(eventModel.getEnd_date());
            finalDateEnd = dateEnd.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = (String) DateFormat.format("EEE", finalDateStart); // Thursday
        String day          = (String) DateFormat.format("dd",   finalDateStart); // 20
        String monthString  = (String) DateFormat.format("MMM",  finalDateStart); // Jun
        if (finalDateStart == finalDateEnd){
            dateText.setText(dayOfTheWeek + "," + " " + monthString + " " + day);
        }
        else {
            String dayOfTheWeek2 = (String) DateFormat.format("EEE", finalDateEnd); // Thursday
            String day2          = (String) DateFormat.format("dd",   finalDateEnd); // 20
            String monthString2  = (String) DateFormat.format("MMM",  finalDateEnd); // Jun
            dateText.setText(dayOfTheWeek + "," + " " + monthString + " " + day + " " + "-" + " " + dayOfTheWeek2 + "," + " " + monthString2 + " " + day2 );
        }
        titleText.setText(eventModel.getEvent_name());
        venueText.setText(eventModel.getVenue());
        totalPeople.setText(String.valueOf(eventModel.getTotal_people()) + " " + "people interested");
        Picasso.get().load(eventModel.event_image).into(bannerImage);

        btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventModel eventModel1 = eventArrayList.get(i);
                Intent intent = new Intent(context, EventDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event_details", eventModel1);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView event_date, event_title, event_venue, no_of_people;
        public ImageView image_banner;
        public Button btn_view_more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            event_date = itemView.findViewById(R.id.event_date);
            event_title = itemView.findViewById(R.id.event_title);
            event_venue = itemView.findViewById(R.id.event_venue);
            no_of_people = itemView.findViewById(R.id.people_count);
            image_banner = itemView.findViewById(R.id.img_banner);
            btn_view_more = itemView.findViewById(R.id.btn_view_more);
        }
    }

}




