package com.teamtreehouse.giflib.dao;

import com.teamtreehouse.giflib.model.Category;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        //open session
        Session session = sessionFactory.openSession();

        //get all categories with hibernate criteria object
        List<Category> categories = session.createCriteria(Category.class).list();

        //close session
        session.close();

        return categories;
    }

    @Override
    public Category findById(Long id) {
        Session session = sessionFactory.openSession();
        Category category = session.get(Category.class, id);
        Hibernate.initialize(category.getGifs());
        session.close();
        return category;
    }

    @Override
    public void save(Category category) {
        //open session
        Session session = sessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //save category
        session.saveOrUpdate(category);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();
    }

    @Override
    public void delete(Category category) {
        //open session
        Session session = sessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //delete category
        session.delete(category);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();
    }
}
