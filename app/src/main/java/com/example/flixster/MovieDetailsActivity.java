package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.media.Rating;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Date;
import java.util.zip.DataFormatException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_VIDEOS_URL_1 = "https://api.themoviedb.org/3/movie/";
    public static final String MOVIE_VIDEOS_URL_2 = "/videos?api_key=";
    public static final String TAG = "MovieDetailsActivity";

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    TextView tvDate;
    RatingBar rbVoteAverage;
    ImageView ivBackdrop;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        //unwrap extra to get the Movie instance & use this to set other fields
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        tvTitle = (TextView) binding.tvTitle;
        tvOverview = (TextView) binding.tvOverview;
        tvDate = (TextView) binding.tvDate;
        rbVoteAverage = (RatingBar) binding.rbVoteAverage;
        ivBackdrop = (ImageView) binding.ivBackdrop;

        //set imageUrl to poster path if landscape and to backdrop path if portrait
        //same with placeholder
        String imageUrl;
        int placeholder;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageUrl = movie.getPosterPath();
            placeholder = R.drawable.flicks_backdrop_placeholder;
        }
        else{
            imageUrl = movie.getBackdropPath();
            placeholder = R.drawable.flicks_movie_placeholder;
        }
        //rounding corners & loading image to ImageView
        int radius = 30;
        int margin = 10;
        Glide.with(this).load(imageUrl).placeholder(placeholder).transform(new RoundedCornersTransformation(radius, margin)).into(ivBackdrop);

        //set attributes in the layout based on the movie
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        Date releaseDate = movie.getReleaseDate();
        String day = (String) DateFormat.format("dd", releaseDate);
        String month = (String) DateFormat.format("MMMM", releaseDate);
        String year = (String) DateFormat.format("yyyy", releaseDate);
        tvDate.setText(month+" "+day+", "+year);

        //divide voteAverage by 2 if not equal to 0
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //get jsonobject from the video movie api
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(MOVIE_VIDEOS_URL_1+movie.getId()+MOVIE_VIDEOS_URL_2+getString(R.string.movie_api_key), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    //get key from jsonobject
                    JSONArray results = jsonObject.getJSONArray("results");
                    key = results.getJSONObject(0).getString("key");

                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        //if backdrop ImageView is clicked, it will create a new intent to start the MovieTrailerActivity class with the video_id as an extra and start the intent
        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on click works");
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                intent.putExtra("video_id", key);
                MovieDetailsActivity.this.startActivity(intent);
            }
        });

    }
}