package tarea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient
{		
	private static int port = 7070;
	private static String ip_server_tcp = "127.0.0.1";
	
    public static String SendMessage(String msg)
    {
    	try 
    	{
            //SocketAddress sockaddress = new InetSocketAddress(ip_server_tcp, port);
            //Socket client = new Socket();
            //int timeoutMs = 2000;
            //client.connect(sockaddress, timeoutMs);
    		Socket client = new Socket(ip_server_tcp, port);
    		
            SendToServer(msg,client.getOutputStream());
            String response = RecieveResponse(client.getInputStream());
            
            client.close();
            return response;
            
        } catch (UnknownHostException e) 
        {
        	System.out.println("ERROR: Unknown Host");
            e.printStackTrace();
            return "ERROR";
        } /*catch (SocketTimeoutException e) 
        {
        	System.out.println("ERROR: Socket timeout");
            e.printStackTrace();
            return "ERROR";
        }*/ catch (IOException e) 
        {
        	System.out.println("ERROR: Cannot listen on socket");
            e.printStackTrace();
            return "ERROR";
        }
    }
    
    private static void SendToServer(String msg, OutputStream out)
	{
		try 
        {    
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(msg+"\n\n");
            System.out.println("LOG: Message Sent ("+msg+")");
            writer.flush();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
	}
	
	private static String RecieveResponse(InputStream in)
	{
		String response = "";
		try 
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String current;
            while ((current = reader.readLine()) != null) 
            {
                response += current;
            }
            reader.close();
            
        } catch (IOException e)
        {
            e.printStackTrace();
        }
		
		return response;
	}
}
