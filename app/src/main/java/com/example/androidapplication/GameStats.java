package com.example.androidapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class GameStats {
    private static final String PREFS_NAME = "TicTacToeStats";
    private static final String GAMES_PLAYED = "gamesPlayed";
    private static final String PLAYER_WINS = "playerWins";
    private static final String DRAWS = "draws";

    private SharedPreferences prefs;

    public GameStats(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addGame(String winner) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAMES_PLAYED, getGamesPlayed() + 1);
        if (winner.equals("Компьютер")) {
            editor.putInt(PLAYER_WINS, getPlayerWins());
        } else {
            editor.putInt(PLAYER_WINS, getPlayerWins() + 1);
        }
        editor.apply();
    }

    public void addDraw() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAMES_PLAYED, getGamesPlayed() + 1);
        editor.putInt(DRAWS, getDraws() + 1);
        editor.apply();
    }

    public int getGamesPlayed() {
        return prefs.getInt(GAMES_PLAYED, 0);
    }

    public int getPlayerWins() {
        return prefs.getInt(PLAYER_WINS, 0);
    }

    public int getDraws() {
        return prefs.getInt(DRAWS, 0);
    }
}