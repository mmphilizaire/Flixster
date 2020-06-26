package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;
    ItemMovieBinding binding;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        TextView tvVoteAvg;
        TextView tvDate;

        public ViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            tvTitle = binding.tvTitle;
            tvOverview = binding.tvOverview;
            ivPoster = binding.ivPoster;
            tvVoteAvg = binding.tvVoteAvg;
            tvDate = binding.tvDate;
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            float voteAverage = movie.getVoteAverage().floatValue();
            tvVoteAvg.setText(String.format("%.1f", voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage));

            Date releaseDate = movie.getReleaseDate();
            String day = (String) DateFormat.format("dd", releaseDate);
            String month = (String) DateFormat.format("MMMM", releaseDate);
            tvDate.setText(month+" "+day);

            String imageUrl;
            int placeholder;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                placeholder = R.drawable.flicks_backdrop_placeholder;
            }
            else{
                imageUrl = movie.getPosterPath();
                placeholder = R.drawable.flicks_movie_placeholder;
            }
            int radius = 30;
            int margin = 10;
            Glide.with(context).load(imageUrl).placeholder(placeholder).transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }
        }
    }

}
