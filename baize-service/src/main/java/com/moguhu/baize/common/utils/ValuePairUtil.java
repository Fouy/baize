package com.moguhu.baize.common.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValuePairUtil {

	
	public static List <NameValuePair> dozerListPair(Map<String,String> map){
		
		List <NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String ksys : map.keySet()) {
    		NameValuePair nv = new BasicNameValuePair(ksys, map.get(ksys));
    		nvps.add(nv);
		}
		return nvps;
	}
	
}
