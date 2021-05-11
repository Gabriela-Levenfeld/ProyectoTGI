package db.interfaces;

import java.util.List;

import pojos.Pieza;
import pojos.PiezaVehiculo;
import pojos.Tienda;
import pojos.Vehiculo;

public interface DBManager {
	//Acceder a la BD e inicializarlo cuando corresponda
	public void connect();

	//Cerrar la conexi�n con la BD
	public void disconnect();

	public void addPieza(Pieza pieza);

	public List<Pieza> searchPiezas();

	public boolean eliminarPieza(int idPieza);

	public List<Vehiculo> searchVehiculos();

	public void addVehiculo(Vehiculo vehiculo);

	public boolean eliminarVehiculo(String modelo);

	public List<Vehiculo> searchVehiculoByMarca(String marca);

	public Pieza searchPiezaById(int idPieza);

	public Vehiculo searchVehiculoByModelo(String modelo);

	public boolean addPrecio(PiezaVehiculo piezaVehiculo);

	public List<PiezaVehiculo> searchPiezasVehiculos();

	public boolean updatePrecio(PiezaVehiculo piezaVehiculo);

	public List<Tienda> mostrarTiendas();

	public void insertarTienda(Tienda tiendas);
}
