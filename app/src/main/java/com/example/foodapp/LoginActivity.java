package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;
    private Button btnLogin;
    private TextView toSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Sửa thành layout đúng

        // Khởi tạo các view
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        toSignup = findViewById(R.id.ToSignup);

        // Xử lý sự kiện nút đăng nhập
        btnLogin.setOnClickListener(view -> {
            String email = edEmail.getText().toString().trim();
            String pass = edPassword.getText().toString().trim();
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Thêm logic xác thực đăng nhập ở đây (ví dụ: kiểm tra với cơ sở dữ liệu)
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            // Ví dụ: Chuyển đến MainActivity sau khi đăng nhập
            // startActivity(new Intent(this, MainActivity.class));
            // finish();
        });

        // Xử lý sự kiện chuyển đến màn hình đăng ký
        toSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}