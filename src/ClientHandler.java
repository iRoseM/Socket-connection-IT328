import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private List<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.username = in.readLine();
        Server.playerScores.put(username, 0); // تهيئة النقاط عند الاتصال
    }

@Override
public void run() {
try {
        String message;
        while ((message = in.readLine()) != null) {
            if (message.startsWith("PLAY:")) {
                synchronized (Server.playingClients) {
                    if (Server.playingClients.size() < 3 && !Server.playingClients.contains(username)) {
                        Server.playingClients.add(username);
                        Server.broadcastConnectedPlayers();
                        if (Server.playingClients.size() >= 2) {
                            Server.startGame();
                        }
                    }
                }
            } else if (message.equalsIgnoreCase("DISCONNECT")) {
                disconnectClient();
                break;
            } else if (message.startsWith("ANSWER:")) {
                String playerAnswer = message.substring(7).trim();
                synchronized (Server.class) {
                    // Verify the current question index is valid
                    if (Server.currentQuestionIndex < Server.questions.length) {
                        if (playerAnswer.equalsIgnoreCase(Server.answers[Server.currentQuestionIndex])) {
                            int currentScore = Server.playerScores.getOrDefault(username, 0);
                            Server.playerScores.put(username, currentScore + 10);
                            out.println("CORRECT_ANSWER");
                        } else {
                            out.println("WRONG_ANSWER");
                        }

                        // Update scores and proceed to the next question
                        Server.broadcastScores();
                        Server.currentQuestionIndex++;

                        if (Server.currentQuestionIndex < Server.questions.length) {
                            Server.broadcastQuestion();
                        } else {
                            Server.endGame();
                        }
                    } else {
                        Server.endGame(); // End the game if all questions are answered
                        break;
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        disconnectClient();
    }
}


    public void sendMessage(String message) {
        out.println(message);
    }

        public String getUsername() {
        return username;
    }

        public void setUsername(String username) {
        this.username = username;
    }
    private void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
    private void disconnectClient() {
    try {
        synchronized (clients) {
            clients.remove(this);
            broadcast(username + " has left the game.");
            playerList();
        }

        synchronized (Server.playingClients) {
            if (Server.playingClients.contains(username)) {
                Server.playingClients.remove(username);
                Server.broadcastPlayingPlayers();

                // Check if only one player remains in the playing room
                if (Server.playingClients.size() == 1) {
                    String remainingPlayer = Server.playingClients.get(0); // Get the only remaining player
                    Server.playingClients.remove(remainingPlayer); // Remove them from the playing room
                    Server.broadcastPlayingPlayers(); // Update playing players list

                    // Notify the remaining player
                    for (ClientHandler client : Server.clients) {
                        if (client.getUsername().equals(remainingPlayer)) {
                            client.sendMessage("KICKED:You cannot play alone. Please wait for others to join.");
                        }
                    }
                }
            }
        }

        // Inform the disconnected client
        sendMessage("DISCONNECTED:You have been disconnected.");
        System.out.println(username + " disconnected.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void playerList() {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler client : clients) {
            sb.append(client.username).append(",");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); // Remove trailing comma
        }

        broadcast("Connected users: " + sb.toString());
    }
}
