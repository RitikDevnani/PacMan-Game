package com.example.packamngame;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PacmanGame extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 40;
    private static final int PELLET_SIZE = 10;
    private static final int ENEMY_SIZE = 40;
    private static final int ENEMY_SPEED = 6;

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private com.example.packamngame.Walls walls;
    private com.example.packamngame.Pellets pellets;
    private com.example.packamngame.Enemies enemies;
    private com.example.packamngame.Pacman pacman;
    private GraphicsContext gc;  // Define gc as a class-level variable

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pac-Man Game");
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();  // Initialize gc here

        Scene scene = new Scene(new StackPane(canvas));
        primaryStage.setScene(scene);
        primaryStage.show();

        walls = new com.example.packamngame.Walls(gc, WIDTH, HEIGHT, PLAYER_SIZE);
        walls.initializeMaze();

        pacman = initializePlayerPosition();  // Use initializePlayerPosition to set pacman's initial position

        pellets = new com.example.packamngame.Pellets(gc, WIDTH, HEIGHT, PLAYER_SIZE, PELLET_SIZE);
        pellets.initializePellets(walls);

        enemies = new com.example.packamngame.Enemies(gc, WIDTH, HEIGHT, PLAYER_SIZE, ENEMY_SIZE, ENEMY_SPEED, pacman);
        enemies.initializeEnemies(walls);

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw(gc);
            }
        };
        timer.start();
    }

    private com.example.packamngame.Pacman initializePlayerPosition() {
        for (int i = 0; i < walls.getMaze().length; i++) {
            for (int j = 0; j < walls.getMaze()[i].length; j++) {
                if (!walls.getMaze()[i][j]) {
                    var pacman = new com.example.packamngame.Pacman(gc);
                    pacman.playerX = i * PLAYER_SIZE;
                    pacman.playerY = j * PLAYER_SIZE;
                    return pacman;
                }
            }
        }
        throw new IllegalStateException("Cannot initialize position for Pacman.");
    }

    private void update() {
        pacman.update(pressedKeys, walls);
        enemies.update(pacman, walls);
        if (enemies.checkCollisionWithPacman(pacman)) {
            gameOver();
        }
        pellets.checkCollision(pacman);
    }

    private void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        walls.draw();
        pellets.draw();
        enemies.draw();
        pacman.draw();
    }

    private void gameOver() {
        System.out.println("Game Over! Resetting positions.");
        System.out.println("Your final score is " +  pacman.getScore());
        pacman = initializePlayerPosition(); // Reset Pacman position
        enemies.initializeEnemies(walls); // Reset enemies
        //pellets.initializePellets(walls);
    }
}
