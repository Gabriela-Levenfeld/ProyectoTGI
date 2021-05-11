package pojos;

import java.sql.Date;

public class Pedido {
	private int id;
	private Date fecha;
	private boolean online;
	private Tienda tienda;
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
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", online=" + online + ", tienda=" + tienda + ", usuario="
				+ usuario + "]";
	}
	
}
