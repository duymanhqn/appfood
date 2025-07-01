package com.example.foodapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView historyListView;
    ArrayList<String> orderList;
    HistoryAdapter adapter; // custom adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);
        orderList = new ArrayList<>();
        adapter = new HistoryAdapter(this, orderList);
        historyListView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("iduser", -1);

        if (userId == -1) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        loadOrderHistory(userId);
    }

    private void loadOrderHistory(int makh) {
        String url = "http://10.0.2.2:8888/api/history/" + makh;


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray orders = response.getJSONArray("orders");
                        orderList.clear();

                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject order = orders.getJSONObject(i);

                            int idDH = order.getInt("idDH");
                            String thoigian = order.getString("thoigiandathang");
                            int tongtien = order.getInt("tongtien");

                            JSONArray chitiet = order.getJSONArray("chitiet");
                            StringBuilder items = new StringBuilder();

                            for (int j = 0; j < chitiet.length(); j++) {
                                JSONObject mon = chitiet.getJSONObject(j);
                                String ten = mon.getString("tenmAn");
                                int sl = mon.getInt("sluong");
                                items.append("- ").append(ten).append(" x").append(sl).append("\n");
                            }

                            String orderText = "🧾 Đơn #" + idDH +
                                    "\n🕒 " + thoigian +
                                    "\n💰 Tổng tiền: " + tongtien + " VND\n" +
                                    "🍽 Chi tiết:\n" + items;
                            orderList.add(orderText);
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Lỗi server: " + error.getMessage(), Toast.LENGTH_LONG).show());

        queue.add(req);
    }
}