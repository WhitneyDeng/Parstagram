package com.example.parstagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parstagram.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity
{
    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // if user already logged in, start on MainActivity (instead of LoginActivity)
        if (ParseUser.getCurrentUser() != null)
        {
            goMainActivity();
        }

        // set up view
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password)
    {
        Log.i(TAG, "Attempting to login user " + username);
        //todo: navigate to main activity if signed in properly
        // why in background? don't want this to run in main thread to cause bad user experience
        ParseUser.logInInBackground(username, password, new LogInCallback()
        {
            @Override
            public void done(ParseUser user, ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Issue with login", e);

                    Toast.makeText(LoginActivity.this, "Issue with Login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // navigate to MainActivity
    private void goMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        // remove LoginActivity from back stack
        // user should be unable to return to login screen through back button
        finish();
    }
}