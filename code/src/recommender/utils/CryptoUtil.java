package recommender.utils;

import java.security.MessageDigest;
import java.security.Security;

import org.apache.commons.codec.binary.Hex;

public class CryptoUtil {
	
	/**
	 * Digests a String data into a Crypted Byte Array
	 * @param data String data
	 * @return Byte Crypted Byte Array
	 * @throws RecommenderException
	 */
	public static byte[] digestToBinary(String data) throws RecommenderException {
		byte[] result = null;
		
		try
		{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			
			byte[] clearText = data.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(clearText);
			
			result = messageDigest.digest();
		}
		catch(Exception ex) {
			throw new RecommenderException(RecommenderException.MSG_ERROR_CRYPTO);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Encrypts the data using the password algorithm
	 * @param data Data to encrypt
	 * @return Encrypted password
	 * @throws RecommenderException
	 */
	public static String encyptPassword(String data) throws RecommenderException {
		String result = null;
		
		try
		{
			String salt = String.valueOf(System.currentTimeMillis());
			salt = salt.substring(salt.length() - 4);
			
			result = Hex.encodeHexString(CryptoUtil.digestToBinary(data + salt)) + salt;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			result = null;
			
			throw new RecommenderException(RecommenderException.MSG_ERROR_CRYPTO);
		}
		
		return result;
	}
}
