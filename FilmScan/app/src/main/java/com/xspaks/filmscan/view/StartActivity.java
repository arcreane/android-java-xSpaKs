package com.xspaks.filmscan.view;

import static com.xspaks.filmscan.database.JsonStorage.loadGameObjectsFromJson;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.model.GameObject;
import com.xspaks.filmscan.model.Score;
import com.xspaks.filmscan.viewmodel.StartViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private StartViewModel viewModel;
    private PhotoDatabase database = new PhotoDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        viewModel = new ViewModelProvider(this).get(StartViewModel.class);

        database.clearGameObjects();
        if (!database.areAllObjectsValidated()) {
            StartViewModel.GameDifficulty difficulty = StartViewModel.GameDifficulty.getDifficultyFromNumberOfObjects(database.existingObjectsLength());

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
        for (StartViewModel.GameDifficulty difficulty : StartViewModel.GameDifficulty.values()) {
            Button button = new Button(this);
            button.setText(difficulty.getLabel());
            button.setOnClickListener(v -> viewModel.setSelectedDifficulty(difficulty));
            buttonContainer.addView(button);
        }

        TextView bestScoreText = findViewById(R.id.bestScoreText);
        Score bestScore = database.getBestScore();

        if (bestScore != null) {
            String message = "Meilleur score : " + bestScore.getUsername() + " - " + bestScore.getPoints() + " pts";
            bestScoreText.setText(message);
        } else {
            bestScoreText.setText("Aucun score enregistré");
        }

        // Difficulty selection management
        viewModel.getSelectedDifficulty().observe(this, difficulty -> {
            List<String> allObjectsString = loadGameObjectsFromJson(this);

            Collections.shuffle(allObjectsString);
            for(int i = 0; i < difficulty.getNumberOfObjects(); i++) {
                String name = allObjectsString.get(i);
                database.insertGameObject(name);
            }

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("difficulty", difficulty.name());
            startActivity(intent);
            finish();
        });
    }
}