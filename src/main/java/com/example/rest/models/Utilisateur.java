package com.example.rest.models;


import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Data
@Getter @Setter
@Builder
@Entity

@NoArgsConstructor
@XmlRootElement

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

    public void setPassword(String password) {
        // get the salt and create the password
        this.salt = HashService.gensalt() ;
        this.password = get_SHA_512_SecurePassword(password,this.getSalt());
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

