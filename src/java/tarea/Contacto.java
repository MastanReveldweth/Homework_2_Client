package tarea;

public class Contacto
{
	String name;
	String ip;
	String port;
	
	public Contacto(String str)
	{
		String[] stra = str.split("&");
		this.name = stra[0].substring("name=".length());
		this.ip = stra[1].substring("ip=".length());
		this.port = stra[2].substring("puerto=".length());
	}
	
	public Contacto(String name, String ip, String port)
	{
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public String toString()
	{
		return "Name: " + this.name + " IP: " + this.ip + " Port: " + this.port;
	}
}
