package com.wangfei.test;

import org.json.JSONArray;
import org.json.JSONObject;

public class myTest {
	
	public static void main(String[] args) throws Exception {
		String jsonString = "[{'Address':'苏A·W526Z'},{'Address':'京A·12345'}]";
		
		JSONArray jArray = new JSONArray(jsonString);
		System.out.println(jArray.get(0));
		
		
		
	}
			
}
