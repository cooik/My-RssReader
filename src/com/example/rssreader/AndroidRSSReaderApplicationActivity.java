package com.example.rssreader;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidRSSReaderApplicationActivity extends Activity {

	private ProgressDialog pDialog;

	ArrayList<HashMap<String, String>> rssFeedList;

	RSSParser rssParser = new RSSParser();

	RSSFeed rssFeed;

	Button btnAddSite;

	String[] sqliteIds;

	ListView lv;

	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.site_list);
		btnAddSite = (Button) findViewById(R.id.btnAddSite);

		rssFeedList = new ArrayList<HashMap<String, String>>();
		new loadStoreSites().execute();

		lv = (ListView) findViewById(R.id.list);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// getting values from selected ListItem
				String sqlite_id = ((TextView) view
						.findViewById(R.id.sqlite_id)).getText().toString();
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						ListRSSItemActivity.class);
				// passing sqlite row id
				in.putExtra(TAG_ID, sqlite_id);
				startActivity(in);
			}
		});

		btnAddSite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						AddNewSiteActivity.class);
				// starting new activity and expecting some response back
				// depending on the result will decide whether new website is
				// added to SQLite database or not
				startActivityForResult(i, 100);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.list) {
			menu.setHeaderTitle("Delete");
			menu.add(Menu.NONE, 0, 0, "Delete Feed");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		if (menuItemIndex == 0) {
			RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
					getApplicationContext());
			WebSite site = new WebSite();
			site.setId(Integer.parseInt(sqliteIds[info.position]));
			rssDb.deleteSite(site);
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		return true;
	}

	class loadStoreSites extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(
					AndroidRSSReaderApplicationActivity.this);
			pDialog.setMessage("Loading websites...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			if (pDialog.isShowing() == false) {
				pDialog.show();

			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
							getApplicationContext());
					List<WebSite> siteList = rssDb.getAllSite();
					if (siteList.size() > 0) {
						sqliteIds = new String[siteList.size()];
						for (int i = 0; i < siteList.size(); i++) {
							WebSite s = siteList.get(i);

							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_ID, s.getId().toString());
							map.put(TAG_TITLE, s.getTitle());
							map.put(TAG_LINK, s.getLink());

							rssFeedList.add(map);

							sqliteIds[i] = s.getId().toString();
						}
					}
					ListAdapter adapter = new SimpleAdapter(
							AndroidRSSReaderApplicationActivity.this,
							rssFeedList, R.layout.site_list_row, new String[] {
									TAG_ID, TAG_TITLE, TAG_LINK }, new int[] {
									R.id.sqlite_id, R.id.title, R.id.link });

					lv.setAdapter(adapter);

					registerForContextMenu(lv);

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
