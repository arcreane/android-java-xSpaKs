package com.xspaks.filmscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xspaks.filmscan.enums.GameDifficulty;
import com.xspaks.filmscan.model.GameObject;
import com.xspaks.filmscan.model.Score;

import java.util.ArrayList;
import java.util.List;

public class PhotoDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "GamePhotoValidatorDB";
    public static final String GAME_OBJECTS_TABLE = "game_objects";
    public static final String SCORE_TABLE = "scores";

    public PhotoDatabase(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + GAME_OBJECTS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "validated INTEGER DEFAULT 0, " +
                "created_at LONG NOT NULL, " +
                "validated_at LONG NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SCORE_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "points INTEGER NOT NULL," +
                "difficulty TEXT NOT NULL, " +
                "created_at LONG NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GAME_OBJECTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);
        onCreate(db);
    }

    public void insertGameObject(String name, long current_timestamp) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("validated", 0);
        values.put("created_at", current_timestamp);
        values.put("updated_at", current_timestamp);
        database.insert(GAME_OBJECTS_TABLE, null, values);
    }

    public List<GameObject> getAllGameObjects() {
        SQLiteDatabase database = getReadableDatabase();
        List<GameObject> gameObjects = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + GAME_OBJECTS_TABLE, null);
        while (cursor.moveToNext()) {
            int idCol = cursor.getColumnIndex("id");
            int id = cursor.getInt(idCol);

            int nameCol = cursor.getColumnIndex("name");
            String name = cursor.getString(nameCol);

            int validatedCol = cursor.getColumnIndex("validated");
            int validated = cursor.getInt(validatedCol);

            int createdAtCol = cursor.getColumnIndex("created_at");
            long createdAt = cursor.getInt(createdAtCol);

            int validatedAtCol = cursor.getColumnIndex("validated_at");
            long validatedAt = cursor.getInt(validatedAtCol);

            gameObjects.add(new GameObject(id, name, validated, createdAt, validatedAt));
        }
        cursor.close();
        return gameObjects;
    }

    public long getGameStartDate() {
        List<GameObject> existingGameObjects = getAllGameObjects();
        return existingGameObjects.get(0).getCreatedAt();
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

    public void updateGameObjectStatus(int id, boolean isValidated, long current_timestamp) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("validated", isValidated ? 1 : 0);
        values.put("validated_at", current_timestamp);
        database.update(GAME_OBJECTS_TABLE, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void clearGameObjects() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + GAME_OBJECTS_TABLE);
    }

    public List<Score> getAllScores() {
        SQLiteDatabase database = getReadableDatabase();
        List<Score> scores = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + SCORE_TABLE, null);
        while (cursor.moveToNext()) {
            int idCol = cursor.getColumnIndex("id");
            int id = cursor.getInt(idCol);

            int usernameCol = cursor.getColumnIndex("username");
            String username = cursor.getString(usernameCol);

            int pointsCol = cursor.getColumnIndex("points");
            int points = cursor.getInt(pointsCol);

            int diffifcultyCol = cursor.getColumnIndex("difficulty");
            String difficulty = cursor.getString(diffifcultyCol);

            int createdAtCol = cursor.getColumnIndex("created_at");
            long createdAt = cursor.getInt(createdAtCol);

            scores.add(new Score(id, username, points, GameDifficulty.valueOf(difficulty), createdAt));
        }
        cursor.close();
        return scores;
    }

    public List<Score> getBestScores(int limit) {
        SQLiteDatabase database = getReadableDatabase();
        List<Score> bestScores = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + SCORE_TABLE + " ORDER BY points DESC LIMIT " + limit, null);
        while (cursor.moveToNext()) {
            int idCol = cursor.getColumnIndex("id");
            int id = cursor.getInt(idCol);

            int usernameCol = cursor.getColumnIndex("username");
            String username = cursor.getString(usernameCol);

            int pointsCol = cursor.getColumnIndex("points");
            int points = cursor.getInt(pointsCol);

            int difficultyCol = cursor.getColumnIndex("difficulty");
            String difficulty = cursor.getString(difficultyCol);

            int createdAtCol = cursor.getColumnIndex("created_at");
            long createdAt = cursor.getInt(createdAtCol);

            bestScores.add(new Score(id, username, points, GameDifficulty.valueOf(difficulty), createdAt));
        }
        cursor.close();
        return bestScores;
    }

    public Score getBestScore() {
        SQLiteDatabase database = getReadableDatabase();
        Score bestScore = null;

        Cursor cursor = database.rawQuery("SELECT * FROM " + SCORE_TABLE + " ORDER BY points DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            int idCol = cursor.getColumnIndex("id");
            int id = cursor.getInt(idCol);

            int usernameCol = cursor.getColumnIndex("username");
            String username = cursor.getString(usernameCol);

            int pointsCol = cursor.getColumnIndex("points");
            int points = cursor.getInt(pointsCol);

            int difficultyCol = cursor.getColumnIndex("difficulty");
            String difficulty = cursor.getString(difficultyCol);

            int createdAtCol = cursor.getColumnIndex("created_at");
            long createdAt = cursor.getInt(createdAtCol);

            bestScore = new Score(id, username, points, GameDifficulty.valueOf(difficulty), createdAt);
        }

        cursor.close();
        return bestScore;
    }

    public void insertScore(String username, int points, GameDifficulty difficulty, long current_timestamp) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("points", points);
        values.put("difficulty", difficulty.name());
        values.put("created_at", current_timestamp);
        database.insert(SCORE_TABLE, null, values);
    }

    public void closeDatabase() {
        SQLiteDatabase database = getWritableDatabase();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
