package com.ujwal.see;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.widget.KhaltiButton;

import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {
    EditText edit_text_quantity, edit_text_total_cost;
    KhaltiButton button_open_khalti;
    ProgressBar progress_bar_checkout;
    long total_cost = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editorPreferences2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        this.setTitle("Purchase Ticket");
        sharedPreferences = getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        sharedPreferences2 = getSharedPreferences("LoginForm",MODE_PRIVATE);
        editorPreferences2 = sharedPreferences2.edit();

        progress_bar_checkout = (ProgressBar) findViewById(R.id.progress_bar_checkout);
        button_open_khalti = (KhaltiButton) findViewById(R.id.button_open_khalti);
        edit_text_quantity = (EditText)findViewById(R.id.edit_text_quantity);
        edit_text_total_cost = (EditText)findViewById(R.id.edit_text_total_cost);
        edit_text_total_cost.setEnabled(false);
        edit_text_total_cost.setInputType(InputType.TYPE_NULL);
        edit_text_total_cost.setFocusableInTouchMode(false);

        edit_text_quantity.addTextChangedListener(quantityTextWatcher);

        button_open_khalti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });



    }


    private TextWatcher quantityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String quantity  = edit_text_quantity.getText().toString().trim();
            try{
                if (Integer.parseInt(quantity) > sharedPreferences.getInt("total_tickets", 0)){
                    edit_text_quantity.setError("Only" + " " + String.valueOf(sharedPreferences.getInt("total_tickets", 0)) + " " + "tickets available");
                    button_open_khalti.setEnabled(false);
                }

                if (Integer.parseInt(quantity) <= sharedPreferences.getInt("total_tickets", 0)){
                    edit_text_quantity.setError(null);
                    button_open_khalti.setEnabled(true);
                    int quantity2 = Integer.parseInt(edit_text_quantity.getText().toString().trim());
                    double cost_per_ticket = Double.parseDouble(sharedPreferences.getString("cost_per_ticket", null));
                    double total_amount = quantity2 * cost_per_ticket;
                    total_cost = (long) total_amount;
                    edit_text_total_cost.setText(String.valueOf(total_cost));
                }
            }catch (NumberFormatException ex){
                edit_text_quantity.setError(null);
                edit_text_total_cost.setText("");
                button_open_khalti.setEnabled(false);
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void openKhalti() {
        long amount = Long.parseLong(edit_text_total_cost.getText().toString().trim());
        openKhaltiApp(amount);

    }

    private void openKhaltiApp(long amount) {
         amount *= 100;
         Config.Builder builder = new Config.Builder("test_public_key_6239eac0ae384e8a874a3514b2f294a8", "Product Id", "Product Name", amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                Toast.makeText(Checkout.this, "Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.e("hello", errorMap.toString());
                Toast.makeText(Checkout.this, errorMap.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        Config config = builder.build();

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(Checkout.this, config);
        khaltiCheckOut.show();
    }



    public void proceed(){
        String quantity;
        boolean error = false;

        quantity = edit_text_quantity.getText().toString().trim();

        try {
            if (quantity.isEmpty()){
                edit_text_quantity.setError("Please fill this field");
                error = true;
            }

            if (Integer.parseInt(quantity) > sharedPreferences.getInt("total_tickets", 0)){
                edit_text_quantity.setError("Tickets not available");
                error = true;
            }
            if (Integer.parseInt(quantity) == 0){
                edit_text_quantity.setError("Cannot purchase 0 ticket");
                error = true;
            }
        }catch (Exception ex){
            edit_text_quantity.setError("Please fill the field");
            error = true;
        }


        if (error == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(Checkout.this);
            builder.setTitle("Note");
            builder.setMessage("Once the payment is done, it will not be refunded. Are you sure you want to proceed?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addPurchase();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }


    }

    public void addPurchase(){
        progress_bar_checkout.setVisibility(View.VISIBLE);
        String url = "https://ujwalparajuli.000webhostapp.com/android/purchase.php";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress_bar_checkout.setVisibility(View.GONE);
                if (response.trim().equals("success")) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    openKhalti();
//                    Toast.makeText(Checkout.this, "Payment Successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Checkout.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();

                } else if (response.trim().equals("error")) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Error While Inserting", Toast.LENGTH_SHORT).show();

                }
                else if (response.trim().equals("error2")) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Error While Updating", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar_checkout.setVisibility(View.GONE);
                if (error instanceof NetworkError) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Checkout.this, "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(sharedPreferences2.getInt("user_id", 0)));
                params.put("email", sharedPreferences2.getString("email", null));
                params.put("full_name", sharedPreferences2.getString("full_name", null));
                params.put("event_id",String.valueOf(sharedPreferences.getInt("event_id", 0)));
                params.put("quantity", edit_text_quantity.getText().toString());
                params.put("total_cost", edit_text_total_cost.getText().toString());
                params.put("total_quantity", String.valueOf(sharedPreferences.getInt("total_tickets", 0)));
                params.put("event_name", sharedPreferences.getString("event_name", null));
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

}

