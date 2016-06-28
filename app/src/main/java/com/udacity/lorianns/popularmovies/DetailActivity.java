package com.udacity.lorianns.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView title = (TextView) findViewById(R.id.title);
        TextView releaseYear = (TextView) findViewById(R.id.releaseYear);
        TextView duration = (TextView) findViewById(R.id.duration);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView overview = (TextView) findViewById(R.id.overview);
        ImageView ivPoster = (ImageView) findViewById(R.id.imageView);

        Intent i = getIntent();
        if (getIntent() != null || getIntent().hasExtra("MOVIE_DATA")) {
            MovieEntity movie = getIntent().getParcelableExtra("MOVIE_DATA");

            title.setText(movie.getTitle());
            releaseYear.setText(movie.getReleaseDate());
            duration.setText(movie.getTitle());
            rating.setText(movie.getRating() + "/10");
            overview.setText(movie.getOverview());

            Picasso.with(this).load(movie.getImagePath()).into(ivPoster);
        }

    }
}
