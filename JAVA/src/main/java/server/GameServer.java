package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);
            
            while (true) {
                System.out.println("Waiting for Player 1...");
                Socket player1 = serverSocket.accept();
                System.out.println("Player 1 connected!");
                sendMessage(player1, "Welcome Player 1! Waiting for Player 2...");

                System.out.println("Waiting for Player 2...");
                Socket player2 = serverSocket.accept();
                System.out.println("Player 2 connected!");
                sendMessage(player2, "Welcome Player 2! Game is starting...");

                // Start game session in a new thread
                GameSession session = new GameSession(player1, player2);
                new Thread(session::start).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(Socket socket, String message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(message);
    }
}