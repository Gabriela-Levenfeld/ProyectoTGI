package pojos;

public class Pieza {
	private int idPieza;
	private String tipo;
	
	public Pieza() {
		super();
	}
	
	public Pieza(int idPieza, String tipo) {
		super();
		this.idPieza = idPieza;
		this.tipo = tipo;
	}

	public Pieza(String tipo) {
		super();
		this.tipo = tipo;
	}

	public int getIdPieza() {
		return idPieza;
	}

	public void setIdPieza(int idPieza) {
		this.idPieza = idPieza;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Pieza [idPieza=" + idPieza + ", tipo=" + tipo + "]";
	}


	
}
