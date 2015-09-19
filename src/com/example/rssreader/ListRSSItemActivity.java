package com.example.rssreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.array;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListRSSItemActivity extends ListActivity {
	private ProgressDialog pDialog;

	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();

	RSSParser rssParser = new RSSParser();

	List<RSSItem> rssItems = new ArrayList<RSSItem>();

	RSSFeed rssFeed;

	private static String TAG_TITLE = "title";
	private static String TAG_LINK = "link";
	private static String TAG_DESRIPTION = "description";
	private static String TAG_PUB_DATE = "pubDate";
	private static String TAG_GUID = "guid"; // not used

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rss_item_list);
		Intent i = getIntent();

		Integer site_id = Integer.parseInt(i.getStringExtra("id"));

		RSSDatabaseHandler rssDB = new RSSDatabaseHandler(
				getApplicationContext());

		WebSite site = rssDB.getSite(site_id);
		String rss_link = site.getRSSLink();

		new loadRSSFeedItems().execute(rss_link);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(),
						DisPlayWebPageActivity.class);
				String page_url = ((TextView) view.findViewById(R.id.page_url))
						.getText().toString();
				Toast.makeText(getApplicationContext(), page_url,
						Toast.LENGTH_SHORT).show();
				in.putExtra("page_url", page_url);
				startActivity(in);
			}

		});
	}

	class loadRSSFeedItems extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ListRSSItemActivity.this);
			pDialog.setMessage("Loading recent articles...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String rss_url = params[0];

			rssItems = rssParser.getRSSFeedItems(rss_url);

			for (RSSItem item : rssItems) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_TITLE, item.getTitle());
				map.put(TAG_LINK, item.getLink());
				map.put(TAG_PUB_DATE, item.getPubdate());
				String description = item.getDescription();
				if (description.length() > 100) {
					description = description.substring(0, 97) + "..";
				}
				map.put(TAG_DESRIPTION, description);

				rssItemList.add(map);
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ListAdapter adapter = new SimpleAdapter(
							ListRSSItemActivity.this, rssItemList,
							R.layout.rss_item_list_row, new String[] {
									TAG_LINK, TAG_TITLE, TAG_PUB_DATE,
									TAG_DESRIPTION }, new int[] {
									R.id.page_url, R.id.title, R.id.pub_date,
									R.id.link });

					setListAdapter(adapter);
				}
			});

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}
}
