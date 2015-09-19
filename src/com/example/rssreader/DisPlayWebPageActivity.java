package com.example.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class DisPlayWebPageActivity extends Activity {
	WebView webview;
	TextView articletitle;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);

		Intent in = getIntent();
		final String page_url = in.getStringExtra("page_url");
		String article_title = in.getStringExtra("article_title");

		articletitle = (TextView) findViewById(R.id.articletitle);
		articletitle.setText(article_title);

		button = (Button) findViewById(R.id.openinbrower);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri url = Uri.parse(page_url);
				intent.setData(url);
				startActivity(intent);
			}
		});

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
