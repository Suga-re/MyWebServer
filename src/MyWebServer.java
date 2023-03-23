package src;
import java.io.*;
import java.net.*;

public class MyWebServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(port);

        String path = args[1];
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
            if (request[0].equals("GET") && request[2].equals("HTTP/1.1")){ //For get requests
                System.out.println(request.length);
                String extension=request[1];

                if(extension.equals("/") || extension.equals("/favicon.ico")){
                    path +="/index.html";
                    System.out.println(path);
                }
                else{
                    path += extension;
                    System.out.println(path);
                    //get the files and display it for the page
                }
            }
            else if (request[0].equals("HEAD")){ //for head requests

            }
            else{
                //Insert Error code Command 501 Not Implemented
            }

            FileInputStream fis=null;
            boolean fileFound=true;
            try
            {
                fis = new FileInputStream(path);
            }catch(FileNotFoundException e)
            {
                fileFound=false;
            }




            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//            outToClient.writeBytes(sampleResponse);

            if(fileFound)
            {
                String statusLine = "HTTP/1.0 200 OK\r\n";
                outToClient.writeBytes(statusLine);
                sendBytesToClient(fis, outToClient);
            }
            else{
                System.out.print("file not found");
            }

            path = args[1]; //reset path

           connectionSocket.close();
        }

    }
    //send the file byte by byte to client
    private static void sendBytesToClient( FileInputStream fis, OutputStream os) throws
            Exception
    {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while((bytes = fis.read(buffer)) != -1)
        {
            os.write(buffer, 0, bytes);
        }
    }

    private static String fileType(String fileString){
        return "yes";
    }
}