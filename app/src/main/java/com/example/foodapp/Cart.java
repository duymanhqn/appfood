package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.apiImages.CartModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView tvTotal;
    private Button btnCheckout;

    private List<CartModel> list = new ArrayList<>();
    private CartAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        tvTotal      = findViewById(R.id.tvTotal);
        btnCheckout  = findViewById(R.id.btnCheckout);

        // Đọc cùng prefs
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("iduser", -1);
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        adapter = new CartAdapter(this, list, this::calculateTotal);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);

        fetchCart();

        btnCheckout.setOnClickListener(v ->
                Toast.makeText(this, "Đi đến thanh toán", Toast.LENGTH_SHORT).show()
        );
    }

    private void fetchCart() {
        String url = "http://10.0.2.2:8888/api/cart/" + userId;
        RequestQueue q = Volley.newRequestQueue(this);

        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject o = response.getJSONObject(i);
                            list.add(new CartModel(
                                    o.getInt("idGH"),
                                    o.getString("TenMon"),
                                    o.getInt("dongia"),
                                    o.getString("DVT"),
                                    o.getString("hinhanh"),
                                    o.getInt("sluong")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    calculateTotal();
                },
                error -> Toast.makeText(this, "Không tải được giỏ hàng", Toast.LENGTH_SHORT).show()
        );
        q.add(req);
    }

    private void calculateTotal() {
        int sum = 0;
        for (CartModel m : list) sum += m.getDongia() * m.getSoluong();
        tvTotal.setText("Tổng tiền: " + sum + "đ");
    }
}
