package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Parcel
public class Movie {

    //fields
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    SimpleDateFormat format;
    Date releaseDate;
    Integer id;


    //constructors
    public Movie(){

    }

    public Movie(JSONObject jsonObject) throws JSONException, ParseException {
        //set fields using jsonobject parameter
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        format = new SimpleDateFormat("yyyy-MM-dd");
        voteAverage = jsonObject.getDouble("vote_average");
        releaseDate = format.parse(jsonObject.getString("release_date"));
        id = jsonObject.getInt("id");
    }

    //create list of movies from jsonarray
    public static List<Movie> fromJSONArray(JSONArray movieJSONArray) throws JSONException, ParseException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJSONArray.length(); i++){
            movies.add(new Movie(movieJSONArray.getJSONObject(i)));
        }
        return movies;
    }

    //getters
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Integer getId() {
        return id;
    }
}
