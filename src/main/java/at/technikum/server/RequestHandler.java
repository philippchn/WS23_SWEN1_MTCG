package at.technikum.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler {
    private final Socket socket;
    private final ServerApplication serverApplication;
    private BufferedReader bufferedReader;

    private PrintWriter printWriter;
    RequestHandler(Socket socket, ServerApplication serverApplication)
    {
        this.socket = socket;
        this.serverApplication = serverApplication;
    }

    public void handle() throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println(getHttpStringFromStream(bufferedReader));

        printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.write("HTTP/200/r/n");

        printWriter.close();
        bufferedReader.close();
        socket.close();
    }

    private String getHttpStringFromStream(BufferedReader bufferedReader) throws IOException {
        StringBuilder result = new StringBuilder();

        String inputLine;
        while((inputLine = bufferedReader.readLine()) != null && !inputLine.isEmpty())
        {
            result.append(inputLine).append(System.lineSeparator());
        }

        return result.toString();
    }
}
