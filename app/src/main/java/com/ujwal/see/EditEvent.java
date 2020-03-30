package com.ujwal.see;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditEvent extends AppCompatActivity {
    Bitmap bitmap;
    Spinner category_spinner;
    ImageView img_view_event, image_view_replace;
    EditText edit_text_event_name, edit_text_city, edit_text_venue, edit_text_start_date, edit_text_start_time, edit_text_end_date, edit_text_end_time, edit_text_description, edit_text_cost_per_ticket, edit_text_total_ticket;
    RadioButton rb_yes_2, rb_no_2;
    RadioButton radio_button;
    RadioGroup radio_group_2;
    Button button_edit_event;
    ProgressBar progress_bar_edit_event;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    private static final int PERMISSION_REQUEST = 1;
    private static final int IMAGE_REQUEST = 2;

    Calendar mCurrentDate;
    int day, month, year;
    int currentHour, currentMinute;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        setTitle("Edit Event");
        sharedPreferences = getSharedPreferences("Event_Details",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        image_view_replace = (ImageView)findViewById(R.id.imageView_replace);
        progress_bar_edit_event = (ProgressBar)findViewById(R.id.progressBarEditEvent);
        img_view_event = (ImageView)findViewById(R.id.img_event_2);
        edit_text_event_name = (EditText)findViewById(R.id.edit_txt_event_name_2);
        edit_text_city = (EditText)findViewById(R.id.edit_txt_city_2);
        edit_text_venue = (EditText)findViewById(R.id.edit_txt_venue_2);
        edit_text_start_date = (EditText)findViewById(R.id.edit_txt_start_date_2);
        edit_text_end_date = (EditText)findViewById(R.id.edit_txt_end_date_2);
        edit_text_start_time = (EditText)findViewById(R.id.edit_txt_start_time_2);
        edit_text_end_time = (EditText)findViewById(R.id.edit_txt_end_time_2);
        edit_text_description = (EditText)findViewById(R.id.edit_txt_description_2);
        edit_text_cost_per_ticket = (EditText)findViewById(R.id.edit_txt_cost_per_ticket_2);
        edit_text_total_ticket = (EditText)findViewById(R.id.edit_txt_total_tickets_2);

        String photoUri = sharedPreferences.getString("event_image", null);
        Picasso.get().load(photoUri).into(img_view_event);
        edit_text_event_name.setText(sharedPreferences.getString("event_name", null));
        edit_text_city.setText(sharedPreferences.getString("event_city", null));
        edit_text_venue.setText(sharedPreferences.getString("event_venue", null));
        edit_text_start_date.setText(sharedPreferences.getString("event_start_date", null));
        edit_text_end_date.setText(sharedPreferences.getString("event_end_date", null));
        edit_text_start_time.setText(sharedPreferences.getString("event_start_time", null));
        edit_text_end_time.setText(sharedPreferences.getString("event_end_time", null));
        edit_text_description.setText(sharedPreferences.getString("event_description", null));
        edit_text_cost_per_ticket.setText(sharedPreferences.getString("cost_per_ticket", null));
        edit_text_total_ticket.setText(String.valueOf(sharedPreferences.getInt("total_tickets", 0)));

        edit_text_venue.addTextChangedListener(venueNameTextWatcher);

        radio_group_2 = (RadioGroup)findViewById(R.id.radio_group_2);

        rb_yes_2 = (RadioButton)findViewById(R.id.rb_yes_2);
        rb_no_2 = (RadioButton)findViewById(R.id.rb_no_2);

        if (sharedPreferences.getString("ticket_required", null) == "Yes"){
            rb_yes_2.setChecked(true);
        }
        else {
            rb_no_2.setChecked(true);
        }

        button_edit_event = (Button)findViewById(R.id.btn_edit_event);

        category_spinner = (Spinner)findViewById(R.id.spinner_category_2);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category_spinner.setAdapter(myAdapter);
        int spinnerPosition = myAdapter.getPosition(sharedPreferences.getString("event_category", null));
        category_spinner.setSelection(spinnerPosition);
        myAdapter.notifyDataSetChanged();

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                }catch (Exception ex){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radio_group_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.rb_no_2:
                        edit_text_cost_per_ticket.setEnabled(false);
                        edit_text_cost_per_ticket.setInputType(InputType.TYPE_NULL);
                        edit_text_cost_per_ticket.setFocusableInTouchMode(false);

                        edit_text_total_ticket.setEnabled(false);
                        edit_text_total_ticket.setInputType(InputType.TYPE_NULL);
                        edit_text_total_ticket.setFocusableInTouchMode(false);
                        break;
                    case R.id.rb_yes_2:
                        edit_text_cost_per_ticket.setEnabled(true);
                        edit_text_cost_per_ticket.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit_text_cost_per_ticket.setFocusableInTouchMode(true);

                        edit_text_total_ticket.setEnabled(true);
                        edit_text_total_ticket.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit_text_total_ticket.setFocusableInTouchMode(true);
                        break;
                }
            }
        });

        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month + 1;

        //set start date on edit text and show date picker dialog

        edit_text_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        edit_text_start_date.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                },year, month - 1, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });



        //set end date on edit text and show date picker dialog

        edit_text_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        edit_text_end_date.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                },year, month - 1, day);

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date;
                long longDate = 0;
                try {
                    date = (Date)formatter.parse(edit_text_start_date.getText().toString());
                    longDate = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.getDatePicker().setMinDate(longDate);
                datePickerDialog.show();
            }
        });


        //set start time on edit text and show time picker dialog
        currentHour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        currentMinute = mCurrentDate.get(Calendar.MINUTE);


        edit_text_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12){
                            amPm = "PM";
                        }
                        else {
                            amPm = "AM";
                        }
                        edit_text_start_time.setText(String.format("%02d:%02d", hourOfDay, minute) + " " + amPm);

                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });

        //set end time on edit text and show time picker dialog

        edit_text_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12){
                            amPm = "PM";
                        }
                        else {
                            amPm = "AM";
                        }
                        edit_text_end_time.setText(String.format("%02d:%02d", hourOfDay, minute) + " " + amPm);

                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });




        img_view_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFileChoose();
            }
        });

        if (Build.VERSION.SDK_INT >= 23){
            if (checkPermission()){

            }
            else {
                requestPermission();
            }
        }
    }


    private TextWatcher venueNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String venue_name = edit_text_venue.getText().toString().trim();
            if (!venue_name.matches("[a-zA-Z\\s]+")){
                edit_text_venue.setError("Numbers Not Allowed");
                button_edit_event.setEnabled(false);
            }
            else {
                edit_text_venue.setError(null);
                button_edit_event.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditEvent.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(EditEvent.this, "Please allow this permission in App Setting", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(EditEvent.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(EditEvent.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(EditEvent.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditEvent.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void displayFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri imgPath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(EditEvent.this.getContentResolver(), imgPath);
                img_view_event.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }catch (Exception ex){
            Toast.makeText(EditEvent.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public void updateEvent(View view) {
        final String images, event_name, city_name, venue_name, start_date, end_date, start_time, end_time, category, description, ticket_required, cost_per_ticket, total_tickets;
        int radioId = radio_group_2.getCheckedRadioButtonId();
        radio_button = findViewById(radioId);
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/updateEvent.php";

        images = getStringImage(bitmap);
        event_name = edit_text_event_name.getText().toString().trim();
        city_name = edit_text_city.getText().toString().trim();
        venue_name = edit_text_venue.getText().toString().trim();
        start_date = edit_text_start_date.getText().toString().trim();
        start_time = edit_text_start_time.getText().toString().trim();
        end_date = edit_text_end_date.getText().toString().trim();
        end_time = edit_text_end_time.getText().toString().trim();
        category = category_spinner.getSelectedItem().toString().trim();
        description = edit_text_description.getText().toString().trim();
        ticket_required = radio_button.getText().toString().trim();
        cost_per_ticket = edit_text_cost_per_ticket.getText().toString().trim();
        total_tickets = edit_text_total_ticket.getText().toString().trim();

        if (images.isEmpty()){
            Toast.makeText(EditEvent.this, "Please Choose Image", Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (event_name.isEmpty()){
            edit_text_event_name.setError("Please fill this field");
            error = true;
        }

        if (city_name.isEmpty()){
            edit_text_city.setError("Please fill this field");
            error = true;
        }

        if (venue_name.isEmpty()){
            edit_text_venue.setError("Please fill this field");
            error = true;
        }

        if (!venue_name.matches("[a-zA-Z\\s]+")){
            edit_text_venue.setError("Numbers Not Allowed");
            error = true;
        }

        if (start_date.isEmpty()){
            edit_text_start_date.setError("Please fill this field");
            error = true;
        }

        if (end_date.isEmpty()){
            edit_text_end_date.setError("Please fill this field");
            error = true;
        }

        if (start_time.isEmpty()){
            edit_text_start_time.setError("Please fill this field");
            error = true;
        }

        if (end_time.isEmpty()){
            edit_text_end_time.setError("Please fill this field");
            error = true;
        }

        if (description.isEmpty()){
            edit_text_description.setError("Please fill this field");
            error = true;
        }

        if (ticket_required == "Yes"){
            if (cost_per_ticket.isEmpty()){
                edit_text_cost_per_ticket.setError("Please fill this field");
                error = true;
            }

            if (total_tickets.isEmpty()){
                edit_text_total_ticket.setError("Please fill this field");
                error = true;
            }
        }


        if (error == false){
            progress_bar_edit_event.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(EditEvent.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")){
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditEvent.this, HomeActivity.class);
                        startActivity(intent);
                        finish();


                    }
                    else if (response.trim().equals("error")) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this, "Error While Updating to ticket table", Toast.LENGTH_SHORT).show();

                    }
                    else if (response.trim().equals("error1")){
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this, "Error while updating to event details table", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("error2")){
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this, "Could not upload image", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar_edit_event.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(EditEvent.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", images);
                    params.put("event_name", event_name);
                    params.put("city_name", city_name);
                    params.put("venue_name", venue_name);
                    params.put("start_date", start_date);
                    params.put("start_time", start_time);
                    params.put("end_date", end_date);
                    params.put("end_time", end_time);
                    params.put("category", category);
                    params.put("description", description);
                    params.put("ticket_required", ticket_required);
                    params.put("cost_per_ticket", cost_per_ticket);
                    params.put("total_tickets", total_tickets);
                    params.put("event_id", String.valueOf(sharedPreferences.getInt("event_id", 0)));
                    return params;
                }
            };
            requestQueue.add(stringRequest);

        }

    }
}
