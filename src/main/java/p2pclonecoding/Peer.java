package p2pclonecoding;



import javax.json.Json;
import java.io.*;
import java.net.Socket;

public class Peer {
    public static void main(String[] args) throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> 유저이름과 포트 넘버를 입력해주세요.");
        String[] setupValues = bf.readLine().split(" ");
        ServerThread serverThread = new ServerThread(setupValues[1]);
        serverThread.start();
        new Peer().updateListenToPeers(bf, setupValues[0], serverThread);
    }

    public void updateListenToPeers(BufferedReader bf, String username, ServerThread serverThread) throws Exception {
        System.out.println("> (space separated) hostname : port #");
        System.out.println("peers to receive messages from (s to skip)");
        String input = bf.readLine();
        String[] inputValues = input.split(" ");
        if (!input.equals("s")) {
            for (String inputValue : inputValues) {
                String[] address = inputValue.split(":");
                Socket socket = null;
                try {
                    socket = new Socket(address[0], Integer.valueOf(address[1]));
                    new PeerThread(socket).start();
                } catch (Exception e) {
                    if (socket != null) {
                        socket.close();
                    } else {
                        System.out.println("유효하지않은 입력입니다. skipping to next step.");
                    }
                }
                communicate(bf, username, serverThread);
            }
        }
    }

    public void communicate(BufferedReader bf, String username, ServerThread serverThread) {
        try {
            System.out.println("> 이제부터 채팅을 할 수 있습니다 :) 채팅방을 나가려면 e, 바꾸려면 c를 누르세요");
            boolean flag = true;
            while (flag) {
                String message = bf.readLine();
                if (message.equals("e")) {
                    flag = false;
                    break;
                } else if (message.equals("c")) {
                    updateListenToPeers(bf, username, serverThread);
                } else {
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                            .add("username", username)
                            .add("message", message)
                            .build());
                    serverThread.sendMessage(stringWriter.toString());
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
