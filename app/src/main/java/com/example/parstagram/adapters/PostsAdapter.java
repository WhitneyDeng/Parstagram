package com.example.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.example.parstagram.activities.PostDetailsActivity;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
  public static final String TAG = "PostsAdapter";

  private Context context;
  private List<Post> posts;

  public PostsAdapter(Context context, List<Post> posts) {
    this.context = context;
    this.posts = posts;
  }

  @NonNull
  @Override
  public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
    Post post = posts.get(position);

    holder.bind(post);
  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

  //FOR: swipe down to refresh
  // Clean all elements of the recycler
  public void clear() {
    posts.clear();
    notifyDataSetChanged();
  }

  //FOR: swipe down to refresh
  // Add a list of items -- change to type used
  public void addAll(List<Post> list) {
    posts.addAll(list);
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      tvUsername = itemView.findViewById(R.id.tvUsername);
      ivImage = itemView.findViewById(R.id.ivImage);
      tvDescription = itemView.findViewById(R.id.tvDescription);

      itemView.setOnClickListener(this);
    }

    public void bind(Post post) {
      tvUsername.setText(post.getUser().getUsername());
      tvDescription.setText(post.getDescription());

      ParseFile image = post.getImage();
      if (image != null) {
        Glide.with(context)
                .load(image.getUrl())
                .into(ivImage);
      } else {
        // empty ImageView if post has no image
        ivImage.setImageResource(0);
      }
    }

    // if row is clicked
    @Override
    public void onClick(View v) {
      // gets item position
      int position = getAdapterPosition();

      Log.i(TAG, String.format("Row %d clicked", position));

      // make sure the position is valid, i.e. actually exists in the view
      if (position != RecyclerView.NO_POSITION) {
        // get the post at the position, this won't work if the class is static
        Post post = posts.get(position);
        // create intent for the new activity
        Intent intent = new Intent(context, PostDetailsActivity.class);

        // qq: since only need
        intent.putExtra(Post.class.getSimpleName(), post);
        // show the activity
        context.startActivity(intent);
      }
    }
  }
}
