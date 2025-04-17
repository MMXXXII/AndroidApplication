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
    private Paint winLinePaint;
    private GameListener gameListener;
    private boolean isGameOver = false;
    private Random random = new Random();
    private boolean isSinglePlayerMode = true;
    private int[] winningLine; // Stores coordinates of winning line

    // ... keep existing constructor and interface ...

    private void initBoard() {
        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.grid_lines));
        linePaint.setStrokeWidth(8f);
        linePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        winLinePaint = new Paint();
        winLinePaint.setColor(getResources().getColor(R.color.winning_line));
        winLinePaint.setStrokeWidth(16f);
        winLinePaint.setAntiAlias(true);
        winLinePaint.setStrokeCap(Paint.Cap.ROUND);

        resetBoard();
    }

    public interface GameListener {
        void onGameWon(char winner);
        void onGameDraw();
    }

    public TicTacToeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoard();
    }
    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        winningLine = null;
        isGameOver = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cellWidth = getWidth() / 3f;
        float cellHeight = getHeight() / 3f;

        // Рисуем сетку
        for (int i = 1; i < 3; i++) {
            canvas.drawLine(cellWidth * i, 0, cellWidth * i, getHeight(), linePaint);
            canvas.drawLine(0, cellHeight * i, getWidth(), cellHeight * i, linePaint);
        }

        // Рисуем X и O
        float symbolSize = Math.min(cellWidth, cellHeight) * 0.2f;
        textPaint.setTextSize(Math.min(cellWidth, cellHeight) * 0.6f);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float centerX = cellWidth * (i + 0.5f);
                float centerY = cellHeight * (j + 0.5f);

                if (board[i][j] == 'X') {
                    textPaint.setColor(getResources().getColor(R.color.x_color));
                    canvas.drawText("X", centerX, centerY + symbolSize, textPaint);
                } else if (board[i][j] == 'O') {
                    textPaint.setColor(getResources().getColor(R.color.o_color));
                    canvas.drawText("O", centerX, centerY + symbolSize, textPaint);
                }
            }
        }

        // Рисуем линию победителя с анимацией
        if (winningLine != null) {
            float startX = cellWidth * (winningLine[0] + 0.5f);
            float startY = cellHeight * (winningLine[1] + 0.5f);
            float endX = cellWidth * (winningLine[2] + 0.5f);
            float endY = cellHeight * (winningLine[3] + 0.5f);
            canvas.drawLine(startX, startY, endX, endY, winLinePaint);
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
                char currentPlayer = getCurrentPlayer();
                board[row][col] = currentPlayer;
                invalidate();

                if (checkWinner(currentPlayer)) {
                    isGameOver = true;
                    if (gameListener != null) gameListener.onGameWon(currentPlayer);
                } else if (isBoardFull()) {
                    isGameOver = true;
                    if (gameListener != null) gameListener.onGameDraw();
                } else if (isSinglePlayerMode && currentPlayer == 'X') {
                    makeComputerMove();
                }
            }
        }
        return true;
    }

    private char getCurrentPlayer() {
        int xCount = 0, oCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X') xCount++;
                if (board[i][j] == 'O') oCount++;
            }
        }
        return xCount <= oCount ? 'X' : 'O';
    }

    private void makeComputerMove() {
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
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                winningLine = new int[]{i, 0, i, 2};
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                winningLine = new int[]{0, i, 2, i};
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            winningLine = new int[]{0, 0, 2, 2};
            return true;
        }
        if (board[2][0] == player && board[1][1] == player && board[0][2] == player) {
            winningLine = new int[]{2, 0, 0, 2};
            return true;
        }

        return false;
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