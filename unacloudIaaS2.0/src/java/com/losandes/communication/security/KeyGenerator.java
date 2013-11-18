package com.losandes.communication.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.SecureRandom;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * RSA random key generation.
 */
public class KeyGenerator {
    /**
     * The provider used to generate RSA keys
     */
    private static Provider p=new BouncyCastleProvider();
    /**
     * Generates a RSA key pair with the given size
     * @param size
     * @return
     */
    public KeyPair generateKeys(int size) {
        try {
            SecureRandom random = new SecureRandom();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA",p);
            generator.initialize(size, random);
            return generator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
