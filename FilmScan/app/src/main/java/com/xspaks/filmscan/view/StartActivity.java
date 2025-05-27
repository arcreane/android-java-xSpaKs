package com.xspaks.filmscan.view;

import static com.xspaks.filmscan.database.JsonStorage.loadGameObjectsFromJson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.adapter.ScoreAdapter;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.enums.GameDifficulty;
import com.xspaks.filmscan.model.Score;

import java.util.Collections;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private GameDifficulty difficulty;
    private PhotoDatabase database = new PhotoDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database.clearGameObjects();
        if (!database.areAllObjectsValidated()) {
            difficulty = GameDifficulty.getDifficultyFromNumberOfObjects(database.existingObjectsLength());

            // Security if database has too many objects stored for some reasons
            if (difficulty == null) {
                database.clearGameObjects();
            } else {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", difficulty.name());
                startActivity(intent);
                finish();
            }
        }

        // Add difficulty buttons based on enum values
        LinearLayout buttonContainer = findViewById(R.id.button_container);
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            Button button = new Button(this);
            button.setText(difficulty.getLabel());
            button.setBackgroundColor(ContextCompat.getColor(this, difficulty.getColor()));

            // Margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 20, 0, 0); // 16px en haut
            button.setLayoutParams(params);
            button.setTextColor(getResources().getColor(R.color.black));

            // ➕ OnClickListener pour lancer la partie
            button.setOnClickListener(v -> {
                List<String> allObjectsString = loadGameObjectsFromJson(this);
                Collections.shuffle(allObjectsString);
                database.clearGameObjects();

                for (int i = 0; i < difficulty.getNumberOfObjects(); i++) {
                    String name = allObjectsString.get(i);
                    int currentTimestamp = (int) System.currentTimeMillis();
                    database.insertGameObject(name, currentTimestamp);
                }

                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", difficulty.name());
                startActivity(intent);
                finish();
            });

            buttonContainer.addView(button);
        }

        TextView noScoreText = findViewById(R.id.noScoreText);
        RecyclerView recyclerView = findViewById(R.id.scoreRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Score> topScores = database.getBestScores(5);

        if (topScores.isEmpty()) {
            noScoreText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noScoreText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ScoreAdapter scoreAdapter = new ScoreAdapter(topScores);
            recyclerView.setAdapter(scoreAdapter);
        }
    }
}