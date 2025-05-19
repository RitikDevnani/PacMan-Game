package com.example.packamngame;

import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Pacman {
    int score = 0;
    private static final int PLAYER_SIZE = 40;
    public GraphicsContext gc;
    public double playerX;
    public double playerY;
    public double playerSpeed = 4;
    public double mouthAngle = 45;
    public boolean mouthOpening = true;
    public double mouthDirection = 90;

    // Add a constructor to set gc
    public Pacman(GraphicsContext gc) {
        this.gc = gc;
    }

    public void increaseScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void draw() {
        gc.setFill(Color.YELLOW);
        gc.fillArc(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE,
                mouthDirection + mouthAngle,
                360 - 2 * mouthAngle,
                ArcType.ROUND);
    }

    public void update(Set<KeyCode> pressedKeys, com.example.packamngame.Walls walls) {
        if (pressedKeys.contains(KeyCode.LEFT) && playerX > 0) {
            if (!isCollision(playerX - playerSpeed, playerY, walls)) {
                playerX -= playerSpeed;
                mouthDirection = 180;
            }
        }

        if (pressedKeys.contains(KeyCode.RIGHT) && playerX < 1000 - PLAYER_SIZE) {  // Use width instead of screenWidth
            if (!isCollision(playerX + playerSpeed, playerY, walls)) {
                playerX += playerSpeed;
                mouthDirection = 0; // Facing right
            }
        }
        if (pressedKeys.contains(KeyCode.UP) && playerY > 0) {
            if (!isCollision(playerX, playerY - playerSpeed, walls)) {
                playerY -= playerSpeed;
                mouthDirection = 90; // Facing up
            }
        }
        if (pressedKeys.contains(KeyCode.DOWN) && playerY < 600 - PLAYER_SIZE) {  // Use height instead of screenHeight
            if (!isCollision(playerX, playerY + playerSpeed, walls)) {
                playerY += playerSpeed;
                mouthDirection = 270; // Facing down
            }
        }

        if (mouthOpening) {
            mouthAngle += 2;
            if (mouthAngle >= 45) {
                mouthOpening = false;
            }
        } else {
            mouthAngle -= 2;
            if (mouthAngle <= 10) {
                mouthOpening = true;
            }
        }

        if (mouthOpening) {
            mouthAngle += 2;
            if (mouthAngle >= 45) {
                mouthOpening = false;
            }
        } else {
            mouthAngle -= 2;
            if (mouthAngle <= 10) {
                mouthOpening = true;
            }
        }
    }

    private boolean isCollision(double x, double y, com.example.packamngame.Walls walls) {
        boolean[][] maze = walls.getMaze();
        int gridXStart = (int) (x / PLAYER_SIZE);
        int gridYStart = (int) (y / PLAYER_SIZE);
        int gridXEnd = (int) ((x + PLAYER_SIZE - 1) / PLAYER_SIZE);
        int gridYEnd = (int) ((y + PLAYER_SIZE - 1) / PLAYER_SIZE);

        if (gridXStart < 0 || gridXEnd >= maze.length || gridYStart < 0 || gridYEnd >= maze[0].length) {
            return true;
        }
        return maze[gridXStart][gridYStart] || maze[gridXEnd][gridYStart] || maze[gridXStart][gridYEnd] || maze[gridXEnd][gridYEnd];
    }
}
