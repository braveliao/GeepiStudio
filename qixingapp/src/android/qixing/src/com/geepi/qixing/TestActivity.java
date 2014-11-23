package com.geepi.qixing;



import java.io.InputStream;

import com.geepi.common.LogUtil;
import com.geepi.network.adapter.AuthAdapter;
import com.geepi.network.adapter.WXRequestAdapterInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity {
	
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private AuthAdapter mAuthAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		button1 = (Button)findViewById(R.id.button1);
		button2 = (Button)findViewById(R.id.button2);
		button3 = (Button)findViewById(R.id.button3);
		button4 = (Button)findViewById(R.id.button4);
		button1.setOnClickListener(myOnClickListenter);
		button2.setOnClickListener(myOnClickListenter);
		button3.setOnClickListener(myOnClickListenter);
		button4.setOnClickListener(myOnClickListenter);

		mAuthAdapter = new AuthAdapter();
	}
	
	private OnClickListener myOnClickListenter=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button1:			
				 mAuthAdapter.setListener(requestOperateDelegate);
				 mAuthAdapter.loginAuth(111, "liaoyong", "liaoyong");
				break;
			case R.id.button2:
//				Intent intent = new Intent(TestActivity.this, MediaPlayActivity.class);
//				startActivity(intent);
				break;
			default :
				break;
			}
		}
	};
	
	private WXRequestAdapterInterface requestOperateDelegate = new WXRequestAdapterInterface() {

		@Override
		public void ZGWResponseBinaryDelegate(InputStream responseData,
				int resultCode, int tag) {;
				LogUtil.d("test:result:" + resultCode);
		}

		@Override
		public void ZGWResponseJsonDelegate(int resultCode, int tag,
				Object response) {
			LogUtil.d("test:result:" + resultCode);
		}

		@Override
		public void ZGWResponseProgressDelegate(int bytesWritten, int totalSize) {
		}

	};
}
