package com.example.rest.models;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;

public class HashService {

    public static String hash(String passwordToHash ,String salt){
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
    public static String gensalt(){
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
        return sbRandomString.toString();
    }
    public static String getIpAdress() throws SocketException {
        Enumeration e1 = NetworkInterface.getNetworkInterfaces();
        while(e1.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e1.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
            }
        }
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                //System.out.println(interfaces.nextElement());
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                //System.out.println("Interface"+ iface.toString());
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // *EDIT*
                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                    if (iface.getDisplayName().contains("Wireless"))
                        return ip;;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return  null ;
    }
}
