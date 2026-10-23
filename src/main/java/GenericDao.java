import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GenericDao<T, ID extends Serializable> implements IGenericDao<T, ID> {

	protected SessionFactory sessionFactory;

	public GenericDao() {
		sessionFactory = SessionManager.getSessionFactory();
	}

	@Override
	public void saveOrUpdate(T entity) {
		Session session = sessionFactory.getCurrentSession();
		boolean propia = !session.getTransaction().isActive();
		try {
			if (propia) {
				session.beginTransaction();
			}
			session.merge(entity);
			if (propia) {
				session.getTransaction().commit();
			}
		} catch (RuntimeException e) {
			if (!propia) {
				throw e;
			}
			handleException(session, e);
		}
	}

	@Override
	public T get(ID id) {
		Session session = sessionFactory.getCurrentSession();
		boolean propia = !session.getTransaction().isActive();
		try {
			if (propia) {
				session.beginTransaction();
			}
			T entity = session.get(getEntityClass(), id);
			if (propia) {
				session.getTransaction().commit();
			}
			return entity;
		} catch (RuntimeException e) {
			if (!propia) {
				throw e;
			}
			handleException(session, e);
			return null;
		}
	}

	@Override
	public void delete(ID id) {
		Session session = sessionFactory.getCurrentSession();
		boolean propia = !session.getTransaction().isActive();
		try {
			if (propia) {
				session.beginTransaction();
			}
			T entity = session.get(getEntityClass(), id);
			if (entity != null) {
				session.remove(entity);
			} else {
				System.out.println("No existeix aquest ID");
			}
			if (propia) {
				session.getTransaction().commit();
			}
		} catch (RuntimeException e) {
			if (!propia) {
				throw e;
			}
			handleException(session, e);
		}
	}

	@Override
	public void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		boolean propia = !session.getTransaction().isActive();
		try {
			if (propia) {
				session.beginTransaction();
			}
			session.remove(session.merge(entity));
			if (propia) {
				session.getTransaction().commit();
			}
		} catch (RuntimeException e) {
			if (!propia) {
				throw e;
			}
			handleException(session, e);
		}
	}

	@Override
	public List<T> list() {
		Session session = sessionFactory.getCurrentSession();
		boolean propia = !session.getTransaction().isActive();
		try {
			if (propia) {
				session.beginTransaction();
			}
			List<T> entities = session.createQuery("FROM " + getEntityClass().getName(), getEntityClass())
					.getResultList();
			if (propia) {
				session.getTransaction().commit();
			}
			return entities;
		} catch (RuntimeException e) {
			if (!propia) {
				throw e;
			}
			handleException(session, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected void handleException(Session session, RuntimeException e) {
		e.printStackTrace();
		if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
			session.getTransaction().rollback();
		}
	}
}
