package com.udacity.lorianns.popularmovies;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private ArrayList<MovieEntity> movieArray;
    private String selectedSort;

    private ImageAdapter imageAdapter;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectedSort = getString(R.string.pref_sort_by_default);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        if (savedInstanceState == null || !savedInstanceState.containsKey("MOVIE_ARRAY")) {
            movieArray = new ArrayList<>();
        } else {
            movieArray = savedInstanceState.getParcelableArrayList("MOVIE_ARRAY");
            selectedSort = savedInstanceState.getString("SORT_BY");
        }

        //set UI controls
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("MOVIE_DATA", movieArray.get(position));
                startActivity(intent);
            }
        });

        imageAdapter = new ImageAdapter(getActivity(), movieArray);
        gridview.setAdapter(imageAdapter);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        fetchMovieData(selectedSort);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.movielistfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_most_popular:
                fetchMovieData(getString(R.string.pref_sort_by_default));
                return true;
            case R.id.action_top_rated:
                fetchMovieData(getString(R.string.pref_sort_by_top_rated));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("MOVIE_ARRAY", movieArray);
        outState.putString("SORT_BY", selectedSort);
    }


    private void fetchMovieData(String sortBy) {
        selectedSort = sortBy;
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(sortBy);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, MovieEntity[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        public FetchMovieTask() {
            super();
        }


        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private MovieEntity[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

            final String _LIST = "results";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(_LIST);
            MovieEntity[] movieList = new MovieEntity[movieJsonArray.length()];

            for (int i = 0; i < movieJsonArray.length(); i++) {

                JSONObject movie = movieJsonArray.getJSONObject(i);
                movieList[i] = new MovieEntity(movie);
            }

            return movieList;
        }

        @Override
        protected MovieEntity[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String sortBy = params[0];

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "https://api.themoviedb.org/3/movie/" + sortBy + "?";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieEntity[] result) {

            if (result != null) {
                imageAdapter.clear();
                movieArray.addAll(new ArrayList<MovieEntity>(Arrays.asList(result)));
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

}
