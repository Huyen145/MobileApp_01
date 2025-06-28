package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_cart);

        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Đang chuyển đến trang thanh toán...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class)); // hoặc CheckoutActivity.class nếu bạn có trang riêng
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity1.class));
                } else if (itemId == R.id.nav_cart) {
                    return true;
                } else if (itemId == R.id.nav_account) {
                    startActivity(new Intent(this, AccountActivity.class));
                } else {
                    return false;
                }

                overridePendingTransition(0, 0);
                return true;
            });
        }
    }

}
