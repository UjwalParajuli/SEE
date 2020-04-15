package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    SmileRating smileRating;
    EditText edit_text_feedback;
    Button button_send_feedback;
    String rating = "";
    ProgressBar progress_bar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        sharedPreferences = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();

        smileRating = (SmileRating)findViewById(R.id.smile_rating);
        edit_text_feedback = (EditText)findViewById(R.id.edit_text_feedback);
        button_send_feedback = (Button)findViewById(R.id.button_send_feedback);
        progress_bar = (ProgressBar)findViewById(R.id.progress_bar_feedback);

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley){
                    case SmileRating.TERRIBLE:
                        rating = "TERRIBLE";
                        break;
                    case SmileRating.BAD:
                        rating = "BAD";
                        break;
                    case SmileRating.OKAY:
                        rating = "OKAY";
                        break;
                    case SmileRating.GOOD:
                        rating = "GOOD";
                        break;
                    case SmileRating.GREAT:
                        rating = "GREAT";
                        break;
                }
            }
        });

    }

    public void sendFeedback(View view) {
        String rating_type, message;
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/sendFeedback.php";
        rating_type = rating;
        message = edit_text_feedback.getText().toString().trim();

        if (rating_type == "" && message.isEmpty()){
            Toast.makeText(this, "Please either choose rating or write your feedback.", Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (error == false){
            progress_bar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(Feedback.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress_bar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")){
                        Toast.makeText(getApplicationContext(), "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("error")) {
                        Toast.makeText(getApplicationContext(), "Sending Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_bar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(getApplicationContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                    params.put("rating_type", rating_type);
                    params.put("message", message);
                    params.put("email", sharedPreferences.getString("email", null));
                    return params;
                }
            };
            requestQueue.add(stringRequest);



        }
    }
}
