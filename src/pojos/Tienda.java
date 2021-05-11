package pojos;

public class Tienda {
	private int id;
	private String localizacion;
	private String horario;
	
	public Tienda() {
		super();
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
	
	@Override
	public String toString() {
		return "Tienda [id=" + id + ", localizacion=" + localizacion + ", horario=" + horario + "]";
	}
	
	
}
