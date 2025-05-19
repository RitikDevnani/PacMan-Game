package com.example.packamngame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.HashSet;
import java.util.Set;

public class Enemies {
    // Replace record with a regular class
    public class Enemy {
        private final int x;
        private final int y;

        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }
    }

    public GraphicsContext gc;
    private final int width;
    private final int height;
    private final int playerSize;
    private final int enemySize;
    private final int enemySpeed;
    private final Set<Enemy> enemies = new HashSet<>();
    private Pacman pacman;// Reference to Pacman instance
    int enemiesAdded = 0;

    // Constructor now accepts Pacman instance
    public Enemies(GraphicsContext gc, int width, int height, int playerSize, int enemySize, int enemySpeed, Pacman pacman) {
        this.gc = gc;  // Assigning gc here
        this.width = width;
        this.height = height;
        this.playerSize = playerSize;
        this.enemySize = enemySize;
        this.enemySpeed = enemySpeed;
        this.pacman = pacman;
    }

    public int size() {
        return enemiesAdded;
    }

    public void initializeEnemies(com.example.packamngame.Walls walls) {
        enemies.clear(); // Clear existing enemies
        enemiesAdded = 0;
        int minDistanceFromPlayer = 10;
        int playerXGrid = (int) pacman.playerX / playerSize;
        int playerYGrid = (int) pacman.playerY / playerSize;

        int x_axis = 760, y_axis =  520;

        for(int i = 0; i <  4; i++) {
            enemies.add(new Enemy(x_axis, y_axis));
            //System.out.println("Enemy added at: " + x_axis + ", " + y_axis);
            x_axis += 40;
            y_axis -= 40;
        }
    }

    public void update(Pacman pacman, Walls walls) {
        Set<Enemy> updatedEnemies = new HashSet<>();

        for (var enemy : enemies) {
            double diffX = pacman.playerX - enemy.x();
            double diffY = pacman.playerY - enemy.y();

            int newX = enemy.x();
            int newY = enemy.y();

            boolean moved = false;

            // Prioritize movement based on same position on one axis
            if (enemy.x() == pacman.playerX) {
                // Same x position, move by y coordinate
                double nextY = enemy.y() + Math.signum(diffY) * enemySpeed;
                if (!isCollision(enemy.x(), nextY, walls)) {
                    newY = (int) nextY;  // Cast to int
                    moved = true;
                }
            } else if (enemy.y() == pacman.playerY) {
                // Same y position, move by x coordinate
                double nextX = enemy.x() + Math.signum(diffX) * enemySpeed;
                if (!isCollision(nextX, enemy.y(), walls)) {
                    newX = (int) nextX;  // Cast to int
                    moved = true;
                }
            } else {
                // Standard movement logic based on smaller difference between x and y
                if (Math.abs(diffX) <= Math.abs(diffY)) {
                    // Move by x coordinate
                    double nextX = enemy.x() + Math.signum(diffX) * enemySpeed;
                    if (!isCollision(nextX, enemy.y(), walls)) {
                        newX = (int) nextX;  // Cast to int
                        moved = true;
                    }
                } else {
                    // Move by y coordinate
                    double nextY = enemy.y() + Math.signum(diffY) * enemySpeed;
                    if (!isCollision(enemy.x(), nextY, walls)) {
                        newY = (int) nextY;  // Cast to int
                        moved = true;
                    }
                }
            }

            // If collision occurred or collided with another enemy, move backward
            if (!moved || isEnemyCollision(newX, newY)) {
                // Move backward in x direction
                if (Math.abs(diffX) <= Math.abs(diffY)) {
                    newX = enemy.x() - (int) Math.signum(diffX) * enemySpeed;  // Cast to int
                } else {
                    // Move backward in y direction
                    newY = enemy.y() - (int) Math.signum(diffY) * enemySpeed;  // Cast to int
                }
            }

            updatedEnemies.add(new Enemy(newX, newY));
        }

        enemies.clear();
        enemies.addAll(updatedEnemies);
    }

    private boolean isEnemyCollision(int x, int y) {
        for (Enemy other : enemies) {
            if (other.x() == x && other.y() == y) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollision(double x, double y, com.example.packamngame.Walls walls) {
        // Boundary check
        if (x < 0 || x + playerSize > width || y < 0 || y + playerSize > height) {
            return true; // Out of bounds
        }

        boolean[][] maze = walls.getMaze();
        int gridXStart = (int) (x / playerSize);
        int gridYStart = (int) (y / playerSize);
        int gridXEnd = (int) ((x + playerSize - 1) / playerSize);
        int gridYEnd = (int) ((y + playerSize - 1) / playerSize);

        if (gridXStart < 0 || gridXEnd >= maze.length || gridYStart < 0 || gridYEnd >= maze[0].length) {
            return true;
        }
        return maze[gridXStart][gridYStart] || maze[gridXEnd][gridYStart] || maze[gridXStart][gridYEnd] || maze[gridXEnd][gridYEnd];
    }


    public void draw() {
        gc.setFill(Color.RED);
        for (var enemy : enemies) {
            gc.fillArc(enemy.x(), enemy.y(), enemySize, enemySize, 0, 180, ArcType.ROUND);
            gc.fillRect(enemy.x(), enemy.y() + enemySize / 2, enemySize, enemySize / 2);
            gc.setFill(Color.WHITE);
            gc.fillOval(enemy.x() + enemySize * 0.2, enemy.y() + enemySize * 0.2, enemySize * 0.2, enemySize * 0.2);
            gc.fillOval(enemy.x() + enemySize * 0.6, enemy.y() + enemySize * 0.2, enemySize * 0.2, enemySize * 0.2);
            gc.setFill(Color.RED);
        }
    }

    public boolean checkCollisionWithPacman(Pacman pacman) {
        for (Enemy enemy : enemies) {
            double distance = Math.sqrt(Math.pow(pacman.playerX - enemy.x(), 2) + Math.pow(pacman.playerY - enemy.y(), 2));
            if (distance < playerSize) {
                return true;  // Collision detected
            }
        }
        return false;  // No collision
    }

}
