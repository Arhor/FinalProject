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
 * Class EncryptAction provides encryption service
 *
 * @author Maxmim Burishinets
 * @version 1.0 03 Sep 2018
 */
public class EncryptAction {

    private static final String ENCRYPTION_TYPE = "SHA-512";

    private EncryptAction() {}

    /**
     * Method takes String value to encrypt and object that will be used as
     * encryption 'salt' and returns 129-digits hash-code of passed value
     *
     * @param toEncrypt User's input to encrypt
     * @param salt Object that will be converted to text string and
     *             used as encryption salt
     * @param <T>  Generic object-type for encryption 'salt' object
     * @return String representation of HEX number as result of encryption
     * @throws ProjectException exception occurs when one (or both) of passed
     * parameters are 'null'
     */
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
