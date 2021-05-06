package com.example.rest.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class WifiPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int     id ;
    private  String  ssid  ;
    private  String  bssid ;
    private  String  frequency ;
    private  String  level ;
    private  String  centrefrequence0  ;
    private BigInteger date ;
    private  String  salle  ;
    @ManyToOne
    private Utilisateur utilisateur;
}

