package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            (new ServerThread(client)).start();
            
        }
        
    }

    
}
