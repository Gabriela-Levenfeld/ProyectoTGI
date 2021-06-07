package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import db.interfaces.DBManager;
import db.interfaces.UsuariosManager;
import db.jdbc.JDBCManager;
import db.jpa.JPAUsuariosManager;
import logging.MyLogger;
import pojos.DNI;
import pojos.Pedido;
import pojos.PedidoPiezaVehiculo;
import pojos.Pieza;
import pojos.PiezaVehiculo;
import pojos.Rol;
import pojos.Tienda;
import pojos.Usuario;
import pojos.UsuarioJPA;
import pojos.Vehiculo;


public class Menu {
	final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static DBManager dbman = new JDBCManager();
	private static UsuariosManager userman = new JPAUsuariosManager();
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	private final static DateTimeFormatter formatterPedido= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final static String[] TIENDAS_NOMBRES = {"Pozuelo", "Alcobendas", "Madrid", "Parla"};
	private final static String[] TIENDAS_HORARIO = {"8:00-14:00","8:00-14:00", "14:00-20:00","8:00-20:00"};
	
	public static void main(String[] args) {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbman.connect();
		userman.connect();
		
		int respuesta;
		do {
			System.out.println("\nElige una opción:");
			System.out.println("1. Registrarse");
			System.out.println("2. Login");
			System.out.println("3. Actualizar contraseña");
			System.out.println("4. Actualizar email");
			System.out.println("5. Darse de baja en la Base de Datos");
			System.out.println("0. Salir");
			try {
				respuesta = Integer.parseInt(reader.readLine());
				LOGGER.info("El usuario elige " + respuesta);
			} catch (NumberFormatException | IOException e) {
				respuesta = -1;
				LOGGER.warning("El usuario no ha introducido un número");
				e.printStackTrace();
			}
			switch(respuesta) {
				case 1:
					registrarse();
					break;
				case 2:
					login();
					break;
				case 3:
					actualizarPassword();
					break;
				case 4:
					actualizarEmail();
					break;
				case 5:
					borrarUsuario();
					break;
				case 0:
					System.out.println("Fin del programa");
					break;
			}
		} while (respuesta != 0);
		
		userman.disconnect();
		dbman.disconnect();
	}

