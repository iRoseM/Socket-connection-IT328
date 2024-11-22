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
                    // التحقق من صحة الفهرس قبل معالجة الإجابة
                    if (Server.currentQuestionIndex < Server.questions.length) {
                        if (playerAnswer.equalsIgnoreCase(Server.answers[Server.currentQuestionIndex])) {
                            int currentScore = Server.playerScores.getOrDefault(username, 0);
                            Server.playerScores.put(username, currentScore + 10);
                            Server.broadcastScores();
                            Server.currentQuestionIndex++;

                            // التحقق إذا كانت هناك أسئلة متبقية
                            if (Server.currentQuestionIndex < Server.questions.length) {
                                Server.broadcastQuestion();
                            } else {
                                Server.endGame();
                            }
                        } else {
                            out.println("WRONG_ANSWER");
                        }
                    } else {
                        Server.endGame(); // إنهاء اللعبة إذا انتهت الأسئلة
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
            }
        }

        // Inform the user
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
