package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class HttpServer {
    
    ServerSocket socket;
    Socket client;
    DataOutputStream outStream;
    BufferedReader inStream;


    public HttpServer(ServerSocket socket){
        this.socket = socket;
        
    }


    public void start() throws IOException{
        while(true){
            client = socket.accept();
            inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outStream = new DataOutputStream(client.getOutputStream());
            communicate();
        }
        
    }

    private void communicate() throws IOException{

        
            String request = inStream.readLine();
            System.out.println(request);
            
            try{
                if(request.split(" ")[0].contains("GET")){
                    String path = request.split("\r\n")[0].substring(5).split(" ")[0]+"/";
                    if(getBody(path) == null)
                        sendError();
                    else 
                        sendPage(path);
                }   
            }catch(Exception e){

            }
            
    }

    private void sendPage(String pageRequest) throws IOException{
        
        String contentBody = getBody(pageRequest);
        outStream.write("HTTP/1.0 200 OK\r\n".getBytes());
        outStream.write("Content-Type: text/html\r\n".getBytes());
        outStream.write(("Content-Length:"+ Integer.toString(contentBody.getBytes().length) + "\r\n").getBytes());
        outStream.write("\r\n".getBytes());
        outStream.write((contentBody + "\r\n").getBytes());
        outStream.write("\r\n".getBytes());
        
        outStream.close();
        inStream.close();
        
    }

    private void sendError() throws IOException{
        String contentBody = getBody("error");
        outStream.write("HTTP/1.0 404 Page not found\r\n".getBytes());
        outStream.write("Content-Type: text/html\r\n".getBytes());
        outStream.write(("Content-Length:"+ Integer.toString(contentBody.getBytes().length) + "\r\n").getBytes());
        outStream.write("\r\n".getBytes());
        outStream.write((contentBody + "\r\n").getBytes());

        
    }

    private String getBody(String bodyPath) throws IOException{
        String body;
        try{
             body = Files.readString(new File("httpprova/src/main/java/it/fi/meucci/"+bodyPath+"/index.html").toPath());
        }catch(Exception e){
            return null;
        }
        return body;

    }
}
