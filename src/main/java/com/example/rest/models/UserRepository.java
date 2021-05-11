package com.example.rest.models;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.rmi.CORBA.Util;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Observable;


/**
 *
 * This class implement The IUserrepository
 *
 */
@Data
@Getter @Setter
public class UserRepository extends Observable implements IUserrepository {

    Utilisateur currentUser ;

    @Override
    public void save(Utilisateur utilisateur) {
        entityManager.getTransaction().begin();
        entityManager.persist(utilisateur);
        entityManager.getTransaction().commit();
        System.out.println("saved user : ");

    }

    @Override
    public List<Utilisateur> findAll() {
        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("select s from Utilisateur s");
        entityManager.getTransaction().commit();
        setChanged();
        notifyObservers();

        return q.getResultList();

    }

    @Override
    public void update(Utilisateur user) {
        try {
            
            entityManager.getTransaction().begin();
            Query q = entityManager.createQuery("update Utilisateur u set u.firstname = :valuefname, u.lastname = :valuelname, u.login = : valuelogin where u.id= :value");
            q.setParameter("valuefname",  user.getFirstname());
            q.setParameter("valuelname",  user.getLastname());
            q.setParameter("valuelogin",  user.getLogin());
            q.setParameter("value",  user.getId());
            int rowsUpdated = q.executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void  delete(int id) {
        entityManager.getTransaction().begin();
        Utilisateur utilisateur =entityManager.find(Utilisateur.class ,id );
        entityManager.remove( utilisateur);
        entityManager.getTransaction().commit();
    }

    /**
     * Set new Admin
     *
     * @param user
     */
    @Override
    public void setAdmin(Utilisateur user) {
        try {
            entityManager.getTransaction().begin();
            Query q = entityManager.createQuery("update Utilisateur u set u.isAdmin = 1 where u.id= :value");
            q.setParameter("value",  user.getId());
            int rowsUpdated = q.executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Remove Admin
     *
     * @param user
     */
    @Override
    public void RemoveAdmin(Utilisateur user) {
        try {
            entityManager.getTransaction().begin();
            Query q = entityManager.createQuery("update Utilisateur u set u.isAdmin = 0 where u.id= :value");
            q.setParameter("value",  user.getId());
            int rowsUpdated = q.executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Utilisateur findById(int uuid) {
        entityManager.getTransaction().begin();
        Utilisateur utilisateur =entityManager.find(Utilisateur.class ,uuid );
        entityManager.getTransaction().commit();
        return utilisateur;
    }

    @Override
    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
        entityManager.getTransaction().begin();
        Utilisateur  aut1 = Utilisateur.builder()
                .firstname("pape mor")
                .lastname("cisse")
                .login("pucisse200")
                .password("Djingue158#")
                .isAdmin(true)
                .build();
        Utilisateur  aut2 = Utilisateur.builder()
                .firstname("admin")
                .lastname("admin")
                .login("admin")
                .password("admin")
                .isAdmin(false)
                .build();

        entityManager.persist(aut1);

        entityManager.persist(aut2);
        entityManager.getTransaction().commit();
    }
    @Override
    public boolean exist(String login, String password) {
         currentUser  = new Utilisateur() ;
        boolean bool = false ;
        currentUser.setLogin(login);
        currentUser.setPassword(password);

        List<Utilisateur>  users  = findAll() ;

        for (Utilisateur use : users){
            System.out.println(use.getPassword());
            if(use.getPassword().equals((currentUser.getPassword()))){ bool=true;
            currentUser = use ;}
        }
        return bool;
    }

    public UserRepository() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
        entityManager = entityManagerFactory.createEntityManager();
        // fullTextSession =  Search.getFullTextEntityManager(entityManager);

    }

    public static UserRepository getInstance() {
        if (serverInstance == null) {
            serverInstance = new UserRepository();
        }
        return serverInstance;
    }
    private static UserRepository serverInstance;
    public static EntityManager entityManager;

}

