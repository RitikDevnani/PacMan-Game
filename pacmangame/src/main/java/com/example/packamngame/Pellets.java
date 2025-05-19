package com.example.packamngame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

public class Pellets {
    private final int width;
    private final int height;
    private final int playerSize;
    private final int pelletSize;
    private final Set<double[]> pellets = new HashSet<>();
    public GraphicsContext gc;

    // Add constructor to set gc
    public Pellets(GraphicsContext gc, int width, int height, int playerSize, int pelletSize) {
        this.gc = gc;
        this.width = width;
        this.height = height;
        this.playerSize = playerSize;
        this.pelletSize = pelletSize;
    }

    public Pellets(int width, int height, int playerSize, int pelletSize) {
        this.width = width;
        this.height = height;
        this.playerSize = playerSize;
        this.pelletSize = pelletSize;
    }

    public void initializePellets(com.example.packamngame.Walls walls) {
        for (int i = 0; i < width; i += playerSize) {
            for (int j = 0; j < height; j += playerSize) {
                if (!walls.getMaze()[i / playerSize][j / playerSize]) {
                    pellets.add(new double[]{i + playerSize / 2.0 - pelletSize / 2.0, j + playerSize / 2.0 - pelletSize / 2.0});
                }
            }
        }
    }

    public void checkCollision(com.example.packamngame.Pacman pacman) {
        pellets.removeIf(pellet -> {
            if (pacman.playerX < pellet[0] + pelletSize && pacman.playerX + playerSize > pellet[0] &&
                    pacman.playerY < pellet[1] + pelletSize && pacman.playerY + playerSize > pellet[1]) {
                pacman.increaseScore();  // Increase score when a pellet is eaten
                return true;  // Remove the pellet from the set
            }
            return false;
        });
    }

    public void draw() {
        gc.setFill(Color.WHITE);
        for (double[] pellet : pellets) {
            gc.fillOval(pellet[0], pellet[1], pelletSize, pelletSize);
        }
    }
}