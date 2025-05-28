package com.xspaks.filmscan.view;

import static com.xspaks.filmscan.database.JsonStorage.loadGameObjectsFromJson;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.adapter.ScoreAdapter;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.enums.GameDifficulty;
import com.xspaks.filmscan.model.Score;

import java.util.Collections;
import java.util.List;

public class MenuFragment extends Fragment {

    private GameDifficulty difficulty;
    private PhotoDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = new PhotoDatabase(requireContext());
        //database.clearScores();
        //database.clearGameObjects();
        if (!database.areAllObjectsValidated()) {
            difficulty = GameDifficulty.getDifficultyFromNumberOfObjects(database.existingObjectsLength());

            if (difficulty == null) {
                database.clearGameObjects();
            } else {
                Intent intent = new Intent(requireContext(), GameActivity.class);
                intent.putExtra("difficulty", difficulty.name());
                startActivity(intent);
                return;
            }
        }

        LinearLayout buttonContainer = view.findViewById(R.id.button_container);
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            Button button = new Button(requireContext());
            button.setText(difficulty.getLabel());
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), difficulty.getColor()));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 20, 0, 0);
            button.setLayoutParams(params);
            button.setTextColor(getResources().getColor(R.color.black));

            button.setOnClickListener(v -> {
                List<String> allObjectsString = loadGameObjectsFromJson(requireContext());
                Collections.shuffle(allObjectsString);
                database.clearGameObjects();

                for (int i = 0; i < difficulty.getNumberOfObjects(); i++) {
                    String name = allObjectsString.get(i);
                    long currentTimestamp = System.currentTimeMillis();
                    database.insertGameObject(name, currentTimestamp);
                }

                Intent intent = new Intent(requireContext(), GameActivity.class);
                intent.putExtra("difficulty", difficulty.name());
                startActivity(intent);
            });

            buttonContainer.addView(button);
        }
    }
}