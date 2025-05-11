package com.example.androidapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;  // Добавьте эту строку


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TicTacToeBoard gameBoard;
    private TextView statusText;
    private Button resetButton;
    private Switch playerModeSwitch;
    private EditText player1NameInput;
    private EditText player2NameInput;
    private GameStats gameStats;
    private boolean isSinglePlayerMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            gameBoard = findViewById(R.id.gameBoard);
            statusText = findViewById(R.id.statusText);
            resetButton = findViewById(R.id.resetButton);
            playerModeSwitch = findViewById(R.id.playerModeSwitch);
            player1NameInput = findViewById(R.id.player1NameInput);
            player2NameInput = findViewById(R.id.player2NameInput);
            Button statsButton = findViewById(R.id.statsButton); // Найти кнопку статистики

            if (gameBoard == null) {
                throw new IllegalStateException("TicTacToeBoard view not found!");
            }

            gameStats = new GameStats(this);

            // Обработчик нажатия кнопки статистики
            statsButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
            });

            setupGameListeners();
            resetGame(); // Инициализируем начальное состояние
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "Ошибка при запуске: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void setupGameListeners() {
        try {
            resetButton.setOnClickListener(v -> resetGame());

            playerModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isSinglePlayerMode = !isChecked;
                player2NameInput.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                gameBoard.setSinglePlayerMode(!isChecked); // This line is important
                resetGame();
            });

            gameBoard.setGameListener(new TicTacToeBoard.GameListener() {
                @Override
                public void onGameWon(char winner) {
                    String winnerName = winner == 'X' ?
                            player1NameInput.getText().toString() :
                            (isSinglePlayerMode ? "Компьютер" : player2NameInput.getText().toString());
                    statusText.setText("Победитель: " + winnerName);
                    gameStats.addGame(winnerName);
                }

                @Override
                public void onGameDraw() {
                    statusText.setText("Ничья!");
                    gameStats.addDraw();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in setupGameListeners: ", e);
            Toast.makeText(this, "Ошибка при настройке: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void resetGame() {
        try {
            gameBoard.resetBoard();
            String playerName = player1NameInput.getText().toString();
            if (playerName.isEmpty()) {
                playerName = "Игрок 1";
                player1NameInput.setText(playerName);
            }
            statusText.setText("Ход игрока: " + playerName);
        } catch (Exception e) {
            Log.e(TAG, "Error in resetGame: ", e);
            Toast.makeText(this, "Ошибка при сбросе игры: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}