package refactorp2p.p2pclonecoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PeerHandler implements Runnable {
    private Socket socket;
    private Set<String> processedMessages; // 처리된 메시지를 저장하는 Set
    private boolean isSocketClosed;

    public PeerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.isSocketClosed=false;
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
                    } else {
                        System.out.println(message);
                    }
                } catch (IOException e) {
//                    System.out.println("1");
                    System.out.println("채팅 종료");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleMessages(Socket socket) {
        try {
            BufferedReader peerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = "";
            while (true) {
                try {
                    message = peerReader.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(message);
                }
                catch (IOException e) {
//                    System.out.println("2");
                    break;
                }
            }
        } catch (IOException e) {
//            System.out.println("3");
            e.printStackTrace();
        }
    }

    public void sendMessage(Set<Socket> uniquePeers, String message)
    {
        for (Socket socket : uniquePeers) {
            try {
                if(!socket.isClosed()){
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(message);
                }
            } catch (IOException e) {
//                System.out.println("4");
                e.printStackTrace();
            }
        }
    }
}