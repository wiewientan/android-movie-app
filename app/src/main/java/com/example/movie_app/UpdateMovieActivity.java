package com.example.movie_app;

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

public class UpdateMovieActivity extends AppCompatActivity {
    EditText etName, etGenre, etDate, etProducer;
    Button btnUpdate, btnDelete;
    String id;

    // GANTI IP INI SESUAI IP LAPTOPMU
    String URL_UPDATE = "http://10.10.16.166/movie_api/edit_movie.php";
    String URL_DELETE = "http://10.10.16.166/movie_api/hapus_movie.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        // Inisialisasi View sesuai ID di XML
        etName = findViewById(R.id.etUpdateMovieName);
        etGenre = findViewById(R.id.etUpdateGenre);
        etDate = findViewById(R.id.etUpdateDate);
        etProducer = findViewById(R.id.etUpdateProducer);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Ambil data dari Intent (Kunci/Key harus sama dengan di MovieAdapter)
        id = getIntent().getStringExtra("id");
        etName.setText(getIntent().getStringExtra("movie_name"));
        etGenre.setText(getIntent().getStringExtra("genre"));
        etDate.setText(getIntent().getStringExtra("date_release"));
        etProducer.setText(getIntent().getStringExtra("producer"));

        // Date Picker agar milih tanggal tinggal klik
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                // Format YYYY-MM-DD agar masuk ke MySQL dengan benar
                etDate.setText(y + "-" + String.format("%02d", (m + 1)) + "-" + String.format("%02d", d));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnUpdate.setOnClickListener(v -> updateData());

        btnDelete.setOnClickListener(v -> deleteData());
    }

    private void updateData() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATE,
                response -> {
                    Toast.makeText(this, "Response: " + response, Toast.LENGTH_SHORT).show();
                    if(response.contains("Berhasil")) finish();
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("movie_name", etName.getText().toString());
                params.put("date_release", etDate.getText().toString());
                // Genre dan Producer tidak dikirim karena tidak diupdate di query PHP
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void deleteData() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE,
                response -> {
                    Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}