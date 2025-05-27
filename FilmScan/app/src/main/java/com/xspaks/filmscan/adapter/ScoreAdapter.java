package com.xspaks.filmscan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.model.Score;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private List<Score> scores;

    public ScoreAdapter(List<Score> scores) {
        this.scores = scores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Score score = scores.get(position);
        holder.usernameText.setText(score.getUsername());
        holder.pointsText.setText(score.getPoints() + " pts");
        holder.dateText.setText(score.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText, pointsText, dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.usernameText);
            pointsText = itemView.findViewById(R.id.pointsText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}
