package pojos;

public class PiezaVehiculo {
	private Pieza pieza;
	private Vehiculo vehiculo;
	private float precio;
	
	public PiezaVehiculo(){
		super();
	}
	
	public PiezaVehiculo(Pieza pieza, Vehiculo vehiculo, float precio){
		super();
		this.pieza = pieza;
		this.vehiculo = vehiculo;
		this.precio = precio;
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
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	@Override
	public String toString() {
		return "PiezaVehiculo [pieza=" + pieza + ", vehiculo=" + vehiculo + ", precio=" + precio + "]";
	}
	

}
