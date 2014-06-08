package tarea;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class TCPProtocol
{	
	//MESSAGE Nombre&#&ip&#&NombreContacto&#&IpContacto&#&Msg
	public static String PrepareMsg(String ip, String str)
	{
		
		try
		{
			str = URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			System.out.println("ERROR: Cant Decode Post Contents");
			e.printStackTrace();
		}
		
		String[] stra = str.split("&");

		return "MESSAGE "
				+stra[0].substring("username=".length())
				+"&#&"+ip
				+"&#&"+stra[1].substring("contactname=".length())
				+"&#&"+stra[2].substring("ipcontacto=".length())
				+"&#&"+stra[3].substring("message=".length());
		/*
		this.name = stra[0].substring("name=".length());
		this.ip = stra[1].substring("ip=".length());
		this.port = stra[2].substring("puerto=".length());
		*/
	}
}

