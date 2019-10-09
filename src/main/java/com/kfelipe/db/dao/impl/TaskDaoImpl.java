package com.kfelipe.db.dao.impl;

import com.kfelipe.db.Project;
import com.kfelipe.db.Task;
import com.kfelipe.db.dao.TaskDao;
import com.kfelipe.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TaskDaoImpl implements TaskDao {

    private static TaskDao INSTANCE = null;

    public static TaskDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskDaoImpl();
        }
        return INSTANCE;
    }

    public Long save(Task task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            return (Long) session.save(task);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public Task getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.byId(Task.class).getReference(id);
        }
    }

    @Override
    public List<Task> getMainTasksByProject(Project project) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "from Task where parentTask is null AND projectId = " + project.getId();
            return session.createQuery(query , Task.class)
                    .list();
        }
    }

    @Override
    public Task getTaskByIdAndProject(Long id, Project project) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "from Task where id=" + id + " AND projectId = " + project.getId();
            return session.createQuery(query , Task.class)
                    .uniqueResult();
        }
    }

}
