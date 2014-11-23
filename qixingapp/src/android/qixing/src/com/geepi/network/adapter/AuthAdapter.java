package com.geepi.network.adapter;

import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geepi.common.LogUtil;
import com.geepi.common.NameDef;
import com.geepi.common.Util;
import com.geepi.network.JsonHttpResponseHandler;
import com.geepi.network.RequestParams;

import android.util.Log;

public class AuthAdapter {
	private static WXRequestAdapterInterface _requestInterface = null;
	public void setListener(WXRequestAdapterInterface requestInterface){
		_requestInterface = requestInterface;
	}
	
	public static int optTag = -1;
	public static Object responseObj = null;
	public static int resultCode = -1;
	
	//"RestUSER_BASE"
	public void loginAuth(int tag,String username, String password){
		optTag = tag;
		RequestParams params = new RequestParams();
    	params.put(NameDef.kUserName,username);
    	params.put(NameDef.kPassWord,password);
    	WXRequest.post(NameDef.kValMethodLogin,params, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
            	LogUtil.d("http success..." + statusCode);         
            	resultCode = 0;
            	if (statusCode != HttpStatus.SC_OK) {
                	resultCode = NameDef.kRequestError;
            	} else {
                	HashMap<String, Object> tempResponse = (HashMap<String, Object>) Util.getMapForJson(response);                	
                	resultCode = (Integer) tempResponse.get(NameDef.kResponseStatus);
                	if (resultCode == 0) {
                		responseObj = tempResponse.get(NameDef.kResponseResult);
                		tempResponse = (HashMap<String, Object>) Util.getMapForJson((JSONObject) responseObj);          
                	}
            	}
            	_requestInterface.ZGWResponseJsonDelegate(resultCode,optTag,responseObj);
            }

			@Override
            public void onSuccess(int statusCode, Header[] headers,
            		JSONArray response) {
            	// TODO Auto-generated method stub
            	LogUtil.d("http success..." + statusCode);
            	responseObj = response;
            	resultCode = 0;
            	if (statusCode != HttpStatus.SC_OK) {
                	resultCode = NameDef.kRequestError;
            	} 
            	_requestInterface.ZGWResponseJsonDelegate(resultCode,optTag,responseObj);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers,
            		Throwable throwable, JSONArray errorResponse) {
            	// TODO Auto-generated method stub
            	Log.d("tag", "statusCode:" + statusCode);
            	resultCode = NameDef.kTimeOut;
            	responseObj = errorResponse;
            	_requestInterface.ZGWResponseJsonDelegate(resultCode,optTag,responseObj);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers,
            		Throwable throwable, JSONObject errorResponse) {
            	// TODO Auto-generated method stub
            	Log.d("tag", "statusCode:" + statusCode);
            	resultCode = NameDef.kTimeOut;
            	responseObj = errorResponse;
            	_requestInterface.ZGWResponseJsonDelegate(resultCode,optTag,responseObj);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            	Log.d("tag", "statusCode:" + statusCode);
            	resultCode = NameDef.kRequestError;
            	responseObj = responseString;
            	_requestInterface.ZGWResponseJsonDelegate(resultCode,optTag,responseObj);
            }
        });
	}
	
	
}
