package db.jdbc;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import db.interfaces.DBManager;
import pojos.Pieza;
import pojos.PiezaVehiculo;
import pojos.Tienda;
import pojos.Usuario;
import pojos.Vehiculo;

public class JDBCManager implements DBManager {
	final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private Connection c;
	
	private final String addPieza = "INSERT INTO Piezas (Tipo) VALUES(?);";
	private final String searchPieza = "SELECT * FROM Piezas;";
	private final String eliminarUnaPieza = "DELETE FROM Piezas WHERE IdPieza = ?;";
	private final String searchVehiculo = "SELECT * FROM Vehiculos;";
	private final String addVehiculo = "INSERT INTO Vehiculos (Marca, Modelo) VALUES (?, ?);";
	private final String eliminarUnVehiculo = "DELETE FROM Vehiculos WHERE Modelo = ?;";
	private final String searchVehiculoPorMarca = "SELECT * FROM Vehiculos WHERE Marca = ?;";
	private final String searchPiezaById = "SELECT * FROM Piezas WHERE IdPieza = ?;";
	private final String searchVehiculoByModelo = "SELECT * FROM Vehiculos WHERE Modelo = ?;";
	private final String addPrecio = "INSERT INTO PiezasVehiculos (IdPieza, Modelo, Precio) VALUES (?,?,?);";
	private final String searchtablaConPrecio = "SELECT * FROM PiezasVehiculos;";
	private final String updatePrecio = "UPDATE PiezasVehiculos SET Precio = ? WHERE Id = ?;";
	private final String searchTiendas ="SELECT * FROM Tiendas;";
	private final String insertarTiendas = "INSERT INTO Tiendas (Localizacion, Horario) VALUES (?,?);";
	private final String addUsuario ="INSERT INTO Usuarios VALUES (?,?,?,?,?,?);" ;
	private final String searchUsuarioById = "SELECT * FROM Usuarios WHERE Id = ?;";
	private final String eliminarUnUsuario = "DELETE FROM Usuarios WHERE Id = ?;";
	private final String searchPiezaVehiculoById = "SELECT * FROM PiezasVehiculos WHERE Id = ?;";
	private final String searchTiendaById =  "SELECT * FROM Tiendas WHERE Id = ?;";
	
