package com.example.parstagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parstagram.R;
import com.example.parstagram.activities.fragments.ComposeFragment;
import com.example.parstagram.activities.fragments.FeedFragment;
import com.example.parstagram.activities.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
  public static final String TAG = "MainActivity";


  private BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    bottomNavigationView = findViewById(R.id.bottomNavigation);

    final FragmentManager fragmentManager = getSupportFragmentManager();
    //todo: hide option menu

    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
          case  R.id.action_home:
            fragment = new FeedFragment();
            break;
          case R.id.action_compose:
            fragment = new ComposeFragment();
            break;
          case R.id.action_profile:
          default:
            fragment = new ProfileFragment();
            break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment)  //replace FrameLayout with fragment
                .commit();                            //make the change immediately
        return true;
      }
    });
    // default bottom nav selection
    bottomNavigationView.setSelectedItemId(R.id.action_home);
  }

  // Menu icons are inflated just as they were with actionbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch(id)
    {
      case R.id.miLogout:
        Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
        logout();
      default:
    }
    return super.onOptionsItemSelected(item);
  }

  private void logout()
  {
    ParseUser.logOut();
    goLoginActivity();
//        ParseUser currentUser = ParseUser.getCurrentUser();   //todo: update the current user by calling the ParseUser's getCurrentUser() how does this change anything?
  }

  private void goLoginActivity()
  {
    Intent i = new Intent(this, LoginActivity.class);
    startActivity(i);
    finish();
  }
}