package com.example.rest.models;


import lombok.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;


@Data
@Getter @Setter
@Builder
@Entity
@XmlRootElement
@NoArgsConstructor
public class Utilisateur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private  String firstname;
    private String lastname;
    private  String login;
    private String password;
    private boolean isAdmin;
    private  String salt ;

    public Utilisateur(int id, String firstname, String lastname, String login, String password, boolean isAdmin, String salt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.salt = salt;
    }

    public void   setPassword(String password) {
        //string containing allowed characters, modify according to your needs
        String strAllowedCharacters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        //initialize Random
        Random random = new Random();
        StringBuilder sbRandomString = new StringBuilder(20);

        for(int i = 0 ; i < 20; i++){

            //get random integer between 0 and string length
            int randomInt = random.nextInt(strAllowedCharacters.length());

            //get char from randomInt index from string and append in StringBuilder
            sbRandomString.append(strAllowedCharacters.charAt(randomInt) );
        }
        //this.salt  = sbRandomString.toString() ;
        this.password = get_SHA_512_SecurePassword(password, "salt");
    }

    public String  get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}

