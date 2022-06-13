package com.example.parstagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity
{
    private Post post;

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvRelativeTimestamp;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvRelativeTimestamp = findViewById(R.id.tvRelativeTimestamp);
        tvDescription = findViewById(R.id.tvDescription);

        // unwrap the movie passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", post.getDescription()));

        // set the title and overview
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        ParseFile image = post.getImage();
        if (image != null)
        {
            Glide.with(this)
                    .load(image.getUrl())
                    .into(ivImage);
        }
        else
        {
            // empty ImageView if post has no image
            ivImage.setImageResource(0);
        }
    }
}