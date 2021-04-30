package db.interfaces;

import java.util.List;

import pojos.Pieza;

public interface DBManager {
	//Acceder a la BD e inicializarlo cuando corresponda
	public void connect();

	//Cerrar la conexión con la BD
	public void disconnect();

	public void addPieza(Pieza pieza);

	public List<Pieza> searchPiezas();

	public boolean eliminarPieza(int idPieza);
}
