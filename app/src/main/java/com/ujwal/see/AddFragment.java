package com.ujwal.see;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class AddFragment extends Fragment {
    Bitmap bitmap;
    Spinner category_spinner;
    ImageView img_view_event;
    EditText edit_text_event_name, edit_text_city, edit_text_venue, edit_text_start_date, edit_text_start_time, edit_text_end_date, edit_text_end_time, edit_text_description, edit_text_cost_per_ticket, edit_text_total_ticket;
    RadioButton rb_yes, rb_no;
    RadioButton radio_button;
    RadioGroup radio_group;
    Button button_create_event;
    ProgressBar progress_bar_create_event;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    private static final int PERMISSION_REQUEST = 1;
    private static final int IMAGE_REQUEST = 2;

    Calendar mCurrentDate;
    int day, month, year;
    int currentHour, currentMinute;
    String amPm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActivity().setTitle("Add Event");
        View view = inflater.inflate(R.layout.fragment_add, container, false);

//        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Event");
        sharedPreferences = getContext().getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        progress_bar_create_event = (ProgressBar)view.findViewById(R.id.progressBarAddEvent);
        img_view_event = (ImageView)view.findViewById(R.id.img_event);
        edit_text_event_name = (EditText)view.findViewById(R.id.edit_txt_event_name);
        edit_text_city = (EditText)view.findViewById(R.id.edit_txt_city);
        edit_text_venue = (EditText)view.findViewById(R.id.edit_txt_venue);
        edit_text_start_date = (EditText)view.findViewById(R.id.edit_txt_start_date);
        edit_text_end_date = (EditText)view.findViewById(R.id.edit_txt_end_date);
        edit_text_start_time = (EditText)view.findViewById(R.id.edit_txt_start_time);
        edit_text_end_time = (EditText)view.findViewById(R.id.edit_txt_end_time);
        edit_text_description = (EditText)view.findViewById(R.id.edit_txt_description);
        edit_text_cost_per_ticket = (EditText)view.findViewById(R.id.edit_txt_cost_per_ticket);
        edit_text_total_ticket = (EditText)view.findViewById(R.id.edit_txt_total_tickets);

        edit_text_venue.addTextChangedListener(venueNameTextWatcher);

        radio_group = (RadioGroup)view.findViewById(R.id.radio_group);

        rb_yes = (RadioButton)view.findViewById(R.id.rb_yes);
        rb_no = (RadioButton)view.findViewById(R.id.rb_no);


        button_create_event = (Button)view.findViewById(R.id.btn_create_event);

        category_spinner = (Spinner)view.findViewById(R.id.spinner_category);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category_spinner.setAdapter(myAdapter);
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

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.rb_no:
                        edit_text_cost_per_ticket.setEnabled(false);
                        edit_text_cost_per_ticket.setInputType(InputType.TYPE_NULL);
                        edit_text_cost_per_ticket.setFocusableInTouchMode(false);

                        edit_text_total_ticket.setEnabled(false);
                        edit_text_total_ticket.setInputType(InputType.TYPE_NULL);
                        edit_text_total_ticket.setFocusableInTouchMode(false);
                        break;
                    case R.id.rb_yes:
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
        //edit_text_start_date.setText(day+"-"+month+"-"+year);

        edit_text_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
        //edit_text_end_date.setText(day+"-"+month+"-"+year);

        edit_text_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

        //edit_text_start_time.setText(currentHour + ":" + currentMinute);

        edit_text_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
        //edit_text_end_time.setText(currentHour + ":" + currentMinute);

        edit_text_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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


        button_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();


            }
        });

        return view;
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
                button_create_event.setEnabled(false);
            }
            else {
                edit_text_venue.setError(null);
                button_create_event.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getContext(), "Please allow this permission in App Setting", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
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
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgPath);
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
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }





    public void addEvent(){
        final String images, event_name, city_name, venue_name, start_date, end_date, start_time, end_time, category, description, ticket_required, cost_per_ticket, total_tickets;
        int radioId = radio_group.getCheckedRadioButtonId();
        String image1;
        radio_button = getActivity().findViewById(radioId);
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/addEvent.php";

        try {
            int check = bitmap.getWidth();
            image1 = getStringImage(bitmap);
        }catch (Exception ex){
            image1 = sharedPreferences.getString("image", null);
        }

        images = image1;

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
            Toast.makeText(getContext(), "Please Choose Image", Toast.LENGTH_SHORT).show();
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
            progress_bar_create_event.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")){
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "Successfully Created", Toast.LENGTH_SHORT).show();
                        Fragment homeFragment = new HomeFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, homeFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else if (response.trim().equals("error")) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "Event Already Created", Toast.LENGTH_SHORT).show();

                    }
                    else if (response.trim().equals("ticketError")){
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "Error while inserting to Ticket table", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("eventDetailsError")){
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "Error while inserting to event_details table", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("error2")){
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "Could not upload file", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar_create_event.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
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
                    params.put("user_id", String.valueOf(sharedPreferences.getInt("user_id", -1)));
                    return params;
                }
            };
            requestQueue.add(stringRequest);

        }
    }


}