	@Override
	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/coches.db");
			Statement stmt = c.createStatement();
			stmt.execute("PRAGMA foreign_keys=ON");
			stmt.close();
			initializeDB();
			LOGGER.info("Se ha establecido la conexión con la BD");
		} catch (ClassNotFoundException e) {
			LOGGER.severe("No se ha encontrado la clase org.sqlite.JDBC");
			e.printStackTrace();
		} catch (SQLException e) {
			LOGGER.severe("Error al crear la conexión con la DB");
			e.printStackTrace();
		}		
	}


	private void initializeDB() {
		// Inicializar Base de Datos
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "Vehiculos (Modelo STRING PRIMARY KEY, Marca STRING)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "Piezas (IdPieza INTEGER PRIMARY KEY, Tipo STRING NOT NULL)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "PiezasVehiculos (Id INTEGER PRIMARY KEY, IdPieza INTEGER REFERENCES Piezas ON DELETE CASCADE, Modelo STRING REFERENCES Vehiculos ON DELETE CASCADE, Precio REAL)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "Tiendas (Id INTEGER PRIMARY KEY, Localizacion STRING, Horario STRING)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "Pedidos (Id STRING PRIMARY KEY, Fecha DATE, Online NUMERIC, IdTienda INTEGER REFERENCES Tiendas ON DELETE CASCADE, IdUsuario INTEGER REFERENCES Usuarios ON DELETE CASCADE)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "Usuarios (Id INTEGER PRIMARY KEY, Dni STRING, Nombre STRING, Apellidos STRING, Direccion STRING, Tarjeta INTEGER)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " 
					+ "PedidosPiezaVehiculo (Id INTEGER PRIMARY KEY, Cantidad INTEGER, IdPedido INTEGER REFERENCES Pedidos ON DELETE CASCADE, IdPiezaVehiculo REFERENCES PiezasVehiculos ON DELETE CASCADE)");
			stmt.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al crear las tablas");
			e.printStackTrace();
		}
	}


	@Override
	public void disconnect() {
		try {
			c.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al cerrar la conexión con la BD");
			e.printStackTrace();
		}
	}


	@Override
	public void addPieza(Pieza pieza) {
		try {
			PreparedStatement prep = c.prepareStatement(addPieza);
			prep.setString(1, pieza.getTipo());
			prep.executeUpdate();
			prep.close();
		}catch(SQLException e) {
			LOGGER.severe("Error al insertar pieza " + pieza);
			e.printStackTrace();
		}
		
	}


	@Override
	public List<Pieza> searchPiezas() {
		List<Pieza> piezas = new ArrayList<Pieza>();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(searchPieza);
			
			while(rs.next()) {
				int idPieza = rs.getInt("IdPieza");
				String tipo = rs.getString("Tipo");
				Pieza pieza = new Pieza(idPieza, tipo);
				piezas.add(pieza);
				LOGGER.fine("Pieza encontrada: " + pieza);
			}
			rs.close();
			stmt.close();			
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return piezas;	
	}

	@Override
	public boolean eliminarPieza(int idPieza) {
		boolean existe = false;
		try {
			PreparedStatement prep = c.prepareStatement(eliminarUnaPieza);
			prep.setInt(1,idPieza);
			int filasModificadas = prep.executeUpdate();
			if(filasModificadas == 1)
				existe = true;
			prep.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}


	@Override
	public List<Vehiculo> searchVehiculos() {
		List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(searchVehiculo);
			
			while(rs.next()) {
				String marca = rs.getString("Marca");
				String modelo = rs.getString("Modelo");
				Vehiculo vehiculo = new Vehiculo(marca, modelo);
				vehiculos.add(vehiculo);
				LOGGER.fine("Vehículo encontrado: " + vehiculo);
			}
			rs.close();
			stmt.close();			
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return vehiculos;
	}


	@Override
	public void addVehiculo(Vehiculo vehiculo) {
		try {
			PreparedStatement prep = c.prepareStatement(addVehiculo);
			prep.setString(1, vehiculo.getMarca());
			prep.setString(2, vehiculo.getModelo());
			prep.executeUpdate();
			prep.close();
		}catch(SQLException e) {
			LOGGER.severe("Error al insertar vehículo " + vehiculo);
			e.printStackTrace();
		}
	}


	@Override
	public boolean eliminarVehiculo(String modelo) {
		boolean existe = false;
		try {
			PreparedStatement prep = c.prepareStatement(eliminarUnVehiculo);
			prep.setString(1,modelo);
			int filasModificadas = prep.executeUpdate();
			if(filasModificadas == 1)
				existe = true;
			prep.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}


	@Override
	public List<Vehiculo> searchVehiculoByMarca(String marcaABuscar) {
		List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();
		try {
			PreparedStatement prep = c.prepareStatement(searchVehiculoPorMarca);
			//prep.setString(1,"%" + marca + "%");
			prep.setString(1,marcaABuscar);
			ResultSet rs = prep.executeQuery();
			while(rs.next()){
				String marca = rs.getString("Marca");
				String modelo = rs.getString("Modelo");
				Vehiculo vehiculo = new Vehiculo (marca, modelo);
				vehiculos.add(vehiculo);
				LOGGER.fine("Vehículo encontrado: " + vehiculo);
			}
			prep.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return vehiculos;
	}


	@Override
	public Pieza searchPiezaById(int idPiezaABuscar) {
		Pieza pieza = null;
		try {
			PreparedStatement prep = c.prepareStatement(searchPiezaById);
			prep.setString(1,idPiezaABuscar + "");
			ResultSet rs = prep.executeQuery();
			while(rs.next()){
				int idPieza = rs.getInt("IdPieza");
				String tipo = rs.getString("Tipo");
				pieza = new Pieza(idPieza, tipo);
				LOGGER.fine("Pieza encontrada buscada por id: " + pieza);
			}
			prep.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return pieza;
	}


	@Override
	public Vehiculo searchVehiculoByModelo(String modeloABuscar) {
		Vehiculo vehiculo = null;
		try {
			PreparedStatement prep = c.prepareStatement(searchVehiculoByModelo);
			prep.setString(1,modeloABuscar + "");
			ResultSet rs = prep.executeQuery();
			while(rs.next()){
				String marca = rs.getString("Marca");
				String modelo = rs.getString("Modelo");
				vehiculo = new Vehiculo(marca, modelo);
				LOGGER.fine("Vehiculo encontrado buscado por modelo: " + vehiculo);
			}
			prep.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return vehiculo;

	}


	@Override
	public boolean addPrecio(PiezaVehiculo piezaVehiculo) {
		boolean existe = false;
		try {
			PreparedStatement prep = c.prepareStatement(addPrecio);
			prep.setInt(1, piezaVehiculo.getPieza().getIdPieza());
			prep.setString(2, piezaVehiculo.getVehiculo().getModelo());
			prep.setDouble(3, piezaVehiculo.getPrecio());
			int filasModificadas = prep.executeUpdate();
			if(filasModificadas == 1)
				existe = true;
			prep.close();
		}catch(SQLException e) {
			LOGGER.severe("Error al insertar pieza " + piezaVehiculo);
			e.printStackTrace();
		}
		return existe;
	}


	@Override
	public List<PiezaVehiculo> searchPiezasVehiculos() {
		List<PiezaVehiculo> tablaConPrecio = new ArrayList<>();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(searchtablaConPrecio);
			while(rs.next()){
				int id = rs.getInt("Id");
				int idPieza = rs.getInt("IdPieza");
				Pieza pieza = searchPiezaById(idPieza);
				String modelo = rs.getString("Modelo");
				Vehiculo vehiculo = searchVehiculoByModelo(modelo);
				float precio = rs.getFloat("Precio");
				PiezaVehiculo piezaVehiculo = new PiezaVehiculo(id, pieza, vehiculo, precio);
				tablaConPrecio.add(piezaVehiculo);
				LOGGER.fine("Precio encontrado: " + piezaVehiculo);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return tablaConPrecio;
	}


	@Override
	public boolean updatePrecio(PiezaVehiculo piezaVehiculo) {
		boolean existe = false;
		try {
			PreparedStatement prep = c.prepareStatement(updatePrecio);
			prep.setDouble(1, piezaVehiculo.getPrecio());
			prep.setInt(2, piezaVehiculo.getId());
			int filasModificadas = prep.executeUpdate();
			if(filasModificadas == 1)
				existe = true;
			prep.close();
		}catch(SQLException e) {
			LOGGER.severe("Error al actualizar el precio");
			e.printStackTrace();
		}
		return existe;
	}

	@Override
	public List<Tienda> mostrarTiendas() {
		List<Tienda> tiendas = new ArrayList<Tienda>();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(searchTiendas);
			
			while(rs.next()) {
				int idTienda = rs.getInt("Id");
				String localizacion = rs.getString("Localizacion");
				String horario = rs.getString("Horario");
				Tienda tienda = new Tienda(idTienda, localizacion, horario);
				tiendas.add(tienda);
				LOGGER.fine("Tienda encontrada: " + tienda);
			}
			rs.close();
			stmt.close();			
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return tiendas;
	}
	
	public void insertarTienda(Tienda tienda) {
		try {
			PreparedStatement prep = c.prepareStatement(insertarTiendas);
			prep.setString(1, tienda.getLocalizacion());
			prep.setString(2, tienda.getHorario());
			prep.executeUpdate();
			prep.close();			
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un INSERT");
			e.printStackTrace();
		}
	}

	@Override
	public void addUsuario(Usuario usuario) {
        try {
            PreparedStatement prep = c.prepareStatement(addUsuario);
            prep.setInt(1, usuario.getId());
            prep.setString(2, usuario.getDni());
            prep.setString(3, usuario.getNombre());
            prep.setString(4, usuario.getApellido());
            prep.setString(5, usuario.getDireccion());
            prep.setInt(6, usuario.getTarjeta());
            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            LOGGER.severe("Error al insertar usuario: " + usuario);
            e.printStackTrace();
        }
	}
	
    @Override
    public Usuario searchUsuarioById(Integer idUsuario) {
    	Usuario usuario = null;
        try {
            PreparedStatement prep = c.prepareStatement(searchUsuarioById);
            prep.setInt(1, idUsuario);
            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                String nombre = rs.getString("Nombre");
                usuario = new Usuario(idUsuario, nombre);
                LOGGER.fine("Cliente encontrado buscando por id: " + usuario);
            }
            prep.close();
        } catch (SQLException e) {
            LOGGER.severe("Error al hacer un SELECT");
            e.printStackTrace();
        }
        return usuario;
    }


	@Override
	public void eliminarUsuarioById(int idUsuarioAEliminar) {
		try {
			PreparedStatement prep = c.prepareStatement(eliminarUnUsuario);
			prep.setInt(1,idUsuarioAEliminar);
			prep.executeUpdate();
			prep.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}


	@Override
	public PiezaVehiculo searchPiezasVehiculosById(int idCompra) {
		PiezaVehiculo piezaVehiculo = null;
		try {
            PreparedStatement prep = c.prepareStatement(searchPiezaVehiculoById);
            prep.setInt(1, idCompra);
            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                int idPieza = rs.getInt("IdPieza");
                Pieza pieza = searchPiezaById(idPieza);
                
                String modelo = rs.getString("Modelo");
                Vehiculo vehiculo= searchVehiculoByModelo(modelo);
              
                double precio = rs.getDouble("Precio");
                
                piezaVehiculo = new PiezaVehiculo(pieza, vehiculo, precio);
                LOGGER.fine("Relacion pieza-vehiculo encontrada buscando por id: " + piezaVehiculo);
            }
            prep.close();
        } catch (SQLException e) {
            LOGGER.severe("Error al hacer un SELECT");
            e.printStackTrace();
        }
		return piezaVehiculo;
	}


	@Override
	public Tienda searchTiendaById(int seleccionTienda) {
		Tienda tienda = null;
		try {
			PreparedStatement prep = c.prepareStatement(searchTiendaById);
			prep.setInt(1, seleccionTienda);
			ResultSet rs = prep.executeQuery();
			while(rs.next()){
				int idTienda = rs.getInt("Id");
				String localizacion = rs.getString("Localizacion");
				String horario = rs.getString("Horario");
				tienda = new Tienda(idTienda, localizacion, horario);
				LOGGER.fine("Pieza encontrada buscada por id: " + tienda);
			}
			prep.close();
		} catch (SQLException e) {
			LOGGER.severe("Error al hacer un SELECT");
			e.printStackTrace();
		}
		return tienda;
	}

}
