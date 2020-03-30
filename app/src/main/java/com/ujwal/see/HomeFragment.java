package com.ujwal.see;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements LocationListener {
    RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4;
    ArrayList<EventModel> eventModelArrayList;
    ArrayList<EventModel> eventModelArrayList2;
    ArrayList<EventModel> eventModelArrayList3;
    ArrayList<EventModel> eventModelArrayList4;
    EventAdapter eventAdapter;
    EventAdapter eventAdapter2;
    EventAdapter eventAdapter3;
    EventAdapter eventAdapter4;
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    String cityName = "0";
    TextView textView, textView2, textView3, textView4;
//    ProgressBar progressBarHome;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView = view.findViewById(R.id.demo_id);
        textView2 = view.findViewById(R.id.demo_id_2);
        textView3 = view.findViewById(R.id.demo_id_3);
        textView4 = view.findViewById(R.id.demo_id_4);
        //progressBarHome = view.findViewById(R.id.progressBarHome);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            textView3.setText("GPS not enabled");
        }
        else
        {   // Either GPS provider or network provider is enabled

            // First get location from Network Provider
            if (isNetworkEnabled)
            {
                locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null)
                {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null)
                    {
                        onLocationChanged(location);
                        loc_func(location);
                    }
                }
            }// End of IF network enabled

            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled)
            {
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null)
                {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null)
                    {
                        onLocationChanged(location);
                        loc_func(location);
                    }
                }

            }// End of if GPS Enabled




        }// End of Either GPS provider or network provider is enabled

        recyclerView = view.findViewById(R.id.rc_view_1);
        recyclerView2 = view.findViewById(R.id.rc_view_2);
        recyclerView3 = view.findViewById(R.id.rc_view_3);
        recyclerView4 = view.findViewById(R.id.rc_view_4);
        eventModelArrayList = new ArrayList<>();
        eventModelArrayList2 = new ArrayList<>();
        eventModelArrayList3 = new ArrayList<>();
        eventModelArrayList4 = new ArrayList<>();
        eventAdapter = new EventAdapter(eventModelArrayList, getContext());
        eventAdapter2 = new EventAdapter(eventModelArrayList2, getContext());
        eventAdapter3 = new EventAdapter(eventModelArrayList3, getContext());
        eventAdapter4 = new EventAdapter(eventModelArrayList4, getContext());


        getEvents();
        getUpcomingEvents();
        getEventsNearbyYou();
        getOngoingEvents();

        return view;

    }

    @Override
    public void onLocationChanged(Location location){
        try {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } catch (Exception ex){
            textView3.setText("GPS Connection Problem");
            //Toast.makeText(getContext(), longitude + " " + latitude, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void loc_func(Location location){
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getLocality();
            //Toast.makeText(getContext(),  cityName, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public void getEvents(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getEvents.php";
        //progressBarHome.setVisibility(View.VISIBLE);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressBarHome.setVisibility(View.GONE);
                //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
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

                            EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name);
                            eventModelArrayList.add(eventModel);
                            Collections.reverse(eventModelArrayList);

                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);


                        if (eventModelArrayList.size() <= 0){
                            try {
                                LinearLayout linearLayout = getView().findViewById(R.id.linearLayout);
                                recyclerView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }catch (Exception ex){

                            }

                        }

                        recyclerView.setAdapter(eventAdapter);
                        eventAdapter.notifyDataSetChanged();

//                        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//                            @Override
//                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                                EventModel eventModel = eventModelArrayList.get(position);
//                                Intent intent = new Intent(getContext(), EventDetails.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("event_details", eventModel);
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
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }

    public void getUpcomingEvents(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getUpcomingEvents.php";
        //progressBarHome.setVisibility(View.VISIBLE);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressBarHome.setVisibility(View.GONE);
                //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
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

                            EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name);
                            eventModelArrayList2.add(eventModel);
                            Collections.reverse(eventModelArrayList2);

                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView2.setLayoutManager(linearLayoutManager);


                        if (eventModelArrayList2.size() <= 0){
                            try {
                                LinearLayout linearLayout = getView().findViewById(R.id.linearLayout2);
                                recyclerView2.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }catch (Exception ex){

                            }

                        }

                        recyclerView2.setAdapter(eventAdapter2);
                        eventAdapter2.notifyDataSetChanged();

//                        ItemClickSupport.addTo(recyclerView2).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//                            @Override
//                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                                EventModel eventModel = eventModelArrayList2.get(position);
//                                Intent intent = new Intent(getContext(), EventDetails.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("event_details2", eventModel);
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
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }

    public void getEventsNearbyYou(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getEventsNearbyYou.php";
        //progressBarHome.setVisibility(View.VISIBLE);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressBarHome.setVisibility(View.GONE);
                //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    LinearLayout linearLayout = getView().findViewById(R.id.linearLayout3);
                    recyclerView3.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    //Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
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

                            EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name);
                            eventModelArrayList3.add(eventModel);
                            Collections.reverse(eventModelArrayList3);

                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView3.setLayoutManager(linearLayoutManager);


                        if (eventModelArrayList3.size() <= 0){
                            try {

                            }catch (Exception ex){
                                LinearLayout linearLayout = getView().findViewById(R.id.linearLayout3);
                                recyclerView3.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }

                        }

                        recyclerView3.setAdapter(eventAdapter3);
                        eventAdapter3.notifyDataSetChanged();

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
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city_name", cityName);
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    public void getOngoingEvents() {
        String url = "https://ujwalparajuli.000webhostapp.com/android/getOngoingEvents.php";
        //progressBarHome.setVisibility(View.VISIBLE);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressBarHome.setVisibility(View.GONE);
                //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                } else {
                    try {
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

                            EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name);
                            eventModelArrayList4.add(eventModel);
                            Collections.reverse(eventModelArrayList4);

                        }


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView4.setLayoutManager(linearLayoutManager);


                        if (eventModelArrayList4.size() <= 0) {
                            try {
                                LinearLayout linearLayout = getView().findViewById(R.id.linearLayout4);
                                recyclerView4.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }catch (Exception ex){

                            }

                        }

                        recyclerView4.setAdapter(eventAdapter4);
                        eventAdapter4.notifyDataSetChanged();

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
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {

        };
        requestQueue.add(stringRequest);
    }



    }







