package com.ujwal.see;

import android.content.Context;
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
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerNotificationAdapter extends RecyclerView.Adapter<CustomerNotificationAdapter.ViewHolder> {
    ArrayList<EventModel> eventArrayList;
    Context context;

    public CustomerNotificationAdapter(ArrayList<EventModel> eventArrayList, Context context) {
        this.eventArrayList = eventArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.notification_item, parent, false);

        CustomerNotificationAdapter.ViewHolder viewHolder = new CustomerNotificationAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EventModel eventModel = eventArrayList.get(position);

        CircleImageView image_profile = holder.image_profile;
        ImageView event_image = holder.image_event;
        TextView text_notification = holder.text_notification;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date past = sdf.parse(eventModel.getCreated_on());
            Date now = new Date();
            long seconds=TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<60)
            {
                text_notification.setText(eventModel.getOrganizer_name() + " " + "has created a new event." + " " + seconds + " " + "seconds ago.");
            }
            else if(minutes<60)
            {
                text_notification.setText(eventModel.getOrganizer_name() + " " + "has created a new event." + " " + minutes + " " + "minutes ago.");
            }
            else if(hours<24)
            {
                text_notification.setText(eventModel.getOrganizer_name() + " " + "has created a new event." + " " + hours + " " + "hours ago.");
            }
            else
            {
                text_notification.setText(eventModel.getOrganizer_name() + " " + "has created a new event." + " " + days + " " + "days ago.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load(eventModel.event_image).into(event_image);
        Picasso.get().load(eventModel.user_image).into(image_profile);


    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text_notification;
        public ImageView image_event;
        public CircleImageView image_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_notification = itemView.findViewById(R.id.text_view_notification_message);
            image_profile = itemView.findViewById(R.id.profile_image_notification);
            image_event = itemView.findViewById(R.id.image_view_event_notification);
        }
    }
}
