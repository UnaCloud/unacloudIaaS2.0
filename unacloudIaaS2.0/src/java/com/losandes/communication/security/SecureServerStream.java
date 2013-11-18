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
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
/**
 *
 * @author Clouder
 */
public class SecureServerStream extends AbstractCommunicator{

    private static BigInteger modulus=new BigInteger("11986832794202572811",10),exponent=new BigInteger("1354570450620015473",10);
    static{
        try{
            BufferedReader br = new BufferedReader(new FileReader("secrets.txt"));
            modulus=new BigInteger(br.readLine(),10);
            exponent=new BigInteger(br.readLine(),10);
            br.close();
        }catch(Exception e){

        }
    }
    public static boolean setKeys(String mod,String exp){
        try{
            PrintWriter pw = new PrintWriter("secrets.txt");
            pw.println(mod);
            pw.println(exp);
            pw.close();
            modulus=new BigInteger(mod,10);
            exponent=new BigInteger(exp,10);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public SecureServerStream()throws ConnectionException{
        Security.addProvider(new BouncyCastleProvider());
            RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(modulus,exponent);
            
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
