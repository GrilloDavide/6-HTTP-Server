package it.fi.meucci;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException{

        HttpServer server = new HttpServer(new ServerSocket(8000));
        server.start();
    }
}
