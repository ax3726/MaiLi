package com.gsy.ml.ui.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private static String md5Key = "/1*7aBq9-ml"; // 混合将要加密的MD5密码串

	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public MD5() {
	}

	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 返回形式只为数字
	private static String byteToNum(byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	// 转换字节数组为16进制字串
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String GetMD5Code(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	// 密码加密 与php加密一致
	public static String md5php(String input) throws NoSuchAlgorithmException {
		String result = input;
		if (input != null) {
			MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while (result.length() < 32) {
				result = "0" + result;
			}
		}
		return result;
	}

	/**
	 * 对本APP密码进行加密 主要是讲用户的用户+密码+KEY，的字符串进行MD5加密。然后摘取第7到27位作为密码信息摘要
	 * 
	 * @param pwd
	 */
	public static String MD5Pwd(String username, String pwd) {

		String str = username + pwd + md5Key;
		String md5str = GetMD5Code(str);
		String md5Pwd = md5str.substring(7 , 27);
		return md5Pwd;
	}

	/**
	 * 对本APP密码进行加密 主要是讲用户的用户+密码+KEY，的字符串进行MD5加密。然后摘取第7到27位作为密码信息摘要
	 */
	public static String MD5Sign(String keyStr) {
		//Log.e("keyStr",keyStr);
		String md5str = GetMD5Code(keyStr);
		String md5Pwd = md5str.substring(3, 13)+md5str.substring(13, 23);
		return md5Pwd;
	}

	/**
	 * 对本APP密码进行加密 主要是讲用户的用户+密码+KEY，的字符串进行MD5加密。然后摘取第7到27位作为密码信息摘要
	 */
	public static String MD5Sign2(String keyStr,String key) {
		//Log.e("keyStr",keyStr);
		String md5str = GetMD5Code(keyStr);
		String md5str2=GetMD5Code(md5str+key);
		String md5Pwd = md5str.substring(3, 13)+md5str2.substring(13, 23);
		return md5Pwd;
	}
}