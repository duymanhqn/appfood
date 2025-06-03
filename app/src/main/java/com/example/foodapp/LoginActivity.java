
package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;
    private Button btnLogin;
    private TextView toSignup;
    RequestQueue requestQueue;

    private static final String TAG = "LoginActivity";
    private static final String API_URL = "http://10.0.2.2:8888/api/PostLoginuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        toSignup = findViewById(R.id.ToSignup);
        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(view -> login());
        toSignup.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến Đăng ký", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

    }

    private void login() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject loginData = new JSONObject();
            loginData.put("email", email);
            loginData.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    loginData,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                String userEmail = response.getJSONObject("user").getString("email");
                                Toast.makeText(this, "Đăng nhập thành công: " + userEmail, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON error: " + e.getMessage());
                        }
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorBody = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorBody);
                                String message = errorJson.optString("message");
                                Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error response: " + e.getMessage());
                                Toast.makeText(this, "Lỗi server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Lỗi kết nối server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e(TAG, "Lỗi tạo JSON: " + e.getMessage());
        }

    }

}
