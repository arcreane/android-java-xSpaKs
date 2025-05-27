package com.xspaks.filmscan.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xspaks.filmscan.GoogleVision;
import com.xspaks.filmscan.R;
import com.xspaks.filmscan.adapter.GameObjectAdapter;
import com.xspaks.filmscan.api.GoogleVisionResult;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.model.GameObject;
import com.xspaks.filmscan.model.GoogleVisionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final PhotoDatabase db = new PhotoDatabase(this);
    private GameObjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        List<GameObject> allObjects = db.getAllGameObjects();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GameObjectAdapter(allObjects);
        recyclerView.setAdapter(adapter);

        Button boutonScan = findViewById(R.id.buttonScan);
        boutonScan.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(GameActivity.this, "Impossible d'ouvrir la caméra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the image bitmap from the intent
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            GoogleVision.request(imageBitmap, new GoogleVisionResult() {
                @Override
                public void onResult(String result) {
                    analyzeGoogleVisionResults(result);
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    protected void analyzeGoogleVisionResults(String result) {
        List<GoogleVisionItem> parsedResults = parseGoogleVisionResults(result);
        List<GameObject> existingGameObjects = db.getAllGameObjects();

        // If an item sent by Google Vision matches a database object at >70% precision, validate it
        for (GoogleVisionItem item : parsedResults) {
            for (GameObject gameObject : existingGameObjects) {
                if (item.getDescription().equals(gameObject.getName())
                        && item.getScore() > 0.70) {
                    validatePhoto(gameObject.getId());
                }
            }
        }
    }

    protected List<GoogleVisionItem> parseGoogleVisionResults(String result) {
        List<GoogleVisionItem> parsedResults = new ArrayList<>();

        try {
            JSONObject jsonRoot = new JSONObject(result);
            JSONArray responses = jsonRoot.getJSONArray("responses");
            if (responses.length() > 0) {
                JSONObject firstResponse = responses.getJSONObject(0);
                JSONArray labelAnnotations = firstResponse.getJSONArray("labelAnnotations");

                for (int i = 0; i < labelAnnotations.length(); i++) {
                    JSONObject label = labelAnnotations.getJSONObject(i);
                    String description = label.getString("description");
                    double score = label.getDouble("score");

                    parsedResults.add(new GoogleVisionItem(description, score));
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "Erreur de parsing", e);
        }

        return parsedResults;
    }

    protected void validatePhoto(int gameObjectID) {
        db.updateGameObjectStatus(gameObjectID, true, (int) System.currentTimeMillis());
        List<GameObject> updatedGameObjects = db.getAllGameObjects();
        adapter.updateItems(updatedGameObjects);
        checkGameOver(updatedGameObjects);
    }

    protected void checkGameOver(List<GameObject> gameObjects) {
        boolean allValidated = true;
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isValidated()) {
                allValidated = false;
                break;
            }
        }

        if (allValidated) {
            Intent intent = new Intent(this, EndActivity.class);
            intent.putExtra("numberOfObjects", gameObjects.size());
            intent.putExtra("gameStartedAt", db.getGameStartDate());
            startActivity(intent);
            db.clearGameObjects();
            finish();
        }
    }
}

