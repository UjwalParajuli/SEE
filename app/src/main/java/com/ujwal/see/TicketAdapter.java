package com.ujwal.see;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    ArrayList<TicketModel> ticketModelArrayList;
    Context context;

    public TicketAdapter(ArrayList<TicketModel> ticketModelArrayList, Context context) {
        this.ticketModelArrayList = ticketModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ticket_item, parent, false);

        TicketAdapter.ViewHolder viewHolder = new TicketAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TicketModel ticketModel = ticketModelArrayList.get(position);

        TextView event_name = holder.event_name;
        TextView venue = holder.venue;
        TextView full_date = holder.full_date;
        TextView ticket_number = holder.ticket_number;
        TextView user_name = holder.user_name;
        TextView total_purchased = holder.total_purchased;
        TextView purchased_date = holder.purchased_date;
        TextView total_cost = holder.total_cost;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = null, dateEnd = null;
        long finalDateStart = 0, finalDateEnd = 0;
        try {
            dateStart = format.parse(ticketModel.getStart_date());
            finalDateStart = dateStart.getTime();
            dateEnd = format.parse(ticketModel.getEnd_date());
            finalDateEnd = dateEnd.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = (String) DateFormat.format("EEE", finalDateStart); // Thursday
        String day          = (String) DateFormat.format("dd",   finalDateStart); // 20
        String monthString  = (String) DateFormat.format("MMM",  finalDateStart); // Jun
        String year = (String) DateFormat.format("yyyy", finalDateStart);
        if (finalDateStart == finalDateEnd){
            full_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + "," + " " + year);
        }
        else {
            String dayOfTheWeek2 = (String) DateFormat.format("EEE", finalDateEnd); // Thursday
            String day2          = (String) DateFormat.format("dd",   finalDateEnd); // 20
            String monthString2  = (String) DateFormat.format("MMM",  finalDateEnd); // Jun
            String year2 = (String) DateFormat.format("yyyy", finalDateStart);
            full_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + " " + "-" + " " + monthString2 + " " + day2 + "," + " " + year2);
        }

        event_name.setText(ticketModel.getEvent_name());
        venue.setText(ticketModel.getVenue());
        ticket_number.setText(String.valueOf(ticketModel.getTicket_number()));
        user_name.setText(ticketModel.getUser_name());
        total_purchased.setText(String.valueOf(ticketModel.getTotal_purchased_tickets()));
        purchased_date.setText(ticketModel.getPurchased_date());
        total_cost.setText("Rs. " + String.valueOf(ticketModel.getTotal_cost()));



    }

    @Override
    public int getItemCount() {
        return ticketModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView event_name, venue, full_date, ticket_number, user_name, total_purchased, purchased_date, total_cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            event_name = itemView.findViewById(R.id.text_view_ticket_event_name);
            venue = itemView.findViewById(R.id.text_view_ticket_event_venue);
            full_date = itemView.findViewById(R.id.text_view_ticket_event_date);
            ticket_number = itemView.findViewById(R.id.text_view_ticket_number);
            user_name = itemView.findViewById(R.id.text_view_ticket_user_name);
            total_purchased = itemView.findViewById(R.id.text_view_ticket_quantity);
            purchased_date = itemView.findViewById(R.id.text_view_ticket_purchased_date);
            total_cost = itemView.findViewById(R.id.text_view_ticket_total_cost);
        }
    }
}
