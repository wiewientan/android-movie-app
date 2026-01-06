package com.example.movie_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    Button btnAdd, btnReset;
    EditText etSearchRange;

    List<Movie> movieList = new ArrayList<>();
    List<Movie> filteredList = new ArrayList<>();
    MovieAdapter adapter;

    // Pastikan IP ini benar (cek ipconfig)
    String URL_GET = "http://10.10.16.166/movie_api/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovies = findViewById(R.id.rvMovies);
        btnAdd = findViewById(R.id.btnAddMovie);
        btnReset = findViewById(R.id.btnReset);
        etSearchRange = findViewById(R.id.etFilterDate);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(filteredList);
        rvMovies.setAdapter(adapter);

        // FITUR SEARCH RANGE
        etSearchRange.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Pilih Rentang Tanggal");

            final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

            // Tambahkan listener untuk menangani error crash jika fragment manager bermasalah
            picker.show(getSupportFragmentManager(), "DATE_RANGE_PICKER");

            picker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                String startDate = sdf.format(new Date(selection.first));
                String endDate = sdf.format(new Date(selection.second));

                etSearchRange.setText(startDate + " - " + endDate);
                jalankanFilterRange(startDate, endDate);
            });
        });

        btnReset.setOnClickListener(v -> {
            etSearchRange.setText("");
            filteredList.clear();
            filteredList.addAll(movieList);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Data Reset", Toast.LENGTH_SHORT).show();
        });

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddMovieActivity.class));
        });
    }

    private void jalankanFilterRange(String start, String end) {
        filteredList.clear();
        for (Movie film : movieList) {
            String tglFilm = film.getDateRelease();
            // Bandingkan string (YYYY-MM-DD sangat aman dibandingkan string)
            if (tglFilm.compareTo(start) >= 0 && tglFilm.compareTo(end) <= 0) {
                filteredList.add(film);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Film tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }

    private void loadMovies() {
        movieList.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            movieList.add(new Movie(
                                    obj.getString("id"),
                                    obj.getString("movie_name"),
                                    obj.getString("genre"),
                                    obj.getString("date_release"),
                                    obj.getString("producer")
                            ));
                        }
                        filteredList.clear();
                        filteredList.addAll(movieList);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Format data salah!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Server tidak terjangkau!", Toast.LENGTH_LONG).show()
        );
        Volley.newRequestQueue(this).add(request);
    }
}