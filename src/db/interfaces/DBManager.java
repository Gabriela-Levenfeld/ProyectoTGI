package db.interfaces;

import java.util.List;

import pojos.Pedido;
import pojos.PedidoPiezaVehiculo;
import pojos.Pieza;
import pojos.PiezaVehiculo;
import pojos.Tienda;
import pojos.Usuario;
import pojos.Vehiculo;

public interface DBManager {
	//Acceder a la BD e inicializarlo cuando corresponda
	public void connect();

	//Cerrar la conexión con la BD
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

	public void addUsuario(Usuario usuario);
	
	public Usuario searchUsuarioById(Integer idUsuario);

	public void eliminarUsuarioById(int id);

	//Métodos nuevos añadidos
	public PiezaVehiculo searchPiezasVehiculosById(int idCompra);

	public Tienda searchTiendaById(int seleccionTienda);

	public void addPedido(Pedido pedido);

	public void addpedidoPiezaVehiculo(PedidoPiezaVehiculo pedidoPiezaVehiculo);

}
