package com.project.universityproject.theifmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etUserPassword;
    private EditText etConPassword;

    private Button btnRegister;

    private DataBaseHelper databaseHelper;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialView();
        setOnclick();
    }


    private void initialView() {
        etUserName =  findViewById(R.id.user_name);
        etConPassword =  findViewById(R.id.confirm_Password);
        etUserPassword = findViewById(R.id.user_password);

        btnRegister = findViewById(R.id.btn_registration);

        databaseHelper = new DataBaseHelper(this);
        user = new User();

    }

    private void setOnclick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserInDB(etUserName.getText().toString().trim(), etUserPassword.getText().toString().trim(), etConPassword.getText().toString().trim());
            }
        });
    }

    private void setUserInDB(String userName, String password, String conPassword) {

        if (!databaseHelper.checkUser(userName)) {

            user.setUserName(userName);
            user.setPassword(password);

            databaseHelper.addUser(user);

            // Toast to show success message that record saved successfully
            Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show();
            emptyInputEditText();

        } else {

            Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        etUserName.setText(null);
        etUserPassword.setText(null);
        etConPassword.setText(null);
    }

    public void onBackLogin(View view) {
        onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
