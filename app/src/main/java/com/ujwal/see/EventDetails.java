package com.ujwal.see;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetails extends AppCompatActivity {
    Bundle bundle;
    EventModel eventModel;
    ImageView image_full;
    TextView text_title, text_date, text_venue, text_city, text_people, text_description, text_ticket_required, text_available_tickets, text_cost_of_ticket, text_organizer;
    Button button_interested, button_not_interested, button_purchase_ticket, button_share, button_edit, button_delete, button_report;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editorPreferences2;
    Toolbar toolbar;
    ArrayList<String> listdata = new ArrayList<String>();
    String [] emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();
        sharedPreferences2 = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences2 = sharedPreferences2.edit();
        editorPreferences2.apply();
        image_full = (ImageView) findViewById(R.id.image_full);
        text_title = (TextView) findViewById(R.id.text_title);
        text_date = (TextView) findViewById(R.id.text_date);
        text_venue = (TextView) findViewById(R.id.text_venue);
        text_city = (TextView) findViewById(R.id.text_city);
        text_people = (TextView) findViewById(R.id.text_people);
        text_description = (TextView) findViewById(R.id.text_description);
        text_ticket_required = (TextView) findViewById(R.id.text_ticket_required);
        text_available_tickets = (TextView) findViewById(R.id.text_available_tickets);
        text_cost_of_ticket = (TextView) findViewById(R.id.text_cost_of_ticket);
        text_organizer = (TextView) findViewById(R.id.text_organizer);
        button_interested = (Button) findViewById(R.id.button_interested);
        button_not_interested = (Button) findViewById(R.id.button_not_interested);
        button_share = (Button) findViewById(R.id.button_share);
        button_edit = (Button) findViewById(R.id.button_edit);
        button_delete = (Button) findViewById(R.id.button_delete);
        button_report = (Button) findViewById(R.id.button_view_report);
        button_purchase_ticket = (Button)findViewById(R.id.button_purchase_ticket);

        bundle = getIntent().getExtras();
        eventModel = (EventModel) bundle.getSerializable("event_details");
        this.setTitle(eventModel.getEvent_name());

        checkInterested();
        if (sharedPreferences2.getInt("user_id", 0) == eventModel.getOrganizer_id()){
            button_interested.setVisibility(View.GONE);
            button_not_interested.setVisibility(View.GONE);
            button_report.setVisibility(View.VISIBLE);
            button_edit.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences2.getInt("user_type", 0) == 4){
            button_interested.setVisibility(View.GONE);
        }

        if (sharedPreferences2.getInt("user_id", 0) != eventModel.getOrganizer_id() && eventModel.getTicket_required().equals("Yes")){
            button_purchase_ticket.setVisibility(View.VISIBLE);
        }

        getData();
        getEmail();

    }

    public void getData() {
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
        String day = (String) DateFormat.format("dd", finalDateStart); // 20
        String monthString = (String) DateFormat.format("MMM", finalDateStart); // Jun
        String year = (String) DateFormat.format("yyyy", finalDateStart);
        if (finalDateStart == finalDateEnd) {
            text_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + "," + " " + year + " " + "at" + " " + eventModel.getStart_time() + " " + "-" + " " + eventModel.getEnd_time());
        } else {
            String dayOfTheWeek2 = (String) DateFormat.format("EEE", finalDateEnd); // Thursday
            String day2 = (String) DateFormat.format("dd", finalDateEnd); // 20
            String monthString2 = (String) DateFormat.format("MMM", finalDateEnd); // Jun
            String year2 = (String) DateFormat.format("yyyy", finalDateStart);
            text_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + " " + "-" + " " + monthString2 + " " + day2 + "," + " " + year2 + " " + "at" + " " + eventModel.getStart_time() + " " + "-" + " " + eventModel.getEnd_time());
        }

        text_venue.setText(eventModel.getVenue());
        text_city.setText(eventModel.getCity());
        text_people.setText(String.valueOf(eventModel.getTotal_people() + " " + "people interested"));
        text_description.setText(eventModel.getEvent_description());
        text_ticket_required.setText("Ticket Required:" + " " + eventModel.getTicket_required());
        text_available_tickets.setText("Available Tickets:" + " " + String.valueOf(eventModel.getTotal_tickets()));
        text_cost_of_ticket.setText("Cost Per Ticket:" + " " + String.valueOf(eventModel.getCost_per_ticket()));
        text_organizer.setText(eventModel.getOrganizer_name());

    }


    public void editEvent(View view) {
        Intent intent = new Intent(EventDetails.this, EditEvent.class);
        editorPreferences.putString("event_image", eventModel.getEvent_image());
        editorPreferences.putString("event_name", eventModel.getEvent_name());
        editorPreferences.putString("event_venue", eventModel.getVenue());
        editorPreferences.putString("event_city", eventModel.getCity());
        editorPreferences.putString("event_start_date", eventModel.getStart_date());
        editorPreferences.putString("event_end_date", eventModel.getEnd_date());
        editorPreferences.putString("event_start_time", eventModel.getStart_time());
        editorPreferences.putString("event_end_time", eventModel.getEnd_time());
        editorPreferences.putString("event_description", eventModel.getEvent_description());
        editorPreferences.putString("ticket_required", eventModel.getTicket_required());
        editorPreferences.putString("event_category", eventModel.getEvent_category());
        editorPreferences.putInt("total_tickets", eventModel.getTotal_tickets());
        editorPreferences.putInt("event_id", eventModel.getEvent_id());
        editorPreferences.putString("cost_per_ticket", String.valueOf(eventModel.getCost_per_ticket()));
        editorPreferences.apply();
        startActivity(intent);

    }

    public void deleteEvent(View view) {
        final AlertDialog dialog = new AlertDialog.Builder(EventDetails.this)
                .setTitle("Delete Event?")
                .setMessage("Are you sure want to delete this event?")
                .setPositiveButton(android.R.string.yes, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.no, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String url = "https://ujwalparajuli.000webhostapp.com/android/deleteEvent.php";
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        final RequestQueue requestQueue = Volley.newRequestQueue(EventDetails.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.trim().equals("success")) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Intent intent = new Intent(EventDetails.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else if (response.trim().equals("error")) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Error While Deleting", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof NetworkError) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (error instanceof ServerError) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (error instanceof AuthFailureError) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (error instanceof ParseError) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (error instanceof NoConnectionError) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (error instanceof TimeoutError) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(EventDetails.this, "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("event_id", String.valueOf(eventModel.getEvent_id()));
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);

                    }
                });
            }
        });
        dialog.show();
    }

    public void shareEvent(View view) {
        shareContent();

    }

    private void shareContent() {
        emailList = new String[listdata.size()];
        emailList = listdata.toArray(emailList);
        Bitmap bitmap = getBitmapFromView(image_full);
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //to access inside the directory
            StrictMode.setVmPolicy(builder.build());
            File file = new File(this.getExternalCacheDir(), "event_banner.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (sharedPreferences2.getInt("user_type", 0) == 2){
                intent.putExtra(Intent.EXTRA_EMAIL, emailList);
            }
            intent.putExtra(Intent.EXTRA_SUBJECT, "Invitation for Event");
            intent.putExtra(Intent.EXTRA_TEXT,eventModel.getEvent_name() + "\n" + "\n" + eventModel.getEvent_description() + "\n" + "\n" +
                    "Date: " + eventModel.getStart_date() + "\n" + "\n" +
            "To know more about the event, download Search Event Everywhere (SEE) app from Google Play Store." + "\n" + "\n" +
            "If already downloaded, login to your account and view the detailed information.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Bitmap getBitmapFromView (View view){
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public  void getEmail(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getEmails.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(EventDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(EventDetails.this, "No Emails Found", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String email = jsonResponse.getString("email");

                            listdata.add(email);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(EventDetails.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", sharedPreferences2.getString("email", null));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void interested(View view) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        String url = "https://ujwalparajuli.000webhostapp.com/android/interested.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(EventDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    Toast.makeText(EventDetails.this, "Could not added", Toast.LENGTH_SHORT).show();


                }
                else {
                    button_interested.setVisibility(View.GONE);
                    button_not_interested.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        int total_people = jsonResponse.getInt("total_people");
                        text_people.setText(String.valueOf(total_people) + " " + "people interested");

                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (error instanceof NetworkError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(sharedPreferences2.getInt("user_id", 0)));
                params.put("event_id", String.valueOf(eventModel.getEvent_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    public void checkInterested(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        String url = "https://ujwalparajuli.000webhostapp.com/android/checkInterested.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(EventDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    button_interested.setVisibility(View.GONE);
                    button_not_interested.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (error instanceof NetworkError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(sharedPreferences2.getInt("user_id", 0)));
                params.put("event_id", String.valueOf(eventModel.getEvent_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void notInterested(View view) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        String url = "https://ujwalparajuli.000webhostapp.com/android/notInterested.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(EventDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    Toast.makeText(EventDetails.this, "Could not removed", Toast.LENGTH_SHORT).show();


                }
                else {
                    button_interested.setVisibility(View.VISIBLE);
                    button_not_interested.setVisibility(View.GONE);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        int total_people = jsonResponse.getInt("total_people");
                        text_people.setText(String.valueOf(total_people) + " " + "people interested");

                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (error instanceof NetworkError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EventDetails.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(sharedPreferences2.getInt("user_id", 0)));
                params.put("event_id", String.valueOf(eventModel.getEvent_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void purchaseTicket(View view) {
        if (eventModel.getTotal_tickets() > 0){
            Intent intent = new Intent(EventDetails.this, Checkout.class);
            editorPreferences.putInt("event_id", eventModel.getEvent_id());
            editorPreferences.putInt("total_tickets", eventModel.getTotal_tickets());
            editorPreferences.putString("cost_per_ticket", String.valueOf(eventModel.getCost_per_ticket()));
            editorPreferences.putString("event_name", eventModel.getEvent_name());
            editorPreferences.apply();
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(EventDetails.this);
            builder.setMessage("Sorry! All tickets are sold out.");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }

    public void viewReport(View view) {
        Intent intent = new Intent(EventDetails.this, Report.class);
        editorPreferences.putInt("event_id", eventModel.getEvent_id());
        editorPreferences.putString("event_name", eventModel.getEvent_name());
        editorPreferences.apply();
        startActivity(intent);

    }
}
