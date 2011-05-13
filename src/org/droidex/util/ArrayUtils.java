package org.droidex.util;

import java.lang.reflect.Array;

public class ArrayUtils
{
	public static <T> T[] concat(T[] first, T[]... rest)
	{
		int totalLength = first.length;
		for (T[] arr : rest) {
			totalLength += arr.length;
		}

		T[] result = (T[]) Array.newInstance(first.getClass(), totalLength);
		System.arraycopy(first, 0, result, 0, first.length);
		int pos = first.length;

		for (T[] arr : rest) {
			System.arraycopy(arr, 0, result, pos, arr.length);
			pos += arr.length;
		}

		return result;
	}
	
	public static byte[] concat(byte[] first, byte[]... rest)
	{
		int totalLength = first.length;
		for (byte[] arr : rest) {
			totalLength += arr.length;
		}

		byte[] result = new byte[totalLength];
		System.arraycopy(first, 0, result, 0, first.length);
		int pos = first.length;

		for (byte[] arr : rest) {
			System.arraycopy(arr, 0, result, pos, arr.length);
			pos += arr.length;
		}

		return result;
	}
}
