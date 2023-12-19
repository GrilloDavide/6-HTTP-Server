package it.fi.meucci;

import java.io.IOException;
import java.net.ServerSocket;

public class App 
{
    public static void main( String[] args ) throws IOException{

        HttpServer server = new HttpServer(new ServerSocket(8000));
        server.start();
    }
}
