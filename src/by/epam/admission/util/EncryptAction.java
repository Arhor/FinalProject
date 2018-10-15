/*
 * class: EncryptAction
 */

package by.epam.admission.util;

import by.epam.admission.exception.ProjectException;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Maxmim Burishinets
 * @version 1.0 03 Sep 2018
 */
public class EncryptAction {

    private static final String ENCRYPTION_TYPE = "SHA-512";

    private EncryptAction() {}

    public static <T> String encrypt(String toEncrypt, T salt)
            throws ProjectException {
        if (toEncrypt == null || salt == null) {
            throw new ProjectException("Wrong encryption arguments");
        }
        String result;
        try {
            String saltString = String.valueOf(salt);
            MessageDigest md = MessageDigest.getInstance(ENCRYPTION_TYPE);
            md.update(saltString.getBytes(StandardCharsets.UTF_8));
            byte[] rawBytes = toEncrypt.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = md.digest(rawBytes);
            result = HexBin.encode(encryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ProjectException("Encryption error", e);
        }
        return result;
    }
}
