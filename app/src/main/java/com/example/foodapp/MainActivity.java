
package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Apiconnect.FoodApiService;
import com.example.foodapp.apiImages.ImageModel.MonAn;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ImageView dislayimages;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;

    private Handler handler = new Handler();
    private Runnable imageSlider;

    private List<MonAn> monAnList = new ArrayList<>();
    private List<MonAn> fullList = new ArrayList<>();

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

    private Retrofit retrofit;
    private FoodApiService apiService;

    private int iduser = -1; // Lưu iduser lấy từ login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dislayimages = findViewById(R.id.dislayimages);
        edtSearch = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recyclerView);

        // Lấy iduser từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        iduser = prefs.getInt("iduser", -1);

        if (iduser == -1) {
            Toast.makeText(this, "Không tìm thấy ID người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            // Có thể điều hướng về LoginActivity nếu cần
        }
//        else {
//            Toast.makeText(this, " " , Toast.LENGTH_SHORT).show();
//        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(this, monAnList);
        recyclerView.setAdapter(adapter);

        // Init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(FoodApiService.class);

        // Load danh sách món ăn
        fetchMonAnFromApi();

        // Auto image slider
        imageSlider = new Runnable() {
            @Override
            public void run() {
                dislayimages.setImageResource(imageIds[currentIndex]);
                currentIndex = (currentIndex + 1) % imageIds.length;
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(imageSlider);

        // Search
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMonAn(s.toString());
            }
        });

        // 👉 Thêm sự kiện chuyển sang giỏ hàng
        ImageButton btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Cart.class);
            intent.putExtra("iduser", iduser); // Truyền iduser nếu cần
            startActivity(intent);
        });
        ImageButton btnMy = findViewById(R.id.btnMy);
        btnMy.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("iduser", iduser); // Truyền id người dùng qua Profile
            startActivity(intent);
        });
        ImageButton btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("iduser", iduser);
            startActivity(intent);
        });
    }

    private void fetchMonAnFromApi() {
        Call<List<MonAn>> call = apiService.getDanhSachMonAn();
        call.enqueue(new Callback<List<MonAn>>() {
            @Override
            public void onResponse(Call<List<MonAn>> call, Response<List<MonAn>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullList = response.body();
                    monAnList.clear();
                    monAnList.addAll(fullList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi lấy dữ liệu món ăn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MonAn>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchMonAn(String keyword) {
        monAnList.clear();
        if (keyword.trim().isEmpty()) {
            monAnList.addAll(fullList);
        } else {
            for (MonAn item : fullList) {
                if (item.getTenmAn().toLowerCase().contains(keyword.toLowerCase())) {
                    monAnList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(imageSlider);
    }
}
