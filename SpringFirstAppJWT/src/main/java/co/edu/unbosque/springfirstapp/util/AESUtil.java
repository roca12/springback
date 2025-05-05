package co.edu.unbosque.springfirstapp.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidad para operaciones de cifrado AES y funciones de hash.
 * Proporciona métodos para cifrar y descifrar texto usando AES en modo GCM,
 * así como métodos para generar hashes usando varios algoritmos (MD5, SHA1, SHA256, etc.).
 */
public class AESUtil {

	/**
	 * Algoritmo de cifrado utilizado (AES).
	 */
	private final static String ALGORITMO = "AES";

	/**
	 * Modo de cifrado y padding utilizados (AES en modo GCM sin padding).
	 */
	private final static String TIPOCIFRADO = "AES/GCM/NoPadding";

	/**
	 * Cifra un texto utilizando AES en modo GCM.
	 * 
	 * @param llave Clave de cifrado (debe tener 16 caracteres para AES-128)
	 * @param iv Vector de inicialización (IV) para el cifrado
	 * @param texto Texto a cifrar
	 * @return Texto cifrado en formato Base64
	 */
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

	/**
	 * Descifra un texto cifrado con AES en modo GCM.
	 * 
	 * @param llave Clave de cifrado (debe ser la misma utilizada para cifrar)
	 * @param iv Vector de inicialización (debe ser el mismo utilizado para cifrar)
	 * @param encrypted Texto cifrado en formato Base64
	 * @return Texto descifrado
	 */
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

	/**
	 * Descifra un texto utilizando una clave y vector de inicialización predeterminados.
	 * 
	 * @param encrypted Texto cifrado en formato Base64
	 * @return Texto descifrado
	 */
	public static String decrypt(String encrypted) {
		String iv = "programacioncomp";
		String key = "llavede16carater";
		return decrypt(key, iv, encrypted);
	}

	/**
	 * Cifra un texto utilizando una clave y vector de inicialización predeterminados.
	 * 
	 * @param plainText Texto a cifrar
	 * @return Texto cifrado en formato Base64
	 */
	public static String encrypt(String plainText) {
		String iv = "programacioncomp";
		String key = "llavede16carater";
		return encrypt(key, iv, plainText);
	}

	/**
	 * Genera un hash MD5 del contenido proporcionado.
	 * 
	 * @param content Texto a convertir en hash
	 * @return Representación hexadecimal del hash MD5
	 */
	public static String hashingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}

	/**
	 * Genera un hash SHA-1 del contenido proporcionado.
	 * 
	 * @param content Texto a convertir en hash
	 * @return Representación hexadecimal del hash SHA-1
	 */
	public static String hashingToSHA1(String content) {
		return DigestUtils.sha1Hex(content);
	}

	/**
	 * Genera un hash SHA-256 del contenido proporcionado.
	 * 
	 * @param content Texto a convertir en hash
	 * @return Representación hexadecimal del hash SHA-256
	 */
	public static String hashingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}

	/**
	 * Genera un hash SHA-384 del contenido proporcionado.
	 * 
	 * @param content Texto a convertir en hash
	 * @return Representación hexadecimal del hash SHA-384
	 */
	public static String hashingToSHA384(String content) {
		return DigestUtils.sha384Hex(content);
	}

	/**
	 * Genera un hash SHA-512 del contenido proporcionado.
	 * 
	 * @param content Texto a convertir en hash
	 * @return Representación hexadecimal del hash SHA-512
	 */
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
//		String contrasena="soyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseña";
//		System.out.println(hashingToMD5(contrasena));
//		System.out.println(hashingToSHA1(contrasena));
//		System.out.println(hashingToSHA256(contrasena));
//		System.out.println(hashingToSHA384(contrasena));
//		System.out.println(hashingToSHA512(contrasena));
//	}

}
