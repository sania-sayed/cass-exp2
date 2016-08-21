package tcpclients;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class TCPClients {

    byte[] skey = new byte[1000];
    String skeyString;
    static byte[] raw;
    static String encryptedData;
    String decryptedMessage;
    static String inputMessage;
    private byte[] ebyte;
    static int ch;
    static String[] data = new String[10];

    public TCPClients(String inputMessage2)
    {
	inputMessage = inputMessage2;
    }

    public String Encryption() throws Exception
    {
        
        int i=0;
        try
	{
         
            generateSymmetricKey();

            byte[] ibyte = inputMessage.getBytes();
            byte[] ebyte=encrypt(raw, ibyte);
            String encryptedData = new String(ebyte);
            data[i]=encryptedData;
            i++;
            System.out.println("Encrypted message "+encryptedData);


        
            byte[] dbyte= decrypt(raw,ebyte);
            String decryptedMessage = new String(dbyte);
            data[i]=decryptedMessage;
            System.out.println("Decrypted message "+decryptedMessage);

            return encryptedData;
            //return decryptedMessage;
        }
        catch(Exception e)
        {
            System.out.println(e);
	}
	return null;
    }

    void generateSymmetricKey()
    {
	try
	{
            Random r = new Random();
            int num = r.nextInt(10000);
            String knum = String.valueOf(num);
            byte[] knumb = knum.getBytes();
            skey=getRawKey(knumb);
            skeyString = new String(skey);
            System.out.println("DES Symmetric key = "+skeyString);
	}
	catch(Exception e)
	{
            System.out.println(e);
	}
    }

    private static byte[] getRawKey(byte[] seed) throws Exception
    {
	KeyGenerator kgen = KeyGenerator.getInstance("DES");
	SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	sr.setSeed(seed);
	kgen.init(56, sr);
	SecretKey skey = kgen.generateKey();
	raw = skey.getEncoded();
	return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception
    {
	SecretKeySpec skeySpec = new SecretKeySpec(raw, "DES");
	Cipher cipher = Cipher.getInstance("DES");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	byte[] encrypted = cipher.doFinal(clear);
	return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception
    {
	SecretKeySpec skeySpec = new SecretKeySpec(raw, "DES");
	Cipher cipher = Cipher.getInstance("DES");
	cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	byte[] decrypted = cipher.doFinal(encrypted);
	return decrypted;
    }

    public static void main(String[] args) throws Exception{
        // TODO code application logic here

        String encryptedMessage;
        int i=0;
        Scanner sc = new Scanner(System.in);

        //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 6789);

        BufferedReader in = new BufferedReader(new FileReader("message"));
        PrintWriter out = new PrintWriter(new FileWriter("encryption"));

        BufferedReader read = new BufferedReader(new FileReader("message"));
        PrintWriter write = new PrintWriter(new FileWriter("decryption"));


        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        

        do
        {
            System.out.println("1. Upload");
            System.out.println("2. Download");
            System.out.println("3.Exit");
            System.out.println("Enter Your Choice: ");

            ch = sc.nextInt();
            switch(ch)
            {
                case 1:
                        try
                        {
                            while((inputMessage = in.readLine())!=null)
                            {
                                outToServer.writeBytes(inputMessage + '\n');
                                TCPClients des = new TCPClients(inputMessage);
                                encryptedMessage = des.Encryption();
                                out.println(data[i]);
                            }                            
                        }

                        finally
                        {
                            in.close();
                            out.close();
                        }
                    break;

                case 2:
                        i++;
                        try
                        {
                            /*while((inputMessage = read.readLine())!=null)
                            {
                                outToServer.writeBytes(inputMessage + '\n');                            
                                String decryptedMessage = inFromServer.readLine();                                
                                TCPClients des = new TCPClients(inputMessage);                                
                                String decryptedData = des.Encryption();   */
                                write.println(data[i]);
                           // }
                        }

                        finally
                        {
                            //read.close();
                            write.close();
                        }

                    break;

                case 3: System.out.println("DONE");
                    break;

                default: System.out.println("INVALID CHOICE");
            }
        }while(ch!=3);
        clientSocket.close();
    }
}