package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// GameSession manages a 2-player game with turn-based guessing
public class GameSession {
    private final Socket player1;
    private final Socket player2;
    private final PrintWriter out1;
    private final PrintWriter out2;
    private final BufferedReader in1;
    private final BufferedReader in2;
    private int secretCode;
    private int currentTurn; // 1 or 2
    private boolean gameOver;
    private boolean sessionActive;

    public GameSession(Socket player1, Socket player2) throws IOException {
        this.player1 = player1;
        this.player2 = player2;
        this.out1 = new PrintWriter(player1.getOutputStream(), true);
        this.out2 = new PrintWriter(player2.getOutputStream(), true);
        this.in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
        this.in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
        this.sessionActive = true;
    }

    // Start the game session
    public void start() {
        while (sessionActive) {
            startNewGame();

            // Start threads to handle each player's input
            Thread t1 = new Thread(() -> handlePlayer(1, in1));
            Thread t2 = new Thread(() -> handlePlayer(2, in2));
            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (sessionActive) {
                handleRestart();
            }
        }

        cleanup();
    }

    // Initialize a new game
    private void startNewGame() {
        this.secretCode = new GameLogic().generateSecretCode();
        this.currentTurn = 1;
        this.gameOver = false;

        broadcast("====================================");
        broadcast("Game started! Secret code has been generated.");
        broadcast("Player 1 goes first.");
        notifyTurn();
    }

    // Handle input from a player
    private void handlePlayer(int playerNum, BufferedReader in) {
        try {
            String inputLine;
            while (!gameOver && (inputLine = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(inputLine)) {
                    broadcast("Player " + playerNum + " left the game.");
                    gameOver = true;
                    sessionActive = false;
                    break;
                }

                synchronized (this) {
                    if (currentTurn != playerNum) {
                        sendToPlayer(playerNum, "It's not your turn! Wait for Player " + currentTurn);
                        continue;
                    }

                    processGuess(playerNum, inputLine);
                }
            }
        } catch (IOException e) {
            if (!gameOver) {
                broadcast("Player " + playerNum + " disconnected.");
                gameOver = true;
                sessionActive = false;
            }
        }
    }

    // Process a player's guess
    private void processGuess(int playerNum, String input) {
        try {
            int guess = GameLogic.validateGuess(input);
            String prefix = GameLogic.generateTimestampPrefix();

            broadcast(prefix + " Player " + playerNum + " guessed: " + guess);

            if (guess == secretCode) {
                broadcast("====================================");
                broadcast(prefix + " GAME OVER!");
                broadcast("Player " + playerNum + " wins! The code was " + secretCode);
                broadcast("====================================");
                gameOver = true;
            } else {
                broadcast(prefix + " Wrong guess! Try again.");
                // Switch turns
                currentTurn = (currentTurn == 1) ? 2 : 1;
                notifyTurn();
            }
        } catch (IllegalArgumentException e) {
            sendToPlayer(playerNum, "Invalid input: " + e.getMessage());
        }
    }

    // Handle restart logic
    private void handleRestart() {
        broadcast("Do you want to play again? (yes/no)");

        try {
            String response1 = in1.readLine();
            String response2 = in2.readLine();

            if ("yes".equalsIgnoreCase(response1) && "yes".equalsIgnoreCase(response2)) {
                broadcast("Both players agreed! Starting new game...");
            } else {
                broadcast("Game session ended. Thanks for playing!");
                sessionActive = false;
            }
        } catch (IOException e) {
            sessionActive = false;
        }
    }

    // Send message to both players
    private void broadcast(String message) {
        out1.println(message);
        out2.println(message);
    }

    // Send message to specific player
    private void sendToPlayer(int playerNum, String message) {
        if (playerNum == 1) {
            out1.println(message);
        } else {
            out2.println(message);
        }
    }

    // Notify whose turn it is
    private void notifyTurn() {
        sendToPlayer(currentTurn, "Your turn! Enter your guess:");
        int otherPlayer = (currentTurn == 1) ? 2 : 1;
        sendToPlayer(otherPlayer, "Waiting for Player " + currentTurn + " to guess...");
    }

    // Cleanup resources
    private void cleanup() {
        try {
            player1.close();
            player2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}