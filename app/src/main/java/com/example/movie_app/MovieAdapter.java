package com.example.movie_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    // Variabel untuk mendeteksi waktu klik terakhir
    private long lastClickTime = 0;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.txtName.setText(movie.getMovieName());
        holder.txtGenre.setText("Genre : " + movie.getGenre());
        holder.txtDate.setText("Date : " + movie.getDateRelease());
        holder.txtProducer.setText("Producer : " + movie.getProducer());

        // Logika Double Click pada Item
        holder.itemView.setOnClickListener(v -> {
            long clickTime = System.currentTimeMillis();
            // Jika jarak antar klik kurang dari 300 milidetik, dianggap Double Click
            if (clickTime - lastClickTime < 300) {
                Context context = v.getContext();
                Intent intent = new Intent(context, UpdateMovieActivity.class);

                // Mengirim data film ke UpdateMovieActivity
                intent.putExtra("id", movie.getId()); // Pastikan di Movie.java ada method getId()
                intent.putExtra("movie_name", movie.getMovieName());
                intent.putExtra("genre", movie.getGenre());
                intent.putExtra("date_release", movie.getDateRelease());
                intent.putExtra("producer", movie.getProducer());

                context.startActivity(intent);
            }
            lastClickTime = clickTime;
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtGenre, txtDate, txtProducer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtMovieName);
            txtGenre = itemView.findViewById(R.id.txtGenre);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtProducer = itemView.findViewById(R.id.txtProducer);
        }
    }
}