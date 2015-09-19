package com.example.rssreader;

import java.util.List;

public class RSSFeed {
	String _title;
	String _description;
	String _link;
	String _rss_link;
	String _languaeg;
	List<RSSItem> _items;

	public RSSFeed(String _title, String _description, String _link,
			String _rss_link, String _languaeg) {
		super();
		this._title = _title;
		this._description = _description;
		this._link = _link;
		this._rss_link = _rss_link;
		this._languaeg = _languaeg;
	}

	public void setItems(List<RSSItem> items) {
		this._items = items;
	}

	public List<RSSItem> getItems() {
		return this._items;
	}

	public String getTitle() {
		return this._title;
	}

	public String getDescription() {
		return this._description;
	}

	public String getLink() {
		return this._link;
	}

	public String getRSSLink() {
		// TODO Auto-generated method stub
		return this._rss_link;
	}

	public String getLanguage() {
		return this._languaeg;
	}
}
