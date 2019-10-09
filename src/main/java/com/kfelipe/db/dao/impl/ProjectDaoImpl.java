package com.kfelipe.db.dao.impl;

import com.kfelipe.db.Project;
import com.kfelipe.db.dao.ProjectDao;
import com.kfelipe.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProjectDaoImpl implements ProjectDao {

    private static ProjectDaoImpl INSTANCE = null;

    public static ProjectDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProjectDaoImpl();
        }
        return INSTANCE;
    }

    public Long save(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            return (Long) session.save(project);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public Project getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.byId(Project.class).getReference(id);
        }
    }

    public List<Project> getAllProjects() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Project", Project.class).list();
        }
    }
}
