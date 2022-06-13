package com.example.parstagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.parstagram.R;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity
{
    public static final String TAG = "FeedActivity";
    public static final int INITIAL_POST_LOAD_SIZE = 20;

    private RecyclerView rvPosts;

    protected LinearLayoutManager layoutManager;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvPosts = findViewById(R.id.rvPosts);

        // initialize the array that will hold posts and create a PostsAdapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        // recycler view setup: layout manager & the adapter
        layoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(layoutManager); //set layout manager
        rvPosts.setAdapter(adapter); //set adapter for rv

        queryPosts();
    }

    private void queryPosts()
    {
        // specify which database class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // specify query params | ref: https://dashboard.back4app.com/apidocs/6RnGHwzgtrpaxaT57tN9dRxkdjRAaDFk9ogeWMTF?java#queries
        query.include(Post.KEY_USER);   // include User data of each Post class in response
        query.orderByDescending(Post.KEY_CREATED_AT); // order in descending order by creation date
        query.setLimit(INITIAL_POST_LOAD_SIZE); // only load first 20 posts

        // perform query
        query.findInBackground(new FindCallback<Post>()
        {
            @Override
            public void done(List<Post> posts, ParseException e)
            {
                // check for & handle error
                if (e != null)
                {
                    Log.e(TAG, "Issue with post query", e);
                    return ;
                }

                // Log each received post
//                for (Post post : posts)
//                {
//                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
//                }

                // save posts to list & notify adapter of new data (to show on screen)
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}