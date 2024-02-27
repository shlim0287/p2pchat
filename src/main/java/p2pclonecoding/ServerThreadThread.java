package p2pclonecoding;



import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;
    private PrintWriter printWriter;

    public ServerThreadThread(Socket socket, ServerThread serverThread) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String message = bf.readLine();
                if (message != null) {
                    serverThread.sendMessage(message);
                }
            }
        } catch (Exception e) {
            serverThread.getClientThreads().remove(this);
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}