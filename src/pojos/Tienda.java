package pojos;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "tienda")
@XmlAccessorType(XmlAccessType.FIELD)

public class Tienda implements Serializable{
	
	private static final long serialVersionUID = 8154503738238822194L;
	
	@XmlAttribute
	private int id;
	@XmlElement(name = "localizacion")
	private String localizacion;
	@XmlElement(name = "horario")
	private String horario;
	
	//Para simular la relación uno a muchos en java
	@XmlElement(name = "pedido")
	private ArrayList<Pedido> pedidos;
			
	public Tienda() {
		super();
		pedidos = new ArrayList<>();
	}
	
	public Tienda(int id, String localizacion, String horario) {
		super();
		this.id = id;
		this.localizacion = localizacion;
		this.horario = horario;
	}
	
	
	
	public Tienda(String localizacion, String horario) {
		super();
		this.localizacion = localizacion;
		this.horario = horario;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	

	public ArrayList<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(ArrayList<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	public void addPedido(Pedido p) {
		if(!pedidos.contains(p))
			pedidos.add(p);
	}
	
	public void removePedidos(Pedido p) {
		pedidos.remove(p);
	}
	
	@Override
	public String toString() {
		return "Tienda [id=" + id + ", localizacion=" + localizacion + ", horario=" + horario + "]";
	}
	
	
}
