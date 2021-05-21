package db.jpa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import db.interfaces.UsuariosManager;
import pojos.Rol;
import pojos.Usuario;
import pojos.UsuarioJPA;

public class JPAUsuariosManager implements UsuariosManager{
	
	final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private EntityManagerFactory factory;
	private EntityManager em;
	private final String PERSISTENCE_PROVIDER = "taller-provider";
	
	@Override
	public void connect() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_PROVIDER);
		em = factory.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		if(getRoles().size() == 0) {
			addRol(new Rol("usuarioJPA"));
			addRol(new Rol("admin"));
		}
	}
	
	@Override
	public void disconnect() {
		em.close();
		factory.close();
	}
	
	@Override
	public void addRol(Rol rol) {
		em.getTransaction().begin();
		em.persist(rol);
		em.getTransaction().commit();
	}
	
	@Override
	public List<Rol> getRoles() {
		Query q = em.createNativeQuery("SELECT * FROM Roles", Rol.class);
		return q.getResultList();
	}

	@Override
	public Rol getRolById(int rolId) {
		Query q = em.createNativeQuery("SELECT * FROM Roles WHERE id = ?", Rol.class);
		q.setParameter(1, rolId);
		Rol rolEntontrado = (Rol) q.getSingleResult();
		if(rolEntontrado == null) {
			//El rol no existe
			return null;
		}else {
			return rolEntontrado;
		}
	}
	
	@Override
	public void addUsuario(UsuarioJPA usuario) {
		em.getTransaction().begin();
		em.persist(usuario);
		em.getTransaction().commit();
	}
	
	/*
	 Hemos decidido cambiar el tipo de encriptación por uno más seguro y adecuado para nuestra BD
	 */
	@Override
	public UsuarioJPA checkPass(String email, String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			//MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pass.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM UsuariosJPA WHERE email = ? AND password = ?", UsuarioJPA.class);
			q.setParameter(1, email);
			q.setParameter(2, hash);
			UsuarioJPA usuario = (UsuarioJPA) q.getSingleResult();
			return usuario;
		}catch(NoSuchAlgorithmException | NoResultException e) {
			return null;
		}
	}

	@Override
	public void cambiarPassword(int idUsuarioJPA, String nuevaPass) {
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			//MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(nuevaPass.getBytes());
			byte [] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM UsuariosJPA WHERE ID = ?",UsuarioJPA.class);
			q.setParameter(1, idUsuarioJPA);
			UsuarioJPA usuarioJPA = (UsuarioJPA) q.getSingleResult();
			
			em.getTransaction().begin();
			usuarioJPA.setPassword(hash);
			em.getTransaction().commit();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void cambiarEmail(int idUsuarioJPA, String nuevoEmail) {
		try{
			Query q = em.createNativeQuery("SELECT * FROM UsuariosJPA WHERE ID = ?",UsuarioJPA.class);
			q.setParameter(1, idUsuarioJPA);
			UsuarioJPA usuarioJPA = (UsuarioJPA) q.getSingleResult();
			
			em.getTransaction().begin();
			usuarioJPA.setEmail(nuevoEmail);
			em.getTransaction().commit();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void eliminarUsuarioJPA(int idUsuarioJPA) {
		try{
			Query q = em.createNativeQuery("SELECT * FROM UsuariosJPA WHERE ID = ?", UsuarioJPA.class);
			q.setParameter(1, idUsuarioJPA);
			UsuarioJPA usuarioJPA = (UsuarioJPA) q.getSingleResult();
			
			em.getTransaction().begin();
			em.remove(usuarioJPA);
			em.getTransaction().commit();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
