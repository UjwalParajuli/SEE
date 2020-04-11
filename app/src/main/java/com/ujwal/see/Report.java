package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class Report extends AppCompatActivity {
    RecyclerView recyclerView, recyclerView2;
    ArrayList<InterestedModel> interestedModelArrayList;
    ArrayList<PurchasedModel> purchasedModelArrayList;
    InterestedAdapter interestedAdapter;
    PurchasedAdapter purchasedAdapter;
    TextView text_view, text_view_2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        sharedPreferences = getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();

        this.setTitle(sharedPreferences.getString("event_name", null));

        text_view = (TextView)findViewById(R.id.error_message_interested);
        text_view_2 = (TextView)findViewById(R.id.error_message_purchased);

        recyclerView = (RecyclerView)findViewById(R.id.rc_view_interested_peoples);
        recyclerView2 = (RecyclerView)findViewById(R.id.rc_view_purchased);

        interestedModelArrayList = new ArrayList<>();
        purchasedModelArrayList = new ArrayList<>();

        interestedAdapter = new InterestedAdapter(interestedModelArrayList, Report.this);
        purchasedAdapter = new PurchasedAdapter(purchasedModelArrayList, Report.this);

        getInterestedPeople();
        getPurchased();

    }


    public void getInterestedPeople(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getInterestedPeoples.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(Report.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(Report.this, "No Data", Toast.LENGTH_SHORT).show();
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

                            InterestedModel interestedModel = new InterestedModel(full_name, email, phone, address, image, interested_date);
                            interestedModelArrayList.add(interestedModel);
                            Collections.reverse(interestedModelArrayList);
                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Report.this, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);


                        if (interestedModelArrayList.size() <= 0){
                            try {
                                LinearLayout linearLayout = findViewById(R.id.linear_layout_interested);
                                recyclerView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }catch (Exception ex){

                            }

                        }

                        recyclerView.setAdapter(interestedAdapter);
                        interestedAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                InterestedModel interestedModel = interestedModelArrayList.get(position);
                                Intent intent = new Intent(Report.this, Interested.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("interested_people_details", interestedModel);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });



                    } catch (JSONException e) {
                        Toast.makeText(Report.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(sharedPreferences.getInt("event_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    public void getPurchased(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getPurchasedPeoples.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(Report.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(Report.this, "No Data", Toast.LENGTH_SHORT).show();
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

                            PurchasedModel purchasedModel = new PurchasedModel(full_name, email, phone, address, image, ticket_no, quantity, amount, purchased_date);
                            purchasedModelArrayList.add(purchasedModel);
                            Collections.reverse(purchasedModelArrayList);
                        }



                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Report.this, RecyclerView.VERTICAL, false);
                        recyclerView2.setLayoutManager(linearLayoutManager);


                        if (purchasedModelArrayList.size() <= 0){
                            try {
                                LinearLayout linearLayout = findViewById(R.id.linear_layout_purchased);
                                recyclerView2.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }catch (Exception ex){

                            }

                        }

                        recyclerView2.setAdapter(purchasedAdapter);
                        purchasedAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recyclerView2).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                PurchasedModel purchasedModel = purchasedModelArrayList.get(position);
                                Intent intent = new Intent(Report.this, Purchased.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("purchased_people_details", purchasedModel);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });



                    } catch (JSONException e) {
                        Toast.makeText(Report.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //progressBarHome.setVisibility(View.GONE);
                    //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Report.this,"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(sharedPreferences.getInt("event_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);


    }
}
