package com.ujwal.see;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    CircleImageView profile_image;
    TextView text_user_name, text_user_type, text_total_attended, text_total_organized, text_total_tickets, text_change_password, text_about_us, text_help, text_logout, text_organized, text_purchased, text_interested;
    Button button_edit_profile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActivity().setTitle("Your Profile");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getContext().getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();

        profile_image = (CircleImageView)view.findViewById(R.id.profile_image);
        text_user_name = (TextView)view.findViewById(R.id.text_view_user_name);
        text_user_type = (TextView)view.findViewById(R.id.text_view_user_type);
        text_total_attended = (TextView)view.findViewById(R.id.text_view_count_attended);
        text_total_organized = (TextView)view.findViewById(R.id.text_view_organized);
        text_total_tickets = (TextView)view.findViewById(R.id.text_view_tickets);
        text_change_password = (TextView)view.findViewById(R.id.text_view_change_password);
        text_about_us = (TextView)view.findViewById(R.id.text_view_about_us);
        text_help = (TextView)view.findViewById(R.id.text_view_help);
        text_logout = (TextView)view.findViewById(R.id.text_view_logout);
        button_edit_profile = (Button)view.findViewById(R.id.button_edit_profile);
        text_organized = (TextView)view.findViewById(R.id.textView27);
        text_interested = (TextView)view.findViewById(R.id.textView24);
        text_purchased = (TextView)view.findViewById(R.id.textView23);

        getImage();
        getCount();
        text_user_name.setText(sharedPreferences.getString("full_name", null));
        if (sharedPreferences.getInt("user_type", 0) == 2){
            text_user_type.setText("Event Organizer");
        }
        else if(sharedPreferences.getInt("user_type", 0) == 3) {
            text_user_type.setText("Customer");
        }

        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);

            }
        });

        text_total_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InterestedEvents.class);
                startActivity(intent);
            }
        });

        text_total_organized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrganizedEvents.class);
                startActivity(intent);
            }
        });

        text_total_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PurchasedTickets.class);
                startActivity(intent);
            }
        });

        text_organized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrganizedEvents.class);
                startActivity(intent);
            }
        });

        text_interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InterestedEvents.class);
                startActivity(intent);
            }
        });

        text_purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PurchasedTickets.class);
                startActivity(intent);
            }
        });



        return view;
    }

    public void getImage(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getImage.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Image Found", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String image = jsonResponse.getString("image");
                        Picasso.get().load(image).into(profile_image);
                        editorPreferences.putString("image", image);
                        editorPreferences.commit();

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
                params.put("user_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void getCount(){
        String url = "https://ujwalparajuli.000webhostapp.com/android/getCount.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int total_interested = jsonResponse.getInt("total_interested");
                        int total_purchased = jsonResponse.getInt("total_interested");
                        int total_organized = jsonResponse.getInt("total_organized");
                        text_total_attended.setText(String.valueOf(total_interested));
                        text_total_organized.setText(String.valueOf(total_organized));
                        text_total_tickets.setText(String.valueOf(total_purchased));

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
                params.put("user_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }


}
