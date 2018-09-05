/*
 * class: Coder
 */

package by.epam.admission.dao;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Maxmim Burishinets
 * @version 1.0 03 Sep 2018
 */
public class Coder {

    private static final Logger LOG = LogManager.getLogger(Coder.class);

    public <T> String encrypt(String toEncrypt, T salt) {
        String result = null;
        try {
            String saltString = String.valueOf(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(saltString.getBytes(StandardCharsets.UTF_8));
            byte[] rawBytes = toEncrypt.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = md.digest(rawBytes);
            result = HexBin.encode(encryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Encryption error", e);
        }
        return result;
    }
}
