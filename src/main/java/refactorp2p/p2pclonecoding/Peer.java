package refactorp2p.p2pclonecoding;

import javax.json.Json;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

public class Peer {
    public static void main(String[] args) {
        try {
            UserInputHandler userInputHandler = new UserInputHandler();
            System.out.println("> enter username & port # for this peer");
            String[] setupValues = userInputHandler.getUserInput().split(" ");

            NetworkCommunication networkCommunication = new NetworkCommunication(setupValues[1]);
            networkCommunication.start();

            connectToPeers(userInputHandler, networkCommunication);

            communicate(userInputHandler, setupValues[0], networkCommunication);

            userInputHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connectToPeers(UserInputHandler userInputHandler, NetworkCommunication networkCommunication) throws IOException {
        System.out.println("> enter (space separated) hostname:port#");
        System.out.println("peers to receive messages from (s to skip):");
        String input = userInputHandler.getUserInput();
        if (!input.equals("s")) {
            String[] inputValues = input.split(" ");
            for (String address : inputValues) {
                String[] addressSplit = address.split(":");
                String hostname = addressSplit[0];
                int portNumber = Integer.parseInt(addressSplit[1]);
                // 이미 연결된 소켓인지 확인
                boolean isConnected = false;
                for (Socket socket : networkCommunication.getConnectedPeers()) {
                    if (socket.getInetAddress().getHostName().equals(hostname) && socket.getPort() == portNumber) {
                        isConnected = true;
                        break;
                    }
                }
                if (!isConnected) {
                    networkCommunication.connectToPeer(hostname, portNumber);
                } else {
                    System.out.println("Already connected to peer: " + hostname + ":" + portNumber);
                }
            }
        }
    }

    public static void communicate(UserInputHandler userInputHandler, String username, NetworkCommunication networkCommunication) throws IOException {
        System.out.println("> you can now communicate (e to exit, c to change)");
        boolean flag = true;
        while (flag) {
            String message = userInputHandler.getUserInput();
            if (message.equals("e")) {
                flag = false;
                break;
            } else if (message.equals("c")) {
                connectToPeers(userInputHandler, networkCommunication);
            } else {
                StringWriter stringWriter = new StringWriter();
                Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                        .add("username", username)
                        .add("message", message)
                        .build());
                networkCommunication.sendMessage(stringWriter.toString());
            }
        }
        networkCommunication.close();
    }
}