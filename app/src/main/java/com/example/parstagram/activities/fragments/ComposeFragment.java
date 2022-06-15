package com.example.parstagram.activities.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.R;
import com.example.parstagram.activities.FeedActivity;
import com.example.parstagram.activities.LoginActivity;
import com.example.parstagram.activities.MainActivity;
import com.example.parstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment
{
  public final static String TAG = "ComposeFragment";

  public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034; // note: can be any arbitrary number
  public String photoFileName = "photo.jpg";
  File photoFile;

  private EditText etDescription;
  private Button btnCaptureImage;
  private ImageView ivPostImage;
  private Button btnSubmit;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public ComposeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment ComposeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static ComposeFragment newInstance(String param1, String param2) {
    ComposeFragment fragment = new ComposeFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

//  @Override
//  public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    if (getArguments() != null) {
//      mParam1 = getArguments().getString(ARG_PARAM1);
//      mParam2 = getArguments().getString(ARG_PARAM2);
//    }
//  }

  // The onCreateView method is called when Fragment should create its View object hierarchy,
  // either dynamically or via XML layout inflation.
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_compose, container, false);
  }

  // This event is triggered soon after onCreateView().
  // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // Setup any handles to view objects here
    // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    etDescription = view.findViewById(R.id.etDescription);
    btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
    ivPostImage = view.findViewById(R.id.ivPostImage);
    btnSubmit = view.findViewById(R.id.btnSubmit);

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
          Toast.makeText(getContext(), "error: description cannot be empty", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        if (photoFile == null || ivPostImage.getDrawable() == null) {
          Toast.makeText(getContext(), "error: image cannot be empty", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
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
    Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile); // note: make sure authority match android:authorities="com.codepath.fileprovider" in AndroidManifest
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
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
    File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
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
          Toast.makeText(getContext(), "error while saving post", Toast.LENGTH_SHORT).show();
          return; //note: not in tutorial (remove this line to clear interface even on failure to save post)
        }
        Log.i(TAG, "post saved successfully");

        // reset UI (visual indication of post success)
        etDescription.setText(""); // clear description line
        ivPostImage.setImageResource(0); // clear image view
      }
    });
  }

  //ref: add option menu to fragment: https://stackoverflow.com/questions/8308695/how-to-add-options-menu-to-fragment-in-android
  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case R.id.miLogout:
        Toast.makeText(getContext(), "logout clicked", Toast.LENGTH_SHORT).show();
        logout();
        break;
//      case R.id.miFeed:
//        goFeedActivity();
//        break;
      default:
        Toast.makeText(getContext(), "error: menu itme not recognised", Toast.LENGTH_SHORT).show();
    }
    return super.onOptionsItemSelected(item);
  }


  private void logout() {

    ParseUser.logOut();
    goLoginActivity();
//        ParseUser currentUser = ParseUser.getCurrentUser();   //qq: update the current user by calling the ParseUser's getCurrentUser() how does this change anything?
  }

  private void goLoginActivity() {
    Intent i = new Intent(getContext(), LoginActivity.class);
    startActivity(i);
    getActivity().finish();
  }

//  private void goFeedActivity() {
//    Intent i = new Intent(getContext(), FeedActivity.class);
//    startActivity(i);
//  }
}