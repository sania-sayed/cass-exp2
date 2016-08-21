package tcpserver;
import java.net.*;
import java.io.*;


public class TCPServer
{
 
    public static void main(String[] args) throws Exception
    {
        // TODO code application logic here

        String clientLine;
	ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server Connection");

        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            
            String inputMessage = inFromClient.readLine();
            if(inputMessage.endsWith(".txt"))
            {
                BufferedReader br = new BufferedReader(new FileReader(inputMessage));
                String input=br.readLine();
                outToClient.writeBytes(input + '\n');
            }
            
            else
            {
                File file=new File("server");
                if(!file.exists())
                {
                     file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(inputMessage);
                bw.close();
            }
        }

    }
}