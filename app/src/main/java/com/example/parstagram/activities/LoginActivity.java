package com.example.parstagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parstagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
  public static final String TAG = "LoginActivity";
  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private Button btnSignup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // if user already logged in, start on MainActivity (instead of LoginActivity)
    if (ParseUser.getCurrentUser() != null) {
      goMainActivity();
    }

    // set up view
    etUsername = findViewById(R.id.etUsername);
    etPassword = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);
    btnSignup = findViewById(R.id.btnSignup);

    // set up login & signup listeners
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick login button");
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        loginUser(username, password);
      }
    });

    btnSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick sign up button");
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        signupUser(username, password);
      }
    });
  }

  // src: https://guides.codepath.org/android/Building-Data-driven-Apps-with-Parse#user-signup
  private void signupUser(String username, String password) {
    Log.i(TAG, "Attempting to signup user" + username);
    ParseUser user = new ParseUser();

    user.setUsername(username);
    user.setPassword(password);

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with signup", e);

          Toast.makeText(LoginActivity.this, "Issue with Signup: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        goMainActivity();
        Toast.makeText(LoginActivity.this, "Signup Success", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
      }
    });
  }

  private void loginUser(String username, String password) {
    Log.i(TAG, "Attempting to login user " + username);
    //todo: navigate to main activity if signed in properly
    // why in background? don't want this to run in main thread to cause bad user experience
    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with login", e);

          Toast.makeText(LoginActivity.this, "Issue with Login: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        goMainActivity();
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
      }
    });
  }

  // navigate to MainActivity
  private void goMainActivity() {
    Intent i = new Intent(this, CreatePostActivity.class);
    startActivity(i);

    // remove LoginActivity from back stack
    // user should be unable to return to login screen through back button
    finish();
  }
}