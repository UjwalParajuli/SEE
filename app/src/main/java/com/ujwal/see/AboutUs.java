package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    TextView text_facebook, text_instagram, text_twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        this.setTitle("About");

        text_facebook = (TextView)findViewById(R.id.text_view_facebook);
        text_instagram = (TextView)findViewById(R.id.text_view_instagram);
        text_twitter = (TextView)findViewById(R.id.text_view_twitter);

        text_facebook.setMovementMethod(LinkMovementMethod.getInstance());
        text_instagram.setMovementMethod(LinkMovementMethod.getInstance());
        text_twitter.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
