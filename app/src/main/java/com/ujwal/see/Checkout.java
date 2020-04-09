package com.ujwal.see;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;

import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {
    EditText edit_text_quantity, edit_text_total_cost;
    Button button_open_khalti;
    long total_cost = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        sharedPreferences = getSharedPreferences("Event_Details", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();

        button_open_khalti = (Button)findViewById(R.id.button_open_khalti);
        edit_text_quantity = (EditText)findViewById(R.id.edit_text_quantity);
        edit_text_total_cost = (EditText)findViewById(R.id.edit_text_total_cost);
        edit_text_total_cost.setEnabled(false);
        edit_text_total_cost.setInputType(InputType.TYPE_NULL);
        edit_text_total_cost.setFocusableInTouchMode(false);

        edit_text_quantity.addTextChangedListener(quantityTextWatcher);



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
                    edit_text_quantity.setError("Only" + String.valueOf(sharedPreferences.getInt("total_tickets", 0)) + " " + "tickets available");
                }

                if (Integer.parseInt(quantity) <= sharedPreferences.getInt("total_tickets", 0)){
                    edit_text_quantity.setError(null);
                    int quantity2 = Integer.parseInt(edit_text_quantity.getText().toString().trim());
                    double cost_per_ticket = Double.parseDouble(sharedPreferences.getString("cost_per_ticket", null));
                    double total_amount = quantity2 * cost_per_ticket;
                    total_cost = (long) total_amount;
                    edit_text_total_cost.setText(String.valueOf(total_cost));
                }
            }catch (NumberFormatException ex){
                edit_text_quantity.setError(null);
                edit_text_total_cost.setText("");
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void openKhalti(View view) {
        long amount = Long.parseLong(edit_text_total_cost.getText().toString().trim());
        openKhaltiApp(amount);

    }

    private void openKhaltiApp(long amount) {
        amount *= 100;
         Config.Builder builder = new Config.Builder("test_public_key_6239eac0ae384e8a874a3514b2f294a8", "Product ID", "Product Name", amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("Payment confirmed", data + "");
            }

            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }


        });

        Config config = builder.build();

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        khaltiCheckOut.show();
    }
}
