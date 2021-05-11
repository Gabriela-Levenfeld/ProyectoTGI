package pojos;

public class PiezaVehiculo {
	int id;
	private Pieza pieza;
	private Vehiculo vehiculo;
	private double precio;
	
	public PiezaVehiculo(){
		super();
	}
	
	public PiezaVehiculo(Pieza pieza, Vehiculo vehiculo, double precio){
		super();
		this.pieza = pieza;
		this.vehiculo = vehiculo;
		this.precio = precio;
	}
	
	public PiezaVehiculo(int id, Pieza pieza, Vehiculo vehiculo, double precio){
		super();
		this.id = id;
		this.pieza = pieza;
		this.vehiculo = vehiculo;
		this.precio = precio;
	}
	
	public PiezaVehiculo(int id){
		super();
		this.id = id;
	}
	
	public Pieza getPieza() {
		return pieza;
	}
	public void setPieza(Pieza pieza) {
		this.pieza = pieza;
	}
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PiezaVehiculo [id=" + id + ", pieza=" + pieza + ", vehiculo=" + vehiculo + ", precio=" + precio + "]";
	}

}
