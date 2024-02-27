package p2pclonecoding;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private Set<ServerThreadThread> clientThreads = new HashSet<>();

    public ServerThread(String portNumber) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(portNumber));
    }

    public void run() {
        try {
            while (true) {
                ServerThreadThread serverThreadThread = new ServerThreadThread(serverSocket.accept(), this);
                clientThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        try {
            clientThreads.forEach(t -> t.getPrintWriter().println(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<ServerThreadThread> getClientThreads() {
        return clientThreads;
    }
}