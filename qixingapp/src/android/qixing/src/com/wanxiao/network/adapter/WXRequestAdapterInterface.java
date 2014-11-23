package com.wanxiao.network.adapter;

import java.io.InputStream;


public interface WXRequestAdapterInterface {
	
	public void ZGWResponseBinaryDelegate(InputStream responseData,int resultCode,int tag);
	public void ZGWResponseJsonDelegate(int resultCode,int tag,Object response);
	public void ZGWResponseProgressDelegate(int bytesWritten, int totalSize);
}
