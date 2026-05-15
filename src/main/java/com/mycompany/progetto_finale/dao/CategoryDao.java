package com.mycompany.progetto_finale.dao;

import com.mycompany.progetto_finale.dto.CategoryPayload;
import com.mycompany.progetto_finale.model.Category;
import com.mycompany.progetto_finale.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CategoryDao {

    public List<Category> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Category order by categoryId", Category.class).list();
        }
    }

    public Category findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Category.class, id);
        }
    }

    public Category create(CategoryPayload payload) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Integer nextId = session.createNativeQuery(
                    "select coalesce(max(CategoryID), 0) + 1 from Categories", Integer.class
            ).getSingleResult();

            Category category = new Category();
            category.setCategoryId(nextId);
            category.setCategoryName(payload.getCategoryName());
            category.setDescription(payload.getDescription());

            session.persist(category);
            transaction.commit();
            return category;
        } catch (Exception ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    public Category update(int id, CategoryPayload payload) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Category category = session.get(Category.class, id);
            if (category == null) {
                transaction.rollback();
                return null;
            }

            category.setCategoryName(payload.getCategoryName());
            category.setDescription(payload.getDescription());

            session.merge(category);
            transaction.commit();
            return category;
        } catch (Exception ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    public boolean delete(int id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Category category = session.get(Category.class, id);
            if (category == null) {
                transaction.rollback();
                return false;
            }

            session.remove(category);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    private void rollbackQuietly(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
