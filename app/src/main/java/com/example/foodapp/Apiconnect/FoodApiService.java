package com.example.foodapp.Apiconnect; // ✅ đúng package

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// ✅ đúng model import
import com.example.foodapp.apiImages.ImageModel;

public interface FoodApiService {

    // ✅ Lấy danh sách món ăn
    @GET("api/foodapi")
    Call<List<ImageModel.MonAn>> getDanhSachMonAn();

    // ✅ Tìm kiếm món ăn từ server qua query ?keyword=...
    @GET("search")
    Call<List<ImageModel.MonAn>> searchMonAn(@Query("keyword") String keyword);
}
