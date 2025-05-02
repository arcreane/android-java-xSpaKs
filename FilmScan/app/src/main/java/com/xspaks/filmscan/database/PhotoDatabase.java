package com.xspaks.filmscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xspaks.filmscan.model.GameObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoDatabase {
    private static final String DB_PATH = "/data/data/com.xspaks.filmscan/databases/";
    private static final String DB_NAME = "GamePhotoValidatorDB";
    private static final String TABLE_NAME = "game_objects";

    private SQLiteDatabase database;

    public PhotoDatabase() {
        try {
            String dbFullPath = DB_PATH + DB_NAME;
            database = SQLiteDatabase.openOrCreateDatabase(dbFullPath, null);
            createTableIfNotExists();
        } catch (SQLException e) {
            Log.e("PhotoDatabase", "Erreur ouverture DB : " + e.getMessage());
        }
    }

    private void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "validated INTEGER DEFAULT 0)";
        database.execSQL(createTableQuery);
    }

    public void insertGameObject(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("validated", 0);
        database.insert(TABLE_NAME, null, values);
    }

    public List<GameObject> getAllGameObjects() {
        List<GameObject> gameObjects = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int validated = cursor.getInt(2);
            gameObjects.add(new GameObject(id, name, validated));
        }
        cursor.close();
        return gameObjects;
    }

    public boolean areAllObjectsValidated() {
        List<GameObject> existingGameObjects = getAllGameObjects();
        boolean allValidated = true;

        for (GameObject gameObject : existingGameObjects) {
            if (!gameObject.isValidated()) {
                allValidated = false;
                break;
            }
        }

        return allValidated;
    }

    public int existingObjectsLength() {
        return getAllGameObjects().size();
    }

    public void updateGameObjectStatus(int id, boolean isValidated) {
        ContentValues values = new ContentValues();
        values.put("validated", isValidated ? 1 : 0);
        database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void clearGameObjects() {
        database.execSQL("DELETE FROM game_objects");
    }

    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
