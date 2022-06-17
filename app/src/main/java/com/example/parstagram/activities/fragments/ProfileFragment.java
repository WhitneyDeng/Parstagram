package com.example.parstagram.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends FeedFragment{

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    layoutManager = new GridLayoutManager(getContext(), 3);
    rvPosts.setLayoutManager(layoutManager); //set layout manager
  }

  @Override
  protected void queryPosts() {
    // specify which database class to query
    ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

    // specify query params | ref: https://dashboard.back4app.com/apidocs/6RnGHwzgtrpaxaT57tN9dRxkdjRAaDFk9ogeWMTF?java#queries
    query.include(Post.KEY_USER);   // include User data of each Post class in response
    query.orderByDescending(Post.KEY_CREATED_AT); // order in descending order by creation date
    query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
    query.setLimit(INITIAL_POST_LOAD_SIZE); // only load first 20 posts

    // perform query
    query.findInBackground(new FindCallback<Post>() {
      @Override
      public void done(List<Post> posts, ParseException e) {
        // check for & handle error
        if (e != null) {
          Log.e(TAG, "Issue with post query", e);
          return;
        }
        Log.i(TAG, "Post query success");

        // save posts to list & notify adapter of new data (to show on screen)
        adapter.addAll(posts);
        adapter.notifyDataSetChanged();
      }
    });
  }
}
