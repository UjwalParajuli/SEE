package com.ujwal.see;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    EditText edit_text_password_2;
    Button button_change_password;
    ProgressBar progress_bar_change_password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edit_text_password_2 = (EditText) findViewById(R.id.edit_txt_password_2);
        button_change_password = (Button) findViewById(R.id.btn_change_password);
        progress_bar_change_password = (ProgressBar) findViewById(R.id.progress_bar_change_password);

        edit_text_password_2.addTextChangedListener(passwordTextWatcher);

        sharedPreferences = getSharedPreferences("ForgotPassword",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
    }

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_password_2.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()) {
                edit_text_password_2.setError("Password too weak. Must be 6 character long");
                button_change_password.setEnabled(false);

            } else {
                edit_text_password_2.setError(null);
                button_change_password.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void changePassword(View view) {
        final String email, new_password;
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/changePassword.php";
        new_password = edit_text_password_2.getText().toString().trim();

        if (new_password.isEmpty()) {
            edit_text_password_2.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(new_password).matches()) {
            edit_text_password_2.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (error == false){
            progress_bar_change_password.setVisibility(View.VISIBLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")){
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Password Successfully Updated. Please log in to your account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (response.trim().equals("error")) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Sorry! Email not found", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("dbError")){
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"The server could not be found. Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cannot connect to Internet...Please check your connection!",Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar_change_password.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Connection TimeOut! Please check your internet connection.",Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", sharedPreferences.getString("email", null));
                    params.put("password", new_password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}
