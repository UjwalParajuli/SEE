package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ms.square.android.expandabletextview.ExpandableTextView;

public class Help extends AppCompatActivity {
    ExpandableTextView expandableTextView, expandableTextView2, expandableTextView3, expandableTextView4;
    String question = "How can I add event?" + "\n" + "\n"
            + "At first, when you are logged in this app you will be considered as customer. " +
            "Then if you want to add your event and want to display in the app, you have to contact the admin " +
            "to change your account type to Event Organizer. To change your account type from customer to event " +
            "organizer, you need to pay a certain amount of money to the admin. After changing the account type to " +
            "Event Organizer, then only you will be able to add your event.";

    String question2 = "How can I perform CRUD operation on my event?" + "\n" + "\n"
            + "After you organize your event, you can see your organized event in home page or in your profile." +
            "When you click that event, you can see two buttons 'Edit' and 'Delete' at the bottom. Then you can perform CRUD " +
            "operation.";

    String question3 = "How can I know who is interested or who has purchased tickets for my event?" + "\n" + "\n"
            + "You can see 'View Report' button in your event. After you click that button, you can see who is " +
            "interested in your event or who has purchased tickets for your event.";

    String question4 = "How can I share the event?" + "\n" + "\n"
            + "You can see 'Share' button at the bottom of the event. After you click that button, you will be asked to choose " +
            "the app from which you want to share the event.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.setTitle("Help");

        expandableTextView = (ExpandableTextView)findViewById(R.id.expand_text_view);
        expandableTextView2 = (ExpandableTextView)findViewById(R.id.expand_text_view_2);
        expandableTextView3 = (ExpandableTextView)findViewById(R.id.expand_text_view_3);
        expandableTextView4 = (ExpandableTextView)findViewById(R.id.expand_text_view_4);
        expandableTextView.setText(question);
        expandableTextView2.setText(question2);
        expandableTextView3.setText(question3);
        expandableTextView4.setText(question4);
    }
}
