package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Purchased extends AppCompatActivity {
    Bundle bundle;
    PurchasedModel purchasedModel;
    ImageView cover_photo;
    CircleImageView profile_photo;
    TextView full_name, email_address, address, mobile, purchased_date, ticket_number, total_ticket, total_cost;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased);

        cover_photo = (ImageView)findViewById(R.id.cover_image_2);
        profile_photo = (CircleImageView) findViewById(R.id.main_profile_2);
        full_name = (TextView)findViewById(R.id.user_name_detail_2);
        email_address = (TextView)findViewById(R.id.email_detail_2);
        address = (TextView)findViewById(R.id.address_detail_2);
        mobile = (TextView)findViewById(R.id.phone_detail_2);
        purchased_date = (TextView)findViewById(R.id.purchased_date_detail);
        ticket_number = (TextView)findViewById(R.id.ticket_number_detail);
        total_ticket = (TextView)findViewById(R.id.quantity_detail);
        total_cost = (TextView)findViewById(R.id.cost_detail);

        sharedPreferences = getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();
        this.setTitle(sharedPreferences.getString("event_name", null));

        bundle = getIntent().getExtras();
        purchasedModel = (PurchasedModel) bundle.getSerializable("purchased_people_details");

        Picasso.get().load(purchasedModel.getUser_image()).transform(new BlurTransformation(this, 85, 1)).into(cover_photo);
        Picasso.get().load(purchasedModel.getUser_image()).into(profile_photo);
        full_name.setText(purchasedModel.getUser_name());
        email_address.setText(purchasedModel.getEmail());
        address.setText(purchasedModel.getAddress());
        mobile.setText(purchasedModel.getMobile());
        purchased_date.setText(purchasedModel.getPurchased_date());
        ticket_number.setText(purchasedModel.getTicket_no());
        total_ticket.setText(purchasedModel.getQuantity());
        total_cost.setText(purchasedModel.getCost());
    }
}
