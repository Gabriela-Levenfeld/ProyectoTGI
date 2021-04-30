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

public class JDBCManager implements DBManager {
	final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private Connection c;
	private final String addPieza = "INSERT INTO Piezas (Tipo) VALUES(?);";
	private final String searchPieza = "SELECT * FROM Piezas;";
	private final String eliminarUnaPieza = "DELETE FROM Piezas WHERE idPieza = ?;";

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
					+ "Piezas (IdPieza INTEGER PRIMARY KEY, Tipo STRING)");
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
	

}
