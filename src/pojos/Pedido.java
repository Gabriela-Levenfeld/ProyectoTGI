package pojos;

import java.io.Serializable;
import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "pedido")
@XmlAccessorType(XmlAccessType.FIELD)

public class Pedido implements Serializable{
	private static final long serialVersionUID = -8348912392169638115L;
	
	@XmlAttribute
	private int id;
	@XmlElement
	private Date fecha;
	@XmlElement
	private boolean online;
	@XmlTransient
	private Tienda tienda; //Declarar como transient
	@XmlElement
	private Usuario usuario;
	
	public Pedido() {
		super();
	}
	
	public Pedido(int id, Date fecha, boolean online, Tienda tienda, Usuario usuario) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.online = online;
		this.tienda = tienda;
		this.usuario = usuario;
	}
	
	public Pedido(Date fecha, boolean online, Tienda tienda, Usuario usuario) {
		super();
		this.fecha = fecha;
		this.online = online;
		this.tienda = tienda;
		this.usuario = usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Tienda getTienda() {
		return tienda;
	}

	public void setTienda(Tienda tienda) {
		this.tienda = tienda;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", online=" + online + ", tienda=" + tienda + ", usuario="
				+ usuario + "]";
	}
	
}
