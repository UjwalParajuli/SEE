package com.ujwal.see;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText edit_text_email, edit_text_password;
    Button button_login_1;
    TextView text_view_sign_up, text_view_forgot_password;
    CheckBox check_box_remember_me;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    ProgressBar progress_bar_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)){
            finish();
        }
        setContentView(R.layout.activity_login);

        edit_text_email = (EditText) findViewById(R.id.edit_txt_email);
        edit_text_password = (EditText) findViewById(R.id.edit_txt_password);
        text_view_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        button_login_1 = (Button) findViewById(R.id.btn_login_1);
        text_view_forgot_password = (TextView) findViewById(R.id.txt_forgot_password);
        check_box_remember_me = (CheckBox) findViewById(R.id.check_box_remember);
        progress_bar_login = (ProgressBar) findViewById(R.id.progress_bar_login);
        sharedPreferences = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();

        edit_text_email.setText(sharedPreferences.getString("email2", null));
        edit_text_password.setText(sharedPreferences.getString("password", null));
        boolean check = sharedPreferences.getBoolean("state",false);
        if (check){
            check_box_remember_me.setChecked(true);
        }
    }


    public void openSignUpPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }

    public void openForgotPassword(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        final String email, password;
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/login.php";

        email = edit_text_email.getText().toString().trim();
        password = edit_text_password.getText().toString().trim();

        if (email.isEmpty()) {
            edit_text_email.setError("Please fill this field");
            error = true;
        }

        if (password.isEmpty()) {
            edit_text_password.setError("Please fill this field");
            error = true;
        }

        if (error == false){
            progress_bar_login.setVisibility(View.VISIBLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress_bar_login.setVisibility(View.GONE);
                    if (response.trim().equals("error")) {
                        Toast.makeText(getApplicationContext(), "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            int id = jsonResponse.getInt("id");
                            String name = jsonResponse.getString("full_name");
                            String email = jsonResponse.getString("email");
                            String address = jsonResponse.getString("address");
                            String phone = jsonResponse.getString("phone");
                            int user_type = jsonResponse.getInt("user_type_id");

                            editorPreferences.putString("full_name", name);
                            editorPreferences.putString("email", email);
                            editorPreferences.putString("address", address);
                            editorPreferences.putString("phone", phone);
                            editorPreferences.putInt("user_type", user_type);
                            editorPreferences.putInt("user_id", id);
                            editorPreferences.apply();

                            if (check_box_remember_me.isChecked()){
                                editorPreferences.putString("email2", email);
                                editorPreferences.putString("password", password);
                                editorPreferences.putBoolean("state",true);
                                editorPreferences.apply();

                            }
                            else {
                                editorPreferences.remove("email2");
                                editorPreferences.remove("password");
                                editorPreferences.putBoolean("state", false);
                                editorPreferences.apply();
                            }
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }



    }
}
