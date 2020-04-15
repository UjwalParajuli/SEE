package com.ujwal.see;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UpdatePassword extends AppCompatActivity {
    EditText edit_text_old_password, edit_text_new_password, edit_text_confirm;
    Button button_update_password;
    ProgressBar progress_bar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    String oldPassword;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        sharedPreferences = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();

        edit_text_old_password = (EditText)findViewById(R.id.edit_text_old_password);
        edit_text_new_password = (EditText)findViewById(R.id.edit_text_new_password);
        edit_text_confirm = (EditText)findViewById(R.id.edit_text_confirm_new_password);
        button_update_password = (Button)findViewById(R.id.button_update_password);
        progress_bar = (ProgressBar)findViewById(R.id.progress_bar_update_password);

        edit_text_old_password.addTextChangedListener(oldPasswordTextWatcher);
        edit_text_new_password.addTextChangedListener(newPasswordTextWatcher);
        edit_text_confirm.addTextChangedListener(confirmPasswordTextWatcher);

        oldPassword = sharedPreferences.getString("password2", null);



    }

    private TextWatcher oldPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_old_password.getText().toString().trim();
            if (!pass.equals(oldPassword)){
                edit_text_old_password.setError("Old password doesn't match");
                button_update_password.setEnabled(false);

            }
            else {
                edit_text_old_password.setError(null);
                button_update_password.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher newPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_new_password.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                edit_text_new_password.setError("Password too weak. Must be 6 character long");
                button_update_password.setEnabled(false);

            }
            else {
                edit_text_new_password.setError(null);
                button_update_password.setEnabled(true);
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
            String pass = edit_text_new_password.getText().toString().trim();
            String pass2 = edit_text_confirm.getText().toString().trim();
            if (!pass.equals(pass2)){
                edit_text_confirm.setError("Password doesn't match");
                button_update_password.setEnabled(false);

            }
            else {
                edit_text_confirm.setError(null);
                button_update_password.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void updatePassword(View view) {
        final String old_password, confirm_password, new_password;
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/updatePassword.php";
        old_password = edit_text_old_password.getText().toString().trim();
        new_password = edit_text_new_password.getText().toString().trim();
        confirm_password = edit_text_confirm.getText().toString().trim();


        if (old_password.isEmpty()){
            edit_text_old_password.setError("Please fill this field");
            error = true;
        }
        if (new_password.isEmpty()){
            edit_text_new_password.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(new_password).matches()) {
            edit_text_new_password.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (confirm_password.isEmpty()){
            edit_text_confirm.setError("Please fill this field");
            error = true;
        }
        if (!new_password.equals(confirm_password)) {
            edit_text_confirm.setError("Password doesn't match");
            error = true;
        }

        if (error == false){
            progress_bar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(UpdatePassword.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress_bar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")){
                        editorPreferences.putString("password2", confirm_password);
                        editorPreferences.commit();
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                    else if (response.trim().equals("error")) {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
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
                    params.put("password", confirm_password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}
