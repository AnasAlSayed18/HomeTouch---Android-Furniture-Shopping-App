package com.example.hometouch2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hometouch2.FurnitureItem.FurnitureItem;
import com.example.hometouch2.FurnitureItem.FurnitureAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;

    private Spinner typeSpinner, statusSpinner;
    private RecyclerView furnitureRecyclerView;

    private List<FurnitureItem> furnitureList = new ArrayList<>();
    private FurnitureAdapter furnitureAdapter;

    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
        onOptionsItemSelected();
        loadFurnitureItemsFromSharedPreferences();

    }
    @Override
    protected void onResume() {
        super.onResume();
        List<FurnitureItem> updatedFurnitureData = SharedPreferencesHelper.getFurnitureList(this);
        furnitureAdapter.updateList(updatedFurnitureData);
    }

    private void setupViews() {
        searchView = findViewById(R.id.searchView);
        typeSpinner = findViewById(R.id.typeSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        furnitureRecyclerView = findViewById(R.id.furnitureRecyclerView);


        setupRecyclerView();
        setupSpinner();
    }

    private void setupRecyclerView() {
        List<FurnitureItem> savedFurnitureData = SharedPreferencesHelper.getFurnitureList(this);


        // Set up the FurnitureAdapter
        furnitureAdapter = new FurnitureAdapter(this, savedFurnitureData);
        furnitureRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns grid
        furnitureRecyclerView.setAdapter(furnitureAdapter);
    }

    private void setupSpinner() {
        // Set up type spinner (filter by type)
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_filter_array, android.R.layout.simple_spinner_item);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // Set up status spinner (filter by status)
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_filter_array, android.R.layout.simple_spinner_item);

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Set up search query listener to filter by name
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query, typeSpinner.getSelectedItem().toString(), statusSpinner.getSelectedItem().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, typeSpinner.getSelectedItem().toString(), statusSpinner.getSelectedItem().toString());
                return false;
            }
        });

        // Set up filter listeners for type and status spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterList(searchView.getQuery().toString(), typeSpinner.getSelectedItem().toString(), statusSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterList(searchView.getQuery().toString(), "All", statusSpinner.getSelectedItem().toString());
            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterList(searchView.getQuery().toString(), typeSpinner.getSelectedItem().toString(), statusSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterList(searchView.getQuery().toString(), typeSpinner.getSelectedItem().toString(), "All");
            }
        });
    }

    private void filterList(String query, String type, String status) {
        List<FurnitureItem> filteredList = new ArrayList<>();

        for (FurnitureItem item : furnitureList) {
            if ((type.equals("All") || item.getType().equalsIgnoreCase(type)) &&
                    (status.equals("All") || item.getStatus().equalsIgnoreCase(status)) &&
                    (item.getName().toLowerCase().contains(query.toLowerCase()))) {
                filteredList.add(item);
            }
        }

        // Update the RecyclerView with the filtered list
        furnitureAdapter.updateList(filteredList);
    }

    private void loadFurnitureItemsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("FurniturePrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("furniture_list", null);

        if (json != null) {
            // If data exists in SharedPreferences, deserialize it into a list
            Gson gson = new Gson();
            Type type = new TypeToken<List<FurnitureItem>>() {}.getType();
            furnitureList = gson.fromJson(json, type);

            // Set up the RecyclerView with the loaded furniture items
            furnitureAdapter.updateList(furnitureList);
        } else {
            // If no data exists in SharedPreferences, show a message
            Toast.makeText(this, "No furniture items available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onOptionsItemSelected() {
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_search);

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
                finish();
                return true;
            }

            return false;
        });
    }
}
