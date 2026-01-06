package com.example.movie_app;// Sesuaikan package name kamu

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMovieActivity extends AppCompatActivity {
    EditText etName, etGenre, etDate, etProducer;
    Button btnDone;
    // GANTI IP INI DENGAN IP LAPTOP KAMU (Cek di CMD: ipconfig)
    String URL_ADD = "http://10.10.16.166/movie_api/tambah_movie.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        etName = findViewById(R.id.etMovieName);
        etGenre = findViewById(R.id.etGenre);
        etDate = findViewById(R.id.etDateRelease);
        etProducer = findViewById(R.id.etProducer);
        btnDone = findViewById(R.id.btnDone);

        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                etDate.setText(year + "-" + (month + 1) + "-" + day);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnDone.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_ADD,
                response -> {
                    // Log atau Toast isinya supaya ketahuan kalau PHP-nya yang error
                    Toast.makeText(AddMovieActivity.this, "Response: " + response, Toast.LENGTH_LONG).show();
                    if(response.contains("Berhasil")) {
                        finish();
                    }
                },
                error -> Toast.makeText(AddMovieActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("movie_name", etName.getText().toString());
                params.put("genre", etGenre.getText().toString());
                params.put("date_release", etDate.getText().toString());
                params.put("producer", etProducer.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}