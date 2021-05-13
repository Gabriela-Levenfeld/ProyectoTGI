package db.jpa;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import db.interfaces.UsuariosManager;
import pojos.Rol;

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
}
