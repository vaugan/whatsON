package com.nextgen.bemore;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebviewActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	WebView beMoreWebView;
	
//	Button btnBoxOffice, btnOMusic, btnKalahari;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
    
         
//        btnBoxOffice =(Button) findViewById(R.id.btnBoxOffice);
//        btnOMusic = (Button) findViewById(R.id.btnOMusic);
//        btnKalahari = (Button) findViewById(R.id.btnKalhari);
        
        // register button listeners
        
//        btnBoxOffice.setOnClickListener(this);
//        btnOMusic.setOnClickListener(this);
//        btnKalahari.setOnClickListener(this);
        
        String item_url ="http://www.kalahari.com/";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
          item_url = extras.getString("buy_item_url");
        }

        beMoreWebView =  (WebView) findViewById(R.id.webview);
        beMoreWebView.getSettings().setJavaScriptEnabled(true);
        beMoreWebView.loadUrl(item_url);   // to load the webview with default url;
//        btnBoxOffice.setFocusableInTouchMode(true);
        
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Uri uri=null;
		 Intent intent=null;
//		switch (v.getId()) {
//		case R.id.btnBoxOffice:
//				// launch boxoffice site
//			beMoreWebView.loadUrl("http://boxoffice.dstv.com/");
//			btnBoxOffice.setSoundEffectsEnabled(true);
//			btnBoxOffice.setFocusableInTouchMode(true);
//			btnKalahari.setFocusableInTouchMode(false);
//			btnOMusic.setFocusableInTouchMode(false);
//			
//			break;
//		case R.id.btnOMusic:
//				// launch OMusic site
//			btnKalahari.setFocusableInTouchMode(false);
//			btnBoxOffice.setFocusableInTouchMode(false);
//			btnOMusic.setFocusableInTouchMode(true);
//			beMoreWebView.loadUrl("http://omusic.dstv.com/");
//			break;
//		case R.id.btnKalhari:
//				// launch Kalahari site
//			btnBoxOffice.setFocusableInTouchMode(false);
//			btnOMusic.setFocusableInTouchMode(false);
//			btnKalahari.setFocusableInTouchMode(true);
//			beMoreWebView.loadUrl("http://www.kalahari.com/");
//			break;
//		
//		}
	
	beMoreWebView.setWebViewClient(new BeMoreWebviewClient());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && beMoreWebView.canGoBack()) {
	    	beMoreWebView.goBack();
	        return true;
	    }
	    
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	private class BeMoreWebviewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}  
}

