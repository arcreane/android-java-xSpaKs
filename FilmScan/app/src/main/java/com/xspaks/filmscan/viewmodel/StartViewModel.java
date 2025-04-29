package com.xspaks.filmscan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartViewModel extends ViewModel {

    public enum GameDifficulty {
        EASY("Facile", 3),
        MEDIUM("Moyen", 6),
        HARD("Difficile", 10);

        private final String label;
        private final int numberOfObjects;
        GameDifficulty(String label, int numberOfObjects) {
            this.label = label;
            this.numberOfObjects = numberOfObjects;
        }

        public String getLabel() {
            return label;
        }

        public int getNumberOfObjects() {
            return numberOfObjects;
        }
    }
    private final MutableLiveData<GameDifficulty> selectedDifficulty = new MutableLiveData<>();

    public LiveData<GameDifficulty> getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void setSelectedDifficulty(GameDifficulty difficulty) {
        selectedDifficulty.setValue(difficulty);
    }
}
