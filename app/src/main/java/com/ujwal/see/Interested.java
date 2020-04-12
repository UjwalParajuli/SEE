package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Interested extends AppCompatActivity {
    Bundle bundle;
    InterestedModel interestedModel;
    ImageView cover_photo;
    CircleImageView profile_photo;
    TextView full_name, email_address, address, mobile, interested_date;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested);

        cover_photo = (ImageView)findViewById(R.id.cover_image);
        profile_photo = (CircleImageView) findViewById(R.id.main_profile);
        full_name = (TextView)findViewById(R.id.user_name_detail);
        email_address = (TextView)findViewById(R.id.email_detail);
        address = (TextView)findViewById(R.id.address_detail);
        mobile = (TextView)findViewById(R.id.phone_detail);
        interested_date = (TextView)findViewById(R.id.interested_date_detail);

        sharedPreferences = getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        this.setTitle(sharedPreferences.getString("event_name", null));

        bundle = getIntent().getExtras();
        interestedModel = (InterestedModel) bundle.getSerializable("interested_people_details");

        Picasso.get().load(interestedModel.getUser_image()).transform(new BlurTransformation(this, 85, 1)).into(cover_photo);
        Picasso.get().load(interestedModel.getUser_image()).into(profile_photo);
        full_name.setText(interestedModel.getUser_name());
        email_address.setText(interestedModel.getEmail());
        address.setText(interestedModel.getAddress());
        mobile.setText(interestedModel.getMobile());
        interested_date.setText(interestedModel.getInterested_date());


    }
}
