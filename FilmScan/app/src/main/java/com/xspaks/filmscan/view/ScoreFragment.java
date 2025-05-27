package com.xspaks.filmscan.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.adapter.ScoreAdapter;
import com.xspaks.filmscan.database.PhotoDatabase;
import com.xspaks.filmscan.model.Score;

import java.util.List;

public class ScoreFragment extends Fragment {

    private PhotoDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = new PhotoDatabase(requireContext());

        View easyBlock = view.findViewById(R.id.easyScores);
        ((TextView) easyBlock.findViewById(R.id.scoreTitle)).setText(R.string.easy_scores);
        TextView noEasyScoreText = easyBlock.findViewById(R.id.noScoreText);
        RecyclerView easyRecyclerView = easyBlock.findViewById(R.id.scoreRecyclerView);
        easyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Score> topEasyScores = database.getBestScores(5, "EASY");
        if (topEasyScores.isEmpty()) {
            noEasyScoreText.setVisibility(View.VISIBLE);
            easyRecyclerView.setVisibility(View.GONE);
        } else {
            noEasyScoreText.setVisibility(View.GONE);
            easyRecyclerView.setVisibility(View.VISIBLE);
            ScoreAdapter scoreAdapter = new ScoreAdapter(topEasyScores);
            easyRecyclerView.setAdapter(scoreAdapter);
        }

        View mediumBlock = view.findViewById(R.id.mediumScores);
        ((TextView) mediumBlock.findViewById(R.id.scoreTitle)).setText(R.string.medium_scores);
        TextView noMediumScoreText = mediumBlock.findViewById(R.id.noScoreText);
        RecyclerView mediumRecyclerView = mediumBlock.findViewById(R.id.scoreRecyclerView);
        mediumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Score> topMediumScores = database.getBestScores(5, "MEDIUM");
        if (topMediumScores.isEmpty()) {
            noMediumScoreText.setVisibility(View.VISIBLE);
            mediumRecyclerView.setVisibility(View.GONE);
        } else {
            noMediumScoreText.setVisibility(View.GONE);
            mediumRecyclerView.setVisibility(View.VISIBLE);
            ScoreAdapter scoreAdapter = new ScoreAdapter(topMediumScores);
            mediumRecyclerView.setAdapter(scoreAdapter);
        }

        View hardBlock = view.findViewById(R.id.hardScores);
        ((TextView) hardBlock.findViewById(R.id.scoreTitle)).setText(R.string.hard_scores);
        TextView noHardScoreText = hardBlock.findViewById(R.id.noScoreText);
        RecyclerView hardRecyclerView = hardBlock.findViewById(R.id.scoreRecyclerView);
        hardRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Score> topHardScores = database.getBestScores(5, "HARD");
        if (topHardScores.isEmpty()) {
            noHardScoreText.setVisibility(View.VISIBLE);
            hardRecyclerView.setVisibility(View.GONE);
        } else {
            noHardScoreText.setVisibility(View.GONE);
            hardRecyclerView.setVisibility(View.VISIBLE);
            ScoreAdapter scoreAdapter = new ScoreAdapter(topHardScores);
            hardRecyclerView.setAdapter(scoreAdapter);
        }
    }
}