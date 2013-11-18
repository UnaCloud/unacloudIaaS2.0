/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication.security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.losandes.communication.security.utils.*;

/**
 * Secure server side stream used to communicate with clients
 * @author Clouder
 */
public class SecureServerStream extends AbstractCommunicator{

    /**
     * RSA modulus and exponent used to cipher this secure stream
     */
    private static String modulus="11986832794202572811",exponent="1354570450620015473";
    static{
        try{
            BufferedReader br = new BufferedReader(new FileReader("secrets.txt"));
            modulus=br.readLine();
            exponent=br.readLine();
            br.close();
        }catch(Exception e){

        }
    }

    /**
     * Changes the keys used by the instances of this secure stream
     * @param mod
     * @param exp
     * @return
     */
    public static boolean setKeys(String mod,String exp){
        try{
            PrintWriter pw = new PrintWriter("secrets.txt");
            pw.println(mod);
            pw.println(exp);
            pw.close();
            modulus=mod;
            exponent=exp;
            return true;
        }catch(Exception e){

        }
        return false;
    }

    /**
     * Instantiates a new secure stream
     * @throws ConnectionException
     */
    public SecureServerStream()throws ConnectionException{
        Security.addProvider(new BouncyCastleProvider());
            RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus, 10),new BigInteger(exponent, 10));
            
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            key = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);
        } catch (Exception ex) {
            throw new ConnectionException("Unable to create key");
        }
        try {
            cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
        } catch (Exception ex) {
            throw new ConnectionException("Unable to create cipher");
        }
    }

}
