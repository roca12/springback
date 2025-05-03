package co.edu.unbosque.springfirstapp.util;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;

public class AESUtil {

  private static final String ALGORITMO = "AES";

  private static final String TIPOCIFRADO = "AES/GCM/NoPadding";

  public static String encrypt(String llave, String iv, String texto) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance(TIPOCIFRADO);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    }

    SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
    try {
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }

    byte[] encrypted = null;
    try {
      encrypted = cipher.doFinal(texto.getBytes());
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
    }

    return new String(encodeBase64(encrypted));
  }

  public static String decrypt(String llave, String iv, String encrypted) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance(TIPOCIFRADO);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    }

    SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {

      e.printStackTrace();
    }

    byte[] enc = decodeBase64(encrypted);
    byte[] decrypted = null;
    try {
      decrypted = cipher.doFinal(enc);
      return new String(decrypted);
    } catch (IllegalBlockSizeException | BadPaddingException e) {

      e.printStackTrace();
    }
    return "";
  }

  public static String decrypt(String encrypted) {
    String iv = "programacioncomp";
    String key = "llavede16carater";
    return decrypt(key, iv, encrypted);
  }

  public static String encrypt(String plainText) {
    String iv = "programacioncomp";
    String key = "llavede16carater";
    return encrypt(key, iv, plainText);
  }

  public static String hashingToMD5(String content) {
    return DigestUtils.md5Hex(content);
  }

  public static String hashingToSHA1(String content) {
    return DigestUtils.sha1Hex(content);
  }

  public static String hashingToSHA256(String content) {
    return DigestUtils.sha256Hex(content);
  }

  public static String hashingToSHA384(String content) {
    return DigestUtils.sha384Hex(content);
  }

  public static String hashingToSHA512(String content) {
    return DigestUtils.sha512Hex(content);
  }

  //	public static void main(String[] args) {
  //		String texto = "zambrano lo robaron hace meses";
  //		System.out.println(texto);
  //
  //		String codificado = encrypt(texto);
  //		System.out.println(codificado);
  //
  //		String decodificado = decrypt(codificado);
  //		System.out.println(decodificado);
  //
  //		String
  // contrasena="soyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseña";
  //		System.out.println(hashingToMD5(contrasena));
  //		System.out.println(hashingToSHA1(contrasena));
  //		System.out.println(hashingToSHA256(contrasena));
  //		System.out.println(hashingToSHA384(contrasena));
  //		System.out.println(hashingToSHA512(contrasena));
  //	}

}
