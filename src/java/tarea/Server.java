package tarea;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server
{
	public static void init(String portStr, String docRoot)
	{
		int port = Integer.parseInt(portStr);
		
		try
		{
			ExecutorService scheduler = Executors.newCachedThreadPool();
	          
	        ServerSocket server = new ServerSocket(port);
	        
	        System.out.println("LOG: Starting Server now... (IPHOST="+getIPAdress()+", HTTP port="+portStr+", docRoot=" + docRoot + ")");
	        while (true)
            {
	        	Socket client = server.accept();
	        	String ipClient = client.getInetAddress().getHostAddress();
	        	if(ipClient.compareTo("127.0.0.1")==0)
	        	{
	        		ipClient = getIPAdress();
	        	}
	        		
	            Runnable r = new HTTPThread(client.getInputStream(), client.getOutputStream(), docRoot, ipClient);
	            scheduler.execute(r);      
	            System.out.println("LOG: New Thread Created");
            }
		}
		catch (IOException e)
	    {
			System.out.println("ERROR: Cannot listen on socket");
	        e.printStackTrace(); 
	    }
	}
	
	
	
	public static String getIPAdress()
	{
		String retorno = "ERROR";
		try
		{
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements())
				{
					InetAddress i = (InetAddress) ee.nextElement();
					if(!i.isLinkLocalAddress())
					{
						if(i.getHostAddress().startsWith("192.168"))
						{
							return i.getHostAddress();
						}
					}
				}
			}
		}catch (IOException e)
	    {
			System.out.println("ERROR: Cannot get Network Interfaces");
	        e.printStackTrace(); 
	    }
		
		return retorno;
	}

}
