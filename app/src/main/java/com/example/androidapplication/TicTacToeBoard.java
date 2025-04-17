package com.example.androidapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeBoard extends View {
    private char[][] board = new char[3][3];
    private Paint linePaint;
    private Paint textPaint;
    private GameListener gameListener;
    private boolean isGameOver = false;
    private Random random = new Random();
    private boolean isSinglePlayerMode = true;

    public interface GameListener {
        void onGameWon(char winner);
        void onGameDraw();
    }

    public TicTacToeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoard();
    }

    private void initBoard() {
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(4f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(100f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        isGameOver = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cellWidth = getWidth() / 3f;
        float cellHeight = getHeight() / 3f;

        // Draw grid lines
        for (int i = 1; i < 3; i++) {
            canvas.drawLine(cellWidth * i, 0, cellWidth * i, getHeight(), linePaint);
            canvas.drawLine(0, cellHeight * i, getWidth(), cellHeight * i, linePaint);
        }

        // Draw X's and O's
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != ' ') {
                    canvas.drawText(String.valueOf(board[i][j]),
                            cellWidth * (i + 0.5f),
                            cellHeight * (j + 0.7f),
                            textPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isGameOver) {
            float x = event.getX();
            float y = event.getY();
            int row = (int) (x / (getWidth() / 3));
            int col = (int) (y / (getHeight() / 3));

            if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ') {
                board[row][col] = 'X';
                invalidate();

                if (checkWinner('X')) {
                    isGameOver = true;
                    if (gameListener != null) gameListener.onGameWon('X');
                } else if (isBoardFull()) {
                    isGameOver = true;
                    if (gameListener != null) gameListener.onGameDraw();
                } else if (isSinglePlayerMode) {
                    makeComputerMove();
                }
            }
        }
        return true;
    }

    private void makeComputerMove() {
        // Простой ИИ: случайный ход
        List<Point> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    availableMoves.add(new Point(i, j));
                }
            }
        }

        if (!availableMoves.isEmpty()) {
            Point move = availableMoves.get(random.nextInt(availableMoves.size()));
            board[move.x][move.y] = 'O';
            invalidate();

            if (checkWinner('O')) {
                isGameOver = true;
                if (gameListener != null) gameListener.onGameWon('O');
            } else if (isBoardFull()) {
                isGameOver = true;
                if (gameListener != null) gameListener.onGameDraw();
            }
        }
    }

    private boolean checkWinner(char player) {
        // Проверка по горизонтали и вертикали
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }

        // Проверка по диагоналям
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[2][0] == player && board[1][1] == player && board[0][2] == player);
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }

    public void setSinglePlayerMode(boolean singlePlayer) {
        this.isSinglePlayerMode = singlePlayer;
    }
}