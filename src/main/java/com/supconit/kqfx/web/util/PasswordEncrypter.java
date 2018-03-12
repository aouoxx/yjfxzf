/**
 * 
 */
package com.supconit.kqfx.web.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zongkai
 *
 */
public class PasswordEncrypter {
	// 默认密钥
	private static String strDefaultKey = "changfengPolice";
    
	/**
	 * DES加密
	 * @param password 密码
	 * @return String 密文
	 * @exception 
	 * @since  1.0.0
	 */
    public static String encrypt(String password) {
	    try {
			Key key = getKey(strDefaultKey.getBytes());
			Cipher encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			return byteArr2HexStr(encryptCipher.doFinal(password.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
     * DES解密
     * @param strIn 密文
     * @return String 明文
     * @exception 
     * @since  1.0.0
     */
    public static String decrypt(String strIn) {
    	try {
			Key key = getKey(strDefaultKey.getBytes());
			Cipher decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
			return new String(decryptCipher.doFinal(hexStr2ByteArr(strIn)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	  }

    /**  
	   * 将byte数组转换为表示16进制值的字符串 
	   * 和hexStr2ByteArr(String strIn) 互为可逆的转换过程  
	   * @param bytes  需要转换的byte数组  
	   * @return 转换后的字符串  
	   * @throws Exception  
	   */
	  private static String byteArr2HexStr(byte[] bytes) throws Exception {
	    int iLen = bytes.length;
	    // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍   
	    StringBuffer sb = new StringBuffer(iLen * 2);
	    for (int i = 0; i < iLen; i++) {
	      int intTmp = bytes[i];
	      // 把负数转换为正数   
	      while (intTmp < 0) {
	        intTmp = intTmp + 256;
	      }
	      // 小于0F的数需要在前面补0   
	      if (intTmp < 16) {
	        sb.append("0");
	      }
	      sb.append(Integer.toString(intTmp, 16));
	    }
	    return sb.toString();
	  }

	  /**  
	   * 将表示16进制值的字符串转换为byte数组
	   *  和byteArr2HexStr(byte[] bytes)互为可逆的转换过程  
	   * @param strIn 需要转换的字符串  
	   * @return 转换后的byte数组
	   * @throws Exception  
	   */
	  public static byte[] hexStr2ByteArr(String strIn) throws Exception {
	    byte[] bytes = strIn.getBytes();
	    int iLen = bytes.length;

	    // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2   
	    byte[] arrOut = new byte[iLen / 2];
	    for (int i = 0; i < iLen; i = i + 2) {
	      String strTmp = new String(bytes, i, 2);
	      arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
	    }
	    return arrOut;
	  }
	  
	  /**  
	   * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位  
	   * @param arrBTmp 构成该字符串的字节数组  
	   * @return 生成的密钥  
	   * @throws Exception
	   */
	  private static Key getKey(byte[] arrBTmp) throws Exception {
	    // 创建一个空的8位字节数组（默认值为0）   
	    byte[] arrB = new byte[8];

	    // 将原始字节数组转换为8位   
	    for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
	      arrB[i] = arrBTmp[i];
	    }

	    // 生成密钥   
	    Key key = new SecretKeySpec(arrB, "DES");

	    return key;
	  }
}
