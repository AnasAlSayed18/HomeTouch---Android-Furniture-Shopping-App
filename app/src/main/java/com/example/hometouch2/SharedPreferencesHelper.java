package com.example.hometouch2;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hometouch2.FurnitureItem.FurnitureItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "FurniturePrefs";
    private static final String KEY_FURNITURE_LIST = "furniture_list";

    // Save furniture items list to SharedPreferences
    public static void saveFurnitureList(Context context, List<FurnitureItem> furnitureList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(furnitureList);
        editor.putString(KEY_FURNITURE_LIST, json);
        editor.apply();
    }

    // Get furniture items list from SharedPreferences
    public static List<FurnitureItem> getFurnitureList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_FURNITURE_LIST, null);

        Gson gson = new Gson();
        Type type = new TypeToken<List<FurnitureItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