	private static void registrarse() {
		try {
			System.out.println("Indique su email:");
			String email = reader.readLine();
			System.out.println("Indique su contraseña:");
			String pass = reader.readLine();
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			//MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pass.getBytes());
			byte[] hash = md.digest();
			
			System.out.println("Roles disponibles");
			System.out.println(userman.getRoles());
			System.out.println("Indique el id del rol:");
			int rolId = Integer.parseInt(reader.readLine());
			Rol rol = userman.getRolById(rolId);
			if(rol == null) {
				System.out.println("No existe dicho rol.");
			}else {
				if(rolId==1) {
					System.out.println("Indique su DNI:");
					String dni = reader.readLine();
					System.out.println("Indique su nombre:");
					String nombre = reader.readLine();
					System.out.println("Indique su apellido:");
					String apellido = reader.readLine();
					System.out.println("Indique su direccion:");
					String direccion = reader.readLine();
					System.out.println("Indique su número de tarjeta:");
					int tarjeta = Integer.parseInt(reader.readLine());	
				
					UsuarioJPA usuarioJPA = new UsuarioJPA(email, hash, rol);
					LOGGER.info(usuarioJPA.toString());
					userman.addUsuario(usuarioJPA);
					System.out.println("Te has registrado con éxito");
					LOGGER.info(usuarioJPA.toString());
				
					Usuario usuario = new Usuario (usuarioJPA.getId(), dni, nombre, apellido, direccion, tarjeta);
					dbman.addUsuario(usuario);
				}else {
					/*En nuestro diseño hemos decidido que un Administrador NO es un usuario.
					 Por tanto, no le incluimos en la tabla de Usuarios, ni le solicitamos más datos que los necesarios para Usuario JPA
					 */
					UsuarioJPA usuarioJPA = new UsuarioJPA(email, hash, rol);
					userman.addUsuario(usuarioJPA);
					System.out.println("Te has registrado con éxito");
				}
			}
		} catch (IOException e) {
			LOGGER.warning("Error al registrarse");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private static void login() {
		try {
			System.out.println("Indique su email:");
			String email = reader.readLine();
			System.out.println("Indique su contraseña:");
			String pass = reader.readLine();
			
			UsuarioJPA usuarioJPA = userman.checkPass(email, pass);
			
			if (usuarioJPA == null) {
				System.out.println("ACCESO DENEGADO");
				System.out.println("Email o contraseña incorrectos");
			} else {
			    Usuario usuario = dbman.searchUsuarioById(usuarioJPA.getId());
			    if(usuarioJPA.getRol().getNombre().equalsIgnoreCase("usuarioJPA")) {
			    	//En este caso es un Usuario y cuenta con el atributo nombre
			    	 System.out.println("Bienvenido " + usuario.getNombre());
			    	 System.out.println("\n");
			    	 
			    	 System.out.println("MENU USUARIO\n");
			    	 menuUsuario(usuario);
			    }else if (usuarioJPA.getRol().getNombre().equalsIgnoreCase("admin")){
			    	//Si soy admin, no tengo ese atributo nombre
			    	 System.out.println("Bienvenido Administrador\n");
			    	 System.out.println("MENU ADMINISTRADOR\n");
			    	 menuAdministrador();
			    } else {
					LOGGER.warning("Rol incorrecto");
				}
			}
		} catch (IOException e) {
			LOGGER.severe("Error al hacer login");
			e.printStackTrace();
		}
	}
	
	private static void actualizarPassword() {
		try {
			System.out.println("Indique su email:");
			String email = reader.readLine();
			System.out.println("Indique su contraseña:");
			String pass = reader.readLine();
			
			UsuarioJPA usuarioJPA = userman.checkPass(email, pass);
			
			if (usuarioJPA == null) {
				System.out.println("ACCESO DENEGADO");
				System.out.println("Email o contraseña incorrectos");
			} else {
			    Usuario usuario = dbman.searchUsuarioById(usuarioJPA.getId());
			    if(usuarioJPA.getRol().getNombre().equalsIgnoreCase("usuarioJPA")) {
			    	//En este caso es un Usuario y cuenta con el atributo nombre
			    	 System.out.println("Bienvenido " + usuario.getNombre());
			    	 System.out.println("\n");
			    }else {
			    	//Si soy admin, no tengo ese atributo nombre e imprimimos un mensaje general
			    	 System.out.println("Bienvenido Administrador\n");
			    }
			    
			    System.out.println("Introduzca la nueva contraseña: ");
			    String nuevaPass = reader.readLine();
			    String comprobarPass="";
			    while(!nuevaPass.equals(comprobarPass)) {
			    	System.out.println("Introduzca de nuevo la contraseña");
			    	comprobarPass = reader.readLine();
			    }
			    int idUsuarioJPA = usuarioJPA.getId();
			    userman.cambiarPassword(idUsuarioJPA, nuevaPass);
			    System.out.println("Su contraseña ha sido actualizada correctamente");
			}
		} catch (IOException e) {
			LOGGER.severe("Error al hacer login");
			e.printStackTrace();
		}
	}
	private static void actualizarEmail() {
		try {
			System.out.println("Indique su email:");
			String email = reader.readLine();
			System.out.println("Indique su contraseña:");
			String pass = reader.readLine();
			
			UsuarioJPA usuarioJPA = userman.checkPass(email, pass);
			
			if (usuarioJPA == null) {
				System.out.println("ACCESO DENEGADO");
				System.out.println("Email o contraseña incorrectos");
			} else {
			    Usuario usuario = dbman.searchUsuarioById(usuarioJPA.getId());
			    if(usuarioJPA.getRol().getNombre().equalsIgnoreCase("usuarioJPA")) {
			    	//En este caso es un Usuario y cuenta con el atributo nombre
			    	 System.out.println("Bienvenido " + usuario.getNombre());
			    	 System.out.println("\n");
			    }else {
			    	//Si soy admin, no tengo ese atributo nombre e imprimimos un mensaje general
			    	 System.out.println("Bienvenido Administrador\n");
			    }
			    
			    System.out.println("Introduzca el nuevo email: ");
			    String nuevoEmail = reader.readLine();
			    
			    int idUsuarioJPA = usuarioJPA.getId();
			    userman.cambiarEmail(idUsuarioJPA, nuevoEmail);
			    System.out.println("Su email ha sido actualizado correctamente");
			}
		} catch (IOException e) {
			LOGGER.severe("Error al hacer login");
			e.printStackTrace();
		}
	}
	
	private static void borrarUsuario() {
		/* En este método vamos a borrar al usuario tanto en nuestra tabla UsuarioJPA como en nuestra tabla Usuarios
		 * (perteneciente a JDBC)
		*/
		
		try {
			System.out.println("Indique su email:");
			String email = reader.readLine();
			System.out.println("Indique su contraseña:");
			String pass = reader.readLine();
			
			UsuarioJPA usuarioJPA = userman.checkPass(email, pass);
			
			if (usuarioJPA == null) {
				System.out.println("ACCESO DENEGADO");
				System.out.println("Email o contraseña incorrectos");
			} else {
			    Usuario usuario = dbman.searchUsuarioById(usuarioJPA.getId());
			    if(usuarioJPA.getRol().getNombre().equalsIgnoreCase("usuarioJPA")) {
			    	//En este caso es un Usuario y cuenta con el atributo nombre
			    	 System.out.println("Bienvenido " + usuario.getNombre());
			    	 System.out.println("\n");
			    	 
			    	 //En este caso también hay que borrar al Usuario JDBC
			    	 dbman.eliminarUsuarioById(usuario.getId());
							    	 
			    }else {
			    	//Si soy admin, no tengo ese atributo nombre e imprimimos un mensaje general
			    	 System.out.println("Bienvenido Administrador\n");
			    }
			    
			    int idUsuarioJPA = usuarioJPA.getId();
			    userman.eliminarUsuarioJPA(idUsuarioJPA);
			    System.out.println("Ha sido dado de baja correctamente");
			}
		} catch (IOException e) {
			LOGGER.severe("Error al hacer login");
			e.printStackTrace();
		}
	}

	private static void menuAdministrador() {
		int respuesta = -1;
		while(respuesta != 0) {
			System.out.println("Elije una opción: ");
			System.out.println("1- Añadir nueva pieza");
			System.out.println("2- Eliminar pieza");
			System.out.println("3- Añadir nuevo vehículo");
			System.out.println("4- Eliminar vehículo");
			System.out.println("5- Descatalogar una marca");
			System.out.println("6- Añadir precio");
			System.out.println("7- Modificar precio");
			System.out.println("0- Regreso a menú principal");	
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
					System.out.println("Regreso a menú principal");
					break;
				default:
					System.out.println("El número introducido es incorrecto");
					break;
			}
		}	
	}
	
	private static void menuUsuario(Usuario usuario) {
		int respuesta = -1;
		while(respuesta != 0) {
			System.out.println("Elije una opción: ");
			System.out.println("1- Mostrar catálogo");
			//System.out.println("2- Compra");
			System.out.println("0- Regreso a menú principal");	
			try {
				respuesta = Integer.parseInt(reader.readLine());
				LOGGER.info("El usuario elije " + respuesta);
			} catch (NumberFormatException | IOException e) {
				LOGGER.warning("El usuario no ha introducido un numero válido.");
				e.printStackTrace();
			}
			switch(respuesta) {
				case 1:
					mostrarPiezaVehiculoPrecio();
					break;
				case 2:
					compra(usuario);
					break;
				case 0:
					System.out.println("Fin");
					break;
				default:
					System.out.println("El número introducido es incorrecto");
					break;
			}
		}	
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
		} catch (NumberFormatException|NullPointerException| IOException e) {
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
		try {
			System.out.println("Introduzca el id de la fila a modificar:");
			int idFilaPrecioAModificar = Integer.parseInt(reader.readLine());
			
			System.out.println("Introduzca el precio actualizado (utilice punto, no coma):");
			double precio = Double.parseDouble(reader.readLine());
				
			PiezaVehiculo piezaVehiculo = new PiezaVehiculo(idFilaPrecioAModificar, precio);
			boolean existe = dbman.updatePrecio(piezaVehiculo);;
			if (existe == false ) {
				System.out.println("El precio no se ha añadido porque los datos introducidos son incorrectos");
			}else {
				System.out.println("El precio se ha actualizado correctamente");
			}			
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}
	}

	//Metodo que da error
	private static void compra(Usuario usuario) {
		List<Tienda> tiendas = dbman.mostrarTiendas();
		if(tiendas.size()==0) {
			generarTiendas();
		}else {
			try {
				System.out.println("COMPRA\n");
				mostrarPiezaVehiculoPrecio(); //Mostramos los productos disponibles (con precio incluido)
				System.out.println("Introduzca el id de la fila a comprar:");
				int idCompra = Integer.parseInt(reader.readLine());
				PiezaVehiculo piezaVehiculo = dbman.searchPiezasVehiculosById(idCompra);
				if(piezaVehiculo==null) {
					//No existe dicho producto y se finaliza el método
					System.out.println("El pedido no se ha podido realizar porque no existe dicho producto");
				}else {
					//Existe el id y se prosigue con la compra
					System.out.println("Introduzca la cantidad a comprar:");
					int cantidad=-1;
					do {
						cantidad = Integer.parseInt(reader.readLine());
					}while(cantidad<=0); //No se establece un limite superior ya que hemos supuesto stock infinito
					
					System.out.println("¿Desea recibir el pedido a domicilio?(s/n)");
					String seleccionOnline = reader.readLine();
					boolean online;
					int seleccionTienda;
					Tienda tienda;
					if(seleccionOnline.equalsIgnoreCase("n")) {
						//El usuario selecciona "Recoger el pedido en una Tienda Física" (Online=NO)
						online = false;
						mostrarTiendas();
						seleccionTienda = -1;
						do {
							//Se obliga al usuario a seleccionar una tienda existente
							System.out.println("Seleccione el id de la Tienda donde desee recoger su pedido");
							seleccionTienda = Integer.parseInt(reader.readLine());
						}while(dbman.searchTiendaById(seleccionTienda) == null);
						tienda = dbman.searchTiendaById(seleccionTienda);
					}else {
						//El usuario selecciona "Recibir el pedido en su domicilio" (Online=SÍ)
						online = true;
						tienda = null;
					}
					//Generamos una fecha con el siguiente formato: "yyyy-MM-dd HH:mm:ss"
					//La fecha se deberia genera automaticamentem, pero no esta muy conseguido
					LocalDate fecha = LocalDate.parse("2021-06-07 10:11:11", formatterPedido);
					
							
					//En ambos casos necesitamos un Usuario, motivo por el que se lo pasamos a este métodos desde el momento en el que hace login
					Pedido pedido = new Pedido(Date.valueOf(fecha), online, tienda, usuario);
					dbman.addPedido(pedido); //Lo añadimos a la BBDD	
						
					PedidoPiezaVehiculo pedidoPiezaVehiculo = new PedidoPiezaVehiculo(pedido, cantidad, piezaVehiculo);
					dbman.addpedidoPiezaVehiculo(pedidoPiezaVehiculo); //Lo añadimos a la BBDD
					
					System.out.println("Su pedido se ha realizado correctamente");
					}
					
				} catch (NullPointerException | IOException e) {
					e.printStackTrace();
				}
		}
	}


	private static void generarTiendas() {
		int numTiendas = 4;
		for(int i = 0; i < numTiendas; i++) {
			Tienda tienda = new Tienda(TIENDAS_NOMBRES[i], TIENDAS_HORARIO[i]);
			dbman.insertarTienda(tienda);
		}
		System.out.println("Se han generado " + numTiendas + " tiendas.");
	}
	
	private static void mostrarTiendas() {
		List<Tienda> tiendas = dbman.mostrarTiendas();
		System.out.println("Tiendas disponibles: ");
		for(Tienda tienda : tiendas) {
			System.out.println(tienda);
		}
	}
}
