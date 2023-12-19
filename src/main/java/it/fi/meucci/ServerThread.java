package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

public class ServerThread extends Thread {

    DataOutputStream outStream;
    BufferedReader inStream;

    public ServerThread(Socket client) throws IOException {
        outStream = new DataOutputStream(client.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));

    }

    @Override
    public void run() {
        String request = "";
        try {
            request = inStream.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(request);


        if (request.split(" ")[0].contains("GET")) {
            String path = request.split(" ")[1].split(" ")[0];
            System.out.println(path);

            if (checkIfRequestIsFile(path)) {
                String fileName = getFileName(path).trim();
                String extension = fileName.split("\\.")[1].trim();
                String contentType = "";
                switch (extension) {
                    case "html", "scss", "css", "js":
                        try {
                            send(extension, "text", Objects.requireNonNull(getBodyText(path)));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "png", "jpeg", "jpg", "ico":
                        try {
                            send(extension, "image", Objects.requireNonNull(getBodyText(path)));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        try {
                            sendError();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }
            } else {
                try {
                    byte[] body = getBodyText(path.trim() + "/index.html");
                    if (body == null)
                        sendError();
                    else
                        send("html", "text", body);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void send(String extension, String contentType, byte[] body) throws IOException {

        outStream.write("HTTP/1.1 200 OK\r\n".getBytes());
        outStream.write(("Content-Type: " + contentType + "/" + extension + "\r\n").getBytes());
        outStream.write(("Content-Length:" + body.length + "\r\n").getBytes());
        outStream.write("\r\n".getBytes());
        outStream.write(body);
        outStream.write("\r\n".getBytes());

        outStream.close();
        inStream.close();

    }

    private boolean checkIfRequestIsFile(String path) {
        String[] strings = path.split("/");
        try {
            return strings[strings.length - 1].split("\\.")[1] != null;
        } catch (Exception e) {
            return false;
        }
    }

    private String getFileName(String path) {
        String[] strings = path.split("/");
        return strings[strings.length - 1];
    }


    private void sendError() throws IOException {
        byte[] contentBody = getBodyText("error/index.html");
        outStream.write("HTTP/1.0 404 Page not found\r\n".getBytes());
        outStream.write("Content-Type: text/html\r\n".getBytes());
        assert contentBody != null;
        outStream.write(("Content-Length:" + contentBody.length + "\r\n").getBytes());
        outStream.write("\r\n".getBytes());
        outStream.write(contentBody);
        outStream.write("\r\n".getBytes());


    }

    private byte[] getBodyText(String bodyPath) throws IOException {
        byte[] body;
        try {
            body = Files.readAllBytes(new File("src/main/java/it/fi/meucci/" + bodyPath.trim()).toPath());
        } catch (Exception e) {
            return null;
        }
        return body;

    }
}
