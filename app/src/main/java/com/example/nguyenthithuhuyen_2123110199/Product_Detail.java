package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Product_Detail extends AppCompatActivity {

    private List<Product> relatedList = new ArrayList<>();
    private ProductAdapter adapter;
    private RecyclerView relatedProductsRecycler;
    private Product product;
    private String currentCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem chi tiết sản phẩm!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_product_detail);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int price = intent.getIntExtra("price", -1);
        int discount = intent.getIntExtra("discount", -1);
        String imageUrl = intent.getStringExtra("image");
        String description = intent.getStringExtra("description");
        int quantity = intent.getIntExtra("quantity", 0);
        int view = intent.getIntExtra("view", 0);
        currentCategory = intent.getStringExtra("category");

        // Khởi tạo product từ dữ liệu nhận được
        product = new Product(name, price, discount, imageUrl, currentCategory, description, view, quantity);

        // Ánh xạ View
        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productSellPrice = findViewById(R.id.product_sell_price);
        TextView productRentPrice = findViewById(R.id.product_rent_price);
        TextView productDescription = findViewById(R.id.product_description);
        TextView productQuantity = findViewById(R.id.product_quantity);
        TextView productView = findViewById(R.id.product_view);

        // Gán dữ liệu
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(productImage);
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        productName.setText(name);
        if (price != -1) productSellPrice.setText(formatter.format(price) + " đ");
        if (discount != -1) productRentPrice.setText(formatter.format(discount) + " đ");
        productDescription.setText(description);
        productQuantity.setText("Kho: " + quantity);
        productView.setText("Lượt xem: " + view);

        // Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // RecyclerView sản phẩm liên quan
        relatedProductsRecycler = findViewById(R.id.rv_related_products);
        relatedProductsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, relatedList);
        relatedProductsRecycler.setAdapter(adapter);

        // Nút Mua ngay
        Button btnBuyNow = findViewById(R.id.btn_buy_now);
        btnBuyNow.setOnClickListener(v -> {
            // Ghi sản phẩm vào SharedPreferences để hiển thị tại HomeActivity
            SharedPreferences cartPref = getSharedPreferences("cart", MODE_PRIVATE);
            SharedPreferences.Editor editor = cartPref.edit();

            ArrayList<Product> tempCart = new ArrayList<>();
            tempCart.add(product);

            Gson gson = new Gson();
            String jsonCart = gson.toJson(tempCart);
            editor.putString("cart_items", jsonCart);
            editor.apply();

            // Mở màn hình thanh toán
            Intent paymentIntent = new Intent(Product_Detail.this, HomeActivity.class);
            startActivity(paymentIntent);
        });

        // Gọi API lấy sản phẩm liên quan
        fetchRelatedProducts();

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity1.class));
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(this, CartActivity.class));
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

    private void fetchRelatedProducts() {
        String url = "https://6868e205d5933161d70cb9e2.mockapi.io/products";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    relatedList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String category = obj.getString("category");
                            if (!category.equals(currentCategory)) continue;

                            String productName = obj.getString("productName");
                            int price = obj.getInt("price");
                            int discount = obj.getInt("discount");
                            String image = obj.getString("image");
                            String description = obj.getString("description");
                            int view = obj.getInt("view");
                            int quantity = obj.getInt("quantity");

                            relatedList.add(new Product(
                                    productName, price, discount, image, category, description, view, quantity
                            ));
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Lỗi phân tích JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("API_ERROR", "Lỗi khi gọi API: " + error.getMessage())
        );

        queue.add(request);
    }
}
