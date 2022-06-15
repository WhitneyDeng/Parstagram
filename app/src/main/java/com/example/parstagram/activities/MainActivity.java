package com.example.parstagram.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity {
  public static final String TAG = "MainActivity";


  private BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose);


    bottomNavigationView = findViewById(R.id.bottomNavigation);

//    final FragmentManager fragmentManager = getSupportFragmentManager();
//
//    // define your fragments here
//    final Fragment fragment1 = new FirstFragment();
//    final Fragment fragment2 = new SecondFragment();
//    final Fragment fragment3 = new ThirdFragment();


    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
          case R.id.action_home:
//            fragment = fragment1;
            Toast.makeText(MainActivity.this, "home selected", Toast.LENGTH_SHORT).show();
            break;
          case R.id.action_compose:
//            fragment = fragment2;
            Toast.makeText(MainActivity.this, "compose selected", Toast.LENGTH_SHORT).show();
            break;
          case R.id.action_profile:
          default:
//            fragment = fragment3;
            Toast.makeText(MainActivity.this, "profile selected", Toast.LENGTH_SHORT).show();
            break;
        }
//        fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit();
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