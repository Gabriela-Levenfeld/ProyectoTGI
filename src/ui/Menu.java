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
import pojos.PiezaVehiculo;
import pojos.Vehiculo;

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
				LOGGER.warning("El usuario no ha introducido un numero válido.");
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
					addVehiculo(); //xc90 != XC90
					break;
				case 4:
					eliminarVehiculo();
					break;
				case 5:
					descatalogarUnaMarca();
					break;
				case 6:
					addPrecio();
					break;
				case 7:
					modificarPrecio();
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

	private static void menuAdministrador() {
		System.out.println("Elije una opción: ");
		System.out.println("1- Añadir nueva pieza");
		System.out.println("2- Eliminar pieza");
		System.out.println("3- Añadir nuevo vehículo");
		System.out.println("4- Eliminar vehículo");
		System.out.println("5- Descatalogar una marca");
		System.out.println("6- Añadir precio");
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
			e.printStackTrace();
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
	
	private static void searchVehiculos() {
		List<Vehiculo> vehiculos = dbman.searchVehiculos();
		System.out.println("Se han encontrado los siguientes vehículos: ");
		for(Vehiculo vehiculo : vehiculos) {
			System.out.println(vehiculo);
		}
	}
	
	private static void addVehiculo() {
		searchVehiculos();
		try {
			System.out.println("Indique la marca del vehículo:");
			String marca = reader.readLine();
			System.out.println("Indique el modelo del vehículo (en minúscula):");
			String modelo = reader.readLine();
			Vehiculo vehiculo = new Vehiculo(marca, modelo);
			dbman.addVehiculo(vehiculo);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private static void eliminarVehiculo() {
		searchVehiculos();
		System.out.println("Seleccione el modelo del vehículo a eliminar: ");
		try {
			String modelo =  reader.readLine();
			boolean existe = dbman.eliminarVehiculo(modelo);
			if (existe) {
				System.out.println("El vehículo se ha eliminado");
			}else {
				System.out.println("El vehículo no existe");
			}
		}catch(NumberFormatException | IOException e){
			e.printStackTrace();
		}	
	}
	
	private static void descatalogarUnaMarca() {
		searchVehiculos();
		System.out.println("Seleccione la marca del vehículo a eliminar: ");
		try {
			String marcaABuscar =  reader.readLine();
			List<Vehiculo> vehiculos = dbman.searchVehiculoByMarca(marcaABuscar);
			if (vehiculos.size() > 0) {
				System.out.println("Se van a borrar los siguientes vehiculos:");
				for(Vehiculo vehiculo : vehiculos) {
					System.out.println(vehiculo);
				}
				System.out.println("¿Confirmar borrado?(s/n)");
				String respuesta = reader.readLine();
				if(respuesta.equalsIgnoreCase("s")) {
					int contador = 0;
					do {
						String modelo = vehiculos.get(contador).getModelo();
						boolean existeVehiculo = dbman.eliminarVehiculo(modelo);
						if(existeVehiculo == false) {
							System.out.println("Ha habido un error al intentar eliminar el vehículo");
						}
						contador++;
					}while(contador<vehiculos.size());
				}else {
					System.out.println("Se ha cancelado la operación de borrado");
				}
				System.out.println("Se han borrado todos los vehículos de la marca " + marcaABuscar);
			} else {
				System.out.println("El vehículo no existe");
				}
		}catch(NumberFormatException | IOException e){
				e.printStackTrace();
		}
	}

	private static void addPrecio() {		
		try {
			searchPiezas();
			System.out.println("Introduzca el id de la pieza:");
			int idPieza = Integer.parseInt(reader.readLine());
			Pieza pieza = dbman.searchPiezaById(idPieza);
			
			searchVehiculos();
			System.out.println("Introduzca el modelo del vehículo:");
			String modelo = reader.readLine();
			Vehiculo vehiculo = dbman.searchVehiculoByModelo(modelo);
			
			if(pieza == null || vehiculo == null) {
				System.out.println("Los datos introducidos son incorrectos");
			}else {
				System.out.println("Introduzca el precio (utilice punto, no coma):");
				double precio = Double.parseDouble(reader.readLine());
				
				PiezaVehiculo piezaVehiculo = new PiezaVehiculo(pieza, vehiculo, precio);
				boolean existe = dbman.addPrecio(piezaVehiculo);;
				if (existe == false ) {
					System.out.println("El precio no se ha añadido porque los datos introducidos son incorrectos");
				}else {
					System.out.println("El precio se ha añadido correctamente");
				}	
			}		
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void mostrarPiezaVehiculoPrecio() {
		List<PiezaVehiculo> piezasVehiculos = dbman.searchPiezasVehiculos();
		System.out.println("Se han encontrado las siguientes piezas y vehículos: ");
		for(PiezaVehiculo piezavehiculo : piezasVehiculos) {
			System.out.println(piezavehiculo);
		}		
	}

	private static void modificarPrecio() {
		mostrarPiezaVehiculoPrecio();		
	}
}
