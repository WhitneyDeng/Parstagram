package com.example.parstagram.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeActivity extends AppCompatActivity {
  public static final String TAG = "MainActivity";
  public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034; // note: can be any arbitrary number
  public String photoFileName = "photo.jpg";
  File photoFile;

  private EditText etDescription;
  private Button btnCaptureImage;
  private ImageView ivPostImage;
  private Button btnSubmit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etDescription = findViewById(R.id.etDescription);
    btnCaptureImage = findViewById(R.id.btnCaptureImage);
    ivPostImage = findViewById(R.id.ivPostImage);
    btnSubmit = findViewById(R.id.btnSubmit);

    btnCaptureImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        launchCamera();
      }
    });

    // when submit clicked, take information populated in view & push to database
    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String description = etDescription.getText().toString();
        if (description.isEmpty()) {
          Toast.makeText(ComposeActivity.this, "error: description cannot be empty", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        if (photoFile == null || ivPostImage.getDrawable() == null) {
          Toast.makeText(ComposeActivity.this, "error: image cannot be empty", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        ParseUser currentUser = ParseUser.getCurrentUser();
        savePost(description, currentUser, photoFile);
      }
    });
  }

  private void launchCamera() {
    // create Intent to take a picture and return control to the calling application
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Create a File reference for future access
    photoFile = getPhotoFileUri(photoFileName);

    // wrap File object (representing camera) into a content provider
    // required for API >= 24
    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
    Uri fileProvider = FileProvider.getUriForFile(ComposeActivity.this, "com.codepath.fileprovider", photoFile); // note: make sure authority match android:authorities="com.codepath.fileprovider" in AndroidManifest
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getPackageManager()) != null) {
      // Start the image capture intent to take photo
      startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
  }

  // Uri = uniform research identifier
  // Returns the File for a photo stored on disk given the fileName
  private File getPhotoFileUri(String photoFileName) {
    // Get safe storage directory for photos
    // Use `getExternalFilesDir` on Context to access package-specific directories.
    // This way, we don't need to request external read/write runtime permissions.
    // filepath to directory where photo will be stored
    File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
      Log.d(TAG, "failed to create directory");
    }

    // Return the file target for the photo based on filename
    // filepath to future photo
    return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
  }

  // method invoked when child application (e.g. camera) returns to parent application (Parstagram)
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) // returning from camera
    {
      if (resultCode == RESULT_OK)    //picture was taken
      {
        // by this point we have the camera photo on disk
        // get bitmap of photo taken
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        // RESIZE BITMAP, see section below: Bitmap is large and might exceed memory available to Parstagram. If encounter problem, resize bitmap: https://guides.codepath.org/android/Accessing-the-Camera-and-Stored-Media#resizing-the-picture
        // Load the taken image into a preview
        ivPostImage.setImageBitmap(takenImage);
      } else { // Result was a failure
        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
      }
    }
  }

  // create new post object and push to database
  private void savePost(String description, ParseUser currentUser, File photoFile) {
    // create & populate new Post model object
    Post post = new Post();
    post.setDescription(description);
    post.setImage(new ParseFile(photoFile));
    post.setUser(currentUser);

    // push Post object to database
    post.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        // error with pushing to databse
        if (e != null) {
          Log.e(TAG, "error while saving post", e);
          Toast.makeText(ComposeActivity.this, "error while saving post", Toast.LENGTH_SHORT).show();
          return; //note: not in tutorial (remove this line to clear interface even on failure to save post)
        }
        Log.i(TAG, "post saved successfully");

        // reset UI (visual indication of post success)
        etDescription.setText(""); // clear description line
        ivPostImage.setImageResource(0); // clear image view
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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case R.id.miLogout:
        logout();
        break;
      case R.id.miFeed:
        goFeedActivity();
        break;
      default:
        Toast.makeText(ComposeActivity.this, "error: menu itme not recognised", Toast.LENGTH_SHORT).show();
    }
    return super.onOptionsItemSelected(item);
  }

  private void goFeedActivity() {
    Intent i = new Intent(this, FeedActivity.class);
    startActivity(i);
  }

  private void logout() {
    ParseUser.logOut();
    goLoginActivity();
//        ParseUser currentUser = ParseUser.getCurrentUser();   //qq: update the current user by calling the ParseUser's getCurrentUser() how does this change anything?
  }

  private void goLoginActivity() {
    Intent i = new Intent(this, LoginActivity.class);
    startActivity(i);
    finish();
  }
}