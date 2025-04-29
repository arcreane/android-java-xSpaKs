package com.xspaks.filmscan.database;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xspaks.filmscan.model.GameObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {
    public static List<String> loadGameObjectsFromJson(Context context) {
        try {
            InputStream is = context.getAssets().open("game_objects.json");
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            return new Gson().fromJson(reader, new TypeToken<List<String>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
