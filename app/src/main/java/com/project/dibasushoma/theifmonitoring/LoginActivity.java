package com.project.dibasushoma.theifmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView tvRegister;
    private EditText etUserName;
    private EditText etUserPassword;
    private Button btnLogin;

    private DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialView();
        setOnClick();
    }



    private void initialView() {
        tvRegister = (TextView) findViewById(R.id.tv_register);
        etUserName = (EditText) findViewById(R.id.userId);
        etUserPassword = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        databaseHelper = new DataBaseHelper(this);

    }

    private void setOnClick() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUserAndEntry(etUserName.getText().toString().trim(), etUserPassword.getText().toString().trim());
            }
        });
    }


    private void getUserAndEntry(String userName, String password){
        if(userName == null && TextUtils.isEmpty(userName)){
            etUserName.setError("User Name Empty");
        }
        if (password == null && TextUtils.isEmpty(password)){
            etUserPassword.setError("User Password Is empty");
        }

        if (databaseHelper.checkUser(userName,password)) {

            Intent accountsIntent = new Intent(this, MainActivity.class);
            accountsIntent.putExtra("UserName",userName);
            startActivity(accountsIntent);
            Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
            onBackPressed();


        } else {
            Toast.makeText(this, "Wrong User Name or Password", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
