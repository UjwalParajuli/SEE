package com.ujwal.see;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    EditText edit_text_city_name, edit_text_start_date, edit_text_end_date;
    Spinner search_spinner;
    Button button_search;
    RecyclerView recyclerView;
    ProgressBar progress_bar_search;
    TextView text_view_search;
    Calendar mCurrentDate;
    int day, month, year;
    ArrayList<EventModel> eventModelArrayList;
    EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActivity().setTitle("Search Events");
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        edit_text_city_name = (EditText)view.findViewById(R.id.edit_text_city_name);
        edit_text_start_date = (EditText)view.findViewById(R.id.search_start_date);
        edit_text_end_date = (EditText)view.findViewById(R.id.search_end_date);
        search_spinner = (Spinner)view.findViewById(R.id.spinner_search);
        button_search = (Button)view.findViewById(R.id.button_search);
        recyclerView = (RecyclerView)view.findViewById(R.id.rc_view_search);
        progress_bar_search = (ProgressBar)view.findViewById(R.id.progress_bar_search);
        text_view_search = (TextView)view.findViewById(R.id.text_view_search);

        eventModelArrayList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventModelArrayList, getContext());

        edit_text_city_name.addTextChangedListener(cityNameTextWatcher);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.search_categories));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        search_spinner.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month + 1;

        //set start date on edit text and show date picker dialog
        edit_text_start_date.setText(day+"-"+month+"-"+year);

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
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });



        //set end date on edit text and show date picker dialog
        edit_text_end_date.setText(day+"-"+month+"-"+year);

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

//                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                Date date;
//                long longDate = 0;
//                try {
//                    date = (Date)formatter.parse(edit_text_start_date.getText().toString());
//                    longDate = date.getTime();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                //datePickerDialog.getDatePicker().setMinDate(longDate);
                datePickerDialog.show();
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEvents();
            }
        });

        return view;
    }


    private TextWatcher cityNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String venue_name = edit_text_city_name.getText().toString().trim();
            if (!venue_name.matches("[a-zA-Z\\s]+")){
                edit_text_city_name.setError("Numbers Not Allowed");
                button_search.setEnabled(false);
            }
            else {
                edit_text_city_name.setError(null);
                button_search.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void searchEvents(){
        final String city, category, start_date, end_date;
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/searchEvents.php";

        city = edit_text_city_name.getText().toString().trim();
        category = search_spinner.getSelectedItem().toString().trim();
        start_date = edit_text_start_date.getText().toString().trim();
        end_date = edit_text_end_date.getText().toString().trim();

        if (city.isEmpty()){
            edit_text_city_name.setError("Please fill this field");
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

        if (error == false){
            progress_bar_search.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("error")){
                        progress_bar_search.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        progress_bar_search.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        try {
                            eventModelArrayList.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonResponse = jsonArray.getJSONObject(i);
                                int event_id = jsonResponse.getInt("id");
                                int organizer_id = jsonResponse.getInt("organizer_id");
                                int total_people = jsonResponse.getInt("total_people");
                                String event_name = jsonResponse.getString("name");
                                String event_city = jsonResponse.getString("city");
                                String venue = jsonResponse.getString("venue");
                                String start_date = jsonResponse.getString("start_date");
                                String start_time = jsonResponse.getString("start_time");
                                String end_date = jsonResponse.getString("end_date");
                                String end_time = jsonResponse.getString("end_time");
                                String category = jsonResponse.getString("category");
                                String description = jsonResponse.getString("description");
                                String image = jsonResponse.getString("image");
                                String ticket_required = jsonResponse.getString("ticket_required");
                                double cost_per_ticket = jsonResponse.getDouble("cost_per_ticket");
                                int total_tickets = jsonResponse.getInt("total_tickets");
                                String organizer_name = jsonResponse.getString("full_name");
                                String created_on = jsonResponse.getString("created_on");
                                String user_image = jsonResponse.getString("user_image");

                                EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name, created_on, user_image);
                                eventModelArrayList.add(eventModel);


                            }


                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);


                            if (eventModelArrayList.size() <= 0) {
                                try {
                                    recyclerView.setVisibility(View.GONE);
                                    text_view_search.setVisibility(View.VISIBLE);
                                }catch (Exception ex){

                                }

                            }
                            else {
                                text_view_search.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            recyclerView.setAdapter(eventAdapter);
                            eventAdapter.notifyDataSetChanged();

//                        ItemClickSupport.addTo(recyclerView3).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//                            @Override
//                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                                EventModel eventModel = eventModelArrayList3.get(position);
//                                Intent intent = new Intent(getContext(), EventDetails.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("event_details3", eventModel);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            }
//                        });


                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        try {
                            progress_bar_search.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){

                        }

                    } else if (error instanceof ServerError) {
                        try {
                            progress_bar_search.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){

                        }

                    } else if (error instanceof AuthFailureError) {
                        try {
                            progress_bar_search.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){

                        }

                    } else if (error instanceof ParseError) {
                        try {
                            progress_bar_search.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){

                        }

                    } else if (error instanceof NoConnectionError) {
                        try {
                            progress_bar_search.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){

                        }

                    } else if (error instanceof TimeoutError) {
                        try {
                            progress_bar_search.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){

                        }

                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("city_name", city);
                    params.put("start_date", start_date);
                    params.put("end_date", end_date);
                    params.put("category", category);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
        }


    }
