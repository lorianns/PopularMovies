package com.udacity.lorianns.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            TextView title = (TextView) rootView.findViewById(R.id.title);
            TextView releaseYear = (TextView) rootView.findViewById(R.id.releaseYear);
            TextView rating = (TextView) rootView.findViewById(R.id.rating);
            TextView overview = (TextView) rootView.findViewById(R.id.overview);
            ImageView ivPoster = (ImageView) rootView.findViewById(R.id.imageView);

            // The detail Activity called via intent.  Inspect the intent for movie data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("MOVIE_DATA")) {
                MovieEntity movie = intent.getParcelableExtra("MOVIE_DATA");

                title.setText(movie.getTitle());
                releaseYear.setText(movie.getReleaseDate());
                rating.setText(String.format(getString(R.string.rating), movie.getRating()));
                overview.setText(movie.getOverview());

                Picasso.with(getActivity()).load(movie.getImagePath()).into(ivPoster);
            }
            return rootView;
        }
    }
}
