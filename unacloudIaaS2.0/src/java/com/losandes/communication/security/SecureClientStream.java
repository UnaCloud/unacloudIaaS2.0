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
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
/**
 *
 * @author Clouder
 */
public class SecureClientStream extends AbstractCommunicator {

    private static String modulus="11986832794202572811",exponent="65537";
    static{
        try{
            BufferedReader br = new BufferedReader(new FileReader("version/secretsPublic.txt"));
            modulus=br.readLine();
            exponent=br.readLine();
            br.close();
        }catch(Exception e){

        }
    }
    public static boolean setKeys(String mod,String exp){
        try{
            PrintWriter pw = new PrintWriter("version/secretsPublic.txt");
            pw.println(mod);
            pw.println(exp);
            pw.close();
            modulus=mod;
            exponent=exp;
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public SecureClientStream() throws ConnectionException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            RSAPublicKeySpec privKeySpec = new RSAPublicKeySpec(new BigInteger(modulus, 10), new BigInteger(exponent, 10));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            key = (RSAPublicKey) keyFactory.generatePublic(privKeySpec);
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
    

