package com.ujwal.see;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NotificationFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editorPreferences2;

    RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6;
    ArrayList<EventModel> eventModelArrayList;
    ArrayList<EventModel> eventModelArrayList2;
    ArrayList<InterestedModel> interestedModelArrayList;
    ArrayList<InterestedModel> interestedModelArrayList2;
    ArrayList<PurchasedModel> purchasedModelArrayList;
    ArrayList<PurchasedModel> purchasedModelArrayList2;
    CustomerNotificationAdapter customerNotificationAdapter;
    CustomerNotificationAdapter customerNotificationAdapter2;
    InterestedNotificationAdapter interestedNotificationAdapter;
    InterestedNotificationAdapter interestedNotificationAdapter2;
    PurchasedNotificationAdapter purchasedNotificationAdapter;
    PurchasedNotificationAdapter purchasedNotificationAdapter2;


    TextView textView, textView2, textView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActivity().setTitle("Notifications");
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();

        sharedPreferences2 = getContext().getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences2 = sharedPreferences2.edit();
        editorPreferences2.apply();
        //textView = view.findViewById(R.id.demo_id_today_notification);
        textView = view.findViewById(R.id.text_view_today_notification);
        textView2 = view.findViewById(R.id.text_view_earlier_notification);
        textView3 = view.findViewById(R.id.text_view_no_notification);

        recyclerView = view.findViewById(R.id.rc_view_today_created_notification);
        recyclerView2 = view.findViewById(R.id.rc_view_earlier_created_notification);
        recyclerView3 = view.findViewById(R.id.rc_view_today_interested_notification);
        recyclerView4 = view.findViewById(R.id.rc_view_earlier_interested_notification);
        recyclerView5 = view.findViewById(R.id.rc_view_today_purchased_notification);
        recyclerView6 = view.findViewById(R.id.rc_view_earlier_purchased_notification);

        eventModelArrayList = new ArrayList<>();
        eventModelArrayList2 = new ArrayList<>();
        interestedModelArrayList = new ArrayList<>();
        interestedModelArrayList2 = new ArrayList<>();
        purchasedModelArrayList = new ArrayList<>();
        purchasedModelArrayList2 = new ArrayList<>();

        customerNotificationAdapter = new CustomerNotificationAdapter(eventModelArrayList, getContext());
        customerNotificationAdapter2 = new CustomerNotificationAdapter(eventModelArrayList2, getContext());
        interestedNotificationAdapter = new InterestedNotificationAdapter(interestedModelArrayList, getContext());
        interestedNotificationAdapter2 = new InterestedNotificationAdapter(interestedModelArrayList2, getContext());
        purchasedNotificationAdapter = new PurchasedNotificationAdapter(purchasedModelArrayList, getContext());
        purchasedNotificationAdapter2 = new PurchasedNotificationAdapter(purchasedModelArrayList2, getContext());

        if (sharedPreferences.getInt("user_type", 0) == 3){
            getTodayNotification();
            getEarlierNotification();
        }

        if (sharedPreferences.getInt("user_type", 0) == 2){
            getTodayInterested();
            getTodayPurchased();
            getEarlierInterested();
            getEarlierPurchased();

        }

        return view;
    }



    public void getTodayNotification(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getTodayNotification.php";
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
                            String created_on = jsonResponse.getString("created_on");
                            String user_image = jsonResponse.getString("user_image");

                            EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name, created_on, user_image);
                            eventModelArrayList.add(eventModel);
                            Collections.reverse(eventModelArrayList);

                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);


                        if (eventModelArrayList.size() <= 0){
                            try {
                                textView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                            }catch (Exception ex){

                            }

                        }
                        else {
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        recyclerView.setAdapter(customerNotificationAdapter);
                        customerNotificationAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                EventModel eventModel = eventModelArrayList.get(position);
                                Intent intent = new Intent(getContext(), EventDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("event_details", eventModel);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });



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

    public void getEarlierNotification(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getEarlierNotification.php";
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
                            String created_on = jsonResponse.getString("created_on");
                            String user_image = jsonResponse.getString("user_image");

                            EventModel eventModel = new EventModel(event_id, organizer_id, total_people, total_tickets, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, cost_per_ticket, organizer_name, created_on, user_image);
                            eventModelArrayList2.add(eventModel);
                            Collections.reverse(eventModelArrayList2);

                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView2.setLayoutManager(linearLayoutManager);


                        if (eventModelArrayList2.size() <= 0){
                            try {
                                textView2.setVisibility(View.GONE);
                                recyclerView2.setVisibility(View.GONE);
                            }catch (Exception ex){

                            }

                        }
                        if (eventModelArrayList.size() <= 0 && eventModelArrayList2.size() <= 0){
                            textView.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);
                            textView3.setVisibility(View.VISIBLE);
                        }
                        if (eventModelArrayList2.size() > 0){
                            recyclerView2.setVisibility(View.VISIBLE);
                        }

                        recyclerView2.setAdapter(customerNotificationAdapter2);
                        customerNotificationAdapter2.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView2).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                EventModel eventModel = eventModelArrayList2.get(position);
                                Intent intent = new Intent(getContext(), EventDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("event_details", eventModel);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });



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

    public void getTodayInterested(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getTodayNotificationInterest.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String full_name = jsonResponse.getString("full_name");
                            String email = jsonResponse.getString("email");
                            String phone = jsonResponse.getString("phone");
                            String address = jsonResponse.getString("address");
                            String image = jsonResponse.getString("image");
                            String interested_date = jsonResponse.getString("interested_date");
                            String event_name = jsonResponse.getString("name");
                            String event_image = jsonResponse.getString("event_image");

                            InterestedModel interestedModel = new InterestedModel(full_name, email, phone, address, image, interested_date, event_name, event_image);
                            interestedModelArrayList.add(interestedModel);
                            Collections.reverse(interestedModelArrayList);
                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView3.setLayoutManager(linearLayoutManager);


                        if (interestedModelArrayList.size() <= 0){
                            try {
                                recyclerView3.setVisibility(View.GONE);
                            }catch (Exception ex){

                            }

                        }
                        else {
                            recyclerView3.setVisibility(View.VISIBLE);
                        }

                        recyclerView3.setAdapter(interestedNotificationAdapter);
                        interestedNotificationAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView3).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                InterestedModel interestedModel = interestedModelArrayList.get(position);
                                Intent intent = new Intent(getContext(), Interested.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("interested_people_details", interestedModel);
                                intent.putExtras(bundle);
                                editorPreferences2.putString("event_name", interestedModel.getEvent_name());
                                editorPreferences2.apply();
                                startActivity(intent);
                            }
                        });



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
                params.put("organizer_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    public void getEarlierInterested(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getEarlierNotificationInterest.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String full_name = jsonResponse.getString("full_name");
                            String email = jsonResponse.getString("email");
                            String phone = jsonResponse.getString("phone");
                            String address = jsonResponse.getString("address");
                            String image = jsonResponse.getString("image");
                            String interested_date = jsonResponse.getString("interested_date");
                            String event_name = jsonResponse.getString("name");
                            String event_image = jsonResponse.getString("event_image");

                            InterestedModel interestedModel = new InterestedModel(full_name, email, phone, address, image, interested_date, event_name, event_image);
                            interestedModelArrayList2.add(interestedModel);
                            Collections.reverse(interestedModelArrayList2);
                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView4.setLayoutManager(linearLayoutManager);


                        if (interestedModelArrayList2.size() <= 0){
                            try {
                                recyclerView4.setVisibility(View.GONE);
                            }catch (Exception ex){

                            }

                        }

                        else {
                            recyclerView4.setVisibility(View.VISIBLE);
                        }

                        recyclerView4.setAdapter(interestedNotificationAdapter2);
                        interestedNotificationAdapter2.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView4).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                InterestedModel interestedModel = interestedModelArrayList2.get(position);
                                Intent intent = new Intent(getContext(), Interested.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("interested_people_details", interestedModel);
                                intent.putExtras(bundle);
                                editorPreferences2.putString("event_name", interestedModel.getEvent_name());
                                editorPreferences2.apply();
                                startActivity(intent);
                            }
                        });



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
                params.put("organizer_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    public void getTodayPurchased(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getTodayNotificationPurchase.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String full_name = jsonResponse.getString("full_name");
                            String email = jsonResponse.getString("email");
                            String phone = jsonResponse.getString("phone");
                            String address = jsonResponse.getString("address");
                            String image = jsonResponse.getString("image");
                            String purchased_date = jsonResponse.getString("purchased_date");
                            String quantity = jsonResponse.getString("quantity");
                            String amount = jsonResponse.getString("amount");
                            String ticket_no = jsonResponse.getString("ticket_no");
                            String event_name = jsonResponse.getString("name");
                            String event_image = jsonResponse.getString("event_image");

                            PurchasedModel purchasedModel = new PurchasedModel(full_name, email, phone, address, image, ticket_no, quantity, amount, purchased_date, event_name, event_image);
                            purchasedModelArrayList.add(purchasedModel);
                            Collections.reverse(purchasedModelArrayList);
                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView5.setLayoutManager(linearLayoutManager);


                        if (purchasedModelArrayList.size() <= 0){
                            try {
                                recyclerView5.setVisibility(View.GONE);
                            }catch (Exception ex){

                            }

                        }
                        if (interestedModelArrayList.size() <= 0 && purchasedModelArrayList.size() <= 0){
                            textView.setVisibility(View.GONE);
                        }

                        if (purchasedModelArrayList.size() > 0){
                            recyclerView5.setVisibility(View.VISIBLE);
                        }

                        recyclerView5.setAdapter(purchasedNotificationAdapter);
                        purchasedNotificationAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView5).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                PurchasedModel purchasedModel = purchasedModelArrayList.get(position);
                                Intent intent = new Intent(getContext(), Purchased.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("purchased_people_details", purchasedModel);
                                intent.putExtras(bundle);
                                editorPreferences2.putString("event_name", purchasedModel.getEvent_name());
                                editorPreferences2.apply();
                                startActivity(intent);
                            }
                        });



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
                params.put("organizer_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    public void getEarlierPurchased(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getEarlierNotificationPurchase.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            String full_name = jsonResponse.getString("full_name");
                            String email = jsonResponse.getString("email");
                            String phone = jsonResponse.getString("phone");
                            String address = jsonResponse.getString("address");
                            String image = jsonResponse.getString("image");
                            String purchased_date = jsonResponse.getString("purchased_date");
                            String quantity = jsonResponse.getString("quantity");
                            String amount = jsonResponse.getString("amount");
                            String ticket_no = jsonResponse.getString("ticket_no");
                            String event_name = jsonResponse.getString("name");
                            String event_image = jsonResponse.getString("event_image");

                            PurchasedModel purchasedModel = new PurchasedModel(full_name, email, phone, address, image, ticket_no, quantity, amount, purchased_date, event_name, event_image);
                            purchasedModelArrayList2.add(purchasedModel);
                            Collections.reverse(purchasedModelArrayList2);
                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView6.setLayoutManager(linearLayoutManager);


                        if (purchasedModelArrayList2.size() <= 0){
                            try {
                                recyclerView6.setVisibility(View.GONE);
                            }catch (Exception ex){

                            }

                        }
                        if (interestedModelArrayList2.size() <= 0 && purchasedModelArrayList2.size() <= 0){
                            textView2.setVisibility(View.GONE);
                        }
                        if (purchasedModelArrayList2.size() > 0){
                            recyclerView6.setVisibility(View.VISIBLE);
                        }
                        if (interestedModelArrayList.size() <= 0 && interestedModelArrayList2.size() <= 0 && purchasedModelArrayList.size() <=0 && purchasedModelArrayList2.size() <= 0){
                            textView.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);
                            recyclerView3.setVisibility(View.GONE);
                            recyclerView4.setVisibility(View.GONE);
                            recyclerView5.setVisibility(View.GONE);
                            recyclerView6.setVisibility(View.GONE);
                            textView3.setVisibility(View.VISIBLE);
                        }



                        recyclerView6.setAdapter(purchasedNotificationAdapter2);
                        purchasedNotificationAdapter2.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView6).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                PurchasedModel purchasedModel = purchasedModelArrayList2.get(position);
                                Intent intent = new Intent(getContext(), Purchased.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("purchased_people_details", purchasedModel);
                                intent.putExtras(bundle);
                                editorPreferences2.putString("event_name", purchasedModel.getEvent_name());
                                editorPreferences2.apply();
                                startActivity(intent);
                            }
                        });



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
                params.put("organizer_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }
}
