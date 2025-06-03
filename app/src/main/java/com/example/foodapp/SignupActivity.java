//package com.example.foodapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SignupActivity extends AppCompatActivity {
//    private EditText edESignup, edPSignup, edRePass, phone, diachi;
//    private Button btnSignup;
//
//    private TextView toLogin;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        edESignup = findViewById(R.id.edESignup);
//        edPSignup = findViewById(R.id.edPSignup);
//        edRePass = findViewById(R.id.edRePass);
//        phone = findViewById(R.id.phone);
//        diachi = findViewById(R.id.diachi);
//        btnSignup = findViewById(R.id.btnSignup);
//
//        toLogin = findViewById(R.id.ToLogin);
//
//        btnSignup.setOnClickListener(v -> {
//            String email = edESignup.getText().toString().trim();
//            String password = edPSignup.getText().toString().trim();
//            String rePassword = edRePass.getText().toString().trim();
//            String phoneNumber = phone.getText().toString().trim();
//            String address = diachi.getText().toString().trim();
//
//            if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty() ||
//                    phoneNumber.isEmpty() || address.isEmpty()) {
//                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!password.equals(rePassword)) {
//                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            RequestQueue queue = Volley.newRequestQueue(this);
//            String url = "https://10.0.2.2:8888/api/user"; // Sử dụng cho emulator
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                    response -> {
//                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
//                        edESignup.setText("");
//                        edPSignup.setText("");
//                        edRePass.setText("");
//                        phone.setText("");
//                        diachi.setText("");
//                    },
//                    error -> Toast.makeText(this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("email", email);
//                    params.put("password", password);
//                    params.put("phone", phoneNumber); // Ánh xạ với sdt trong MySQL
//                    params.put("address", address);   // Ánh xạ với diachi trong MySQL
//                    return params;
//                }
//            };
//
//            queue.add(stringRequest);
//        });
//
//        toLogin.setOnClickListener(v -> {
//            Toast.makeText(this, "Chuyển đến Đăng nhập", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, LoginActivity.class));
//        });
//    }
//}

package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText edESignup, edPSignup, edRePass, phone, diachi;
    private Button btnSignup;
    private TextView toLogin;

    private static final String API_URL = "http://10.0.2.2:8888/api/Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edESignup = findViewById(R.id.edESignup);   // email
        edPSignup = findViewById(R.id.edPSignup);   // password
        edRePass = findViewById(R.id.edRePass);     // re-enter password
        phone = findViewById(R.id.phone);           // SDT
        diachi = findViewById(R.id.diachi);         // địa chỉ

        btnSignup = findViewById(R.id.btnSignup);   // button đăng ký
        toLogin = findViewById(R.id.ToLogin);       // chuyển đến login

        btnSignup.setOnClickListener(v -> {
            String email = edESignup.getText().toString().trim();
            String password = edPSignup.getText().toString().trim();
            String rePassword = edRePass.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();
            String address = diachi.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty() ||
                    phoneNumber.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                    response -> {
                        if (response.contains("Đăng ký thành công")) {
                            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                            // Chuyển sang trang Login sau khi đăng ký thành công
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Đóng SignUpActivity
                        } else {
                            Toast.makeText(this, "Phản hồi không hợp lệ từ server: " + response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                            Toast.makeText(this, "Email đã tồn tại, vui lòng chọn email khác", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Lỗi: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone", phoneNumber);
                    params.put("address", address);
                    return params;
                }
            };

            queue.add(stringRequest);
        });

        toLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}
