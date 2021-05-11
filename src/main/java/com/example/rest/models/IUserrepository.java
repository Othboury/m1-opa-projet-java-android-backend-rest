package com.example.rest.models;

import org.glassfish.jersey.internal.util.Producer;

import javax.rmi.CORBA.Util;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;

/**
 *
 *  This Interface describe all the interaction with the database
 *
 */
public interface IUserrepository {

    /**
     *
     * save a User
     * @param user
     */
    public void save(Utilisateur user);

    /**
     *
     *  get all Users
     * @return
     */

    public List<Utilisateur> findAll() ;

    /**
     *
     * Find a user by Id
     * @param id
     */
    public Utilisateur findById(int id) ;

    /**
     *
     * Update a user
     * @param
     */
    public void update(Utilisateur user) ;
    /**
     *
     * Delete a User by Id
     * @param id
     */
    public void delete(int id ) ;
    /**
     *
     * Set new Admin
     * @param user
     */
    public void setAdmin(Utilisateur user);
    /**
     *
     * Remove Admin
     * @param user
     */
    public void RemoveAdmin(Utilisateur user);

    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException;

    public boolean exist(String login , String password) ;
}
