package pojos;

public class PedidoPiezaVehiculo {
	private int id;
	private Pedido pedido;
	private int cantidad;
	private PiezaVehiculo piezaVehiculo;
	
	public PedidoPiezaVehiculo(){
		super();
	}
	
	public PedidoPiezaVehiculo(Pedido pedido, int cantidad, PiezaVehiculo piezaVehiculo){
		super();
		this.pedido = pedido;
		this.cantidad = cantidad;
		this.piezaVehiculo = piezaVehiculo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public PiezaVehiculo getPiezaVehiculo() {
		return piezaVehiculo;
	}

	public void setPiezaVehiculo(PiezaVehiculo piezaVehiculo) {
		this.piezaVehiculo = piezaVehiculo;
	}

	@Override
	public String toString() {
		return "PedidoPiezaVehiculo [id=" + id + ", pedido=" + pedido + ", cantidad=" + cantidad + ", piezaVehiculo="
				+ piezaVehiculo + "]";
	}
	
	
}
