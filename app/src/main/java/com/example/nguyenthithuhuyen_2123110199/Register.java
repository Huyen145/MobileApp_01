package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    EditText phoneInput, fullnameInput, emailInput, passwordInput, rePasswordInput;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Ánh xạ view
        phoneInput = findViewById(R.id.phoneInput);
        fullnameInput = findViewById(R.id.fullnameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rePasswordInput = findViewById(R.id.rePasswordInput);
        registerButton = findViewById(R.id.registerButton);

        // Bắt sự kiện nhấn nút Đăng ký
        registerButton.setOnClickListener(v -> {
            String phone = phoneInput.getText().toString().trim();
            String name = fullnameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = rePasswordInput.getText().toString().trim();

            if (!phone.matches("\\d{10}")) {
                phoneInput.setError("Số điện thoại phải gồm đúng 10 chữ số");
                phoneInput.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(name)) {
                fullnameInput.setError("Vui lòng nhập họ tên");
                fullnameInput.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Email không hợp lệ");
                emailInput.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordInput.setError("Mật khẩu phải tối thiểu 6 ký tự");
                passwordInput.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                rePasswordInput.setError("Mật khẩu nhập lại không khớp");
                rePasswordInput.requestFocus();
                return;
            }

            Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(Register.this, MainActivity.class));
             finish();
        });
    }
}
