package com.example.parstagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
  }

  // Menu icons are inflated just as they were with actionbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);

    return true;
  }
}