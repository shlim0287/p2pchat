package refactorp2p.p2pclonecoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputHandler {
    private BufferedReader bufferedReader;

    public UserInputHandler() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getUserInput() throws IOException {
        return bufferedReader.readLine();
    }

    public void close() throws IOException {
        bufferedReader.close();
    }
}