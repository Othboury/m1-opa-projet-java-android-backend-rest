package com.example.rest.models;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;


/**
 *
 *  This Interface describe all the interaction with the database
 *
 */
public interface IUserrepository {

    /**
     *
     * Add a new user
     * @param user
     */
    public void save(Utilisateur user);
    /**
     *  Fetch all users
     */
    public List<Utilisateur> findAll() ;
    /**
     * Find an user by his id
     * @param id
     */
    public Utilisateur findById(int id) ;
    /**
     *
     * Update an user infos
     */
    public void update(Utilisateur user) ;
    /**
     *
     * Delete an User by Id
     * @param id
     */
    public void delete(int id ) ;

    /**
     *
     * Initilialize the database
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Check if an user exist in the databse
     * @param login
     * @param password
     */
    public boolean exist(String login , String password) ;
}
