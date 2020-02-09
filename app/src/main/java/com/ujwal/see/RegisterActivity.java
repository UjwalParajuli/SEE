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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText edit_text_name, edit_text_email_1, edit_text_password_1, edit_text_confirm_password, edit_text_phone, edit_text_address;
    Button button_sign_up_1;
    TextView text_view_login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)){
            finish();
        }
        setContentView(R.layout.activity_register);

        edit_text_name = (EditText) findViewById(R.id.edit_txt_name);
        edit_text_email_1 = (EditText) findViewById(R.id.edit_txt_email_1);
        edit_text_password_1 = (EditText) findViewById(R.id.edit_txt_password_1);
        edit_text_confirm_password = (EditText) findViewById(R.id.edit_txt_confirm_password);
        edit_text_phone = (EditText) findViewById(R.id.edit_txt_phone);
        edit_text_address = (EditText) findViewById(R.id.edit_txt_address);
        button_sign_up_1 = (Button) findViewById(R.id.btn_sign_up_1);
        text_view_login = (TextView) findViewById(R.id.txt_login);

        edit_text_name.addTextChangedListener(nameTextWatcher);
        edit_text_email_1.addTextChangedListener(emailTextWatcher);
        edit_text_password_1.addTextChangedListener(passwordTextWatcher);
        edit_text_confirm_password.addTextChangedListener(confirmPasswordTextWatcher);
        edit_text_phone.addTextChangedListener(phoneTextWatcher);

        sharedPreferences = getSharedPreferences("RegisterForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
    }

    public void openLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = edit_text_name.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")){
                edit_text_name.setError("Please enter your name properly");
                button_sign_up_1.setEnabled(false);
            }
            else {
                edit_text_name.setError(null);
                button_sign_up_1.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = edit_text_email_1.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email_1.setError("Please enter a valid email address");
                button_sign_up_1.setEnabled(false);
            }
            else {
                edit_text_email_1.setError(null);
                button_sign_up_1.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_password_1.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                edit_text_password_1.setError("Password too weak. Must be 6 character long");
                button_sign_up_1.setEnabled(false);

            }
            else {
                edit_text_password_1.setError(null);
                button_sign_up_1.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher confirmPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_password_1.getText().toString().trim();
            String pass2 = edit_text_confirm_password.getText().toString().trim();
            if (!pass.equals(pass2)){
                edit_text_confirm_password.setError("Password doesn't match");
                button_sign_up_1.setEnabled(false);
            }
            else {
                edit_text_confirm_password.setError(null);
                button_sign_up_1.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = edit_text_phone.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                edit_text_phone.setError("Please enter valid phone number");
                button_sign_up_1.setEnabled(false);
            }
            else {
                edit_text_phone.setError(null);
                button_sign_up_1.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       intent.putExtra("EXIT", true);
       startActivity(intent);
       finish();
    }

    public void registerUser(View view) {
        final String full_name, email, password, confirm_password, phone, address;
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/verify.php";
        full_name = edit_text_name.getText().toString().trim();
        email = edit_text_email_1.getText().toString().trim();
        password = edit_text_password_1.getText().toString().trim();
        confirm_password = edit_text_confirm_password.getText().toString().trim();
        phone = edit_text_phone.getText().toString().trim();
        address = edit_text_address.getText().toString().trim();

        if (full_name.isEmpty()){
            edit_text_name.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            edit_text_name.setError("Please enter your name properly");
            error = true;
        }
        if (email.isEmpty()){
            edit_text_email_1.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_text_email_1.setError("Please enter a valid email address");
            error = true;
        }
        if (password.isEmpty()){
            edit_text_password_1.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            edit_text_password_1.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (confirm_password.isEmpty()){
            edit_text_confirm_password.setError("Please fill this field");
            error = true;
        }
        if (!password.equals(confirm_password)) {
            edit_text_confirm_password.setError("Password doesn't match");
            error = true;
        }
        if (phone.isEmpty()){
            edit_text_phone.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            edit_text_phone.setError("Please enter valid phone number");
            error = true;
        }
        if (address.isEmpty()){
            edit_text_address.setError("Please fill this field");
            error = true;
        }
        if (error == false){
            final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")){
                        editorPreferences.putString("full_name", full_name);
                        editorPreferences.putString("email", email);
                        editorPreferences.putString("password", password);
                        editorPreferences.putString("phone", phone);
                        editorPreferences.putString("address", address);
                        editorPreferences.apply();
                        Toast.makeText(getApplicationContext(), "Successfully Signed Up. Please verify your account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                        startActivity(intent);

                    }
                    else if (response.trim().equals("error")) {
                        Toast.makeText(getApplicationContext(), "Email Already Exists", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("dbError")){
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
                    params.put("email", email);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }



    }
}
