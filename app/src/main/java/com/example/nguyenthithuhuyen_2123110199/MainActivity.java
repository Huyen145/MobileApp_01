package com.example.nguyenthithuhuyen_2123110199;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private RequestQueue mRequestQueue;

    private final String LOGIN_URL = "https://6868e205d5933161d70cb9e2.mockapi.io/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        // Mở màn hình đăng ký
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Register.class)));

        // Xử lý nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                doLogin(username, password);
            }
        });
    }

    private void doLogin(String username, String password) {
        mRequestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                LOGIN_URL,
                null,
                response -> {
                    boolean found = false;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject user = response.getJSONObject(i);
                            String name = user.getString("name");
                            String pass = user.getString("password");

                            if (username.equals(name) && password.equals(pass)) {
                                found = true;
                                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                // Lưu thông tin nếu cần
                                getSharedPreferences("user", MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("isLoggedIn", true)
                                        .putString("username", name)
                                        .putString("userId", user.getString("id"))
                                        .apply();
                                startActivity(new Intent(this, HomeActivity1.class));
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Lỗi phân tích dữ liệu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (!found) {
                        Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("MockLogin", "Lỗi: " + error.toString());
                    Toast.makeText(this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
                }
        );

        mRequestQueue.add(jsonRequest);
    }
}
