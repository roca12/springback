@ -16,12 +16,31 @@ import java.security.InvalidAlgorithmParameterException;
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
@ -48,6 +67,14 @@ public class AESUtil {
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
@ -78,34 +105,76 @@ public class AESUtil {

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
		return  DigestUtils.md5Hex(content);
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
