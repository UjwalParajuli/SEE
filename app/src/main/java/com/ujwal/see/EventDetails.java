package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetails extends AppCompatActivity {
    Bundle bundle;
    EventModel eventModel;
    ImageView image_full;
    TextView text_title, text_date, text_venue, text_city, text_people, text_description, text_ticket_required, text_available_tickets, text_cost_of_ticket, text_organizer;
    Button button_book, button_cancel, button_share, button_edit, button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        image_full = (ImageView)findViewById(R.id.image_full);
        text_title = (TextView)findViewById(R.id.text_title);
        text_date = (TextView)findViewById(R.id.text_date);
        text_venue = (TextView)findViewById(R.id.text_venue);
        text_city = (TextView)findViewById(R.id.text_city);
        text_people = (TextView)findViewById(R.id.text_people);
        text_description = (TextView)findViewById(R.id.text_description);
        text_ticket_required = (TextView)findViewById(R.id.text_ticket_required);
        text_available_tickets = (TextView)findViewById(R.id.text_available_tickets);
        text_cost_of_ticket = (TextView)findViewById(R.id.text_cost_of_ticket);
        text_organizer = (TextView)findViewById(R.id.text_organizer);
        button_book = (Button)findViewById(R.id.button_book);
        button_cancel = (Button) findViewById(R.id.button_cancel_book);
        button_share = (Button) findViewById(R.id.button_share);
        button_edit = (Button)findViewById(R.id.button_edit);
        button_delete = (Button)findViewById(R.id.button_delete);

        bundle = getIntent().getExtras();
        eventModel = (EventModel) bundle.getSerializable("event_details");
        this.setTitle(eventModel.getEvent_name());
        getData();
    }

    public void getData(){
        Picasso.get().load(eventModel.getEvent_image()).into(image_full);
        text_title.setText(eventModel.getEvent_name());

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
        String dayOfTheWeek = (String) DateFormat.format("EEEE", finalDateStart); // Thursday
        String day          = (String) DateFormat.format("dd",   finalDateStart); // 20
        String monthString  = (String) DateFormat.format("MMM",  finalDateStart); // Jun
        String year         = (String) DateFormat.format("yyyy",  finalDateStart);
        if (finalDateStart == finalDateEnd){
            text_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + "," + " " + year + " " + "at" + " " + eventModel.getStart_time() + " " + "-" + " " + eventModel.getEnd_time());
        }
        else {
            String dayOfTheWeek2 = (String) DateFormat.format("EEE", finalDateEnd); // Thursday
            String day2          = (String) DateFormat.format("dd",   finalDateEnd); // 20
            String monthString2  = (String) DateFormat.format("MMM",  finalDateEnd); // Jun
            String year2         = (String) DateFormat.format("yyyy",  finalDateStart);
            text_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + " " + "-" + " " + monthString2 + " " + day2 + "," + " " + year2 + " " + "at" + " " + eventModel.getStart_time() + " " + "-" + " " + eventModel.getEnd_time() );
        }

        text_venue.setText(eventModel.getVenue());
        text_city.setText(eventModel.getCity());
        text_people.setText(String.valueOf(eventModel.getTotal_people() + " " + "people coming"));
        text_description.setText(eventModel.getEvent_description());
        text_ticket_required.setText("Ticket Required:" + " " + eventModel.getTicket_required());
        text_available_tickets.setText("Available Tickets:" + " " + String.valueOf(eventModel.getTotal_tickets()));
        text_cost_of_ticket.setText("Cost Per Ticket:" + " " + String.valueOf(eventModel.getCost_per_ticket()));
        text_organizer.setText(eventModel.getOrganizer_name());

    }
}
