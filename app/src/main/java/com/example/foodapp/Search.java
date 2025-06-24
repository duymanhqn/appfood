package com.example.foodapp;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Apiconnect.FoodApiService;
import com.example.foodapp.apiImages.ImageModel;
import com.example.foodapp.apiImages.ImageModel.MonAn;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<MonAn> monAnList;

    private void searchMonAn(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApiService apiService = retrofit.create(FoodApiService.class);
        Call<List<ImageModel.MonAn>> call = apiService.searchMonAn(keyword);

        call.enqueue(new Callback<List<ImageModel.MonAn>>() {
            @Override
            public void onResponse(Call<List<ImageModel.MonAn>> call, Response<List<ImageModel.MonAn>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    monAnList = response.body();
                    adapter = new FoodAdapter(Search.this, monAnList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Search.this, "Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImageModel.MonAn>> call, Throwable t) {
                Toast.makeText(Search.this, "Lỗi tìm kiếm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
