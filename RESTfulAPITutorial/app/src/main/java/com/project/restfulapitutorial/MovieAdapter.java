package com.project.restfulapitutorial;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// Custom adapter to set up layout and data in the RecyclerView
public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private Context context;

    public MovieAdapter(List<Movie> movies, Context context) {
        mMovies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.single_movie_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        String movieTitle = movie.getTitle();
        holder.titleTextView.setText(movieTitle);

        String movieYear = movie.getYear();
        holder.yearTextView.setText(movieYear);

        // Loads image from API or default if the first is unavailable
        String posterUrl = movie.getPoster();
        Glide
                .with(context)
                .load(posterUrl)
                .placeholder(R.mipmap.no_image)
                .into(holder.posterImageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;
        public TextView titleTextView;
        public TextView yearTextView;

        public ViewHolder(View view) {
            super(view);

            posterImageView = view.findViewById(R.id.singleItemImage);
            titleTextView = view.findViewById(R.id.singleItemTitle);
            yearTextView = view.findViewById(R.id.singleItemYear);
        }
    }
}
