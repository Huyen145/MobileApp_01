package com.example.nguyenthithuhuyen_2123110199;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail, etNewPassword;
    private Button btnResetPassword;
    private final String API_URL = "https://6868e205d5933161d70cb9e2.mockapi.io/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();

            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tìm người dùng
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    API_URL,
                    null,
                    response -> {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject user = response.getJSONObject(i);
                                if (user.getString("email").equalsIgnoreCase(email)) {
                                    String userId = user.getString("id");
                                    updatePassword(userId, newPassword);
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(this, "Không tìm thấy người dùng với email này!", Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);
        });
    }

    private void updatePassword(String userId, String newPassword) {
        String updateUrl = API_URL + "/" + userId;

        JSONObject newPasswordObj = new JSONObject();
        try {
            newPasswordObj.put("password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                updateUrl,
                response -> Toast.makeText(this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public byte[] getBody() {
                return newPasswordObj.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
