package com.xspaks.filmscan.view;

import static com.xspaks.filmscan.database.JsonStorage.loadGameObjectsFromJson;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.model.GameObject;
import com.xspaks.filmscan.viewmodel.StartViewModel;

import java.util.Collections;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private StartViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        viewModel = new ViewModelProvider(this).get(StartViewModel.class);

        LinearLayout buttonContainer = findViewById(R.id.button_container);

        for (StartViewModel.GameDifficulty difficulty : StartViewModel.GameDifficulty.values()) {
            Button button = new Button(this);
            button.setText(difficulty.getLabel());
            button.setOnClickListener(v -> viewModel.setSelectedDifficulty(difficulty));
            buttonContainer.addView(button);
        }

        viewModel.getSelectedDifficulty().observe(this, difficulty -> {
            List<String> allObjects = loadGameObjectsFromJson(this);
            PhotoDatabase photoDatabase = new PhotoDatabase();

            Collections.shuffle(allObjects);
            for(int i = 0; i < difficulty.getNumberOfObjects(); i++) {
                photoDatabase.insertGameObject(allObjects.get(i));
            }

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("difficulty", difficulty.name());
            startActivity(intent);
            finish();
        });
    }
}