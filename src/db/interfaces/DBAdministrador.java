package db.interfaces;

public interface DBAdministrador {
	//Acceder a la BD e inicializarlo cuando corresponda
	public void connect();

	//Cerrar la conexi�n con la BD
	public void disconnect();
}
