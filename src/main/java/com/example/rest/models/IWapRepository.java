package com.example.rest.models;

import java.util.List;

public interface IWapRepository {
    /**
     *  Save a WifiPoint
     * @param wifiPoint
     */
    public void save(WifiPoint wifiPoint);

    /**
     *  Fetch all WifiPoints
     * @return
     */
    public List<WifiPoint> findAll() ;
}
