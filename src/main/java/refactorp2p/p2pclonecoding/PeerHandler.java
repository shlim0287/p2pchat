package refactorp2p.p2pclonecoding;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PeerHandler extends Thread {
    private Socket socket;
    private Set<String> processedMessages; // 처리된 메시지를 저장하는 Set
    private boolean isSocketClosed;


    public PeerHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader peerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!isSocketClosed) {
                try {
                    String message = peerReader.readLine();
                    if (message == null) {
                        // 소켓이 닫혔음을 알림
                        isSocketClosed = true;
                        System.out.println("Connection closed by peer.");
                        break;
                    } else {
                        handleMessages(message);
                        //System.out.println(message);
                    }
                } catch (IOException e) {
//                    System.out.println("1");
                    //System.out.println("채팅 종료");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleMessages(String message) {
        try {
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
            if (jsonObject.containsKey("username")) {
                System.out.println("[" + jsonObject.getString("username") + "]: " + jsonObject.getString("message"));
            }
        } catch (JsonException e) {
            interrupt();
        }
    }

    public void sendMessage(Set<Socket> uniquePeers, String message)
    {
        for (Socket socket : uniquePeers) {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}