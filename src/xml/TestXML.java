package xml;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import logging.MyLogger;
import pojos.Pedido;
import pojos.Tienda;
import pojos.Usuario;

public class TestXML {
	final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) throws JAXBException, IOException {
		MyLogger.setupFromFile();
		
		Tienda tienda = new Tienda(1, "Pozuelo", "8:00-14:00");
		
		//marshallingTienda(tienda);
		//unMarshallingTienda();
		
		Usuario usuario = new Usuario()
		Pedido pedido = new Pedido (1, "2021-06-15 13:44", true, tienda, usuario);
		//marshallingPedido(pedido);
		//unMarshallingPedido();
	}
	
	private static void marshallingTienda(Tienda t) throws JAXBException {
		// Creamos el JAXBContext
		JAXBContext jaxbC = JAXBContext.newInstance(Tienda.class);
		// Creamos el JAXBMarshaller
		Marshaller jaxbM = jaxbC.createMarshaller();
		// Formateo bonito
		jaxbM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		jaxbM.setProperty("com.sun.xml.bind.xmlHeaders", "\n<!DOCTYPE tienda SYSTEM \"tienda.dtd\">");
		jaxbM.setProperty("com.sun.xml.bind.xmlDeclaration", false);
		// Escribiendo en un fichero
		File XMLfile = new File("./xml/Tienda.xml");
		jaxbM.marshal(t, XMLfile);
		// Escribiendo por pantalla
		jaxbM.marshal(t, System.out);
	}
	
	private static void unMarshallingTienda() throws JAXBException {
		// Creamos el JAXBContext
		JAXBContext jaxbC = JAXBContext.newInstance(Tienda.class);
		// Creamos el JAXBMarshaller
		Unmarshaller jaxbU = jaxbC.createUnmarshaller();
		// Leyendo un fichero
		File XMLfile = new File("./xml/Tienda.xml");
		// Creando el objeto
		Tienda t = (Tienda) jaxbU.unmarshal(XMLfile);
		// Escribiendo por pantalla el objeto
		System.out.println(t);
	}
	
	private static void marshallingPedidos(Pedido t) throws JAXBException {
		// Creamos el JAXBContext
		JAXBContext jaxbC = JAXBContext.newInstance(Pedido.class);
		// Creamos el JAXBMarshaller
		Marshaller jaxbM = jaxbC.createMarshaller();
		// Formateo bonito
		jaxbM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		jaxbM.setProperty("com.sun.xml.bind.xmlHeaders", "\n<!DOCTYPE tienda SYSTEM \"pedido.dtd\">");
		jaxbM.setProperty("com.sun.xml.bind.xmlDeclaration", false);
		// Escribiendo en un fichero
		File XMLfile = new File("./xml/Pedido.xml");
		jaxbM.marshal(t, XMLfile);
		// Escribiendo por pantalla
		jaxbM.marshal(t, System.out);
	}
	
	private static void unMarshallingPedidos() throws JAXBException {
		// Creamos el JAXBContext
		JAXBContext jaxbC = JAXBContext.newInstance(Pedido.class);
		// Creamos el JAXBMarshaller
		Unmarshaller jaxbU = jaxbC.createUnmarshaller();
		// Leyendo un fichero
		File XMLfile = new File("./xml/Pedido.xml");
		// Creando el objeto
		Pedido t = (Pedido) jaxbU.unmarshal(XMLfile);
		// Escribiendo por pantalla el objeto
		System.out.println(t);
	}
}
