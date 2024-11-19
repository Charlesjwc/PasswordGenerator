package utils;
import utils.*;

import java.util.*;
import java.io.*;

public class Encryptor {
	/**	Encrypts the a String using a char[] key and int[] index
	 * 	@param	String	to encrypt
	 * 	@param	char[]	key for encryption
	 * 	@param	int[]	index of encryption key
	 * 	@return	String	encrypted String
	 */
	public static String encrypt(String str, char[] key, int[] keyIndex) {
		//	Make sure string isn't empty or null
		if (str == null || str.length() == 0)
			return str;
		//	Convert str from String to char[], then call other encrypt method
		return encrypt(str.toCharArray(), key, keyIndex);
	}
	
	/**	Encrypts the a String using a char[] key and int[] index
	 * 	@param	char[]	to encrypt (ARRAY WILL BE CHANGED)
	 * 	@param	char[]	key for encryption
	 * 	@param	int[]	index of encryption key
	 * 	@return	String	encrypted String
	 */
	public static String encrypt(char[] str, char[] key, int[] keyIndex) {
		//	Length of key
		int n = key.length;
		//	Encrypt str
		for (int i = 0; i < str.length; i++) {
			//	Shift char
			int shifted = str[i] + key[keyIndex[0]];
			//	Make sure char is within bounds
			while (shifted < 32)
				shifted += 95;
			while (shifted > 126)
				shifted -= 95;
			
			//	Update char
			str[i] = (char)shifted;
			
			//	Increment keyIndex
			keyIndex[0] = (keyIndex[0] + 1) % key.length;
		}
		//	Return string
		return new String(str);
	}
	
	/**	Decrypts the a String using a char[] key and int[] index
	 * 	@param	String	to decrypt
	 * 	@param	char[]	key for encryption
	 * 	@param	int[]	index of encryption key
	 * 	@return	String	decrypted String
	 */
	public static String decrypt(String str, char[] key, int[] keyIndex) {
		//	Make sure string isn't empty or null
		if (str == null || str.length() == 0)
			return str;
		//	Convert str from String to char[], then call other encrypt method
		return decrypt(str.toCharArray(), key, keyIndex);
	}
	
	/**	Decrypts the a String using a char[] key and int[] index
	 * 	@param	char[]	to decrypt (ARRAY WILL BE CHANGED)
	 * 	@param	char[]	key for encryption
	 * 	@param	int[]	index of encryption key
	 * 	@return	String	decrypted String
	 */
	public static String decrypt(char[] str, char[] key, int[] keyIndex) {
		//	Length of key
		int n = key.length;
		//	Encrypt str
		for (int i = 0; i < str.length; i++) {
			//	Shift char
			int shifted = str[i] - key[keyIndex[0]];
			//	Make sure char is within bounds
			while (shifted < 32)
				shifted += 95;
			while(shifted > 125)
				shifted -= 95;
			
			//	Update char
			str[i] = (char)shifted;
			
			//	Increment keyIndex
			keyIndex[0] = (keyIndex[0] + 1) % key.length;
		}
		return new String(str);
	}
}





