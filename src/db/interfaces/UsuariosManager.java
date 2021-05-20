package db.interfaces;

import java.util.List;

import pojos.Rol;
import pojos.Usuario;
import pojos.UsuarioJPA;

public interface UsuariosManager {
	void connect();

	void disconnect();

	void addRol(Rol rol);

	List<Rol> getRoles();

	Rol getRolById(int rolId);
	
	void addUsuario(UsuarioJPA usuario);

	UsuarioJPA checkPass(String email, String pass);

//	void cambiarPassword(int idUsuarioJPA, String nuevaPass);

	
}
