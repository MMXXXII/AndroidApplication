package com.example.androidapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {
    private GameStats gameStats;
    private TextView statsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        statsDetails = findViewById(R.id.statsDetails);
        Button resetStatsButton = findViewById(R.id.resetStatsButton);

        gameStats = new GameStats(this);
        updateStats();

        resetStatsButton.setOnClickListener(v -> {
            gameStats.resetStats();
            updateStats();
            Toast.makeText(this, "Статистика сброшена", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateStats() {
        int gamesPlayed = gameStats.getGamesPlayed();
        int playerWins = gameStats.getPlayerWins();
        int draws = gameStats.getDraws();

        String statsText = "Игр сыграно: " + gamesPlayed +
                "\nПобеды игрока: " + playerWins +
                "\nНичьи: " + draws;

        statsDetails.setText(statsText);
    }
}
