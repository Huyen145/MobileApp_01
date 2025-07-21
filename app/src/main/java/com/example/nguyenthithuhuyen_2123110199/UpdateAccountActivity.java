package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateAccountActivity extends AppCompatActivity {
    EditText fullnameInput, emailInput, phoneInput, addressInput, birthDateInput, passwordInput;
    Button updateButton;
    SharedPreferences prefs;

    String API_BASE_URL = "https://6868e205d5933161d70cb9e2.mockapi.io/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        // Ánh xạ View
        fullnameInput = findViewById(R.id.fullnameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        addressInput = findViewById(R.id.addressInput);
        birthDateInput = findViewById(R.id.birthDateInput);
        passwordInput = findViewById(R.id.passwordInput);
        updateButton = findViewById(R.id.updateButton);

        // Lấy thông tin từ SharedPreferences
        prefs = getSharedPreferences("user", MODE_PRIVATE);
        String userJson = prefs.getString("userInfo", "");
        String userId = prefs.getString("userId", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_LONG).show();
            return;
        }

        if (!userJson.isEmpty()) {
            try {
                JSONObject userObj = new JSONObject(userJson);
                fullnameInput.setText(userObj.optString("name"));
                emailInput.setText(userObj.optString("email"));
                phoneInput.setText(userObj.optString("phone"));
                addressInput.setText(userObj.optString("address"));
                birthDateInput.setText(userObj.optString("dob"));
            } catch (JSONException e) {
                Toast.makeText(this, "Lỗi khi đọc dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
            }
        }

        updateButton.setOnClickListener(v -> {
            try {
                // Lấy thông tin mới
                String name = fullnameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String address = addressInput.getText().toString().trim();
                String dob = birthDateInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Tạo JSON
                JSONObject updatedUser = new JSONObject();
                updatedUser.put("name", name);
                updatedUser.put("email", email);
                updatedUser.put("phone", phone);
                updatedUser.put("address", address);
                updatedUser.put("dob", dob);
                updatedUser.put("username", prefs.getString("username", ""));
                updatedUser.put("password", password.isEmpty()
                        ? prefs.getString("password", "") : password);

                String apiUrl = API_BASE_URL + userId;

                RequestQueue queue = Volley.newRequestQueue(this);

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.PUT,
                        apiUrl,
                        updatedUser,
                        response -> {
                            Toast.makeText(this, "✅ Cập nhật thành công! Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();

                            // Xoá dữ liệu SharedPreferences
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();

                            // Chuyển về màn hình đăng nhập
                            Intent intent = new Intent(UpdateAccountActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        },
                        error -> {
                            Log.e("API_ERROR", "❌ Lỗi API: " + error.toString());
                            Toast.makeText(this, "❌ Lỗi kết nối API!", Toast.LENGTH_SHORT).show();
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                queue.add(request);
            } catch (JSONException e) {
                Toast.makeText(this, "Lỗi khi tạo dữ liệu JSON!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
