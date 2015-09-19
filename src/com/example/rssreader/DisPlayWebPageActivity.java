package com.example.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisPlayWebPageActivity extends Activity {
	WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);

		Intent in = getIntent();
		String page_url = in.getStringExtra("page_url");

		webview = (WebView) findViewById(R.id.webpage);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(page_url);
		webview.setWebViewClient(new DisPlayWebPageActivityClient());

	}

	private class DisPlayWebPageActivityClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
	}
}
