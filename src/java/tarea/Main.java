package tarea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
	public static boolean entrar = true;
	public static String portStr = "7080";
	public static String docRoot = "src/Docs/";
	public static String nombre;
	public static String direccion_ip;
	public static String puerto;
	
	public static void main(String[] args)
    {
		Server server = new Server();
		server.init(portStr, docRoot);
    }
}
