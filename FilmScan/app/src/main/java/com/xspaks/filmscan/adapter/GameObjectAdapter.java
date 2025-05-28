package com.xspaks.filmscan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xspaks.filmscan.R;
import com.xspaks.filmscan.model.GameObject;

import java.util.List;

public class GameObjectAdapter extends RecyclerView.Adapter<GameObjectAdapter.ViewHolder> {

    private List<GameObject> items;

    public GameObjectAdapter(List<GameObject> items) {
        this.items = items;
    }

    public void updateItems(List<GameObject> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView validatedTextView;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.textName);
            validatedTextView = view.findViewById(R.id.textValidatedAt);
        }
    }

    @NonNull
    @Override
    public GameObjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_object, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameObjectAdapter.ViewHolder holder, int position) {
        GameObject item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.validatedTextView.setText(item.getFormattedValidatedDate());

        if (item.isValidated()) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.valid_green));
            holder.validatedTextView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.invalid_red));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
