/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progetto_finale;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author marco
 */
public class CatalogoDao {
    public Catalogo getCatalogo(int id) {

		Transaction transaction = null;
		Catalogo user = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();
			// get an user object
			user = session.get(Catalogo.class, id);
			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Delete User
	 * 
	 * @param id
	 */
	public Boolean deleteCatalogo(int id) {
            Transaction transaction = null;
            // Uso il try-with-resources per la sessione (va bene qui perché non facciamo query complesse dopo il catch)
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();

                Catalogo catalogo = session.get(Catalogo.class, id);
                if (catalogo != null) {
                    session.remove(catalogo);
                    transaction.commit(); // <-- COMMIT PRIMA DEL RETURN
                    System.out.println("user is deleted");
                    return true; 
                }
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return false;
        }


	/**
	 * Get all Films
	 * 
	 * @return
	 */
	public List<Catalogo> getAllCatalogo() {
            Transaction transaction = null;
            List<Catalogo> listOfUser = null;
            Session session = null; 

            try {
                // 1. Apri la sessione manualmente (senza try-with-resources)
                session = HibernateUtil.getSessionFactory().openSession();
                transaction = session.beginTransaction();

                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Catalogo> criteria = builder.createQuery(Catalogo.class);
                Root<Catalogo> contactRoot = criteria.from(Catalogo.class);
                criteria.select(contactRoot);

                listOfUser = session.createQuery(criteria).getResultList();

                transaction.commit();
            } catch (Exception e) {
                // 2. Adesso la sessione è ancora aperta e il rollback funzionerà
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                // 3. Chiudi la sessione solo qui alla fine
                if (session != null && session.isOpen()) {
                    session.close();
                }
            } 
            return listOfUser;
        }

}
