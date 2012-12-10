package recommender.utils;

import java.security.MessageDigest;
import java.security.Security;

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
	
}
