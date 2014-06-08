package tarea;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HTTPThread implements Runnable 
{
    InputStream in;
    OutputStream out;
    String docRoot;
    String ipClient;

    private String fileLoc;
    
    HTTPThread(InputStream in, OutputStream out, String docRoot, String ipClient) 
    {
        this.in = in;
        this.out = out;
        this.docRoot = docRoot;
        this.ipClient = ipClient;
    }

    public void run() 
    {
        try 
        {
            //Muestra Request request y Lo almacena
            System.out.println("LOG: Preparing to read request");
            Request request = new Request();
            
            request.readInput(in);
            
            //request.readAllInput(in);
            
            //System.out.println(request.getString());
            
            System.out.println("LOG: Request read successfully!\n");
            //System.out.println(request.getString());
            
            if(request.getMethod().compareTo("GET")==0)
            {
            	System.out.println("LOG: Process GET\n");
            	
            	if(request.getUrl().compareTo("chat.html")==0)
            	{
            		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
            		String response = TCPClient.SendMessage("REQUEST CHATLOG "+this.ipClient);
            		//System.out.println(response);
            		response = response.replace("[<--nextline-->]", "<br/>");
            		writer.print(response);
            		writer.close();
            	}else if(request.getUrl().compareTo("contacto.html")==0)
            	{
            		ArrayList<Contacto> Lista = ArchivoContactosXML.getList();
            		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        			for(Contacto c : Lista)
        			{
        				writer.print("<li>"+c.name+"::"+c.ip+"</li>\n");
        				System.out.println("LOG: Response Sent ("+request.getUrl()+")\n");
        			}
        			writer.close();
            	}else  	
            	{
            		//Conseguir el nombre del Archivo del Request
            		fileLoc = docRoot + request.getUrl();
            		//System.out.println(fileLoc);
            		File fileexist = new File(fileLoc);
            		if(fileexist.exists() && !fileexist.isDirectory()) 
            		{ 
            			PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));

                		//Enviar Response y Body del Html
                		if(request.getUrl().compareTo("listacontactos.html")==0)
                		{
                			ArrayList<Contacto> Lista = ArchivoContactosXML.getList();
                    	
                			if(Lista.isEmpty())
                			{
                				writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                				writer.print(fileReader(fileLoc));
                				System.out.println("LOG: Response Sent ("+request.getUrl()+")\n");
                			}
                			else
                			{
                				String[] file = fileReader(fileLoc).split("<!--LIST-->");
                				writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                				writer.print(file[0]);
                				writer.print("<!--LIST-->\n");
                				for(Contacto c : Lista)
                				{
                					writer.print("<li>"+c.name+"::"+c.ip+"</li>\n");
                				}
                				writer.print(file[1]);
                				System.out.println("LOG: Response Sent ("+request.getUrl()+")\n");
                			}
                		}
                		else
                		{
                			writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                			writer.print(fileReader(fileLoc));
                			System.out.println("LOG: Response Sent ("+request.getUrl()+")\n");
                		}
                	
                		writer.close();
            		}
            		else
                	{
                		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                		writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                		System.out.println("LOG: Response Sent ("+request.getUrl()+") Not Found\n");
                		writer.close();
                	}
            	}
            }
            if(request.getMethod().compareTo("POST")==0)
            {
            	System.out.println(request.getUrl());
            	String cadenaPost;
            	if(request.getUrl().compareTo("/formulario.html")==0)
            	{
            		System.out.println("LOG: Process POST");
                	System.out.println("LOG: Content ("+request.getContent()+")\n");
                	cadenaPost = request.getContent();
                	ArchivoContactosXML.insert(new Contacto(cadenaPost));
                	String Msg = "Contacto Agregado";
                	
                	//Conseguir el nombre del Archivo del Request
                    fileLoc = docRoot + request.getUrl();
                    //System.out.println(fileLoc);
                    
                    String[] file = fileReader(fileLoc).split("<!--END-->");
                    
                	PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                	
                    //Enviar Response
                    writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                    writer.print(file[0]);
                    writer.print("<!--END-->");
                    writer.print("<script type=\"text/javascript\">alert('" + Msg + "');</script>");
                    writer.print(file[1]);
                    writer.close();
            	}else if(request.getUrl().compareTo("/mensajeria.html")==0)
            	{
            		System.out.println("LOG: Process POST");
                	System.out.println("LOG: Content ("+request.getContent()+")\n");
                	
                	System.out.println("LOG: TCP Server Response ("+TCPClient.SendMessage(TCPProtocol.PrepareMsg(ipClient,request.getContent()))+")");
                    
                    //Conseguir el nombre del Archivo del Request
                    fileLoc = docRoot + request.getUrl();
                    //System.out.println(fileLoc);
                    
                    String Msg = "Mensaje Enviado";
                    
                    String[] file = fileReader(fileLoc).split("<!--END-->");
                    
                	PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                	
                    //Enviar Response
                    writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                    writer.print(file[0]);
                    writer.print("<!--END-->");
                    writer.print("<script type=\"text/javascript\">alert('" + Msg + "');</script>");
                    writer.print(file[1]);
                    writer.close();
            	}
            	else if(request.getUrl().compareTo("/upload.html")==0)
            	{
            		System.out.println("LOG: Process POST");
                	//System.out.println("LOG: Content ("+request.getContent()+")\n");
                	//System.out.println("LOG: Content ("+request.getString()+")\n");
                	//System.out.println("LOG: TCP Server Response ("+TCPClient.SendMessage(TCPProtocol.PrepareMsg(ipClient,request.getContent()))+")");
                    
                    //Conseguir el nombre del Archivo del Request
                    //fileLoc = docRoot + request.getUrl();
                    //System.out.println(fileLoc);
                    
                    //String Msg = "Mensaje Enviado";
                    
                    //String[] file = fileReader(fileLoc).split("<!--END-->");
                    
                	PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                	
                    //Enviar Response
                    writer.print("HTTP/1.0 200 OK\nContent-Type: text/html\n\n");
                    //writer.print(file[0]);
                    //writer.print("<!--END-->");
                    //writer.print("<script type=\"text/javascript\">alert('" + Msg + "');</script>");
                    //writer.print(file[1]);
                    writer.close();
            	}
            	
            }
            
            out.close();
            in.close();
        }

        catch (IOException e) 
        {
            System.out.println("ERROR: Could not listen on socket");
            System.exit(-1);
        }

    }
    
    //Retorna Contenidos de la pagina como un String (html)
    public String fileReader(String file) 
    {

        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner;
        try 
        {
            scanner = new Scanner(new FileInputStream(file));
            while (scanner.hasNextLine()) 
            {
                text.append(scanner.nextLine() + NL);
            }

            return text.toString();
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("ERROR: File Not Found "+file);
            System.exit(-1);
            return null;
        }
    }
}
