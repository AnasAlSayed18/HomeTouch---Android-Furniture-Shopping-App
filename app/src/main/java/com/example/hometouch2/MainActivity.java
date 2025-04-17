package com.example.hometouch2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hometouch2.FurnitureItem.FurnitureItem;
import com.example.hometouch2.FurnitureItem.FurnitureAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declare the UI components
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNav;
    private SearchView searchView;
    private FurnitureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setEdgeToEdgePadding();
        setupViews();
        loadItemsFromSharedPreferences();
    }
    @Override
    protected void onResume() {
        super.onResume();
        List<FurnitureItem> updatedFurnitureData = SharedPreferencesHelper.getFurnitureList(this);
        adapter.updateList(updatedFurnitureData); // You already have this method in your adapter
    }
    private void setEdgeToEdgePadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupViews() {
        // Initialize UI components
        recyclerView = findViewById(R.id.furnitureRecyclerView);
        searchView = findViewById(R.id.searchView);

        saveFirstTimeItemToSharedPreferences();
        setupRecyclerView();
        setupSearchView();
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        List<FurnitureItem> savedFurnitureData = SharedPreferencesHelper.getFurnitureList(this);


        // Set up the FurnitureAdapter
        adapter = new FurnitureAdapter(this, savedFurnitureData);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for home transition
                finish(); // Close current activity
                return true;

            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for search transition
                finish(); // Close current activity
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for cart transition
                finish();
                return true;

            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Animation for profile transition
                return true;
            }

            return false;
        });
    }




    // Filter the furniture list based on search query
    private void filterList(String query) {
        List<FurnitureItem> originalList = SharedPreferencesHelper.getFurnitureList(this); // get full list
        List<FurnitureItem> filteredList = new ArrayList<>();

        for (FurnitureItem item : originalList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.updateList(filteredList);
    }


    // Save the first furniture items into SharedPreferences if it's the first time
    public void saveFirstTimeItemToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("FurniturePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            List<FurnitureItem> tempList = new ArrayList<>();

            tempList.add(new FurnitureItem("Deluxe Microfiber Sofa", "$499", R.drawable.sofa1, 3, "Sofa", "Available"));
            tempList.add(new FurnitureItem("Austin Modern Sectional", "$699", R.drawable.sofa2, 1, "Sofa", "Available"));
            tempList.add(new FurnitureItem("Green Living Room Sofa", "$899", R.drawable.sofa3, 4, "Sofa", "Available"));
            tempList.add(new FurnitureItem("Bella Designer Couch", "$749", R.drawable.sofa4, 2, "Sofa", "Available"));
            tempList.add(new FurnitureItem("Braden Luxury Recliner", "$620", R.drawable.sofa5, 0, "Sofa", "Sold Out"));
            tempList.add(new FurnitureItem("Braden Double Sofa", "$530", R.drawable.sofa6, 6, "Sofa", "Available"));
            tempList.add(new FurnitureItem("Sraya Organic Sofa", "$850", R.drawable.sofa7, 5, "Sofa", "Available"));
            tempList.add(new FurnitureItem("Raya Detailed Couch", "$680", R.drawable.sofa8, 2, "Sofa", "Available"));

            tempList.add(new FurnitureItem("Margo Brown Armchair", "$129", R.drawable.chair1, 10, "Chair", "Available"));
            tempList.add(new FurnitureItem("Dr. Lina Leather Chair", "$149", R.drawable.chair2, 8, "Chair", "Available"));
            tempList.add(new FurnitureItem("Classic Oak Chair", "$99", R.drawable.chair3, 5, "Chair", "Available"));
            tempList.add(new FurnitureItem("Skyline Modern Chair", "$110", R.drawable.chair4, 4, "Chair", "Available"));
            tempList.add(new FurnitureItem("Comfy Mesh Chair", "$105", R.drawable.chair5, 0, "Chair", "Sold Out"));
            tempList.add(new FurnitureItem("Elegant Swivel Chair", "$135", R.drawable.chair6, 3, "Chair", "Available"));
            tempList.add(new FurnitureItem("Beige Fabric Chair", "$92", R.drawable.chair7, 6, "Chair", "Available"));
            tempList.add(new FurnitureItem("Olive Lounge Chair", "$115", R.drawable.chair8, 2, "Chair", "Available"));

            tempList.add(new FurnitureItem("Round Dining Table", "$299", R.drawable.table1, 2, "Table", "Available"));
            tempList.add(new FurnitureItem("Glass Top Coffee Table", "$180", R.drawable.table2, 3, "Table", "Available"));
            tempList.add(new FurnitureItem("Rustic Wooden Table", "$350", R.drawable.table3, 1, "Table", "Available"));
            tempList.add(new FurnitureItem("Elegant Marble Table", "$420", R.drawable.table4, 0, "Table", "Sold Out"));
            tempList.add(new FurnitureItem("Folding Patio Table", "$159", R.drawable.table5, 5, "Table", "Available"));
            tempList.add(new FurnitureItem("Rectangular Study Table", "$240", R.drawable.table6, 3, "Table", "Available"));
            tempList.add(new FurnitureItem("Farmhouse Dining Table", "$460", R.drawable.table7, 4, "Table", "Available"));
            tempList.add(new FurnitureItem("Compact Work Table", "$210", R.drawable.table8, 6, "Table", "Available"));

            tempList.add(new FurnitureItem("Modern King Bed", "$799", R.drawable.bed1, 4, "Bed", "Available"));
            tempList.add(new FurnitureItem("Classic Twin Bed", "$379", R.drawable.bed2, 0, "Bed", "Sold Out"));
            tempList.add(new FurnitureItem("Luxury Platform Bed", "$720", R.drawable.bed4, 1, "Bed", "Available"));
            tempList.add(new FurnitureItem("Wooden Bunk Bed", "$520", R.drawable.bed5, 3, "Bed", "Available"));

            tempList.add(new FurnitureItem("Ceiling Pendant Light", "$42", R.drawable.light1, 5, "Light", "Available"));
            tempList.add(new FurnitureItem("Vintage Desk Lamp", "$58", R.drawable.light2, 4, "Light", "Available"));
            tempList.add(new FurnitureItem("LED Floor Lamp", "$65", R.drawable.light3, 2, "Light", "Available"));
            tempList.add(new FurnitureItem("Wall Mounted Lamp", "$49", R.drawable.light4, 3, "Light", "Available"));

            tempList.add(new FurnitureItem("Beige Area Rug", "$85", R.drawable.rug1, 3, "Area Rug", "Available"));
            tempList.add(new FurnitureItem("Patterned Living Rug", "$95", R.drawable.rug2, 4, "Area Rug", "Available"));
            tempList.add(new FurnitureItem("Grey Modern Rug", "$120", R.drawable.rug3, 1, "Area Rug", "Available"));

            tempList.add(new FurnitureItem("Oak Study Desk", "$215", R.drawable.desk1, 2, "Desk", "Available"));
            tempList.add(new FurnitureItem("Compact Corner Desk", "$195", R.drawable.desk2, 5, "Desk", "Available"));

            Collections.shuffle(tempList);

            List<FurnitureItem> furnitureItems = new ArrayList<>(tempList);

            Gson gson = new Gson();
            String json = gson.toJson(furnitureItems);
            editor.putString("furniture_list", json);
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        }
    }


    // Load furniture items from SharedPreferences
    public void loadItemsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("FurniturePrefs", MODE_PRIVATE);

        String json = sharedPreferences.getString("furniture_list", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FurnitureItem>>() {}.getType();
            List<FurnitureItem> furnitureItems = gson.fromJson(json, type);

            adapter.updateList(furnitureItems);
        }
    }




}
