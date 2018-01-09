package com.util;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StringUtil {

	public static class InvalidLengthException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidLengthException(int length) {
			this.length = length;
		}

		public int getLength() {
			return length;
		}

		private int length;
	}

	public static class InvalidCharException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidCharException(int pos) {
			this.pos = pos;
		}

		public int getPos() {
			return pos;
		}

		private int pos;
	}

	/**
	 * 方法名：safeString <br/>
	 * 描述：获取非空的（安全的）字符串。 <br/>
	 * 
	 * @param str
	 *            输入字符串 <br/>
	 * @return 如果输入字符串为null，返回""，否则返回输入的字符串。 <br/>
	 */
	public static String safeString(String str) {
		return (str == null) ? "" : str;
	}

	/**
	 * 方法名：isEmpty <br/>
	 * 描述：判断字符串是否null或长度为0. <br/>
	 * 
	 * @param str
	 *            输入字符串 <br/>
	 * @return boolean <br/>
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * 方法名：safeCompare <br/>
	 * 描述：在用户认为""与null等价的前提下，比较字符串大小。 <br/>
	 * 
	 * @param left
	 *            左操作数 <br/>
	 * @param right
	 *            右操作数 <br/>
	 * @return 如果相等，返回0；左操作数大，返回1；否则，返回-1。 <br/>
	 */
	public static int safeCompare(String left, String right) {
		return safeString(left).compareTo(safeString(right));
	}

	/**
	 * 方法名：safeCompareIgnoreCase <br/>
	 * 描述：在用户认为""与null等价、且大小写不敏感的前提下，比较字符串大小。 <br/>
	 * 
	 * @param left
	 *            左操作数 <br/>
	 * @param right
	 *            右操作数 <br/>
	 * @return 如果相等，返回0；左操作数大，返回1；否则，返回-1。 <br/>
	 */
	public static int safeCompareIgnoreCase(String left, String right) {
		return safeString(left).compareToIgnoreCase(safeString(right));
	}

	/**
	 * 方法名：safeEqual <br/>
	 * 描述：在用户认为""与null等价的前提下，判断字符串是否相等。 <br/>
	 * 
	 * @param left
	 *            左操作数 <br/>
	 * @param right
	 *            右操作数 <br/>
	 * @return 如果相等，返回true，否则false <br/>
	 */
	public static boolean safeEqual(String left, String right) {
		return safeString(left).equals(safeString(right));
	}

	/**
	 * 方法名：bufferToHex <br/>
	 * 描述：十六进制表示字符串 <br/>
	 * 
	 * @param buffer
	 *            字节缓冲区 <br/>
	 * @return 由0-9a-f组成的字符串 <br/>
	 */
	public static String bufferToHex(byte[] buffer) {
		return bufferToHex(buffer, 0, buffer.length);
	}

	/**
	 * 方法名：bufferToHex <br/>
	 * 描述：十六进制表示字符串 <br/>
	 * 
	 * @param buffer
	 *            字节缓冲区 <br/>
	 * @param offset
	 *            格式化的起始位置 <br/>
	 * @param length
	 *            需格式化的长度 <br/>
	 * @return 由0-9a-f组成的字符串 <br/>
	 */
	public static String bufferToHex(byte[] buffer, int offset, int length) {
		final String hexArray = "0123456789abcdef";
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int ch = buffer[offset + i] & 0xff;
			strBuffer.append(hexArray.charAt(ch >> 4));
			strBuffer.append(hexArray.charAt(ch & 0xf));
		}
		return strBuffer.toString();
	}

	/**
	 * 方法名：hexToBuffer <br/>
	 * 描述：十六进制格式化后的字符串还原成字节数组 <br/>
	 * 
	 * @param hex
	 *            由0-9a-fA-F组成的长度为偶数的字符串 <br/>
	 * @return 字节缓冲区 <br/>
	 * @throws StringUtil.InvalidLengthException
	 *             , StringUtil.InvalidCharException<br/>
	 */
	public static byte[] hexToBuffer(String hex) throws InvalidLengthException, InvalidCharException {
		byte[] result = new byte[hex.length() / 2];
		hexToBuffer(hex, result, 0);
		return result;
	}

	/**
	 * 方法名：hexToBuffer <br/>
	 * 描述：十六进制格式化后的字符串还原到字节数组 <br/>
	 * 
	 * @param hex
	 *            由0-9a-fA-F组成的长度为偶数的字符串
	 * @param buffer
	 *            目标字节缓冲区数组
	 * @param pos
	 *            起始位置
	 * @return 返回的长度
	 * @throws StringUtil.InvalidLengthException
	 *             , StringUtil.InvalidCharException<br/>
	 */
	public static int hexToBuffer(String hex, byte[] buffer, int pos) throws InvalidLengthException,
			InvalidCharException {
		if ((hex.length() & 1) != 0) {
			throw new InvalidLengthException(hex.length());
		}

		int result = hex.length() >> 1;

		for (int i = 0; i < hex.length(); i++) {
			char ch = hex.charAt(i);
			if (ch >= '0' && ch <= '9') {
				ch -= '0';
			} else if (ch >= 'a' && ch <= 'f') {
				ch -= 'a';
				ch += 10;
			} else if (ch >= 'A' && ch <= 'F') {
				ch -= 'A';
				ch += 10;
			} else {
				throw new InvalidCharException(i);
			}

			if ((i & 1) == 0) {
				buffer[pos] = (byte) (ch << 4);
			} else {
				buffer[pos++] |= ch;
			}
		}
		return result;
	}

	/**
	 * 方法名：arrayToString <br/>
	 * 描述：格式化数组 <br/>
	 * 
	 * @param objs
	 *            元素为对象的数组 <br/>
	 * @return 字符串 <br/>
	 */
	public static String arrayToString(Object[] objs) {
		if (objs == null) {
			return "null";
		}

		StringBuffer result = new StringBuffer("{");
		for (Object obj : objs) {
			if (result.length() > 1) {
				result.append(",");
			}
			if (obj == null) {
				result.append("null");
			} else {
				result.append("\"");
				result.append(obj);
				result.append("\"");
			}
		}
		result.append("}");
		return result.toString();
	}

	/**
	 * 格式化金额字符串
	 * 
	 * @param money
	 *            金额（单位为分）
	 * @return
	 */
	public static String formatMoney(int money) {
		if (money % 100 == 0) {
			return String.valueOf(money / 100);
		} else if (money % 10 == 0) {
			return new DecimalFormat("#0.0").format((float) money / 100);
		} else {
			return new DecimalFormat("#0.00").format((float) money / 100);
		}
	}

	public static String join(JsonObject json, String separator) {
		if (json == null) {
			return "";
		}
		StringBuilder sbStr = new StringBuilder(64);
		Iterator<Entry<String, JsonElement>> iterator = json.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, JsonElement> next = iterator.next();
			sbStr.append(next.getValue().getAsString()).append(separator);
		}
		if (sbStr.length() > 0) {
			sbStr.deleteCharAt(sbStr.length() - separator.length());
		}
		return sbStr.toString();
	}

	public static String join(JsonArray json, String separator) {
		if (json == null) {
			return "";
		}
		StringBuilder sbStr = new StringBuilder(64);
		for (JsonElement element : json) {
			sbStr.append(element.getAsString()).append(separator);
		}
		if (sbStr.length() > 0) {
			sbStr.deleteCharAt(sbStr.length() - separator.length());
		}
		return sbStr.toString();
	}
}
