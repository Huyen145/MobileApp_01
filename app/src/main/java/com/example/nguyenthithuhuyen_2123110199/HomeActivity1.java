package com.example.nguyenthithuhuyen_2123110199;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity1 extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;
    List<Product> filteredList;

    RequestQueue requestQueue;

    private final String PRODUCT_API_URL = "https://6868e205d5933161d70cb9e2.mockapi.io/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        setupCategoryClickListeners();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        fetchProductsFromApi();

        setupBottomNavigation();
        setupSearchFeature(); // 🆕 Tìm kiếm theo EditText + ImageView
    }

    private void setupCategoryClickListeners() {
        LinearLayout phoneCategory = findViewById(R.id.categoryPhone);
        LinearLayout watchCategory = findViewById(R.id.categoryWatch);
        LinearLayout headphoneCategory = findViewById(R.id.categoryHeadphone);
        LinearLayout laptopCategory = findViewById(R.id.categoryLaptop);

        phoneCategory.setOnClickListener(v -> openCategory("Điện thoại"));
        watchCategory.setOnClickListener(v -> openCategory("Phụ kiện"));
        headphoneCategory.setOnClickListener(v -> openCategory("Tai nghe"));
        laptopCategory.setOnClickListener(v -> openCategory("Laptop"));
    }

    private void openCategory(String category) {
        Intent intent = new Intent(HomeActivity1.this, ProductByCategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(HomeActivity1.this, CartActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(HomeActivity1.this, AccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void setupSearchFeature() {
        EditText edtSearch = findViewById(R.id.searchView);  // EditText dùng để nhập từ khóa
        ImageView imgSearch = findViewById(R.id.imgSearch);  // ImageView để nhấn tìm

        imgSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString().trim();
            filterProducts(keyword);
        });
    }

    private void filterProducts(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Product product : productList) {
                if (product.getProductName().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchProductsFromApi() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                PRODUCT_API_URL,
                null,
                response -> {
                    productList.clear();
                    filteredList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Product product = Product.fromJson(obj);
                            productList.add(product);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    filteredList.addAll(productList); // ban đầu hiển thị tất cả
                    adapter.notifyDataSetChanged();
                },
                error -> Log.e("FetchProducts", "Lỗi khi gọi API: " + error.toString())
        );

        requestQueue.add(request);
    }
}
