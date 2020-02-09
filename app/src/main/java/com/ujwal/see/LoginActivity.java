package com.ujwal.see;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText edit_text_email, edit_text_password;
    Button button_login_1;
    TextView text_view_sign_up;

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
}
