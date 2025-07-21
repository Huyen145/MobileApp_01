package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {

    TextView txtName, txtEmail, txtPhone, txtAddress, txtGender, txtDob, txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.editProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, UpdateAccountActivity.class));
        });
        // Ánh xạ TextView
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtGender = findViewById(R.id.txtGender);
        txtDob = findViewById(R.id.txtDob);
        txtUsername = findViewById(R.id.txtUsername);

        // Lấy thông tin đăng nhập từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        String userJson = prefs.getString("userInfo", "");
        if (!userJson.isEmpty()) {
            try {
                JSONObject userObj = new JSONObject(userJson);
                txtName.setText("Họ tên: " + userObj.optString("name"));
                txtEmail.setText("Email: " + userObj.optString("email"));
                txtPhone.setText("SĐT: " + userObj.optString("phone"));
                txtAddress.setText("Địa chỉ: " + userObj.optString("address"));
                txtGender.setText("Giới tính: " + userObj.optString("gender"));
                txtDob.setText("Ngày sinh: " + userObj.optString("dob"));
                txtUsername.setText("Tài khoản: " + userObj.optString("username"));
            } catch (JSONException e) {
                Toast.makeText(this, "Lỗi dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        }

        // Xử lý đăng xuất
        findViewById(R.id.logoutSection).setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Điều hướng bằng BottomNavigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_account);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity1.class));
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(this, CartActivity.class));
                } else if (itemId == R.id.nav_account) {
                    return true;
                }
                overridePendingTransition(0, 0);
                return true;
            });
        }
    }
}
