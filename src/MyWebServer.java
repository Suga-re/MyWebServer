package src;
import java.io.*;
import java.net.*;

public class MyWebServer {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(port);

        String rootPath = args[1];
        System.out.println("server is running on port "+ port);

        String clientSentence;


        while(true){
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected to server");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            while((clientSentence = inFromClient.readLine())!=null){
                System.out.println(clientSentence);
                if(clientSentence.isEmpty()){
                    break;
                }
            };

            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            outToClient.write("Hello World".getBytes());
//            connectionSocket.close();
        }

    }
}