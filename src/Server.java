import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 6789;
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static ArrayList<String> playingClients = new ArrayList<>();
    public static HashMap<String, Integer> playerScores = new HashMap<>(); // لتخزين أسماء اللاعبين ونقاطهم

    static String[] questions = {
        "Red + Yellow = ?", "Blue + Yellow = ?", "Red + Blue = ?", "Red + White = ?",
        "Yellow + Green = ?", "Blue + White = ?", "Green + Red = ?", "Orange + Blue = ?",
        "Yellow + Blue + Red = ?", "Green + Yellow = ?"
    };

    static String[] answers = {
        "Orange", "Green", "Purple", "Pink", "Lime",
        "Light Blue", "Brown", "Gray", "Brown", "Chartreuse"
    };

    static int currentQuestionIndex = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                broadcastConnectedPlayers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // بدء اللعبة
    public static synchronized void startGame() {
    if (playingClients.size() >= 2 && playingClients.size() <= 3) {
        if (playingClients.size() == 2)
            startTimer();
        
        StringBuilder gameStartMessage = new StringBuilder("GAME_START:");
        for (String player : playingClients) {
            gameStartMessage.append(player).append(",");
        }
        if (gameStartMessage.length() > 11) {  // "GAME_START:" has length 11
            gameStartMessage.setLength(gameStartMessage.length() - 1);
        }

        // Notify all clients in the `playingClients` list
        for (ClientHandler client : clients) {
            if (playingClients.contains(client.getUsername())) {
                client.sendMessage(gameStartMessage.toString());
            }
        }

        // Immediately send the first question after starting the game
    }
}

    // بث الأسئلة لجميع اللاعبين
    public static synchronized void broadcastQuestion() {
        if (currentQuestionIndex < questions.length) {
            String questionMessage = "QUESTION:" + questions[currentQuestionIndex] + ";OPTIONS:";
            List<String> options = new ArrayList<>();
            options.add(answers[currentQuestionIndex]);
            String[] distractors = {"Blue", "Yellow", "Red", "White", "Black", "Purple", "Green", "Pink"};
            while (options.size() < 4) {
                String option = distractors[(int) (Math.random() * distractors.length)];
                if (!options.contains(option)) {
                    options.add(option);
                }
            }
            Collections.shuffle(options);
            for (String option : options) {
                questionMessage += option + ",";
            }
            questionMessage = questionMessage.substring(0, questionMessage.length() - 1);
            for (ClientHandler client : clients) {
                if (playingClients.contains(client.getUsername())) {
                    client.sendMessage(questionMessage);
                }
            }
        }
    }

    // بث النقاط المحدثة لجميع اللاعبين
    public static synchronized void broadcastScores() {
        StringBuilder scoresMessage = new StringBuilder("SCORES:");
        for (String player : playingClients) {
            scoresMessage.append(player).append("=").append(playerScores.getOrDefault(player, 0)).append("\n");
        }
        if (scoresMessage.length() > 7) { // إزالة الفاصلة الأخيرة
            scoresMessage.setLength(scoresMessage.length() - 1);
        }
        for (ClientHandler client : clients) {
            if (playingClients.contains(client.getUsername())) {
                client.sendMessage(scoresMessage.toString());
            }
        }
    }

    // إنهاء اللعبة وعرض قائمة الفائزين أو رسالة "لا يوجد فائز"
    
    // بث قائمة اللاعبين المتصلين
    public static synchronized void broadcastConnectedPlayers() {
        StringBuilder connectedPlayers = new StringBuilder("Connected users: ");
        for (ClientHandler client : clients) {
            connectedPlayers.append(client.getUsername()).append(",");
        }

        if (connectedPlayers.length() > 0) {
            connectedPlayers.setLength(connectedPlayers.length() - 1);  // Remove trailing comma
        }

        // Broadcast to all clients
        for (ClientHandler client : clients) {
            client.sendMessage(connectedPlayers.toString());
        }
        
    }
    
    public static synchronized void broadcastPlayingPlayers() {
    StringBuilder playingList = new StringBuilder("PLAYING:");
    for (String player : playingClients) {
        playingList.append(player).append(",");
    }

    // Remove the trailing comma if there are players
    if (playingList.length() > 8) {  // "PLAYING:" has length 8
        playingList.setLength(playingList.length() - 1);
    }

    // Broadcast the message to all connected clients
    for (ClientHandler client : clients) {
        client.sendMessage(playingList.toString());
    }
    }   

    // هذه الدالة يجب استدعاؤها عند انتهاء الأسئلة أو عند تجاوز الفهرس
public static synchronized void endGame() {
    String winnerMessage = "GAME_OVER: ";
    int maxScore = -1;
    List<String> winners = new ArrayList<>();

    // حساب النقاط وتحديد الفائزين
    for (String player : playingClients) {
        int score = playerScores.getOrDefault(player, 0);
        if (score > maxScore) {
            maxScore = score;
            winners.clear();
            winners.add(player);
        } else if (score == maxScore) {
            winners.add(player);
        }
    }

    // تحديد الرسالة المرسلة
    if (maxScore == 0) {
        winnerMessage += "No winner this time. Everyone scored 0.";
    } else if (winners.size() == 1) {
        winnerMessage += "Winner: " + winners.get(0) + " with score: " + maxScore;
    } else {
        winnerMessage += "Winners: " + String.join(", ", winners) + " with score: " + maxScore;
    }

    // إرسال رسالة النهاية لجميع العملاء
    System.out.println(winnerMessage);
    for (ClientHandler client : clients) {
        client.sendMessage(winnerMessage); // إرسال نتيجة النهاية لكل لاعب
    }
}
public static void startTimer() {
    final int[] timeRemaining = {30};
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            timeRemaining[0]--;
            System.out.println("Time remaining: " + timeRemaining[0] + " seconds");

            // Check if time is up
            if (timeRemaining[0] <= 0) {
                timer.cancel();
                System.out.println("Waiting Time's up! Starting the game.");
                broadcastQuestion();
            }
            
            // Check if players have reached 3, then stop the timer
            if (playingClients.size() >= 3) {
                timer.cancel();
                System.out.println("3 players joined. Starting the game immediately.");
                broadcastQuestion();
            }
        }
    }, 0, 1000);
}

public static void stopTimer() {
    System.out.println("Game stopped.");
}
}

    
