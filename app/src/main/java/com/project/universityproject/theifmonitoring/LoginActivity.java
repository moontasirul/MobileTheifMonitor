package com.project.universityproject.theifmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a activity class, which is act for all login UI
 * and login related background
 * process
 */

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


    /**
     * initialize all UI elements
     */
    private void initialView() {
        tvRegister = findViewById(R.id.tv_register);
        etUserName = findViewById(R.id.userId);
        etUserPassword =  findViewById(R.id.user_password);
        btnLogin =  findViewById(R.id.btn_login);
        databaseHelper = new DataBaseHelper(this);
    }




    /**
     * set  Click Listener for all button
     */
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


    /**
     * Get user info from UI and check validity the get data form database...
     * if user is valid then transfer to the next activity otherwise show error
     * @param userName
     * @param password
     */
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
            Toast.makeText(this, "Welcome "+ userName, Toast.LENGTH_LONG).show();
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
