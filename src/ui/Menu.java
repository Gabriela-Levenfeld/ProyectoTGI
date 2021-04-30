package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

import db.interfaces.DBManager;
import db.jdbc.JDBCManager;
import logging.MyLogger;
import pojos.Pieza;

public class Menu {
	final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static DBManager dbman = new JDBCManager();
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbman.connect();
		int respuesta = -1;
		
		while(respuesta != 0) {
			menuAdministrador();
			try {
				respuesta = Integer.parseInt(reader.readLine());
				LOGGER.info("El usuario elije " + respuesta);
			} catch (NumberFormatException | IOException e) {
				LOGGER.warning("El usuario no ha introducido un numero valido.");
				e.printStackTrace();
			}
			switch(respuesta) {
				case 1:
					addPieza();
					break;
				case 2:
					eliminarPieza();
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 0:
					System.out.println("Fin");
					dbman.disconnect();
					break;
				default:
					System.out.println("El número introducido es incorrecto");
					break;
			}
		}
		
	}
	
	private static void eliminarPieza() {
		searchPiezas();
		System.out.println("Seleccione el id de la pieza a eliminar: ");
		try {
			int idPieza = Integer.parseInt(reader.readLine());
			boolean existe = dbman.eliminarPieza(idPieza);
			if (existe) {
				System.out.println("La pieza se ha eliminado");
			}else {
				System.out.println("La pieza no existe");
			}
		}catch(NumberFormatException | IOException e){
			e.printStackTrace();
		}
		
	}

	private static void menuAdministrador() {
		System.out.println("Elije una opción: ");
		System.out.println("1- Añadir nueva pieza");
		System.out.println("2- Eliminar pieza");
		System.out.println("3- Añadir nuevo vehículo");
		System.out.println("4- Eliminar vehículo");
		System.out.println("5- Añadir precio");
		System.out.println("6- Eliminar precio"); //Tema2, diapo 28
		System.out.println("7- Modificar precio");
		System.out.println("0- Salir");		
	}
	
	private static void menuUsuario() {
		System.out.println("Elije una opción: ");
		System.out.println("0- Salir");		
	}
	
	private static void searchPiezas() {
		List<Pieza> piezas = dbman.searchPiezas();
		System.out.println("Se han encontrado las siguientes piezas: ");
		for(Pieza pieza : piezas) {
			System.out.println(pieza);
		}
		
	}
	
	private static void addPieza() {
		searchPiezas();
		try {
			System.out.println("Indique el nombre de la pieza:");
			String nombre = reader.readLine();
			Pieza pieza = new Pieza(nombre);
			dbman.addPieza(pieza);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
