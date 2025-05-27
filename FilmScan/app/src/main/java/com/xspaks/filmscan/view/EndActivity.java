package com.xspaks.filmscan.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.enums.GameDifficulty;
import com.xspaks.filmscan.model.Score;

import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    private final PhotoDatabase database = new PhotoDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        EditText nameInput = findViewById(R.id.nameInput);
        Button replayButton = findViewById(R.id.replayButton);
        Button quitButton = findViewById(R.id.quitButton);
        TextView bestScoreText = findViewById(R.id.bestScoreMessage);

        int numberOfObjects = getIntent().getIntExtra("numberOfObjects", 0);
        GameDifficulty difficulty = GameDifficulty.getDifficultyFromNumberOfObjects(numberOfObjects);
        long gameStartedAt = getIntent().getLongExtra("gameStartedAt", 0);
        long currentDate = System.currentTimeMillis();

        long durationMillis = currentDate - gameStartedAt;
        long durationSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
        int baseScore = 1000;
        int score = (int) (baseScore / (1 + Math.log1p(durationSeconds)));

        Score bestScore = database.getBestScore();
        if (bestScore == null || bestScore.getPoints() < score) {
            bestScoreText.setVisibility(View.VISIBLE);
        }

        replayButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                database.insertScore(name, score, difficulty, (int) System.currentTimeMillis());
            }

            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        });

        quitButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                database.insertScore(name, score, difficulty, currentDate);
            }

            finishAffinity();
        });
    }
}