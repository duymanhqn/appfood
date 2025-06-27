// ProfileActivity.java - Full Sửa đã fix backend và API
package com.example.foodapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword, edtPhone, edtAddress;
    Button btnEdit, btnSave;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("iduser", -1);

        setEditable(false);
        btnSave.setVisibility(View.GONE);

        if (userId != -1) {
            loadUserInfo(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_LONG).show();
        }

        btnEdit.setOnClickListener(v -> {
            setEditable(true);
            btnSave.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        });

        btnSave.setOnClickListener(v -> {
            updateUserInfo();
            setEditable(false);
            btnSave.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        });
    }

    private void setEditable(boolean editable) {
        edtEmail.setEnabled(false);
        edtPassword.setEnabled(editable);
        edtPhone.setEnabled(editable);
        edtAddress.setEnabled(editable);
    }

    private void loadUserInfo(int id) {
        String url = "http://10.0.2.2:8888/api/user/" + id;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    edtEmail.setText(response.optString("email", ""));
                    edtPassword.setText(response.optString("password", ""));
                    edtPhone.setText(response.optString("sdt", ""));
                    edtAddress.setText(response.optString("diachi", ""));
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_LONG).show();
                }
        );
        queue.add(request);
    }

    private void updateUserInfo() {
        if (userId == -1) {
            Toast.makeText(this, "Không có ID người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String sdt = edtPhone.getText().toString().trim();
        String diachi = edtAddress.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8888/api/user/update"; // Đúng router backend

        JSONObject body = new JSONObject();
        try {
            body.put("iduser", userId);
            body.put("password", password);
            body.put("sdt", sdt);
            body.put("diachi", diachi);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    boolean success = response.optBoolean("success", false);
                    String message = response.optString("message", "Không rõ kết quả");
                    if (success) {
                        Toast.makeText(this, "\u2705 " + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "\u274C " + message, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "\u26A0\uFE0F Lỗi kết nối", Toast.LENGTH_LONG).show();
                }
        );
        queue.add(request);
    }
}
