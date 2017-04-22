package com.feigebbm.utils;

import java.io.UnsupportedEncodingException;

public class ToUTF8String {
	public static String UTF8String(String str) throws UnsupportedEncodingException{
		return new String(str.getBytes("iso-88591"),"utf-8");
	}
}
