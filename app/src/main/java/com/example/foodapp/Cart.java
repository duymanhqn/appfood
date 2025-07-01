// Cart.java
package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.apiImages.CartModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView tvTotal;
    private Button btnPay;

    private List<CartModel> list = new ArrayList<>();
    private CartAdapter adapter;
    private int userId;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnPay = findViewById(R.id.btnPay);

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

        btnPay.setOnClickListener(v -> showPayDialog());
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
        totalPrice = 0;
        for (CartModel m : list)
            totalPrice += m.getDongia() * m.getSoluong();
        tvTotal.setText("Tổng tiền: " + totalPrice + "đ");
    }

    private void showPayDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_pay, null);
        dialog.setContentView(view);

        TextView txtTotalAmount = view.findViewById(R.id.txtTotalAmount);
        Button btnConfirmPay = view.findViewById(R.id.btnConfirmPay);

        txtTotalAmount.setText("Tổng tiền: " + totalPrice + "đ");

        btnConfirmPay.setOnClickListener(v -> {
            String paymentMethod = "COD";
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            JSONObject donHangObj = new JSONObject();
            try {
                donHangObj.put("makh", userId);
                donHangObj.put("tongtien", totalPrice);
                donHangObj.put("pttt", paymentMethod);
                donHangObj.put("thoigian", currentTime);

                JSONArray itemsArray = new JSONArray();
                for (CartModel m : list) {
                    JSONObject item = new JSONObject();
                    item.put("TenMon", m.getTenMon());
                    item.put("dongia", m.getDongia());
                    item.put("DVT", m.getDVT());
                    item.put("hinhanh", m.getHinhanh());
                    item.put("sluong", m.getSoluong());
                    itemsArray.put(item);
                }

                donHangObj.put("items", itemsArray); // KEY PHẢI LÀ items

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi dữ liệu JSON", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://10.0.2.2:8888/api/cart/PayCart";
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, donHangObj,
                    resp -> {
                        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        fetchCart(); // reload cart
                    },
                    err -> {
                        Toast.makeText(this, "Lỗi đặt hàng", Toast.LENGTH_SHORT).show();
                        err.printStackTrace();
                    });
            queue.add(req);
        });

        dialog.show();
    }
}
