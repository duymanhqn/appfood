
package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class Detailfood extends AppCompatActivity {

    ImageView imageView;
    TextView tvTenmonna, tvGiaAndDvt, tvMota, tvQuantity;
    Button btnDecrease, btnIncrease, button;

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailfood);

        imageView   = findViewById(R.id.image);
        tvTenmonna  = findViewById(R.id.tvTenmonna);
        tvGiaAndDvt = findViewById(R.id.tvGiaAndDvt);
        tvMota      = findViewById(R.id.tvMota);
        tvQuantity  = findViewById(R.id.tvQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        button      = findViewById(R.id.button);

        String tenMonAn = getIntent().getStringExtra("tenmAn");
        String mota     = getIntent().getStringExtra("mota");
        String dvt      = getIntent().getStringExtra("dvt");
        String gia      = getIntent().getStringExtra("gia");       // e.g. "50.000"
        String hinhanh  = getIntent().getStringExtra("hinhanh");

        tvTenmonna.setText(tenMonAn);
        tvGiaAndDvt.setText(gia + " " + dvt);
        tvMota.setText(mota);
        tvQuantity.setText(String.valueOf(quantity));

        Glide.with(this)
                .load("http://10.0.2.2:8888/images/" + hinhanh)
                .placeholder(R.drawable.boluclac)
                .into(imageView);

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        button.setOnClickListener(v -> {
            // Lấy iduser
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int makh = prefs.getInt("iduser", -1);
            if (makh == -1) {
                Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuẩn bị JSON body
            JSONObject body = new JSONObject();
            try {
                body.put("TenMon", tenMonAn);

                // --- XỬ LÝ giá có dấu chấm
                String giaClean = gia.replace(".", "").replace(",", "");
                int dongia = Integer.parseInt(giaClean);
                body.put("dongia", dongia);

                body.put("DVT", dvt);
                body.put("hinhanh", hinhanh);
                body.put("sluong", quantity);
                body.put("makh", makh);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi tạo dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi lên API
            String url = "http://10.0.2.2:8888/api/cart/add";
            RequestQueue q = Volley.newRequestQueue(this);
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST, url, body,
                    resp -> {
                        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, Cart.class));
                    },
                    err -> Toast.makeText(this, "Thêm thất bại: " + err.getMessage(),
                            Toast.LENGTH_LONG).show()
            );
            q.add(req);
        });
    }
}


