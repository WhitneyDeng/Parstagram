package com.example.parstagram.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.R;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
  public static final String TAG = "FeedFragment";

  public static final int INITIAL_POST_LOAD_SIZE = 20;

  protected RecyclerView rvPosts;
  protected LinearLayoutManager layoutManager;
  protected PostsAdapter adapter;

  private SwipeRefreshLayout swipeContainer;

  public FeedFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_feed, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
//    ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);

    rvPosts = view.findViewById(R.id.rvPosts);

    // initialize the array that will hold posts and create a PostsAdapter
    adapter = new PostsAdapter(getContext(), new ArrayList<>());

    // recycler view setup: layout manager & the adapter
    layoutManager = new LinearLayoutManager(getContext());
    rvPosts.setLayoutManager(layoutManager); //set layout manager
    rvPosts.setAdapter(adapter); //set adapter for rv

    queryPosts();

    // SETUP: pull to refresh
    swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);    // signal refresh has finished (hide refresh indicator on UI)
      }
    });

    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
  }

  protected void queryPosts() {
    // specify which database class to query
    ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

    // specify query params | ref: https://dashboard.back4app.com/apidocs/6RnGHwzgtrpaxaT57tN9dRxkdjRAaDFk9ogeWMTF?java#queries
    query.include(Post.KEY_USER);   // include User data of each Post class in response
    query.orderByDescending(Post.KEY_CREATED_AT); // order in descending order by creation date
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