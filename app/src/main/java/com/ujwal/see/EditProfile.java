package com.ujwal.see;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {
    Bitmap bitmap;
    Bitmap bitmap1;
    ImageView background_image;
    CircleImageView profile_img;
    EditText text_name, text_email, text_phone, text_address;
    Button button_update;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    ProgressBar progress_bar;
    String check_email;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");

    private static final int PERMISSION_REQUEST = 1;
    private static final int IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sharedPreferences = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        editorPreferences.apply();

        background_image = (ImageView)findViewById(R.id.cover_image_edit_profile);
        profile_img = (CircleImageView) findViewById(R.id.main_edit_profile);
        text_name = (EditText)findViewById(R.id.edit_profile_name);
        text_email = (EditText)findViewById(R.id.edit_profile_email);
        text_phone = (EditText)findViewById(R.id.edit_profile_phone);
        text_address = (EditText)findViewById(R.id.edit_profile_address);
        button_update = (Button)findViewById(R.id.button_update_profile);
        progress_bar = (ProgressBar)findViewById(R.id.progress_bar_update_profile);

        String imageUri = sharedPreferences.getString("image", null);
        Picasso.get().load(imageUri).transform(new BlurTransformation(this, 85, 1)).into(background_image);
        Picasso.get().load(imageUri).into(profile_img);
        text_name.setText(sharedPreferences.getString("full_name", null));
        text_email.setText(sharedPreferences.getString("email", null));
        text_phone.setText(sharedPreferences.getString("phone", null));
        text_address.setText(sharedPreferences.getString("address", null));

        check_email = sharedPreferences.getString("email", null);

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFileChoose();
            }
        });

        if (Build.VERSION.SDK_INT >= 23){
            if (checkPermission()){

            }
            else {
                requestPermission();
            }
        }

        text_name.addTextChangedListener(nameTextWatcher);
        text_email.addTextChangedListener(emailTextWatcher);
        text_phone.addTextChangedListener(phoneTextWatcher);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = text_name.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")){
                text_name.setError("Please enter your name properly");
                button_update.setEnabled(false);
            }
            else {
                text_name.setError(null);
                button_update.setEnabled(true);
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
            String email = text_email.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                text_email.setError("Please enter a valid email address");
                button_update.setEnabled(false);
            }
            else {
                text_email.setError(null);
                button_update.setEnabled(true);
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
            String phone = text_phone.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                text_phone.setError("Please enter valid phone number");
                button_update.setEnabled(false);
            }
            else {
                text_phone.setError(null);
                button_update.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(EditProfile.this, "Please allow this permission in App Setting", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(EditProfile.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditProfile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void displayFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri imgPath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(EditProfile.this.getContentResolver(), imgPath);
                profile_img.setImageBitmap(bitmap);

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }catch (Exception ex){
            Toast.makeText(EditProfile.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }


    public void updateProfile(View view) {
        if (check_email.equals(text_email.getText().toString().trim())){
            profileUpdate();
        }
        else {
            sendCode();
        }


    }

    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    public void sendCode(){
        final String full_name, email, phone, address, image;
        String image1 = null;
        String imageUrl = sharedPreferences.getString("image",null);
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/updateVerify.php";
        try {
            int check = bitmap.getWidth();
            image1 = getStringImage(bitmap);
        }catch (Exception ex){
            bitmap1 = getBitmapFromURL(imageUrl);
            image1 = getStringImage(bitmap1);
        }
        image = image1;
        full_name = text_name.getText().toString().trim();
        email = text_email.getText().toString().trim();
        phone = text_phone.getText().toString().trim();
        address = text_address.getText().toString().trim();

        if (full_name.isEmpty()){
            text_name.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            text_name.setError("Please enter your name properly");
            error = true;
        }
        if (email.isEmpty()){
            text_email.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_email.setError("Please enter a valid email address");
            error = true;
        }
        if (phone.isEmpty()){
            text_phone.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            text_phone.setError("Please enter valid phone number");
            error = true;
        }
        if (address.isEmpty()){
            text_address.setError("Please fill this field");
            error = true;
        }
        if (error == false) {
            progress_bar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        editorPreferences.putString("full_name", full_name);
                        editorPreferences.putString("email", email);
                        editorPreferences.putString("phone", phone);
                        editorPreferences.putString("address", address);
                        editorPreferences.putString("image", image);
                        editorPreferences.commit();
                        Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), UpdateVerification.class);
                        startActivity(intent);

                    } else if (response.trim().equals("error")) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Sorry! Email Already Exists", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("dbError")) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
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

    public void profileUpdate(){
        final String full_name, email, phone, address, image;
        String image1 = null;
        String imageUrl = sharedPreferences.getString("image",null);
        boolean error = false;
        String url = "https://ujwalparajuli.000webhostapp.com/android/updateProfile.php";
        try {
            int check = bitmap.getWidth();
            image1 = getStringImage(bitmap);
        }catch (Exception ex){
            bitmap1 = getBitmapFromURL(imageUrl);
            image1 = getStringImage(bitmap1);
        }
        image = image1;
        full_name = text_name.getText().toString().trim();
        email = text_email.getText().toString().trim();
        phone = text_phone.getText().toString().trim();
        address = text_address.getText().toString().trim();

        if (full_name.isEmpty()){
            text_name.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            text_name.setError("Please enter your name properly");
            error = true;
        }
        if (email.isEmpty()){
            text_email.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_email.setError("Please enter a valid email address");
            error = true;
        }
        if (phone.isEmpty()){
            text_phone.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            text_phone.setError("Please enter valid phone number");
            error = true;
        }
        if (address.isEmpty()){
            text_address.setError("Please fill this field");
            error = true;
        }
        if (error == false) {
            progress_bar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        editorPreferences.putString("full_name", full_name);
                        editorPreferences.putString("email", email);
                        editorPreferences.putString("phone", phone);
                        editorPreferences.putString("address", address);
                        editorPreferences.putString("image", image);
                        editorPreferences.commit();
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (response.trim().equals("error1")) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("error2")) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        progress_bar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", String.valueOf(sharedPreferences.getInt("user_id", 0)));
                    params.put("image", image);
                    params.put("full_name", full_name);
                    params.put("email", email);
                    params.put("phone", phone);
                    params.put("address", address);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }
}
