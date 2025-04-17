package com.example.hometouch2.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hometouch2.MainActivity;
import com.example.hometouch2.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin;

    private TextView tvSignUp;

    private CheckBox checkRemember;
    private SharedPreferences sharedPrefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupViews();
        setCheckRemember ();
        checkLoginStatus();

    }
    // Initialize UI components
    private void setupViews(){
        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        checkRemember = findViewById(R.id.checkRemember);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPrefs = getSharedPreferences("userData", MODE_PRIVATE);

        btnLogin.setOnClickListener(view -> {
            String inputUsername = etLoginUsername.getText().toString().trim();
            String inputPassword = etLoginPassword.getText().toString();

            String savedUsername = sharedPrefs.getString("username", "xz");
            String savedPassword = sharedPrefs.getString("password", "xz");

            if (inputUsername.equals(savedUsername) && inputPassword.equals(savedPassword)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
    private  void setCheckRemember () {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if (checkRemember.isChecked()) {
            editor.putBoolean("isRemembered", true);
        } else {
            editor.putBoolean("isRemembered", false);
        }
        editor.apply();
    }
    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean isRemembered = sharedPreferences.getBoolean("isRemembered", false);

        if (isLoggedIn) {

            startActivity(new Intent(this, MainActivity.class));
            finish();

        }

        if (isRemembered) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            etLoginUsername.setText(savedUsername);
            etLoginPassword.setText(savedPassword);

        }
    }
}