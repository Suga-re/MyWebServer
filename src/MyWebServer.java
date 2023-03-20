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


            clientSentence = inFromClient.readLine();
            System.out.println(clientSentence);


            /*
            Parse GET request
             */
            String[] request = clientSentence.split(" ", 3);
            if (request[0].equals("GET")){
                System.out.println(request.length);
                String extension=request[1];

                if(extension.equals("/")){
                    System.out.println("main page");
                }
                else{
                    System.out.println(request[1]);
                    //get the files and display it for the page
                }
            }





            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
           connectionSocket.close();
        }

    }
}