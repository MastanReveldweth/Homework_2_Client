package tarea;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request
{
	private String Method;
	private String Url;
	/*
	//Podriamos usarlas despues
	private String HTTPver;
	private String Host;
	private String UserAgent; //User-Agent
	private String Accept;
	private String AcceptLanguage; //Accept-Language
	private String AcceptEncoding; //Accept-Encoding
	private String Referer;
	private String Connection;
	private String ContentType; //Content-Type
	*/
	private String ContentLength; //Content-Length
	private String Content;
	private String Str;
	private String Boundary;
	private Boolean isPost;
	private Boolean isData;
	
	public Request()
	{
		isPost = false;
		isData = false;
	}
	
	public String getMethod()
	{
		return Method;
	}
	
	public String getUrl()
	{
		return Url;
	}
	
	public String getContent()
	{
		if(Content==null)
		{
			return "";
		}
		else
		{
			return Content;
		}
	}
	
	public String getString()
	{
		return Str;
	}
	
	public void readInput(InputStream is) 
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        
        try
        {
            String current;
            String request = "";
            while (!(current = in.readLine()).isEmpty())
            {
                request += current + System.getProperty("line.separator"); 
                
                if(current.startsWith("POST"))
                {
                	isPost = true;
                }
                //aqui se rellenarian las otras como Host o User-Agent
                
                if(isPost)
                {
                	if(current.startsWith("Content-Length: "))
                    {
                		ContentLength = current.substring("Content-Length: ".length());
                    }
                	
                	if(current.startsWith("Content-Type: multipart/form-data"))
                	{
                		isData = true;
                		Boundary = current.substring("Content-Type: multipart/form-data; boundary=".length());
                	}
                }
            }
            
            
            if (isPost && !isData) 
            {
            	StringBuilder body = new StringBuilder();
                int c = 0;
                for (int i = 0; i < Integer.parseInt(ContentLength); i++) 
                {
                    c = in.read();
                    body.append((char) c);
                }
                
                Content = body.toString();
            }else if (isPost && isData)
            {
            	/*
            	Content = "";
            	do
                {
            		Content += current + System.getProperty("line.separator"); 
            		//System.out.println(Content);
                }while (!(current = in.readLine()).startsWith("Content-Type:"));
            	//System.out.print(Content);
            	*/
            	//in.close();
            	
            	Content = "";
            	StringBuilder body = new StringBuilder();
                int c = 0;
                for (int i = 0; i < Integer.parseInt(ContentLength); i++) 
                {
                    c = in.read();
                    body.append((char) c);
                }
                
                Content = body.toString();
                System.out.println(Content);
                
                
            	/*
            	BufferedInputStream bin = new BufferedInputStream(is);

            	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("d.png"));
            	int data;
           	    while ((data = bin.read()) != -1)
           	    {
            		System.out.println(data);
            	    bos.write(data);
            	}
            	*/
                //File file = new File("ads.png");
                
                
            	
            }else
            {
            	
            }
            
            Url = request.substring(5, request.indexOf("H") - 1);
            Method = request.substring(0, request.indexOf("/") - 1);
            Str = request;
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
