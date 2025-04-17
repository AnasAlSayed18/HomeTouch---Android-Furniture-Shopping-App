package com.example.hometouch2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hometouch2.Registration.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private ImageView profileImage;
    private TextView profileName, profileEmail, profileBalance;
    private Button logoutButton, addBalanceButton;
    private EditText addBalanceInput;
    private SharedPreferences sharedPreferences;
    private Uri selectedImageUri;
    private BottomNavigationView bottomNav;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        setUpViews();
        loadUserData();
        onLogout();
        onEditProfileImage();
        onAddBalance();
        onOptionsItemSelected();
    }

    private void setUpViews() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileBalance = findViewById(R.id.profileBalance);
        addBalanceInput = findViewById(R.id.addBalanceInput);
        addBalanceButton = findViewById(R.id.addBalanceButton);
        logoutButton = findViewById(R.id.logoutButton);
    }
    private void onAddBalance() {
        addBalanceButton.setOnClickListener(v -> {
            String input = addBalanceInput.getText().toString().trim();
            if (!input.isEmpty()) {
                float current = sharedPreferences.getFloat("balance", 0f);
                float added = Float.parseFloat(input);

                if (added < 0) {
                    Toast.makeText(this, "Cannot add negative balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (added > 1000) {
                    Toast.makeText(this, "Cannot add more than $1000 at once", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (added == 0) {
                    Toast.makeText(this, "Cannot add $0", Toast.LENGTH_SHORT).show();
                    return;
                }
                float updated = current + added;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("balance", updated);
                editor.apply();

                profileBalance.setText("Balance: $" + updated);
                Toast.makeText(this, "Balance updated!", Toast.LENGTH_SHORT).show();
                addBalanceInput.setText("");
            } else {
                Toast.makeText(this, "Enter a value to add", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onEditProfileImage() {
        profileImage.setOnClickListener(v -> openGallery()
        );
    }
    private void onLogout() {
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadUserData() {

        String name = sharedPreferences.getString("username", "Ahmad");
        String email = sharedPreferences.getString("email", "Ahmad14@gmail.com");
        float balance = sharedPreferences.getFloat("balance", 0f);


        profileName.setText(name);
        profileEmail.setText(email);
        profileBalance.setText("Balance: $" + balance);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                profileImage.setImageURI(selectedImageUri); // Display selected image

                // Save the URI string to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profileImageUri", selectedImageUri.toString());
                editor.apply();
            }else{
                selectedImageUri = data.getData();
            }
        }
    }




    public void onOptionsItemSelected() {
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                finish();
                return true;

            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });


    }
}
