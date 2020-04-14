package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import java.util.Map;

public class PurchasedTickets extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<TicketModel> ticketModelArrayList;
    TicketAdapter ticketAdapter;
    ProgressBar progress_bar;
    TextView text_view;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_tickets);

        this.setTitle("Your Tickets");

        recyclerView = (RecyclerView)findViewById(R.id.rc_view_purchased_tickets);
        ticketModelArrayList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(ticketModelArrayList, PurchasedTickets.this);
        progress_bar = (ProgressBar)findViewById(R.id.progress_bar_purchased_tickets);
        text_view = (TextView)findViewById(R.id.text_view_purchased_tickets);

        sharedPreferences = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();

        getPurchasedTickets();


    }

    public void getPurchasedTickets(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getPurchasedTickets.php";
        progress_bar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(PurchasedTickets.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress_bar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("error")) {
                    Toast.makeText(PurchasedTickets.this, "No Data", Toast.LENGTH_SHORT).show();
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
                            String user_name  = sharedPreferences.getString("full_name", null);
                            int ticket_number = jsonResponse.getInt("ticket_no");
                            String purchased_date = jsonResponse.getString("purchased_date");
                            int quantity = jsonResponse.getInt("quantity");
                            double amount = jsonResponse.getDouble("amount");

                            TicketModel ticketModel = new TicketModel(event_id, organizer_id, total_people, total_tickets, ticket_number, quantity, user_name, purchased_date, event_name, event_city, venue, start_date, end_date, start_time, end_time, category, description, image, ticket_required, organizer_name, cost_per_ticket, amount);
                            ticketModelArrayList.add(ticketModel);
                            Collections.reverse(ticketModelArrayList);

                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PurchasedTickets.this, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);


                        if (ticketModelArrayList.size() <= 0){
                            try {
                                recyclerView.setVisibility(View.GONE);
                                text_view.setVisibility(View.VISIBLE);
                            }catch (Exception ex){

                            }

                        }

                        recyclerView.setAdapter(ticketAdapter);
                        ticketAdapter.notifyDataSetChanged();

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
                        Toast.makeText(PurchasedTickets.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (error instanceof NetworkError) {
                    //progress_bar.setVisibility(View.GONE);
                    //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(PurchasedTickets.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progress_bar.setVisibility(View.GONE);
                    //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(PurchasedTickets.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(PurchasedTickets.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(PurchasedTickets.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(PurchasedTickets.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(PurchasedTickets.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }
}
