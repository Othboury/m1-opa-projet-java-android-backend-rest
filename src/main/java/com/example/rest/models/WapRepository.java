package com.example.rest.models;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class WapRepository  implements  IWapRepository{
    @Override
    public void save(WifiPoint wifiPoint) {
        entityManager.getTransaction().begin();
        entityManager.persist(wifiPoint);
        entityManager.getTransaction().commit();
        System.out.println("saved wifipoint ");

    }
    @Override
    public List<WifiPoint> findAll() {
        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("select s from WifiPoint s");
        entityManager.getTransaction().commit();

        return q.getResultList();
    }

    public WapRepository() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
        entityManager = entityManagerFactory.createEntityManager();
        // fullTextSession =  Search.getFullTextEntityManager(entityManager);
    }

    public static WapRepository getInstance() {
        if (serverInstance == null) {
            serverInstance = new WapRepository();
        }
        return serverInstance;
    }
    private static WapRepository serverInstance;
    public static EntityManager entityManager;
}
