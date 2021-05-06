package com.example.rest.models;

import java.util.List;

public interface IWapRepository {
    /**
     *
     * save a User
     * @param wifiPoint
     */
    public void save(WifiPoint wifiPoint);

    /**
     *
     *  get all Users
     * @return
     */
    public List<WifiPoint> findAll() ;
}
