import java.io.*;
import java.net.*;


public class ServerConnection implements Runnable {
    private Socket client;
    private Client clientFrame;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(Socket s, Client frame) throws IOException {
        this.client = s;
        this.clientFrame = frame;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override

public void run() {
    try {
        String serverResponse;
        while ((serverResponse = in.readLine()) != null) {
            System.out.println("Received from server: " + serverResponse);
            if (serverResponse.startsWith("Connected users: ")) {
                    clientFrame.updateConnectedPlayers(serverResponse.substring(17));
            }
                // Handling currently playing clients update
            if (serverResponse.startsWith("PLAYING:")) {
                clientFrame.updatePlayingClients(serverResponse.substring(8));
            }
            if (serverResponse.startsWith("SCORES:")) {
                clientFrame.updateScores(serverResponse.substring(7));
            }if (serverResponse.equals("ROOM_FULL")) {
                clientFrame.displayRoomFullMessage();
            }
            
            else if (serverResponse.startsWith("QUESTION:")) {
                clientFrame.displayQuestion(serverResponse.substring(9));
            } else if (serverResponse.startsWith("GAME_OVER:")) {
                clientFrame.displayGameOver(serverResponse.substring(10));
            } else if (serverResponse.startsWith("PLAYERS:")) {
                clientFrame.updateConnectedPlayers(serverResponse.substring(8));
            } else if (serverResponse.equals("WRONG_ANSWER")) {
                clientFrame.showWrongAnswer();
            }
            
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {

    }

}}
