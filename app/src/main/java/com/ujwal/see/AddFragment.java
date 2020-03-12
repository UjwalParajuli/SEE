package com.ujwal.see;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddFragment extends Fragment {
    Toolbar toolbar;
    Spinner category_spinner;
    ImageView img_view_event;
    EditText edit_text_event_name, edit_text_city, edit_text_venue, edit_text_start_date, edit_text_start_time, edit_text_end_date, edit_text_end_time, edit_text_description, edit_text_cost_per_ticket, edit_text_total_ticket;
    RadioButton rb_yes, rb_no;
    RadioButton radio_button;
    RadioGroup radio_group;
    Button button_create_event;

    Calendar mCurrentDate;
    int day, month, year;
    int currentHour, currentMinute;
    String amPm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Event");

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

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month + 1;

        edit_text_start_date.setText(day+"/"+month+"/"+year);

        edit_text_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        edit_text_start_date.setText(dayOfMonth + "/" + month + "/" + year);

                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        currentHour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        currentMinute = mCurrentDate.get(Calendar.MINUTE);

        edit_text_start_time.setText(currentHour + ":" + currentMinute);

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


        button_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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


}
