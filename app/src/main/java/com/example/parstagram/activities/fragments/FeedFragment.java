package com.example.parstagram.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

  public FeedFragment() {
    // Required empty public constructor
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_feed, container, false);
  }
}