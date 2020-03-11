package com.ujwal.see;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class ForgotPassVerificationActivity extends AppCompatActivity {
    EditText edit_text_code_2;
    Button button_verify_2;
    ProgressBar progress_bar_verification_2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_verification);

        edit_text_code_2 = (EditText) findViewById(R.id.edit_txt_code_2);
        button_verify_2 = (Button) findViewById(R.id.btn_verify_2);
        progress_bar_verification_2 = (ProgressBar) findViewById(R.id.progress_bar_verification_2);

        sharedPreferences = getSharedPreferences("ForgotPassword",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void verifyUser(View view) {
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/verifyCode.php";
        final String code = edit_text_code_2.getText().toString().trim();
        String email = sharedPreferences.getString("email", null);
        if (code.isEmpty()) {
            edit_text_code_2.setError("Please enter the code");
            error = true;
        }
        if (error == false) {
            progress_bar_verification_2.setVisibility(View.VISIBLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassVerificationActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Successfully Verified.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(intent);
                        finish();
                    } else if (response.trim().equals("error")) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        edit_text_code_2.setError("Incorrect Code");
                    } else if (response.trim().equals("dbError")) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar_verification_2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("code", code);
                    params.put("email", sharedPreferences.getString("email", null));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}
