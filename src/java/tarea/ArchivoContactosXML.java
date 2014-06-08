package tarea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ArchivoContactosXML
{
	// Metodos para leer
	public static void readXML() 
	{
		try 
		{
			File file = new File("contactos.xml");
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document doc = documentBuilder.parse(file);

			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("contacto");

			System.out.println("Root element : " + doc.getDocumentElement().getNodeName());

			for (int temp = 0; temp < nodeList.getLength(); temp++) 
			{
				Node node = nodeList.item(temp);

				System.out.println("\nElement type :" + node.getNodeName());

				if (node.getNodeType() == Node.ELEMENT_NODE)
				{

					Element contacto = (Element) node;

					System.out.println("Name : " + contacto.getElementsByTagName("name").item(0).getTextContent());
					System.out.println("Ip : " + contacto.getElementsByTagName("ip").item(0).getTextContent());
					System.out.println("Port : " + contacto.getElementsByTagName("port").item(0).getTextContent());
				}
			}
		}
		catch (Exception e) 
		{
		   e.printStackTrace();
		}
	}
	
	public static ArrayList<Contacto> getList() 
	{
		try 
		{
			ArrayList<Contacto> Contactos = new ArrayList<Contacto>();
			
			File file = new File("contactos.xml");
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document doc = documentBuilder.parse(file);

			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("contacto");

			for (int temp = 0; temp < nodeList.getLength(); temp++) 
			{
				Node node = nodeList.item(temp);

				if (node.getNodeType() == Node.ELEMENT_NODE)
				{

					Element e = (Element) node;
					
					Contactos.add(new Contacto(e.getElementsByTagName("name").item(0).getTextContent(),
												e.getElementsByTagName("ip").item(0).getTextContent(),
												e.getElementsByTagName("port").item(0).getTextContent()));
				}
			}
			
			return Contactos;
		}
		catch (Exception e) 
		{
		   e.printStackTrace();
		   return new ArrayList<Contacto>();
		}
	}
	
	
	// Metodos para insertar
	public static void insert(Contacto nuevo)
	{
		File file = new File("contactos.xml");
		if(file.exists() && !file.isDirectory()) 
		{ 
			appendXML(nuevo, file);
		}
		else
		{
			createXML(nuevo, file);
		}
	}
	
	public static void appendXML(Contacto nuevo, File file) 
	{
		try 
		{   
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            
            Document document = documentBuilder.parse(file);
            // root element
            Element rootElement = document.getDocumentElement();
            
            // name element
            Element name = document.createElement("name");
            //Text value = document.createTextNode(nuevo.name);
            name.setTextContent(nuevo.name);
            
            // ip element
            Element ip = document.createElement("ip");
            //Text value = document.createTextNode(nuevo.ip);
            ip.setTextContent(nuevo.ip);
            
            // ip element
            Element port = document.createElement("port");
            //Text value = document.createTextNode(nuevo.port);
            port.setTextContent(nuevo.port);
            
            // contacto element
            Element contacto = document.createElement("contacto");
            // append childs
            contacto.appendChild(name);
            contacto.appendChild(ip);
            contacto.appendChild(port);
            
            //append contacto to contactos element
            rootElement.appendChild(contacto);
            cleanEmptyTextNodes(rootElement);
            
            document.replaceChild(rootElement, rootElement);
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			
            Source source = new DOMSource(document);
            Result result = new StreamResult(file);
            transformer.transform(source, result);
		 
		} 
		catch (SAXException ex) 
		{
			ex.printStackTrace();
		}
		catch (ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		}
		catch (TransformerException tfe) 
		{
			tfe.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void createXML(Contacto nuevo, File file) 
	{
		try 
		{
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("contactos");
			document.appendChild(rootElement);

			// define contactos elements
			Element contacto = document.createElement("contacto");
			rootElement.appendChild(contacto);

			// name elements
			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(nuevo.name));
			contacto.appendChild(name);

			// ip elements
			Element lastname = document.createElement("ip");
			lastname.appendChild(document.createTextNode(nuevo.ip));
			contacto.appendChild(lastname);

			// port elements
			Element port = document.createElement("port");
			port.appendChild(document.createTextNode(nuevo.port));
			contacto.appendChild(port);

			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(file);
			
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			transformer.transform(domSource, streamResult);
		} 
		catch (ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		}
		catch (TransformerException tfe) 
		{
			tfe.printStackTrace();
		}
	}
	
	
	// Metodos para que el archivo xml este bien identado
	private static void cleanEmptyTextNodes(Node parentNode) 
	{
        boolean removeEmptyTextNodes = false;
        Node childNode = parentNode.getFirstChild();
        while (childNode != null) 
        {
            removeEmptyTextNodes |= checkNodeTypes(childNode);
            childNode = childNode.getNextSibling();
        }

        if (removeEmptyTextNodes) 
        {
            removeEmptyTextNodes(parentNode);
        }
    }
	
	private static boolean checkNodeTypes(Node childNode) 
	{
        short nodeType = childNode.getNodeType();

        if (nodeType == Node.ELEMENT_NODE) 
        {
            cleanEmptyTextNodes(childNode); // recurse into subtree
        }

        if (nodeType == Node.ELEMENT_NODE || nodeType == Node.CDATA_SECTION_NODE || nodeType == Node.COMMENT_NODE) 
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
	
	private static void removeEmptyTextNodes(Node parentNode) 
	{
        Node childNode = parentNode.getFirstChild();
        while (childNode != null) 
        {
            // grab the "nextSibling" before the child node is removed
            Node nextChild = childNode.getNextSibling();

            short nodeType = childNode.getNodeType();
            if (nodeType == Node.TEXT_NODE) 
            {
                boolean containsOnlyWhitespace = childNode.getNodeValue().trim().isEmpty();
                if (containsOnlyWhitespace) 
                {
                    parentNode.removeChild(childNode);
                }
            }
            childNode = nextChild;
        }
    }
	
}