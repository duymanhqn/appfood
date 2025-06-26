package com.example.foodapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Detailfood extends AppCompatActivity {

    ImageView imageView;
    TextView tvTenmonna, tvGiaAndDvt, tvMota, tvSoluong, tvQuantity;
    Button btnDecrease, btnIncrease, button;

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailfood);

        imageView = findViewById(R.id.image);
        tvTenmonna = findViewById(R.id.tvTenmonna);
        tvGiaAndDvt = findViewById(R.id.tvGiaAndDvt);
        tvMota = findViewById(R.id.tvMota);
        tvSoluong = findViewById(R.id.tvsoluong);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        button = findViewById(R.id.button);

        String tenMonAn = getIntent().getStringExtra("tenmAn");
        String mota = getIntent().getStringExtra("mota");
        String dvt = getIntent().getStringExtra("dvt");
        String gia = getIntent().getStringExtra("gia");
        String hinhanh = getIntent().getStringExtra("hinhanh");

        tvTenmonna.setText(tenMonAn);
        tvGiaAndDvt.setText(gia + " " + dvt);
        tvMota.setText(mota);
        tvQuantity.setText(String.valueOf(quantity));
        tvSoluong.setText("Số lượng:");

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
            // Xử lý thêm vào giỏ hàng ở đây
        });
    }
}
