package com.example.foodapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.apiImages.ImageModel.MonAn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView dislayimages;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<MonAn> monAnList = new ArrayList<>();

    private Handler handler = new Handler();
    private Runnable imageSlider;
    private int[] imageIds = {
            R.drawable.boluclac,
            R.drawable.cachimchienmam,
            R.drawable.cadieuhonghap,
            R.drawable.changanuong,
            R.drawable.changarutxuong,
            R.drawable.dauhuchien,
            R.drawable.gachienmam,
            R.drawable.gakhosa,
            R.drawable.thitboxao
    };
    private int currentIndex = 0;

    private int iduser = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dislayimages = findViewById(R.id.dislayimages);
        edtSearch = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        iduser = prefs.getInt("iduser", -1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(this, monAnList);
        recyclerView.setAdapter(adapter);

        fetchAllMonAn();

        imageSlider = new Runnable() {
            @Override
            public void run() {
                dislayimages.setImageResource(imageIds[currentIndex]);
                currentIndex = (currentIndex + 1) % imageIds.length;
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(imageSlider);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    fetchAllMonAn();
                } else {
                    searchMonAn(s.toString().trim());
                }
            }
        });
    }

    private void fetchAllMonAn() {
        String url = "http://10.0.2.2:8888/api/foodapi"; // Endpoint lấy toàn bộ
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> parseResponse(response),
                error -> Toast.makeText(this, "Lỗi tải danh sách", Toast.LENGTH_SHORT).show());

        queue.add(req);
    }

    private void searchMonAn(String keyword) {
        String url = "http://10.0.2.2:8888/api/search?keyword=" + keyword;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> parseResponse(response),
                error -> Toast.makeText(this, "Lỗi tìm kiếm", Toast.LENGTH_SHORT).show());

        queue.add(req);
    }

    private void parseResponse(JSONArray response) {
        monAnList.clear();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject o = response.getJSONObject(i);
                MonAn mon = new MonAn(
                        o.getString("tenmAn"),
                        o.getString("Gia"),
                        o.getString("DVT"),
                        o.getString("hinhanh"),
                        o.getString("mota")
                );
                monAnList.add(mon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(imageSlider);
    }
}
