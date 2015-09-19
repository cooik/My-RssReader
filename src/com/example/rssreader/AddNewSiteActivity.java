package com.example.rssreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewSiteActivity extends Activity {
	Button btnSubmit;
	Button btnCancle;
	EditText txtUrl;
	TextView lblMessage;

	RSSParser rssParser = new RSSParser();
	RSSFeed rssFeed;

	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_site);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnCancle = (Button) findViewById(R.id.btnCancel);
		txtUrl = (EditText) findViewById(R.id.txtUrl);
		lblMessage = (TextView) findViewById(R.id.lblMessage);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = txtUrl.getText().toString();
				Log.i("AddNewSite", "AddNewSite---->url:" + url);
				if (url.length() > 0) {
					lblMessage.setText("");
					String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
					if (url.matches(urlPattern)) {
						new loadRSSFeed().execute(url);
					} else {
						lblMessage.setText("Please enter a valid url");
					}
				} else {
					lblMessage.setText("Please enter website url");
				}
			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	class loadRSSFeed extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(AddNewSiteActivity.this);
			pDialog.setMessage("Fetching RSS Infomation...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			if (pDialog.isShowing() == false) {
				pDialog.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			rssFeed = rssParser.getRSSFeed(url);
			
			if (rssFeed != null) {
				Log.e("AddNewSite",
						rssFeed.getTitle() + "" + rssFeed.getLink() + ""
								+ rssFeed.getDescription() + ""
								+ rssFeed.getLanguage());
				RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
						getApplicationContext());
				WebSite site = new WebSite(rssFeed.getTitle(),
						rssFeed.getLink(), rssFeed.getRSSLink(),
						rssFeed.getDescription());
				rssDb.addSite(site);
				Intent i = getIntent();
				setResult(100, i);
				finish();
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						lblMessage
								.setText("Rss url not found.Please check the url or try again ");

					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			// runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// if (rssFeed != null) {
			//
			// }
			// }
			// });
		}
	}
}
