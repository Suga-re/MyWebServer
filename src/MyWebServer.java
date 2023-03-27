package src;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyWebServer {
/*
Variables
 */
    static String path;
    static File f;
    static String statusCode;

    static Date date;
    static String serverName="Johnny's Server";
    static long lastModified;
    static long contentLength=0;

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(port);
        System.out.println("server is running on port "+ port);

        String clientSentence;

        while(true){

            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected to server");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


            clientSentence = inFromClient.readLine();
            System.out.println(clientSentence);



           path=fullFilePath(args[1],clientSentence);
           f=new File(path);

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


            if(fileFound)
            {
                statusCode = "HTTP/1.1 200 OK";

                f= new File(path);
                String header= formatHeader(statusCode, f);
                System.out.print(header);
                if (clientSentence.contains("HEAD")){
                    outToClient.writeBytes(header);
                }
                else if (clientSentence.contains("GET")){
                    outToClient.writeBytes(header);
                    sendBytesToClient(fis, outToClient);
                }
            }
            else{
                System.out.print("file not found");
            }
            System.out.print("\n");
           connectionSocket.close();
        }

    }


    private static void sendBytesToClient( FileInputStream fis, DataOutputStream os) throws
            Exception      //send the file byte by byte to client
    {

        int bytes;
//        BufferedReader br = new BufferedReader(new InputStreamReader(fis)); // second parameter is Character set
        byte[] buffer = new byte[(int)contentLength];
        System.out.println("this is content length: "+contentLength);

        while((bytes = fis.read(buffer)) > 0) { //if there is a byte to read then write to output strea,
            os.write(buffer, 0, bytes);
            os.flush();
        }
    }
    /*
    returns path is request is valid and returns 501 error code if not a proper request

     */
    private static String fullFilePath(String rootPath,String clientSentence){
        String path = rootPath;
        /*
            Parse GET request
             */
        String[] request = clientSentence.split(" ", 3);
        if (request[2].equals("HTTP/1.1")){
            String extension=request[1];

            if(extension.equals("/") || extension.equals("/favicon.ico") ){
                path +="/index.html";
                System.out.println(path);
            }
            else{
                path += extension;
                System.out.println(path);
            }
        }

        return(path);
    }
    private static String formatHeader(String statusCode, File f){ //header formatter
        SimpleDateFormat HTTPDateFormat = new SimpleDateFormat("EEE MMM d hh:mm:ss zzz yyyy");

        date = new Date(); //set date to request date and time
        String formatDate= HTTPDateFormat.format(date);
        contentLength=f.length();
        lastModified= f.lastModified();
        String formatLastModified = HTTPDateFormat.format(lastModified);

        String response= statusCode + '\n' +
                "Date: " + formatDate + "\n" +
                "Server: " + serverName + "\n" +
                "Last-Modified:" + formatLastModified + "\n" +
                "Content-Length:" + contentLength +"\n\n";

        return response;

    }

}