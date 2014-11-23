package com.geepi.network.adapter;

import com.geepi.network.AsyncHttpClient;
import com.geepi.network.AsyncHttpResponseHandler;
import com.geepi.network.RequestParams;

public class WXRequest {
/*	*//**
	 * 正式开发API URL
	 */
	private static final String BASE_URL = "http://geepiqixing.jd-app.com";
	/**
	 * 测试开发API URL
	 */
//	private static final String BASE_URL = "http://apitest.ttexx.com";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
